package controller.player.command;

import controller.TradeController;
import controller.command.Command;
import model.player.PlayerModel;

import java.util.List;

public class PayCommand implements Command {
    private final TradeController tradeController;
    private final int money;
    private final List<PlayerModel> creditors;
    private final List<PlayerModel> debtors;

    public PayCommand(TradeController tradeController,
                      List<PlayerModel> creditors,
                      List<PlayerModel> debtors,
                      int money
    ) {
        this.tradeController = tradeController;
        this.money = money;
        this.creditors = creditors;
        this.debtors = debtors;
    }

    @Override
    public String getCommandName() {
        return "";
    }

    @Override
    public boolean isEnabled() {
        return true;
    }

    @Override
    public void execute() {
        if (creditors.size() == 0) {
            for (PlayerModel debtor : debtors) {
                tradeController.spendTransaction(debtor, money);
            }
        } else if (debtors.size() == 0) {
            for (PlayerModel creditor : creditors) {
                tradeController.earnTransaction(creditor, money);
            }
        } else {
            for (PlayerModel debtor : debtors) {
                for (PlayerModel creditor : creditors) {
                    tradeController.makeTransaction(creditor, debtor, money);
                }
            }
        }
    }
}
