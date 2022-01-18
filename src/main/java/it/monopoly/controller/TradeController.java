package it.monopoly.controller;

import it.monopoly.controller.player.PlayerController;
import it.monopoly.controller.property.PropertyController;
import it.monopoly.manager.player.PlayerManager;
import it.monopoly.manager.property.PropertyManager;
import it.monopoly.model.player.PlayerModel;
import it.monopoly.model.property.PropertyModel;

public class TradeController {
    private final ManagerController<PlayerModel, PlayerManager> playerController;
    private final ManagerController<PropertyModel, PropertyManager> propertyController;

    public TradeController(PlayerController playerController, PropertyController propertyController) {
        this.playerController = playerController;
        this.propertyController = propertyController;
    }

    public boolean buyProperty(PlayerModel buyer, PropertyModel propertyWithoutOwner) {
        PropertyManager propertyManager = propertyController.getManager(propertyWithoutOwner);
        int price = propertyManager.getPriceManager().getPrice(propertyWithoutOwner);
        return buyPropertyWithoutOwner(buyer, propertyWithoutOwner, price);
    }

    public boolean buyPropertyAfterAuction(PlayerModel buyer, PropertyModel propertyWithoutOwner, int price) {
        return buyPropertyWithoutOwner(buyer, propertyWithoutOwner, price);
    }

    public boolean buyPropertyFromPlayer(PlayerModel buyer, PlayerModel seller, PropertyModel property, int price) {
        PropertyManager propertyManager = propertyController.getManager(property);
        boolean canSell = propertyManager.canSell() && propertyManager.getOwner().equals(seller);
        if (canSell) {
            makeTransaction(seller, buyer, price);
            setNewOwner(buyer, property);
        }
        return canSell;
    }

    public void spendTransaction(PlayerModel debtor, int price) {
        makeTransaction(null, debtor, price);
    }

    public void earnTransaction(PlayerModel creditor, int price) {
        makeTransaction(creditor, null, price);
    }

    public void makeTransaction(PlayerModel creditor, PlayerModel debtor, int price) {
        if (debtor != null) {
            PlayerManager debtorManager = playerController.getManager(debtor);
            debtorManager.spend(price);
        }
        if (creditor != null) {
            PlayerManager creditorManager = playerController.getManager(creditor);
            creditorManager.earn(price);
        }
    }

    private boolean buyPropertyWithoutOwner(PlayerModel buyer, PropertyModel propertyWithoutOwner, int price) {
        boolean hasNoOwner = propertyController.getManager(propertyWithoutOwner).getOwner() == null;
        if (hasNoOwner) {
            spendTransaction(buyer, price);
            setNewOwner(buyer, propertyWithoutOwner);
        }
        return hasNoOwner;
    }

    private void setNewOwner(PlayerModel buyer, PropertyModel property) {
        if (buyer == null) {
            return;
        }
        propertyController.getManager(property).setOwner(buyer);
    }

}
