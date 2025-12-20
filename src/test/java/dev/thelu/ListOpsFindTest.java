package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.find")
class ListOpsFindTest {

  @Test
  @DisplayName("should find first matching element")
  void testFind() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);

    Optional<Integer> result = ListOps.find(numbers, n -> n > 3);

    assertThat(result).contains(4);
  }

  @Test
  @DisplayName("should return empty when no match")
  void testFindNoMatch() {
    List<Integer> numbers = List.of(1, 2, 3);

    Optional<Integer> result = ListOps.find(numbers, n -> n > 10);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should return empty for empty list")
  void testFindEmptyList() {
    List<Integer> empty = List.of();

    Optional<Integer> result = ListOps.find(empty, n -> true);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testFindNullList() {
    assertThatThrownBy(() -> ListOps.find(null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFindNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.find(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
