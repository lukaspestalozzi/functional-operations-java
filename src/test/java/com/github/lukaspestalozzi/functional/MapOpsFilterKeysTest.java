package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.filterKeys")
class MapOpsFilterKeysTest {

  @Test
  @DisplayName("should keep only entries with matching keys")
  void testFilterKeys() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("apple", 1);
    map.put("banana", 2);
    map.put("apricot", 3);

    Map<String, Integer> result = MapOps.filterKeys(map, k -> k.startsWith("a"));

    assertThat(result).hasSize(2).containsEntry("apple", 1).containsEntry("apricot", 3);
  }

  @Test
  @DisplayName("should handle empty map")
  void testFilterKeysEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Map<String, Integer> result = MapOps.filterKeys(empty, k -> true);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testFilterKeysNullMap() {
    assertThatThrownBy(() -> MapOps.filterKeys(null, k -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFilterKeysNullPredicate() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.filterKeys(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
