package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.script.Execution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Connor on 7/16/2016.
 */
public class Offer {


    //Load in Constants
    private final static int grandExchangeContainerId = Constants.grandExchangeContainerId;
    private final static int buyButtonTextureId = Constants.buyButtonTextureId;
    private final static int sellButtonTextureId = Constants.sellButtonTextureId;
    private final static int searchResultsComponentId = Constants.searchResultsComponentId;
    private final static int confirmOfferComponentId = Constants.confirmOfferComponentId;
    private final static int currentItemComponentId = Constants.currentItemComponentId;
    private final static int quantityComponentId = Constants.quantityComponentId;
    private final static int offerItemComponentId = Constants.offerItemComponentId;

    /**
     * Starts specified type of offer
     *
     * @param type Buy or Sell
     * @return Success status
     */
    public static Boolean start(String type) {
        if (type == "Buy"){
            return startBuy();
        }
        return false;
    }

    public static String getType() {
        if (!Interfaces.newQuery().visible().textContains("Buy Offer").results().isEmpty())
            return "Buy";
        else if (!Interfaces.newQuery().visible().textContains("Sell Offer").results().isEmpty())
            return "Sell";
        else
            return null;
    }

    public static Boolean isOpen() {
        return !Interfaces.newQuery().containers(grandExchangeContainerId).visible().texts("Confirm Offer").results().isEmpty();
    }

    /**
     * Check whether an item is being searched for.
     * @return
     */
    public static Boolean isSearching() {
        return (isOpen() && Interfaces.newQuery().visible().texts("Click here to search for an item to buy.").results().isEmpty());
    }


    /**
     * Sets the quantity of currently buying or selling item.
     *
     * @param n
     * @return
     */
    public static Boolean setQuantity(int n) {
        //TODO add ability to click other buttons
        InterfaceComponent addOneButton = Interfaces.newQuery().containers(105).visible().actions("Add 1").results().first();
        for (int i = 0; i < n; i++) {
            if (!addOneButton.click()) {
                return false;
            }
        }
        return true;
    }

    public static int getQuantity(){
        InterfaceComponent textbox = Interfaces.getAt(grandExchangeContainerId, quantityComponentId);
        if (textbox != null)
            return Integer.parseInt(textbox.getText());
        else
            return -1;
    }

    /**
     * Clicks an available buy button to begin offer.
     *
     * @return
     */
    private static Boolean startBuy() {
        InterfaceComponent buyButton = getBuyButton();
        if (buyButton != null) {
            buyButton.click();
            return Execution.delayUntil(Offer::isOpen, 2000);
        }
        return false;
    }

    /**
     * Finds the result button for the item being searched for in a grand exchange query.
     *
     * @param item
     * @return
     */
    private static InterfaceComponent getTargetItemSearchResultButton(String item) {
        return Interfaces.newQuery().containers(105).texts(item).visible().heights(32).widths(121).results().first();
    }

    /**
     * Types into the offer search filter and clicks the target item.
     *
     * @param item
     * @return
     */
    public static Boolean searchItem(String item) {
        //TODO add logic for if not in the search area.
        //TODO add continuing if narrowed to one result.

        if (!isSearching()){
            InterfaceComponent textbox = Interfaces.newQuery().visible().texts("Click here to search for an item to buy.").results().first();
            if (textbox != null && textbox.isVisible() && textbox.isValid())
                Mouse.click(textbox, Mouse.Button.LEFT);
        }else {
            if (!Keyboard.type(item, false)) {
                return false;
            }
            if (!Execution.delayUntil(() -> getTargetItemSearchResultButton(item) != null, 2000)) {
                return false;
            }
            InterfaceComponent itemButton = getTargetItemSearchResultButton(item);
            InterfaceComponent searchBounds = Interfaces.getAt(grandExchangeContainerId, searchResultsComponentId);

            if (itemButton == null || searchBounds == null) {
                return false;
            }

            while (itemButton.getBounds().getCenterY() > searchBounds.getBounds().getMaxY()) {
                Mouse.move(searchBounds.getBounds().getCenterPoint());
                Mouse.scroll(true);
            }

            return itemButton.click();
        }
        return false;
    }

    /**
     * Iterates through buy button components and returns first available buy button
     *
     * @return First available buy button interface component
     */
    private static InterfaceComponent getBuyButton() {
        InterfaceComponent comp = Interfaces.newQuery().visible().textures(buyButtonTextureId).results().first();
        if (comp != null && comp.isValid() && comp.isVisible()) {
            return comp;
        }
        return null;
    }

    /**
     * Obtains the confirmation button during offer creation.
     *
     * @return
     */
    private static InterfaceComponent getConfirmButton() {
        return Interfaces.getAt(grandExchangeContainerId, confirmOfferComponentId);
    }

    /**
     * Clicks the confirm offer button during offer creation.
     *
     * @return
     */
    public static Boolean confirm() {
        InterfaceComponent confirmButton = getConfirmButton();
        if (confirmButton != null && confirmButton.isValid() && confirmButton.isVisible()) {
            return confirmButton.click();
        }
        return false;
    }

    /**
     * Backs out of an offer during creation.
     *
     * @return
     */
    private static Boolean backOut() {
        Logger.debug("Backing out of offer.");
        InterfaceComponent backButton = Interfaces.newQuery().actions("Back").results().first();
        if (backButton!=null && backButton.isVisible() && backButton.isValid()) {
            return backButton.click();
        }
        Logger.error("Problem backing out of offer.");
        return false;
    }

    /**
     * Returns the item name in the current open offer.
     *
     * @return
     */
    @Deprecated
    public static String getCurrentItemName() {
        InterfaceComponent button = Interfaces.getAt(grandExchangeContainerId, currentItemComponentId);
        if (button != null)
            return button.getText();
        else
            return null;
    }


    /**
     * Returns GrandExchange.Item object for item in current offer.
     * Allows access to name, price, etc.
     *
     * @return
     */
    public static GrandExchange.Item getCurrentItem() {
        InterfaceComponent itemButton = Interfaces.getAt(grandExchangeContainerId, offerItemComponentId);
        GrandExchange.Item item = null;
        if (itemButton!=null && itemButton.isValid() && itemButton.isVisible()) {
            item = GrandExchange.lookup(itemButton.getContainedItemId());
        }
        return item;
    }


    /**
     * Returns the current offer price per item
     *
     * @return
     */
    public static int getCurrentPrice() {
        Pattern pattern = Pattern.compile("(\\d*) gp");
        InterfaceComponent priceBox = Interfaces.newQuery().containers(grandExchangeContainerId).visible().texts(pattern).actions("Enter Number").results().first();
        if (priceBox != null) {
            Matcher m = pattern.matcher(priceBox.getText());
            return Integer.parseInt(m.group(1));
        }
        return -1;
    }



}
