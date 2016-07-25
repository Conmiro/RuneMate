package com.Conmiro.bots.api.GrandExchange.Exchange;

import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import static com.Conmiro.bots.api.GrandExchange.Exchange.Constants.firstSlotComponentId;
import static com.Conmiro.bots.api.GrandExchange.Exchange.Constants.lastSlotComponentId;

/**
 * Created by Connor on 7/24/2016.
 */
public class OfferSlots {

    public static OfferSlot getFree() {
        for (OfferSlot slot : getAll()) {
            if (slot.getType().equals("Empty"))
                return slot;
        }
        return null;
    }

    public static List<OfferSlot> getAll() {
        List<OfferSlot> slots = new LinkedList<>();
        int numberSlots = lastSlotComponentId - firstSlotComponentId;
        for (int i = 1; i <= numberSlots; i++){
            slots.add(new OfferSlot(i));
        }
        return slots;
    }

    public static int getFreeCount() {
        return getAll().size();
    }

    public static int getCompletedCount() {
        int i = 0;
        for (OfferSlot slot: getAll()) {
            if (slot.isCompleted()){
                i++;
            }
        }
        return i;
    }

    public static int getEmptyCount() {
        return getFreeCount();
    }



}
