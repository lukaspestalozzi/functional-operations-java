package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.distinct")
class ListOpsDistinctTest {

  @Test
  @DisplayName("should remove duplicate elements")
  void testDistinct() {
    List<Integer> numbers = List.of(1, 2, 2, 3, 1, 4, 3);

    List<Integer> result = ListOps.distinct(numbers);

    assertThat(result).containsExactly(1, 2, 3, 4);
  }

  @Test
  @DisplayName("should preserve order")
  void testDistinctPreservesOrder() {
    List<String> words = List.of("c", "a", "b", "a", "c");

    List<String> result = ListOps.distinct(words);

    assertThat(result).containsExactly("c", "a", "b");
  }

  @Test
  @DisplayName("should handle list with no duplicates")
  void testDistinctNoDuplicates() {
    List<Integer> numbers = List.of(1, 2, 3, 4);

    List<Integer> result = ListOps.distinct(numbers);

    assertThat(result).containsExactly(1, 2, 3, 4);
  }

  @Test
  @DisplayName("should handle empty list")
  void testDistinctEmptyList() {
    List<Integer> empty = List.of();

    List<Integer> result = ListOps.distinct(empty);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testDistinctNullList() {
    assertThatThrownBy(() -> ListOps.distinct(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("list");
  }
}
