package domain;

import java.util.List;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

class LottoTicketTest {

    public static Stream<Arguments> lottoNumbersWithWrongSize() {
        return Stream.of(
                Arguments.of(
                        List.of(1),
                        List.of(1, 2),
                        List.of(1, 2, 3),
                        List.of(1, 2, 3, 4),
                        List.of(1, 2, 3, 4, 5)
                )
        );
    }

    public static Stream<Arguments> lottoNumbersNotInRange() {
        return Stream.of(
                Arguments.of(
                        List.of(0, 1, 2, 3, 4, 5),
                        List.of(1, 2, 3, 4, 5, 46),
                        List.of(-1, 2, 3, 4, 5, 6)
                )
        );
    }

    public static Stream<Arguments> lottoNumbersInRange() {
        return Stream.of(
                Arguments.of(
                        // 경계값(1, 45) 테스트 케이스
                        List.of(1, 2, 3, 4, 5, 45),
                        List.of(1, 12, 13, 14, 16, 45),
                        List.of(1, 2, 3, 43, 44, 45)
                )
        );
    }

    public static Stream<Arguments> calculateCountMatchedNumbersCases() {
        return Stream.of(
                Arguments.arguments(List.of(11, 12, 13, 14, 15, 16), List.of(1, 2, 3, 4, 5, 6), 0),
                Arguments.arguments(List.of(1, 12, 13, 14, 15, 16), List.of(1, 2, 3, 4, 5, 6), 1),
                Arguments.arguments(List.of(1, 2, 13, 14, 15, 16), List.of(1, 2, 3, 4, 5, 6), 2),
                Arguments.arguments(List.of(1, 2, 3, 14, 15, 16), List.of(1, 2, 3, 4, 5, 6), 3),
                Arguments.arguments(List.of(1, 2, 3, 4, 15, 16), List.of(1, 2, 3, 4, 5, 6), 4),
                Arguments.arguments(List.of(1, 2, 3, 4, 5, 16), List.of(1, 2, 3, 4, 5, 6), 5),
                Arguments.arguments(List.of(1, 2, 3, 4, 5, 6), List.of(1, 2, 3, 4, 5, 6), 6)
        );
    }

    public static Stream<Arguments> bonusNumberNotMatchedCases() {
        return Stream.of(
                Arguments.arguments(List.of(1, 2, 3, 4, 5, 6), 7, false),
                Arguments.arguments(List.of(1, 2, 3, 4, 5, 6), 8, false),
                Arguments.arguments(List.of(1, 2, 3, 4, 5, 6), 9, false),
                Arguments.arguments(List.of(1, 2, 3, 4, 5, 6), 10, false)

        );
    }

    public static Stream<Arguments> bonusNumberMatchedCases() {
        return Stream.of(
                Arguments.arguments(List.of(1, 2, 3, 4, 5, 6), 1, true),
                Arguments.arguments(List.of(1, 2, 3, 4, 5, 6), 2, true),
                Arguments.arguments(List.of(1, 2, 3, 4, 5, 6), 3, true)
        );
    }

    @DisplayName("로또 번호의 개수가 6개가 아닌 경우 예외가 발생한다")
    @ParameterizedTest
    @MethodSource("lottoNumbersWithWrongSize")
    void 로또_번호의_개수가_6개가_아닌_경우_테스트(List<Integer> numbers) {
        // when & then
        Assertions.assertThatThrownBy(() -> {
                    LottoTicket.from(numbers);
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로또 번호는 6개여야 합니다.");
    }

    @DisplayName("로또 번호가 1 이상 45 이하가 아닌 경우 예외가 발생한다")
    @ParameterizedTest
    @MethodSource("lottoNumbersNotInRange")
    void 로또_번호가_1_이상_45_이하가_아닌_경우_예외_발생(List<Integer> numbers) {
        // when & then
        Assertions.assertThatThrownBy(() -> {
                    LottoTicket.from(numbers);
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("로또 번호는 1 이상 45 이하여야 합니다.");
    }

    @DisplayName("로또 번호가 1 이상 45 이하인 경우 정상적으로 로또가 발행된다.")
    @ParameterizedTest
    @MethodSource("lottoNumbersInRange")
    void 로또_번호가_1_이상_45_이하인_경우(List<Integer> numbers) {
        // when & then
        Assertions.assertThatCode(() -> {
            LottoTicket.from(numbers);
        }).doesNotThrowAnyException();
    }

    @DisplayName("로또 번호가 중복된 경우 예외가 발생한다")
    @Test
    void 로또_번호가_중복된_경우_예외_발생() {
        // given
        List<Integer> numbers = List.of(1, 2, 3, 4, 5, 5);

        // when & then
        Assertions.assertThatThrownBy(() -> {
                    LottoTicket.from(numbers);
                }).isInstanceOf(IllegalArgumentException.class)
                .hasMessage("중복된 번호가 존재합니다.");
    }

    @DisplayName("당첨번호 매칭 개수가 올바르게 계산되는지 테스트")
    @ParameterizedTest
    @MethodSource("calculateCountMatchedNumbersCases")
    void 당첨번호_매칭_개수_테스트(List<Integer> lottoNumbers, List<Integer> winningNumbers, int expected) {
        // given
        LottoTicket lottoTicket = LottoTicket.from(lottoNumbers);
        LottoTicket winningLottoTicket = new LottoTicket(winningNumbers.stream().map(LottoNumber::new).toList());

        // when
        int actual = lottoTicket.countMatchedLottoNumbers(winningLottoTicket);
        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("보너스 번호가 매칭되지 않는 경우 false를 반환")
    @ParameterizedTest
    @MethodSource("bonusNumberNotMatchedCases")
    void 보너스_번호가_매칭되지_않는_경우(List<Integer> lottoNumbers, int bonusNumber, boolean expected) {
        // given
        LottoTicket lottoTicket = LottoTicket.from(lottoNumbers);
        LottoNumber bonusLottoNumber = new LottoNumber(bonusNumber);

        // when
        boolean actual = lottoTicket.containsLottoNumber(bonusLottoNumber);
        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }

    @DisplayName("보너스 번호가 매칭되는 경우 true를 반환")
    @ParameterizedTest
    @MethodSource("bonusNumberMatchedCases")
    void 보너스_번호가_매칭되는_경우(List<Integer> lottoNumbers, int bonusNumber, boolean expected) {
        // given
        LottoTicket lottoTicket = LottoTicket.from(lottoNumbers);
        LottoNumber bonusLottoNumber = new LottoNumber(bonusNumber);

        // when
        boolean actual = lottoTicket.containsLottoNumber(bonusLottoNumber);
        // then
        Assertions.assertThat(actual).isEqualTo(expected);
    }
}
