package com.Conmiro.bots.api.GrandExchange.Exchange;

import com.runemate.game.api.hybrid.entities.details.Interactable;
import com.runemate.game.api.hybrid.entities.details.Locatable;
import com.runemate.game.api.hybrid.location.Area;
import com.runemate.game.api.hybrid.location.Coordinate;
import com.runemate.game.api.hybrid.util.calculations.Distance;

/**
 * Created by Connor on 7/23/2016.
 */
public class OfferSlot implements Locatable, Interactable {

    private final static int grandExchangeContainerId = Constants.grandExchangeContainerId;
    private int componentId;


    public int getComponentId() {
        return componentId;
    }


    public Coordinate getPosition() {
        return null;
    }

    public Coordinate.HighPrecision getHighPrecisionPosition() {
        return null;
    }

    public Area.Rectangular getArea() {
        return null;
    }
}
