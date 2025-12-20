package dev.thelu;

import java.util.AbstractMap;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Functional operations for Maps. Uses for loops instead of streams for performance. All methods
 * return new maps, never modifying the input.
 */
public final class MapOps {

  private MapOps() {}

  /**
   * Transforms values in the map, keeping keys unchanged.
   *
   * @param map the input map
   * @param mapper function to transform each value
   * @param <K> key type
   * @param <V> original value type
   * @param <R> result value type
   * @return new map with transformed values
   */
  public static <K, V, R> Map<K, R> mapValues(
      Map<K, V> map, Function<? super V, ? extends R> mapper) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    Map<K, R> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : map.entrySet()) {
      result.put(entry.getKey(), mapper.apply(entry.getValue()));
    }
    return result;
  }

  /**
   * Transforms keys in the map, keeping values unchanged. If multiple keys map to the same new key,
   * the last value wins.
   *
   * @param map the input map
   * @param mapper function to transform each key
   * @param <K> original key type
   * @param <V> value type
   * @param <R> result key type
   * @return new map with transformed keys
   */
  public static <K, V, R> Map<R, V> mapKeys(
      Map<K, V> map, Function<? super K, ? extends R> mapper) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    Map<R, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : map.entrySet()) {
      result.put(mapper.apply(entry.getKey()), entry.getValue());
    }
    return result;
  }

  /**
   * Transforms entries in the map to new key-value pairs.
   *
   * @param map the input map
   * @param mapper function to transform each entry to a new entry
   * @param <K> original key type
   * @param <V> original value type
   * @param <K2> result key type
   * @param <V2> result value type
   * @return new map with transformed entries
   */
  public static <K, V, K2, V2> Map<K2, V2> mapEntries(
      Map<K, V> map, Function<Map.Entry<K, V>, Map.Entry<K2, V2>> mapper) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    Map<K2, V2> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : map.entrySet()) {
      Map.Entry<K2, V2> newEntry = mapper.apply(entry);
      if (newEntry != null) {
        result.put(newEntry.getKey(), newEntry.getValue());
      }
    }
    return result;
  }

  /**
   * Filters entries based on a predicate that takes both key and value.
   *
   * @param map the input map
   * @param predicate predicate to test each key-value pair
   * @param <K> key type
   * @param <V> value type
   * @return new map containing only entries matching the predicate
   */
  public static <K, V> Map<K, V> filter(
      Map<K, V> map, BiPredicate<? super K, ? super V> predicate) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    Map<K, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (predicate.test(entry.getKey(), entry.getValue())) {
        result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }

  /**
   * Filters entries based on a predicate on keys only.
   *
   * @param map the input map
   * @param predicate predicate to test each key
   * @param <K> key type
   * @param <V> value type
   * @return new map containing only entries whose keys match the predicate
   */
  public static <K, V> Map<K, V> filterKeys(Map<K, V> map, Predicate<? super K> predicate) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    Map<K, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (predicate.test(entry.getKey())) {
        result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }

  /**
   * Filters entries based on a predicate on values only.
   *
   * @param map the input map
   * @param predicate predicate to test each value
   * @param <K> key type
   * @param <V> value type
   * @return new map containing only entries whose values match the predicate
   */
  public static <K, V> Map<K, V> filterValues(Map<K, V> map, Predicate<? super V> predicate) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    Map<K, V> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (predicate.test(entry.getValue())) {
        result.put(entry.getKey(), entry.getValue());
      }
    }
    return result;
  }

  /**
   * Reduces the map entries to a single value.
   *
   * @param map the input map
   * @param identity the initial value
   * @param accumulator function to combine the accumulator with each key-value pair
   * @param <K> key type
   * @param <V> value type
   * @param <R> result type
   * @return the reduced value
   */
  public static <K, V, R> R reduce(
      Map<K, V> map, R identity, TriFunction<R, ? super K, ? super V, R> accumulator) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(accumulator, "accumulator must not be null");

    R result = identity;
    for (Map.Entry<K, V> entry : map.entrySet()) {
      result = accumulator.apply(result, entry.getKey(), entry.getValue());
    }
    return result;
  }

  /**
   * Finds the first entry matching the predicate.
   *
   * @param map the input map
   * @param predicate predicate to test each key-value pair
   * @param <K> key type
   * @param <V> value type
   * @return Optional containing the first matching entry, or empty if none match
   */
  public static <K, V> Optional<Map.Entry<K, V>> find(
      Map<K, V> map, BiPredicate<? super K, ? super V> predicate) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (predicate.test(entry.getKey(), entry.getValue())) {
        return Optional.of(
            new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), entry.getValue()));
      }
    }
    return Optional.empty();
  }

  /**
   * Checks if any entry matches the predicate.
   *
   * @param map the input map
   * @param predicate predicate to test each key-value pair
   * @param <K> key type
   * @param <V> value type
   * @return true if any entry matches, false otherwise
   */
  public static <K, V> boolean any(Map<K, V> map, BiPredicate<? super K, ? super V> predicate) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (predicate.test(entry.getKey(), entry.getValue())) {
        return true;
      }
    }
    return false;
  }

  /**
   * Checks if all entries match the predicate.
   *
   * @param map the input map
   * @param predicate predicate to test each key-value pair
   * @param <K> key type
   * @param <V> value type
   * @return true if all entries match, false otherwise
   */
  public static <K, V> boolean all(Map<K, V> map, BiPredicate<? super K, ? super V> predicate) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (!predicate.test(entry.getKey(), entry.getValue())) {
        return false;
      }
    }
    return true;
  }

  /**
   * Partitions the map into two maps based on a predicate. The first map contains entries matching
   * the predicate, the second contains entries not matching.
   *
   * @param map the input map
   * @param predicate predicate to partition by
   * @param <K> key type
   * @param <V> value type
   * @return list of two maps: [matching, non-matching]
   */
  public static <K, V> List<Map<K, V>> partition(
      Map<K, V> map, BiPredicate<? super K, ? super V> predicate) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(predicate, "predicate must not be null");

    Map<K, V> matching = new LinkedHashMap<>();
    Map<K, V> nonMatching = new LinkedHashMap<>();

    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (predicate.test(entry.getKey(), entry.getValue())) {
        matching.put(entry.getKey(), entry.getValue());
      } else {
        nonMatching.put(entry.getKey(), entry.getValue());
      }
    }

    List<Map<K, V>> result = new ArrayList<>(2);
    result.add(matching);
    result.add(nonMatching);
    return result;
  }

  /**
   * Merges two maps, using the combiner function to resolve conflicts when both maps have the same
   * key.
   *
   * @param map1 the first map
   * @param map2 the second map
   * @param combiner function to combine values when keys conflict
   * @param <K> key type
   * @param <V> value type
   * @return new map containing all entries from both maps
   */
  public static <K, V> Map<K, V> merge(
      Map<K, V> map1, Map<K, V> map2, BiFunction<? super V, ? super V, ? extends V> combiner) {
    Objects.requireNonNull(map1, "map1 must not be null");
    Objects.requireNonNull(map2, "map2 must not be null");
    Objects.requireNonNull(combiner, "combiner must not be null");

    Map<K, V> result = new LinkedHashMap<>(map1);
    for (Map.Entry<K, V> entry : map2.entrySet()) {
      K key = entry.getKey();
      V value = entry.getValue();
      if (result.containsKey(key)) {
        result.put(key, combiner.apply(result.get(key), value));
      } else {
        result.put(key, value);
      }
    }
    return result;
  }

  /**
   * Inverts the map, swapping keys and values. If multiple keys have the same value, the last key
   * wins.
   *
   * @param map the input map
   * @param <K> key type (becomes value type)
   * @param <V> value type (becomes key type)
   * @return new map with keys and values swapped
   */
  public static <K, V> Map<V, K> invert(Map<K, V> map) {
    Objects.requireNonNull(map, "map must not be null");

    Map<V, K> result = new LinkedHashMap<>();
    for (Map.Entry<K, V> entry : map.entrySet()) {
      result.put(entry.getValue(), entry.getKey());
    }
    return result;
  }

  /**
   * Flat maps over values, producing multiple values per key. Each value is mapped to a collection,
   * and all results are collected into a list of entries.
   *
   * @param map the input map
   * @param mapper function to map each value to a collection
   * @param <K> key type
   * @param <V> value type
   * @param <R> result value type
   * @return list of all key-value pairs after flat mapping
   */
  public static <K, V, R> List<Map.Entry<K, R>> flatMapValues(
      Map<K, V> map, Function<? super V, ? extends Collection<? extends R>> mapper) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(mapper, "mapper must not be null");

    List<Map.Entry<K, R>> result = new ArrayList<>();
    for (Map.Entry<K, V> entry : map.entrySet()) {
      Collection<? extends R> mapped = mapper.apply(entry.getValue());
      if (mapped != null) {
        for (R value : mapped) {
          result.add(new AbstractMap.SimpleImmutableEntry<>(entry.getKey(), value));
        }
      }
    }
    return result;
  }

  /**
   * Gets a value from the map, or computes a default if the key is absent. Unlike
   * Map.computeIfAbsent, this does not modify the original map.
   *
   * @param map the input map
   * @param key the key to look up
   * @param defaultMapper function to compute default value
   * @param <K> key type
   * @param <V> value type
   * @return the value if present, otherwise the computed default
   */
  public static <K, V> V getOrCompute(
      Map<K, V> map, K key, Function<? super K, ? extends V> defaultMapper) {
    Objects.requireNonNull(map, "map must not be null");
    Objects.requireNonNull(defaultMapper, "defaultMapper must not be null");

    V value = map.get(key);
    if (value != null) {
      return value;
    }
    if (map.containsKey(key)) {
      return null; // Key exists but value is null
    }
    return defaultMapper.apply(key);
  }

  /**
   * Takes the first n entries from the map. Since maps may not preserve insertion order depending
   * on implementation, this uses the iteration order.
   *
   * @param map the input map
   * @param n number of entries to take
   * @param <K> key type
   * @param <V> value type
   * @return new map with at most n entries
   */
  public static <K, V> Map<K, V> take(Map<K, V> map, int n) {
    Objects.requireNonNull(map, "map must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    Map<K, V> result = new LinkedHashMap<>();
    int count = 0;
    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (count >= n) {
        break;
      }
      result.put(entry.getKey(), entry.getValue());
      count++;
    }
    return result;
  }

  /**
   * Drops the first n entries from the map. Since maps may not preserve insertion order depending
   * on implementation, this uses the iteration order.
   *
   * @param map the input map
   * @param n number of entries to drop
   * @param <K> key type
   * @param <V> value type
   * @return new map without the first n entries
   */
  public static <K, V> Map<K, V> drop(Map<K, V> map, int n) {
    Objects.requireNonNull(map, "map must not be null");
    if (n < 0) {
      throw new IllegalArgumentException("n must not be negative");
    }

    Map<K, V> result = new LinkedHashMap<>();
    int count = 0;
    for (Map.Entry<K, V> entry : map.entrySet()) {
      if (count >= n) {
        result.put(entry.getKey(), entry.getValue());
      }
      count++;
    }
    return result;
  }

  /**
   * Functional interface for functions taking three arguments.
   *
   * @param <T> first argument type
   * @param <U> second argument type
   * @param <V> third argument type
   * @param <R> result type
   */
  @FunctionalInterface
  public interface TriFunction<T, U, V, R> {
    R apply(T t, U u, V v);
  }
}
