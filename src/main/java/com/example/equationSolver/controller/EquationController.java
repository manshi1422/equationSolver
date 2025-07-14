package com.example.equationSolver.controller;

import com.example.equationSolver.services.EquationService;
import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/equations")
public class EquationController {

    private final EquationService service;

    public EquationController(EquationService service) {
        this.service = service;
    }

    @PostMapping("/store")
    public ResponseEntity<?> store(@RequestBody Map<String, String> request) throws JsonProcessingException {
        Long id = service.store(request.get("equation"));
        return ResponseEntity.ok(Map.of("message", "Equation stored successfully", "equationId", id));
    }

    @GetMapping
    public ResponseEntity<?> getAll() {
        return ResponseEntity.ok(Map.of("equations", service.getAll()));
    }

    @PostMapping("/{id}/evaluate")
    public ResponseEntity<?> evaluate(@PathVariable Long id, @RequestBody Map<String, Map<String, Double>> request)
            throws JsonProcessingException {
        double result = service.evaluate(id, request.get("variables"));
        return ResponseEntity.ok(Map.of("equationId", id, "result", result));
    }
}
