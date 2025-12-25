package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.filterThenMap")
class SetOpsFilterThenMapTest {

  @Test
  @DisplayName("should filter then map in single pass")
  void testFilterThenMap() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

    Set<Integer> result = SetOps.filterThenMap(numbers, n -> n % 2 == 0, n -> n * 10);

    assertThat(result).containsExactlyInAnyOrder(20, 40);
  }

  @Test
  @DisplayName("should handle empty set")
  void testFilterThenMapEmptySet() {
    Set<Integer> empty = Set.of();

    Set<Integer> result = SetOps.filterThenMap(empty, n -> true, n -> n * 2);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should handle no matches after filter")
  void testFilterThenMapNoMatches() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    Set<Integer> result = SetOps.filterThenMap(numbers, n -> n > 100, n -> n * 2);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testFilterThenMapNullSet() {
    assertThatThrownBy(() -> SetOps.filterThenMap(null, n -> true, n -> n))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFilterThenMapNullPredicate() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.filterThenMap(numbers, null, n -> n))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testFilterThenMapNullMapper() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.filterThenMap(numbers, n -> true, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }
}
