package com.Conmiro.bots.PortableTanning;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;


/**
 * Created by Conmiro on 7/4/2016.
 */
public class PortableTanning extends LoopingScript {


    private GameObject portableCrafter;
    private GameObject bankChest;
    private String color = "Black";


    @Override
    public void onLoop() {

        if (!Inventory.contains(color+" dragonhide")) {
            if (Bank.isOpen()){
                if (Bank.containsAnyOf(color+" dragonhide")) {
                    Keyboard.typeKey('1');
                    Execution.delayUntil(() -> !Bank.isOpen(),3000);
                }else {
                    Logger.error("Bank doesn't contain specified dragonhide.");
                    Execution.delay(3000,5000);
                }
            }else {
                bankChest = Banks.getLoadedBankChests().nearest();
                if (bankChest == null || !bankChest.click()){
                    Logger.error("Problem clicking bank.");
                }else {
                    Execution.delayUntil(Bank::isOpen, 1000);
                }
            }
        }else {
            portableCrafter = GameObjects.newQuery().names("Portable crafter").results().nearest();
            if (Interfaces.newQuery().containers(1370).texts("Tan").results().first() != null) {
                Keyboard.typeKey(' ');
            }else {
                if (portableCrafter != null && portableCrafter.isValid() && portableCrafter.isVisible()){

                    //if (!portableCrafter.interact("Tan Leather", "Portable crafter")){
                    if (!portableCrafter.interact("Tan Leather")) {
                        Logger.error("Interact failed");
                    }else {
                        Execution.delayUntil(() -> Interfaces.newQuery().containers(1370).texts("Tan").results().first() != null, 1000);
                    }
                }else {
                    Logger.error("Portable crafter not found.");
                }
            }
        }
    }

    @Override
    public void onStart(String... args) {
        setLoopDelay(320,400);
        Logger.info("Starting bot.");


    }

    public void onStop() {
        Logger.info("Stopping bot.");
    }



}
