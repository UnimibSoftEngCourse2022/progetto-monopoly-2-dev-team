package controller.player;

import controller.property.PropertyController;
import manager.player.PlayerManager;
import model.player.PlayerModel;
import model.property.PropertyModel;

public class TradeController {
    private final PlayerController playerController;
    private final PropertyController propertyController;

    public TradeController(PlayerController playerController, PropertyController propertyController) {
        this.playerController = playerController;
        this.propertyController = propertyController;
    }

    public void changeOwner(PlayerModel buyer, PlayerModel seller, PropertyModel property, int money) {
        //TODO Checks with property manager
        makeTransaction(seller, buyer, money);
        changeOwner(buyer, seller, property);
    }

    public void changeOwner(PlayerModel seller, PlayerModel buyer, PropertyModel property) {
        if (seller == null || buyer == null) {
            return;
        }
        //TODO Change owner with propertyManager
    }

    public void makeTransaction(PlayerModel creditor, PlayerModel debtor, int money) {
        if (creditor == null || debtor == null) {
            return;
        }
        PlayerManager creditorManager = playerController.getManager(creditor);
        PlayerManager debtorManager = playerController.getManager(debtor);
        debtorManager.spend(money);
        creditorManager.earn(money);
    }

}
