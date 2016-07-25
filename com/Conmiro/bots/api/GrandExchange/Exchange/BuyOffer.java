package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.script.Execution;


import static com.Conmiro.bots.api.GrandExchange.Exchange.Constants.*;

/**
 * Subclass of SetupOffer. Deals with the buying offer page.
 * <p>
 * Created by Connor on 7/16/2016.
 */
public class BuyOffer extends SetupOffer {


    /**
     * Clicks an available buy button to begin offer.
     *
     * @return
     */
    public static boolean start() {
        OfferSlot slot = OfferSlots.getFree();
        if (slot != null) {
            return slot.startBuy();
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
     * Types into the offer search filter
     *
     * @param item
     * @return
     */
    public static boolean searchItem(String item) {

        InterfaceComponent searchBox = Interfaces.getAt(grandExchangeContainerId, searchBoxComponentId);

        if (!insideSearchBox()) {
            Logger.debug("not inside the search box");
            searchBox.click();
        } else {
            //if the text box is empty
            if (searchBox.getText().equals("")) {
                Keyboard.type(item, false);
                //if some other item is being searched for
            } else if (!searchBox.getText().equals(item)) {
                for (int i = 0; i < searchBox.getText().length(); i++) {
                    Keyboard.typeKey('\b');
                }
            } else {
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


    private static boolean insideSearchBox() {
        return Interfaces.newQuery().visible().texts("Click here to search for an item to buy.").results().isEmpty();
    }


}
