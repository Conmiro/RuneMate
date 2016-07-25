package com.Conmiro.bots.ExchangeTester;

import com.Conmiro.bots.api.GrandExchange.Exchange.Exchange;
import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.entities.Item;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;

/**
 * Created by Connor on 7/8/2016.
 */
public class ExchangeTester extends LoopingScript {

    @Override
    public void onLoop() {

        Exchange.sellOffer("Monkfish");

        /*
        if (Exchange.isOpen()){
            if (Exchange.hasCompletedOffers()){
                //Logger.debug("Has completed offers");
                Exchange.collectCompletedOffers();
            }
        }else {
            Exchange.open();
        }*/




    }

    public void onStart(String... args) {
        Logger.info(getMetaData().getName() + " script is starting.");
        Logger.info("This bot disables the interface closer");
        setLoopDelay(250, 400);
        GameEvents.RS3.INTERFACE_CLOSER.disable();
        GameObject test = null;
    }

    public void onStop() {
        Logger.info(getMetaData().getName() + " script is ending.");
    }

}
