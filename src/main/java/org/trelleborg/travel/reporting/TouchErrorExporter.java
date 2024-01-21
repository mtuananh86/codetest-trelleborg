package org.trelleborg.travel.reporting;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.trelleborg.travel.Constants;
import org.trelleborg.travel.db.TripDB;
import org.trelleborg.travel.model.Touch;
import org.trelleborg.travel.model.Trip;
import org.trelleborg.travel.model.TripStatus;
import org.trelleborg.travel.util.HashUtil;

import java.io.IOException;
import java.time.Duration;
import java.time.Instant;

import static org.trelleborg.travel.Constants.Fields.*;

public class TouchErrorExporter extends AbstractCsvExporter {

    private TripDB tripDB;

    public TouchErrorExporter(TripDB tripDB, CsvMapper csvMapper) {
        super(csvMapper);
        this.tripDB = tripDB;
    }

    @Override
    protected String[] getColumnNames() {
        return TouchErrorFields;
    }

    @Override
    protected void doExport(SequenceWriter writer) throws IOException {
        // TODO: For real database, let do paging
        var touches = tripDB.getAllErrorTouches();
        for (Touch touch : touches) {
            writer.write(TouchErrorReport.from(touch));
        }
    }

    @Getter
    @ToString
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class TouchErrorReport {
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
        private String hashedPan;

        @JsonProperty(StatusField)
        private String status;

        public static TouchErrorReport from(Touch touch) {
            var datetime = touch.getDateTime();
            var stopId = touch.getStopId();
            var pan = touch.getPan();

            if (touch.isOn()) {
                return TouchErrorReport.builder()
                               .started(datetime)
                               .fromStopId(stopId == null ? null : stopId.name())
                               .hashedPan(pan)
                               .status(touch.getStatus())
                               .build();
            } else {
                return TouchErrorReport.builder()
                               .finished(datetime)
                               .toStopId(stopId == null ? null : stopId.name())
                               .hashedPan(pan)
                               .status(touch.getStatus())
                               .build();
            }
        }
    }
}
