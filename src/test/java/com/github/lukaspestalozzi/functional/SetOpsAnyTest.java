package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.any")
class SetOpsAnyTest {

  @Test
  @DisplayName("should return true when any element matches")
  void testAnyMatch() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

    boolean result = SetOps.any(numbers, n -> n % 2 == 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should return false when no element matches")
  void testAnyNoMatch() {
    Set<Integer> numbers = Set.of(1, 3, 5, 7);

    boolean result = SetOps.any(numbers, n -> n % 2 == 0);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should return false for empty set")
  void testAnyEmptySet() {
    Set<Integer> empty = Set.of();

    boolean result = SetOps.any(empty, n -> true);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testAnyNullSet() {
    assertThatThrownBy(() -> SetOps.any(null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testAnyNullPredicate() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.any(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
