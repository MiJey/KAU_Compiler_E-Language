import java.util.*;

public class Parser {
	Token token;
	Lexer lexer;
	EmojiHelper emojiHelper = new EmojiHelper();
	
	public Parser(Lexer _lexer) {
		lexer = _lexer;
		token = lexer.next();
	}
	
	public String match(TokenType t) {
		String value = token.value();
		if (token.type().equals(t))
			token = lexer.next();
		else
			error(t);
		return value;
	}
	
	public int getDepth() {
		// 문장의 맨 앞에 있는 탭 갯수를 세어서 반환
		int d = 0;
		while (token.type().equals(TokenType.Tab)) {
			match(TokenType.Tab);
			d++;
		}
		return d;
	}
	
	private void error(TokenType tok) {
		System.err.println("Syntax error(TokenType): expecting: " + tok + "; saw: " + token);
		System.exit(1);
	}
	
	private void error(String tok) {
		System.err.println("Syntax error(String): expecting: " + tok + "; saw: " + token);
		System.exit(1);
	}
	
	//======================= Statements =========================

	// Program → Block
	public Program program() {
		Program prog = new Program(block());
		return prog;
	}

	// Block → { [TAB] Statement }
	private Block block() {
		Block b = new Block();
		
		while (isStatement())
			b.members.add(statement());
		
		return b;
	}
	
	// Statement → Block | Skip | Assignment | Function | IfStatement | WhileStatement
	private Statement statement() {
		// 문장 맨 앞의 탭의 갯수를 세어서 깊이를 확인
		int depth = getDepth();
		
		// 탭이 삭제된 상황에서 어떤 유형의 Statement인지 확인
		if (isSkip())           return skip();
		if (isAssignment())     return assignment();
		if (isFunction())       return function();
		if (isIfStatement())    return ifStatement();
		if (isWhileStatement()) return whileStatement();
		
		error("Invalid statement.");
		return null;
	}
	
	// Skip → NEWLINE
	private Skip skip() {
		token = lexer.next();
		return new Skip();
	}
	
	// Assignment → Identifier 👈 Expression NEWLINE
	private Assignment assignment() {
		Variable target = new Variable(match(TokenType.Identifier));
		match(TokenType.Assign);
		Expression source = expression();
		match(TokenType.Newline);
		return new Assignment(target, source);
	}
	
	// Function → ( 📺 | 🎹  | 🎲  | ⏰ ) Expression NEWLINE
	private Function function() {
		TokenType t = token.type();
	
		if (t.equals(TokenType.Print))
			match(TokenType.Print);
		else if (t.equals(TokenType.Scan))
			match(TokenType.Scan);
		else if (t.equals(TokenType.Random))
			match(TokenType.Random);
		else if (t.equals(TokenType.Time))
			match(TokenType.Time);
		else
			error("Invalid function.");
		
		Expression domain = expression();
		match(TokenType.Newline);
		
		return new Function(t, domain);
	}

	// IfStatement →
	// 🤔 Expression NEWLINE Block
	// [ 😞 NEWLINE Block ]
	private Conditional ifStatement() {
		match(TokenType.If);
		Expression e = expression();
		match(TokenType.Newline);
		Block b = block();
		
		if (token.type().equals(TokenType.Else)) {
			match(TokenType.Else);
			match(TokenType.Newline);
			return new Conditional(e, b, block());
		}
		return new Conditional(e, b);
	}
	
	// WhileStatement →
	// ♻️ Expression NEWLINE Block
	private Loop whileStatement() {
		match(TokenType.While);
		Expression e = expression();
		match(TokenType.Newline);
		Block b = block();
		
		return new Loop(e, b);
	}
	
	//------------------------------------------------------------
	
	private boolean isStatement() {
		if (token.type().equals(TokenType.Tab)) 
			return true;
		if (isSkip() || isAssignment() || isFunction() || isIfStatement() || isWhileStatement())
			return true;
		return false;
	}
	
	private boolean isSkip() {
		if (token.type().equals(TokenType.Newline))
			return true;
		return false;
	}
	
	private boolean isAssignment() {
		if (token.type().equals(TokenType.Identifier))
			return true;
		return false;
	}
	
	private boolean isFunction() {
		if (token.type().equals(TokenType.Print)
			|| token.type().equals(TokenType.Scan)
			|| token.type().equals(TokenType.Random)
			|| token.type().equals(TokenType.Time))
			return true;
		return false;
	}
	
	private boolean isIfStatement() {
		if (token.type().equals(TokenType.If))
			return true;
		return false;
	}
	
	private boolean isWhileStatement() {
		if (token.type().equals(TokenType.While))
			return true;
		return false;
	}
	
	//====================== Expressions =========================

	// Expression → Conjunction { 🤷 Conjunction }
	private Expression expression() {
		Expression term1 = conjunction();
		
		while (token.type().equals(TokenType.Or)) {
			Operator op = new Operator(match(token.type()));
			Expression term2 = conjunction();
			term1 = new Binary(op, term1, term2);
		}

		return term1;
	}
	
	// Conjunction → Equality { 👫 Equality }
	private Expression conjunction() {

		Expression term1 = equality();
		while (token.type().equals(TokenType.And)) {
			Operator op = new Operator(match(token.type()));
			Expression term2 = equality();
			term1 = new Binary(op, term1, term2);
		}

		return term1;
	}
	
	// Equality → Relation [ ( ⚖️ | 🥜⚖️ ) Relation ]
	private Expression equality() {

		Expression term1 = relation();
		while (token.type().equals(TokenType.Equals)
			|| token.type().equals(TokenType.NotEqual)) {
			Operator op = new Operator(match(token.type()));
			Expression term2 = relation();
			term1 = new Binary(op, term1, term2);
		}

		return term1;
	}
	
	// Relation → Addition [ ( 💁 | 💁⚖️ | 🙋 | 🙋⚖️ ) Addition ]
	private Expression relation() {

		Expression term1 = addition();
		while (token.type().equals(TokenType.Less)
			|| token.type().equals(TokenType.LessEqual)
			|| token.type().equals(TokenType.Greater)
			|| token.type().equals(TokenType.GreaterEqual)) {
			Operator op = new Operator(match(token.type()));
			Expression term2 = addition();
			term1 = new Binary(op, term1, term2);
		}

		return term1;
	}
	
	// Addition → Term { ( ➕ | ➖ ) Term }
	private Expression addition() {
		Expression term1 = term();
		while (token.type().equals(TokenType.Plus)
			|| token.type().equals(TokenType.Minus)) {
			Operator op = new Operator(match(token.type()));
			Expression term2 = term();
			term1 = new Binary(op, term1, term2);
		}
		return term1;
	}
	
	// Term → Factor { ( 🐇 | ✂️ ) Factor }
	private Expression term() {
		Expression term1 = factor();
		while (token.type().equals(TokenType.Multiply)
			|| token.type().equals(TokenType.Divide)) {
			Operator op = new Operator(match(token.type()));
			Expression term2 = factor();
			term1 = new Binary(op, term1, term2);
		}
		return term1;
	}
	
	// Factor → [ ➖ | 🥜 ] Primary
	private Expression factor() {
		if (token.type().equals(TokenType.Minus)
		 || token.type().equals(TokenType.Not)) {
			Operator op = new Operator(match(token.type()));
			Expression term = primary();
			return new Unary(op, term);
		}
		return primary();
	}
	
	// Primary → Identifier | Literal | 📖Expression📕
	private Expression primary() {
		Expression e = null;
		
		if (token.type().equals(TokenType.Identifier)) {
			e = new Variable(match(TokenType.Identifier));
		} else if (isLiteral()) {
			e = literal();
		} else if (token.type().equals(TokenType.LeftParen)) {
			match(TokenType.LeftParen);
			e = expression();
			match(TokenType.RightParen);
		} else {
			error("primary(Identifier, Literal, LeftParen)");
		}

		return e;
	}
	
	//===================== Lexical Level ========================
	
	private Value literal() {
		Value v = null;
		
		if (token.type().equals(TokenType.IntLiteral))
			v = new IntValue(Integer.parseInt(token.value()));
		else if (token.type().equals(TokenType.True))
			v = new BoolValue(true);
		else if (token.type().equals(TokenType.False))
			v = new BoolValue(false);
		else if (token.type().equals(TokenType.CharLiteral))
			v = new CharValue(token.value().charAt(0));
		else if (token.type().equals(TokenType.FloatLiteral))
			v = new FloatValue(Float.parseFloat(token.value()));
		else if (token.type().equals(TokenType.StringLiteral))
			v = new StringValue(token.value());
		else
			error("literal(Int, Bool, Char, Float, String)");
		
		token = lexer.next();
		return v;
	}
	
	private boolean isLiteral() {
		return token.type().equals(TokenType.IntLiteral)
			|| token.type().equals(TokenType.True)
			|| token.type().equals(TokenType.False)
	        || token.type().equals(TokenType.FloatLiteral)
	        || token.type().equals(TokenType.CharLiteral)
	        || token.type().equals(TokenType.StringLiteral);
	}
	
	//========================== Main ============================
	
	public static void main(String args[]) {
		Parser parser = new Parser(new Lexer(args[0]));
		Program program = parser.program();
		program.display();
	}
}
