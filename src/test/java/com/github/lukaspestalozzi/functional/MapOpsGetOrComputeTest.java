package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.getOrCompute")
class MapOpsGetOrComputeTest {

  @Test
  @DisplayName("should return existing value")
  void testGetOrComputeExisting() {
    Map<String, Integer> map = Map.of("a", 1);

    Integer result = MapOps.getOrCompute(map, "a", k -> 99);

    assertThat(result).isEqualTo(1);
  }

  @Test
  @DisplayName("should compute value when key absent")
  void testGetOrComputeAbsent() {
    Map<String, Integer> map = Map.of("a", 1);

    Integer result = MapOps.getOrCompute(map, "b", k -> k.length() * 10);

    assertThat(result).isEqualTo(10);
  }

  @Test
  @DisplayName("should return null for null value in map")
  void testGetOrComputeNullValue() {
    Map<String, Integer> map = new HashMap<>();
    map.put("a", null);

    Integer result = MapOps.getOrCompute(map, "a", k -> 99);

    assertThat(result).isNull();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testGetOrComputeNullMap() {
    assertThatThrownBy(() -> MapOps.getOrCompute(null, "a", k -> 1))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when defaultMapper is null")
  void testGetOrComputeNullMapper() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.getOrCompute(map, "b", null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("defaultMapper");
  }
}
