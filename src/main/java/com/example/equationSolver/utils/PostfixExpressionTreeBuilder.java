package com.example.equationSolver.utils;

import com.example.equationSolver.dto.Node;

import java.util.List;
import java.util.Stack;

public class PostfixExpressionTreeBuilder {

    public static Node build(List<String> postfix) {
        Stack<Node> stack = new Stack<>();

        for (String token : postfix) {
            Node node = new Node(token);
            if (isOperator(token)) {
                node.right = stack.pop();
                node.left = stack.pop();
            }
            stack.push(node);
        }

        return stack.peek();
    }

    private static boolean isOperator(String token) {
        return "+-*/^".contains(token);
    }
}
