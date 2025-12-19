package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.filter")
class ListOpsFilterTest {

  @Test
  @DisplayName("should keep only elements matching predicate")
  void testFilter() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
    Predicate<Integer> isEven = n -> n % 2 == 0;

    List<Integer> result = ListOps.filter(numbers, isEven);

    assertThat(result).containsExactly(2, 4, 6);
  }

  @Test
  @DisplayName("should handle empty list")
  void testFilterEmptyList() {
    List<Integer> empty = List.of();
    Predicate<Integer> alwaysTrue = n -> true;

    List<Integer> result = ListOps.filter(empty, alwaysTrue);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testFilterNullList() {
    Predicate<Integer> alwaysTrue = n -> true;

    assertThatThrownBy(() -> ListOps.filter(null, alwaysTrue))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("list");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFilterNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.filter(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
