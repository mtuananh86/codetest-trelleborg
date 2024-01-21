package org.trelleborg.travel.model;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@ToString
@Builder(toBuilder = true)
public class CompanyBusTrip {
    private String companyId;
    private String date;
    private String busId;

    @Builder.Default
    private List<Trip> completedTrips = new ArrayList<>();

    @Builder.Default
    private List<Trip> cancelledTrips = new ArrayList<>();

    @Builder.Default
    private List<Trip> pendingTrips = new ArrayList<>();

    private Trip incompletedTrip;

    public CompanyBusTrip addTrip(Trip trip) {
        var tripStatus = trip.getStatus();
        switch (tripStatus) {
            case COMPLETE -> completedTrips.add(trip);
            case CANCEL -> cancelledTrips.add(trip);
            case INCOMPLETE -> pendingTrips.add(trip);
        }
        return this;
    }

    public List<Trip> getCompletedTrips() {
        return completedTrips.stream().collect(Collectors.toList());
    }

    public List<Trip> getCancelledTrips() {
        return cancelledTrips.stream().collect(Collectors.toList());
    }

    public List<Trip> getPendingTrips() {
        return pendingTrips.stream().collect(Collectors.toList());
    }

    public CompanyBusTrip setIncompletedTrip(Trip incompletedTrip) {
        this.incompletedTrip = incompletedTrip;
        return this;
    }

    public Double totalCharges() {
        var total = incompletedTrip != null ? incompletedTrip.getCost() : 0.0;
        total += completedTrips.stream()
                .map(trip -> trip.getCost())
                .mapToDouble(t -> t.doubleValue())
                .sum();
        return total;
    }
}
