package com.Conmiro.bots.GETester;

import com.Conmiro.bots.api.GrandExchange.Exchange.Exchange;
import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;

/**
 * Created by Connor on 7/8/2016.
 */
public class GETester extends LoopingScript {

    @Override
    public void onLoop() {
        if (!Exchange.isOpen()) {
            //Logger.debug("Grand exchange not open");
            Exchange.open();
            Execution.delayUntil(GrandExchange::isOpen, 2000);
        } else {
            //Logger.debug("Grand exchange is open");
            if (!Exchange.buyOffer("Boots")){
                Logger.error("Problem buying item");
            }
            Execution.delay(2000);
        }
    }

    public void onStart(String... args) {
        Logger.info(getMetaData().getName() + " script is starting.");
        Logger.info("This bot disables the interface closer");
        setLoopDelay(250, 400);
        GameEvents.RS3.INTERFACE_CLOSER.disable();

    }

    public void onStop() {
        Logger.info(getMetaData().getName() + " script is ending.");
    }

}
