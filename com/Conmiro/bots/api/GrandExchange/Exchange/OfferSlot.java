package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.Conmiro.bots.api.Logging.Logger.Logger;
import com.runemate.game.api.hybrid.local.hud.interfaces.InterfaceComponent;
import com.runemate.game.api.hybrid.local.hud.interfaces.Interfaces;
import com.runemate.game.api.hybrid.queries.results.InterfaceComponentQueryResults;
import com.runemate.game.api.script.Execution;

import static com.Conmiro.bots.api.GrandExchange.Exchange.Constants.*;


/**
 * Created by Connor on 7/23/2016.
 */
public class OfferSlot{



    private int componentId;
    private int slotNumber;


    public OfferSlot(int slotNumber) {
        this.slotNumber = slotNumber;
        this.componentId = firstSlotComponentId + slotNumber - 1;

    }

    public boolean isCompleted() {
        InterfaceComponent statusBar = Interfaces.getAt(grandExchangeContainerId, this.componentId, offerStatusBarComponentId);
        if (statusBar != null && statusBar.getTextColor().equals(offerCompletedColor)){
            return true;
        }
        return false;
    }

    public boolean isInProgress() {
        InterfaceComponent statusBar = Interfaces.getAt(grandExchangeContainerId, this.componentId, offerStatusBarComponentId);
        if (statusBar != null && statusBar.getTextColor().equals(offerInProgressColor)){
            return true;
        }
        return false;
    }

    public String getType() {
        InterfaceComponent typeTitle = Interfaces.getAt(grandExchangeContainerId, this.componentId, offerTypeComponentId);
        if (typeTitle != null){
            return typeTitle.getText();
        }
        return null;
    }

    public boolean click() {
        if (this.getType().equals("Buy") || this.getType().equals("Sell")) {
            InterfaceComponent offerBox = Interfaces.getAt(grandExchangeContainerId, this.componentId);
            return offerBox.click();
        }
        Logger.error("Offer is not clickable. Make sure the offer is not empty.");
        return false;
    }

    public boolean startBuy() {
        InterfaceComponent offerBox = Interfaces.getAt(grandExchangeContainerId, this.componentId);
        InterfaceComponentQueryResults buttons = Interfaces.newQuery().visible().textures(buyButtonTextureId).results();
        for (InterfaceComponent button : buttons) {
            if (offerBox.getBounds().contains(button.getBounds())){
                button.click();
                return Execution.delayUntil(PendingOffer::isOpen, 2000);
            }
        }
        return false;
    }

    //todo get status

    public int getComponentId() {
        return componentId;
    }

    public int getSlotNumber() { return slotNumber; }

    //TODO getItemName, getItem, getQuantity, getTotalPrice


}
