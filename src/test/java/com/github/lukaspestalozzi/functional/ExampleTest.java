package com.github.lukaspestalozzi.functional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Example test class demonstrating TDD setup with JUnit 5 and AssertJ.
 * <p>
 * This serves as a template for writing tests in this library.
 * Tests should be written BEFORE implementation (TDD approach).
 * </p>
 */
@DisplayName("Example Test Suite")
class ExampleTest {

    @Test
    @DisplayName("should demonstrate basic assertion")
    void testBasicAssertion() {
        // given
        String expected = "Hello, World!";

        // when
        String actual = "Hello, World!";

        // then
        assertThat(actual).isEqualTo(expected);
    }

    @Test
    @DisplayName("should demonstrate fluent assertions")
    void testFluentAssertions() {
        // given
        var numbers = java.util.List.of(1, 2, 3, 4, 5);

        // when / then
        assertThat(numbers)
                .isNotNull()
                .hasSize(5)
                .contains(3)
                .doesNotContain(6);
    }
}
