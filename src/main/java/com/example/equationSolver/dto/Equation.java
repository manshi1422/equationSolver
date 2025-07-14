package com.example.equationSolver.dto;

import java.util.List;

public class Equation {
    private String id;
    private String original;
    private List<String> postfix;
    private Node expressionTree;

    public Equation(String id, String original, List<String> postfix, Node tree) {
        this.id = id;
        this.original = original;
        this.postfix = postfix;
        this.expressionTree = tree;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getOriginal() {
        return original;
    }

    public void setOriginal(String original) {
        this.original = original;
    }

    public List<String> getPostfix() {
        return postfix;
    }

    public void setPostfix(List<String> postfix) {
        this.postfix = postfix;
    }

    public Node getExpressionTree() {
        return expressionTree;
    }

    public void setExpressionTree(Node expressionTree) {
        this.expressionTree = expressionTree;
    }
// Getters
}
