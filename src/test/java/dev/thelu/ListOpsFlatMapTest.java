package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.flatMap")
class ListOpsFlatMapTest {

  @Test
  @DisplayName("should flatten nested lists")
  void testFlatMap() {
    List<List<Integer>> nested = List.of(List.of(1, 2), List.of(3, 4), List.of(5));

    List<Integer> result = ListOps.flatMap(nested, Function.identity());

    assertThat(result).containsExactly(1, 2, 3, 4, 5);
  }

  @Test
  @DisplayName("should map and flatten")
  void testFlatMapWithTransform() {
    List<Integer> numbers = List.of(1, 2, 3);

    List<Integer> result = ListOps.flatMap(numbers, n -> List.of(n, n * 10));

    assertThat(result).containsExactly(1, 10, 2, 20, 3, 30);
  }

  @Test
  @DisplayName("should handle empty list")
  void testFlatMapEmptyList() {
    List<Integer> empty = List.of();

    List<Integer> result = ListOps.flatMap(empty, n -> List.of(n, n));

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should handle null results from mapper")
  void testFlatMapNullResult() {
    List<Integer> numbers = List.of(1, 2, 3);

    List<Integer> result = ListOps.flatMap(numbers, n -> n == 2 ? null : List.of(n));

    assertThat(result).containsExactly(1, 3);
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testFlatMapNullList() {
    assertThatThrownBy(() -> ListOps.flatMap(null, Function.identity()))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testFlatMapNullMapper() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.flatMap(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }
}
