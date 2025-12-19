package com.github.lukaspestalozzi.functional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.any")
class MapOpsAnyTest {

  @Test
  @DisplayName("should return true when any entry matches")
  void testAnyMatch() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3);

    boolean result = MapOps.any(map, (k, v) -> v > 2);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should return false when no entry matches")
  void testAnyNoMatch() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2);

    boolean result = MapOps.any(map, (k, v) -> v > 10);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should return false for empty map")
  void testAnyEmptyMap() {
    Map<String, Integer> empty = Map.of();

    boolean result = MapOps.any(empty, (k, v) -> true);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testAnyNullMap() {
    assertThatThrownBy(() -> MapOps.any(null, (k, v) -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testAnyNullPredicate() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.any(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
