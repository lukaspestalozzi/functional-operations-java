package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.drop")
class MapOpsDropTest {

  @Test
  @DisplayName("should drop first n entries")
  void testDrop() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);
    map.put("d", 4);

    Map<String, Integer> result = MapOps.drop(map, 2);

    assertThat(result).hasSize(2).containsEntry("c", 3).containsEntry("d", 4);
  }

  @Test
  @DisplayName("should return empty when n exceeds size")
  void testDropMoreThanSize() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2);

    Map<String, Integer> result = MapOps.drop(map, 10);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should return all when n is zero")
  void testDropZero() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2);

    Map<String, Integer> result = MapOps.drop(map, 0);

    assertThat(result).hasSize(2);
  }

  @Test
  @DisplayName("should handle empty map")
  void testDropEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Map<String, Integer> result = MapOps.drop(empty, 5);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testDropNullMap() {
    assertThatThrownBy(() -> MapOps.drop(null, 3))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw IllegalArgumentException when n is negative")
  void testDropNegative() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.drop(map, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("negative");
  }
}
