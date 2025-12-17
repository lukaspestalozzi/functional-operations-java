package com.github.lukaspestalozzi.functional;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Functional operations for Java lists, implemented with normal for loops for performance. All
 * operations are designed to be memory-efficient and easy to use.
 *
 * <p>This class provides static utility methods for common functional programming operations on
 * lists, such as map, filter, reduce, etc. All methods create new lists rather than modifying the
 * input lists.
 *
 * @author Lukas Pestalozzi
 * @since 1.0.0
 */
public final class ListOps {

  /** Private constructor to prevent instantiation of utility class. */
  private ListOps() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Transforms each element in the input list using the provided mapper function.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3);
   * List<Integer> doubled = ListOps.map(numbers, n -> n * 2);
   * // Result: [2, 4, 6]
   * }</pre>
   *
   * @param <T> the type of elements in the input list
   * @param <R> the type of elements in the output list
   * @param list the input list to transform
   * @param mapper the function to apply to each element
   * @return a new list containing the transformed elements
   * @throws NullPointerException if list or mapper is null
   */
  public static <T, R> List<R> map(List<T> list, Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(list, "list must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    List<R> result = new ArrayList<>(list.size());
    for (int i = 0; i < list.size(); i++) {
      result.add(mapper.apply(list.get(i)));
    }
    return result;
  }

  /**
   * Filters elements in the input list based on the provided predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
   * List<Integer> evens = ListOps.filter(numbers, n -> n % 2 == 0);
   * // Result: [2, 4, 6]
   * }</pre>
   *
   * @param <T> the type of elements in the list
   * @param list the input list to filter
   * @param predicate the condition that elements must satisfy
   * @return a new list containing only elements that satisfy the predicate
   * @throws NullPointerException if list or predicate is null
   */
  public static <T> List<T> filter(List<T> list, Predicate<? super T> predicate) {
    Objects.requireNonNull(list, "list must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    List<T> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      T element = list.get(i);
      if (predicate.test(element)) {
        result.add(element);
      }
    }
    return result;
  }

  /**
   * Reduces the list to a single value by applying the accumulator function.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4);
   * Integer sum = ListOps.reduce(numbers, 0, Integer::sum);
   * // Result: 10
   * }</pre>
   *
   * @param <T> the type of elements in the list
   * @param <R> the type of the result
   * @param list the input list to reduce
   * @param identity the initial value
   * @param accumulator the function to combine elements
   * @return the reduced value
   * @throws NullPointerException if list or accumulator is null
   */
  public static <T, R> R reduce(List<T> list, R identity, BiFunction<R, ? super T, R> accumulator) {
    Objects.requireNonNull(list, "list must not be null");
    Objects.requireNonNull(accumulator, "accumulator must not be null");

    R result = identity;
    for (int i = 0; i < list.size(); i++) {
      result = accumulator.apply(result, list.get(i));
    }
    return result;
  }

  /**
   * Maps each element to a list and flattens the results into a single list.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<String> words = List.of("hello", "world");
   * List<Character> chars = ListOps.flatMap(words,
   *     s -> s.chars().mapToObj(c -> (char) c).toList());
   * // Result: ['h', 'e', 'l', 'l', 'o', 'w', 'o', 'r', 'l', 'd']
   * }</pre>
   *
   * @param <T> the type of elements in the input list
   * @param <R> the type of elements in the output list
   * @param list the input list
   * @param mapper the function that maps each element to a list
   * @return a new flattened list
   * @throws NullPointerException if list or mapper is null
   */
  public static <T, R> List<R> flatMap(
      List<T> list, Function<? super T, ? extends List<? extends R>> mapper) {
    Objects.requireNonNull(list, "list must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    List<R> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      List<? extends R> mapped = mapper.apply(list.get(i));
      if (mapped != null) {
        result.addAll(mapped);
      }
    }
    return result;
  }

  /**
   * Finds the first element matching the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * Optional<Integer> first = ListOps.find(numbers, n -> n > 3);
   * // Result: Optional[4]
   * }</pre>
   *
   * @param <T> the type of elements in the list
   * @param list the input list to search
   * @param predicate the condition to match
   * @return an Optional containing the first matching element, or empty if none found
   * @throws NullPointerException if list or predicate is null
   */
  public static <T> Optional<T> find(List<T> list, Predicate<? super T> predicate) {
    Objects.requireNonNull(list, "list must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (int i = 0; i < list.size(); i++) {
      T element = list.get(i);
      if (predicate.test(element)) {
        return Optional.of(element);
      }
    }
    return Optional.empty();
  }

  /**
   * Checks if any element in the list matches the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * boolean hasEven = ListOps.any(numbers, n -> n % 2 == 0);
   * // Result: true
   * }</pre>
   *
   * @param <T> the type of elements in the list
   * @param list the input list to check
   * @param predicate the condition to match
   * @return true if any element matches, false otherwise
   * @throws NullPointerException if list or predicate is null
   */
  public static <T> boolean any(List<T> list, Predicate<? super T> predicate) {
    Objects.requireNonNull(list, "list must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (int i = 0; i < list.size(); i++) {
      if (predicate.test(list.get(i))) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if all elements in the list match the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(2, 4, 6, 8);
   * boolean allEven = ListOps.all(numbers, n -> n % 2 == 0);
   * // Result: true
   * }</pre>
   *
   * @param <T> the type of elements in the list
   * @param list the input list to check
   * @param predicate the condition to match
   * @return true if all elements match, false otherwise (returns true for empty list)
   * @throws NullPointerException if list or predicate is null
   */
  public static <T> boolean all(List<T> list, Predicate<? super T> predicate) {
    Objects.requireNonNull(list, "list must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (int i = 0; i < list.size(); i++) {
      if (!predicate.test(list.get(i))) {
        return false;
      }
    }
    return true;
  }

  /**
   * Returns the first n elements of the list.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * List<Integer> firstThree = ListOps.take(numbers, 3);
   * // Result: [1, 2, 3]
   * }</pre>
   *
   * @param <T> the type of elements in the list
   * @param list the input list
   * @param n the number of elements to take
   * @return a new list containing the first n elements
   * @throws NullPointerException if list is null
   * @throws IllegalArgumentException if n is negative
   */
  public static <T> List<T> take(List<T> list, int n) {
    Objects.requireNonNull(list, "list must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    int count = Math.min(n, list.size());
    List<T> result = new ArrayList<>(count);
    for (int i = 0; i < count; i++) {
      result.add(list.get(i));
    }
    return result;
  }

  /**
   * Returns the list without the first n elements.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * List<Integer> afterTwo = ListOps.drop(numbers, 2);
   * // Result: [3, 4, 5]
   * }</pre>
   *
   * @param <T> the type of elements in the list
   * @param list the input list
   * @param n the number of elements to skip
   * @return a new list without the first n elements
   * @throws NullPointerException if list is null
   * @throws IllegalArgumentException if n is negative
   */
  public static <T> List<T> drop(List<T> list, int n) {
    Objects.requireNonNull(list, "list must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    int start = Math.min(n, list.size());
    List<T> result = new ArrayList<>(list.size() - start);
    for (int i = start; i < list.size(); i++) {
      result.add(list.get(i));
    }
    return result;
  }

  /**
   * Combines two lists element-wise using the provided combiner function.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> a = List.of(1, 2, 3);
   * List<Integer> b = List.of(10, 20, 30);
   * List<Integer> sums = ListOps.zip(a, b, Integer::sum);
   * // Result: [11, 22, 33]
   * }</pre>
   *
   * @param <A> the type of elements in the first list
   * @param <B> the type of elements in the second list
   * @param <R> the type of elements in the output list
   * @param listA the first list
   * @param listB the second list
   * @param combiner the function to combine elements
   * @return a new list with combined elements (length is minimum of both lists)
   * @throws NullPointerException if any argument is null
   */
  public static <A, B, R> List<R> zip(
      List<A> listA, List<B> listB, BiFunction<? super A, ? super B, ? extends R> combiner) {
    Objects.requireNonNull(listA, "listA must not be null");
    Objects.requireNonNull(listB, "listB must not be null");
    Objects.requireNonNull(combiner, "combiner must not be null");

    int size = Math.min(listA.size(), listB.size());
    List<R> result = new ArrayList<>(size);
    for (int i = 0; i < size; i++) {
      result.add(combiner.apply(listA.get(i), listB.get(i)));
    }
    return result;
  }

  /**
   * Returns a list with duplicate elements removed, preserving order.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 2, 3, 1, 4);
   * List<Integer> unique = ListOps.distinct(numbers);
   * // Result: [1, 2, 3, 4]
   * }</pre>
   *
   * @param <T> the type of elements in the list
   * @param list the input list
   * @return a new list with duplicates removed
   * @throws NullPointerException if list is null
   */
  public static <T> List<T> distinct(List<T> list) {
    Objects.requireNonNull(list, "list must not be null");

    return new ArrayList<>(new LinkedHashSet<>(list));
  }

  /**
   * Returns the list in reverse order.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * List<Integer> reversed = ListOps.reverse(numbers);
   * // Result: [5, 4, 3, 2, 1]
   * }</pre>
   *
   * @param <T> the type of elements in the list
   * @param list the input list
   * @return a new list with elements in reverse order
   * @throws NullPointerException if list is null
   */
  public static <T> List<T> reverse(List<T> list) {
    Objects.requireNonNull(list, "list must not be null");

    List<T> result = new ArrayList<>(list.size());
    for (int i = list.size() - 1; i >= 0; i--) {
      result.add(list.get(i));
    }
    return result;
  }

  /**
   * Partitions the list into two lists based on the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
   * List<List<Integer>> parts = ListOps.partition(numbers, n -> n % 2 == 0);
   * // Result: [[2, 4, 6], [1, 3, 5]]
   * }</pre>
   *
   * @param <T> the type of elements in the list
   * @param list the input list
   * @param predicate the condition to partition by
   * @return a list of two lists: [matching elements, non-matching elements]
   * @throws NullPointerException if list or predicate is null
   */
  public static <T> List<List<T>> partition(List<T> list, Predicate<? super T> predicate) {
    Objects.requireNonNull(list, "list must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    List<T> matching = new ArrayList<>();
    List<T> notMatching = new ArrayList<>();

    for (int i = 0; i < list.size(); i++) {
      T element = list.get(i);
      if (predicate.test(element)) {
        matching.add(element);
      } else {
        notMatching.add(element);
      }
    }

    List<List<T>> result = new ArrayList<>(2);
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
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * List<Integer> result = ListOps.mapFilter(numbers, n -> n * 2, n -> n > 4);
   * // Result: [6, 8, 10]
   * }</pre>
   *
   * @param <T> the type of elements in the input list
   * @param <R> the type of elements in the output list
   * @param list the input list
   * @param mapper the function to apply to each element
   * @param predicate the condition that mapped elements must satisfy
   * @return a new list with mapped and filtered elements
   * @throws NullPointerException if any argument is null
   */
  public static <T, R> List<R> mapFilter(
      List<T> list, Function<? super T, ? extends R> mapper, Predicate<? super R> predicate) {
    Objects.requireNonNull(list, "list must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    List<R> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      R mapped = mapper.apply(list.get(i));
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
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * List<Integer> result = ListOps.filterMap(numbers, n -> n % 2 == 0, n -> n * 10);
   * // Result: [20, 40]
   * }</pre>
   *
   * @param <T> the type of elements in the input list
   * @param <R> the type of elements in the output list
   * @param list the input list
   * @param predicate the condition that elements must satisfy
   * @param mapper the function to apply to filtered elements
   * @return a new list with filtered and mapped elements
   * @throws NullPointerException if any argument is null
   */
  public static <T, R> List<R> filterMap(
      List<T> list, Predicate<? super T> predicate, Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(list, "list must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    List<R> result = new ArrayList<>();
    for (int i = 0; i < list.size(); i++) {
      T element = list.get(i);
      if (predicate.test(element)) {
        result.add(mapper.apply(element));
      }
    }
    return result;
  }
}
