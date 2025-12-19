package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import java.util.function.BiFunction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.zip")
class SetOpsZipTest {

  @Test
  @DisplayName("should combine two sets element-wise")
  void testZip() {
    Set<Integer> a = Set.of(1, 2, 3);
    Set<Integer> b = Set.of(10, 20, 30);

    Set<Integer> result = SetOps.zip(a, b, Integer::sum);

    assertThat(result).hasSize(3);
  }

  @Test
  @DisplayName("should use minimum size of both sets")
  void testZipDifferentSizes() {
    Set<Integer> a = Set.of(1, 2, 3, 4, 5);
    Set<Integer> b = Set.of(10, 20);

    Set<Integer> result = SetOps.zip(a, b, Integer::sum);

    assertThat(result).hasSize(2);
  }

  @Test
  @DisplayName("should handle different types")
  void testZipDifferentTypes() {
    Set<String> names = Set.of("Alice", "Bob");
    Set<Integer> ages = Set.of(25, 30);
    BiFunction<String, Integer, String> combiner = (name, age) -> name + ":" + age;

    Set<String> result = SetOps.zip(names, ages, combiner);

    assertThat(result).hasSize(2);
  }

  @Test
  @DisplayName("should return empty when first set is empty")
  void testZipFirstEmpty() {
    Set<Integer> empty = Set.of();
    Set<Integer> b = Set.of(1, 2, 3);

    Set<Integer> result = SetOps.zip(empty, b, Integer::sum);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should return empty when second set is empty")
  void testZipSecondEmpty() {
    Set<Integer> a = Set.of(1, 2, 3);
    Set<Integer> empty = Set.of();

    Set<Integer> result = SetOps.zip(a, empty, Integer::sum);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when first set is null")
  void testZipNullFirstSet() {
    Set<Integer> b = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.zip(null, b, Integer::sum))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("setA");
  }

  @Test
  @DisplayName("should throw NullPointerException when second set is null")
  void testZipNullSecondSet() {
    Set<Integer> a = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.zip(a, null, Integer::sum))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("setB");
  }

  @Test
  @DisplayName("should throw NullPointerException when combiner is null")
  void testZipNullCombiner() {
    Set<Integer> a = Set.of(1, 2, 3);
    Set<Integer> b = Set.of(4, 5, 6);

    assertThatThrownBy(() -> SetOps.zip(a, b, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("combiner");
  }
}
