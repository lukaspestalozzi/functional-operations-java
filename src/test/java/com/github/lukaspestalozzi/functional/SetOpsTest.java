package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/** Tests for SetOps functional operations. */
@DisplayName("SetOps Functional Operations")
class SetOpsTest {

  // ==================== Map Tests ====================

  @Nested
  @DisplayName("map")
  class MapTests {

    @Test
    @DisplayName("should transform all elements in a set")
    void testMap() {
      Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
      Function<Integer, Integer> doubler = n -> n * 2;

      Set<Integer> result = SetOps.map(numbers, doubler);

      assertThat(result).containsExactlyInAnyOrder(2, 4, 6, 8, 10);
    }

    @Test
    @DisplayName("should handle empty set")
    void testMapEmptySet() {
      Set<Integer> empty = Set.of();
      Function<Integer, String> toString = Object::toString;

      Set<String> result = SetOps.map(empty, toString);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testMapNullSet() {
      Function<Integer, Integer> identity = n -> n;

      assertThatThrownBy(() -> SetOps.map(null, identity))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw NullPointerException when mapper is null")
    void testMapNullMapper() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.map(numbers, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("mapper");
    }

    @Test
    @DisplayName("should handle type transformation")
    void testMapTypeTransformation() {
      Set<Integer> numbers = Set.of(1, 2, 3);
      Function<Integer, String> toHex = n -> "0x" + Integer.toHexString(n);

      Set<String> result = SetOps.map(numbers, toHex);

      assertThat(result).containsExactlyInAnyOrder("0x1", "0x2", "0x3");
    }
  }

  // ==================== Filter Tests ====================

  @Nested
  @DisplayName("filter")
  class FilterTests {

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
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testFilterNullPredicate() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.filter(numbers, null))
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
      Set<Integer> numbers = Set.of(1, 2, 3, 4);

      Integer result = SetOps.reduce(numbers, 0, Integer::sum);

      assertThat(result).isEqualTo(10);
    }

    @Test
    @DisplayName("should return identity for empty set")
    void testReduceEmptySet() {
      Set<Integer> empty = Set.of();

      Integer result = SetOps.reduce(empty, 42, Integer::sum);

      assertThat(result).isEqualTo(42);
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testReduceNullSet() {
      assertThatThrownBy(() -> SetOps.reduce(null, 0, Integer::sum))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw NullPointerException when accumulator is null")
    void testReduceNullAccumulator() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.reduce(numbers, 0, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("accumulator");
    }
  }

  // ==================== FlatMap Tests ====================

  @Nested
  @DisplayName("flatMap")
  class FlatMapTests {

    @Test
    @DisplayName("should flatten nested sets")
    void testFlatMap() {
      Set<Set<Integer>> nested = Set.of(Set.of(1, 2), Set.of(3, 4), Set.of(5));

      Set<Integer> result = SetOps.flatMap(nested, Function.identity());

      assertThat(result).containsExactlyInAnyOrder(1, 2, 3, 4, 5);
    }

    @Test
    @DisplayName("should map and flatten")
    void testFlatMapWithTransform() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      Set<Integer> result = SetOps.flatMap(numbers, n -> Set.of(n, n * 10));

      assertThat(result).containsExactlyInAnyOrder(1, 10, 2, 20, 3, 30);
    }

    @Test
    @DisplayName("should handle empty set")
    void testFlatMapEmptySet() {
      Set<Integer> empty = Set.of();

      Set<Integer> result = SetOps.flatMap(empty, n -> Set.of(n, n));

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should handle null results from mapper")
    void testFlatMapNullResult() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      Set<Integer> result = SetOps.flatMap(numbers, n -> n == 2 ? null : Set.of(n));

      assertThat(result).containsExactlyInAnyOrder(1, 3);
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testFlatMapNullSet() {
      assertThatThrownBy(() -> SetOps.flatMap(null, Function.identity()))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw NullPointerException when mapper is null")
    void testFlatMapNullMapper() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.flatMap(numbers, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("mapper");
    }
  }

  // ==================== Find Tests ====================

  @Nested
  @DisplayName("find")
  class FindTests {

    @Test
    @DisplayName("should find matching element")
    void testFind() {
      Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

      Optional<Integer> result = SetOps.find(numbers, n -> n > 3);

      assertThat(result).isPresent();
      assertThat(result.get()).isGreaterThan(3);
    }

    @Test
    @DisplayName("should return empty when no match")
    void testFindNoMatch() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      Optional<Integer> result = SetOps.find(numbers, n -> n > 10);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should return empty for empty set")
    void testFindEmptySet() {
      Set<Integer> empty = Set.of();

      Optional<Integer> result = SetOps.find(empty, n -> true);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testFindNullSet() {
      assertThatThrownBy(() -> SetOps.find(null, n -> true))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testFindNullPredicate() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.find(numbers, null))
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
      Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

      boolean result = SetOps.any(numbers, n -> n % 2 == 0);

      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should return false when no element matches")
    void testAnyNoMatch() {
      Set<Integer> numbers = Set.of(1, 3, 5, 7);

      boolean result = SetOps.any(numbers, n -> n % 2 == 0);

      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should return false for empty set")
    void testAnyEmptySet() {
      Set<Integer> empty = Set.of();

      boolean result = SetOps.any(empty, n -> true);

      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testAnyNullSet() {
      assertThatThrownBy(() -> SetOps.any(null, n -> true))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testAnyNullPredicate() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.any(numbers, null))
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
      Set<Integer> numbers = Set.of(2, 4, 6, 8);

      boolean result = SetOps.all(numbers, n -> n % 2 == 0);

      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should return false when not all elements match")
    void testAllNotMatch() {
      Set<Integer> numbers = Set.of(2, 4, 5, 8);

      boolean result = SetOps.all(numbers, n -> n % 2 == 0);

      assertThat(result).isFalse();
    }

    @Test
    @DisplayName("should return true for empty set")
    void testAllEmptySet() {
      Set<Integer> empty = Set.of();

      boolean result = SetOps.all(empty, n -> false);

      assertThat(result).isTrue();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testAllNullSet() {
      assertThatThrownBy(() -> SetOps.all(null, n -> true))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testAllNullPredicate() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.all(numbers, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("predicate");
    }
  }

  // ==================== Take Tests ====================

  @Nested
  @DisplayName("take")
  class TakeTests {

    @Test
    @DisplayName("should take n elements")
    void testTake() {
      Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

      Set<Integer> result = SetOps.take(numbers, 3);

      assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("should take all when n exceeds set size")
    void testTakeMoreThanSize() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      Set<Integer> result = SetOps.take(numbers, 10);

      assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("should return empty when n is zero")
    void testTakeZero() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      Set<Integer> result = SetOps.take(numbers, 0);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should handle empty set")
    void testTakeEmptySet() {
      Set<Integer> empty = Set.of();

      Set<Integer> result = SetOps.take(empty, 5);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testTakeNullSet() {
      assertThatThrownBy(() -> SetOps.take(null, 3))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when n is negative")
    void testTakeNegative() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.take(numbers, -1))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("negative");
    }
  }

  // ==================== Drop Tests ====================

  @Nested
  @DisplayName("drop")
  class DropTests {

    @Test
    @DisplayName("should drop n elements")
    void testDrop() {
      Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

      Set<Integer> result = SetOps.drop(numbers, 2);

      assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("should return empty when n exceeds set size")
    void testDropMoreThanSize() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      Set<Integer> result = SetOps.drop(numbers, 10);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should return all when n is zero")
    void testDropZero() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      Set<Integer> result = SetOps.drop(numbers, 0);

      assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("should handle empty set")
    void testDropEmptySet() {
      Set<Integer> empty = Set.of();

      Set<Integer> result = SetOps.drop(empty, 5);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testDropNullSet() {
      assertThatThrownBy(() -> SetOps.drop(null, 3))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw IllegalArgumentException when n is negative")
    void testDropNegative() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.drop(numbers, -1))
          .isInstanceOf(IllegalArgumentException.class)
          .hasMessageContaining("negative");
    }
  }

  // ==================== Zip Tests ====================

  @Nested
  @DisplayName("zip")
  class ZipTests {

    @Test
    @DisplayName("should combine two sets element-wise")
    void testZip() {
      Set<Integer> a = Set.of(1, 2, 3);
      Set<Integer> b = Set.of(10, 20, 30);

      Set<Integer> result = SetOps.zip(a, b, Integer::sum);

      assertThat(result).hasSize(3);
    }

    @Test
    @DisplayName("should use minimum size of both sets")
    void testZipDifferentSizes() {
      Set<Integer> a = Set.of(1, 2, 3, 4, 5);
      Set<Integer> b = Set.of(10, 20);

      Set<Integer> result = SetOps.zip(a, b, Integer::sum);

      assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("should handle different types")
    void testZipDifferentTypes() {
      Set<String> names = Set.of("Alice", "Bob");
      Set<Integer> ages = Set.of(25, 30);
      BiFunction<String, Integer, String> combiner = (name, age) -> name + ":" + age;

      Set<String> result = SetOps.zip(names, ages, combiner);

      assertThat(result).hasSize(2);
    }

    @Test
    @DisplayName("should return empty when first set is empty")
    void testZipFirstEmpty() {
      Set<Integer> empty = Set.of();
      Set<Integer> b = Set.of(1, 2, 3);

      Set<Integer> result = SetOps.zip(empty, b, Integer::sum);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should return empty when second set is empty")
    void testZipSecondEmpty() {
      Set<Integer> a = Set.of(1, 2, 3);
      Set<Integer> empty = Set.of();

      Set<Integer> result = SetOps.zip(a, empty, Integer::sum);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when first set is null")
    void testZipNullFirstSet() {
      Set<Integer> b = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.zip(null, b, Integer::sum))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("setA");
    }

    @Test
    @DisplayName("should throw NullPointerException when second set is null")
    void testZipNullSecondSet() {
      Set<Integer> a = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.zip(a, null, Integer::sum))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("setB");
    }

    @Test
    @DisplayName("should throw NullPointerException when combiner is null")
    void testZipNullCombiner() {
      Set<Integer> a = Set.of(1, 2, 3);
      Set<Integer> b = Set.of(4, 5, 6);

      assertThatThrownBy(() -> SetOps.zip(a, b, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("combiner");
    }
  }

  // ==================== Distinct Tests ====================

  @Nested
  @DisplayName("distinct")
  class DistinctTests {

    @Test
    @DisplayName("should return same elements (sets are already distinct)")
    void testDistinct() {
      Set<Integer> numbers = Set.of(1, 2, 3, 4);

      Set<Integer> result = SetOps.distinct(numbers);

      assertThat(result).containsExactlyInAnyOrder(1, 2, 3, 4);
    }

    @Test
    @DisplayName("should handle empty set")
    void testDistinctEmptySet() {
      Set<Integer> empty = Set.of();

      Set<Integer> result = SetOps.distinct(empty);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testDistinctNullSet() {
      assertThatThrownBy(() -> SetOps.distinct(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }
  }

  // ==================== Reverse Tests ====================

  @Nested
  @DisplayName("reverse")
  class ReverseTests {

    @Test
    @DisplayName("should return same elements (sets have no order)")
    void testReverse() {
      Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

      Set<Integer> result = SetOps.reverse(numbers);

      assertThat(result).containsExactlyInAnyOrder(1, 2, 3, 4, 5);
    }

    @Test
    @DisplayName("should handle empty set")
    void testReverseEmptySet() {
      Set<Integer> empty = Set.of();

      Set<Integer> result = SetOps.reverse(empty);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testReverseNullSet() {
      assertThatThrownBy(() -> SetOps.reverse(null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }
  }

  // ==================== Partition Tests ====================

  @Nested
  @DisplayName("partition")
  class PartitionTests {

    @Test
    @DisplayName("should partition set by predicate")
    void testPartition() {
      Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);

      List<Set<Integer>> result = SetOps.partition(numbers, n -> n % 2 == 0);

      assertThat(result).hasSize(2);
      assertThat(result.get(0)).containsExactlyInAnyOrder(2, 4, 6);
      assertThat(result.get(1)).containsExactlyInAnyOrder(1, 3, 5);
    }

    @Test
    @DisplayName("should handle all matching")
    void testPartitionAllMatch() {
      Set<Integer> numbers = Set.of(2, 4, 6);

      List<Set<Integer>> result = SetOps.partition(numbers, n -> n % 2 == 0);

      assertThat(result.get(0)).containsExactlyInAnyOrder(2, 4, 6);
      assertThat(result.get(1)).isEmpty();
    }

    @Test
    @DisplayName("should handle none matching")
    void testPartitionNoneMatch() {
      Set<Integer> numbers = Set.of(1, 3, 5);

      List<Set<Integer>> result = SetOps.partition(numbers, n -> n % 2 == 0);

      assertThat(result.get(0)).isEmpty();
      assertThat(result.get(1)).containsExactlyInAnyOrder(1, 3, 5);
    }

    @Test
    @DisplayName("should handle empty set")
    void testPartitionEmptySet() {
      Set<Integer> empty = Set.of();

      List<Set<Integer>> result = SetOps.partition(empty, n -> true);

      assertThat(result).hasSize(2);
      assertThat(result.get(0)).isEmpty();
      assertThat(result.get(1)).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testPartitionNullSet() {
      assertThatThrownBy(() -> SetOps.partition(null, n -> true))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testPartitionNullPredicate() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.partition(numbers, null))
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
      Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

      Set<Integer> result = SetOps.mapFilter(numbers, n -> n * 2, n -> n > 4);

      assertThat(result).containsExactlyInAnyOrder(6, 8, 10);
    }

    @Test
    @DisplayName("should handle empty set")
    void testMapFilterEmptySet() {
      Set<Integer> empty = Set.of();

      Set<Integer> result = SetOps.mapFilter(empty, n -> n * 2, n -> true);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should handle no matches after filter")
    void testMapFilterNoMatches() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      Set<Integer> result = SetOps.mapFilter(numbers, n -> n * 2, n -> n > 100);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testMapFilterNullSet() {
      assertThatThrownBy(() -> SetOps.mapFilter(null, n -> n, n -> true))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw NullPointerException when mapper is null")
    void testMapFilterNullMapper() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.mapFilter(numbers, null, n -> true))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("mapper");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testMapFilterNullPredicate() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.mapFilter(numbers, n -> n, null))
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
      Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

      Set<Integer> result = SetOps.filterMap(numbers, n -> n % 2 == 0, n -> n * 10);

      assertThat(result).containsExactlyInAnyOrder(20, 40);
    }

    @Test
    @DisplayName("should handle empty set")
    void testFilterMapEmptySet() {
      Set<Integer> empty = Set.of();

      Set<Integer> result = SetOps.filterMap(empty, n -> true, n -> n * 2);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should handle no matches after filter")
    void testFilterMapNoMatches() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      Set<Integer> result = SetOps.filterMap(numbers, n -> n > 100, n -> n * 2);

      assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("should throw NullPointerException when set is null")
    void testFilterMapNullSet() {
      assertThatThrownBy(() -> SetOps.filterMap(null, n -> true, n -> n))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("set");
    }

    @Test
    @DisplayName("should throw NullPointerException when predicate is null")
    void testFilterMapNullPredicate() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.filterMap(numbers, null, n -> n))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("predicate");
    }

    @Test
    @DisplayName("should throw NullPointerException when mapper is null")
    void testFilterMapNullMapper() {
      Set<Integer> numbers = Set.of(1, 2, 3);

      assertThatThrownBy(() -> SetOps.filterMap(numbers, n -> true, null))
          .isInstanceOf(NullPointerException.class)
          .hasMessageContaining("mapper");
    }
  }
}
