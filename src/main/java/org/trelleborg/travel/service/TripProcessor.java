package org.trelleborg.travel.service;

import lombok.extern.slf4j.Slf4j;
import org.trelleborg.travel.db.TripDB;
import org.trelleborg.travel.exception.ValidationException;
import org.trelleborg.travel.model.CompanyBusTrip;
import org.trelleborg.travel.model.Touch;
import org.trelleborg.travel.model.Trip;
import org.trelleborg.travel.model.TripStatus;
import org.trelleborg.travel.util.HashUtil;
import org.trelleborg.travel.util.TripValidator;

import java.time.ZoneOffset;
import java.util.*;
import java.util.stream.Collectors;

@Slf4j
public class TripProcessor {
    private TripService tripService;
    private TripDB tripDB;
    private TripValidator tripValidator;

    public TripProcessor(TripService service,
                         TripValidator validator,
                         TripDB db) {
        this.tripService = service;
        this.tripValidator = validator;
        this.tripDB = db;
    }

    public Trip importTrip(Touch touch) {
        Trip trip = null;

        try {
            // TODO: Hash during deserialization to avoid mutate data.
            touch.setPan(HashUtil.hash(touch.getPan()));
            tripValidator.validate(touch);
            trip = tripDB.findFirstPendingTrip(touch.getCompanyId(), touch.getBusId());
            tripValidator.validate(trip, touch);
            if (touch.isOn()) {
                // Not duplicate then create new trip
                // since this touch is ON.
                trip = createNewTrip(touch);
            } else {
                updateTrip(trip, touch);
            }
            updateStatus(trip);
            tripDB.save(trip);
        } catch (ValidationException ex) {
            touch.setStatus(ex.getMessage());
        }

        tripDB.save(touch);

        return trip;
    }

    public List<CompanyBusTrip> createCompanyBusTrips() {
        var trips = tripDB.getAllTrips();
        Map<String, CompanyBusTrip> companyTrips = new HashMap<>();
        for (Trip trip : trips) {
            var companyId = trip.getCompanyId();
            var date = trip.getDate();
            var busId = trip.getStartBus();
            var companyKey = buildCompanyKey(companyId, date, busId);
            var companyTrip = companyTrips.computeIfAbsent(companyKey,
                    k -> CompanyBusTrip
                    .builder()
                    .companyId(companyId)
                    .date(date)
                    .busId(busId)
                    .build());
            companyTrip.addTrip(trip);
        }

        var result = companyTrips
                .values()
                .stream()
                .map(trip -> trip.setIncompletedTrip(calcIncompletedTrip(trip)))
                .sorted(Comparator.comparing(CompanyBusTrip::getDate)
                        .thenComparing(CompanyBusTrip::getCompanyId))
                .collect(Collectors.toList());

        tripDB.saveAll(result);

        return result;
    }

    private Trip createNewTrip(Touch touch) {
        var date = touch.getDateTime()
                .atOffset(ZoneOffset.UTC)
                .toLocalDate()
                .toString();

        return Trip.builder()
                .companyId(touch.getCompanyId())
                .date(date)
                .startBus(touch.getBusId())
                .startTime(touch.getDateTime())
                .fromStopId(touch.getStopId())
                .fromPan(touch.getPan())
                .cost(0.0)
                .status(TripStatus.INCOMPLETE)
                .build();
    }

    private Trip updateTrip(Trip trip, Touch touch) {
        if(trip == null) {
            return trip;
        }
        var hashedPan = HashUtil.hash(touch.getPan());
        if (touch.isOn()) {
            trip.setFromPan(touch.getBusId());
            trip.setStartTime(touch.getDateTime());
            trip.setFromStopId(touch.getStopId());
            trip.setFromPan(hashedPan);
        } else {
            trip.setEndBus(touch.getBusId());
            trip.setEndTime(touch.getDateTime());
            trip.setToStopId(touch.getStopId());
            trip.setToPan(hashedPan);
        }
        return trip;
    }

    private Trip calcIncompletedTrip(CompanyBusTrip companyBusTrip) {
        var pendingTrips = companyBusTrip.getPendingTrips();

        if (pendingTrips.size() <= 1) {
            return null;
        }

        Collections.sort(pendingTrips,
                Comparator.comparing(Trip::getStartTime));

        Trip startTrip = null;
        Trip endTrip = null;
        double maxCost = Double.MIN_VALUE;

        for (Trip currentTrip : pendingTrips) {
            if (startTrip == null) {
                startTrip = currentTrip;
                continue;
            }

            var cost = tripService.getPrice(
                startTrip.getFromStopId(),
                currentTrip.getFromStopId()
            );

            if (cost > maxCost) {
                maxCost = cost;
                endTrip = currentTrip;
            }
        }

        return Trip.builder()
                .companyId(startTrip.getCompanyId())
                .cost(maxCost)
                // Start
                .startTime(startTrip.getStartTime())
                .startBus(startTrip.getStartBus())
                .fromStopId(startTrip.getFromStopId())
                .fromPan(startTrip.getFromPan())
                // Status
                .status(TripStatus.INCOMPLETE)
                // End
                .endBus(endTrip.getStartBus())
                .endTime(endTrip.getStartTime())
                .toStopId(endTrip.getFromStopId())
                .toPan(endTrip.getFromPan())
                .build();
    }

    private void updateStatus(Trip trip) {
        if (trip.isCompleted() || trip.isCancelled()) {
            return;
        }

        if (tripService.isCompleted(trip)) {
            var cost = tripService.getPrice(
                    trip.getFromStopId(),
                    trip.getToStopId()
            );
            trip.setCost(cost);
            trip.setStatus(TripStatus.COMPLETE);
        } else if (tripService.isCancelled(trip)) {
            trip.setCost(0.0);
            trip.setStatus(TripStatus.CANCEL);
        }
    }

    private String buildCompanyKey(String companyId, String date, String busId) {
        return companyId + "|" + date + "|" + busId;
    }
}
