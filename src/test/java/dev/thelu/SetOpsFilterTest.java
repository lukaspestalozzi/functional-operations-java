package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.filter")
class SetOpsFilterTest {

  @Test
  @DisplayName("should keep only elements matching predicate")
  void testFilter() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
    Predicate<Integer> isEven = n -> n % 2 == 0;

    Set<Integer> result = SetOps.filter(numbers, isEven);

    assertThat(result).containsExactlyInAnyOrder(2, 4, 6);
  }

  @Test
  @DisplayName("should handle empty set")
  void testFilterEmptySet() {
    Set<Integer> empty = Set.of();
    Predicate<Integer> alwaysTrue = n -> true;

    Set<Integer> result = SetOps.filter(empty, alwaysTrue);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testFilterNullSet() {
    Predicate<Integer> alwaysTrue = n -> true;

    assertThatThrownBy(() -> SetOps.filter(null, alwaysTrue))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFilterNullPredicate() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.filter(numbers, (Predicate<Integer>) null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }

  @Test
  @DisplayName("should filter with multiple predicates using AND logic")
  void testFilterAllVarargs() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    Predicate<Integer> isEven = n -> n % 2 == 0;
    Predicate<Integer> greaterThan4 = n -> n > 4;

    Set<Integer> result = SetOps.filter(numbers, isEven, greaterThan4);

    assertThat(result).containsExactlyInAnyOrder(6, 8, 10);
  }

  @Test
  @DisplayName("should return all elements when no predicates provided")
  void testFilterAllVarargsEmpty() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    @SuppressWarnings("unchecked")
    Set<Integer> result = SetOps.filter(numbers, new Predicate[0]);

    assertThat(result).containsExactlyInAnyOrder(1, 2, 3);
  }

  @Test
  @DisplayName("should filter with single predicate in varargs")
  void testFilterAllVarargsSingle() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
    Predicate<Integer> isOdd = n -> n % 2 != 0;

    Set<Integer> result = SetOps.filter(numbers, isOdd);

    assertThat(result).containsExactlyInAnyOrder(1, 3, 5);
  }

  @Test
  @DisplayName("should throw NullPointerException when any predicate in varargs is null")
  void testFilterAllVarargsNullPredicate() {
    Set<Integer> numbers = Set.of(1, 2, 3);
    Predicate<Integer> isEven = n -> n % 2 == 0;

    assertThatThrownBy(() -> SetOps.filter(numbers, isEven, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicates array is null")
  void testFilterAllVarargsNullArray() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.filter(numbers, (Predicate<Integer>[]) null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicates");
  }
}
