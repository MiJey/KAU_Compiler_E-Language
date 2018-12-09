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
	private int uni = 0;
	private Emoji emo = Emoji.next;	// 초기값으로 다음 문자를 읽으라는 의미의 특수 이모지
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
				if (line != null && line.length() != 0 && line.charAt(0) == BOM)
					line = line.substring(1, line.length());
    		} catch (IOException e) {
    			System.err.println("nextUnicode(): readLine error: " + e);
    			System.exit(1);
    		}
    		
    		if (line == null) {
    			line = "" + (char)EOF;
    		} else {
    			lineno++;
    			line += (char)LF;
    			System.out.printf("line %2d: %s", lineno, line);
    		}
    		
    		col = 0;
    	}

    	uni = (int)line.charAt(col);
    	// System.out.printf("next Unicode: U+%04X\n", uni);
    	return uni;
    }
	
	private Emoji nextEmoji() {
		// 배열 크기를 하나 늘려서 복사
		int temp[] = new int[unicodes.length + 1];
		for (int i = 0; i < unicodes.length; i++)
			temp[i] = unicodes[i];
		temp[unicodes.length] = nextUnicode();
		unicodes = temp;
		
		Emoji next = emojiHelper.findEmoji(unicodes);
		
		if (next == null) {
			error("nextEmoji: findEmoji returns null, Illegal character");
		} else if (next == Emoji.next) {
			nextEmoji();
			return null;
		}
		
		// System.out.println("next Emoji: " + next.toString());
		uni = 0;
		unicodes = new int[] {};
		emo = next;
		return emo;
	}

	// 연속되는 문자열 찾기(Identifier)
	public String concatLetters() {
		String r = "";
		do {
			r += emo;
			nextEmoji();
		} while (emojiHelper.isLetter(emo));
		return r;
	}
	
	// 연속되는 숫자 찾기(IntLiteral, FloatLiteral)
	public String concatDigits() {
		String r = "";
		do {
			r += emo;
			nextEmoji();
		} while (emojiHelper.isDigit(emo));
		return r;
	}
	
	public String concatString() {
		String r = "";
		nextEmoji();	// get "
		while (emo != Emoji.stringEmoji) {
			r += emo;
			nextEmoji();
		}
		nextEmoji();	// get "
		return r;
	}
	
	// 공백 4개는 탭으로 전환, 4개 미만의 공백은 1개로 침
	public Emoji concatSpaces() {
		int cnt = 0;
		do {
			cnt++;
			nextEmoji();
		} while (emo == Emoji.space && cnt < 4);
		
		if (cnt == 4)
			return Emoji.tab;
		return Emoji.space;
	}
	
	public Token next() {
		while (true) {
			if (emo == Emoji.next) {
				nextEmoji();
				continue;
			}
			
			if (emo == Emoji.newline) {
				// System.out.println(Token.newlineTok.toString());
				nextEmoji();
				return Token.newlineTok;
			}
			
			if (emo == Emoji.eof)
				return Token.eofTok;
			
			// E-Language에서는 공백이나 탭도 중요한 문자이기 때문에 토큰으로 처리함
			if (emo == Emoji.space) {
				Emoji ws = concatSpaces();
				if (ws == Emoji.space)
					return Token.spaceTok;
				else if (ws == Emoji.tab)
					return Token.tabTok;
			} else if (emo == Emoji.tab) {
				nextEmoji();
				return Token.tabTok;
			}
			
			// Keywords
			if (emo == Emoji.assignEmoji) {
				nextEmoji();
				return Token.assignTok;
			} else if (emo == Emoji.ifEmoji) {
				nextEmoji();
				return Token.ifTok;
			} else if (emo == Emoji.elseEmoji) {
				nextEmoji();
				return Token.elseTok;
			} else if (emo == Emoji.whileEmoji) {
				nextEmoji();
				return Token.whileTok;
			}
			
			// 논리연산
			if (emo == Emoji.orEmoji) {
				nextEmoji();
				return Token.orTok;
			} else if (emo == Emoji.andEmoji) {
				nextEmoji();
				return Token.andTok;
			} else if (emo == Emoji.notEmoji) {
				nextEmoji();
				if (emo == Emoji.equalsEmoji) {
					nextEmoji();
					return Token.notEqualTok;
				}
				return Token.notTok;
			} else if (emo == Emoji.equalsEmoji) {
				nextEmoji();
				return Token.equalsTok;
			} else if (emo == Emoji.lessEmoji) {	// 💁 <
				nextEmoji();
				if (emo == Emoji.equalsEmoji) {
					nextEmoji();
					return Token.lessEqualTok;
				}	
				return Token.lessTok;
			} else if (emo == Emoji.greaterEmoji) { // 🙋 >
				nextEmoji();
				if (emo == Emoji.equalsEmoji) {
					nextEmoji();
					return Token.greaterEqualTok;
				}
				return Token.greaterTok;
			}
			
			// true, false
			if (emo == Emoji.trueEmoji) {
				nextEmoji();
				return Token.trueTok;
			} else if (emo == Emoji.falseEmoji) {
				nextEmoji();
				return Token.falseTok;
			}
			
			// 사칙연산
			if (emo == Emoji.plusEmoji) {
				nextEmoji();
				return Token.plusTok;
			} else if (emo == Emoji.minusEmoji) {
				nextEmoji();
				return Token.minusTok;
			} else if (emo == Emoji.multiflyEmoji) {
				nextEmoji();
				return Token.multiplyTok;
			} else if (emo == Emoji.divideEmoji) {
				nextEmoji();
				return Token.divideTok;
			}

			// 괄호
			else if (emo == Emoji.leftparenEmoji) {
				nextEmoji();
				return Token.leftParenTok;
			} else if (emo == Emoji.rightparenEmoji) {
				nextEmoji();
				return Token.rightParenTok;
			} else if (emo == Emoji.leftbracketEmoji) {
				nextEmoji();
				return Token.leftBracketTok;
			} else if (emo == Emoji.rightbracketEmoji) {
				nextEmoji();
				return Token.rightBracketTok;
			}
			
			// 함수
			if (emo == Emoji.printEmoji) {
				nextEmoji();
				return Token.printTok;
			} else if (emo == Emoji.scanEmoji) {
				nextEmoji();
				return Token.scanTok;
			} else if (emo == Emoji.randomEmoji) {
				nextEmoji();
				return Token.randomTok;
			} else if (emo == Emoji.timeEmoji) {
				nextEmoji();
				return Token.timeTok;
			}
			
			// String
			if (emo == Emoji.stringEmoji)
				return Token.mkStringLiteral(concatString());
			
			// Char
			if (emo == Emoji.charEmoji) {
				nextEmoji();	// get '
				String temp;
				if (emojiHelper.isAscii(emo)) {
					temp = emo.toString();	// get char
					nextEmoji();	// get '
					if (emo == Emoji.charEmoji) {
						nextEmoji();
						return Token.mkCharLiteral(temp);
					}
				}
			}
			
			// Identifier
			if (emojiHelper.isLetter(emo))
				return Token.mkIdentTok(concatLetters());
			
			// Digits
			if (emojiHelper.isDigit(emo)) {
				String number = concatDigits();
				
				// Integer
				if (emo != Emoji.periodEmoji)
					return Token.mkIntLiteral(number);
				
				// Float
				number += concatDigits();
				return Token.mkFloatLiteral(number);
			}

			error("next() Illigal character: " + emo.toString());
		}
	}

    public void error (String msg) {
        System.err.println(line);
        System.err.printf("Error(line %d, column %d): %s\n", lineno, col, msg);
        System.exit(1);
    }
    
	// 유니코드 이해를 돕기 위한 테스트 코드
	public static void test() {
		System.out.println("======================Test======================");
		System.out.printf("🍏 \uD83C\uDF4F %s %s \n", Emoji.a, "" + (char)0xD83C + (char)0xDF4F);
				
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

		System.out.println("\n================================================");
	}

	static public void main (String[] argv) {
		// test();
		Lexer lexer = new Lexer(argv[0]);
		Token tok = lexer.next( );
		
		while (tok != Token.eofTok) {
			System.out.println(tok.toString());
			tok = lexer.next();
		}
		
		System.out.println("Finish");
	}
}

