import java.util.*;

class Program {
	Block body;
	
	Program (Block b) {
		body = b;
	}
	
	public void display() {
		System.out.println("<Program>");
		body.display();
		System.out.println("</Program>");
	}
}

//------------------------------------------------------------

abstract class Statement {
	int depth;
	public void display() {}
}

class Block extends Statement {
	public ArrayList<Statement> members = new ArrayList<Statement>();
	int depth;

	Block (int d) { depth = d; }
	
	public void display() {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<Block>");
		
		for (int i = 0; i < members.size(); i++)
			members.get(i).display();
		
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Block>");
	}
}

class Skip extends Statement {
	int depth;
	
	Skip (int d) { depth = d; }
	
	public void display() {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println("  <Skip>");
	}
}

class Assignment extends Statement {
	Variable target;
	Expression source;
	int depth;
	
	Assignment (int d, Variable t, Expression e) {
		depth = d;
		target = t;
		source = e;
	}
	
	public void display() {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<Assignment>");
		
		target.display(depth + 1);
		source.display(depth + 1);
		
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Assignment>");
	}
}

class Function extends Statement {
	TokenType function;
	Expression domain;
	int depth;
	
	Function (int d, TokenType t, Expression e) {
		depth = d;
		function = t;
		domain = e;
	}
	
	public void display() {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<Function type=\"" + function.toString() + "\">");
		
		domain.display(depth + 1);
		
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Function>");
	}
}

class Conditional extends Statement {
	Expression condition;
	Block thenBranch, elseBranch;
	int depth;
	
	Conditional (int d, Expression e, Block tb) {
		depth = d;
		condition = e;
		thenBranch = tb;
	}
	
	Conditional (int d, Expression e, Block tb, Block eb) {
		depth = d;
		condition = e;
		thenBranch = tb;
		elseBranch = eb;
	}
	
	public void display() {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<Conditional>");
		
		condition.display(depth + 1);
		thenBranch.display();
		elseBranch.display();
		
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Conditional>");
	}
}

class Loop extends Statement {
	Expression condition;
	Block block;
	int depth;
	
	Loop (int d, Expression e, Block b) {
		depth = d;
		condition = e;
		block = b;
	}
	
	public void display() {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<Loop>");
		
		condition.display(depth + 1);
		block.display();
		
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Loop>");
	}
}

//------------------------------------------------------------

abstract class Expression {
	public void display(int depth) {}
}

class Variable extends Expression {
	private String id;
	
	Variable (String name) { 
		id = name;
	}
	
	public String toString() { return id; }
	public int hashCode() { return id.hashCode(); }
	
	public boolean equals (Object obj) {
		String s = ((Variable)obj).id;
		return id.equals(s);
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" Variable: " + id);
	}
}

class Binary extends Expression {
	Operator op;
	Expression term1, term2;

	Binary (Operator o, Expression l, Expression r) {
		op = o;
		term1 = l;
		term2 = r;
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<Binary>");
		
		op.display(depth + 1);
		term1.display(depth + 1);
		term2.display(depth + 1);
		
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Binary>");
	}
}

class Unary extends Expression {
	Operator op;
	Expression term;
	
	Unary (Operator o, Expression e) {
		op = o;
		term = e;
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<Unary>");
		
		op.display(depth + 1);
		term.display(depth + 1);
		
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Unary>");
	}
}

//------------------------------------------------------------

abstract class Value extends Expression {
	// int, bool, char, float, string
	protected Type type;
	protected boolean undef = true;
	
	int intValue() {
		assert false: "Should never reach here.";
		return 0;
	}
	
	boolean boolValue() {
		assert false: "Should never reach here.";
		return false;
	}
	
	char charValue() {
		assert false: "Should never reach here.";
		return ' ';
	}
	
	float floatValue() {
		assert false: "Should never reach here.";
		return 0.0f;
	}
	
	String stringValue() {
		assert false: "Should never reach here.";
		return "";
	}
	
	boolean isUndef() { return undef; }
	
	Type type() { return type; }
	
	static Value mkValue(Type type) {
		if (type == Type.INT) return new IntValue();
		if (type == Type.BOOL) return new BoolValue();
		if (type == Type.CHAR) return new CharValue();
		if (type == Type.FLOAT) return new FloatValue();
		if (type == Type.STRING) return new StringValue();
		throw new IllegalArgumentException("Illegal type in mkValue");
	}
	
	public void display() {}
}

class IntValue extends Value {
	private int value = 0;
	IntValue () { type = Type.INT; }
	IntValue (int v) { this(); value = v; undef = false; }
	
	int intValue() {
		assert !undef: "reference to undefined int value";
		return value;
	}
	
	public String toString() {
		if (undef) return "undef";
		return "" + value;
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" IntValue: " + value);
	}
}

class BoolValue extends Value {
	private boolean value = false;
	BoolValue () { type = Type.BOOL; }
	BoolValue (boolean v) { this(); value = v; undef = false; }
	
	boolean boolValue() {
		assert !undef: "reference to undefined bool value";
		return value;
	}
	
	int intValue() {
		assert !undef: "reference to undefined bool value";
		return value ? 1 : 0;
	}
	
	public String toString() {
		if (undef) return "undef";
		return "" + value;
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" BoolValue: " + value);
	}
}

class CharValue extends Value {
	private char value = ' ';
	CharValue () { type = Type.CHAR; }
	CharValue (char v) { this(); value = v; undef = false; }
	
	char charValue() {
		assert !undef: "reference to undefined char value";
		return value;
	}
	
	public String toString() {
		if (undef) return "undef";
		return "" + value;
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" CharValue: " + value);
	}
}

class FloatValue extends Value {
	private float value = 0.0f;
	FloatValue () { type = Type.FLOAT; }
	FloatValue (float v) { this(); value = v; undef = false; }
	
	float floatValue() {
		assert !undef: "reference to undefined char value";
		return value;
	}
	
	public String toString() {
		if (undef) return "undef";
		return "" + value;
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" FloatValue: " + value);
	}
}

class StringValue extends Value {
	private String value = "";
	StringValue () { type = Type.STRING; }
	StringValue (String v) { this(); value = v; undef = false; }
	
	String stringValue() {
		assert !undef: "reference to undefined string value";
		return value;
	}
	
	public String toString() {
		if (undef) return "undef";
		return value;
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" StringValue: " + value);
	}
}

class Type {
    final static Type INT = new Type("int");
    final static Type BOOL = new Type("bool");
    final static Type CHAR = new Type("char");
    final static Type FLOAT = new Type("float");
    final static Type STRING = new Type("string");
	
	private String id;
	private Type (String t) { id = t; }
	public String toString() { return id; }
}

class Operator {
	String val;
	
	Operator (String s) {
		val = s;
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" Operator: " + val);
	}
}