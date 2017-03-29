import java.io.*;
import java.util.Stack;
import java.util.regex.*;
public class CalculatorTest
{
	public static boolean isNumber(String in, int i){
		if(in.charAt(i) <= '9' && in.charAt(i) >= '0'){
			return true;
		}
		else return false;
	}
	
	public static int getRank(char Op){
		if(Op == '+' || Op == '-'){
			return 1;
		}
		else if(Op == '*' || Op == '/' || Op == '%'){
			return 2;
		}
		else if(Op == '~'){
			return 3;
		}
		else if(Op == '^'){
			return 4;
		}
		else if(Op == '(' || Op == ')'){
			return 0;
		}
		else{
			return -1;
		}
	}// 연산자의 우선순위 따지기
	
	public static String toPost(String infix) throws Exception{
		Pattern p = Pattern.compile("\\d\\s+\\d");// (숫자/s숫자) 오류
		Matcher m = p.matcher(infix);
		if(m.find()) throw new Exception();
		infix = infix.replaceAll("\\s", "");
		p = Pattern.compile("\\(\\)|\\(\\D\\)|\\(\\d*[+*-/%^]\\)|\\(\\d*[+*/%^]\\)|\\d[()]\\d|\\)\\d");
		m = p.matcher(infix);// 기타 등등 입력의 오류 걸러내기
		if(m.find()) throw new Exception();

		Stack<Character> OpStack = new Stack<>();
		Long temp;
		String result = "";
		char ch;
		for(int i = 0; i < infix.length(); i++){
			if(isNumber(infix, i)){
				int n = i;
				String subs = "";
				for(;i < infix.length()-1;i++){
					if(!isNumber(infix, i+1)) break;
				}
				int k = i+1;
				subs = infix.substring(n,k);
				temp = Long.parseLong(subs);
				result += temp + " ";
				continue;
			}//string type의 숫자를 Long type으로 바꾸기
			ch = infix.charAt(i);
			if(ch == '-'){
				if(i == 0){
					ch = '~';
				}
				else if(!isNumber(infix,i-1) && infix.charAt(i-1) != ')'){
					ch = '~';
				}
			}//unary 확인하기
			
			if(ch == '('){
					OpStack.push('(');
				}
			else if(OpStack.isEmpty()){
				OpStack.push(ch);
			}
			else if(ch == ')'){
				while(OpStack.peek() != '('){
					result += OpStack.pop() + " ";
				}
				OpStack.pop();
			}// 괄호 처리
			else if(ch == '^' || ch == '~'){
				while(getRank(OpStack.peek()) > getRank(ch)){
					result += OpStack.pop() + " ";
					if(OpStack.isEmpty()){
						break;
					}
				}
				OpStack.push(ch);
			}// right-associative 처리
			else{
				while(getRank(OpStack.peek()) >= getRank(ch)){
					result += OpStack.pop() + " ";
					if(OpStack.isEmpty()){
						break;
					}
				}
				OpStack.push(ch);
			}// left-associative 처리
		}
		while(!OpStack.isEmpty()){
			result += OpStack.pop() + " ";
		}
		result = result.substring(0,result.length()-1);
		return result;
	}
	
	public static void main(String args[])
	{
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

		while (true)
		{
			try
			{
				String input = br.readLine();
				if (input.compareTo("q") == 0)
					break;

				command(input);
			}
			catch (Exception e)
			{
				System.out.println("ERROR");
			}
		}
	}

	private static void command(String input) throws Exception
	{
		String postfix = toPost(input);
		Stack<Long> cal = new Stack<>();
		long op1, op2, result;
	
		for(int i = 0; i < postfix.length(); i++){
			if(isNumber(postfix, i)){
				int n = i;
				String subs = "";
				for(;i < postfix.length()-1;i++){
					if(!isNumber(postfix, i+1)) break;
				}
				int k = i+1;
				subs = postfix.substring(n,k);
				cal.push(Long.parseLong(subs));
			}	//string type의 숫자를 Long type으로 바꾸기

			else {//연산자의 경우에 따른 계산
				switch (postfix.charAt(i)){
				case '+' :
					op1 = cal.pop();
					op2 = cal.pop();
					result = op2 + op1;
					cal.push(result);
					break;
				case '-' :
					op1 = cal.pop();
					op2 = cal.pop();
					result = op2 - op1;
					cal.push(result);
					break;
				case '*' :
					op1 = cal.pop();
					op2 = cal.pop();
					result = op2 * op1;
					cal.push(result);
					break;
				case '~' :
					op1 = cal.pop();
					result = op1 * (-1);
					cal.push(result);
					break;
				case '%' :
					op1 = cal.pop();
					op2 = cal.pop();
					result = op2 % op1;
					cal.push(result);
					break;
				case '/' :
					op1 = cal.pop();
					op2 = cal.pop();
					result = (long)(op2 / op1);
					cal.push(result);
					break;
				case '^' :
					op1 = cal.pop();
					op2 = cal.pop();
					if(op2 == 0 && op1 < 0)	throw new Exception();//0의 음수승 에러
					result = (long) Math.pow(op2, op1);
					cal.push(result);
					break;
				case ' ' :
					break;
				default :
					throw new Exception();
				}
			}
		}
		result = cal.pop();
		if(!cal.isEmpty())	throw new Exception();//스택에 남은 게 있으면 오류
		System.out.println(postfix);
		System.out.println(result + "");
	}
}
