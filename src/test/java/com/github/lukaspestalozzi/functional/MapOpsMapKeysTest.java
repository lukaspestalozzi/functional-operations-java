package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.mapKeys")
class MapOpsMapKeysTest {

  @Test
  @DisplayName("should transform all keys in a map")
  void testMapKeys() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);

    Map<String, Integer> result = MapOps.mapKeys(map, k -> k.toUpperCase());

    assertThat(result).containsEntry("A", 1).containsEntry("B", 2).containsEntry("C", 3);
  }

  @Test
  @DisplayName("should handle empty map")
  void testMapKeysEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Map<Integer, Integer> result = MapOps.mapKeys(empty, String::length);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testMapKeysNullMap() {
    assertThatThrownBy(() -> MapOps.mapKeys(null, k -> k))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testMapKeysNullMapper() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.mapKeys(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }

  @Test
  @DisplayName("should handle key collisions by keeping last value")
  void testMapKeysCollision() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("aa", 1);
    map.put("bb", 2);

    Map<Integer, Integer> result = MapOps.mapKeys(map, String::length);

    assertThat(result).hasSize(1).containsEntry(2, 2);
  }
}
