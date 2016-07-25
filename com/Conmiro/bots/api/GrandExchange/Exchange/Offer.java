package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.script.Execution;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.Conmiro.bots.api.GrandExchange.Exchange.Constants.*;

/**
 * Deals with the currently displayed Offer. May be an offer
 * currently being created, or an offer that is existing.
 * <p>
 * Created by Connor on 7/24/2016.
 */
public class Offer {

    /**
     * Returns whether an offer page is currently open
     *
     * @return True if open
     */
    public static boolean isOpen() {
        return !Interfaces.newQuery().containers(grandExchangeContainerId).visible().actions("Back").results().isEmpty();
    }

    /**
     * Backs out of an offer screen
     *
     * @return Success
     */
    public static boolean backOut() {
        InterfaceComponent backButton = Interfaces.newQuery().actions("Back").results().first();
        if (backButton != null && backButton.isVisible() && backButton.isValid()) {
            backButton.click();
            return Execution.delayUntil(() -> !isOpen(), 2000);
        }
        return false;
    }

    /**
     * Returns the item name in the currently open offer.
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
        if (itemButton != null && itemButton.isValid() && itemButton.isVisible()) {
            item = GrandExchange.lookup(itemButton.getContainedItemId());
        }
        return item;
    }

    /**
     * Gets the quantity that is being bought or sold.
     *
     * @return Quantity of item
     */
    public static int getQuantity() {
        InterfaceComponent textbox = Interfaces.getAt(grandExchangeContainerId, quantityComponentId);
        if (textbox != null)
            return Integer.parseInt(textbox.getText());
        else
            return -1;
    }

    /**
     * Get the type of offer being displayed.
     *
     * @return Buy or Sell
     */
    public static String getType() {
        if (!Interfaces.newQuery().visible().textContains("Buy Offer").results().isEmpty())
            return "Buy";
        else if (!Interfaces.newQuery().visible().textContains("Sell Offer").results().isEmpty())
            return "Sell";
        else
            return null;
    }

    /**
     * Returns the current offer price per item
     *
     * @return Item offer price
     */
    public static int getCurrentPrice() {
        Pattern pattern = Pattern.compile("(\\d*) gp");
        InterfaceComponent priceBox = Interfaces.newQuery().containers(grandExchangeContainerId).visible().texts(pattern).actions("Enter Number").results().first();
        if (priceBox != null) {
            //Logger.debug("priceBox text: " + priceBox.getText());
            Matcher m = pattern.matcher(priceBox.getText());
            //Logger.debug("Match: " + m.matches());
            //Logger.debug("Group count: " + m.groupCount());
            if (m.matches()) {
                return Integer.parseInt(m.group(1));
            }
        }
        return -1;
    }


}
