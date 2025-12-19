package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.map")
class SetOpsMapTest {

  @Test
  @DisplayName("should transform all elements in a set")
  void testMap() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
    Function<Integer, Integer> doubler = n -> n * 2;

    Set<Integer> result = SetOps.map(numbers, doubler);

    assertThat(result).containsExactlyInAnyOrder(2, 4, 6, 8, 10);
  }

  @Test
  @DisplayName("should handle empty set")
  void testMapEmptySet() {
    Set<Integer> empty = Set.of();
    Function<Integer, String> toString = Object::toString;

    Set<String> result = SetOps.map(empty, toString);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testMapNullSet() {
    Function<Integer, Integer> identity = n -> n;

    assertThatThrownBy(() -> SetOps.map(null, identity))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("set");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testMapNullMapper() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.map(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }

  @Test
  @DisplayName("should handle type transformation")
  void testMapTypeTransformation() {
    Set<Integer> numbers = Set.of(1, 2, 3);
    Function<Integer, String> toHex = n -> "0x" + Integer.toHexString(n);

    Set<String> result = SetOps.map(numbers, toHex);

    assertThat(result).containsExactlyInAnyOrder("0x1", "0x2", "0x3");
  }
}
