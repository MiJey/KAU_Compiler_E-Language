public enum TokenType {
	Bool, True, False, Char, Float, Int,
	Identifier, IntLiteral, FloatLiteral, CharLiteral,
	If, Else, While, Assign,
	LeftBrace, RightBrace, LeftBracket, RightBracket, LeftParen, RightParen,	// {} [] ()
	Not, Equals, NotEqual, Less, LessEqual, Greater, GreaterEqual,	// ! == != < <= > >=
	Plus, Minus, Multiply, Divide,	// + - * /
	And, Or,	// && ||
	Comma, Tab, Eol, Eof, Err,
	Print
	
	// Semicolon
}
