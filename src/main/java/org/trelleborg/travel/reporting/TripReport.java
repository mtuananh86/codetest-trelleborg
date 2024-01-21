package org.trelleborg.travel.reporting;

import com.fasterxml.jackson.annotation.*;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.*;
import lombok.extern.jackson.Jacksonized;
import org.trelleborg.travel.Constants;
import org.trelleborg.travel.model.Trip;
import org.trelleborg.travel.model.TripStatus;

import java.time.Duration;
import java.time.Instant;

import static org.trelleborg.travel.Constants.Fields.*;
import static org.trelleborg.travel.Constants.Fields.StatusField;

@Data
@Builder(toBuilder = true)
@Jacksonized
@JsonIgnoreProperties(ignoreUnknown = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({
    StartedField,
    FinishedField,
    DurationSecField,
    FromStopIdField,
    ToStopIdField,
    ChargeAmountField,
    HashedPanField,
    StatusField
})
public class TripReport {
    @JsonProperty(StartedField)
    @JsonFormat(pattern = Constants.DateTime.PATTERN, timezone = "UTC")
    private Instant started;

    @JsonProperty(FinishedField)
    @JsonFormat(pattern = Constants.DateTime.PATTERN, timezone = "UTC")
    private Instant finished;

    @JsonProperty(DurationSecField)
    private Long duration;

    @JsonProperty(FromStopIdField)
    private String fromStopId;

    @JsonProperty(ToStopIdField)
    private String toStopId;

    @JsonProperty(ChargeAmountField)
    private Double chargeAmount;

    @JsonProperty(HashedPanField)
    //@JsonSerialize(using = PanSerializer.class, as=String.class)
    private String hashedPan;

    @JsonProperty(StatusField)
    private TripStatus status;

    public static TripReport from(Trip trip) {
        var startTime = trip.getStartTime();
        var endTime = trip.getEndTime();
        var duration = (endTime == null ? 0 : Duration.between(startTime, endTime).toSeconds());
        return TripReport.builder()
                .started(startTime)
                .finished(endTime)
                .duration(duration)
                .fromStopId(trip.getFromStopId().name())
                .toStopId(trip.getToStopId() == null ? "" : trip.getToStopId().name())
                .chargeAmount(trip.getCost())
                .hashedPan(trip.getFromPan())
                .status(trip.getStatus())
                .build();
    }
}