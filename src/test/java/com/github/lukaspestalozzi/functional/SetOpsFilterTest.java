package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.filter")
class SetOpsFilterTest {

  @Test
  @DisplayName("should keep only elements matching predicate")
  void testFilter() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
    Predicate<Integer> isEven = n -> n % 2 == 0;

    Set<Integer> result = SetOps.filter(numbers, isEven);

    assertThat(result).containsExactlyInAnyOrder(2, 4, 6);
  }

  @Test
  @DisplayName("should handle empty set")
  void testFilterEmptySet() {
    Set<Integer> empty = Set.of();
    Predicate<Integer> alwaysTrue = n -> true;

    Set<Integer> result = SetOps.filter(empty, alwaysTrue);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testFilterNullSet() {
    Predicate<Integer> alwaysTrue = n -> true;

    assertThatThrownBy(() -> SetOps.filter(null, alwaysTrue))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("set");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFilterNullPredicate() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.filter(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
