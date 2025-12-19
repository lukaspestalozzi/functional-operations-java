package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.reverse")
class SetOpsReverseTest {

  @Test
  @DisplayName("should return same elements (sets have no order)")
  void testReverse() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

    Set<Integer> result = SetOps.reverse(numbers);

    assertThat(result).containsExactlyInAnyOrder(1, 2, 3, 4, 5);
  }

  @Test
  @DisplayName("should handle empty set")
  void testReverseEmptySet() {
    Set<Integer> empty = Set.of();

    Set<Integer> result = SetOps.reverse(empty);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testReverseNullSet() {
    assertThatThrownBy(() -> SetOps.reverse(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("set");
  }
}
