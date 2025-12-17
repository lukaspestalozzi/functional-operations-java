package com.github.lukaspestalozzi.functional;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Functional operations for Java lists, implemented with normal for loops for performance.
 * All operations are designed to be memory-efficient and easy to use.
 *
 * <p>This class provides static utility methods for common functional programming operations
 * on lists, such as map, filter, etc. All methods create new lists rather than modifying
 * the input lists.</p>
 *
 * @author Lukas Pestalozzi
 * @since 1.0.0
 */
public final class ListOps {

    /**
     * Private constructor to prevent instantiation of utility class.
     */
    private ListOps() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    /**
     * Transforms each element in the input list using the provided mapper function.
     *
     * <p>This operation creates a new list containing the results of applying the mapper
     * to each element of the input list. The original list is not modified.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<Integer> numbers = List.of(1, 2, 3);
     * List<Integer> doubled = ListOps.map(numbers, n -> n * 2);
     * // Result: [2, 4, 6]
     * }</pre>
     *
     * @param <T>    the type of elements in the input list
     * @param <R>    the type of elements in the output list
     * @param list   the input list to transform
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
     * <p>This operation creates a new list containing only the elements from the input list
     * that satisfy the predicate. The original list is not modified.</p>
     *
     * <p>Example usage:</p>
     * <pre>{@code
     * List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);
     * List<Integer> evens = ListOps.filter(numbers, n -> n % 2 == 0);
     * // Result: [2, 4, 6]
     * }</pre>
     *
     * @param <T>       the type of elements in the list
     * @param list      the input list to filter
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
}
