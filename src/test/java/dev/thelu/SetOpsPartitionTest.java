package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.partition")
class SetOpsPartitionTest {

  @Test
  @DisplayName("should partition set by predicate")
  void testPartition() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5, 6);

    List<Set<Integer>> result = SetOps.partition(numbers, n -> n % 2 == 0);

    assertThat(result).hasSize(2);
    assertThat(result.get(0)).containsExactlyInAnyOrder(2, 4, 6);
    assertThat(result.get(1)).containsExactlyInAnyOrder(1, 3, 5);
  }

  @Test
  @DisplayName("should handle all matching")
  void testPartitionAllMatch() {
    Set<Integer> numbers = Set.of(2, 4, 6);

    List<Set<Integer>> result = SetOps.partition(numbers, n -> n % 2 == 0);

    assertThat(result.get(0)).containsExactlyInAnyOrder(2, 4, 6);
    assertThat(result.get(1)).isEmpty();
  }

  @Test
  @DisplayName("should handle none matching")
  void testPartitionNoneMatch() {
    Set<Integer> numbers = Set.of(1, 3, 5);

    List<Set<Integer>> result = SetOps.partition(numbers, n -> n % 2 == 0);

    assertThat(result.get(0)).isEmpty();
    assertThat(result.get(1)).containsExactlyInAnyOrder(1, 3, 5);
  }

  @Test
  @DisplayName("should handle empty set")
  void testPartitionEmptySet() {
    Set<Integer> empty = Set.of();

    List<Set<Integer>> result = SetOps.partition(empty, n -> true);

    assertThat(result).hasSize(2);
    assertThat(result.get(0)).isEmpty();
    assertThat(result.get(1)).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testPartitionNullSet() {
    assertThatThrownBy(() -> SetOps.partition(null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testPartitionNullPredicate() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.partition(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
