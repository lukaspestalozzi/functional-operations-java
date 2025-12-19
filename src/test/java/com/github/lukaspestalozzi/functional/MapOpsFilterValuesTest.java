package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.filterValues")
class MapOpsFilterValuesTest {

  @Test
  @DisplayName("should keep only entries with matching values")
  void testFilterValues() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);
    map.put("d", 4);

    Map<String, Integer> result = MapOps.filterValues(map, v -> v > 2);

    assertThat(result).hasSize(2).containsEntry("c", 3).containsEntry("d", 4);
  }

  @Test
  @DisplayName("should handle empty map")
  void testFilterValuesEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Map<String, Integer> result = MapOps.filterValues(empty, v -> true);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testFilterValuesNullMap() {
    assertThatThrownBy(() -> MapOps.filterValues(null, v -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFilterValuesNullPredicate() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.filterValues(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
