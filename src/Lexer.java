import java.io.*;
import java.text.Normalizer;

/**
 * 이모지로 이루어져 있는 UTF-8 텍스트 파일을 읽어 E-Language 토큰으로 변환해줍니다.
 * E-Language에서 알파벳에 해당되는 이모지를 읽기 위해 유니코드를 읽은 후 조합합니다.
 * 조합해서 나온 이모지에 해당하는 토큰을 반환합니다.
 * 
 * 1. 유니코드를 한 개 읽는다. (nextUnicode)
 * 2. 읽은 유니코드를 1~2개 조합하여 이모지로 변환한다. (nextEmoji)
 * 3. 이모지에 해당하는 토큰을 반환한다. (next)
 * 
 * @author Moon Yeji
 */
public class Lexer {
	private BufferedReader input;
	private EmojiHelper emojiHelper = new EmojiHelper();

	private final int NUL = 0x0000;	// 널
	private final int SP  = 0x0020;	// 공백
	private final int EOF = 0x0004;	// 파일 끝
	private final int TAB = 0x0009;	// 탭
	private final int LF  = 0x000A;	// 개행
	private final int BOM = 0xFEFF;	// 해당 텍스트가 유니코드임을 나타내는 문자이며, 옵션이므로 모든 유니코드 텍스트에서 나타나는 것은 아님
	
	private int unicodes[] = new int[] {};
	private int uni = NUL;
	private String line = "";
	private int lineno = 0;
	private int col = 1;

	public Lexer (String fileName) {
		try {
			input = new BufferedReader(new InputStreamReader(new FileInputStream(fileName), "UTF-8"));
		} catch (FileNotFoundException e) {
			System.out.println("Lexer: FileNotFoundException: " + fileName);
			System.exit(1);
		} catch (UnsupportedEncodingException e) {
			System.out.println("Lexer: UnsupportedEncodingException: " + e);
			System.exit(1);
		} catch (IOException e) {
			System.out.println("Lexer: IOException: " + e);
			System.exit(1);
		}
	}
    
	private int nextUnicode() {
    	if (uni == EOF)
    		error("Attempt to read past end of file");
    	
    	col++;
    	
    	if (col >= line.length()) {
    		try {
    			line = input.readLine();
    			
    			// 읽어온 텍스트 파일 맨 앞에 BOM(U+FEFF)이 있는 경우 잘라냄
    			if (line.codePointAt(0) == BOM)
    				line = line.substring(1, line.length());
    		} catch (IOException e) {
    			System.err.println("nextUnicode(): readLine error: " + e);
    			System.exit(1);
    		}
    		
    		if (line == null) {
    			line = "" + (char)LF;
    		} else {
    			//System.out.printf("line %3d: %s", lineno, line);
    			lineno++;
    			line += (char)LF;
    		}
    		
    		col = 0;
    	}
    	
    	uni = (int)line.charAt(col);
    	return uni;
    }
	
	private Emoji nextEmoji() {
System.out.printf("nextEmoji() unicodes: ");
		int temp[] = new int[unicodes.length + 1];
		
		for (int i = 0; i < unicodes.length; i++)
			temp[i] = unicodes[i];
		
		temp[unicodes.length] = nextUnicode();
		unicodes = temp;
		
for (int i = 0; i < unicodes.length; i++)
	System.out.printf("%04X ", unicodes[i]);
System.out.println();
		Emoji next = emojiHelper.findEmoji(unicodes);
		
		if (next == null) {
			error("nextEmoji: findEmoji returns null, Illegal character");
		} else if (next == Emoji.next) {
			nextEmoji();
		}
		
		System.out.println(next.toString());
		uni = NUL;
		unicodes = new int[] {};
		return next;
	}
	
	public Token next() {
		return Token.andTok;
	}
//    
//	private void check(int u) {
//        uni = nextUnicode();
//        if (uni != u) {
//        	error("Illegal character, expecting " + (char)uni + (char)u);
//        }
//            
//        uni = nextUnicode();
//    }

    public void error (String msg) {
        System.err.println(line);
        System.err.printf("Error(line %d, column %d): %s\n", lineno, col, msg);
        System.exit(1);
    }
    
    
    
	
	
	private boolean isEof = false;
	private char ch = ' ';
	private String st = "\n\n";
	private final String letters = "🍏🍌🥕💎🐘🖕👓🍔👁🍹🤴💋🌙📒🍊🍑👸🌈🐍🚕☂️✌️🌍🎅⛵️💤";
	private final String digits = "🕛🕐🕑🕒🕓🕔🕕🕖🕗🕘";
	private final char eolnCh = '\n';
	private final char eofCh = '\004';
    //nextemoji() : col과 st를 한 칸 밀고 밀기 전 이모지를 리턴한다
	private String nextEmoji2() { // Return next emoji

        if (st.contains(""+eofCh))
            error("Attempt to read past end of file");
    	if(st.equals("  "))//tab키는 길이가 4
    		col+=2;
    	if(line.length()==0) {
            try {
                line = input.readLine( );
                if(line.charAt(0) == '﻿')//파일 처음에 읽히는 보이지 않는 문자 처리
                	line = line.substring(1, line.length());
            } catch (IOException e) {
                System.err.println(e);
                System.exit(1);
            } // try
            if (line == null) // at end of file
                line = "" + eofCh;
            else {
                System.out.println("\nline " + lineno + "\t:\t" + line);
                lineno++;
                line += eolnCh;
                line += eolnCh;
                line += eolnCh;
            } // if line
            col = 0;
    		return line.substring(col,col+2);
    	}
        if (st.indexOf(eofCh) != -1)
            error("Attempt to read past end of file");
        else {
	        col = col+2;
	        if (line.charAt(col) == eolnCh) {
	            try {
	                line = input.readLine( );
	                if(line!=null)
	                	if(line.charAt(0) == '﻿')//파일 처음에 읽히는 보이지 않는 문자 처리//null이 아닐 때만 실행해야함.
	                		line = line.substring(1, line.length()-1);
	            } catch (IOException e) {
	                System.err.println(e);
	                System.exit(1);
	            } // try
	            if (line == null) // at end of file
	            	return eofCh+"";
//	                line = "" + eofCh;
	            	
	            else {
	                System.out.println("\nline " + lineno + "\t:\t" + line);
	                lineno++;
	                line += eolnCh;
	                line += eolnCh;
	                line += eolnCh;
	            } // if line
	            col = 0;
	        } // if col
	        
//	        col = col+2;
	        return line.substring(col,col+2);
        }
        return null;
    }
    

    public Token next22( ) { // Return next token
        do {
        	if(st.contains(""+eofCh))
        		return Token.eofTok;
            if (isLetter(st)) { // ident or keyword
                String spelling = concat(letters + digits);
                return Token.keyword(spelling);
            } else if (isDigit(st.charAt(0))) { // int or float literal
                String number = concat(digits);
                if (st.charAt(0) != '.')  // int Literal
                    return Token.mkIntLiteral(number);
                number += concat(digits);
                return Token.mkFloatLiteral(number);
            } else {            	
            	switch (st.charAt(0)) {
            
            		case '\t': case '\r':			//안보이는 글자들 처리
            			error("\t not allowed");
            		case eolnCh:
            		st = nextEmoji2();
            		return Token.eol;
            		
            		default:
            			switch(st) {
                    	case "  "://tab tok
                    		st = nextEmoji2();
                    		return Token.tabTok;
                    	case "📈":
                    		st = nextEmoji2();
        	        		return Token.leftBracketTok;
                    	case "📉":
                    		st = nextEmoji2();
        	        		return Token.rightBracketTok;
                    		
                    	case "💬":
                    		st = nextEmoji2();
        	        		return Token.commaTok;
                    		
                    	case "👈":
                    		st = nextEmoji2();
        	        		return Token.assignTok;
                    		
                    	case "🔄":
                    		st = nextEmoji2();
        	        		return Token.whileTok;
                    		
                    	case "📺":
                    		st = nextEmoji2();
        	        		return Token.printTok;
                    		
                    	case "➖":
                    		st = nextEmoji2();
                    		return Token.minusTok;

                    	case "➕":
                    		st = nextEmoji2();
        	        		return Token.plusTok;
                    		
                    	case "🧗":
                    		st = nextEmoji2();
                    		return Token.gtTok;

                    	case "🏄":
                    		st = nextEmoji2();
                    		return Token.ltTok;

                    	case "🤔":
                    		st = nextEmoji2();
                    		return Token.ifTok;

                    	case "🙅":
                    		st = nextEmoji2();
                    		return Token.falseTok;
                    	
                    	case "🙆":
                    		st = nextEmoji2();
                    		return Token.trueTok;
                    	
                    	case "📖":
                    		st = nextEmoji2();
                    		return Token.leftParenTok;
                    		
                    	case "📕":
                    		st = nextEmoji2();
                    		return Token.rightParenTok;

                    	case "⚖️":
                    		st = nextEmoji2();
                    		return Token.eqeqTok;

                    	case "🥜":
                    		st = nextEmoji2();
                    		return Token.notTok;

                    	case "🐇":
                    		st = nextEmoji2();
                    		return Token.multiplyTok;

                    	case "✂️":
                    		st = nextEmoji2();
                    		return Token.divideTok;

                    	case "👫":
                    		st = nextEmoji2();
                    		return Token.andTok;

                    	case "🤷":
                    		st = nextEmoji2();
                    		return Token.orTok;

                    	case " "://띄어쓰기를 콤마로 사용.
//                    		st = nextEmoji();//col++하고 st 2글자만큼 읽어도 될거같은데 , 다음에는 문장 끝날 일이 없음.
                    		col++;
                    		st = line.substring(col,col+2);
                    		return Token.commaTok;
                    	default:
                    		st = nextEmoji2();
                    		return Token.errTok;
                    	}
            			
            	}
            	
            }// switch
        } while (true);
    } // next


	private boolean isLetter(String s) {
        
    	if(letters.indexOf(s)==-1)
        {
    		
//    		System.out.println("#debug1🍏🍏");
//    		System.out.println(String.valueOf("🍏".length()));
//    		System.out.println(String.valueOf("a".length()));
//    		System.out.println("strin" + 'g');
//    		System.out.println(c);
        	
        	return false;
        }
        else
        	return true;
    }
  
	private boolean isDigit(char c) {
        return ('0' <= c && c <= '9');  // lee add code
    }


//
//    //lee add code
//	private Token chkOpt(char c, Token one, Token two) {
//    	ch = nextChar();
//	    if(ch == c) {
//	    	ch = nextChar();
//	    	return two;
//	    }
//	    return one;
//    }
        
        
// student exercise

//ch를 set에 있는 문자들로만 이루어진 토큰으로 자르고(set 에 없는 문자가 나올 때 까지) 그 토큰을 리턴.
    //emoji전용으로 수정.
	private String concat(String set) {//identifyer를 판별할 떄 버그 존재.
        String r = "";
        do {
        	if(letters.contains(st)) { //문자일때 예외처리 다음 글자가 숫자면 charAt함수로 한 글자를 읽고 아니라면 substring으로 두 글자를 읽어야 한다.
                r += st;
                col += 2;
                if(isDigit(line.charAt(col))) {	//다음 글자가 숫자면 charAt함수로 한 글자를 읽고 아니라면 substring으로 두 글자를 읽어야 한다.
    	            st = "" + line.charAt(col);
                }else if(letters.contains(line.substring(col,col+2))) {
                	st = line.substring(col,col+2);
                }else {            	
                	st = line.substring(col,col+2);
                }
        	} else {
	            r += st.charAt(0);
	            col++;
                if(isDigit(line.charAt(col)) || (line.charAt(col)==' ')) {//다음 글자가 숫자면 charAt함수로 한 글자를 읽고 아니라면 substring으로 두 글자를 읽어야 한다.
    	            st = "" + line.charAt(col);                	
                }else if(letters.contains(line.substring(col,col+2))) {                
                	st = line.substring(col,col+2);
                }else {
                	st = line.substring(col,col+2);
                }
        	}
        } while (set.contains(st));
        if(st.charAt(0) != ' ')//뭐떄문에 예외처리했더라
        	st = line.substring(col, col+2);
        return r;
    }
	

    
    //===========================
	private static void printIt(String string) {
        System.out.println(string);
        for (int i = 0; i < string.length(); i++) {
            System.out.print(String.format("U+%04X ", string.codePointAt(i)));
        }
        System.out.println();
    }
	
	// 유니코드 이해를 돕기 위한 테스트 코드
	public static void test() {
		System.out.printf("🍏 \uD83C\uDF4F %s %s \n", Emoji.a, ("" + (char)0xD83C + (char)0xDF4F));
				
		String str = Emoji.a + " " + Emoji.b + " " + Emoji.c + " " + Emoji.d;	// "🍏 🍌 🥕 💎"
		System.out.println(str);
		
		for (int i = 0; i < str.length(); i++)
			System.out.print(String.format("U+%04X ", str.codePointAt(i)));
		System.out.println();

		for (int i = 0; i < str.length(); i++)
			System.out.print(String.format("\\u%04X", (int)str.charAt(i)));
		System.out.println();
		
		for (int i = 0; i < str.length(); i++) {
			int c = (int)str.charAt(i);
			if (c == 0x0020)	// 공백 \u0020
				System.out.printf(" ");
			else
				System.out.print(String.format("\\u%04X", (int)str.charAt(i)));
		}
		
		System.out.println("\nend test");
	}

	static public void main (String[] argv) {
		//test();
		
		Lexer lexer = new Lexer(argv[0]);
		lexer.nextEmoji();
		//lexer.nextUnicode();
		
//      Token tok = lexer.next( );
//      while (tok != Token.eofTok) {
//          System.out.println(tok.toString());
//          tok = lexer.next( );
//      }
	}
}

