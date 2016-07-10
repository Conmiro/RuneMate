package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.Execution;

/**
 *  Helper class for peforming operations while the G.E. is open.
 * Created by Connor on 7/8/2016.
 */
public class Exchange {

    private final static int grandExchangeContainerId = 105;
    private final static int confirmOfferComponentId = 169;
    private final static int searchResultsComponentId = 53;
    private final static int buyButtonTextureId = 1170;

    /**
     * Old way of doing things.........
     */
    //could base these off their images instead.......
    @Deprecated
    private final static int[] buyComponentIds = {175,186,198,211,227,243,260,277};
    @Deprecated
    private final static int[] sellComponentIds = {180,192,204,217,233,249,283,266};


    /**
     * Opens the Grand Exchange via the nearest clerk
     * @return Success value
     */
    public static Boolean open() {
        Npc clerk = Npcs.newQuery().visible().actions("Exchange").results().nearest();
        return clerk.interact("Exchange", clerk.getName());
    }


    /**
     * Queries interface for number of empty slots.
     * @return Number of empty exchange slots available
     */
    public static int getEmptyCount() {
        return Interfaces.newQuery().visible().textures(buyButtonTextureId).results().size();
    }

    /**
     * Iterates through buy button components and returns first available buy button
     * @return First available buy button interface component
     */
    private static InterfaceComponent getBuyButton() {
        InterfaceComponent comp = Interfaces.newQuery().visible().textures(buyButtonTextureId).results().first();
        if (comp != null && comp.isValid() && comp.isVisible()) {
            return comp;
        }
        return null;
    }

    public static Boolean isOpen() {
        return !Interfaces.newQuery().containers(1477).texts("Grand Exchange").visible().results().isEmpty();
    }

    public static Boolean isOfferOpen() {
        return !Interfaces.newQuery().containers(grandExchangeContainerId).visible().texts("Confirm Offer").results().isEmpty();
    }

    private static Boolean startBuyOffer() {
        InterfaceComponent buyButton = getBuyButton();
        if (buyButton!=null) {
            buyButton.click();
            return Execution.delayUntil(Exchange::isOfferOpen, 2000);
        }
        Logger.error("An available buy button was not found. Buy offer not started.");
        return false;
    }

    private static Boolean validate(InterfaceComponent comp){
        if (comp != null && comp.isValid() && comp.isVisible()){
            return true;
        }
        return false;
    }

    /**
     * Backs out of an offer during creation.
     * @return
     */
    private static Boolean backOutOffer() {
        Logger.debug("Backing out of offer.");
        InterfaceComponent backButton = Interfaces.newQuery().actions("Back").results().first();
        if (validate(backButton)){
            return backButton.click();
        }
        Logger.error("Problem backing out of offer.");
        return false;
    }

    /**
     * Creates a new buy offer for given item
     * in an available exchange slot.
     *
     * @param item Item to create the offer for
     * @return Success value
     */
    public static Boolean buyOffer(String item) {
        if (!isOpen()){
            return false;
        }
        if (!startBuyOffer()){
            return false;
        }

        Execution.delay(1000,2000);

        if (!searchItem(item)) {
            backOutOffer();
            return false;
        }

        Execution.delay(500);

        if (!setQuantity(1)) {
            backOutOffer();
            return false;
        }

        Execution.delay(500);

        if (!confirmOffer()) {
            backOutOffer();
            return false;
        }

        return true;
    }

    private static Boolean confirmOffer() {
        InterfaceComponent confirmButton = getConfirmButton();
        if (confirmButton != null && confirmButton.isValid() && confirmButton.isVisible()){
            return confirmButton.click();
        }
        return false;
    }

    private static InterfaceComponent getConfirmButton() {
        return Interfaces.getAt(grandExchangeContainerId,confirmOfferComponentId);
    }

    private static Boolean setQuantity(int n) {
        //TODO add ability to click other buttons
        InterfaceComponent addOneButton = Interfaces.newQuery().containers(105).visible().actions("Add 1").results().first();
        for (int i = 0; i < n; i++){
            if (!addOneButton.click()){
                Logger.error("Problem clicking incrementer.");
                return false;
            }
        }
        return true;
    }

    private static InterfaceComponent getTargetItemSearchResultButton(String item) {
        return Interfaces.newQuery().containers(105).texts(item).visible().heights(32).widths(121).results().first();
    }

    private static Boolean searchItem(String item) {
        //TODO add logic for if not in the search area.
        //TODO add continuing if narrowed to one result.

        if (!Keyboard.type(item, false)){
            Logger.error("Keyboard typing failed.");
            return false;
        }

        if (!Execution.delayUntil(() -> getTargetItemSearchResultButton(item) != null, 2000)){
            return false;
        }
        InterfaceComponent itemButton = getTargetItemSearchResultButton(item);
        InterfaceComponent searchBounds = Interfaces.getAt(grandExchangeContainerId,searchResultsComponentId);

        if (itemButton == null || searchBounds == null){
            Logger.error("The item could not be found in search results.");
            return false;
        }

        while (itemButton.getBounds().getCenterY() > searchBounds.getBounds().getMaxY()){
            Mouse.move(searchBounds.getBounds().getCenterPoint());
            Mouse.scroll(true);
        }

        return itemButton.click();

    }

    /**
     * Determines whether an item search has been narrowed down to one item or not
     * @return True if narrowed down to one item; else false.
     */
    @Deprecated
    private static Boolean singleResultFound() {
        if (Interfaces.newQuery().containers(105).visible().heights(50).widths(167).results().size()==1){
            return true;
        }
        return false;
    }

}
