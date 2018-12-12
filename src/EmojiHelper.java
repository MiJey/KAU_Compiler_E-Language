
public class EmojiHelper {
	// 이모지 전체(next를 제외한 특수 이모지 포함)
	private static Emoji[] emojies = new Emoji[] {
		// 공백
		Emoji.eof, Emoji.space, Emoji.tab, Emoji.newline,
		
		// 키워드
		Emoji.assignEmoji, Emoji.ifEmoji, Emoji.elseEmoji, Emoji.whileEmoji, Emoji.breakEmoji, Emoji.continueEmoji,
		Emoji.orEmoji, Emoji.andEmoji, Emoji.notEmoji, Emoji.equalsEmoji, Emoji.lessEmoji, Emoji.greaterEmoji,
		Emoji.charEmoji, Emoji.stringEmoji, Emoji.trueEmoji, Emoji.falseEmoji,
		Emoji.plusEmoji, Emoji.minusEmoji, Emoji.multiflyEmoji, Emoji.divideEmoji, Emoji.remainderEmoji,
		Emoji.printEmoji, Emoji.inputEmoji, Emoji.randomEmoji, Emoji.timeEmoji,
		Emoji.intTypeEmoji, Emoji.floatTypeEmoji,
		
		// 아스키
		Emoji.exclamation, Emoji.quotation, Emoji.hashtag, Emoji.dollar, Emoji.percent, Emoji.ampersand, Emoji.apostrophe,
		Emoji.leftparenEmoji, Emoji.rightparenEmoji, Emoji.asterisk, Emoji.plus, Emoji.comma, Emoji.hyphen, Emoji.periodEmoji,
		Emoji.slash, Emoji.colon, Emoji.semicolon, Emoji.lessthan, Emoji.equal, Emoji.greaterthan, Emoji.question, Emoji.at,
		Emoji.leftbracketEmoji, Emoji.backslash, Emoji.rightbracketEmoji, Emoji.caret, Emoji.underscore, Emoji.accent,
		Emoji.leftbrace, Emoji.verticalbar, Emoji.rightbrace, Emoji.tilde,
		
		Emoji.zero, Emoji.one, Emoji.two, Emoji.three, Emoji.four, Emoji.five, Emoji.six, Emoji.seven, Emoji.eight, Emoji.nine,
		
		Emoji.a, Emoji.b, Emoji.c, Emoji.d, Emoji.e, Emoji.f, Emoji.g, Emoji.h, Emoji.i, Emoji.j, Emoji.k, Emoji.l, Emoji.m,
		Emoji.n, Emoji.o, Emoji.p, Emoji.q, Emoji.r, Emoji.s, Emoji.t, Emoji.u, Emoji.v, Emoji.w, Emoji.x, Emoji.y, Emoji.z,
		
		Emoji.A, Emoji.B, Emoji.C, Emoji.D, Emoji.E, Emoji.F, Emoji.G, Emoji.H, Emoji.I, Emoji.J, Emoji.K, Emoji.L, Emoji.M,
		Emoji.N, Emoji.O, Emoji.P, Emoji.Q, Emoji.R, Emoji.S, Emoji.T, Emoji.U, Emoji.V, Emoji.W, Emoji.X, Emoji.Y, Emoji.Z
	};
	
	// 아스키 출력 문자 중 특수 기호만
	private static Emoji[] asciiSymbols = new Emoji[] {
		Emoji.exclamation, Emoji.quotation, Emoji.hashtag, Emoji.dollar, Emoji.percent, Emoji.ampersand, Emoji.apostrophe,
		Emoji.leftparenEmoji, Emoji.rightparenEmoji, Emoji.asterisk, Emoji.plus, Emoji.comma, Emoji.hyphen, Emoji.periodEmoji,
		Emoji.slash, Emoji.colon, Emoji.semicolon, Emoji.lessthan, Emoji.equal, Emoji.greaterthan, Emoji.question, Emoji.at,
		Emoji.leftbracketEmoji, Emoji.backslash, Emoji.rightbracketEmoji, Emoji.caret, Emoji.underscore, Emoji.accent,
		Emoji.leftbrace, Emoji.verticalbar, Emoji.rightbrace, Emoji.tilde
	};
	
	// 아스키 출력 문자 중 알파벳만
	private static Emoji[] letters = new Emoji[] {
		Emoji.a, Emoji.b, Emoji.c, Emoji.d, Emoji.e, Emoji.f, Emoji.g, Emoji.h, Emoji.i, Emoji.j, Emoji.k, Emoji.l, Emoji.m,
		Emoji.n, Emoji.o, Emoji.p, Emoji.q, Emoji.r, Emoji.s, Emoji.t, Emoji.u, Emoji.v, Emoji.w, Emoji.x, Emoji.y, Emoji.z,
		
		Emoji.A, Emoji.B, Emoji.C, Emoji.D, Emoji.E, Emoji.F, Emoji.G, Emoji.H, Emoji.I, Emoji.J, Emoji.K, Emoji.L, Emoji.M,
		Emoji.N, Emoji.O, Emoji.P, Emoji.Q, Emoji.R, Emoji.S, Emoji.T, Emoji.U, Emoji.V, Emoji.W, Emoji.X, Emoji.Y, Emoji.Z
	};
	
	// 아스키 출력 문자 중 숫자만
	private static Emoji[] digits = new Emoji[] {
		Emoji.zero, Emoji.one, Emoji.two, Emoji.three, Emoji.four, Emoji.five, Emoji.six, Emoji.seven, Emoji.eight, Emoji.nine
	};
	
	// 위에 있는 아스키 출력 문자 Emoji 배열과 같은 순서인 문자열
	private static String ascii_symbols = "!\"#$%&'()*+,-./:;<=>?@[\\]^_`{|}~";
	private static String ascii_letters = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private static String ascii_digits = "0123456789";
	
	public EmojiHelper() {}
	
	// 주어진 배열과 일치하는 이모지가 있는지 확인
	// 일치하는 경우: 해당 이모지 반환
	// 하나 이상 일치하는 경우: Emoji.next 반환
	// 하나도 일치하지 않는 경우: null 반환(받는 쪽에서 Illegal character 에러 처리 해줘야 함)
	public Emoji findEmoji(int unicodes[]) {
		int maxOverlap = 0;
		
		for (int i = 0; i < emojies.length; i++) {
			int overlap = 0;
			for (int j = 0; j < unicodes.length; j++) {
				if (unicodes[j] != emojies[i].unicodes[j])
					break;
				
				overlap++;
			}
			
			if (overlap == emojies[i].unicodes.length)
				return emojies[i];	// 해당하는 이모지 반환
			
			if (overlap != 0)
				maxOverlap = overlap;
		}
		
		if (maxOverlap != 0)
			return Emoji.next;	// 정확히 해당되는 이모지는 없으나 앞부분이 일치하는 경우가 있으니 다음 유니코드를 읽으라는 의미
		
		System.out.println("");
		return null;	// 이모지일 가능성이 없을 때(Illegal character)
	}
	
	// 문자인지 확인
	public boolean isLetter(Emoji e) {
		for (int i = 0; i < letters.length; i++)
			if (letters[i] == e)
				return true;
		
		return false;
	}
	
	// 숫자인지 확인
	public boolean isDigit(Emoji e) {
		for (int i = 0; i < digits.length; i++)
			if (digits[i] == e)
				return true;
		
		return false;
	}
	
	// 아스키 출력 문자인지 확인
	public boolean isAscii(Emoji e) {
		for (int i = 0; i < asciiSymbols.length; i++)
			if (asciiSymbols[i] == e) return true;
		
		for (int i = 0; i < letters.length; i++)
			if (letters[i] == e) return true;
		
		for (int i = 0; i < digits.length; i++)
			if (digits[i] == e) return true;
		
		return false;
	}
	
	public void printAllEmojies() {
		for (int i = 0; i < emojies.length; i++) {
			System.out.println(emojies[i].toString());
		}
	}
	
	public String parseString(String emojiString) {
		String result = "";
		while (emojiString.length() > 0) {
			for (int i = 0; i < asciiSymbols.length; i++) {
				if (asciiSymbols[i].toString().equals(getFirstEmoji(emojiString))) {
					result += ascii_symbols.substring(i, i+1);
					emojiString = emojiString.substring(asciiSymbols[i].toString().length());
				}
			}
			
			for (int i = 0; i < letters.length; i++) {
				if (letters[i].toString().equals(getFirstEmoji(emojiString))) {
					result += ascii_letters.substring(i, i+1);
					emojiString = emojiString.substring(letters[i].toString().length());
				}
			}

			for (int i = 0; i < digits.length; i++) {
				if (digits[i].toString().equals(getFirstEmoji(emojiString))) {
					result += ascii_digits.substring(i, i+1);
					emojiString = emojiString.substring(digits[i].toString().length());
				}
			}
		}

		return result;
	}
	
	private String getFirstEmoji(String emojiString) {
		int unicodes[] = new int[] {};
		
		for (int i = 0; i < emojiString.length(); i++) {
			// 배열 크기를 하나 늘려서 복사
			int temp[] = new int[unicodes.length + 1];
			for (int j = 0; j < unicodes.length; j++)
				temp[j] = unicodes[j];
			
			temp[unicodes.length] = emojiString.charAt(i);
			unicodes = temp;
			
			Emoji next = findEmoji(unicodes);
			
			if (next == null)
				return "Something wrong!";
			else if (next == Emoji.next)
				continue;
			else
				return next.toString();
		}

		return "Something wrong!";
	}
}
