package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.drop")
class ListOpsDropTest {

  @Test
  @DisplayName("should drop first n elements")
  void testDrop() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);

    List<Integer> result = ListOps.drop(numbers, 2);

    assertThat(result).containsExactly(3, 4, 5);
  }

  @Test
  @DisplayName("should return empty when n exceeds list size")
  void testDropMoreThanSize() {
    List<Integer> numbers = List.of(1, 2, 3);

    List<Integer> result = ListOps.drop(numbers, 10);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should return all when n is zero")
  void testDropZero() {
    List<Integer> numbers = List.of(1, 2, 3);

    List<Integer> result = ListOps.drop(numbers, 0);

    assertThat(result).containsExactly(1, 2, 3);
  }

  @Test
  @DisplayName("should handle empty list")
  void testDropEmptyList() {
    List<Integer> empty = List.of();

    List<Integer> result = ListOps.drop(empty, 5);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testDropNullList() {
    assertThatThrownBy(() -> ListOps.drop(null, 3))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw IllegalArgumentException when n is negative")
  void testDropNegative() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.drop(numbers, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("negative");
  }
}
