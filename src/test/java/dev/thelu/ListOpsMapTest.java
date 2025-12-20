package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.map")
class ListOpsMapTest {

  @Test
  @DisplayName("should transform all elements in a list")
  void testMap() {
    List<Integer> numbers = List.of(1, 2, 3, 4, 5);
    Function<Integer, Integer> doubler = n -> n * 2;

    List<Integer> result = ListOps.map(numbers, doubler);

    assertThat(result).containsExactly(2, 4, 6, 8, 10);
  }

  @Test
  @DisplayName("should handle empty list")
  void testMapEmptyList() {
    List<Integer> empty = List.of();
    Function<Integer, String> toString = Object::toString;

    List<String> result = ListOps.map(empty, toString);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when list is null")
  void testMapNullList() {
    Function<Integer, Integer> identity = n -> n;

    assertThatThrownBy(() -> ListOps.map(null, identity))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when mapper is null")
  void testMapNullMapper() {
    List<Integer> numbers = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.map(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("mapper");
  }

  @Test
  @DisplayName("should handle type transformation")
  void testMapTypeTransformation() {
    List<Integer> numbers = List.of(1, 2, 3);
    Function<Integer, String> toHex = n -> "0x" + Integer.toHexString(n);

    List<String> result = ListOps.map(numbers, toHex);

    assertThat(result).containsExactly("0x1", "0x2", "0x3");
  }
}
