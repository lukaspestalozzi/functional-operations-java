# Functional Operations Java

[![CI](https://github.com/lukaspestalozzi/functional-operations-java/actions/workflows/ci.yml/badge.svg)](https://github.com/lukaspestalozzi/functional-operations-java/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

High-performance functional operations for Java collections. Uses simple for loops instead of streams for better performance and lower memory footprint.

## Features

- **ListOps**: map, filter (single/varargs), reduce, flatMap, find, any, all, none, take, drop, zip, distinct, reverse, partition, mapThenFilter, filterThenMap
- **SetOps**: Same operations for Sets, except reverse (all methods accept any Iterable as input)
- **MapOps**: mapValues, mapKeys, mapEntries, filter, filterKeys, filterValues, reduce, find, any, all, none, partition, merge, invert, flatMapValues, getOrCompute, take, drop

## Quick Start

```java
import dev.thelu.ListOps;
import dev.thelu.SetOps;
import dev.thelu.MapOps;

// List operations
List<Integer> doubled = ListOps.map(List.of(1, 2, 3), x -> x * 2);        // [2, 4, 6]
List<Integer> evens = ListOps.filter(List.of(1, 2, 3, 4), x -> x % 2 == 0); // [2, 4]
List<Integer> evenAndBig = ListOps.filter(List.of(1, 2, 3, 4, 5, 6),
    x -> x % 2 == 0, x -> x > 3);                                           // [4, 6]
int sum = ListOps.reduce(List.of(1, 2, 3), 0, Integer::sum);               // 6

// Map operations
Map<String, Integer> prices = Map.of("apple", 1, "banana", 2);
Map<String, Integer> doubled = MapOps.mapValues(prices, v -> v * 2);       // {apple=2, banana=4}
Map<String, Integer> cheap = MapOps.filterValues(prices, v -> v < 2);      // {apple=1}
```

## Requirements

- Java 17+
- Maven 3.8.1+ (or use included wrapper)

## Development Setup

```bash
# Clone the repository
git clone https://github.com/lukaspestalozzi/functional-operations-java.git
cd functional-operations-java

# Build and run tests
./mvnw clean verify

# Fix code formatting
./mvnw spotless:apply

# Run benchmarks
./run-benchmarks.sh --quick
```

## Project Structure

```
src/main/java/dev/thelu/
├── ListOps.java    # List operations (17 methods)
├── SetOps.java     # Set operations (16 methods)
└── MapOps.java     # Map operations (18 methods)

src/test/java/dev/thelu/
├── ListOps*Test.java   # One test file per ListOps method
├── SetOps*Test.java    # One test file per SetOps method
└── MapOps*Test.java    # One test file per MapOps method
```

## Documentation

- [requirements.md](requirements.md) - Functional requirements and API specification
- [CLAUDE.md](CLAUDE.md) - AI assistant guidance
- [BENCHMARKS.md](BENCHMARKS.md) - Performance benchmarks documentation

## License

MIT License - see [LICENSE](LICENSE) for details.
