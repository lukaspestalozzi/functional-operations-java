# Performance Benchmarks

This document describes the performance benchmarking setup for the Functional Operations Java library. Performance is a key aspect of this library, measured in both execution speed and memory allocation.

## Overview

We use **JMH (Java Microbenchmark Harness)**, the industry-standard tool for Java performance testing. Our benchmark suite measures:

- **Throughput**: Operations per second (higher is better)
- **Average Time**: Microseconds per operation (lower is better)
- **Memory Allocation**: Bytes allocated per operation (lower is better)
- **Comparison**: Performance vs Java Streams API

## Key Performance Indicators (KPIs)

### Primary Metrics

1. **Throughput (ops/sec)**: Number of operations completed per second
2. **Memory Allocation (bytes/op)**: Memory allocated per operation
3. **Performance Ratio**: ListOps performance vs Java Streams (target: ≥ 1.0x)

### Regression Thresholds

- **Warning**: Performance degrades by >5%
- **Failure**: Performance degrades by >10%
- **Memory**: Allocation increases by >15%

## Running Benchmarks

### Quick Start

Use the provided script for easy benchmark execution:

```bash
# Full benchmark suite
./run-benchmarks.sh

# Quick mode (faster, fewer iterations)
./run-benchmarks.sh --quick

# With memory profiling
./run-benchmarks.sh --gc

# Quick mode with memory profiling
./run-benchmarks.sh -q -g
```

### Manual Execution

```bash
# Compile with benchmark profile
./mvnw clean test-compile -Pbenchmark

# Run all benchmarks
./mvnw exec:exec -Pbenchmark

# Run specific benchmark
./mvnw exec:exec -Pbenchmark \
  -Dexec.args="-classpath %classpath org.openjdk.jmh.Main ListOpsBenchmark.listOps_map"

# Run with GC profiler for memory tracking
./mvnw exec:exec -Pbenchmark \
  -Dexec.args="-classpath %classpath org.openjdk.jmh.Main .* -prof gc"
```

### Custom JMH Options

```bash
./mvnw exec:exec -Pbenchmark \
  -Dexec.args="-classpath %classpath org.openjdk.jmh.Main \
    .* \
    -wi 3 \           # Warmup iterations
    -i 5 \            # Measurement iterations
    -f 2 \            # Forks
    -t 1 \            # Threads
    -rf json \        # Result format
    -rff results.json" # Result file
```

## Benchmark Suite

### 1. ListOpsBenchmark

Tests core operations across different data sizes:

- **Data Sizes**: 10, 1,000, 100,000 elements
- **Operations**:
  - `map` - Transform elements
  - `filter` - Select elements
  - Chained operations (map → filter, filter → map)
  - Complex chains (multiple operations)
  - Edge cases (filter almost all/none)

Each operation is tested against Java Streams equivalent for comparison.

### 2. MemoryAllocationBenchmark

Focuses on memory allocation patterns:

- **Metrics**: `gc.alloc.rate.norm` (bytes allocated per operation)
- **Operations**: map, filter, and chained operations
- **Purpose**: Ensure memory efficiency

## CI/CD Integration

### Automated Benchmark Runs

Benchmarks run automatically on:

- **Pull Requests**: Quick benchmark on PR open/update
- **Manual Trigger**: Full benchmark suite via workflow dispatch

### GitHub Actions Workflow

```yaml
# Located at: .github/workflows/benchmark.yml
```

The workflow:

1. Runs benchmarks on PR commits
2. Generates JSON results
3. Uploads artifacts (retained for 30 days)
4. Comments on PR with summary
5. Runs separate memory allocation benchmarks

### Viewing Results

1. **GitHub Actions**: Check the "Performance Benchmarks" workflow
2. **Artifacts**: Download `benchmark-results-pr-*` from workflow run
3. **PR Comments**: View automated comment with summary

## Interpreting Results

### Example Output

```
Benchmark                                   (listSize)   Mode  Cnt      Score      Error   Units
ListOpsBenchmark.listOps_map                        10  thrpt   10  12345.678 ±  123.456  ops/us
ListOpsBenchmark.streams_map                        10  thrpt   10  10234.567 ±  102.345  ops/us
ListOpsBenchmark.listOps_map                      1000  thrpt   10    123.456 ±    1.234  ops/us
ListOpsBenchmark.streams_map                      1000  thrpt   10    101.234 ±    1.012  ops/us
```

### Analysis

- **Score**: Higher is better for throughput (ops/us)
- **Error**: Confidence interval (±)
- **Comparison**: `listOps_*` vs `streams_*` for same operation

In the example above, `listOps_map` is ~20% faster than `streams_map`.

### Memory Results

```
Benchmark                                   Mode  Cnt      Score      Error   Units
ListOpsBenchmark.listOps_map:gc.alloc.rate  thrpt   10   1234.567 ±   12.345  MB/sec
```

- **gc.alloc.rate.norm**: Bytes allocated per operation (lower is better)
- **gc.count**: Number of GC collections (lower is better)

## Best Practices

### When to Run Benchmarks

1. **Before PR**: Run locally to check for regressions
2. **After Changes**: Any performance-critical code changes
3. **Regular Baseline**: Establish performance baselines for each release

### Development Workflow

```bash
# 1. Make changes to ListOps
vim src/main/java/com/github/lukaspestalozzi/functional/ListOps.java

# 2. Run quick benchmark
./run-benchmarks.sh -q

# 3. Check results
cat target/jmh-result.json | jq '.[] | {benchmark: .benchmark, score: .primaryMetric.score}'

# 4. If good, commit and push (triggers CI benchmarks)
git add .
git commit -m "Optimize map operation"
git push
```

### Benchmark Development

When adding new operations to `ListOps`:

1. Add corresponding benchmark in `ListOpsBenchmark.java`
2. Add memory benchmark in `MemoryAllocationBenchmark.java`
3. Include Java Streams comparison
4. Test with multiple data sizes

## Troubleshooting

### Benchmarks Take Too Long

Use quick mode:

```bash
./run-benchmarks.sh -q
```

Or reduce iterations:

```bash
./mvnw exec:exec -Pbenchmark \
  -Dexec.args="-classpath %classpath org.openjdk.jmh.Main .* -wi 1 -i 2 -f 1"
```

### High Variance in Results

- Ensure no other intensive processes are running
- Increase warmup iterations: `-wi 5`
- Increase forks for more stable results: `-f 3`
- Close unnecessary applications

### Out of Memory

Increase JVM heap size:

```bash
# Edit pom.xml benchmark profile, change jvmArgs:
-Xms4G -Xmx4G
```

## Advanced Topics

### Custom Benchmarks

Create new benchmark classes in `src/test/java/.../benchmarks/`:

```java
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
public class MyBenchmark {

    @Param({"100", "1000"})
    private int size;

    @Benchmark
    public void myTest() {
        // Your benchmark code
    }
}
```

### Profilers

JMH supports various profilers:

```bash
# GC profiler (memory)
-prof gc

# Stack profiler (hotspots)
-prof stack

# Hardware counters (CPU cache, branch prediction)
-prof perfnorm
```

### Comparing Branches

```bash
# Baseline (main branch)
git checkout main
./run-benchmarks.sh
mv target/jmh-result.json baseline.json

# Feature branch
git checkout feature-branch
./run-benchmarks.sh
mv target/jmh-result.json feature.json

# Compare (requires jmh-compare tool or manual analysis)
```

## Resources

- [JMH Official Samples](http://hg.openjdk.java.net/code-tools/jmh/file/tip/jmh-samples/src/main/java/org/openjdk/jmh/samples/)
- [JMH Documentation](https://github.com/openjdk/jmh)
- [Avoiding Microbenchmark Pitfalls](https://shipilev.net/blog/2016/arrays-wisdom-ancients/)

## Performance Goals

| Operation | Target vs Streams | Current Status |
|-----------|-------------------|----------------|
| map       | ≥ 1.0x           | TBD            |
| filter    | ≥ 1.0x           | TBD            |
| chained   | ≥ 1.0x           | TBD            |

Update this table after establishing baseline measurements.
