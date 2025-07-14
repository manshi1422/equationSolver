package com.example.equationSolver.entities;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "equations")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EquationEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String original;

    @Column(name = "postfix_json", columnDefinition = "TEXT")
    private String postfixJson;

    @Column(name = "tree_json", columnDefinition = "TEXT")
    private String treeJson;
}
