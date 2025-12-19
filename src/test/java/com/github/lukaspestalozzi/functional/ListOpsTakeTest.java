package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.take")
class ListOpsTakeTest {

  @Test
  @DisplayName("should take first n elements")
  void testTake() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);

    List<Integer> result = ListOps.take(numbers, 3);

    assertThat(result).containsExactly(1, 2, 3);
  }

  @Test
  @DisplayName("should take all when n exceeds list size")
  void testTakeMoreThanSize() {
    List<Integer> numbers = List.of(1, 2, 3);

    List<Integer> result = ListOps.take(numbers, 10);

    assertThat(result).containsExactly(1, 2, 3);
  }

  @Test
  @DisplayName("should return empty when n is zero")
  void testTakeZero() {
    List<Integer> numbers = List.of(1, 2, 3);

    List<Integer> result = ListOps.take(numbers, 0);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should handle empty list")
  void testTakeEmptyList() {
    List<Integer> empty = List.of();

    List<Integer> result = ListOps.take(empty, 5);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testTakeNullList() {
    assertThatThrownBy(() -> ListOps.take(null, 3))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("list");
  }

  @Test
  @DisplayName("should throw IllegalArgumentException when n is negative")
  void testTakeNegative() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.take(numbers, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("negative");
  }
}
