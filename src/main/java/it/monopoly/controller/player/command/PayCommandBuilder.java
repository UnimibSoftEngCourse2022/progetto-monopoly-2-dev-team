package it.monopoly.controller.player.command;

import it.monopoly.controller.TradeController;
import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.model.player.PlayerModel;

import java.util.ArrayList;
import java.util.List;

public class PayCommandBuilder implements CommandBuilder {
    private final TradeController tradeController;
    private int money;
    private final List<PlayerModel> creditors = new ArrayList<>();
    private final List<PlayerModel> debtors = new ArrayList<>();

    public PayCommandBuilder(TradeController tradeController) {
        this.tradeController = tradeController;
    }

    public PayCommandBuilder setMoney(int money) {
        this.money = money;
        return this;
    }

    public PayCommandBuilder addCreditor(PlayerModel player) {
        creditors.add(player);
        return this;
    }

    public PayCommandBuilder addDebtor(PlayerModel player) {
        debtors.add(player);
        return this;
    }

    public PayCommand build() {
        return new PayCommand(tradeController, creditors, debtors, money);
    }
}
