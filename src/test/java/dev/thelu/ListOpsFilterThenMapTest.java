package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.filterThenMap")
class ListOpsFilterThenMapTest {

  @Test
  @DisplayName("should filter then map in single pass")
  void testFilterThenMap() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);

    List<Integer> result = ListOps.filterThenMap(numbers, n -> n % 2 == 0, n -> n * 10);

    assertThat(result).containsExactly(20, 40);
  }

  @Test
  @DisplayName("should handle empty list")
  void testFilterThenMapEmptyList() {
    List<Integer> empty = List.of();

    List<Integer> result = ListOps.filterThenMap(empty, n -> true, n -> n * 2);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should handle no matches after filter")
  void testFilterThenMapNoMatches() {
    List<Integer> numbers = List.of(1, 2, 3);

    List<Integer> result = ListOps.filterThenMap(numbers, n -> n > 100, n -> n * 2);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testFilterThenMapNullList() {
    assertThatThrownBy(() -> ListOps.filterThenMap(null, n -> true, n -> n))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFilterThenMapNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.filterThenMap(numbers, null, n -> n))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testFilterThenMapNullMapper() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.filterThenMap(numbers, n -> true, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }
}
