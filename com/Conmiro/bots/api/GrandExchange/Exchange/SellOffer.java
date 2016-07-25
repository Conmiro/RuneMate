package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.runemate.game.api.hybrid.local.hud.interfaces.Inventory;
import com.runemate.game.api.script.Execution;


/**
 * Deals with the sell offer page.
 * <p>
 * Created by Connor on 7/24/2016.
 */
public class SellOffer extends SetupOffer {

    public static Boolean start(String item) {
        Inventory.getItems(item).first().click();
        return Execution.delayUntil(BuyOffer::isOpen, 2000);
    }

}
