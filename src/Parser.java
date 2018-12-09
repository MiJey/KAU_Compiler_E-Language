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
		Program prog = new Program(block(0));
		return prog;
	}

	// Block → { [TAB] Statement }
	private Block block(int depth) {
		Block b = new Block(depth);
		
		while (isStatement()) {
			// 현재 블록의 깊이보다 읽은 탭의 갯수가 적을 경우 블록에서 빠져나감
			if (depth > getDepth())
				break;
			b.members.add(statement(depth));
		}
			
		return b;
	}
	
	// Statement → Block | Skip | Assignment | Function | IfStatement | WhileStatement
	private Statement statement(int depth) {
		if (isSkip())           return skip(depth);
		if (isAssignment())     return assignment(depth);
		if (isFunction())       return function(depth);
		if (isIfStatement())    return ifStatement(depth);
		if (isWhileStatement()) return whileStatement(depth);
		
		error("Invalid statement.");
		return null;
	}
	
	// Skip → NEWLINE
	private Skip skip(int depth) {
		token = lexer.next();
		return new Skip(depth);
	}
	
	// Assignment → Identifier 👈 Expression NEWLINE
	private Assignment assignment(int depth) {
		Variable target = new Variable(match(TokenType.Identifier), null, null);
		match(TokenType.Assign);
		Expression source = expression();
		match(TokenType.Newline);
		return new Assignment(depth, target, source);
	}
	
	// Function → ( 📺 | 🎹  | 🎲  | ⏰ ) Expression NEWLINE
	private Function function(int depth) {
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
		
		return new Function(depth, t, domain);
	}

	// IfStatement →
	// 🤔 Expression NEWLINE Block
	// [ 😞 NEWLINE Block ]
	private Conditional ifStatement(int depth) {
		match(TokenType.If);
		Expression e = expression();
		match(TokenType.Newline);
		Block b = block(depth + 1);
		
		if (token.type().equals(TokenType.Else)) {
			match(TokenType.Else);
			match(TokenType.Newline);
			return new Conditional(depth, e, b, block(depth + 1));
		}
		return new Conditional(depth, e, b);
	}
	
	// WhileStatement →
	// ♻️ Expression NEWLINE Block
	private Loop whileStatement(int depth) {
		match(TokenType.While);
		Expression e = expression();
		match(TokenType.Newline);
		Block b = block(depth + 1);
		
		return new Loop(depth, e, b);
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
	
	// Primary → Identifier | Literal | Array | 📖Expression📕
	private Expression primary() {
		Expression e = null;
		
		if (token.type().equals(TokenType.Identifier)) {
			// Identifier → Letter { Letter | Digit } [ Array [ Array ] ]
			String id = match(TokenType.Identifier);
			Array d1 = null, d2 = null;
			
			// 1차원 배열
			if (token.type().equals(TokenType.LeftBracket))
				d1 = array();
			// 2차원 배열
			if (token.type().equals(TokenType.LeftBracket))
				d2 = array();
			
			e = new Variable(id, d1, d2);
		} else if (isLiteral()) {
			e = literal();
		} else if (token.type().equals(TokenType.LeftBracket)) {
			e = array();
		} else if (token.type().equals(TokenType.LeftParen)) {
			match(TokenType.LeftParen);
			e = expression();
			match(TokenType.RightParen);
		} else {
			error("primary(Identifier, Literal, LeftBracket, LeftParen)");
		}

		return e;
	}
	
	// Array → 📈Expression { SPACE Expression }📉
	private Array array() {
		Array arr = new Array();

		match(TokenType.LeftBracket);
		
		while (!token.type().equals(TokenType.RightBracket)) {
			arr.list.add(expression());
			
			if (token.type().equals(TokenType.Space))
				match(TokenType.Space);
		}
		
		match(TokenType.RightBracket);
		
		return arr;
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
