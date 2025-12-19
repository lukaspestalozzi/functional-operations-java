package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Optional;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.find")
class SetOpsFindTest {

  @Test
  @DisplayName("should find matching element")
  void testFind() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

    Optional<Integer> result = SetOps.find(numbers, n -> n > 3);

    assertThat(result).isPresent();
    assertThat(result.get()).isGreaterThan(3);
  }

  @Test
  @DisplayName("should return empty when no match")
  void testFindNoMatch() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    Optional<Integer> result = SetOps.find(numbers, n -> n > 10);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should return empty for empty set")
  void testFindEmptySet() {
    Set<Integer> empty = Set.of();

    Optional<Integer> result = SetOps.find(empty, n -> true);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testFindNullSet() {
    assertThatThrownBy(() -> SetOps.find(null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("set");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFindNullPredicate() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.find(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
