package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.Execution;


import java.util.LinkedList;
import java.util.List;

import static com.Conmiro.bots.api.GrandExchange.Exchange.Constants.*;

/**
 * Helper class for performing operations while the G.E. is open.
 * Created by Connor on 7/8/2016.
 */
public class Exchange {


    /**
     * Opens the Grand Exchange via the nearest clerk
     *
     * @return Success value
     */
    public static Boolean open() {
        Npc clerk = Npcs.newQuery().visible().actions("Exchange").results().nearest();
        clerk.interact("Exchange", clerk.getName());
        return Execution.delayUntil(Exchange::isOpen,500);
    }

    /**
     * Checks whether the Grand Exchange interface is open or not.
     *
     * @return True if Grand Exchange is open.
     */
    public static Boolean isOpen() {
        return !Interfaces.newQuery().containers(1477).texts("Grand Exchange").visible().results().isEmpty();
    }


    //Currently assuming item is in inventory
    public static Boolean sellOffer(String item) {
        if (!Exchange.isOpen()) {
            Exchange.open();
        }else {
            if (!NewOffer.isOpen()){
                if (Inventory.contains(item)){
                    NewOffer.startSell(item);
                }
            }else{
                if (!NewOffer.getCurrentItemName().equals(item)){
                    NewOffer.backOut();
                }else {
                    return NewOffer.confirm();
                }
            }

        }
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

        if (!Exchange.isOpen()){
            Logger.status("Opening");
            Exchange.open();
        }else {
            if (!NewOffer.isOpen()) {
                Logger.status("Starting buy offer");
                NewOffer.startBuy();
            }else {
                if (NewOffer.getCurrentItemName().compareToIgnoreCase(item) != 0){
                    Logger.status("Searching for item");
                    NewOffer.searchItem(item);
                }else {
                    if (NewOffer.getQuantity() != 1){
                        Logger.status("Setting quantity");
                        NewOffer.setQuantity(1);
                    }else {
                        Logger.info("Buying " + NewOffer.getCurrentItem().getName() + " for " + NewOffer.getCurrentPrice() + " gp.");
                        Logger.info("Current Grand Exchange Price is: " + NewOffer.getCurrentItem().getPrice());
                        Logger.status("Confirming offer");
                        return NewOffer.confirm();
                    }
                }
            }
        }
        return false;
    }


    /**
     *
     * @return True if all completed offers have been collected from.
     */

    public static Boolean collectCompletedOffers() {
        if (OfferSlots.getCompletedCount() == 0){
            return true;
        }else {
            //If for some reason in the create offer page
            if (NewOffer.isOpen()) {
                NewOffer.backOut();
            }else{
                if (Offer.isOpen()) {
                    //If viewing an offer that does not have collectible items
                    if (!Offer.hasCollectibles()){
                        Offer.backOut();
                    }else {
                        Offer.collect();
                    }
                } else {
                    for (OfferSlot slot : OfferSlots.getAll()) {
                        if (slot.isCompleted()) {
                            slot.click();
                            Execution.delayUntil(Offer::isOpen, 1000);
                        }
                    }
                }
            }
        }

        return false;
    }



}
