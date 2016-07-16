package com.Conmiro.bots.GeneralTester;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.entities.GameObject;
import com.runemate.game.api.hybrid.input.Mouse;
import com.runemate.game.api.hybrid.local.Screen;
import com.runemate.game.api.hybrid.local.hud.Menu;
import com.runemate.game.api.hybrid.local.hud.MenuItem;
import com.runemate.game.api.hybrid.local.hud.interfaces.Chatbox;
import com.runemate.game.api.hybrid.region.GameObjects;
import com.runemate.game.api.script.Execution;
import com.runemate.game.api.script.framework.LoopingScript;
import com.runemate.game.api.script.framework.listeners.ChatboxListener;
import com.runemate.game.api.script.framework.listeners.events.MessageEvent;
import javafx.scene.input.MouseButton;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * Created by Connor on 7/10/2016.
 */
public class GeneralTester extends LoopingScript implements ChatboxListener {

    GameObject portableCrafter = null;
    int attempts = 0;
    int failed = 0;
    int realClicks = 0;

    @Override
    public void onLoop() {
        portableCrafter = GameObjects.newQuery().names("Portable crafter").results().nearest();
        if (!portableCrafter.interact("Tan Leather"))
            failed++;
        attempts++;
        Logger.debug("Failed/Attempts: " + failed+"/"+attempts);

        Execution.delay(300,500);
        Mouse.move(Screen.getBounds().getCenterPoint());
        Execution.delay(300,500);
        Mouse.click(Mouse.Button.RIGHT);
    }

    public void onStart(String... args) {
        setLoopDelay(1500,2000);

        getEventDispatcher().addListener(this);


    }



    @Override
    public void onMessageReceived(MessageEvent messageEvent) {
        if (messageEvent.getMessage().compareTo("You do not have any leather to tan.") == 0){
            Logger.debug("SUCCESSFULLY CLICKED");
            realClicks++;
            Logger.debug("Real Clicks: " + realClicks);
        }
    }
}

