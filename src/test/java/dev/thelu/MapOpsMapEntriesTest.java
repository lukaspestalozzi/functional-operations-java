package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.AbstractMap;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.mapEntries")
class MapOpsMapEntriesTest {

  @Test
  @DisplayName("should transform all entries in a map")
  void testMapEntries() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);

    Map<String, String> result =
        MapOps.mapEntries(
            map, e -> new AbstractMap.SimpleEntry<>(e.getKey().toUpperCase(), "v" + e.getValue()));

    assertThat(result).containsEntry("A", "v1").containsEntry("B", "v2");
  }

  @Test
  @DisplayName("should handle empty map")
  void testMapEntriesEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Map<String, String> result =
        MapOps.mapEntries(empty, e -> new AbstractMap.SimpleEntry<>(e.getKey(), "x"));

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testMapEntriesNullMap() {
    assertThatThrownBy(() -> MapOps.mapEntries(null, e -> e))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testMapEntriesNullMapper() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.mapEntries(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }

  @Test
  @DisplayName("should skip null entries from mapper")
  void testMapEntriesSkipNull() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);

    Map<String, Integer> result =
        MapOps.mapEntries(
            map,
            e ->
                e.getValue() == 2 ? null : new AbstractMap.SimpleEntry<>(e.getKey(), e.getValue()));

    assertThat(result).hasSize(2).containsEntry("a", 1).containsEntry("c", 3);
  }
}
