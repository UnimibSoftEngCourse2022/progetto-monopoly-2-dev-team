package it.monopoly.controller.board.space;

import it.monopoly.controller.board.card.DrawableCardController;
import it.monopoly.controller.command.CommandBuilder;
import it.monopoly.controller.command.CommandBuilderDispatcher;
import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.player.command.MoveCommand;
import it.monopoly.controller.player.command.MoveCommandBuilder;
import it.monopoly.controller.player.command.PayCommandBuilder;
import it.monopoly.model.DrawableCardModel;
import it.monopoly.model.enums.CreditorDebtor;
import it.monopoly.model.enums.Movement;
import it.monopoly.model.enums.Pay;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

import java.util.List;

import static it.monopoly.model.enums.Movement.MOVE_OF;
import static it.monopoly.model.enums.Movement.MOVE_TO;

public abstract class DrawableCardSpace extends AbstractSpace {
    private final PlayerController playerController;
    protected DrawableCardController drawableCardController;

    protected DrawableCardSpace(CommandBuilderDispatcher commandBuilderDispatcher,
                                DrawableCardController drawableCardController,
                                PlayerController playerController) {
        super(commandBuilderDispatcher);
        this.drawableCardController = drawableCardController;
        this.playerController = playerController;
    }

    @Override
    public void applyEffect(PlayerModel player) {

        DrawableCardModel card = draw();

        CommandBuilder commandBuilder = null;

        if (card != null) {
            if (card.getMovement() != null) {
                commandBuilder = movementEffect(player, card);
            } else if (card.getPay() != null) {
                commandBuilder = payEffect(player, card);
            } else {
                playerController.getManager(player).keepCard(card);
            }
        }

        if (commandBuilder != null) {
            commandBuilder
                    .build()
                    .execute();
        }
    }

    private PayCommandBuilder payEffect(PlayerModel player, DrawableCardModel card) {

        Pay pay = card.getPay();
        CreditorDebtor debtor = card.getDebtor();
        CreditorDebtor creditor = card.getCreditor();
        int amount = 0;

        List<PlayerModel> players = playerController.getModels();

        PayCommandBuilder commandBuilder = commandBuilderDispatcher
                .createCommandBuilder(PayCommandBuilder.class);

        // check amount
        if (Pay.AMOUNT.equals(pay)) {
            amount = card.getAmount();
        } else {
            int houses = 0;
            int hotels = 0;
            for (PropertyModel propertyModel : playerController.getManager(player).getProperties()) {
                houses += propertyModel.getHouseNumber();
                hotels += propertyModel.getHotelNumber();
            }
            amount = houses * card.getHouseFee() + hotels * card.getHotelFee();
        }
        commandBuilder.setMoney(amount);

        // check debtor
        if (CreditorDebtor.PLAYER.equals(debtor)) {
            commandBuilder.addDebtor(player);
        } else if (CreditorDebtor.ALL.equals(debtor)) {
            for (PlayerModel playerModels : players) {
                commandBuilder.addDebtor(playerModels);
            }
        }

        //check creditor
        if (CreditorDebtor.PLAYER.equals(creditor)) {
            commandBuilder.addCreditor(player);
        } else if (CreditorDebtor.ALL.equals(creditor)) {
            for (PlayerModel playerModels : players) {
                commandBuilder.addCreditor(playerModels);
            }
        }

        return commandBuilder;
    }

    private MoveCommandBuilder movementEffect(PlayerModel player, DrawableCardModel card) {

        Movement movement = card.getMovement();
        int where = card.getWhere();
        boolean direct = card.isDirect();

        MoveCommandBuilder commandBuilder = commandBuilderDispatcher
                .createCommandBuilder(MoveCommandBuilder.class)
                .setPlayer(player)
                .setDirectMovement(direct);

        if (MOVE_OF.equals(movement)) {
            commandBuilder.setSpace(MoveCommand.Type.MOVE_OF, where);
        } else if (MOVE_TO.equals(movement)) {
            commandBuilder.setSpace(MoveCommand.Type.MOVE_TO, where).setGoToJail(where == 10);
        } else {
            commandBuilder.setNearSpaces(card.getNear());
        }

        return commandBuilder;
    }

    protected abstract DrawableCardModel draw();
}
