import controller.MainController;
import domain.LottoMachine;
import domain.RandomIntegerGenerator;

public class Application {
    public static void main(String[] args) {
        MainController mainController = new MainController(
                new LottoMachine(new RandomIntegerGenerator())
        );
        mainController.run();
    }
}
