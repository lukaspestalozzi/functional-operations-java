package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.find")
class MapOpsFindTest {

  @Test
  @DisplayName("should find first matching entry")
  void testFind() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);

    Optional<Map.Entry<String, Integer>> result = MapOps.find(map, (k, v) -> v > 1);

    assertThat(result).isPresent();
    assertThat(result.get().getKey()).isEqualTo("b");
    assertThat(result.get().getValue()).isEqualTo(2);
  }

  @Test
  @DisplayName("should return empty when no match")
  void testFindNoMatch() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2);

    Optional<Map.Entry<String, Integer>> result = MapOps.find(map, (k, v) -> v > 10);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should return empty for empty map")
  void testFindEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Optional<Map.Entry<String, Integer>> result = MapOps.find(empty, (k, v) -> true);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testFindNullMap() {
    assertThatThrownBy(() -> MapOps.find(null, (k, v) -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testFindNullPredicate() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.find(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
