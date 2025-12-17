package com.github.lukaspestalozzi.functional.benchmarks;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

import com.github.lukaspestalozzi.functional.ListOps;

/**
 * JMH Benchmarks for ListOps operations.
 *
 * <p>Measures both throughput (ops/sec) and memory allocation for map, filter, and chained
 * operations compared to Java Streams.
 *
 * <p>Run with: mvn clean test-compile -Pbenchmark && mvn exec:exec -Pbenchmark
 *
 * @author Lukas Pestalozzi
 */
@BenchmarkMode({Mode.Throughput, Mode.AverageTime})
@OutputTimeUnit(TimeUnit.MICROSECONDS)
@State(Scope.Benchmark)
@Fork(value = 2, jvmArgs = {"-Xms2G", "-Xmx2G"})
@Warmup(iterations = 3, time = 2)
@Measurement(iterations = 5, time = 2)
public class ListOpsBenchmark {

  @Param({"10", "1000", "100000"})
  private int listSize;

  private List<Integer> testData;

  @Setup
  public void setup() {
    testData = IntStream.range(0, listSize).boxed().collect(Collectors.toList());
  }

  // ==================== Map Operation Benchmarks ====================

  @Benchmark
  public void listOps_map(Blackhole bh) {
    List<Integer> result = ListOps.map(testData, n -> n * 2);
    bh.consume(result);
  }

  @Benchmark
  public void streams_map(Blackhole bh) {
    List<Integer> result = testData.stream().map(n -> n * 2).collect(Collectors.toList());
    bh.consume(result);
  }

  // ==================== Filter Operation Benchmarks ====================

  @Benchmark
  public void listOps_filter(Blackhole bh) {
    List<Integer> result = ListOps.filter(testData, n -> n % 2 == 0);
    bh.consume(result);
  }

  @Benchmark
  public void streams_filter(Blackhole bh) {
    List<Integer> result = testData.stream().filter(n -> n % 2 == 0).collect(Collectors.toList());
    bh.consume(result);
  }

  // ==================== Chained Operations Benchmarks ====================

  @Benchmark
  public void listOps_chainedMapFilter(Blackhole bh) {
    List<Integer> result = ListOps.filter(ListOps.map(testData, n -> n * 2), n -> n % 4 == 0);
    bh.consume(result);
  }

  @Benchmark
  public void streams_chainedMapFilter(Blackhole bh) {
    List<Integer> result =
        testData.stream()
            .map(n -> n * 2)
            .filter(n -> n % 4 == 0)
            .collect(Collectors.toList());
    bh.consume(result);
  }

  @Benchmark
  public void listOps_chainedFilterMap(Blackhole bh) {
    List<Integer> result = ListOps.map(ListOps.filter(testData, n -> n % 2 == 0), n -> n * 3);
    bh.consume(result);
  }

  @Benchmark
  public void streams_chainedFilterMap(Blackhole bh) {
    List<Integer> result =
        testData.stream()
            .filter(n -> n % 2 == 0)
            .map(n -> n * 3)
            .collect(Collectors.toList());
    bh.consume(result);
  }

  // ==================== Complex Chain Benchmarks ====================

  @Benchmark
  public void listOps_complexChain(Blackhole bh) {
    List<Integer> result =
        ListOps.map(
            ListOps.filter(
                ListOps.map(ListOps.filter(testData, n -> n > 10), n -> n * 2), n -> n < 1000),
            n -> n + 1);
    bh.consume(result);
  }

  @Benchmark
  public void streams_complexChain(Blackhole bh) {
    List<Integer> result =
        testData.stream()
            .filter(n -> n > 10)
            .map(n -> n * 2)
            .filter(n -> n < 1000)
            .map(n -> n + 1)
            .collect(Collectors.toList());
    bh.consume(result);
  }

  // ==================== Edge Cases ====================

  @Benchmark
  public void listOps_mapWithObjectCreation(Blackhole bh) {
    List<String> result = ListOps.map(testData, n -> "Number: " + n);
    bh.consume(result);
  }

  @Benchmark
  public void streams_mapWithObjectCreation(Blackhole bh) {
    List<String> result = testData.stream().map(n -> "Number: " + n).collect(Collectors.toList());
    bh.consume(result);
  }

  @Benchmark
  public void listOps_filterAlmostAll(Blackhole bh) {
    List<Integer> result = ListOps.filter(testData, n -> n < listSize - 1);
    bh.consume(result);
  }

  @Benchmark
  public void streams_filterAlmostAll(Blackhole bh) {
    List<Integer> result =
        testData.stream().filter(n -> n < listSize - 1).collect(Collectors.toList());
    bh.consume(result);
  }

  @Benchmark
  public void listOps_filterAlmostNone(Blackhole bh) {
    List<Integer> result = ListOps.filter(testData, n -> n > listSize - 2);
    bh.consume(result);
  }

  @Benchmark
  public void streams_filterAlmostNone(Blackhole bh) {
    List<Integer> result =
        testData.stream().filter(n -> n > listSize - 2).collect(Collectors.toList());
    bh.consume(result);
  }
}
