package com.example.equationSolver.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Node {
    public String value;
    public Node left;
    public Node right;

    // No-args constructor required for Jackson
    public Node() {}

    public Node(String value) {
        this.value = value;
    }
}
