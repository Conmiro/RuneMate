package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.hybrid.region.Npcs;
import com.runemate.game.api.script.Execution;

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
    public static boolean open() {
        Npc clerk = Npcs.newQuery().visible().actions("Exchange").results().nearest();
        if (clerk!=null) {
            clerk.interact("Exchange", clerk.getName());
            return Execution.delayUntil(Exchange::isOpen, 500);
        }
        return false;
    }

    /**
     * Checks whether the Grand Exchange interface is open or not.
     *
     * @return True if Grand Exchange is open.
     */
    public static boolean isOpen() {
        return !Interfaces.newQuery().containers(1477).texts("Grand Exchange").visible().results().isEmpty();
    }


    /**
     * Creates a new sell offer with default quantity and price for
     * the given item.
     *
     * @param item Item to create sell offer for.
     * @return Success
     */
    public static boolean sellOffer(String item) {
        if (!Exchange.isOpen()) {
            Exchange.open();
        } else {
            if (!SellOffer.isOpen()) {
                if (Inventory.contains(item)) {
                    SellOffer.start(item);
                }
            } else {
                if (!SellOffer.getCurrentItemName().equals(item)) {
                    SellOffer.backOut();
                } else {
                    return SellOffer.confirm();
                }
            }

        }
        return false;
    }

    /**
     * Creates a new buy offer with quantity of 1 and default price
     * for the given item.
     *
     * @param item Item to create buy offer for.
     * @return Success
     */
    public static boolean buyOffer(String item) {

        if (!Exchange.isOpen()) {
            //Logger.status("Opening");
            Exchange.open();
        } else {
            if (!BuyOffer.isOpen()) {
                // Logger.status("Starting buy offer");
                BuyOffer.start();
            } else {
                if (BuyOffer.getCurrentItemName().compareToIgnoreCase(item) != 0) {
                    // Logger.status("Searching for item");
                    BuyOffer.searchItem(item);
                } else {
                    if (BuyOffer.getQuantity() != 1) {
                        // Logger.status("Setting quantity");
                        BuyOffer.setQuantity(1);
                    } else {
                        // Logger.status("Confirming offer");
                        Logger.info("Buying " + BuyOffer.getCurrentItem().getName() + " for " + BuyOffer.getCurrentPrice() + " gp.");
                        Logger.info("Current Grand Exchange Price is: " + BuyOffer.getCurrentItem().getPrice());
                        return BuyOffer.confirm();
                    }
                }
            }
        }
        return false;
    }

    /**
     * Collets items and coins from fully completed offers.
     *
     * @return Success
     */
    public static boolean collectCompletedOffers() {
        if (OfferSlots.getCompletedCount() == 0) {
            return true;
        } else {
            //If for some reason in the create offer page
            if (SetupOffer.isOpen()) {
                Offer.backOut();
            } else {
                if (PendingOffer.isOpen()) {
                    //If viewing an offer that does not have collectible items
                    if (!PendingOffer.hasCollectibles()) {
                        PendingOffer.backOut();
                    } else {
                        PendingOffer.collect();
                    }
                } else {
                    for (OfferSlot slot : OfferSlots.getAll()) {
                        if (slot.isCompleted()) {
                            slot.click();
                            Execution.delayUntil(PendingOffer::isOpen, 1000);
                        }
                    }
                }
            }
        }
        return false;
    }


}
