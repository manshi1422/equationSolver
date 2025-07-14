package com.example.equationSolver.utils;

import java.util.*;

public class InfixToPostfixConverter {

    private static final Map<String, Integer> precedence = Map.of(
        "+", 1, "-", 1, "*", 2, "/", 2, "^", 3
    );

    public static List<String> convert(String infix) {
        List<String> output = new ArrayList<>();
        Deque<String> stack = new ArrayDeque<>();

        StringTokenizer tokens = new StringTokenizer(infix, "+-*/^() ", true);

        while (tokens.hasMoreTokens()) {
            String token = tokens.nextToken().trim();
            if (token.isEmpty()) continue;

            if (Character.isLetterOrDigit(token.charAt(0))) {
                output.add(token);
            } else if (token.equals("(")) {
                stack.push(token);
            } else if (token.equals(")")) {
                while (!stack.peek().equals("(")) {
                    output.add(stack.pop());
                }
                stack.pop(); // pop '('
            } else { // operator
                while (!stack.isEmpty() && precedence.getOrDefault(stack.peek(), 0) >= precedence.get(token)) {
                    output.add(stack.pop());
                }
                stack.push(token);
            }
        }

        while (!stack.isEmpty()) {
            output.add(stack.pop());
        }

        return output;
    }
}
