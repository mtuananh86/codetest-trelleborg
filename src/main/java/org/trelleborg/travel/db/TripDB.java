package org.trelleborg.travel.db;

import org.trelleborg.travel.model.CompanyBusTrip;
import org.trelleborg.travel.model.Touch;
import org.trelleborg.travel.model.Trip;

import java.util.Collection;
import java.util.List;

public interface TripDB {

    List<Trip> getAllTrips();

    List<CompanyBusTrip> getAllCompanyBusTrips();

    List<Touch> getAllErrorTouches();

    Trip findFirstPendingTrip(String companyId,
                              String busId);

    Trip save(Trip trip);

    CompanyBusTrip save(CompanyBusTrip trip);

    Touch save(Touch touch);

    void saveAll(Collection<CompanyBusTrip> trips);

    void deleteAll();
}
