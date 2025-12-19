package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.drop")
class SetOpsDropTest {

  @Test
  @DisplayName("should drop n elements")
  void testDrop() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

    Set<Integer> result = SetOps.drop(numbers, 2);

    assertThat(result).hasSize(3);
  }

  @Test
  @DisplayName("should return empty when n exceeds set size")
  void testDropMoreThanSize() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    Set<Integer> result = SetOps.drop(numbers, 10);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should return all when n is zero")
  void testDropZero() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    Set<Integer> result = SetOps.drop(numbers, 0);

    assertThat(result).hasSize(3);
  }

  @Test
  @DisplayName("should handle empty set")
  void testDropEmptySet() {
    Set<Integer> empty = Set.of();

    Set<Integer> result = SetOps.drop(empty, 5);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testDropNullSet() {
    assertThatThrownBy(() -> SetOps.drop(null, 3))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw IllegalArgumentException when n is negative")
  void testDropNegative() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.drop(numbers, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("negative");
  }
}
