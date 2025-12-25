package dev.thelu;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.util.Set;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

@DisplayName("SetOps.none")
class SetOpsNoneTest {

  @Test
  @DisplayName("should return true when no elements match")
  void testNoneMatch() {
    Set<Integer> numbers = Set.of(1, 3, 5, 7);

    boolean result = SetOps.none(numbers, n -> n % 2 == 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should return false when any element matches")
  void testSomeMatch() {
    Set<Integer> numbers = Set.of(1, 2, 3, 4, 5);

    boolean result = SetOps.none(numbers, n -> n % 2 == 0);

    assertThat(result).isFalse();
  }

  @Test
  @DisplayName("should return true for empty set")
  void testEmptySet() {
    Set<Integer> empty = Set.of();

    boolean result = SetOps.none(empty, n -> n > 0);

    assertThat(result).isTrue();
  }

  @Test
  @DisplayName("should throw NullPointerException when set is null")
  void testNullSet() {
    assertThatThrownBy(() -> SetOps.none(null, n -> true))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("iterable");
  }

  @Test
  @DisplayName("should throw NullPointerException when predicate is null")
  void testNullPredicate() {
    Set<Integer> numbers = Set.of(1, 2, 3);

    assertThatThrownBy(() -> SetOps.none(numbers, null))
        .isInstanceOf(NullPointerException.class)
        .hasMessageContaining("predicate");
  }
}
