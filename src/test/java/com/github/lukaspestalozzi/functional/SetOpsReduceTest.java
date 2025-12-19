package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.reduce")
class SetOpsReduceTest {

  @Test
  @DisplayName("should sum all elements")
  void testReduceSum() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4);

    Integer result = SetOps.reduce(numbers, 0, Integer::sum);

    assertThat(result).isEqualTo(10);
  }

  @Test
  @DisplayName("should return identity for empty set")
  void testReduceEmptySet() {
    Set<Integer> empty = Set.of();

    Integer result = SetOps.reduce(empty, 42, Integer::sum);

    assertThat(result).isEqualTo(42);
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testReduceNullSet() {
    assertThatThrownBy(() -> SetOps.reduce(null, 0, Integer::sum))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when accumulator is null")
  void testReduceNullAccumulator() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.reduce(numbers, 0, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("accumulator");
  }
}
