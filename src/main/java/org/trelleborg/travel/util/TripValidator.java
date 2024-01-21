package org.trelleborg.travel.util;

import org.apache.commons.lang3.StringUtils;
import org.trelleborg.travel.Constants;
import org.trelleborg.travel.exception.ValidationException;
import org.trelleborg.travel.model.Touch;
import org.trelleborg.travel.model.Trip;

public class TripValidator {

    public void validate(Touch touch) {
        validateId(touch);
        validateDateTime(touch);
        validateCompanyId(touch);
        validateTouchType(touch);
        validateStopPoint(touch);
        validateBusId(touch);
        validatePan(touch);
    }

    public void validate(Trip trip, Touch touch) {
        if (touch.isOff() && trip == null) {
            throw new ValidationException("ON Touch not found");
        }

        if (isDuplicateTouch(trip, touch)) {
            throw new ValidationException("Duplicate touch");
        }
    }

    private boolean isDuplicateTouch(Trip trip, Touch touch) {
        if (touch.isOn()) {
            return trip != null
                && touch.getDateTime().equals(trip.getStartTime())
                && touch.getCompanyId().equals(trip.getCompanyId())
                && touch.getBusId().equals(trip.getStartBus())
                && touch.getStopId().equals(trip.getFromStopId());
        } else {
            return trip != null
                    && touch.getDateTime().equals(trip.getEndTime())
                    && touch.getCompanyId().equals(trip.getCompanyId())
                    && touch.getBusId().equals(trip.getEndBus())
                    && touch.getStopId().equals(trip.getToStopId());
        }
    }

    private void validateId(Touch touch) {
        if (StringUtils.isBlank(touch.getId())) {
            throw new ValidationException("Id is missing.");
        }
    }

    private void validateDateTime(Touch touch) {
        if (touch.getDateTime() == null) {
            throw new ValidationException("DateTime is required.");
        }
    }

    private void validateTouchType(Touch touch) {
        if (touch.getTouchType() == null) {
            throw new ValidationException("TouchType has invalid value.");
        }
    }

    private void validateStopPoint(Touch touch) {
        if (touch.getStopId() == null) {
            throw new ValidationException("StopId has invalid value.");
        }
    }

    private void validateCompanyId(Touch touch) {
        if (StringUtils.isBlank(touch.getCompanyId())) {
            throw new ValidationException("CompanyId is missing.");
        }
    }

    private void validateBusId(Touch touch) {
        if (StringUtils.isBlank(touch.getBusId())) {
            throw new ValidationException("BusId is missing.");
        }
    }

    private void validatePan(Touch touch) {
        if (StringUtils.isBlank(touch.getPan())) {
            throw new ValidationException("Pan number is missing.");
        }

        // Pan is already hashed
        if (touch.getPan().matches("^[0-9a-fA-F]+$")) {
            return;
        }

        if (!Constants.Pan.PATTERN.matcher(touch.getPan()).matches()) {
            throw new ValidationException("Invalid Pan number.");
        }
    }
}
