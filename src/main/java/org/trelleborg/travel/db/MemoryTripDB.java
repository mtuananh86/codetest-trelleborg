package org.trelleborg.travel.db;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import org.trelleborg.travel.model.CompanyBusTrip;
import org.trelleborg.travel.model.Touch;
import org.trelleborg.travel.model.Trip;

import java.util.*;
import java.util.stream.Collectors;

public class MemoryTripDB implements TripDB {
    private Multimap<String, Trip> tripMap = ArrayListMultimap.create();
    private Map<String, CompanyBusTrip> companyTrips = new HashMap<>();
    private Map<String, Touch> touchData = new HashMap<>();

    public List<Trip> getAllTrips() {
        return tripMap.values()
                .stream()
                .sorted(Comparator.comparing(Trip::getStartTime))
                .collect(Collectors.toList());
    }

    @Override
    public Trip findFirstPendingTrip(String companyId, String busId) {
        var key = buildTripKey(companyId, busId);
        var trips = tripMap.get(key);
        if (trips != null) {
            return trips.stream()
                    .filter(trip -> trip != null
                            && companyId.equals(trip.getCompanyId())
                            && busId.equals(trip.getStartBus())
                            && trip.isPending())
                    .findFirst()
                    .orElse(null);
        }
        return null;
    }

    @Override
    public List<CompanyBusTrip> getAllCompanyBusTrips() {
        return companyTrips.values()
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public List<Touch> getAllErrorTouches() {
        return touchData.values().stream()
                .filter(touch -> touch.getStatus() != null)
                .toList();
    }

    @Override
    public Trip save(Trip trip) {
        var key = buildTripKey(trip.getCompanyId(), trip.getStartBus());
        var tripList = tripMap.get(key);
        if (tripList != null && !tripList.contains(trip)) {
            tripMap.put(key, trip);
        }
        return trip;
    }

    @Override
    public CompanyBusTrip save(CompanyBusTrip trip) {
        var key = buildCompanyTripKey(trip.getCompanyId(), trip.getDate(), trip.getBusId());
        companyTrips.put(key, trip);
        return trip;
    }

    @Override
    public Touch save(Touch touch) {
        touchData.put(touch.getId(), touch);
        return touch;
    }

    @Override
    public void saveAll(Collection<CompanyBusTrip> trips) {
        trips.forEach(trip -> save(trip));
    }

    @Override
    public void deleteAll() {
        tripMap.clear();
        companyTrips.clear();
        touchData.clear();
    }

    private String buildCompanyTripKey(String companyId, String date, String busId) {
        return companyId + "|" + date + "|" + busId;
    }

    private String buildTripKey(String companyId, String busId) {
        return companyId + "|" + busId;
    }
}
