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
 * Helper class for performing operations while the G.E. is open.
 * Created by Connor on 7/8/2016.
 */
public class Exchange {


    private final static int buyButtonTextureId = Constants.buyButtonTextureId;


    /**
     * Opens the Grand Exchange via the nearest clerk
     *
     * @return Success value
     */
    public static Boolean open() {
        Npc clerk = Npcs.newQuery().visible().actions("Exchange").results().nearest();
        return clerk.interact("Exchange", clerk.getName());
    }

    /**
     * Checks whether the Grand Exchange interface is open or not.
     *
     * @return True if Grand Exchange is open.
     */
    public static Boolean isOpen() {
        return !Interfaces.newQuery().containers(1477).texts("Grand Exchange").visible().results().isEmpty();
    }

    /**
     * Queries interface for number of empty slots.
     *
     * @return Number of empty exchange slots available
     */
    public static int getFreeSlots() {
        return Interfaces.newQuery().visible().textures(buyButtonTextureId).results().size();
    }

    /**
     * Creates a new buy offer for given item
     * in an available exchange slot.
     *
     * @param item Item to create the offer for
     * @return Success value
     */
    public static Boolean buyOffer(String item) {

        if (!Exchange.isOpen()){
            Logger.status("Opening");
            Exchange.open();
        }else {
            if (!Offer.isOpen()) {
                Logger.status("Starting buy offer");
                Offer.start("Buy");
            }else {
                if (Offer.currentItem().compareToIgnoreCase(item) != 0){
                    Logger.status("Searching for item");
                    Offer.searchItem(item);
                }else {
                    if (Offer.getQuantity() != 1){
                        Logger.status("Setting quantity");
                        Offer.setQuantity(1);
                    }else {
                        Logger.status("Confirming offer");
                        return Offer.confirm();
                    }
                }
            }
        }
        return false;
    }









}
