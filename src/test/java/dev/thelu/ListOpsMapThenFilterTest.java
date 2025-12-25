package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.mapThenFilter")
class ListOpsMapThenFilterTest {

  @Test
  @DisplayName("should map then filter in single pass")
  void testMapThenFilter() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);

    List<Integer> result = ListOps.mapThenFilter(numbers, n -> n * 2, n -> n > 4);

    assertThat(result).containsExactly(6, 8, 10);
  }

  @Test
  @DisplayName("should handle empty list")
  void testMapThenFilterEmptyList() {
    List<Integer> empty = List.of();

    List<Integer> result = ListOps.mapThenFilter(empty, n -> n * 2, n -> true);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should handle no matches after filter")
  void testMapThenFilterNoMatches() {
    List<Integer> numbers = List.of(1, 2, 3);

    List<Integer> result = ListOps.mapThenFilter(numbers, n -> n * 2, n -> n > 100);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testMapThenFilterNullList() {
    assertThatThrownBy(() -> ListOps.mapThenFilter(null, n -> n, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testMapThenFilterNullMapper() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.mapThenFilter(numbers, null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testMapThenFilterNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.mapThenFilter(numbers, n -> n, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
