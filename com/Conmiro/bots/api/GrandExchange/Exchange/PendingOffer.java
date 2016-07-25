package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;


import static com.Conmiro.bots.api.GrandExchange.Exchange.Constants.*;

/**
 * This deals with an offer that has already been made and is in progress
 * or partially/fully completed.
 * <p>
 * Created by Connor on 7/24/2016.
 */
public class PendingOffer extends Offer{


    public static Boolean isOpen() {
        InterfaceComponent inOfferStatusBar = Interfaces.getAt(grandExchangeContainerId, inOfferStatusBarComponentId);
        if (inOfferStatusBar != null && inOfferStatusBar.isVisible() && inOfferStatusBar.isValid())
            return true;
        return false;
    }

    public static Boolean collect() {
        if (isOpen()) {
            InterfaceComponent box1 = Interfaces.getAt(grandExchangeContainerId, collectionBox1ComponentId);
            InterfaceComponent box2 = Interfaces.getAt(grandExchangeContainerId, collectionBox2ComponentId);
            if (box1 != null && box1.isVisible() && box1.isValid() && box1.getContainedItem() != null) {
                return box1.click();
            }

            if (box2 != null && box2.isVisible() && box2.isValid() && box2.getContainedItem() != null) {
                return box2.click();
            }
        }
        return false;
    }

    /**
     * Returns whether or not the current offer has items to collect.
     *
     * @return
     */
    public static Boolean hasCollectibles() {
        if (isOpen()) {
            InterfaceComponent box1 = Interfaces.getAt(grandExchangeContainerId, collectionBox1ComponentId);
            InterfaceComponent box2 = Interfaces.getAt(grandExchangeContainerId, collectionBox2ComponentId);
            if (box1 != null && box1.isVisible() && box1.isValid() && box1.getContainedItem() != null)
                return true;
            if (box2 != null && box2.isVisible() && box2.isValid() && box2.getContainedItem() != null)
                return true;
        }
        return false;
    }


}




