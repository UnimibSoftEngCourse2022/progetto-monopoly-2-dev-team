package it.monopoly.controller.player.command;

import it.monopoly.controller.TradeController;
import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.model.player.PlayerModel;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

public class PayCommandBuilder implements CommandBuilder {
    private final TradeController tradeController;
    private final List<PlayerModel> creditors = new ArrayList<>();
    private final List<PlayerModel> debtors = new ArrayList<>();
    private int money;

    public PayCommandBuilder(TradeController tradeController) {
        this.tradeController = tradeController;
    }

    public PayCommandBuilder setMoney(int money) {
        this.money = money;
        return this;
    }

    public PayCommandBuilder addCreditor(PlayerModel player) {
        if (player != null) {
            creditors.add(player);
        }
        return this;
    }

    public PayCommandBuilder addDebtor(PlayerModel player) {
        if (player != null) {
            debtors.add(player);
        }
        return this;
    }

    public PayCommand build() {
        return new PayCommand(tradeController, creditors, debtors, money);
    }
}
