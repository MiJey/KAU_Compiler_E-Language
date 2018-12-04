
public class EmojiHelper {
	private static Emoji[] emojies = new Emoji[] {
			Emoji.eof, Emoji.space, Emoji.tab, Emoji.newline,
			
			Emoji.exclamation, Emoji.quotation, Emoji.hashtag, Emoji.dollar, Emoji.percent, Emoji.ampersand, Emoji.apostrophe,
			Emoji.leftparenthesis, Emoji.rightparenthesis, Emoji.asterisk, Emoji.plus, Emoji.comma, Emoji.hyphen, Emoji.period,
			Emoji.slash, Emoji.colon, Emoji.semicolon, Emoji.lessthan, Emoji.equal, Emoji.greaterthan, Emoji.question, Emoji.at,
			Emoji.leftbracket, Emoji.backslash, Emoji.rightbracket, Emoji.caret, Emoji.underscore, Emoji.accent,
			Emoji.leftbrace, Emoji.verticalbar, Emoji.rightbrace, Emoji.tilde,
			
			Emoji.one, Emoji.two, Emoji.three, Emoji.four, Emoji.five, Emoji.six, Emoji.seven, Emoji.eight, Emoji.nine, Emoji.ten,
			
			Emoji.a, Emoji.b, Emoji.c, Emoji.d, Emoji.e, Emoji.f, Emoji.g, Emoji.h, Emoji.i, Emoji.j, Emoji.k, Emoji.l, Emoji.m,
			Emoji.n, Emoji.o, Emoji.p, Emoji.q, Emoji.r, Emoji.s, Emoji.t, Emoji.u, Emoji.v, Emoji.w, Emoji.x, Emoji.y, Emoji.z,
			
			Emoji.A, Emoji.B, Emoji.C, Emoji.D, Emoji.E, Emoji.F, Emoji.G, Emoji.H, Emoji.I, Emoji.J, Emoji.K, Emoji.L, Emoji.M,
			Emoji.N, Emoji.O, Emoji.P, Emoji.Q, Emoji.R, Emoji.S, Emoji.T, Emoji.U, Emoji.V, Emoji.W, Emoji.X, Emoji.Y, Emoji.Z
	};
	
	private static Emoji[] letters = new Emoji[] {
			Emoji.a, Emoji.b, Emoji.c, Emoji.d, Emoji.e, Emoji.f, Emoji.g, Emoji.h, Emoji.i, Emoji.j, Emoji.k, Emoji.l, Emoji.m,
			Emoji.n, Emoji.o, Emoji.p, Emoji.q, Emoji.r, Emoji.s, Emoji.t, Emoji.u, Emoji.v, Emoji.w, Emoji.x, Emoji.y, Emoji.z,
			
			Emoji.A, Emoji.B, Emoji.C, Emoji.D, Emoji.E, Emoji.F, Emoji.G, Emoji.H, Emoji.I, Emoji.J, Emoji.K, Emoji.L, Emoji.M,
			Emoji.N, Emoji.O, Emoji.P, Emoji.Q, Emoji.R, Emoji.S, Emoji.T, Emoji.U, Emoji.V, Emoji.W, Emoji.X, Emoji.Y, Emoji.Z
	};
	
	private static Emoji[] digits = new Emoji[] {
			Emoji.one, Emoji.two, Emoji.three, Emoji.four, Emoji.five, Emoji.six, Emoji.seven, Emoji.eight, Emoji.nine, Emoji.ten
	};
	
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
}
