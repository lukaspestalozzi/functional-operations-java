package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Test-driven development tests for ListOps functional operations. Following TDD principles: write
 * tests first, then implement to make them pass.
 */
@DisplayName("ListOps Functional Operations")
class ListOpsTest {

  // ==================== Map Tests ====================

  @Nested
  @DisplayName("map")
  class MapTests {

    @Test
    @DisplayName("should transform all elements in a list")
    void testMap() {
      List<Integer> numbers = List.of(1, 2, 3, 4, 5);
      Function<Integer, Integer> doubler = n -> n * 2;

      List<Integer> result = ListOps.map(numbers, doubler);

      assertThat(result).containsExactly(2, 4, 6, 8, 10);
    }

    @Test
    @DisplayName("should handle empty list")
    void testMapEmptyList() {
      List<Integer> empty = List.of();
      Function<Integer, String> toString = Object::toString;

      List<String> result = ListOps.map(empty, toString);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when list is null")
    void testMapNullList() {
      Function<Integer, Integer> identity = n -> n;

      assertThatThrownBy(() -> ListOps.map(null, identity))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("list");
    }

    @Test
    @DisplayName("should throw NullPointerException when mapper is null")
    void testMapNullMapper() {
      List<Integer> numbers = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.map(numbers, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("mapper");
    }

    @Test
    @DisplayName("should handle type transformation")
    void testMapTypeTransformation() {
      List<Integer> numbers = List.of(1, 2, 3);
      Function<Integer, String> toHex = n -> "0x" + Integer.toHexString(n);

      List<String> result = ListOps.map(numbers, toHex);

      assertThat(result).containsExactly("0x1", "0x2", "0x3");
    }
  }

  // ==================== Filter Tests ====================

  @Nested
  @DisplayName("filter")
  class FilterTests {

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
          .hasMessageContaining("list");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testFilterNullPredicate() {
      List<Integer> numbers = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.filter(numbers, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("predicate");
    }
  }

  // ==================== Reduce Tests ====================

  @Nested
  @DisplayName("reduce")
  class ReduceTests {

    @Test
    @DisplayName("should sum all elements")
    void testReduceSum() {
      List<Integer> numbers = List.of(1, 2, 3, 4);

      Integer result = ListOps.reduce(numbers, 0, Integer::sum);

      assertThat(result).isEqualTo(10);
    }

    @Test
    @DisplayName("should concatenate strings")
    void testReduceConcat() {
      List<String> words = List.of("a", "b", "c");

      String result = ListOps.reduce(words, "", (acc, s) -> acc + s);

      assertThat(result).isEqualTo("abc");
    }

    @Test
    @DisplayName("should return identity for empty list")
    void testReduceEmptyList() {
      List<Integer> empty = List.of();

      Integer result = ListOps.reduce(empty, 42, Integer::sum);

      assertThat(result).isEqualTo(42);
    }

    @Test
    @DisplayName("should throw NullPointerException when list is null")
    void testReduceNullList() {
      assertThatThrownBy(() -> ListOps.reduce(null, 0, Integer::sum))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("list");
    }

    @Test
    @DisplayName("should throw NullPointerException when accumulator is null")
    void testReduceNullAccumulator() {
      List<Integer> numbers = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.reduce(numbers, 0, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("accumulator");
    }
  }

  // ==================== FlatMap Tests ====================

  @Nested
  @DisplayName("flatMap")
  class FlatMapTests {

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
          .hasMessageContaining("list");
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

  // ==================== Find Tests ====================

  @Nested
  @DisplayName("find")
  class FindTests {

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
          .hasMessageContaining("list");
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

  // ==================== Any Tests ====================

  @Nested
  @DisplayName("any")
  class AnyTests {

    @Test
    @DisplayName("should return true when any element matches")
    void testAnyMatch() {
      List<Integer> numbers = List.of(1, 2, 3, 4, 5);

      boolean result = ListOps.any(numbers, n -> n % 2 == 0);

      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should return false when no element matches")
    void testAnyNoMatch() {
      List<Integer> numbers = List.of(1, 3, 5, 7);

      boolean result = ListOps.any(numbers, n -> n % 2 == 0);

      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should return false for empty list")
    void testAnyEmptyList() {
      List<Integer> empty = List.of();

      boolean result = ListOps.any(empty, n -> true);

      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should throw NullPointerException when list is null")
    void testAnyNullList() {
      assertThatThrownBy(() -> ListOps.any(null, n -> true))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("list");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testAnyNullPredicate() {
      List<Integer> numbers = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.any(numbers, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("predicate");
    }
  }

  // ==================== All Tests ====================

  @Nested
  @DisplayName("all")
  class AllTests {

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
          .hasMessageContaining("list");
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

  // ==================== Take Tests ====================

  @Nested
  @DisplayName("take")
  class TakeTests {

    @Test
    @DisplayName("should take first n elements")
    void testTake() {
      List<Integer> numbers = List.of(1, 2, 3, 4, 5);

      List<Integer> result = ListOps.take(numbers, 3);

      assertThat(result).containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("should take all when n exceeds list size")
    void testTakeMoreThanSize() {
      List<Integer> numbers = List.of(1, 2, 3);

      List<Integer> result = ListOps.take(numbers, 10);

      assertThat(result).containsExactly(1, 2, 3);
    }

    @Test
    @DisplayName("should return empty when n is zero")
    void testTakeZero() {
      List<Integer> numbers = List.of(1, 2, 3);

      List<Integer> result = ListOps.take(numbers, 0);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should handle empty list")
    void testTakeEmptyList() {
      List<Integer> empty = List.of();

      List<Integer> result = ListOps.take(empty, 5);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when list is null")
    void testTakeNullList() {
      assertThatThrownBy(() -> ListOps.take(null, 3))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("list");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when n is negative")
    void testTakeNegative() {
      List<Integer> numbers = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.take(numbers, -1))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("negative");
    }
  }

  // ==================== Drop Tests ====================

  @Nested
  @DisplayName("drop")
  class DropTests {

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
          .hasMessageContaining("list");
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

  // ==================== Zip Tests ====================

  @Nested
  @DisplayName("zip")
  class ZipTests {

    @Test
    @DisplayName("should combine two lists element-wise")
    void testZip() {
      List<Integer> a = List.of(1, 2, 3);
      List<Integer> b = List.of(10, 20, 30);

      List<Integer> result = ListOps.zip(a, b, Integer::sum);

      assertThat(result).containsExactly(11, 22, 33);
    }

    @Test
    @DisplayName("should use minimum length of both lists")
    void testZipDifferentLengths() {
      List<Integer> a = List.of(1, 2, 3, 4, 5);
      List<Integer> b = List.of(10, 20);

      List<Integer> result = ListOps.zip(a, b, Integer::sum);

      assertThat(result).containsExactly(11, 22);
    }

    @Test
    @DisplayName("should handle different types")
    void testZipDifferentTypes() {
      List<String> names = List.of("Alice", "Bob");
      List<Integer> ages = List.of(25, 30);
      BiFunction<String, Integer, String> combiner = (name, age) -> name + ":" + age;

      List<String> result = ListOps.zip(names, ages, combiner);

      assertThat(result).containsExactly("Alice:25", "Bob:30");
    }

    @Test
    @DisplayName("should return empty when first list is empty")
    void testZipFirstEmpty() {
      List<Integer> empty = List.of();
      List<Integer> b = List.of(1, 2, 3);

      List<Integer> result = ListOps.zip(empty, b, Integer::sum);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should return empty when second list is empty")
    void testZipSecondEmpty() {
      List<Integer> a = List.of(1, 2, 3);
      List<Integer> empty = List.of();

      List<Integer> result = ListOps.zip(a, empty, Integer::sum);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when first list is null")
    void testZipNullFirstList() {
      List<Integer> b = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.zip(null, b, Integer::sum))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("listA");
    }

    @Test
    @DisplayName("should throw NullPointerException when second list is null")
    void testZipNullSecondList() {
      List<Integer> a = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.zip(a, null, Integer::sum))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("listB");
    }

    @Test
    @DisplayName("should throw NullPointerException when combiner is null")
    void testZipNullCombiner() {
      List<Integer> a = List.of(1, 2, 3);
      List<Integer> b = List.of(4, 5, 6);

      assertThatThrownBy(() -> ListOps.zip(a, b, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("combiner");
    }
  }

  // ==================== Distinct Tests ====================

  @Nested
  @DisplayName("distinct")
  class DistinctTests {

    @Test
    @DisplayName("should remove duplicate elements")
    void testDistinct() {
      List<Integer> numbers = List.of(1, 2, 2, 3, 1, 4, 3);

      List<Integer> result = ListOps.distinct(numbers);

      assertThat(result).containsExactly(1, 2, 3, 4);
    }

    @Test
    @DisplayName("should preserve order")
    void testDistinctPreservesOrder() {
      List<String> words = List.of("c", "a", "b", "a", "c");

      List<String> result = ListOps.distinct(words);

      assertThat(result).containsExactly("c", "a", "b");
    }

    @Test
    @DisplayName("should handle list with no duplicates")
    void testDistinctNoDuplicates() {
      List<Integer> numbers = List.of(1, 2, 3, 4);

      List<Integer> result = ListOps.distinct(numbers);

      assertThat(result).containsExactly(1, 2, 3, 4);
    }

    @Test
    @DisplayName("should handle empty list")
    void testDistinctEmptyList() {
      List<Integer> empty = List.of();

      List<Integer> result = ListOps.distinct(empty);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when list is null")
    void testDistinctNullList() {
      assertThatThrownBy(() -> ListOps.distinct(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("list");
    }
  }

  // ==================== Reverse Tests ====================

  @Nested
  @DisplayName("reverse")
  class ReverseTests {

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

  // ==================== Partition Tests ====================

  @Nested
  @DisplayName("partition")
  class PartitionTests {

    @Test
    @DisplayName("should partition list by predicate")
    void testPartition() {
      List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

      List<List<Integer>> result = ListOps.partition(numbers, n -> n % 2 == 0);

      assertThat(result).hasSize(2);
      assertThat(result.get(0)).containsExactly(2, 4, 6);
      assertThat(result.get(1)).containsExactly(1, 3, 5);
    }

    @Test
    @DisplayName("should handle all matching")
    void testPartitionAllMatch() {
      List<Integer> numbers = List.of(2, 4, 6);

      List<List<Integer>> result = ListOps.partition(numbers, n -> n % 2 == 0);

      assertThat(result.get(0)).containsExactly(2, 4, 6);
      assertThat(result.get(1)).isEmpty();
    }

    @Test
    @DisplayName("should handle none matching")
    void testPartitionNoneMatch() {
      List<Integer> numbers = List.of(1, 3, 5);

      List<List<Integer>> result = ListOps.partition(numbers, n -> n % 2 == 0);

      assertThat(result.get(0)).isEmpty();
      assertThat(result.get(1)).containsExactly(1, 3, 5);
    }

    @Test
    @DisplayName("should handle empty list")
    void testPartitionEmptyList() {
      List<Integer> empty = List.of();

      List<List<Integer>> result = ListOps.partition(empty, n -> true);

      assertThat(result).hasSize(2);
      assertThat(result.get(0)).isEmpty();
      assertThat(result.get(1)).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when list is null")
    void testPartitionNullList() {
      assertThatThrownBy(() -> ListOps.partition(null, n -> true))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("list");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testPartitionNullPredicate() {
      List<Integer> numbers = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.partition(numbers, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("predicate");
    }
  }

  // ==================== MapFilter Tests ====================

  @Nested
  @DisplayName("mapFilter")
  class MapFilterTests {

    @Test
    @DisplayName("should map then filter in single pass")
    void testMapFilter() {
      List<Integer> numbers = List.of(1, 2, 3, 4, 5);

      List<Integer> result = ListOps.mapFilter(numbers, n -> n * 2, n -> n > 4);

      assertThat(result).containsExactly(6, 8, 10);
    }

    @Test
    @DisplayName("should handle empty list")
    void testMapFilterEmptyList() {
      List<Integer> empty = List.of();

      List<Integer> result = ListOps.mapFilter(empty, n -> n * 2, n -> true);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should handle no matches after filter")
    void testMapFilterNoMatches() {
      List<Integer> numbers = List.of(1, 2, 3);

      List<Integer> result = ListOps.mapFilter(numbers, n -> n * 2, n -> n > 100);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when list is null")
    void testMapFilterNullList() {
      assertThatThrownBy(() -> ListOps.mapFilter(null, n -> n, n -> true))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("list");
    }

    @Test
    @DisplayName("should throw NullPointerException when mapper is null")
    void testMapFilterNullMapper() {
      List<Integer> numbers = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.mapFilter(numbers, null, n -> true))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("mapper");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testMapFilterNullPredicate() {
      List<Integer> numbers = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.mapFilter(numbers, n -> n, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("predicate");
    }
  }

  // ==================== FilterMap Tests ====================

  @Nested
  @DisplayName("filterMap")
  class FilterMapTests {

    @Test
    @DisplayName("should filter then map in single pass")
    void testFilterMap() {
      List<Integer> numbers = List.of(1, 2, 3, 4, 5);

      List<Integer> result = ListOps.filterMap(numbers, n -> n % 2 == 0, n -> n * 10);

      assertThat(result).containsExactly(20, 40);
    }

    @Test
    @DisplayName("should handle empty list")
    void testFilterMapEmptyList() {
      List<Integer> empty = List.of();

      List<Integer> result = ListOps.filterMap(empty, n -> true, n -> n * 2);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should handle no matches after filter")
    void testFilterMapNoMatches() {
      List<Integer> numbers = List.of(1, 2, 3);

      List<Integer> result = ListOps.filterMap(numbers, n -> n > 100, n -> n * 2);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when list is null")
    void testFilterMapNullList() {
      assertThatThrownBy(() -> ListOps.filterMap(null, n -> true, n -> n))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("list");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testFilterMapNullPredicate() {
      List<Integer> numbers = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.filterMap(numbers, null, n -> n))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("predicate");
    }

    @Test
    @DisplayName("should throw NullPointerException when mapper is null")
    void testFilterMapNullMapper() {
      List<Integer> numbers = List.of(1, 2, 3);

      assertThatThrownBy(() -> ListOps.filterMap(numbers, n -> true, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("mapper");
    }
  }
}
