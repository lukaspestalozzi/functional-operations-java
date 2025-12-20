package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.reduce")
class ListOpsReduceTest {

  @Test
  @DisplayName("should sum all elements")
  void testReduceSum() {
    List<Integer> numbers = List.of(1, 2, 3, 4);

    Integer result = ListOps.reduce(numbers, 0, Integer::sum);

    assertThat(result).isEqualTo(10);
  }

  @Test
  @DisplayName("should concatenate strings")
  void testReduceConcat() {
    List<String> words = List.of("a", "b", "c");

    String result = ListOps.reduce(words, "", (acc, s) -> acc + s);

    assertThat(result).isEqualTo("abc");
  }

  @Test
  @DisplayName("should return identity for empty list")
  void testReduceEmptyList() {
    List<Integer> empty = List.of();

    Integer result = ListOps.reduce(empty, 42, Integer::sum);

    assertThat(result).isEqualTo(42);
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testReduceNullList() {
    assertThatThrownBy(() -> ListOps.reduce(null, 0, Integer::sum))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when accumulator is null")
  void testReduceNullAccumulator() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.reduce(numbers, 0, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("accumulator");
  }
}
