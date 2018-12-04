public class Token {

    private static final int KEYWORDS = TokenType.Eof.ordinal();

    private static final String[] reserved = new String[KEYWORDS];
    private static Token[] token = new Token[KEYWORDS];

    
    public static final Token eofTok = new Token(TokenType.Eof, "<<EOF>>");
    public static final Token elseTok = new Token(TokenType.Else, "\uD83D\uDE1E");	// 😞
    public static final Token falseTok = new Token(TokenType.False, "🙅");
    public static final Token ifTok = new Token(TokenType.If, "🤔");
    public static final Token trueTok = new Token(TokenType.True, "🙆");
    public static final Token whileTok = new Token(TokenType.While, "🔄");
    public static final Token leftBraceTok = new Token(TokenType.LeftBrace, "{");
    public static final Token rightBraceTok = new Token(TokenType.RightBrace, "}");
    public static final Token leftBracketTok = new Token(TokenType.LeftBracket, "📈");
    public static final Token rightBracketTok = new Token(TokenType.RightBracket, "📉");
    public static final Token leftParenTok = new Token(TokenType.LeftParen, "📖");
    public static final Token rightParenTok = new Token(TokenType.RightParen, "📕");
    public static final Token errTok = new Token(TokenType.Err, "!!");
    public static final Token commaTok = new Token(TokenType.Comma, "💬");
    public static final Token assignTok = new Token(TokenType.Assign, "👈");
    public static final Token eqeqTok = new Token(TokenType.Equals, "⚖️");
    public static final Token ltTok = new Token(TokenType.Less, "<");
    public static final Token lteqTok = new Token(TokenType.LessEqual, "<⚖");
    public static final Token gtTok = new Token(TokenType.Greater, "🏄");
    public static final Token gteqTok = new Token(TokenType.GreaterEqual, "🏄⚖");
    public static final Token notTok = new Token(TokenType.Not, "🥜");
    public static final Token noteqTok = new Token(TokenType.NotEqual, "🥜⚖");
    public static final Token plusTok = new Token(TokenType.Plus, "➕");
    public static final Token minusTok = new Token(TokenType.Minus, "➖");
    public static final Token multiplyTok = new Token(TokenType.Multiply, "🐇");
    public static final Token divideTok = new Token(TokenType.Divide, "✂️");
    public static final Token andTok = new Token(TokenType.And, "👫");
    public static final Token orTok = new Token(TokenType.Or, "🤷");
    //lee add code
    public static final Token spaceTok = new Token(TokenType.Space, "<<SP>>");
    public static final Token tabTok = new Token(TokenType.Tab, "<<TAB>>");
    public static final Token printTok = new Token(TokenType.Print, "📺");
    public static final Token eol = new Token(TokenType.Eol, "\\n");
    

    private TokenType type;
    private String value = "";

    private Token (TokenType t, String v) {//여기 뭐하는덴지 잘 모르겠음.
        type = t;
        value = v;
        if (t.compareTo(TokenType.Eof) < 0) {
            int ti = t.ordinal();
            reserved[ti] = v;
            token[ti] = this;
        }
    }

    public TokenType type( ) { return type; }

    public String value( ) { return value; }

    public static Token keyword  ( String name ) {
        char ch = name.charAt(0);
        if (ch >= 'A' && ch <= 'Z') return mkIdentTok(name); //첫 글자가 대문자인 키워드는 없음. 무조건 식벌자.
        for (int i = 0; i < KEYWORDS; i++)
           if (name.equals(reserved[i]))  return token[i]; //keyword일 때 해당 키워드 토큰을 리턴.
        return mkIdentTok(name);
    } // keyword

    public static Token mkIdentTok (String name) {
        return new Token(TokenType.Identifier, name);
    }

    public static Token mkIntLiteral (String name) {
        return new Token(TokenType.IntLiteral, name);
    }

    public static Token mkFloatLiteral (String name) {
        return new Token(TokenType.FloatLiteral, name);
    }

    public static Token mkCharLiteral (String name) {
        return new Token(TokenType.CharLiteral, name);
    }

    public String toString ( ) {
    	//lee delete code
//        if (type.compareTo(TokenType.Identifier) < 0) 
//        	return value;
        	
        return type + "\t" + value;
    } // toString

    public static void main (String[] args) {
        System.out.println(eofTok);
        System.out.println(whileTok);
    }
} // Token
