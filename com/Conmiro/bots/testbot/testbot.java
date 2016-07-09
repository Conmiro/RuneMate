package com.Conmiro.bots.testbot;

import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
import com.runemate.game.api.hybrid.net.GrandExchange;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;


/**
 * Created by Conmiro on 7/4/2016.
 */
public class testbot extends LoopingScript {


    private GameObject portableCrafter;
    private GameObject bankChest;
    private String color = "Black";


    @Override
    public void onLoop() {



        if (!Inventory.contains(color+" dragonhide")) {
            if (Bank.isOpen()){
                if (Bank.containsAnyOf(color+" dragonhide")) {
                    Keyboard.typeKey('1');
                }else {
                    System.out.println("Out of resources...Possibly Exiting.");
                    Execution.delay(300,500);
                    if (!Bank.containsAnyOf(color+" dragonhide")){
                        System.out.println("Exiting!");
                        //stop();
                    }

                }
            }else {
                bankChest.click();
                Execution.delayUntil(() -> Bank.isOpen(), 1000);
            }
        }else {
            portableCrafter = GameObjects.newQuery().names("Portable crafter").results().nearest();
            if (Interfaces.newQuery().containers(1370).texts("Tan").results().first() != null) {
                Keyboard.typeKey(' ');
            }else {
                if (portableCrafter.isValid() && portableCrafter.isVisible()){
                    if (!portableCrafter.interact("Tan Leather", "Portable crafter")){
                        System.out.println("Interact failed.");
                    }else {
                        Execution.delayUntil(() -> Interfaces.newQuery().containers(1370).texts("Tan").results().first() != null, 1000);
                    }
                }else {
                    System.out.println("No Portable Crafter...Possibly Exiting.");
                    Execution.delay(300,500);
                    if (!portableCrafter.isValid() && !portableCrafter.isVisible()){
                        System.out.println("Exiting!");
                        //stop();
                    }
                }
            }
        }
    }

    @Override
    public void onStart(String... args) {
        setLoopDelay(320,400);


        //maybe this ohnly works for one portable crafter?
        portableCrafter = GameObjects.newQuery().names("Portable crafter").results().nearest();
        bankChest = Banks.getLoadedBankChests().nearest();


        if (bankChest == null || portableCrafter == null) {
            System.out.println("Error. bankChest or portableCrafter was not found.");
            System.out.println("Exiting");
            stop();
        }

        System.out.println("Bot successfully started. 3");


    }

    public void onStop(){
        System.out.println("Stopping bot...");
    }



}
