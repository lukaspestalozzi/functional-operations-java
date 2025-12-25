package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.none")
class MapOpsNoneTest {

  @Test
  @DisplayName("should return true when no entries match")
  void testNoneMatch() {
    Map<String, Integer> map = Map.of("a", 1, "b", 3, "c", 5);

    boolean result = MapOps.none(map, (k, v) -> v % 2 == 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should return false when any entry matches")
  void testSomeMatch() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);

    boolean result = MapOps.none(map, (k, v) -> v % 2 == 0);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should return true for empty map")
  void testEmptyMap() {
    Map<String, Integer> empty = Map.of();

    boolean result = MapOps.none(empty, (k, v) -> v > 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testNullMap() {
    assertThatThrownBy(() -> MapOps.none(null, (k, v) -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testNullPredicate() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.none(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
