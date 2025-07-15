package com.example.equationSolver;

import com.example.equationSolver.entities.EquationEntity;
import com.example.equationSolver.repositories.EquationRepository;
import com.example.equationSolver.services.EquationService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@ExtendWith(MockitoExtension.class)
public class AlgebraicEquationsSolverApplicationTests {

	@Mock
	private EquationRepository equationRepository;

	@InjectMocks
	private EquationService equationService;

	@Spy
	private ObjectMapper objectMapper = new ObjectMapper();

	@Test
	void testStoreEquation() throws Exception {
		String infix = "3 * x + 2 * y - z";
		ArgumentCaptor<EquationEntity> captor = ArgumentCaptor.forClass(EquationEntity.class);
		when(equationRepository.save(any())).thenAnswer(invocation -> {
			EquationEntity entity = invocation.getArgument(0);
			entity.setId(1L);  // manually set ID since @GeneratedValue doesn't run in unit tests
			return entity;
		});

		Long id = equationService.store(infix);

		verify(equationRepository).save(captor.capture());
		EquationEntity saved = captor.getValue();

		assertNotNull(saved);
		assertEquals(infix, saved.getOriginal());
		assertNotNull(saved.getPostfixJson());
		assertNotNull(saved.getTreeJson());
		assertEquals(1L, id);
	}

	@Test
	void testGetAllEquations() {
		EquationEntity e1 = new EquationEntity(1L, "x + y", "[\"x\",\"y\",\"+\"]", "{\"value\":\"+\",\"left\":{\"value\":\"x\"},\"right\":{\"value\":\"y\"}}");
		EquationEntity e2 = new EquationEntity(2L, "x - y", "[\"x\",\"y\",\"-\"]", "{\"value\":\"-\",\"left\":{\"value\":\"x\"},\"right\":{\"value\":\"y\"}}");

		when(equationRepository.findAll()).thenReturn(List.of(e1, e2));

		List<Map<String, Object>> results = equationService.getAll();

		assertEquals(2, results.size());
		assertEquals("x + y", results.get(0).get("equation"));
		assertEquals("x - y", results.get(1).get("equation"));
	}

	@Test
	void testEvaluateEquation() throws Exception {
		EquationEntity e = new EquationEntity(
				1L,
				"x + y",
				"[\"x\",\"y\",\"+\"]",
				"{\"value\":\"+\",\"left\":{\"value\":\"x\"},\"right\":{\"value\":\"y\"}}"
		);

		when(equationRepository.findById(1L)).thenReturn(Optional.of(e));

		double result = equationService.evaluate(1L, Map.of("x", 2.0, "y", 3.0));
		assertEquals(5.0, result);
	}

	@Test
	void testEvaluateEquation_MissingVariable() throws Exception {
		EquationEntity e = new EquationEntity(
				1L,
				"x + y",
				"[\"x\",\"y\",\"+\"]",
				"{\"value\":\"+\",\"left\":{\"value\":\"x\"},\"right\":{\"value\":\"y\"}}"
		);

		when(equationRepository.findById(1L)).thenReturn(Optional.of(e));

		Exception ex = assertThrows(RuntimeException.class, () ->
				equationService.evaluate(1L, Map.of("x", 2.0)) // missing y
		);

		assertTrue(ex.getMessage().contains("Missing variable value"));
	}

	@Test
	void testEvaluateEquation_NotFound() {
		when(equationRepository.findById(999L)).thenReturn(Optional.empty());

		Exception ex = assertThrows(RuntimeException.class, () ->
				equationService.evaluate(999L, Map.of("x", 1.0))
		);

		assertEquals("Not found", ex.getMessage());
	}
}
