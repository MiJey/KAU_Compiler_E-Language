public class Token {

	private static final int KEYWORDS = TokenType.Eof.ordinal();
	private static final String[] reserved = new String[KEYWORDS];
	// private static Token[] tokens = new Token[KEYWORDS];
	
	public static final Token eofTok          = new Token(TokenType.Eof,          Emoji.eof.toString());
	public static final Token spaceTok        = new Token(TokenType.Space,        Emoji.space.toString());
	public static final Token tabTok          = new Token(TokenType.Tab,          Emoji.tab.toString());
	public static final Token newlineTok      = new Token(TokenType.Newline,      Emoji.newline.toString());
	
	public static final Token assignTok       = new Token(TokenType.Assign,       Emoji.assignEmoji.toString());
	public static final Token ifTok           = new Token(TokenType.If,           Emoji.ifEmoji.toString());
	public static final Token elseTok         = new Token(TokenType.Else,         Emoji.elseEmoji.toString());
	public static final Token whileTok        = new Token(TokenType.While,        Emoji.whileEmoji.toString());

	public static final Token orTok           = new Token(TokenType.Or,           Emoji.orEmoji.toString());
	public static final Token andTok          = new Token(TokenType.And,          Emoji.andEmoji.toString());
	public static final Token notTok          = new Token(TokenType.Not,          Emoji.notEmoji.toString());
	public static final Token equalsTok       = new Token(TokenType.Equals,       Emoji.equalsEmoji.toString());
	public static final Token notEqualTok     = new Token(TokenType.NotEqual,     Emoji.notEmoji.toString() + Emoji.equalsEmoji.toString());
	public static final Token lessTok         = new Token(TokenType.Less,         Emoji.lessEmoji.toString());
	public static final Token lessEqualTok    = new Token(TokenType.LessEqual,    Emoji.lessEmoji.toString() + Emoji.equalsEmoji.toString());
	public static final Token greaterTok      = new Token(TokenType.Greater,      Emoji.greaterEmoji.toString());
	public static final Token greaterEqualTok = new Token(TokenType.GreaterEqual, Emoji.greaterEmoji.toString() + Emoji.equalsEmoji.toString());
	
	public static final Token trueTok         = new Token(TokenType.True,         Emoji.trueEmoji.toString());
	public static final Token falseTok        = new Token(TokenType.False,        Emoji.falseEmoji.toString());
	
	public static final Token plusTok         = new Token(TokenType.Plus,         Emoji.plusEmoji.toString());
	public static final Token minusTok        = new Token(TokenType.Minus,        Emoji.minusEmoji.toString());
	public static final Token multiplyTok     = new Token(TokenType.Multiply,     Emoji.multiflyEmoji.toString());
	public static final Token divideTok       = new Token(TokenType.Divide,       Emoji.divideEmoji.toString());
	
	public static final Token leftBracketTok  = new Token(TokenType.LeftBracket,  Emoji.leftbracketEmoji.toString());
	public static final Token rightBracketTok = new Token(TokenType.RightBracket, Emoji.rightbracketEmoji.toString());
	public static final Token leftParenTok    = new Token(TokenType.LeftParen,    Emoji.leftparenEmoji.toString());
	public static final Token rightParenTok   = new Token(TokenType.RightParen,   Emoji.rightparenEmoji.toString());
    
	public static final Token printTok        = new Token(TokenType.Print,        Emoji.printEmoji.toString());
	public static final Token scanTok         = new Token(TokenType.Scan,         Emoji.scanEmoji.toString());
	public static final Token randomTok       = new Token(TokenType.Random,       Emoji.randomEmoji.toString());
	public static final Token timeTok         = new Token(TokenType.Time,         Emoji.timeEmoji.toString());

	private TokenType type;
	private String value = "";

	private Token(TokenType t, String v) {
		type = t;
		value = v;
		if (t.compareTo(TokenType.Eof) < 0) {
			int ti = t.ordinal();
			reserved[ti] = v;
			// tokens[ti] = this;
		}
	}

	public TokenType type() { return type; }
	public String value() { return value; }

	public static Token mkIdentTok(String name) {
		return new Token(TokenType.Identifier, name);
	}

	public static Token mkIntLiteral(String number) {
		return new Token(TokenType.IntLiteral, number);
	}
	
	public static Token mkFloatLiteral(String number) {
		return new Token(TokenType.FloatLiteral, number);
	}
	
	public static Token mkCharLiteral(String ch) {
		return new Token(TokenType.CharLiteral, ch);
	}
	
	public static Token mkStringLiteral(String str) {
		return new Token (TokenType.StringLiteral, str);
	}
	
	public String toString() {
		return String.format("\t%-15s\t%s", type, value);
	}
}
