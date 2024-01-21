package org.trelleborg.travel.config;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.trelleborg.travel.db.TripDB;
import org.trelleborg.travel.reporting.CompanyBusTripExporter;
import org.trelleborg.travel.reporting.TouchErrorExporter;
import org.trelleborg.travel.reporting.TripExporter;

@Configuration
public class ExporterConfig {

    @Bean
    public TripExporter tripExporter(TripDB db, CsvMapper csvMapper) {
        return new TripExporter(db, csvMapper);
    }

    @Bean
    public CompanyBusTripExporter companyBusTripExporter(TripDB db, CsvMapper csvMapper) {
        return new CompanyBusTripExporter(db, csvMapper);
    }

    @Bean
    public TouchErrorExporter touchErrorExporter(TripDB db, CsvMapper csvMapper) {
        return new TouchErrorExporter(db, csvMapper);
    }
}
