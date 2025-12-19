package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.filter")
class ListOpsFilterTest {

  @Test
  @DisplayName("should keep only elements matching predicate")
  void testFilter() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
    Predicate<Integer> isEven = n -> n % 2 == 0;

    List<Integer> result = ListOps.filter(numbers, isEven);

    assertThat(result).containsExactly(2, 4, 6);
  }

  @Test
  @DisplayName("should handle empty list")
  void testFilterEmptyList() {
    List<Integer> empty = List.of();
    Predicate<Integer> alwaysTrue = n -> true;

    List<Integer> result = ListOps.filter(empty, alwaysTrue);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testFilterNullList() {
    Predicate<Integer> alwaysTrue = n -> true;

    assertThatThrownBy(() -> ListOps.filter(null, alwaysTrue))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFilterNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.filter(numbers, (Predicate<Integer>) null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }

  @Test
  @DisplayName("should filter with multiple predicates using AND logic")
  void testFilterAllVarargs() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
    Predicate<Integer> isEven = n -> n % 2 == 0;
    Predicate<Integer> greaterThan4 = n -> n > 4;

    List<Integer> result = ListOps.filter(numbers, isEven, greaterThan4);

    assertThat(result).containsExactly(6, 8, 10);
  }

  @Test
  @DisplayName("should return all elements when no predicates provided")
  void testFilterAllVarargsEmpty() {
    List<Integer> numbers = List.of(1, 2, 3);

    @SuppressWarnings("unchecked")
    List<Integer> result = ListOps.filter(numbers, new Predicate[0]);

    assertThat(result).containsExactly(1, 2, 3);
  }

  @Test
  @DisplayName("should filter with single predicate in varargs")
  void testFilterAllVarargsSingle() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);
    Predicate<Integer> isOdd = n -> n % 2 != 0;

    List<Integer> result = ListOps.filter(numbers, isOdd);

    assertThat(result).containsExactly(1, 3, 5);
  }

  @Test
  @DisplayName("should throw NullPointerException when any predicate in varargs is null")
  void testFilterAllVarargsNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);
    Predicate<Integer> isEven = n -> n % 2 == 0;

    assertThatThrownBy(() -> ListOps.filter(numbers, isEven, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicates array is null")
  void testFilterAllVarargsNullArray() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.filter(numbers, (Predicate<Integer>[]) null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicates");
  }
}
