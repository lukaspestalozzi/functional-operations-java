package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.mapValues")
class MapOpsMapValuesTest {

  @Test
  @DisplayName("should transform all values in a map")
  void testMapValues() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);

    Map<String, Integer> result = MapOps.mapValues(map, v -> v * 2);

    assertThat(result).containsEntry("a", 2).containsEntry("b", 4).containsEntry("c", 6);
  }

  @Test
  @DisplayName("should handle empty map")
  void testMapValuesEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Map<String, String> result = MapOps.mapValues(empty, Object::toString);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testMapValuesNullMap() {
    assertThatThrownBy(() -> MapOps.mapValues(null, v -> v))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testMapValuesNullMapper() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.mapValues(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }

  @Test
  @DisplayName("should handle type transformation")
  void testMapValuesTypeTransformation() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2);

    Map<String, String> result = MapOps.mapValues(map, v -> "val:" + v);

    assertThat(result).containsEntry("a", "val:1").containsEntry("b", "val:2");
  }
}
