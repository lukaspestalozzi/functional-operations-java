package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.mapThenFilter")
class SetOpsMapThenFilterTest {

  @Test
  @DisplayName("should map then filter in single pass")
  void testMapThenFilter() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

    Set<Integer> result = SetOps.mapThenFilter(numbers, n -> n * 2, n -> n > 4);

    assertThat(result).containsExactlyInAnyOrder(6, 8, 10);
  }

  @Test
  @DisplayName("should handle empty set")
  void testMapThenFilterEmptySet() {
    Set<Integer> empty = Set.of();

    Set<Integer> result = SetOps.mapThenFilter(empty, n -> n * 2, n -> true);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should handle no matches after filter")
  void testMapThenFilterNoMatches() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    Set<Integer> result = SetOps.mapThenFilter(numbers, n -> n * 2, n -> n > 100);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testMapThenFilterNullSet() {
    assertThatThrownBy(() -> SetOps.mapThenFilter(null, n -> n, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testMapThenFilterNullMapper() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.mapThenFilter(numbers, null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testMapThenFilterNullPredicate() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.mapThenFilter(numbers, n -> n, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
