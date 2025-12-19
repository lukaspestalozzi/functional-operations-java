# Functional Operations Java

[![CI](https://github.com/lukaspestalozzi/functional-operations-java/actions/workflows/ci.yml/badge.svg)](https://github.com/lukaspestalozzi/functional-operations-java/actions/workflows/ci.yml)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)

High-performance functional operations for Java collections. Uses simple for loops instead of streams for better performance and lower memory footprint.

## Features

- **ListOps**: map, filter, reduce, flatMap, find, any, all, take, drop, zip, distinct, reverse, partition, mapFilter, filterMap
- **SetOps**: Same operations for Sets
- **MapOps**: mapValues, mapKeys, filter, filterKeys, filterValues, reduce, find, any, all, partition, merge, invert, flatMapValues, getOrCompute, take, drop

## Quick Start

```java
import com.github.lukaspestalozzi.functional.ListOps;
import com.github.lukaspestalozzi.functional.SetOps;
import com.github.lukaspestalozzi.functional.MapOps;

// List operations
List<Integer> doubled = ListOps.map(List.of(1, 2, 3), x -> x * 2);        // [2, 4, 6]
List<Integer> evens = ListOps.filter(List.of(1, 2, 3, 4), x -> x % 2 == 0); // [2, 4]
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
src/main/java/com/github/lukaspestalozzi/functional/
├── ListOps.java    # List operations (15 methods)
├── SetOps.java     # Set operations (15 methods)
└── MapOps.java     # Map operations (17 methods)

src/test/java/com/github/lukaspestalozzi/functional/
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
