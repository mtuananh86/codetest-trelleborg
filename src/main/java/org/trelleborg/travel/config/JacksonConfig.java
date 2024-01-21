package org.trelleborg.travel.config;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static com.fasterxml.jackson.databind.DeserializationFeature.READ_UNKNOWN_ENUM_VALUES_AS_NULL;
import static com.fasterxml.jackson.databind.MapperFeature.ACCEPT_CASE_INSENSITIVE_ENUMS;

@Configuration
public class JacksonConfig {

    @Bean
    public CsvMapper csvMapper() {
        var csvMapper = new CsvMapper();
        csvMapper.registerModule(new JavaTimeModule());
        csvMapper.enable(ACCEPT_CASE_INSENSITIVE_ENUMS);
        csvMapper.enable(READ_UNKNOWN_ENUM_VALUES_AS_NULL);
        return csvMapper;
    }

}
