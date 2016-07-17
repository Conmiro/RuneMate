package com.Conmiro.bots.PortableTanning;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Keyboard;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.hud.interfaces.*;
import com.runemate.game.api.hybrid.region.Banks;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import java.time.Instant;


/**
 * Created by Conmiro on 7/4/2016.
 */
public class PortableTanning extends LoopingScript implements ChatboxListener {

    //TODO Add ability to select hide colors in runemate interface
    private String color = "Black"; //color of hide to tan
    private int failed, success = 0; //tracker numbers
    private Instant startTime; //start time

    private Boolean batchComplete = false;

    @Override
    public void onLoop() {
        GameObject portableCrafter = GameObjects.newQuery().names("Portable crafter").results().nearest();
        GameObject bankChest = Banks.getLoadedBankChests().nearest();

        //If inventory has no dragonhide.
        if (!Inventory.contains(color+" dragonhide")) {
            //If bank is open
            if (Bank.isOpen()){
                //If there are hides left.
                if (Bank.containsAnyOf(color+" dragonhide")) {
                    //Quick withdraw
                    Keyboard.typeKey('1');
                    //Delay until bank is closed
                    Execution.delayUntil(() -> !Bank.isOpen(),3000);
                }else {
                    Logger.error("Out of dragonhide!");
                    stop();
                }
            }else {
                //Click the bank
                bankChest.click();
                //Pause for realistic amount of time before moving mouse.
                humanPause();
                //Start to move mouse towards crafter
                Mouse.move(portableCrafter);
                //Let the bank open
                Execution.delayUntil(Bank::isOpen,500);
            }
        }else {
            //If the tanner interface is open
            if (Interfaces.newQuery().containers(1370).texts("Tan").results().first() != null) {
                //Quick tan
                Keyboard.typeKey(' ');
                //Delay until tanner prints message
                Execution.delayUntil(() -> batchComplete,500);
                batchComplete = false;
            }else {
                //Check null, valid, visibility
                if (validate(portableCrafter)){
                    //Interact with crafter
                    if (portableCrafter.interact("Tan Leather")) {
                        humanPause();
                        Mouse.move(bankChest);
                        //Delay until interface is open
                        Execution.delayUntil(() -> Interfaces.newQuery().containers(1370).texts("Tan").results().first() != null, 500);
                    }else {
                        failed++;
                    }
                }else {
                    Logger.error("No portable crafter!");
                    stop();
                }
            }
        }
    }

    private void humanPause() {
        Execution.delay(75,150);
    }

    @Override
    public void onStart(String... args) {
        setLoopDelay(100,200);
        Logger.info("Starting bot.");
        getEventDispatcher().addListener(this);
        Logger.setLogLevel(4);
        startTime = Instant.now();

    }

    public void onStop() {
        Logger.info("Stopping bot.");
    }

    public Boolean validate(GameObject object){
        if (object != null & object.isVisible() && object.isVisible())
            return true;
        else
            return false;
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        String cmp = "The tanner tans 28 " + color + " dragonhides for you.";
        if (messageEvent.getMessage().compareToIgnoreCase(cmp)==0){
            //Logger.info("Successfully tanned dragonhides.");
            success++;
            batchComplete = true;
            Long timeDif = Instant.now().getEpochSecond() - startTime.getEpochSecond();
            Float hidesPerHour = (float)success*28/(float)timeDif*60*60;
            Logger.debug("Hides per hour: " + hidesPerHour);

        }
    }
}
