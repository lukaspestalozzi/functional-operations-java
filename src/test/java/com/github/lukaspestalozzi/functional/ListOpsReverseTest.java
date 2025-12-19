package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.reverse")
class ListOpsReverseTest {

  @Test
  @DisplayName("should reverse the list")
  void testReverse() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);

    List<Integer> result = ListOps.reverse(numbers);

    assertThat(result).containsExactly(5, 4, 3, 2, 1);
  }

  @Test
  @DisplayName("should handle single element")
  void testReverseSingleElement() {
    List<Integer> single = List.of(42);

    List<Integer> result = ListOps.reverse(single);

    assertThat(result).containsExactly(42);
  }

  @Test
  @DisplayName("should handle empty list")
  void testReverseEmptyList() {
    List<Integer> empty = List.of();

    List<Integer> result = ListOps.reverse(empty);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testReverseNullList() {
    assertThatThrownBy(() -> ListOps.reverse(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("list");
  }
}
