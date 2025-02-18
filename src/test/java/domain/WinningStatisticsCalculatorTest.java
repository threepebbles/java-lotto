package domain;

import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class WinningStatisticsCalculatorTest {
    @DisplayName("주어진 로또티켓들로 당첨 통계를 올바르게 계산하는지 테스트")
    @Test
    void 당첨_통계_계산() {
        //given
        LottoTicket fifth = LottoTicket.from(List.of(1, 2, 3, 43, 44, 45));
        LottoTicket fourth = LottoTicket.from(List.of(1, 2, 3, 4, 44, 45));
        LottoTicket second = LottoTicket.from(List.of(1, 2, 3, 4, 5, 7));
        LottoTicket nothing = LottoTicket.from(List.of(31, 32, 33, 34, 35, 36));
        LottoTickets lottoTickets = new LottoTickets(List.of(fifth, fourth, second, nothing));
        LottoTicket winningLottoTicket = new LottoTicket(Stream.of(1, 2, 3, 4, 5, 6).map(LottoNumber::new).toList());
        LottoNumber bonusNumber = new LottoNumber(7);
        DrawResult drawResult = new DrawResult(winningLottoTicket, bonusNumber);

        // when
        Map<LottoPrize, Integer> prizeCounter = WinningStatisticsCalculator.calculateWinningStatistics(
                lottoTickets, drawResult);

        // then
        Assertions.assertThat(prizeCounter.get(LottoPrize.FIFTH)).isEqualTo(1);
        Assertions.assertThat(prizeCounter.get(LottoPrize.FOURTH)).isEqualTo(1);
        Assertions.assertThat(prizeCounter.get(LottoPrize.SECOND)).isEqualTo(1);
        Assertions.assertThat(prizeCounter.get(LottoPrize.NOTHING)).isEqualTo(1);
    }

    @DisplayName("수익률 계산 테스트")
    @Test
    void 수익률_계산() {
        //given
        LottoTicket fifth = LottoTicket.from(List.of(1, 2, 3, 43, 44, 45));
        LottoTicket fourth = LottoTicket.from(List.of(1, 2, 3, 4, 44, 45));
        LottoTicket second = LottoTicket.from(List.of(1, 2, 3, 4, 5, 7));
        LottoTicket nothing = LottoTicket.from(List.of(31, 32, 33, 34, 35, 36));
        LottoTickets lottoTickets = new LottoTickets(List.of(fifth, fourth, second, nothing));
        LottoTicket winningLottoTicket = new LottoTicket(Stream.of(1, 2, 3, 4, 5, 6).map(LottoNumber::new).toList());
        LottoNumber bonusNumber = new LottoNumber(7);
        DrawResult drawResult = new DrawResult(winningLottoTicket, bonusNumber);

        // when
        Map<LottoPrize, Integer> prizeCounter = WinningStatisticsCalculator.calculateWinningStatistics(
                lottoTickets, drawResult);
        double profit = WinningStatisticsCalculator.calculateProfit(prizeCounter);

        // then
        Assertions.assertThat(profit).isEqualTo(7513.75);
    }
}
