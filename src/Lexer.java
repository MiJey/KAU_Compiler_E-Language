import java.io.*;
import java.text.Normalizer;

public class Lexer {

    private boolean isEof = false;
    private char ch = ' '; 
    private String st = "\n\n";
    private BufferedReader input;
    private String line = "";
    private int lineno = 0;
    private int col = 0;
    private final String letters = "🍏🍌🥕💎🐘🖕👓🍔👁🍹🤴💋🌙📒🍊🍑👸🌈🐍🚕☂️✌️🌍🎅⛵️💤";
    private final String digits = "🕛🕐🕑🕒🕓🕔🕕🕖🕗🕘";
    private final char eolnCh = '\n';
    private final char eofCh = '\004';
    

    public Lexer (String fileName) { // source filename
        try {
            input = new BufferedReader (new FileReader(fileName));
        }
        catch (FileNotFoundException e) {
            System.out.println("File not found: " + fileName);
            System.exit(1);
        }
    }
    
    //nextChar() : col을 하나 더하고, 다음 문자를 리턴한다.
    private int nextUnicode() { // Return next char
        if (ch == eofCh)
            error("Attempt to read past end of file");
        col++;   
        if (col >= line.length()) {
            try {
                line = input.readLine( );
                if(line.codePointAt(0) == '﻿')//파일 처음에 읽히는 보이지 않는 문자 처리
                	line = line.substring(1, line.length()-1);
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
            } // if line
            col = 0;
        } // if col
        return line.codePointAt(col);
    }
    
    
    //nextemoji() : col과 st를 한 칸 밀고 밀기 전 이모지를 리턴한다
    private String nextEmoji() { // Return next emoji

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
    

    public Token next( ) { // Return next token
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
            		st = nextEmoji();
            		return Token.eol;
            		
            		default:
            			switch(st) {
                    	case "  "://tab tok
                    		st = nextEmoji();
                    		return Token.tabTok;
                    	case "📈":
                    		st = nextEmoji();
        	        		return Token.leftBracketTok;
                    	case "📉":
                    		st = nextEmoji();
        	        		return Token.rightBracketTok;
                    		
                    	case "💬":
                    		st = nextEmoji();
        	        		return Token.commaTok;
                    		
                    	case "👈":
                    		st = nextEmoji();
        	        		return Token.assignTok;
                    		
                    	case "🔄":
                    		st = nextEmoji();
        	        		return Token.whileTok;
                    		
                    	case "📺":
                    		st = nextEmoji();
        	        		return Token.printTok;
                    		
                    	case "➖":
                    		st = nextEmoji();
                    		return Token.minusTok;

                    	case "➕":
                    		st = nextEmoji();
        	        		return Token.plusTok;
                    		
                    	case "🧗":
                    		st = nextEmoji();
                    		return Token.gtTok;

                    	case "🏄":
                    		st = nextEmoji();
                    		return Token.ltTok;

                    	case "🤔":
                    		st = nextEmoji();
                    		return Token.ifTok;

                    	case "🙅":
                    		st = nextEmoji();
                    		return Token.falseTok;
                    	
                    	case "🙆":
                    		st = nextEmoji();
                    		return Token.trueTok;
                    	
                    	case "📖":
                    		st = nextEmoji();
                    		return Token.leftParenTok;
                    		
                    	case "📕":
                    		st = nextEmoji();
                    		return Token.rightParenTok;

                    	case "⚖️":
                    		st = nextEmoji();
                    		return Token.eqeqTok;

                    	case "🥜":
                    		st = nextEmoji();
                    		return Token.notTok;

                    	case "🐇":
                    		st = nextEmoji();
                    		return Token.multiplyTok;

                    	case "✂️":
                    		st = nextEmoji();
                    		return Token.divideTok;

                    	case "👫":
                    		st = nextEmoji();
                    		return Token.andTok;

                    	case "🤷":
                    		st = nextEmoji();
                    		return Token.orTok;

                    	case " "://띄어쓰기를 콤마로 사용.
//                    		st = nextEmoji();//col++하고 st 2글자만큼 읽어도 될거같은데 , 다음에는 문장 끝날 일이 없음.
                    		col++;
                    		st = line.substring(col,col+2);
                    		return Token.commaTok;
                    	default:
                    		st = nextEmoji();
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

    private void check(char c) {
        ch = nextUnicode();
        if (ch != c) 
            error("Illegal character, expecting " + c);
        ch = nextUnicode();
    }

    //lee add code
    private Token chkOpt(char c, Token one, Token two) {
    	ch = nextUnicode();
	    if(ch == c) {
	    	ch = nextUnicode();
	    	return two;
	    }
	    return one;
    }
        
        
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

    public void error (String msg) {
        System.err.print(line);
        System.err.println("Error: column " + col + " " + msg);
        System.exit(1);
    }
    
    //===========================
    private static void printIt(String string) {
        System.out.println(string);
        for (int i = 0; i < string.length(); i++) {
            System.out.print(String.format("U+%04X ", string.codePointAt(i)));
        }
        System.out.println();
    }
    public static void test() {
        String han = " ";
        printIt(han);

        String nfd = Normalizer.normalize(han, Normalizer.Form.NFD);
        printIt(nfd);

        String nfc = Normalizer.normalize(nfd, Normalizer.Form.NFC);
        printIt(nfc);
    }

    static public void main (String[] argv) {
    	String a = Emoji.a;
    	System.out.println(a);
//        Lexer lexer = new Lexer(argv[0]);
//        Token tok = lexer.next( );
//        while (tok != Token.eofTok) {
//            System.out.println(tok.toString());
//            tok = lexer.next( );
//        }
    }
    

}

