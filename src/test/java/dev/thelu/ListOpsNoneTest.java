package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.none")
class ListOpsNoneTest {

  @Test
  @DisplayName("should return true when no elements match")
  void testNoneMatch() {
    List<Integer> numbers = List.of(1, 3, 5, 7);

    boolean result = ListOps.none(numbers, n -> n % 2 == 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should return false when any element matches")
  void testSomeMatch() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);

    boolean result = ListOps.none(numbers, n -> n % 2 == 0);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should return true for empty list")
  void testEmptyList() {
    List<Integer> empty = List.of();

    boolean result = ListOps.none(empty, n -> n > 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testNullList() {
    assertThatThrownBy(() -> ListOps.none(null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.none(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
