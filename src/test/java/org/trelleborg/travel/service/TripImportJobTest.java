package org.trelleborg.travel.service;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.trelleborg.travel.Application;
import org.trelleborg.travel.db.TripDB;
import org.trelleborg.travel.job.TripImportJob;
import org.trelleborg.travel.reporting.TripReport;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = Application.class)
@ActiveProfiles("test")
public class TripImportJobTest {

    private static final String TEST_PATH = "src/test/resources/csv/trips/{testname}/";
    private static final String INPUT_CSV = TEST_PATH + "input.csv";
    private static final String OUTPUT_CSV = TEST_PATH + "output.csv";

    @Autowired
    private CsvMapper csvMapper;

    @Autowired
    private TripProcessor tripProcessor;

    @Autowired
    private TripDB tripDB;

    private TripImportJob job;

    @BeforeEach
    public void init() {
        tripDB.deleteAll();
        job = new TripImportJob(tripProcessor, csvMapper);
    }

    @Test
    public void testCompletedTrips() throws IOException {
        runTest("completed");
    }

    @Test
    public void testIncompletedTrips() throws IOException {
        runTest("incompleted");
    }

    @Test
    public void testCancelledTrips() throws IOException {
        runTest("cancelled");
    }

    private void runTest(String testname) throws IOException {
        runTest(testname, null);
    }

    private void runTest(String testname,
                         Class<? extends Exception> expectedException) throws IOException {
        var inputFile = INPUT_CSV.replace("{testname}", testname);
        var outputFile = OUTPUT_CSV.replace("{testname}", testname);

        try {
            job.run(inputFile);
        } catch (Exception ex) {
            Assertions.assertEquals(expectedException, ex.getClass());
        }

        var schema = csvMapper.schemaFor(TripReport.class)
                .withHeader();

        var it = csvMapper.readerFor(TripReport.class)
                .with(schema)
                .<TripReport>readValues(new File(outputFile));

        var reports = new ArrayList<TripReport>();

        while(it.hasNext()) {
            reports.add(it.next());
        }

        var trips = tripDB.getAllTrips().stream()
                .map(t -> TripReport.from(t))
                .toList();

        Assertions.assertEquals(trips.size(), reports.size());
        for (int i = 0; i < trips.size(); i++) {
            Assertions.assertEquals(trips.get(i), reports.get(i));
        }
    }
}
