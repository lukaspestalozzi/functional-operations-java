package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.reduce")
class MapOpsReduceTest {

  @Test
  @DisplayName("should sum all values")
  void testReduceSum() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);
    map.put("c", 3);

    Integer result = MapOps.reduce(map, 0, (acc, k, v) -> acc + v);

    assertThat(result).isEqualTo(6);
  }

  @Test
  @DisplayName("should concatenate keys and values")
  void testReduceConcat() {
    Map<String, Integer> map = new LinkedHashMap<>();
    map.put("a", 1);
    map.put("b", 2);

    String result = MapOps.reduce(map, "", (acc, k, v) -> acc + k + v);

    assertThat(result).isEqualTo("a1b2");
  }

  @Test
  @DisplayName("should return identity for empty map")
  void testReduceEmptyMap() {
    Map<String, Integer> empty = Map.of();

    Integer result = MapOps.reduce(empty, 42, (acc, k, v) -> acc + v);

    assertThat(result).isEqualTo(42);
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testReduceNullMap() {
    assertThatThrownBy(() -> MapOps.reduce(null, 0, (acc, k, v) -> acc))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when accumulator is null")
  void testReduceNullAccumulator() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.reduce(map, 0, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("accumulator");
  }
}
