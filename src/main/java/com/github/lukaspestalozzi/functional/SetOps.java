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
 * input collections. LinkedHashSet is used to preserve insertion order where applicable. Methods
 * accept any Iterable for maximum flexibility.
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
   * Transforms each element in the input iterable using the provided mapper function.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3);
   * Set<Integer> doubled = SetOps.map(numbers, n -> n * 2);
   * // Result: {2, 4, 6}
   * }</pre>
   *
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output set
   * @param iterable the input iterable to transform
   * @param mapper the function to apply to each element
   * @return a new set containing the transformed elements
   * @throws NullPointerException if iterable or mapper is null
   */
  public static <T, R> Set<R> map(
      Iterable<? extends T> iterable, Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    Set<R> result = new LinkedHashSet<>();
    for (T element : iterable) {
      result.add(mapper.apply(element));
    }
    return result;
  }

  /**
   * Filters elements in the input iterable based on the provided predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
   * Set<Integer> evens = SetOps.filter(numbers, n -> n % 2 == 0);
   * // Result: {2, 4, 6}
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to filter
   * @param predicate the condition that elements must satisfy
   * @return a new set containing only elements that satisfy the predicate
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> Set<T> filter(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    Set<T> result = new LinkedHashSet<>();
    for (T element : iterable) {
      if (predicate.test(element)) {
        result.add(element);
      }
    }
    return result;
  }

  /**
   * Filters elements in the input iterable based on multiple predicates (AND logic).
   *
   * <p>Elements must satisfy ALL predicates to be included in the result.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
   * Set<Integer> result = SetOps.filterAll(numbers,
   *     n -> n % 2 == 0,  // even
   *     n -> n > 4);      // greater than 4
   * // Result: {6, 8, 10}
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to filter
   * @param predicates the conditions that elements must all satisfy
   * @return a new set containing only elements that satisfy all predicates
   * @throws NullPointerException if iterable is null or any predicate is null
   */
  @SafeVarargs
  public static <T> Set<T> filterAll(
      Iterable<? extends T> iterable, Predicate<? super T>... predicates) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicates, "predicates must not be null");
    for (Predicate<? super T> predicate : predicates) {
      Objects.requireNonNull(predicate, "predicate must not be null");
    }

    Set<T> result = new LinkedHashSet<>();
    outer:
    for (T element : iterable) {
      for (Predicate<? super T> predicate : predicates) {
        if (!predicate.test(element)) {
          continue outer;
        }
      }
      result.add(element);
    }
    return result;
  }

  /**
   * Reduces the iterable to a single value by applying the accumulator function.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4);
   * Integer sum = SetOps.reduce(numbers, 0, Integer::sum);
   * // Result: 10
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param <R> the type of the result
   * @param iterable the input iterable to reduce
   * @param identity the initial value
   * @param accumulator the function to combine elements
   * @return the reduced value
   * @throws NullPointerException if iterable or accumulator is null
   */
  public static <T, R> R reduce(
      Iterable<? extends T> iterable, R identity, BiFunction<R, ? super T, R> accumulator) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(accumulator, "accumulator must not be null");

    R result = identity;
    for (T element : iterable) {
      result = accumulator.apply(result, element);
    }
    return result;
  }

  /**
   * Maps each element to an iterable and flattens the results into a single set.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3);
   * Set<Integer> result = SetOps.flatMap(numbers, n -> Set.of(n, n * 10));
   * // Result: {1, 10, 2, 20, 3, 30}
   * }</pre>
   *
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output set
   * @param iterable the input iterable
   * @param mapper the function that maps each element to an iterable
   * @return a new flattened set
   * @throws NullPointerException if iterable or mapper is null
   */
  public static <T, R> Set<R> flatMap(
      Iterable<? extends T> iterable, Function<? super T, ? extends Iterable<? extends R>> mapper) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    Set<R> result = new LinkedHashSet<>();
    for (T element : iterable) {
      Iterable<? extends R> mapped = mapper.apply(element);
      if (mapped != null) {
        for (R item : mapped) {
          result.add(item);
        }
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
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to search
   * @param predicate the condition to match
   * @return an Optional containing a matching element, or empty if none found
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> Optional<T> find(
      Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (T element : iterable) {
      if (predicate.test(element)) {
        return Optional.of(element);
      }
    }
    return Optional.empty();
  }

  /**
   * Checks if any element in the iterable matches the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * boolean hasEven = SetOps.any(numbers, n -> n % 2 == 0);
   * // Result: true
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to check
   * @param predicate the condition to match
   * @return true if any element matches, false otherwise
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> boolean any(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (T element : iterable) {
      if (predicate.test(element)) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if all elements in the iterable match the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(2, 4, 6, 8);
   * boolean allEven = SetOps.all(numbers, n -> n % 2 == 0);
   * // Result: true
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to check
   * @param predicate the condition to match
   * @return true if all elements match, false otherwise (returns true for empty iterable)
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> boolean all(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (T element : iterable) {
      if (!predicate.test(element)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns n elements from the iterable.
   *
   * <p>Note: The order of elements depends on the iterable's iteration order.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * Set<Integer> some = SetOps.take(numbers, 3);
   * // Result: 3 elements from the set
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @param n the number of elements to take
   * @return a new set containing n elements
   * @throws NullPointerException if iterable is null
   * @throws IllegalArgumentException if n is negative
   */
  public static <T> Set<T> take(Iterable<? extends T> iterable, int n) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    Set<T> result = new LinkedHashSet<>();
    int count = 0;
    for (T element : iterable) {
      if (count >= n) {
        break;
      }
      result.add(element);
      count++;
    }
    return result;
  }

  /**
   * Returns the iterable without the first n elements (iteration order).
   *
   * <p>Note: The order of elements depends on the iterable's iteration order.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * Set<Integer> remaining = SetOps.drop(numbers, 2);
   * // Result: set.size() - 2 elements
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @param n the number of elements to skip
   * @return a new set without the first n elements
   * @throws NullPointerException if iterable is null
   * @throws IllegalArgumentException if n is negative
   */
  public static <T> Set<T> drop(Iterable<? extends T> iterable, int n) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    Set<T> result = new LinkedHashSet<>();
    int count = 0;
    for (T element : iterable) {
      if (count >= n) {
        result.add(element);
      }
      count++;
    }
    return result;
  }

  /**
   * Combines two iterables element-wise using the provided combiner function.
   *
   * <p>Note: The pairing is based on iteration order.
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
   * @param <A> the type of elements in the first iterable
   * @param <B> the type of elements in the second iterable
   * @param <R> the type of elements in the output set
   * @param iterableA the first iterable
   * @param iterableB the second iterable
   * @param combiner the function to combine elements
   * @return a new set with combined elements (size is minimum of both iterables)
   * @throws NullPointerException if any argument is null
   */
  public static <A, B, R> Set<R> zip(
      Iterable<? extends A> iterableA,
      Iterable<? extends B> iterableB,
      BiFunction<? super A, ? super B, ? extends R> combiner) {
    Objects.requireNonNull(iterableA, "iterableA must not be null");
    Objects.requireNonNull(iterableB, "iterableB must not be null");
    Objects.requireNonNull(combiner, "combiner must not be null");

    Set<R> result = new LinkedHashSet<>();
    Iterator<? extends A> iterA = iterableA.iterator();
    Iterator<? extends B> iterB = iterableB.iterator();
    while (iterA.hasNext() && iterB.hasNext()) {
      result.add(combiner.apply(iterA.next(), iterB.next()));
    }
    return result;
  }

  /**
   * Returns a set with duplicate elements removed (sets are already distinct by definition).
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4);
   * Set<Integer> same = SetOps.distinct(numbers);
   * // Result: {1, 2, 3, 4}
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @return a new set with the same elements (duplicates removed if input is not a set)
   * @throws NullPointerException if iterable is null
   */
  public static <T> Set<T> distinct(Iterable<? extends T> iterable) {
    Objects.requireNonNull(iterable, "iterable must not be null");

    Set<T> result = new LinkedHashSet<>();
    for (T element : iterable) {
      result.add(element);
    }
    return result;
  }

  /**
   * Returns the iterable elements in a set (sets have no inherent order to reverse).
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);
   * Set<Integer> same = SetOps.reverse(numbers);
   * // Result: same elements, no order change
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @return a new set with the same elements
   * @throws NullPointerException if iterable is null
   */
  public static <T> Set<T> reverse(Iterable<? extends T> iterable) {
    Objects.requireNonNull(iterable, "iterable must not be null");

    Set<T> result = new LinkedHashSet<>();
    for (T element : iterable) {
      result.add(element);
    }
    return result;
  }

  /**
   * Partitions the iterable into two sets based on the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);
   * List<Set<Integer>> parts = SetOps.partition(numbers, n -> n % 2 == 0);
   * // Result: [{2, 4, 6}, {1, 3, 5}]
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @param predicate the condition to partition by
   * @return a list of two sets: [matching elements, non-matching elements]
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> List<Set<T>> partition(
      Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    Set<T> matching = new LinkedHashSet<>();
    Set<T> notMatching = new LinkedHashSet<>();

    for (T element : iterable) {
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
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output set
   * @param iterable the input iterable
   * @param mapper the function to apply to each element
   * @param predicate the condition that mapped elements must satisfy
   * @return a new set with mapped and filtered elements
   * @throws NullPointerException if any argument is null
   */
  public static <T, R> Set<R> mapFilter(
      Iterable<? extends T> iterable,
      Function<? super T, ? extends R> mapper,
      Predicate<? super R> predicate) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    Set<R> result = new LinkedHashSet<>();
    for (T element : iterable) {
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
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output set
   * @param iterable the input iterable
   * @param predicate the condition that elements must satisfy
   * @param mapper the function to apply to filtered elements
   * @return a new set with filtered and mapped elements
   * @throws NullPointerException if any argument is null
   */
  public static <T, R> Set<R> filterMap(
      Iterable<? extends T> iterable,
      Predicate<? super T> predicate,
      Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    Set<R> result = new LinkedHashSet<>();
    for (T element : iterable) {
      if (predicate.test(element)) {
        result.add(mapper.apply(element));
      }
    }
    return result;
  }
}
