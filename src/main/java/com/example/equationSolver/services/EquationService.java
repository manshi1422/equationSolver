package com.example.equationSolver.services;

import com.example.equationSolver.dto.Node;
import com.example.equationSolver.entities.EquationEntity;
import com.example.equationSolver.repositories.EquationRepository;
import com.example.equationSolver.utils.InfixToPostfixConverter;
import com.example.equationSolver.utils.PostfixExpressionTreeBuilder;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
public class EquationService {

    private final EquationRepository equationRepo;
    private final ObjectMapper objectMapper;

    public EquationService(EquationRepository repo, ObjectMapper mapper) {
        this.equationRepo = repo;
        this.objectMapper = mapper;
    }

    public Long store(String infix) throws JsonProcessingException {
        List<String> postfix = InfixToPostfixConverter.convert(infix);
        Node tree = PostfixExpressionTreeBuilder.build(postfix);

        EquationEntity entity = new EquationEntity();
        entity.setOriginal(infix);
        entity.setPostfixJson(objectMapper.writeValueAsString(postfix));
        entity.setTreeJson(objectMapper.writeValueAsString(tree));

        EquationEntity saved = equationRepo.save(entity);
        return saved.getId();
    }

    public List<Map<String, Object>> getAll() {
        return equationRepo.findAll().stream()
                .map(e -> Map.<String, Object>of(
                        "equationId", e.getId(),         // Long
                        "equation", e.getOriginal()      // String
                ))
                .collect(Collectors.toList());
    }

    public double evaluate(Long id, Map<String, Double> vars) throws JsonProcessingException {
        EquationEntity entity = equationRepo.findById(id).orElseThrow(() -> new RuntimeException("Not found"));

        Node tree = objectMapper.readValue(entity.getTreeJson(), Node.class);
        return evaluateTree(tree, vars);
    }

    private double evaluateTree(Node node, Map<String, Double> vars) {
        if (node == null) return 0;
        String val = node.value;

        if ("+-*/^".contains(val)) {
            double left = evaluateTree(node.left, vars);
            double right = evaluateTree(node.right, vars);
            return switch (val) {
                case "+" -> left + right;
                case "-" -> left - right;
                case "*" -> left * right;
                case "/" -> left / right;
                case "^" -> Math.pow(left, right);
                default -> throw new RuntimeException("Unknown op: " + val);
            };
        }

        if (vars.containsKey(val)) return vars.get(val);
        try {
            return Double.parseDouble(val);
        } catch (NumberFormatException e) {
            throw new RuntimeException("Missing variable value: " + val);
        }
    }
}
