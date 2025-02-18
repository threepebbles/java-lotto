package domain;

import java.util.Map;

public record WinningResult(Map<LottoPrize, Integer> prizeCounter, double profit) {
}
