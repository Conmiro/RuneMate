package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.entities.definitions.ItemDefinition;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.queries.InterfaceComponentQueryBuilder;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.Execution;
import com.sun.media.jfxmedia.logging.Logger;

/**
 * Created by Connor on 7/8/2016.
 */
public class Exchange {

    private final static int grandExchangeContainerId = 105;
    private final static int confirmOfferComponentId = 169;
    private final static int searchResultsComponentId = 53;

    //could base these off their images instead.......hmmmmmmmmmmmm
    private final static int[] buyComponentIds = {175,186,198,211,227,243,260,277};
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
        return Interfaces.newQuery().containers(grandExchangeContainerId).visible().texts("Empty").results().size();
    }

    /**
     * Iterates through buy button components and returns first available buy button
     * @return First available buy button interface component
     */
    public static InterfaceComponent getBuyButton() {
        for (int i: buyComponentIds) {
            InterfaceComponent comp = Interfaces.getAt(grandExchangeContainerId, i);
            if (comp != null && comp.isValid() && comp.isVisible()){
                System.out.println("Component found: " + comp.getContainedItemId() + "," + comp.getIndex());
                return comp;
            }
        }
        return null;
    }

    public static Boolean isOpen() {
        return !Interfaces.newQuery().containers(1477).texts("Grand Exchange").visible().results().isEmpty();
    }

    public static Boolean isOfferOpen() {
        return !Interfaces.newQuery().containers(grandExchangeContainerId).visible().texts("Confirm Offer").results().isEmpty();
    }

    public static Boolean startBuyOffer() {
        InterfaceComponent buyButton = getBuyButton();
        if (buyButton!=null) {
            buyButton.click();
            return Execution.delayUntil(Exchange::isOfferOpen, 2000);
        }
        System.out.println("Buy button not found");
        return false;
    }

    public static Boolean buyItem(String item) {
        if (!isOpen()){
            return false;
        }
        if (!startBuyOffer()){
            System.out.println("Failed to start buy offer");
            return false;
        }

        Execution.delay(1000,2000);

        if (!searchItem(item)) {
            System.out.println("Failed to search for item");
            return false;
        }
        if (!setQuantity(1))
            return false;
        if (!confirmOffer())
            return false;

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
            System.out.println("Failed to type.");
            return false;
        }

        if (!Execution.delayUntil(() -> getTargetItemSearchResultButton(item) != null, 2000)){
            return false;
        }

        InterfaceComponent itemButton = getTargetItemSearchResultButton(item);
        InterfaceComponent searchBounds = Interfaces.getAt(grandExchangeContainerId,searchResultsComponentId);

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