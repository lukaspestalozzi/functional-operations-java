package dev.thelu;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;

import dev.thelu.internal.IterableOps;

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
 * @see <a href="../../../../../../requirements.md">requirements.md</a> for complete API
 *     specification
 */
public final class SetOps {

  private static final int DEFAULT_CAPACITY = 16;

  /** Private constructor to prevent instantiation of utility class. */
  private SetOps() {
    throw new UnsupportedOperationException("Utility class cannot be instantiated");
  }

  /** Returns the size of the iterable if known, otherwise returns the default capacity. */
  private static int initialCapacity(Iterable<?> iterable) {
    return IterableOps.initialCapacity(iterable);
  }

  private static <T> LinkedHashSet<T> newSet(Iterable<?> iterable) {
    return new LinkedHashSet<>(initialCapacity(iterable));
  }

  /**
   * Transforms each element in the input iterable using the provided mapper function.
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
    return IterableOps.map(iterable, mapper, () -> newSet(iterable));
  }

  /**
   * Filters elements in the input iterable based on the provided predicate.
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to filter
   * @param predicate the condition that elements must satisfy
   * @return a new set containing only elements that satisfy the predicate
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> Set<T> filter(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    return IterableOps.filter(iterable, predicate, () -> newSet(iterable));
  }

  /**
   * Filters elements in the input iterable based on multiple predicates (AND logic).
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to filter
   * @param predicates the conditions that elements must all satisfy
   * @return a new set containing only elements that satisfy all predicates
   * @throws NullPointerException if iterable is null or any predicate is null
   */
  @SafeVarargs
  public static <T> Set<T> filter(
      Iterable<? extends T> iterable, Predicate<? super T>... predicates) {
    return IterableOps.filterAll(iterable, () -> newSet(iterable), predicates);
  }

  /**
   * Reduces the iterable to a single value by applying the accumulator function.
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
    return IterableOps.reduce(iterable, identity, accumulator);
  }

  /**
   * Maps each element to an iterable and flattens the results into a single set.
   *
   * <p>If the mapper returns null for any element, that element is silently skipped (no elements
   * are added to the result for that input). This is consistent with Optional.flatMap behavior.
   *
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output set
   * @param iterable the input iterable
   * @param mapper the function that maps each element to an iterable (may return null)
   * @return a new flattened set
   * @throws NullPointerException if iterable or mapper is null
   */
  public static <T, R> Set<R> flatMap(
      Iterable<? extends T> iterable, Function<? super T, ? extends Iterable<? extends R>> mapper) {
    return IterableOps.flatMap(iterable, mapper, () -> newSet(iterable));
  }

  /**
   * Finds an element matching the predicate.
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to search
   * @param predicate the condition to match
   * @return an Optional containing a matching element, or empty if none found
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> Optional<T> find(
      Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    return IterableOps.find(iterable, predicate);
  }

  /**
   * Checks if any element in the iterable matches the predicate.
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to check
   * @param predicate the condition to match
   * @return true if any element matches, false otherwise
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> boolean any(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    return IterableOps.any(iterable, predicate);
  }

  /**
   * Checks if all elements in the iterable match the predicate.
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to check
   * @param predicate the condition to match
   * @return true if all elements match, false otherwise (returns true for empty iterable)
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> boolean all(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    return IterableOps.all(iterable, predicate);
  }

  /**
   * Checks if no elements in the iterable match the predicate.
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable to check
   * @param predicate the condition to match
   * @return true if no elements match, false otherwise (returns true for empty iterable)
   * @throws NullPointerException if iterable or predicate is null
   */
  public static <T> boolean none(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    return IterableOps.none(iterable, predicate);
  }

  /**
   * Returns n elements from the iterable.
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @param n the number of elements to take
   * @return a new set containing n elements
   * @throws NullPointerException if iterable is null
   * @throws IllegalArgumentException if n is negative
   */
  public static <T> Set<T> take(Iterable<? extends T> iterable, int n) {
    return IterableOps.take(iterable, n, LinkedHashSet::new);
  }

  /**
   * Returns the iterable without the first n elements (iteration order).
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @param n the number of elements to skip
   * @return a new set without the first n elements
   * @throws NullPointerException if iterable is null
   * @throws IllegalArgumentException if n is negative
   */
  public static <T> Set<T> drop(Iterable<? extends T> iterable, int n) {
    return IterableOps.drop(iterable, n, LinkedHashSet::new);
  }

  /**
   * Combines two iterables element-wise using the provided combiner function.
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
    return IterableOps.zip(iterableA, iterableB, combiner, LinkedHashSet::new);
  }

  /**
   * Returns a set with duplicate elements removed (sets are already distinct by definition).
   *
   * @param <T> the type of elements in the iterable
   * @param iterable the input iterable
   * @return a new set with the same elements (duplicates removed if input is not a set)
   * @throws NullPointerException if iterable is null
   */
  public static <T> Set<T> distinct(Iterable<? extends T> iterable) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Set<T> result = newSet(iterable);
    for (T element : iterable) {
      result.add(element);
    }
    return result;
  }

  /**
   * Partitions the iterable into two sets based on the predicate.
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

    int halfCapacity = (initialCapacity(iterable) + 1) / 2;
    Set<T> matching = new LinkedHashSet<>(halfCapacity);
    Set<T> notMatching = new LinkedHashSet<>(halfCapacity);

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
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output set
   * @param iterable the input iterable
   * @param mapper the function to apply to each element
   * @param predicate the condition that mapped elements must satisfy
   * @return a new set with mapped and filtered elements
   * @throws NullPointerException if any argument is null
   */
  public static <T, R> Set<R> mapThenFilter(
      Iterable<? extends T> iterable,
      Function<? super T, ? extends R> mapper,
      Predicate<? super R> predicate) {
    return IterableOps.mapThenFilter(iterable, mapper, predicate, () -> newSet(iterable));
  }

  /**
   * Applies filter then map in a single pass for better performance.
   *
   * @param <T> the type of elements in the input iterable
   * @param <R> the type of elements in the output set
   * @param iterable the input iterable
   * @param predicate the condition that elements must satisfy
   * @param mapper the function to apply to filtered elements
   * @return a new set with filtered and mapped elements
   * @throws NullPointerException if any argument is null
   */
  public static <T, R> Set<R> filterThenMap(
      Iterable<? extends T> iterable,
      Predicate<? super T> predicate,
      Function<? super T, ? extends R> mapper) {
    return IterableOps.filterThenMap(iterable, predicate, mapper, () -> newSet(iterable));
  }
}
