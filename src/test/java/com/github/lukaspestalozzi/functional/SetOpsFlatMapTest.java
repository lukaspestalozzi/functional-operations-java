package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.flatMap")
class SetOpsFlatMapTest {

  @Test
  @DisplayName("should flatten nested sets")
  void testFlatMap() {
    Set<Set<Integer>> nested = Set.of(Set.of(1, 2), Set.of(3, 4), Set.of(5));

    Set<Integer> result = SetOps.flatMap(nested, Function.identity());

    assertThat(result).containsExactlyInAnyOrder(1, 2, 3, 4, 5);
  }

  @Test
  @DisplayName("should map and flatten")
  void testFlatMapWithTransform() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    Set<Integer> result = SetOps.flatMap(numbers, n -> Set.of(n, n * 10));

    assertThat(result).containsExactlyInAnyOrder(1, 10, 2, 20, 3, 30);
  }

  @Test
  @DisplayName("should handle empty set")
  void testFlatMapEmptySet() {
    Set<Integer> empty = Set.of();

    Set<Integer> result = SetOps.flatMap(empty, n -> Set.of(n, n));

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should handle null results from mapper")
  void testFlatMapNullResult() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    Set<Integer> result = SetOps.flatMap(numbers, n -> n == 2 ? null : Set.of(n));

    assertThat(result).containsExactlyInAnyOrder(1, 3);
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testFlatMapNullSet() {
    assertThatThrownBy(() -> SetOps.flatMap(null, Function.identity()))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testFlatMapNullMapper() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.flatMap(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }
}
