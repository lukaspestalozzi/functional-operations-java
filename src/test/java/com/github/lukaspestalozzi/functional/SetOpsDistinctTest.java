package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.distinct")
class SetOpsDistinctTest {

  @Test
  @DisplayName("should return same elements (sets are already distinct)")
  void testDistinct() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4);

    Set<Integer> result = SetOps.distinct(numbers);

    assertThat(result).containsExactlyInAnyOrder(1, 2, 3, 4);
  }

  @Test
  @DisplayName("should handle empty set")
  void testDistinctEmptySet() {
    Set<Integer> empty = Set.of();

    Set<Integer> result = SetOps.distinct(empty);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testDistinctNullSet() {
    assertThatThrownBy(() -> SetOps.distinct(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }
}
