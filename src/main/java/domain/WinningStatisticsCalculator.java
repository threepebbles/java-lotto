package domain;

import java.util.HashMap;
import java.util.Map;

public class WinningStatisticsCalculator {
    public static Map<LottoPrize, Integer> calculateWinningStatistics(LottoTickets lottoTickets,
                                                                      DrawResult drawResult) {
        Map<LottoPrize, Integer> prizeCounter = new HashMap<>();
        initializePrizeCounter(prizeCounter);

        lottoTickets.getLottoTickets()
                .forEach(lottoTicket -> {
                    int countMatched = lottoTicket.countMatchedLottoNumbers(drawResult.winningLottoTicket());
                    boolean isBonusNumberMatched = lottoTicket.containsLottoNumber(drawResult.bonusNumber());
                    LottoPrize prize = LottoPrize.getLottoPrize(countMatched, isBonusNumberMatched);
                    int prizeCount = prizeCounter.get(prize);
                    prizeCounter.put(prize, prizeCount + 1);
                });
        return prizeCounter;
    }

    private static void initializePrizeCounter(Map<LottoPrize, Integer> prizeCounter) {
        for (LottoPrize prize : LottoPrize.values()) {
            prizeCounter.put(prize, 0);
        }
    }

    public static double calculateProfit(Map<LottoPrize, Integer> prizeCounter) {
        long sum = 0;
        long lottoTicketNumber = 0;
        for (LottoPrize lottoPrize : LottoPrize.values()) {
            sum += (long) prizeCounter.get(lottoPrize) * lottoPrize.getMoney();
            lottoTicketNumber += prizeCounter.get(lottoPrize);
        }
        return lottoTicketNumber == 0 ? 0 : ((double) sum / (lottoTicketNumber * LottoMachine.LOTTO_PRICE));
    }
}
