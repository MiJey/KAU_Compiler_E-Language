public enum TokenType {
	Identifier, IntLiteral, FloatLiteral, CharLiteral, StringLiteral,
    True, False, And, Or, Assign, Comma,
    Not, Equals, NotEqual, Less, LessEqual, Greater, GreaterEqual,
    Plus, Minus, Multiply, Divide,
    /*LeftBrace, RightBrace, */LeftBracket, RightBracket, LeftParen, RightParen,
    If, Else, While,
    Eof, Eol, Space, Tab, Err,

    // 타 언어에서는 함수로 지원하지만 E-Language에서는 기본적인 키워드로 제공하는 것들
    Print, Scan, Random, Time,
}