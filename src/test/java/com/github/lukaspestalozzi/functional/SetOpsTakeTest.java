package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.take")
class SetOpsTakeTest {

  @Test
  @DisplayName("should take n elements")
  void testTake() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

    Set<Integer> result = SetOps.take(numbers, 3);

    assertThat(result).hasSize(3);
  }

  @Test
  @DisplayName("should take all when n exceeds set size")
  void testTakeMoreThanSize() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    Set<Integer> result = SetOps.take(numbers, 10);

    assertThat(result).hasSize(3);
  }

  @Test
  @DisplayName("should return empty when n is zero")
  void testTakeZero() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    Set<Integer> result = SetOps.take(numbers, 0);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should handle empty set")
  void testTakeEmptySet() {
    Set<Integer> empty = Set.of();

    Set<Integer> result = SetOps.take(empty, 5);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testTakeNullSet() {
    assertThatThrownBy(() -> SetOps.take(null, 3))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("set");
  }

  @Test
  @DisplayName("should throw IllegalArgumentException when n is negative")
  void testTakeNegative() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.take(numbers, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("negative");
  }
}
