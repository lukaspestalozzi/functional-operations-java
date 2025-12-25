package dev.thelu;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.RandomAccess;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Functional operations for Java lists, implemented with normal for loops for performance. All
 * operations are designed to be memory-efficient and easy to use.
 *
 * <p>This class provides static utility methods for common functional programming operations on
 * lists, such as map, filter, reduce, etc. All methods create new lists rather than modifying the
 * input collections. Methods accept any Iterable for maximum flexibility.
 *
 * @author Lukas Pestalozzi
 * @since 1.0.0
 * @see <a href="../../../../../../requirements.md">requirements.md</a> for complete API
 *     specification
 */
public final class ListOps {

  private static final int DEFAULT_CAPACITY = 16;

  /** Private constructor to prevent instantiation of utility class. */
  private ListOps() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /**
   * Returns the size of the iterable if known, otherwise returns the default capacity. This allows
   * pre-sizing of result collections to avoid array resizing overhead.
   */
  private static int initialCapacity(Iterable<?> iterable) {
    return (iterable instanceof Collection) ? ((Collection<?>) iterable).size() : DEFAULT_CAPACITY;
  }

  /**
   * Returns true if the iterable supports fast random access (like ArrayList). Index-based
   * iteration is faster for these collections as it avoids iterator object creation.
   */
  private static boolean isRandomAccess(Iterable<?> iterable) {
    return iterable instanceof List && iterable instanceof RandomAccess;
  }

  /**
   * Transforms each element in the input iterable using the provided mapper function.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3);
   * List<Integer> doubled = ListOps.map(numbers, n -> n * 2);
   * // Result: [2, 4, 6]
   * }</pre>
   *
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output list
   * @param iterable the input iterable to transform
   * @param mapper the function to apply to each element
   * @return a new list containing the transformed elements
   * @throws NullPointerException if iterable or mapper is null
   */
  public static <T, R> List<R> map(
      Iterable<? extends T> iterable, Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    if (isRandomAccess(iterable)) {
      List<? extends T> list = (List<? extends T>) iterable;
      int size = list.size();
      List<R> result = new ArrayList<>(size);
      for (int i = 0; i < size; i++) {
        result.add(mapper.apply(list.get(i)));
      }
      return result;
    }

    List<R> result = new ArrayList<>(initialCapacity(iterable));
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
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
   * List<Integer> evens = ListOps.filter(numbers, n -> n % 2 == 0);
   * // Result: [2, 4, 6]
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to filter
   * @param predicate the condition that elements must satisfy
   * @return a new list containing only elements that satisfy the predicate
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> List<T> filter(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    if (isRandomAccess(iterable)) {
      List<? extends T> list = (List<? extends T>) iterable;
      int size = list.size();
      List<T> result = new ArrayList<>(size);
      for (int i = 0; i < size; i++) {
        T element = list.get(i);
        if (predicate.test(element)) {
          result.add(element);
        }
      }
      return result;
    }

    List<T> result = new ArrayList<>(initialCapacity(iterable));
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
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6, 7, 8, 9, 10);
   * List<Integer> result = ListOps.filter(numbers,
   *     n -> n % 2 == 0,  // even
   *     n -> n > 4);      // greater than 4
   * // Result: [6, 8, 10]
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to filter
   * @param predicates the conditions that elements must all satisfy
   * @return a new list containing only elements that satisfy all predicates
   * @throws NullPointerException if iterable is null or any predicate is null
   */
  @SafeVarargs
  public static <T> List<T> filter(
      Iterable<? extends T> iterable, Predicate<? super T>... predicates) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicates, "predicates must not be null");
    for (Predicate<? super T> predicate : predicates) {
      Objects.requireNonNull(predicate, "predicate must not be null");
    }

    List<T> result = new ArrayList<>(initialCapacity(iterable));
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
   * List<Integer> numbers = List.of(1, 2, 3, 4);
   * Integer sum = ListOps.reduce(numbers, 0, Integer::sum);
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

    if (isRandomAccess(iterable)) {
      List<? extends T> list = (List<? extends T>) iterable;
      R result = identity;
      for (int i = 0, size = list.size(); i < size; i++) {
        result = accumulator.apply(result, list.get(i));
      }
      return result;
    }

    R result = identity;
    for (T element : iterable) {
      result = accumulator.apply(result, element);
    }
    return result;
  }

  /**
   * Maps each element to an iterable and flattens the results into a single list.
   *
   * <p>If the mapper returns null for any element, that element is silently skipped (no elements
   * are added to the result for that input). This is consistent with Optional.flatMap behavior.
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
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output list
   * @param iterable the input iterable
   * @param mapper the function that maps each element to an iterable (may return null)
   * @return a new flattened list
   * @throws NullPointerException if iterable or mapper is null
   */
  public static <T, R> List<R> flatMap(
      Iterable<? extends T> iterable, Function<? super T, ? extends Iterable<? extends R>> mapper) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    List<R> result = new ArrayList<>(initialCapacity(iterable));
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
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to search
   * @param predicate the condition to match
   * @return an Optional containing the first matching element, or empty if none found
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> Optional<T> find(
      Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    if (isRandomAccess(iterable)) {
      List<? extends T> list = (List<? extends T>) iterable;
      for (int i = 0, size = list.size(); i < size; i++) {
        T element = list.get(i);
        if (predicate.test(element)) {
          return Optional.of(element);
        }
      }
      return Optional.empty();
    }

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
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * boolean hasEven = ListOps.any(numbers, n -> n % 2 == 0);
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

    if (isRandomAccess(iterable)) {
      List<? extends T> list = (List<? extends T>) iterable;
      for (int i = 0, size = list.size(); i < size; i++) {
        if (predicate.test(list.get(i))) {
          return true;
        }
      }
      return false;
    }

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
   * List<Integer> numbers = List.of(2, 4, 6, 8);
   * boolean allEven = ListOps.all(numbers, n -> n % 2 == 0);
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

    if (isRandomAccess(iterable)) {
      List<? extends T> list = (List<? extends T>) iterable;
      for (int i = 0, size = list.size(); i < size; i++) {
        if (!predicate.test(list.get(i))) {
          return false;
        }
      }
      return true;
    }

    for (T element : iterable) {
      if (!predicate.test(element)) {
        return false;
      }
    }
    return true;
  }

  /**
   * Checks if no elements in the iterable match the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 3, 5, 7);
   * boolean noEven = ListOps.none(numbers, n -> n % 2 == 0);
   * // Result: true
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to check
   * @param predicate the condition to match
   * @return true if no elements match, false otherwise (returns true for empty iterable)
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> boolean none(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    return !any(iterable, predicate);
  }

  /**
   * Returns the first n elements of the iterable.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * List<Integer> firstThree = ListOps.take(numbers, 3);
   * // Result: [1, 2, 3]
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @param n the number of elements to take
   * @return a new list containing the first n elements
   * @throws NullPointerException if iterable is null
   * @throws IllegalArgumentException if n is negative
   */
  public static <T> List<T> take(Iterable<? extends T> iterable, int n) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    int capacity =
        (iterable instanceof Collection)
            ? Math.min(n, ((Collection<?>) iterable).size())
            : Math.min(n, DEFAULT_CAPACITY);
    List<T> result = new ArrayList<>(capacity);
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
   * Returns the iterable without the first n elements.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * List<Integer> afterTwo = ListOps.drop(numbers, 2);
   * // Result: [3, 4, 5]
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @param n the number of elements to skip
   * @return a new list without the first n elements
   * @throws NullPointerException if iterable is null
   * @throws IllegalArgumentException if n is negative
   */
  public static <T> List<T> drop(Iterable<? extends T> iterable, int n) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    int capacity =
        (iterable instanceof Collection)
            ? Math.max(0, ((Collection<?>) iterable).size() - n)
            : DEFAULT_CAPACITY;
    List<T> result = new ArrayList<>(capacity);
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
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> a = List.of(1, 2, 3);
   * List<Integer> b = List.of(10, 20, 30);
   * List<Integer> sums = ListOps.zip(a, b, Integer::sum);
   * // Result: [11, 22, 33]
   * }</pre>
   *
   * @param <A> the type of elements in the first iterable
   * @param <B> the type of elements in the second iterable
   * @param <R> the type of elements in the output list
   * @param iterableA the first iterable
   * @param iterableB the second iterable
   * @param combiner the function to combine elements
   * @return a new list with combined elements (length is minimum of both iterables)
   * @throws NullPointerException if any argument is null
   */
  public static <A, B, R> List<R> zip(
      Iterable<? extends A> iterableA,
      Iterable<? extends B> iterableB,
      BiFunction<? super A, ? super B, ? extends R> combiner) {
    Objects.requireNonNull(iterableA, "iterableA must not be null");
    Objects.requireNonNull(iterableB, "iterableB must not be null");
    Objects.requireNonNull(combiner, "combiner must not be null");

    int capacity = Math.min(initialCapacity(iterableA), initialCapacity(iterableB));
    List<R> result = new ArrayList<>(capacity);
    Iterator<? extends A> iterA = iterableA.iterator();
    Iterator<? extends B> iterB = iterableB.iterator();
    while (iterA.hasNext() && iterB.hasNext()) {
      result.add(combiner.apply(iterA.next(), iterB.next()));
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
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @return a new list with duplicates removed
   * @throws NullPointerException if iterable is null
   */
  public static <T> List<T> distinct(Iterable<? extends T> iterable) {
    Objects.requireNonNull(iterable, "iterable must not be null");

    LinkedHashSet<T> seen = new LinkedHashSet<>();
    for (T element : iterable) {
      seen.add(element);
    }
    return new ArrayList<>(seen);
  }

  /**
   * Returns the iterable elements in reverse order.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * List<Integer> reversed = ListOps.reverse(numbers);
   * // Result: [5, 4, 3, 2, 1]
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @return a new list with elements in reverse order
   * @throws NullPointerException if iterable is null
   */
  public static <T> List<T> reverse(Iterable<? extends T> iterable) {
    Objects.requireNonNull(iterable, "iterable must not be null");

    List<T> result = new ArrayList<>(initialCapacity(iterable));
    for (T element : iterable) {
      result.add(element);
    }
    Collections.reverse(result);
    return result;
  }

  /**
   * Partitions the iterable into two lists based on the predicate.
   *
   * <p>Example usage:
   *
   * <pre>{@code
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
   * List<List<Integer>> parts = ListOps.partition(numbers, n -> n % 2 == 0);
   * // Result: [[2, 4, 6], [1, 3, 5]]
   * }</pre>
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @param predicate the condition to partition by
   * @return a list of two lists: [matching elements, non-matching elements]
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> List<List<T>> partition(
      Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    int halfCapacity = (initialCapacity(iterable) + 1) / 2;
    List<T> matching = new ArrayList<>(halfCapacity);
    List<T> notMatching = new ArrayList<>(halfCapacity);

    for (T element : iterable) {
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
   * List<Integer> result = ListOps.mapThenFilter(numbers, n -> n * 2, n -> n > 4);
   * // Result: [6, 8, 10]
   * }</pre>
   *
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output list
   * @param iterable the input iterable
   * @param mapper the function to apply to each element
   * @param predicate the condition that mapped elements must satisfy
   * @return a new list with mapped and filtered elements
   * @throws NullPointerException if any argument is null
   */
  public static <T, R> List<R> mapThenFilter(
      Iterable<? extends T> iterable,
      Function<? super T, ? extends R> mapper,
      Predicate<? super R> predicate) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    List<R> result = new ArrayList<>(initialCapacity(iterable));
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
   * List<Integer> numbers = List.of(1, 2, 3, 4, 5);
   * List<Integer> result = ListOps.filterThenMap(numbers, n -> n % 2 == 0, n -> n * 10);
   * // Result: [20, 40]
   * }</pre>
   *
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output list
   * @param iterable the input iterable
   * @param predicate the condition that elements must satisfy
   * @param mapper the function to apply to filtered elements
   * @return a new list with filtered and mapped elements
   * @throws NullPointerException if any argument is null
   */
  public static <T, R> List<R> filterThenMap(
      Iterable<? extends T> iterable,
      Predicate<? super T> predicate,
      Function<? super T, ? extends R> mapper) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    List<R> result = new ArrayList<>(initialCapacity(iterable));
    for (T element : iterable) {
      if (predicate.test(element)) {
        result.add(mapper.apply(element));
      }
    }
    return result;
  }
}
