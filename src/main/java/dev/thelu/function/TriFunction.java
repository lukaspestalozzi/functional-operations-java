package dev.thelu.function;

/**
 * Represents a function that accepts three arguments and produces a result.
 *
 * <p>This is a functional interface whose functional method is {@link #apply(Object, Object,
 * Object)}.
 *
 * @param <T> the type of the first argument
 * @param <U> the type of the second argument
 * @param <V> the type of the third argument
 * @param <R> the type of the result
 */
@FunctionalInterface
public interface TriFunction<T, U, V, R> {
  /**
   * Applies this function to the given arguments.
   *
   * @param t the first function argument
   * @param u the second function argument
   * @param v the third function argument
   * @return the function result
   */
  R apply(T t, U u, V v);
}
