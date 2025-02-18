package controller;

import domain.DrawResult;
import domain.LottoMachine;
import domain.LottoPrize;
import domain.LottoTickets;
import domain.Payment;
import domain.WinningResult;
import domain.WinningStatisticsCalculator;
import java.util.Map;
import view.InputView;
import view.OutputView;

public class MainController {
    private final LottoMachine lottoMachine;

    public MainController(LottoMachine lottoMachine) {
        this.lottoMachine = lottoMachine;
    }

    public void run() {
        LottoTickets lottoTickets = purchaseLottoTickets();
        OutputView.printLottoTickets(lottoTickets);

        DrawResult drawResult = InputView.inputDrawResult();

        WinningResult winningResult = calculateWinningResult(lottoTickets,
                drawResult);
        OutputView.printWinningResult(winningResult);
    }

    private LottoTickets purchaseLottoTickets() {
        Payment payment = InputView.inputPayment();
        return lottoMachine.generateLottoTickets(payment);
    }

    private WinningResult calculateWinningResult(LottoTickets lottoTickets,
                                                 DrawResult drawResult) {
        Map<LottoPrize, Integer> prizeCounter = WinningStatisticsCalculator.calculateWinningStatistics(
                lottoTickets, drawResult);
        double profit = WinningStatisticsCalculator.calculateProfit(prizeCounter);
        return new WinningResult(prizeCounter, profit);
    }
}
