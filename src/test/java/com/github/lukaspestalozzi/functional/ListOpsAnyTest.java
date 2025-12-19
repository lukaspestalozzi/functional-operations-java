package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.any")
class ListOpsAnyTest {

  @Test
  @DisplayName("should return true when any element matches")
  void testAnyMatch() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);

    boolean result = ListOps.any(numbers, n -> n % 2 == 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should return false when no element matches")
  void testAnyNoMatch() {
    List<Integer> numbers = List.of(1, 3, 5, 7);

    boolean result = ListOps.any(numbers, n -> n % 2 == 0);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should return false for empty list")
  void testAnyEmptyList() {
    List<Integer> empty = List.of();

    boolean result = ListOps.any(empty, n -> true);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testAnyNullList() {
    assertThatThrownBy(() -> ListOps.any(null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("list");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testAnyNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.any(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
