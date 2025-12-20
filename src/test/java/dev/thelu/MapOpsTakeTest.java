package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.take")
class MapOpsTakeTest {

  @Test
  @DisplayName("should take first n entries")
  void testTake() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);
    map.put("d", 4);

    Map<String, Integer> result = MapOps.take(map, 2);

    assertThat(result).hasSize(2).containsEntry("a", 1).containsEntry("b", 2);
  }

  @Test
  @DisplayName("should take all when n exceeds size")
  void testTakeMoreThanSize() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2);

    Map<String, Integer> result = MapOps.take(map, 10);

    assertThat(result).hasSize(2);
  }

  @Test
  @DisplayName("should return empty when n is zero")
  void testTakeZero() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2);

    Map<String, Integer> result = MapOps.take(map, 0);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should handle empty map")
  void testTakeEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Map<String, Integer> result = MapOps.take(empty, 5);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testTakeNullMap() {
    assertThatThrownBy(() -> MapOps.take(null, 3))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw IllegalArgumentException when n is negative")
  void testTakeNegative() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.take(map, -1))
        .isInstanceOf(IllegalArgumentException.class)
        .hasMessageContaining("negative");
  }
}
