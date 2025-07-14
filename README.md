
# 🧮 Algebraic Equation Solver (Spring Boot + PostgreSQL)

This project is a Spring Boot RESTful web service that allows users to:

- Store algebraic equations in infix format (e.g. `3 * x + 2 * y - z`)
- Convert and persist them as postfix expression trees in a PostgreSQL database
- Retrieve all stored equations
- Evaluate any equation by substituting variable values

---

## 📌 Features

- ✅ Infix → Postfix conversion
- ✅ Postfix expression tree builder
- ✅ Evaluation with variable substitution
- ✅ REST APIs with JSON
- ✅ PostgreSQL integration
- ✅ JUnit-based unit tests with Mockito

---

## 🚀 Tech Stack

- Java 17+
- Spring Boot 3.x
- Spring Data JPA
- PostgreSQL
- Jackson (for JSON and tree serialization)
- JUnit 5 + Mockito (for testing)

---

## 📦 API Endpoints

### 🔹 1. Store an Equation

**POST** `/api/equations/store`

**Request Body:**
```json
{
  "equation": "3 * x + 2 * y - z"
}
