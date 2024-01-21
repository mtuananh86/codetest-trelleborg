package org.trelleborg.travel.reporting;

import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.trelleborg.travel.db.TripDB;
import org.trelleborg.travel.model.Trip;

import java.io.IOException;

import static org.trelleborg.travel.Constants.Fields.TripReportFields;

public class TripExporter extends AbstractCsvExporter {

    private TripDB tripDB;

    public TripExporter(TripDB tripDB, CsvMapper csvMapper) {
        super(csvMapper);
        this.tripDB = tripDB;
    }

    @Override
    protected String[] getColumnNames() {
        return TripReportFields;
    }

    @Override
    protected void doExport(SequenceWriter writer) throws IOException {
        // TODO: For real database, let do paging
        var trips = tripDB.getAllTrips();
        for (Trip trip : trips) {
            writer.write(TripReport.from(trip));
        }
    }


}
