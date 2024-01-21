package org.trelleborg.travel.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Builder;
import lombok.Data;
import lombok.With;
import org.apache.commons.lang3.StringUtils;

import java.time.Instant;

@Data
@Builder(toBuilder = true)
@With
public class Trip {
    private String companyId;
    private String startBus;
    private String endBus;
    private String date;
    private Instant startTime;
    private Instant endTime;
    private StopPoint fromStopId;
    private StopPoint toStopId;
    private String fromPan;
    private String toPan;
    private Double cost;
    private TripStatus status;

    @JsonIgnore
    public boolean isPending() {
        return status == TripStatus.INCOMPLETE;
    }

    @JsonIgnore
    public boolean isCompleted() {
        return status == TripStatus.COMPLETE;
    }

    @JsonIgnore
    public boolean isCancelled() {
        return status == TripStatus.CANCEL;
    }
}
