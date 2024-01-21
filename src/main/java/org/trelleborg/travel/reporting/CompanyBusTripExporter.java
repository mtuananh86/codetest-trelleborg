package org.trelleborg.travel.reporting;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;
import org.trelleborg.travel.db.TripDB;
import org.trelleborg.travel.model.CompanyBusTrip;

import java.io.IOException;

import static org.trelleborg.travel.Constants.Fields.*;

public class CompanyBusTripExporter extends AbstractCsvExporter {
    private TripDB tripDB;

    public CompanyBusTripExporter(TripDB tripDB, CsvMapper csvMapper) {
        super(csvMapper);
        this.tripDB = tripDB;
    }

    @Override
    protected String[] getColumnNames() {
        return CompanyTripReportFields;
    }

    @Override
    protected void doExport(SequenceWriter writer) throws IOException {
        // TODO: For real database, let do paging
        var companyTrips = tripDB.getAllCompanyBusTrips();
        for (CompanyBusTrip trip : companyTrips) {
            writer.write(CompanyBusTripReport.from(trip));
        }
    }

    @Getter
    @ToString
    @Builder(toBuilder = true)
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private static class CompanyBusTripReport {
        @JsonProperty(DateField)
        private String date;

        @JsonProperty(CompanyIdField)
        private String companyId;

        @JsonProperty(BusIdField)
        private String busId;

        @JsonProperty(CompleteTripCountField)
        private Integer completeCount;

        @JsonProperty(IncompleteTripCountField)
        private Integer incompleteCount;

        @JsonProperty(CancelledTripCountField)
        private Integer cancelledCount;

        @JsonProperty(TotalChargesField)
        private Double totalCharges;

        public static CompanyBusTripReport from(CompanyBusTrip trip) {
            return CompanyBusTripReport.builder()
                    .date(trip.getDate())
                    .companyId(trip.getCompanyId())
                    .busId(trip.getBusId())
                    .completeCount(trip.getCompletedTrips().size())
                    .cancelledCount(trip.getCancelledTrips().size())
                    .incompleteCount(trip.getPendingTrips().size())
                    .totalCharges(trip.totalCharges())
                    .build();
        }
    }
}
