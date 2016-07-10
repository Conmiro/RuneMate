package com.Conmiro.bots.GETester;

import com.Conmiro.bots.api.GrandExchange.Exchange.Exchange;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;

/**
 * Created by Connor on 7/8/2016.
 */
public class GETester extends LoopingScript {


    @Override
    public void onLoop() {
        if (!Exchange.isOpen()){
            System.out.println("Grand exchange is not open");
            Exchange.open();
            Execution.delayUntil(GrandExchange::isOpen,2000);
        }else {
            System.out.println("Grand exchange is open");
            if (!Exchange.isOfferOpen()){
                System.out.println("Starting buy offer");
                Exchange.buyItem("Boots");
                Execution.delay(2000);
            }
        }
    }

    public void onStart(String... args) {
        setLoopDelay(250,400);
        GameEvents.RS3.INTERFACE_CLOSER.disable();
        System.out.println("GE Tester Started");
    }

    public void onStop() {



    }

}
