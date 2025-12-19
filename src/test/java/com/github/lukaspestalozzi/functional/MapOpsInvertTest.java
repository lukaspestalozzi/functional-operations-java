package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.invert")
class MapOpsInvertTest {

  @Test
  @DisplayName("should swap keys and values")
  void testInvert() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);

    Map<Integer, String> result = MapOps.invert(map);

    assertThat(result).containsEntry(1, "a").containsEntry(2, "b").containsEntry(3, "c");
  }

  @Test
  @DisplayName("should handle empty map")
  void testInvertEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Map<Integer, String> result = MapOps.invert(empty);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testInvertNullMap() {
    assertThatThrownBy(() -> MapOps.invert(null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should handle value collisions by keeping last key")
  void testInvertCollision() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 1);

    Map<Integer, String> result = MapOps.invert(map);

    assertThat(result).hasSize(1).containsEntry(1, "b");
  }
}
