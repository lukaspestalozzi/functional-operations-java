package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Test-driven development tests for ListOps functional operations.
 * Following TDD principles: write tests first, then implement to make them pass.
 */
@DisplayName("ListOps Functional Operations")
class ListOpsTest {

    @Test
    @DisplayName("map should transform all elements in a list")
    void testMap() {
        // Given
        List<Integer> numbers = List.of(1, 2, 3, 4, 5);
        Function<Integer, Integer> doubler = n -> n * 2;

        // When
        List<Integer> result = ListOps.map(numbers, doubler);

        // Then
        assertThat(result).containsExactly(2, 4, 6, 8, 10);
    }

    @Test
    @DisplayName("map should handle empty list")
    void testMapEmptyList() {
        // Given
        List<Integer> empty = List.of();
        Function<Integer, String> toString = Object::toString;

        // When
        List<String> result = ListOps.map(empty, toString);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("map should throw NullPointerException when list is null")
    void testMapNullList() {
        // Given
        Function<Integer, Integer> identity = n -> n;

        // When/Then
        assertThatThrownBy(() -> ListOps.map(null, identity))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("list");
    }

    @Test
    @DisplayName("map should throw NullPointerException when mapper is null")
    void testMapNullMapper() {
        // Given
        List<Integer> numbers = List.of(1, 2, 3);

        // When/Then
        assertThatThrownBy(() -> ListOps.map(numbers, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("mapper");
    }

    @Test
    @DisplayName("filter should keep only elements matching predicate")
    void testFilter() {
        // Given
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
        Predicate<Integer> isEven = n -> n % 2 == 0;

        // When
        List<Integer> result = ListOps.filter(numbers, isEven);

        // Then
        assertThat(result).containsExactly(2, 4, 6);
    }

    @Test
    @DisplayName("filter should handle empty list")
    void testFilterEmptyList() {
        // Given
        List<Integer> empty = List.of();
        Predicate<Integer> alwaysTrue = n -> true;

        // When
        List<Integer> result = ListOps.filter(empty, alwaysTrue);

        // Then
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("filter should throw NullPointerException when list is null")
    void testFilterNullList() {
        // Given
        Predicate<Integer> alwaysTrue = n -> true;

        // When/Then
        assertThatThrownBy(() -> ListOps.filter(null, alwaysTrue))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("list");
    }

    @Test
    @DisplayName("filter should throw NullPointerException when predicate is null")
    void testFilterNullPredicate() {
        // Given
        List<Integer> numbers = List.of(1, 2, 3);

        // When/Then
        assertThatThrownBy(() -> ListOps.filter(numbers, null))
                .isInstanceOf(NullPointerException.class)
                .hasMessageContaining("predicate");
    }

    @Test
    @DisplayName("map should handle type transformation")
    void testMapTypeTransformation() {
        // Given
        List<Integer> numbers = List.of(1, 2, 3);
        Function<Integer, String> toHex = n -> "0x" + Integer.toHexString(n);

        // When
        List<String> result = ListOps.map(numbers, toHex);

        // Then
        assertThat(result).containsExactly("0x1", "0x2", "0x3");
    }
}
