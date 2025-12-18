package com.github.lukaspestalozzi.functional;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Functional operations for Java sets, implemented with normal for loops for performance. All
 * operations are designed to be memory-efficient and easy to use.
 *
 * <p>This class provides static utility methods for common functional programming operations on
 * sets, such as map, filter, reduce, etc. All methods create new sets rather than modifying the
 * input sets. LinkedHashSet is used to preserve insertion order where applicable.
 *
 * @author Lukas Pestalozzi
 * @since 1.0.0
 */
public final class SetOps {

  /** Private constructor to prevent instantiation of utility class. */
  private SetOps() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Transforms each element in the input set using the provided mapper function.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3);
   * Set<Integer> doubled = SetOps.map(numbers, n -> n * 2);
   * // Result: {2, 4, 6}
   * }</pre>
   *
   * @param <T> the type of elements in the input set
   * @param <R> the type of elements in the output set
   * @param set the input set to transform
   * @param mapper the function to apply to each element
   * @return a new set containing the transformed elements
   * @throws NullPointerException if set or mapper is null
   */
  public static <T, R> Set<R> map(Set<T> set, Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(set, "set must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    Set<R> result = new LinkedHashSet<>(set.size());
    for (T element : set) {
      result.add(mapper.apply(element));
    }
    return result;
  }

  /**
   * Filters elements in the input set based on the provided predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
   * Set<Integer> evens = SetOps.filter(numbers, n -> n % 2 == 0);
   * // Result: {2, 4, 6}
   * }</pre>
   *
   * @param <T> the type of elements in the set
   * @param set the input set to filter
   * @param predicate the condition that elements must satisfy
   * @return a new set containing only elements that satisfy the predicate
   * @throws NullPointerException if set or predicate is null
   */
  public static <T> Set<T> filter(Set<T> set, Predicate<? super T> predicate) {
    Objects.requireNonNull(set, "set must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    Set<T> result = new LinkedHashSet<>();
    for (T element : set) {
      if (predicate.test(element)) {
        result.add(element);
      }
    }
    return result;
  }

  /**
   * Reduces the set to a single value by applying the accumulator function.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4);
   * Integer sum = SetOps.reduce(numbers, 0, Integer::sum);
   * // Result: 10
   * }</pre>
   *
   * @param <T> the type of elements in the set
   * @param <R> the type of the result
   * @param set the input set to reduce
   * @param identity the initial value
   * @param accumulator the function to combine elements
   * @return the reduced value
   * @throws NullPointerException if set or accumulator is null
   */
  public static <T, R> R reduce(Set<T> set, R identity, BiFunction<R, ? super T, R> accumulator) {
    Objects.requireNonNull(set, "set must not be null");
    Objects.requireNonNull(accumulator, "accumulator must not be null");

    R result = identity;
    for (T element : set) {
      result = accumulator.apply(result, element);
    }
    return result;
  }

  /**
   * Maps each element to a set and flattens the results into a single set.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3);
   * Set<Integer> result = SetOps.flatMap(numbers, n -> Set.of(n, n * 10));
   * // Result: {1, 10, 2, 20, 3, 30}
   * }</pre>
   *
   * @param <T> the type of elements in the input set
   * @param <R> the type of elements in the output set
   * @param set the input set
   * @param mapper the function that maps each element to a set
   * @return a new flattened set
   * @throws NullPointerException if set or mapper is null
   */
  public static <T, R> Set<R> flatMap(
      Set<T> set, Function<? super T, ? extends Set<? extends R>> mapper) {
    Objects.requireNonNull(set, "set must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    Set<R> result = new LinkedHashSet<>();
    for (T element : set) {
      Set<? extends R> mapped = mapper.apply(element);
      if (mapped != null) {
        result.addAll(mapped);
      }
    }
    return result;
  }

  /**
   * Finds an element matching the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * Optional<Integer> found = SetOps.find(numbers, n -> n > 3);
   * // Result: Optional[4] or Optional[5] (order not guaranteed)
   * }</pre>
   *
   * @param <T> the type of elements in the set
   * @param set the input set to search
   * @param predicate the condition to match
   * @return an Optional containing a matching element, or empty if none found
   * @throws NullPointerException if set or predicate is null
   */
  public static <T> Optional<T> find(Set<T> set, Predicate<? super T> predicate) {
    Objects.requireNonNull(set, "set must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (T element : set) {
      if (predicate.test(element)) {
        return Optional.of(element);
      }
    }
    return Optional.empty();
  }

  /**
   * Checks if any element in the set matches the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * boolean hasEven = SetOps.any(numbers, n -> n % 2 == 0);
   * // Result: true
   * }</pre>
   *
   * @param <T> the type of elements in the set
   * @param set the input set to check
   * @param predicate the condition to match
   * @return true if any element matches, false otherwise
   * @throws NullPointerException if set or predicate is null
   */
  public static <T> boolean any(Set<T> set, Predicate<? super T> predicate) {
    Objects.requireNonNull(set, "set must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (T element : set) {
      if (predicate.test(element)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if all elements in the set match the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(2, 4, 6, 8);
   * boolean allEven = SetOps.all(numbers, n -> n % 2 == 0);
   * // Result: true
   * }</pre>
   *
   * @param <T> the type of elements in the set
   * @param set the input set to check
   * @param predicate the condition to match
   * @return true if all elements match, false otherwise (returns true for empty set)
   * @throws NullPointerException if set or predicate is null
   */
  public static <T> boolean all(Set<T> set, Predicate<? super T> predicate) {
    Objects.requireNonNull(set, "set must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (T element : set) {
      if (!predicate.test(element)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns n elements from the set.
   *
   * <p>Note: Sets are unordered, so the elements returned are arbitrary.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * Set<Integer> some = SetOps.take(numbers, 3);
   * // Result: 3 elements from the set
   * }</pre>
   *
   * @param <T> the type of elements in the set
   * @param set the input set
   * @param n the number of elements to take
   * @return a new set containing n elements
   * @throws NullPointerException if set is null
   * @throws IllegalArgumentException if n is negative
   */
  public static <T> Set<T> take(Set<T> set, int n) {
    Objects.requireNonNull(set, "set must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    Set<T> result = new LinkedHashSet<>();
    int count = 0;
    for (T element : set) {
      if (count >= n) {
        break;
      }
      result.add(element);
      count++;
    }
    return result;
  }

  /**
   * Returns the set without the first n elements (iteration order).
   *
   * <p>Note: Sets are unordered, so the elements skipped are arbitrary.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * Set<Integer> remaining = SetOps.drop(numbers, 2);
   * // Result: set.size() - 2 elements
   * }</pre>
   *
   * @param <T> the type of elements in the set
   * @param set the input set
   * @param n the number of elements to skip
   * @return a new set without the first n elements
   * @throws NullPointerException if set is null
   * @throws IllegalArgumentException if n is negative
   */
  public static <T> Set<T> drop(Set<T> set, int n) {
    Objects.requireNonNull(set, "set must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    Set<T> result = new LinkedHashSet<>();
    int count = 0;
    for (T element : set) {
      if (count >= n) {
        result.add(element);
      }
      count++;
    }
    return result;
  }

  /**
   * Combines two sets element-wise using the provided combiner function.
   *
   * <p>Note: Sets are unordered, so the pairing is arbitrary based on iteration order.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> a = Set.of(1, 2, 3);
   * Set<Integer> b = Set.of(10, 20, 30);
   * Set<Integer> sums = SetOps.zip(a, b, Integer::sum);
   * // Result: set of combined elements
   * }</pre>
   *
   * @param <A> the type of elements in the first set
   * @param <B> the type of elements in the second set
   * @param <R> the type of elements in the output set
   * @param setA the first set
   * @param setB the second set
   * @param combiner the function to combine elements
   * @return a new set with combined elements (size is minimum of both sets)
   * @throws NullPointerException if any argument is null
   */
  public static <A, B, R> Set<R> zip(
      Set<A> setA, Set<B> setB, BiFunction<? super A, ? super B, ? extends R> combiner) {
    Objects.requireNonNull(setA, "setA must not be null");
    Objects.requireNonNull(setB, "setB must not be null");
    Objects.requireNonNull(combiner, "combiner must not be null");

    Set<R> result = new LinkedHashSet<>();
    Iterator<A> iterA = setA.iterator();
    Iterator<B> iterB = setB.iterator();
    while (iterA.hasNext() && iterB.hasNext()) {
      result.add(combiner.apply(iterA.next(), iterB.next()));
    }
    return result;
  }

  /**
   * Returns the set unchanged (sets are already distinct by definition).
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4);
   * Set<Integer> same = SetOps.distinct(numbers);
   * // Result: {1, 2, 3, 4}
   * }</pre>
   *
   * @param <T> the type of elements in the set
   * @param set the input set
   * @return a new set with the same elements
   * @throws NullPointerException if set is null
   */
  public static <T> Set<T> distinct(Set<T> set) {
    Objects.requireNonNull(set, "set must not be null");

    return new LinkedHashSet<>(set);
  }

  /**
   * Returns the set unchanged (sets have no inherent order to reverse).
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * Set<Integer> same = SetOps.reverse(numbers);
   * // Result: same elements, no order change
   * }</pre>
   *
   * @param <T> the type of elements in the set
   * @param set the input set
   * @return a new set with the same elements
   * @throws NullPointerException if set is null
   */
  public static <T> Set<T> reverse(Set<T> set) {
    Objects.requireNonNull(set, "set must not be null");

    return new LinkedHashSet<>(set);
  }

  /**
   * Partitions the set into two sets based on the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
   * List<Set<Integer>> parts = SetOps.partition(numbers, n -> n % 2 == 0);
   * // Result: [{2, 4, 6}, {1, 3, 5}]
   * }</pre>
   *
   * @param <T> the type of elements in the set
   * @param set the input set
   * @param predicate the condition to partition by
   * @return a list of two sets: [matching elements, non-matching elements]
   * @throws NullPointerException if set or predicate is null
   */
  public static <T> List<Set<T>> partition(Set<T> set, Predicate<? super T> predicate) {
    Objects.requireNonNull(set, "set must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    Set<T> matching = new LinkedHashSet<>();
    Set<T> notMatching = new LinkedHashSet<>();

    for (T element : set) {
      if (predicate.test(element)) {
        matching.add(element);
      } else {
        notMatching.add(element);
      }
    }

    List<Set<T>> result = new ArrayList<>(2);
    result.add(matching);
    result.add(notMatching);
    return result;
  }

  /**
   * Applies map then filter in a single pass for better performance.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * Set<Integer> result = SetOps.mapFilter(numbers, n -> n * 2, n -> n > 4);
   * // Result: {6, 8, 10}
   * }</pre>
   *
   * @param <T> the type of elements in the input set
   * @param <R> the type of elements in the output set
   * @param set the input set
   * @param mapper the function to apply to each element
   * @param predicate the condition that mapped elements must satisfy
   * @return a new set with mapped and filtered elements
   * @throws NullPointerException if any argument is null
   */
  public static <T, R> Set<R> mapFilter(
      Set<T> set, Function<? super T, ? extends R> mapper, Predicate<? super R> predicate) {
    Objects.requireNonNull(set, "set must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    Set<R> result = new LinkedHashSet<>();
    for (T element : set) {
      R mapped = mapper.apply(element);
      if (predicate.test(mapped)) {
        result.add(mapped);
      }
    }
    return result;
  }

  /**
   * Applies filter then map in a single pass for better performance.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * Set<Integer> result = SetOps.filterMap(numbers, n -> n % 2 == 0, n -> n * 10);
   * // Result: {20, 40}
   * }</pre>
   *
   * @param <T> the type of elements in the input set
   * @param <R> the type of elements in the output set
   * @param set the input set
   * @param predicate the condition that elements must satisfy
   * @param mapper the function to apply to filtered elements
   * @return a new set with filtered and mapped elements
   * @throws NullPointerException if any argument is null
   */
  public static <T, R> Set<R> filterMap(
      Set<T> set, Predicate<? super T> predicate, Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(set, "set must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    Set<R> result = new LinkedHashSet<>();
    for (T element : set) {
      if (predicate.test(element)) {
        result.add(mapper.apply(element));
      }
    }
    return result;
  }
}
