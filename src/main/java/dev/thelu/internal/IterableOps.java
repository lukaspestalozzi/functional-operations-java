package dev.thelu.internal;

import java.util.Collection;
import java.util.Iterator;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Internal shared implementation for iterable operations. Used by ListOps and SetOps to reduce code
 * duplication.
 *
 * <p>This class is not part of the public API and may change without notice.
 */
public final class IterableOps {

  private static final int DEFAULT_CAPACITY = 16;

  private IterableOps() {}

  /** Returns the size of the iterable if known, otherwise returns the default capacity. */
  public static int initialCapacity(Iterable<?> iterable) {
    return (iterable instanceof Collection) ? ((Collection<?>) iterable).size() : DEFAULT_CAPACITY;
  }

  /** Transforms each element using the mapper function. */
  public static <T, R, C extends Collection<R>> C map(
      Iterable<? extends T> iterable,
      Function<? super T, ? extends R> mapper,
      Supplier<C> collectionFactory) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    C result = collectionFactory.get();
    for (T element : iterable) {
      result.add(mapper.apply(element));
    }
    return result;
  }

  /** Filters elements based on the predicate. */
  public static <T, C extends Collection<T>> C filter(
      Iterable<? extends T> iterable,
      Predicate<? super T> predicate,
      Supplier<C> collectionFactory) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    C result = collectionFactory.get();
    for (T element : iterable) {
      if (predicate.test(element)) {
        result.add(element);
      }
    }
    return result;
  }

  /** Filters elements based on multiple predicates (AND logic). */
  @SafeVarargs
  public static <T, C extends Collection<T>> C filterAll(
      Iterable<? extends T> iterable,
      Supplier<C> collectionFactory,
      Predicate<? super T>... predicates) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicates, "predicates must not be null");
    for (Predicate<? super T> predicate : predicates) {
      Objects.requireNonNull(predicate, "predicate must not be null");
    }

    C result = collectionFactory.get();
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

  /** Reduces the iterable to a single value. */
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

  /** Maps each element to an iterable and flattens the results. Null mapper results are skipped. */
  public static <T, R, C extends Collection<R>> C flatMap(
      Iterable<? extends T> iterable,
      Function<? super T, ? extends Iterable<? extends R>> mapper,
      Supplier<C> collectionFactory) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    C result = collectionFactory.get();
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

  /** Finds an element matching the predicate. */
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

  /** Checks if any element matches the predicate. */
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

  /** Checks if all elements match the predicate. */
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

  /** Checks if no elements match the predicate. */
  public static <T> boolean none(Iterable<? extends T> iterable, Predicate<? super T> predicate) {
    return !any(iterable, predicate);
  }

  /** Takes the first n elements. */
  public static <T, C extends Collection<T>> C take(
      Iterable<? extends T> iterable, int n, Supplier<C> collectionFactory) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    C result = collectionFactory.get();
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

  /** Drops the first n elements. */
  public static <T, C extends Collection<T>> C drop(
      Iterable<? extends T> iterable, int n, Supplier<C> collectionFactory) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    C result = collectionFactory.get();
    int count = 0;
    for (T element : iterable) {
      if (count >= n) {
        result.add(element);
      }
      count++;
    }
    return result;
  }

  /** Combines two iterables element-wise. */
  public static <A, B, R, C extends Collection<R>> C zip(
      Iterable<? extends A> iterableA,
      Iterable<? extends B> iterableB,
      BiFunction<? super A, ? super B, ? extends R> combiner,
      Supplier<C> collectionFactory) {
    Objects.requireNonNull(iterableA, "iterableA must not be null");
    Objects.requireNonNull(iterableB, "iterableB must not be null");
    Objects.requireNonNull(combiner, "combiner must not be null");

    C result = collectionFactory.get();
    Iterator<? extends A> iterA = iterableA.iterator();
    Iterator<? extends B> iterB = iterableB.iterator();
    while (iterA.hasNext() && iterB.hasNext()) {
      result.add(combiner.apply(iterA.next(), iterB.next()));
    }
    return result;
  }

  /** Removes duplicates. */
  public static <T, C extends Collection<T>> C distinct(
      Iterable<? extends T> iterable, Supplier<C> collectionFactory) {
    Objects.requireNonNull(iterable, "iterable must not be null");

    C result = collectionFactory.get();
    for (T element : iterable) {
      if (!result.contains(element)) {
        result.add(element);
      }
    }
    return result;
  }

  /** Applies map then filter in a single pass. */
  public static <T, R, C extends Collection<R>> C mapThenFilter(
      Iterable<? extends T> iterable,
      Function<? super T, ? extends R> mapper,
      Predicate<? super R> predicate,
      Supplier<C> collectionFactory) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    C result = collectionFactory.get();
    for (T element : iterable) {
      R mapped = mapper.apply(element);
      if (predicate.test(mapped)) {
        result.add(mapped);
      }
    }
    return result;
  }

  /** Applies filter then map in a single pass. */
  public static <T, R, C extends Collection<R>> C filterThenMap(
      Iterable<? extends T> iterable,
      Predicate<? super T> predicate,
      Function<? super T, ? extends R> mapper,
      Supplier<C> collectionFactory) {
    Objects.requireNonNull(iterable, "iterable must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    C result = collectionFactory.get();
    for (T element : iterable) {
      if (predicate.test(element)) {
        result.add(mapper.apply(element));
      }
    }
    return result;
  }
}
