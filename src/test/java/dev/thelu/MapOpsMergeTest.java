package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("MapOps.merge")
class MapOpsMergeTest {

  @Test
  @DisplayName("should merge two maps without conflicts")
  void testMergeNoConflict() {
    Map<String, Integer> map1 = Map.of("a", 1, "b", 2);
    Map<String, Integer> map2 = Map.of("c", 3, "d", 4);

    Map<String, Integer> result = MapOps.merge(map1, map2, Integer::sum);

    assertThat(result)
        .hasSize(4)
        .containsEntry("a", 1)
        .containsEntry("b", 2)
        .containsEntry("c", 3)
        .containsEntry("d", 4);
  }

  @Test
  @DisplayName("should use combiner for conflicts")
  void testMergeWithConflict() {
    Map<String, Integer> map1 = Map.of("a", 1, "b", 2);
    Map<String, Integer> map2 = Map.of("b", 3, "c", 4);

    Map<String, Integer> result = MapOps.merge(map1, map2, Integer::sum);

    assertThat(result).hasSize(3).containsEntry("a", 1).containsEntry("b", 5).containsEntry("c", 4);
  }

  @Test
  @DisplayName("should handle empty first map")
  void testMergeEmptyFirst() {
    Map<String, Integer> empty = Map.of();
    Map<String, Integer> map2 = Map.of("a", 1);

    Map<String, Integer> result = MapOps.merge(empty, map2, Integer::sum);

    assertThat(result).containsExactlyEntriesOf(map2);
  }

  @Test
  @DisplayName("should handle empty second map")
  void testMergeEmptySecond() {
    Map<String, Integer> map1 = Map.of("a", 1);
    Map<String, Integer> empty = Map.of();

    Map<String, Integer> result = MapOps.merge(map1, empty, Integer::sum);

    assertThat(result).containsExactlyEntriesOf(map1);
  }

  @Test
  @DisplayName("should throw NullPointerException when map1 is null")
  void testMergeNullMap1() {
    Map<String, Integer> map2 = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.merge(null, map2, Integer::sum))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map1");
  }

  @Test
  @DisplayName("should throw NullPointerException when map2 is null")
  void testMergeNullMap2() {
    Map<String, Integer> map1 = Map.of("a", 1);

    assertThatThrownBy(() -> MapOps.merge(map1, null, Integer::sum))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("map2");
  }

  @Test
  @DisplayName("should throw NullPointerException when combiner is null")
  void testMergeNullCombiner() {
    Map<String, Integer> map1 = Map.of("a", 1);
    Map<String, Integer> map2 = Map.of("b", 2);

    assertThatThrownBy(() -> MapOps.merge(map1, map2, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("combiner");
  }
}
