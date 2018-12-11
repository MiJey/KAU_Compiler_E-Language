public enum TokenType {
	Eof, Space, Tab, Newline,
	
	Assign, If, Else, While, Break, Continue,
	Or, And, Not, Equals, NotEqual, Less, LessEqual, Greater, GreaterEqual,
	Identifier, IntLiteral, FloatLiteral, CharLiteral, StringLiteral,
	True, False,
	Plus, Minus, Multiply, Divide, Remainder,
	
    LeftBracket, RightBracket, LeftParen, RightParen,
    
    // 타 언어에서는 함수로 지원하지만 E-Language에서는 기본적인 키워드로 제공하는 것들
	Print, Input, Random, Time, 
	
	// Input에서 쓰이는 타입
	IntType, FloatType
}