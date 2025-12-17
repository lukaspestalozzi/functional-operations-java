package com.github.lukaspestalozzi.functional.benchmarks;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import com.github.lukaspestalozzi.functional.ListOps;

/**
 * Memory allocation benchmarks for ListOps operations.
 *
 * <p>This benchmark specifically measures memory allocation per operation using JMH's GC profiler.
 * Run with: mvn clean test-compile -Pbenchmark && mvn exec:exec -Pbenchmark -Djmh.args="-prof gc"
 *
 * <p>Key metrics to monitor:
 *
 * <ul>
 *   <li>gc.alloc.rate.norm: Bytes allocated per operation
 *   <li>gc.count: Number of garbage collections
 * </ul>
 *
 * @author Lukas Pestalozzi
 */
@BenchmarkMode(Mode.SingleShotTime)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@State(Scope.Thread)
@Fork(value = 1)
@Warmup(iterations = 3, batchSize = 100)
@Measurement(iterations = 10, batchSize = 100)
public class MemoryAllocationBenchmark {

  private static final int LIST_SIZE = 10000;
  private List<Integer> testData;

  @Setup(Level.Iteration)
  public void setup() {
    testData = IntStream.range(0, LIST_SIZE).boxed().collect(Collectors.toList());
  }

  // ==================== Map Allocation ====================

  @Benchmark
  public void listOps_map_allocation(Blackhole bh) {
    List<Integer> result = ListOps.map(testData, n -> n * 2);
    bh.consume(result);
  }

  @Benchmark
  public void streams_map_allocation(Blackhole bh) {
    List<Integer> result = testData.stream().map(n -> n * 2).collect(Collectors.toList());
    bh.consume(result);
  }

  // ==================== Filter Allocation ====================

  @Benchmark
  public void listOps_filter_allocation(Blackhole bh) {
    List<Integer> result = ListOps.filter(testData, n -> n % 2 == 0);
    bh.consume(result);
  }

  @Benchmark
  public void streams_filter_allocation(Blackhole bh) {
    List<Integer> result = testData.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
    bh.consume(result);
  }

  // ==================== Chained Operations Allocation ====================

  @Benchmark
  public void listOps_chain_allocation(Blackhole bh) {
    List<Integer> result = ListOps.filter(ListOps.map(testData, n -> n * 2), n -> n % 4 == 0);
    bh.consume(result);
  }

  @Benchmark
  public void streams_chain_allocation(Blackhole bh) {
    List<Integer> result =
        testData.stream().map(n -> n * 2).filter(n -> n % 4 == 0).collect(Collectors.toList());
    bh.consume(result);
  }
}
