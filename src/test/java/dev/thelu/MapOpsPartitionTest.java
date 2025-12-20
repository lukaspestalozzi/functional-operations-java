package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.partition")
class MapOpsPartitionTest {

  @Test
  @DisplayName("should partition map by predicate")
  void testPartition() {
    Map<String, Integer> map = Map.of("a", 1, "b", 2, "c", 3, "d", 4);

    List<Map<String, Integer>> result = MapOps.partition(map, (k, v) -> v % 2 == 0);

    assertThat(result).hasSize(2);
    assertThat(result.get(0)).containsEntry("b", 2).containsEntry("d", 4);
    assertThat(result.get(1)).containsEntry("a", 1).containsEntry("c", 3);
  }

  @Test
  @DisplayName("should handle all matching")
  void testPartitionAllMatch() {
    Map<String, Integer> map = Map.of("a", 2, "b", 4);

    List<Map<String, Integer>> result = MapOps.partition(map, (k, v) -> v % 2 == 0);

    assertThat(result.get(0)).hasSize(2);
    assertThat(result.get(1)).isEmpty();
  }

  @Test
  @DisplayName("should handle none matching")
  void testPartitionNoneMatch() {
    Map<String, Integer> map = Map.of("a", 1, "b", 3);

    List<Map<String, Integer>> result = MapOps.partition(map, (k, v) -> v % 2 == 0);

    assertThat(result.get(0)).isEmpty();
    assertThat(result.get(1)).hasSize(2);
  }

  @Test
  @DisplayName("should handle empty map")
  void testPartitionEmptyMap() {
    Map<String, Integer> empty = Map.of();

    List<Map<String, Integer>> result = MapOps.partition(empty, (k, v) -> true);

    assertThat(result).hasSize(2);
    assertThat(result.get(0)).isEmpty();
    assertThat(result.get(1)).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when map is null")
  void testPartitionNullMap() {
    assertThatThrownBy(() -> MapOps.partition(null, (k, v) -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testPartitionNullPredicate() {
    Map<String, Integer> map = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.partition(map, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
