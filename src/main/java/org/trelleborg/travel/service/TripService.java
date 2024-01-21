package org.trelleborg.travel.service;

import org.trelleborg.travel.model.StopPoint;
import org.trelleborg.travel.model.Trip;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static org.trelleborg.travel.model.StopPoint.StopA;
import static org.trelleborg.travel.model.StopPoint.StopB;
import static org.trelleborg.travel.model.StopPoint.StopC;

public class TripService {
    private static final Map<String, Double> priceMap = new HashMap<>();

    static {
        registerPrice(StopA, StopB, 4.5);
        registerPrice(StopB, StopC, 6.25);
        registerPrice(StopA, StopC, 8.45);
    }

    private static void registerPrice(StopPoint firstPoint, StopPoint secondPoint, double price) {
        priceMap.put(firstPoint.name() + secondPoint, price);
        priceMap.put(secondPoint.name() + firstPoint, price);
    }

    public double getPrice(StopPoint firstPoint, StopPoint secondPoint) {
        return priceMap.get(firstPoint.name() + secondPoint.name());
    }

    public boolean isCompleted(Trip trip) {
        return  trip.getStartBus() != null && trip.getEndBus() != null &&
                !Objects.equals(trip.getFromStopId(), trip.getToStopId());
    }

    public boolean isCancelled(Trip trip) {
        return  trip.getStartBus() != null
                && trip.getEndBus() != null
                && Objects.equals(trip.getFromStopId(), trip.getToStopId());
    }
}
