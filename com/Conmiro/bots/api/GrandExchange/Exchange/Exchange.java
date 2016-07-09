package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.runemate.game.api.hybrid.entities.Npc;
import com.runemate.game.api.hybrid.local.hud.interfaces.Bank;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.region.Npcs;
import com.sun.media.jfxmedia.logging.Logger;

/**
 * Created by Connor on 7/8/2016.
 */
public class Exchange {

    /**
     * Opens the Grand Exchange via the nearest clerk
     * @return
     */
    public static Boolean open() {
        Npc clerk = Npcs.newQuery().visible().actions("Exchange").results().nearest();
        return clerk.interact("Exchange", clerk.getName());
    }

    /**
     *
     * @return
     */
    public static int getEmptyCount() {
        return 0;
    }

}
