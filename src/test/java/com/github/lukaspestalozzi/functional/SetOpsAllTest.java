package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.all")
class SetOpsAllTest {

  @Test
  @DisplayName("should return true when all elements match")
  void testAllMatch() {
    Set<Integer> numbers = Set.of(2, 4, 6, 8);

    boolean result = SetOps.all(numbers, n -> n % 2 == 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should return false when not all elements match")
  void testAllNotMatch() {
    Set<Integer> numbers = Set.of(2, 4, 5, 8);

    boolean result = SetOps.all(numbers, n -> n % 2 == 0);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should return true for empty set")
  void testAllEmptySet() {
    Set<Integer> empty = Set.of();

    boolean result = SetOps.all(empty, n -> false);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testAllNullSet() {
    assertThatThrownBy(() -> SetOps.all(null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testAllNullPredicate() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.all(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
