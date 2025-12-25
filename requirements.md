# Functional Requirements

Reference specification for the functional-operations-java library.

## Core Principles

1. **Performance First**: Use for loops instead of streams for optimal performance
2. **Immutability**: Never modify input collections; always return new collections
3. **Null Safety**: Validate inputs with clear error messages
4. **Type Safety**: Full generic type support with proper variance

## ListOps

Functional operations for `java.util.List`. All methods accept any `Iterable` for maximum flexibility.

| Method | Signature | Description |
|--------|-----------|-------------|
| `map` | `<T,R> List<R> map(Iterable<T>, Function<T,R>)` | Transform each element |
| `filter` | `<T> List<T> filter(Iterable<T>, Predicate<T>)` | Keep elements matching predicate |
| `filter` | `<T> List<T> filter(Iterable<T>, Predicate<T>...)` | Keep elements matching ALL predicates (varargs) |
| `reduce` | `<T,R> R reduce(Iterable<T>, R, BiFunction<R,T,R>)` | Fold to single value |
| `flatMap` | `<T,R> List<R> flatMap(Iterable<T>, Function<T,Iterable<R>>)` | Map and flatten |
| `find` | `<T> Optional<T> find(Iterable<T>, Predicate<T>)` | First matching element |
| `any` | `<T> boolean any(Iterable<T>, Predicate<T>)` | Any element matches |
| `all` | `<T> boolean all(Iterable<T>, Predicate<T>)` | All elements match |
| `take` | `<T> List<T> take(Iterable<T>, int)` | First n elements |
| `drop` | `<T> List<T> drop(Iterable<T>, int)` | Skip first n elements |
| `zip` | `<A,B,R> List<R> zip(Iterable<A>, Iterable<B>, BiFunction<A,B,R>)` | Combine two iterables |
| `distinct` | `<T> List<T> distinct(Iterable<T>)` | Remove duplicates (preserve order) |
| `reverse` | `<T> List<T> reverse(Iterable<T>)` | Reverse order |
| `partition` | `<T> List<List<T>> partition(Iterable<T>, Predicate<T>)` | Split by predicate |
| `mapFilter` | `<T,R> List<R> mapFilter(Iterable<T>, Function<T,R>, Predicate<R>)` | Map then filter |
| `filterMap` | `<T,R> List<R> filterMap(Iterable<T>, Predicate<T>, Function<T,R>)` | Filter then map |

## SetOps

Functional operations for `java.util.Set`. Similar to ListOps but returns `Set`. All methods accept any `Iterable` for maximum flexibility.

| Method | Signature | Description |
|--------|-----------|-------------|
| `map` | `<T,R> Set<R> map(Iterable<T>, Function<T,R>)` | Transform each element |
| `filter` | `<T> Set<T> filter(Iterable<T>, Predicate<T>)` | Keep elements matching predicate |
| `filter` | `<T> Set<T> filter(Iterable<T>, Predicate<T>...)` | Keep elements matching ALL predicates (varargs) |
| `reduce` | `<T,R> R reduce(Iterable<T>, R, BiFunction<R,T,R>)` | Fold to single value |
| `flatMap` | `<T,R> Set<R> flatMap(Iterable<T>, Function<T,Iterable<R>>)` | Map and flatten |
| `find` | `<T> Optional<T> find(Iterable<T>, Predicate<T>)` | Any matching element |
| `any` | `<T> boolean any(Iterable<T>, Predicate<T>)` | Any element matches |
| `all` | `<T> boolean all(Iterable<T>, Predicate<T>)` | All elements match |
| `take` | `<T> Set<T> take(Iterable<T>, int)` | Take n elements |
| `drop` | `<T> Set<T> drop(Iterable<T>, int)` | Drop n elements |
| `zip` | `<A,B,R> Set<R> zip(Iterable<A>, Iterable<B>, BiFunction<A,B,R>)` | Combine two iterables |
| `distinct` | `<T> Set<T> distinct(Iterable<T>)` | Remove duplicates (identity for sets) |
| `partition` | `<T> List<Set<T>> partition(Iterable<T>, Predicate<T>)` | Split by predicate |
| `mapFilter` | `<T,R> Set<R> mapFilter(Iterable<T>, Function<T,R>, Predicate<R>)` | Map then filter |
| `filterMap` | `<T,R> Set<R> filterMap(Iterable<T>, Predicate<T>, Function<T,R>)` | Filter then map |

**Note**: Uses `LinkedHashSet` internally to preserve insertion order where applicable. Unlike ListOps, SetOps does not have a `reverse()` method as it's not meaningful for sets.

## MapOps

Functional operations for `java.util.Map`.

| Method | Signature | Description |
|--------|-----------|-------------|
| `mapValues` | `<K,V,R> Map<K,R> mapValues(Map<K,V>, Function<V,R>)` | Transform values |
| `mapKeys` | `<K,V,R> Map<R,V> mapKeys(Map<K,V>, Function<K,R>)` | Transform keys |
| `mapEntries` | `<K,V,K2,V2> Map<K2,V2> mapEntries(Map<K,V>, Function<Entry,Entry>)` | Transform entries |
| `filter` | `<K,V> Map<K,V> filter(Map<K,V>, BiPredicate<K,V>)` | Filter by key+value |
| `filterKeys` | `<K,V> Map<K,V> filterKeys(Map<K,V>, Predicate<K>)` | Filter by key |
| `filterValues` | `<K,V> Map<K,V> filterValues(Map<K,V>, Predicate<V>)` | Filter by value |
| `reduce` | `<K,V,R> R reduce(Map<K,V>, R, TriFunction<R,K,V,R>)` | Fold to single value |
| `find` | `<K,V> Optional<Entry<K,V>> find(Map<K,V>, BiPredicate<K,V>)` | First matching entry |
| `any` | `<K,V> boolean any(Map<K,V>, BiPredicate<K,V>)` | Any entry matches |
| `all` | `<K,V> boolean all(Map<K,V>, BiPredicate<K,V>)` | All entries match |
| `partition` | `<K,V> List<Map<K,V>> partition(Map<K,V>, BiPredicate<K,V>)` | Split by predicate |
| `merge` | `<K,V> Map<K,V> merge(Map<K,V>, Map<K,V>, BiFunction<V,V,V>)` | Merge with combiner |
| `invert` | `<K,V> Map<V,K> invert(Map<K,V>)` | Swap keys and values |
| `flatMapValues` | `<K,V,R> List<Entry<K,R>> flatMapValues(Map<K,V>, Function<V,Collection<R>>)` | Flat map values |
| `getOrCompute` | `<K,V> V getOrCompute(Map<K,V>, K, Function<K,V>)` | Get or compute default |
| `take` | `<K,V> Map<K,V> take(Map<K,V>, int)` | Take n entries |
| `drop` | `<K,V> Map<K,V> drop(Map<K,V>, int)` | Drop n entries |

**Note**: Uses `LinkedHashMap` internally to preserve insertion order.

## Error Handling

All methods must:
- Throw `NullPointerException` with descriptive message when required parameters are null
- Throw `IllegalArgumentException` for invalid numeric arguments (e.g., negative counts)

## Test Requirements

- One test file per method (e.g., `ListOpsMapTest.java`)
- Minimum test cases per method:
  - Normal operation with typical input
  - Empty collection input
  - Null collection input (expect NPE)
  - Null function/predicate input (expect NPE)
  - Edge cases specific to the method

## Future Considerations

Potential additions for future versions:
- `groupBy`: Group elements by key function
- `sortBy`: Sort by comparator or key function
- `chunk`: Split into fixed-size chunks
- `interleave`: Interleave multiple collections
- `scan`: Running accumulation (like reduce but returns all intermediate values)
- `window`: Sliding window operations
