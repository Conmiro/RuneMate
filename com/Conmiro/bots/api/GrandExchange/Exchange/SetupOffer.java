package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.script.Execution;

import static com.Conmiro.bots.api.GrandExchange.Exchange.Constants.confirmOfferComponentId;
import static com.Conmiro.bots.api.GrandExchange.Exchange.Constants.grandExchangeContainerId;

/**
 * Deals with the setup offer pages for either buy or sell.
 * <p>
 * Created by Connor on 7/24/2016.
 */
public class SetupOffer extends Offer {


    /**
     * Sets the quantity of currently buying or selling item.
     *
     * @param n
     * @return
     */
    public static boolean setQuantity(int n) {
        //TODO add ability to click other buttons
        InterfaceComponent addOneButton = Interfaces.newQuery().containers(105).visible().actions("Add 1").results().first();
        for (int i = 0; i < n; i++) {
            if (!addOneButton.click()) {
                return false;
            }
        }
        return true;
    }


    public static boolean isOpen() {
        InterfaceComponent confirmButton = getConfirmButton();
        if (confirmButton != null && confirmButton.isVisible() && confirmButton.isValid())
            return true;
        return false;
    }

    /**
     * Obtains the confirmation button during offer creation.
     *
     * @return
     */
    private static InterfaceComponent getConfirmButton() {
        return Interfaces.getAt(grandExchangeContainerId, confirmOfferComponentId);
    }

    /**
     * Clicks the confirm offer button during offer creation.
     *
     * @return
     */
    public static boolean confirm() {
        InterfaceComponent confirmButton = getConfirmButton();
        if (confirmButton != null && confirmButton.isValid() && confirmButton.isVisible()) {
            confirmButton.click();
            return Execution.delayUntil(() -> !isOpen(), 2000);
        }
        return false;
    }


}
