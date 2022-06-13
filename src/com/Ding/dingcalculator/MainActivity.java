package com.Ding.dingcalculator;

import java.text.NumberFormat;
import java.util.Locale;
import java.util.Stack;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Toast;

public class MainActivity extends Activity {

	private Stack stack = new Stack();

	/** Called when the activity is first created. */
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		//
		NumberFormat nf1 = NumberFormat.getInstance(Locale.KOREA);
		Toast.makeText(getApplicationContext(), nf1.format(100000), Toast.LENGTH_LONG).show();

//		try {
//			Toast.makeText(getApplicationContext(), String.valueOf(postfixCalc(postfix("200*(35-15)/50"))), Toast.LENGTH_LONG).show();
//		} catch (Exception e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
		

	}

	// 연산자 우선순위를 판정 숫자화 반환
	private int operatorPriority(char operator) {
		if (operator == '(')
			return 0;
		if (operator == '+' || operator == '-')
			return 1;
		if (operator == '*' || operator == '/')
			return 2;
		return 3;
	}

	// 연산자를 표현한 문자인지 검사
	public boolean isOperator(char ch) {
		return (ch == '+' || ch == '-' || ch == '*' || ch == '/');
	}

	// 숫자를 표현한 문자인지 검사
	public boolean isNumeric(char ch) {
		return (ch >= '0' && ch <= '9');
	}

	// 중위표현식을 후위표현식으로 변환
	public String postfix(String expression) throws Exception {
		StringBuilder sb = new StringBuilder();
		char[] exp;
		char ch;

		if (expression == null) {
			throw new NullPointerException("expression is null");
		}

		exp = expression.toCharArray();
		for (int i = 0; i < exp.length; i++) {
			if (exp[i] == '(') {
				stack.push(exp[i]);

			} else if (exp[i] == ')') {
				while ((ch = (Character) stack.pop()) != '(') {
					sb.append(ch);
					sb.append(' ');
				}

			} else if (isOperator(exp[i])) {
				while (!stack.isEmpty()
						&& operatorPriority((Character) stack.peek()) >= operatorPriority(exp[i])) {
					sb.append(stack.pop());
					sb.append(' ');
				}
				stack.push(exp[i]);

			} else if (isNumeric(exp[i])) {
				do {
					sb.append(exp[i++]);
				} while (i < exp.length && isNumeric(exp[i]));
				sb.append(' ');
				i--;
			}
		}

		while (!stack.isEmpty()) {
			sb.append(stack.pop());
			sb.append(' ');
		}

		return sb.toString();
	}

	// 후위표현식을 계산
	public double postfixCalc(String expression) throws Exception {
		char[] exp;
		double num;

		if (expression == null) {
			throw new NullPointerException("expression is null");
		}

		exp = expression.toCharArray();
		for (int i = 0; i < exp.length; i++) {
			if (isNumeric(exp[i])) {
				num = 0;

				do {
					num = num * 10 + exp[i++] - '0';
					//Log.d("asdasd", String.valueOf(num));
				} while (i < exp.length && isNumeric(exp[i]));
				stack.push(num);
				i--;

			} else if (exp[i] == '+') {
				stack.push((Double) stack.pop() + (Double) stack.pop());

			} else if (exp[i] == '*') {
				stack.push((Double) stack.pop() * (Double) stack.pop());

			} else if (exp[i] == '-') {
				num = (Double) stack.pop();
				stack.push((Double) stack.pop() - num);
			} else if (exp[i] == '/') {
				num = (Double) stack.pop();
				stack.push((Double) stack.pop() / num);
			}
		}

		return (Double) stack.pop();
	}
}
