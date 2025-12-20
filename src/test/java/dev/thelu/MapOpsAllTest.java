package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.all")
class MapOpsAllTest {

  @Test
  @DisplayName("should return true when all entries match")
  void testAllMatch() {
    Map<String, Integer> map = Map.of("a", 2, "b", 4, "c", 6);

    boolean result = MapOps.all(map, (k, v) -> v % 2 == 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should return false when not all entries match")
  void testAllNotMatch() {
    Map<String, Integer> map = Map.of("a", 2, "b", 3, "c", 4);

    boolean result = MapOps.all(map, (k, v) -> v % 2 == 0);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should return true for empty map")
  void testAllEmptyMap() {
    Map<String, Integer> empty = Map.of();

    boolean result = MapOps.all(empty, (k, v) -> false);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testAllNullMap() {
    assertThatThrownBy(() -> MapOps.all(null, (k, v) -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testAllNullPredicate() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.all(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
