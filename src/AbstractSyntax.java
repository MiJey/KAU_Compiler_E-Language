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
	
	//Program 클래스의 toPython 함수는 전체 프로그램을 파이썬 소스로 고쳐준다.
	public void toPython() {

		System.out.println("from random import *"); 
		//randint()함수를 사용하는 키워드가 있으므로 random을 import해준다.
		body.toPython();
	}
}

//------------------------------------------------------------

abstract class Statement {
	int depth;
	public void display() {}
	//statement를 Python으로 고치기 위하여 toPython()함수는 필수적이다.
	abstract public void toPython();
}

class Block extends Statement {
	public ArrayList<Statement> members = new ArrayList<Statement>();
	int depth;

	Block (int d) { depth = d; }
	
	public void display() {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<Block>");
		
		for (int i = 0; i < members.size(); i++) {
			members.get(i).display();
//			System.out.print(members.get(i).);
		}
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Block>");
	}
	
	// Block은 파이썬에서 따로 표시되지 않고 다른 Statement들의 집합을 차례대로 출력해주는 역할.
	public void toPython() {
		for (int i = 0; i < members.size(); i++) {
			members.get(i).toPython();
		}
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
	
	//skip도 하나의 statement이므로 파이썬으로 변환해준다.
	public void toPython() {
		for (int i = 0; i < depth; i++)
			System.out.print("    ");
		System.out.println("");
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
	
	
	public void toPython() {
		//들여쓰기 맞춰주는 부분
		for (int i = 0; i < depth; i++)
			System.out.print("    ");
		
		//대입문이므로 target = source 순으로 출력해준다.
		target.toPython(depth + 1);
		System.out.print(" = ");
		source.toPython(depth + 1);
		System.out.println("");
	}
	
}

class FunctionStatement extends Statement {
	Expression function;
	int depth;
	
	FunctionStatement (int d, Expression e) {
		depth = d;
		function = e;
	}
	
	public void display() {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<FunctionStatement>");
		
		function.display(depth + 1);
		
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</FunctionStatement>");
	}
	
	public void toPython() {
		for (int i = 0; i < depth; i++)
			System.out.print("    ");
		//functionstate 하위의function을 출력한다.
		function.toPython(depth + 1);
		
		System.out.println("");
	}

}

class Conditional extends Statement {
	Expression condition;
	Block thenBranch, elseBranch = null;
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
		if (elseBranch != null) elseBranch.display();
		
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Conditional>");
	}
    
	public void toPython() {
		for (int i = 0; i < depth; i++)
			System.out.print("    ");
		//if문도 순서대로 출력한다.
		System.out.print("if(");
		condition.toPython(depth + 1);
		System.out.println("):");
		
		thenBranch.toPython();
		if (elseBranch != null) {
			System.out.print("else:");
			
			elseBranch.toPython();
			System.out.println("");
			
		}
		
		for (int i = 0; i < depth; i++)
			System.out.print("    ");
//		System.out.println(" └</Conditional>");
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
	
	public void toPython() {
		for (int i = 0; i < depth; i++)
			System.out.print("    ");

		//while문도 순서대로 출력한다.
		System.out.print("while(");
		condition.toPython(depth + 1);
		System.out.println("):");
		block.toPython();
	}
	
}

class BreakStatement extends Statement {
	int depth;
	
	BreakStatement (int d) { depth = d; }
	
	public void display() {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println("  <BreakStatement>");
	}
	
	//e-lang에서는 break문도 하나의 statement로 취금한다.
	public void toPython() {
		for (int i = 0; i < depth; i++)
			System.out.print("    ");
		System.out.println(" break");
	}

}

class ContinueStatement extends Statement {
	int depth;
	
	ContinueStatement (int d) { depth = d; }
	
	public void display() {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println("  <ContinueStatement>");
	}

	//continue도 break와 같이 statement로 취급한다.
	public void toPython() {
		for (int i = 0; i < depth; i++)
			System.out.print("    ");
		System.out.println("  continue");
	}

}

//------------------------------------------------------------

abstract class Expression {
	public void display(int depth) {}
	abstract public void toPython(int depth);
	
}

class Variable extends Expression {
	private String id;
	private Expression d1, d2;	// arr[d1][d2]
	
	Variable (String name) {
		id = name;
	}
	
	Variable (String name, Expression d1, Expression d2) { 
		id = name;
		this.d1 = d1;
		this.d2 = d2;
	}
	
	public String toString() { 
		String result = id;
		if (d1 != null)
			result += "[" + d1.toString() + "]";
		if (d2 != null)
			result += "[" + d2.toString() + "]";
		return result;
	}
	
	public int hashCode() { return id.hashCode(); }
	
	public boolean equals (Object obj) {
		String s = ((Variable)obj).id;
		return id.equals(s);
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" Variable: " + toString());
	}
	//lexical level은 대부분 간단하게 출력만 해 주면 된다.
	public void toPython(int depth) {
		System.out.print(toString()); //변수명만 출력
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
	//binary를 문법 순서에 맞게 출력해준다.
	public void toPython(int depth) {
		term1.toPython(depth + 1);
		op.toPython(depth + 1);
		term2.toPython(depth + 1);
		
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
	//unary또한 순서대로 출력
	public void toPython(int depth) {
		
		op.toPython(depth + 1);
		term.toPython(depth + 1);
		
		for (int i = 0; i < depth; i++)
			System.out.print("    ");
	}
	
}

class Array extends Expression {
	public ArrayList<Expression> members = new ArrayList<Expression>();
	
	public String toString() {
		String result = "[";
		
		for (int i = 0; i < members.size() - 1; i++)
			result += members.get(i).toString() + ", ";
		
		if (members.size() != 0)
			result += members.get(members.size() - 1).toString();
		
		result += "]";
		return result;
	}

	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<Array>");
		
		for (int i = 0; i < members.size(); i++)
			members.get(i).display(depth + 1);
		
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Array>");
	}
	
	public void toPython(int depth) {
		//Python의 리스트는 대괄호로 묶으므로 대괄호를 열고 순서대로 만든다.
		System.out.print("[");
		members.get(0).toPython(depth + 1);
		for (int i = 1; i < members.size(); i++) {
			System.out.print(", ");			
			members.get(i).toPython(depth + 1);
		}
		System.out.print("]");
	}
	
}

class Function extends Expression {
	TokenType function;
	TokenType functionType = null;
	Expression param = null;
	
	// time
	Function (TokenType t) {
		function = t;
	}
	
	// print, input, random
	Function (TokenType t, Expression p) {
		function = t;
		param = p;
	}
	
	// int(input), float(input)
	Function (TokenType t, TokenType t2, Expression p) {
		function = t;
		functionType = t2;
		param = p;
	}
	
	public void display(int depth) {
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" ┌<Function>");
		
		for (int i = 0; i < depth + 1; i++)
			System.out.print(" │ ");
		System.out.println(" Type: " + function.toString() + (functionType == null ? "" : "(" + functionType.toString() + ")"));
		
		if (param != null)
			param.display(depth + 1);
			
		for (int i = 0; i < depth; i++)
			System.out.print(" │ ");
		System.out.println(" └</Function>");
	}
	
	public void toPython(int depth) {
		//e-lang에서 제공하는 키워드함수는 상황에따라 적절히 파이썬으로 변환해주어야한다.
		//print키워드는 print함수를 이용한다.
		if(function == TokenType.Print) {
			System.out.print("print(");
			if (param != null)
				param.toPython(depth + 1);
			System.out.print(")");
		}
		
		//input키워드는 input함수를 이용하되 위치와 뒤따르는 키워드에 따라 매핑 방식이 달라진다.
		if(function == TokenType.Input) {
			if (param != null)
				param.toPython(depth + 1);
			if(functionType == TokenType.IntType)
				System.out.print(" = int(input())");
			else if(functionType == TokenType.FloatType)
				System.out.print("= float(input())");
			else
				System.out.print("= input()");
		}
		//random 키워드도 사용하는 위치나 뒤따르는 키워드에 따라서 매핑 방식이 달라진다.
		if(function == TokenType.Random) {
			if (param != null) {
				System.out.print("randint(0,");
				param.toPython(depth + 1);
				System.out.print(")");
			}else {
				System.out.print("randint(0,100)");
			}
		}
			
		
	}
}


//------------------------------------------------------------
//lexical level은 간단하게 출력만 해 준다.
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

	public void toPython(int depth) {
//		for (int i = 0; i < depth; i++)
//			System.out.print("    ");
		System.out.print(value);
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
	
	public void toPython(int depth) {
//		for (int i = 0; i < depth; i++)
//			System.out.print(" │ ");
		if(value) {
			System.out.print("True");
		}else {
			System.out.print("False");
		}
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

	public void toPython(int depth) {
//		for (int i = 0; i < depth; i++)
//			System.out.print(" │ ");
		System.out.print("'" + value + "'");
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
	
	public void toPython(int depth) {
//		for (int i = 0; i < depth; i++)
//			System.out.print(" │ ");
		System.out.print(value);
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

	public void toPython(int depth) {
//		for (int i = 0; i < depth; i++)
//			System.out.print(" │ ");
		System.out.print("'" + value + "'");
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
	
	public void toPython(int depth) {
		System.out.print(val);
	}
}