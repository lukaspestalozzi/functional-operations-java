package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.List;
import java.util.function.BiFunction;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("ListOps.zip")
class ListOpsZipTest {

  @Test
  @DisplayName("should combine two lists element-wise")
  void testZip() {
    List<Integer> a = List.of(1, 2, 3);
    List<Integer> b = List.of(10, 20, 30);

    List<Integer> result = ListOps.zip(a, b, Integer::sum);

    assertThat(result).containsExactly(11, 22, 33);
  }

  @Test
  @DisplayName("should use minimum length of both lists")
  void testZipDifferentLengths() {
    List<Integer> a = List.of(1, 2, 3, 4, 5);
    List<Integer> b = List.of(10, 20);

    List<Integer> result = ListOps.zip(a, b, Integer::sum);

    assertThat(result).containsExactly(11, 22);
  }

  @Test
  @DisplayName("should handle different types")
  void testZipDifferentTypes() {
    List<String> names = List.of("Alice", "Bob");
    List<Integer> ages = List.of(25, 30);
    BiFunction<String, Integer, String> combiner = (name, age) -> name + ":" + age;

    List<String> result = ListOps.zip(names, ages, combiner);

    assertThat(result).containsExactly("Alice:25", "Bob:30");
  }

  @Test
  @DisplayName("should return empty when first list is empty")
  void testZipFirstEmpty() {
    List<Integer> empty = List.of();
    List<Integer> b = List.of(1, 2, 3);

    List<Integer> result = ListOps.zip(empty, b, Integer::sum);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should return empty when second list is empty")
  void testZipSecondEmpty() {
    List<Integer> a = List.of(1, 2, 3);
    List<Integer> empty = List.of();

    List<Integer> result = ListOps.zip(a, empty, Integer::sum);

    assertThat(result).isEmpty();
  }

  @Test
  @DisplayName("should throw NullPointerException when first list is null")
  void testZipNullFirstList() {
    List<Integer> b = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.zip(null, b, Integer::sum))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterableA");
  }

  @Test
  @DisplayName("should throw NullPointerException when second list is null")
  void testZipNullSecondList() {
    List<Integer> a = List.of(1, 2, 3);

    assertThatThrownBy(() -> ListOps.zip(a, null, Integer::sum))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterableB");
  }

  @Test
  @DisplayName("should throw NullPointerException when combiner is null")
  void testZipNullCombiner() {
    List<Integer> a = List.of(1, 2, 3);
    List<Integer> b = List.of(4, 5, 6);

    assertThatThrownBy(() -> ListOps.zip(a, b, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("combiner");
  }
}
