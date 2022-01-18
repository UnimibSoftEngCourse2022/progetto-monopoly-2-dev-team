package it.monopoly.controller.player.command;

import it.monopoly.controller.TradeController;
import it.monopoly.controller.command.Command;
import it.monopoly.model.player.PlayerModel;

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
        if (creditors.isEmpty()) {
            for (PlayerModel debtor : debtors) {
                tradeController.spendTransaction(debtor, money);
            }
        } else if (debtors.isEmpty()) {
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
