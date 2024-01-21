package org.trelleborg.travel.config;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.trelleborg.travel.job.TripImportJob;
import org.trelleborg.travel.service.TripProcessor;

@Configuration
public class JobConfig {

    @Bean
    public TripImportJob tripImportJob(TripProcessor processor, CsvMapper csvMapper) {
        return new TripImportJob(processor, csvMapper);
    }
}
