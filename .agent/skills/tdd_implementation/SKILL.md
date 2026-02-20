---
name: tdd_implementation
description: Enforces Test-Driven Development (TDD) by writing failing tests first. Use when implementing Domain or Data layers.
---

# TDD Implementation Skill

## Objective
Enforce the creation of unit tests based on TDD best practices BEFORE implementing the actual code, specifically during Domain and Data layer implementation.

## Best Practices

### 1. Write Tests First - Always TDD
- **Rule**: No production code should be written unless there is a failing test for it.
- **Process**: Red (Write failing test) -> Green (Write minimal code to pass) -> Refactor (Clean up code).

### 2. One Assert Per Test
- **Rule**: Each test method should verify a single behavior or outcome.
- **Why**: Makes it easier to pinpoint exactly what failed.

### 3. Descriptive Test Names
- **Rule**: Test names should clearly describe the scenario and expected outcome.
- **Format**: `should [expected behavior] when [scenario]` or `given [context] when [action] then [result]`.
- **Example**: `should return success when api call is valid`

### 4. Arrange-Act-Assert (AAA)
- **Rule**: Structure every test into these three distinct sections.
    - **Arrange**: Set up the data, mocks, and conditions.
    - **Act**: Execute the function or method under test.
    - **Assert**: Verify the result matches expectations.

### 5. Mock External Dependencies
- **Rule**: Unit tests must be isolated. Use `dev.mokkery` or similar libraries to mock Repositories, DataSources, or API clients.
- **Do Not**: Make real network calls or database operations in unit tests.

### 6. Test Edge Cases
- **Rule**: Don't just test the "happy path".
- **scenarios**:
    - Null or empty inputs
    - Large inputs / Boundaries
    - Invalid formats
    - Network timeouts / Errors

### 7. Test Error Paths
- **Rule**: Verify that exceptions are thrown or Result.Failure is returned as expected.

### 8. Keep Tests Fast
- **Rule**: Individual unit tests should run in milliseconds (< 50ms).
- **Avoid**: `Thread.sleep()`, heavy initialization, or real I/O.

### 9. Clean Up After Tests
- **Rule**: Ensure no shared state leaks between tests. Use `@BeforeTest` and `@AfterTest` to reset mocks and state.

### 10. Review Coverage
- **Rule**: Aim for high code coverage (80%+) to ensure robustness.

## When NOT to Use

TDD is not appropriate for:
- **Pure UI layout code** (no business logic, only visual arrangement)
- **Simple data class definitions** (models with no behavior)
- **Configuration files** (build scripts, manifest files)
- **Exploratory prototyping** (rapid proof-of-concept code that will be discarded)

**For Presentation Layer (ViewModels)**: TDD is OPTIONAL but recommended for complex state management.

---

## Instructions
When implementing a Domain UseCase or Data Repository:
1.  **Analyze requirements**: Understand the input and expected output.
2.  **Create Test Class**: Create the test file in `commonTest` mirroring the implementation path.
3.  **Write Failing Test**: Write a test case for the first scenario.
4.  **Run Test**: Confirm it fails.
5.  **Implement Code**: Write just enough code to make the test pass.
6.  **Run Test**: Confirm it passes.
7.  **Refactor**: Clean up and optimize.
8.  **Repeat**: Move to the next scenario/edge case.

## Success Metrics
- [ ] 80%+ code coverage achieved
- [ ] All tests passing (green)
- [ ] No skipped or disabled tests without valid reason
- [ ] Fast test execution (< 30s for suite)
- [ ] E2E tests cover critical user flows (if applicable)
- [ ] Tests catch bugs before production
