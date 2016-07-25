package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.script.Execution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Conmiro.bots.api.GrandExchange.Exchange.Constants.*;

/**
 * Created by Connor on 7/16/2016.
 *
 * Deals with operations when starting a new offer.
 */
public class NewOffer {


    /**
     * Clicks an available buy button to begin offer.
     *
     * @return
     */
    public static Boolean startBuy() {
        OfferSlot slot = OfferSlots.getFree();
        if (slot != null){
            return slot.startBuy();
        }
        return false;
    }

    public static Boolean startSell(String item) {
        Inventory.getItems(item).first().click();
        return Execution.delayUntil(NewOffer::isOpen, 2000);
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
     * Finds the result button for the item being searched for in a grand exchange query.
     *
     * @param item
     * @return
     */
    private static InterfaceComponent getTargetItemSearchResultButton(String item) {
        return Interfaces.newQuery().containers(105).texts(item).visible().heights(32).widths(121).results().first();
    }

    /**
     * Types into the offer search filter
     *
     * @param item
     * @return
     */
    public static Boolean searchItem(String item) {

        InterfaceComponent searchBox = Interfaces.getAt(grandExchangeContainerId, searchBoxComponentId);

        if (!insideSearchBox()){
            Logger.debug("not inside the search box");
            searchBox.click();
        }else {
            //if the text box is empty
            if (searchBox.getText().equals("")){
                Keyboard.type(item, false);
            //if some other item is being searched for
            }else if (!searchBox.getText().equals(item)) {
                for (int i = 0; i < searchBox.getText().length(); i++){
                    Keyboard.typeKey('\b');
                }
            }else {
                return selectItem(item);
            }
        }
        return false;
    }

    /**
     * Selects item from the search results.
     *
     * @return
     */
    private static boolean selectItem(String item) {
        InterfaceComponent itemButton = getTargetItemSearchResultButton(item);
        InterfaceComponent searchBounds = Interfaces.getAt(grandExchangeContainerId, searchResultsComponentId);
        //scroll until item is in view
        while (itemButton.getBounds().getCenterY() > searchBounds.getBounds().getMaxY()) {
            Mouse.move(searchBounds.getBounds().getCenterPoint());
            Mouse.scroll(true);
        }
        itemButton.click();
        return Execution.delayUntil(() -> getCurrentItemName().compareTo(item) == 0, 2000);

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
            confirmButton.click();
            return Execution.delayUntil(() -> !isOpen(), 2000);
        }
        return false;
    }

    /**
     * Backs out of an offer during creation.
     *
     * @return
     */
    public static Boolean backOut() {
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
            //Logger.debug("priceBox text: " + priceBox.getText());
            Matcher m = pattern.matcher(priceBox.getText());
            //Logger.debug("Match: " + m.matches());
            //Logger.debug("Group count: " + m.groupCount());
            return Integer.parseInt(m.group(1));
        }
        return -1;
    }

    private static Boolean insideSearchBox() {
        return Interfaces.newQuery().visible().texts("Click here to search for an item to buy.").results().isEmpty();
    }



}
