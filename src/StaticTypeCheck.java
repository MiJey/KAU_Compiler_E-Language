// StaticTypeCheck.java

import java.util.*;

// Static type checking for Clite is defined by the functions 
// V and the auxiliary functions typing and typeOf.  These
// functions use the classes in the Abstract Syntax of Clite.

public class StaticTypeCheck {
	private static int warning = 0;
	
	//E-language는 선언부가 따로 없기 때문에 대입연산자 수행 시 타입을 업데이트 해주어야 한다.
	public static void is_declared (TypeMap tm,  Variable v, Type t) {
        if(tm.containsKey(v)) {
        	tm.replace(v, t);
        	return;
        }
        tm.put(v, t);
        return;
    }

    public static void check(boolean test, String msg) {
        if (test)  return;
        System.err.println(msg);
        System.exit(1);
    }
    
    //선언부 없이 body에 대한 V만 호출한다.
    public static void V (Program p) {
        V (p.body ,p.declarations);
    } 

    public static Type typeOf (Expression e, TypeMap tm) {
        if (e instanceof Value) return ((Value)e).type;
        if (e instanceof Variable) {
            Variable v = (Variable)e;
            check (tm.containsKey(v), "undefined variable: " + v);
            return (Type) tm.get(v);
        }
        if (e instanceof Binary) {
            Binary b = (Binary)e;
            
            Type typ1 = typeOf(b.term1,tm);
            Type typ2 = typeOf(b.term2,tm);
            if(b.op.val == "+" || b.op.val == "-" || b.op.val == "*") {
                if ((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT)) {
                	if(typ1==Type.FLOAT || typ2==Type.FLOAT) return (Type.FLOAT);
                	else return (Type.INT);
                }
                else if (b.op.val == "+" && typeOf(b.term1,tm)== Type.STRING && typeOf(b.term2,tm)== Type.STRING)
                    return (Type.STRING);
            }
            if(b.op.val == "/" || b.op.val == "%") {
            	 if ((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT)) {
            		if(typ1==Type.FLOAT || typ2==Type.FLOAT) return (Type.FLOAT);
                 	else return (Type.INT);
            	 }
            }
            if (b.op.val ==  "&" || b.op.val == "|" || b.op.val == "<" || b.op.val == "<=" || b.op.val == "==" ||
            		b.op.val == "!=" || b.op.val == ">" || b.op.val == ">=") 
                return (Type.BOOL);
        }
        if (e instanceof Unary) {
            Unary u = (Unary)e;
            if (u.op.val == "!")        return (Type.BOOL);
            else if (u.op.val == "-")	return typeOf(u.term,tm);
            else if (u.op.val == "int") return (Type.INT);
            else if (u.op.val == "float") return (Type.FLOAT);
            else if (u.op.val == "char")  return (Type.CHAR);
            else if (u.op.val == "string")  return (Type.STRING);
        }

        //Function : int형 함수는 int 반환, void형 함수는 void 반환 
        if(e instanceof Function) {
        	if(((Function) e).function == TokenType.Time) return (Type.INT);
        	else if(((Function) e).function == TokenType.Random) return (Type.INT);
        	else return (Type.BOOL);
        }

        //Array : Array값 반환 
        if(e instanceof Array) {
        	return (Type.ARRAY);
        }
        e.display(10);
        throw new IllegalArgumentException("[typeOf] Expression \'" +e.toString()+ "\' has error");
    } 

    public static void V (Expression e, TypeMap tm) {
        if (e instanceof Value) 
            return;
        if (e instanceof Variable) { 
            Variable v = (Variable)e;
            check( tm.containsKey(v)
                   , "[V(E) Variable] undeclared variable: " + v);
            return;
        }
        if (e instanceof Binary) {
            Binary b = (Binary) e;
            Type typ1 = typeOf(b.term1, tm);
            Type typ2 = typeOf(b.term2, tm);
            
            V (b.term1, tm);
            V (b.term2, tm);
            
            if(b.op.val == "+" || b.op.val == "-" || b.op.val == "*") {
                if ((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT));
                else if (b.op.val == "+" && typ1== Type.STRING && typ2== Type.STRING);
                else check(false, "[V(E) Binary] type error for " + e.toString());
            }
            else if(b.op.val == "/" || b.op.val == "%") {
            	if(b.term2 instanceof Value) {
	            	if((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT)) {
	            		if(typ2==Type.FLOAT && ((Value)b.term2).mkValue(((Value)b.term2).type()).floatValue() != 0.0);
		            	else if(typ2==Type.INT && typeOf(b.term2,tm)== Type.FLOAT && ((Value)b.term2).mkValue(((Value)b.term2).type()).intValue() != 0);
		            	else check(false, "[V(E) Binary] type error for " + e.toString());
	            	}
            	} 
            	else {
            		if((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT));
                	else check(false, "[V(E) Binary] type error for " + e.toString());
            	}
            }
            else if (b.op.val == "<" || b.op.val == "<=" || b.op.val == "==" || b.op.val == "!=" || b.op.val == ">" || b.op.val == ">=") {
            	if(typ1 == typ2);
            	else if((typ1==Type.FLOAT || typ1==Type.INT) && (typ2==Type.FLOAT || typ2==Type.INT));
            	else check(false, "[V(E) Binary] type error for " + e.toString());
            }
            else if (b.op.val ==  "&" || b.op.val == "|") {
            	if(typ1== Type.BOOL && typeOf(b.term2,tm)== Type.BOOL);
            	else check(false, "[V(E) Binary] type error for " + e.toString());
            }
            else
                throw new IllegalArgumentException("[V(E) Binary] Binary Operator ERRER");
            return;
        }
        
        if (e instanceof Unary) {	//Unary 식이 bool 타입인지 아닌지를 체크한다.
        	Unary u = (Unary) e;
        	Type type = typeOf(u.term, tm);
        	V(u.term,tm);				// 1)표현식이 유효한지 체크한다.
        	if (u.op.val ==  "&" || u.op.val == "|") {	// 2)operator가 bool인지 체크한다 
        		return;
        	}
        	else if(u.op.val=="!") {	// 2-1)operator가 NOT인지 체크한다. 
        		return;
        	}
        	else if(u.op.val=="-" && 	// 2-2) NEG, INT 인지 체크한다.
        			(type == Type.INT
        			||type == Type.FLOAT)) {
        		if(type == Type.FLOAT) {	// 2-2-1) 네거티브 오퍼레이터에 인트형이 아닌 float형이 들어가면 경고를 해준다.
        			System.out.println("\n[Warning]");
        			e.display(0);
        			System.out.println("implicit conversion from 'float' to 'int'");
        			warning++;
        		}
        		return;
        	}
        }
        if (e instanceof Function) {					//function 인자 값으로 받은 파라미터의 타입을 체크한다.
        	Function f = (Function)e;
        	if(f.param == null) return;
        	if(f.param != null && f.functionType == null) {
        		Type type = typeOf(f.param, tm);
        		
        												//Input 함수는 String만 받을 수 있다.
        		if(f.function == TokenType.Input) {
        			if(type == Type.STRING) return;
        			
        												//Random 함수는 Int만 받을 수 있다.
        		}else if(f.function == TokenType.Random) {
        			if(type == Type.INT) return;
        		
        												//print 함수는 모든 타입을 받을 수 있다.
        		}else if(f.function == TokenType.Print) {	
        			return;
        		}
        	}else if(f.functionType != null) {
        		Type type = typeOf(f.param, tm);
        												//Input-IntType 함수는 String만 받을 수 있다.
        		if(f.functionType ==TokenType.IntType) {
        			if(type == Type.INT) return;
        		}
        												//Input-FloatType 함수는 String만 받을 수 있다.
        		else if(f.functionType ==TokenType.FloatType) {
        			if(type == Type.FLOAT) return;
        		}
        	}
        }
        if (e instanceof Array) {
        	return;
        }
        throw new IllegalArgumentException("[V(Expression)] Expression \'" + e.toString()+ "\' has error");
    }

    public static void V (Statement s, TypeMap tm) {
        if ( s == null )
            throw new IllegalArgumentException( "AST error: null statement");
        if (s instanceof Skip) return;
        if (s instanceof Assignment) {
            Assignment a = (Assignment)s;
            V(a.source, tm);
            
            Type srctype = typeOf(a.source, tm);
            is_declared(tm,a.target,srctype);
            
            return;
        } 
        if (s instanceof Conditional) {			// 조건문의 유효성 검증.
        	Conditional a = (Conditional)s;
        	V(a.condition, tm);					// 조건부는 check the Expression is valid
        	V(a.thenBranch,tm);					// 실행부는 check the Block is valid
        	V(a.elseBranch,tm);
        	
            return;
        }
        if (s instanceof Loop) { 				// loop문 유효성 검증.
        	Loop a = (Loop)s;
        	V(a.condition, tm);					// 조건부는 expression이 유효한지 확인.
        	V(a.block, tm);						// 실행부는 block문이 유효한지 확인. 
        	
            return;
        } 
        if (s instanceof Block) {				
        	Block a = (Block)s;
        	
        	for (Statement member : a.members) // block은 각 라인별로 statement가 유효한지 확인한다.
                V(member,tm);
            return;
        } 
        if(s instanceof FunctionStatement) {
        	FunctionStatement f = (FunctionStatement)s;
        	V(f.function,tm);
        	return;
        }
        if(s instanceof ContinueStatement) {
        	return;
        }
        if(s instanceof BreakStatement) {
        	return;
        }
        throw new IllegalArgumentException("should never reach here");
    }

    public static void main(String args[]) {
        Parser parser  = new Parser(new Lexer(args[0]));
        Program prog = parser.program();
        
        System.out.println("\nBegin type checking...");
        
        V(prog);
        
        for (Variable key : prog.declarations.keySet()) {
			System.out.println(String.format("키 : %s, 값 : %s", key, prog.declarations.get(key)));
		}
        if(warning>0) System.out.println("\nTotal :"+warning + " Warnning");
        else if(warning==0)System.out.print("perfect");
        
        System.out.println("##########Python Code###########");
        prog.toPython();
        
    } //main

} // class StaticTypeCheck