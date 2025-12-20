package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.flatMapValues")
class MapOpsFlatMapValuesTest {

  @Test
  @DisplayName("should flat map values to multiple entries")
  void testFlatMapValues() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 2);
    map.put("b", 3);

    List<Map.Entry<String, Integer>> result = MapOps.flatMapValues(map, v -> List.of(v, v * 10));

    assertThat(result).hasSize(4);
    assertThat(result.get(0).getKey()).isEqualTo("a");
    assertThat(result.get(0).getValue()).isEqualTo(2);
    assertThat(result.get(1).getKey()).isEqualTo("a");
    assertThat(result.get(1).getValue()).isEqualTo(20);
    assertThat(result.get(2).getKey()).isEqualTo("b");
    assertThat(result.get(2).getValue()).isEqualTo(3);
    assertThat(result.get(3).getKey()).isEqualTo("b");
    assertThat(result.get(3).getValue()).isEqualTo(30);
  }

  @Test
  @DisplayName("should handle empty map")
  void testFlatMapValuesEmptyMap() {
    Map<String, Integer> empty = Map.of();

    List<Map.Entry<String, Integer>> result = MapOps.flatMapValues(empty, v -> List.of(v));

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testFlatMapValuesNullMap() {
    assertThatThrownBy(() -> MapOps.flatMapValues(null, v -> List.of(v)))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testFlatMapValuesNullMapper() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.flatMapValues(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }

  @Test
  @DisplayName("should skip null results from mapper")
  void testFlatMapValuesNullResult() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);

    List<Map.Entry<String, Integer>> result =
        MapOps.flatMapValues(map, v -> v == 1 ? null : List.of(v));

    assertThat(result).hasSize(1);
    assertThat(result.get(0).getKey()).isEqualTo("b");
  }
}
