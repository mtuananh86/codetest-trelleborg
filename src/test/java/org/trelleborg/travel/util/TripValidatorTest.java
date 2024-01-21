package org.trelleborg.travel.util;

import org.junit.jupiter.api.Test;
import org.trelleborg.travel.exception.ValidationException;
import org.trelleborg.travel.model.StopPoint;
import org.trelleborg.travel.model.Touch;
import org.trelleborg.travel.model.TouchType;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class TripValidatorTest {
    private TripValidator validator = new TripValidator();
    private Touch sampleTouch = Touch.builder()
            .id(UUID.randomUUID().toString())
            .dateTime(Instant.now())
            .companyId("Company1")
            .busId("Bus10")
            .stopId(StopPoint.StopA)
            .touchType(TouchType.ON)
            .pan("ABCDE1234F")
            .build();

    @Test
    public void testTouchPassValidation() {
        validator.validate(sampleTouch);
    }

    @Test
    public void testTouchNoDateTime() {
        var exception = assertThrows(ValidationException.class,
                () -> validator.validate(sampleTouch.withDateTime(null)));
        assertEquals("DateTime is required.", exception.getMessage());
    }

    @Test
    public void testTouchNoId() {
        var exception = assertThrows(ValidationException.class,
                () -> validator.validate(sampleTouch.withId(null)));
        assertEquals("Id is missing.", exception.getMessage());
    }

    @Test
    public void testTouchNoCompanyId() {
        var exception = assertThrows(ValidationException.class,
                () -> validator.validate(sampleTouch.withCompanyId(null)));
        assertEquals("CompanyId is missing.", exception.getMessage());
    }

    @Test
    public void testTouchNoBusId() {
        var exception = assertThrows(ValidationException.class,
                () -> validator.validate(sampleTouch.withBusId(null)));
        assertEquals("BusId is missing.", exception.getMessage());
    }

    @Test
    public void testTouchNoStopPoint() {
        var exception = assertThrows(ValidationException.class,
                () -> validator.validate(sampleTouch.withStopId(null)));
        assertEquals("StopId has invalid value.", exception.getMessage());
    }

    @Test
    public void testTouchNoTouchType() {
        var exception = assertThrows(ValidationException.class,
                () -> validator.validate(sampleTouch.withTouchType(null)));
        assertEquals("TouchType has invalid value.", exception.getMessage());
    }

    @Test
    public void testTouchPanIsEmpty() {
        var exception = assertThrows(ValidationException.class,
                () -> validator.validate(sampleTouch.withPan(null)));
        assertEquals("Pan number is missing.", exception.getMessage());
    }

    @Test
    public void testTouchInvalidPan() {
        var exception = assertThrows(ValidationException.class,
                () -> validator.validate(sampleTouch.withPan("1234ZYX")));
        assertEquals("Invalid Pan number.", exception.getMessage());
    }
}
