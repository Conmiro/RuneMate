package com.Conmiro.bots.GETester;

import com.Conmiro.bots.api.GrandExchange.Exchange.Exchange;
import com.runemate.game.api.hybrid.GameEvents;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.script.framework.LoopingScript;

/**
 * Created by Connor on 7/8/2016.
 */
public class GETester extends LoopingScript {


    @Override
    public void onLoop() {



        if ( Bank.open()) {
            System.out.println("Successfully opened Grand Exchange.");

        }else {
            System.out.println("Failed to open exchange");
        }
    }

    public void onStart(String... args) {
        setLoopDelay(3000,5000);
        GameEvents.RS3.INTERFACE_CLOSER.disable();


    }

    public void onStop() {



    }

}
