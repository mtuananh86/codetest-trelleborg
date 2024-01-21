package org.trelleborg.travel;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import org.trelleborg.travel.job.TripImportJob;
import org.trelleborg.travel.reporting.CompanyBusTripExporter;
import org.trelleborg.travel.reporting.TouchErrorExporter;
import org.trelleborg.travel.reporting.TripExporter;
import org.trelleborg.travel.service.TripProcessor;

@Component
@Slf4j
@Profile("!test")
public class ConsoleRunner implements CommandLineRunner {

    @Autowired
    private TripProcessor processor;

    @Autowired
    private TripExporter tripExporter;

    @Autowired
    private CompanyBusTripExporter companyBusTripExporter;

    @Autowired
    private TouchErrorExporter touchErrorExporter;

    @Autowired
    private TripImportJob job;

    @Override
    public void run(String... args) throws Exception {
        var file = "input/touchData.csv"; //args[0];
        job.run(file);
        var companyTrips = processor.createCompanyBusTrips();
        companyTrips.stream().forEach(trip -> log.info("" + trip));
        companyBusTripExporter.export("output/companyTrips.csv");
        tripExporter.export("output/trips.csv");
        touchErrorExporter.export("output/unprocessableTouchData.csv");
    }
}