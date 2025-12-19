package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.filter")
class MapOpsFilterTest {

  @Test
  @DisplayName("should keep only entries matching predicate")
  void testFilter() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);
    map.put("d", 4);

    Map<String, Integer> result = MapOps.filter(map, (k, v) -> v % 2 == 0);

    assertThat(result).hasSize(2).containsEntry("b", 2).containsEntry("d", 4);
  }

  @Test
  @DisplayName("should handle empty map")
  void testFilterEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Map<String, Integer> result = MapOps.filter(empty, (k, v) -> true);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testFilterNullMap() {
    assertThatThrownBy(() -> MapOps.filter(null, (k, v) -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFilterNullPredicate() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.filter(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }

  @Test
  @DisplayName("should filter by both key and value")
  void testFilterByKeyAndValue() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2, "aa", 3);

    Map<String, Integer> result = MapOps.filter(map, (k, v) -> k.length() == 1 && v > 1);

    assertThat(result).hasSize(1).containsEntry("b", 2);
  }
}
