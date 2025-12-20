package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.partition")
class ListOpsPartitionTest {

  @Test
  @DisplayName("should partition list by predicate")
  void testPartition() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5, 6);

    List<List<Integer>> result = ListOps.partition(numbers, n -> n % 2 == 0);

    assertThat(result).hasSize(2);
    assertThat(result.get(0)).containsExactly(2, 4, 6);
    assertThat(result.get(1)).containsExactly(1, 3, 5);
  }

  @Test
  @DisplayName("should handle all matching")
  void testPartitionAllMatch() {
    List<Integer> numbers = List.of(2, 4, 6);

    List<List<Integer>> result = ListOps.partition(numbers, n -> n % 2 == 0);

    assertThat(result.get(0)).containsExactly(2, 4, 6);
    assertThat(result.get(1)).isEmpty();
  }

  @Test
  @DisplayName("should handle none matching")
  void testPartitionNoneMatch() {
    List<Integer> numbers = List.of(1, 3, 5);

    List<List<Integer>> result = ListOps.partition(numbers, n -> n % 2 == 0);

    assertThat(result.get(0)).isEmpty();
    assertThat(result.get(1)).containsExactly(1, 3, 5);
  }

  @Test
  @DisplayName("should handle empty list")
  void testPartitionEmptyList() {
    List<Integer> empty = List.of();

    List<List<Integer>> result = ListOps.partition(empty, n -> true);

    assertThat(result).hasSize(2);
    assertThat(result.get(0)).isEmpty();
    assertThat(result.get(1)).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testPartitionNullList() {
    assertThatThrownBy(() -> ListOps.partition(null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testPartitionNullPredicate() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.partition(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
