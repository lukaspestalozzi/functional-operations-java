package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.all")
class ListOpsAllTest {

  @Test
  @DisplayName("should return true when all elements match")
  void testAllMatch() {
    List<Integer> numbers = List.of(2, 4, 6, 8);

    boolean result = ListOps.all(numbers, n -> n % 2 == 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should return false when not all elements match")
  void testAllNotMatch() {
    List<Integer> numbers = List.of(2, 4, 5, 8);

    boolean result = ListOps.all(numbers, n -> n % 2 == 0);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should return true for empty list")
  void testAllEmptyList() {
    List<Integer> empty = List.of();

    boolean result = ListOps.all(empty, n -> false);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testAllNullList() {
    assertThatThrownBy(() -> ListOps.all(null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testAllNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.all(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
