package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.filterMap")
class ListOpsFilterMapTest {

  @Test
  @DisplayName("should filter then map in single pass")
  void testFilterMap() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);

    List<Integer> result = ListOps.filterMap(numbers, n -> n % 2 == 0, n -> n * 10);

    assertThat(result).containsExactly(20, 40);
  }

  @Test
  @DisplayName("should handle empty list")
  void testFilterMapEmptyList() {
    List<Integer> empty = List.of();

    List<Integer> result = ListOps.filterMap(empty, n -> true, n -> n * 2);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should handle no matches after filter")
  void testFilterMapNoMatches() {
    List<Integer> numbers = List.of(1, 2, 3);

    List<Integer> result = ListOps.filterMap(numbers, n -> n > 100, n -> n * 2);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testFilterMapNullList() {
    assertThatThrownBy(() -> ListOps.filterMap(null, n -> true, n -> n))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFilterMapNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.filterMap(numbers, null, n -> n))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testFilterMapNullMapper() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.filterMap(numbers, n -> true, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }
}
