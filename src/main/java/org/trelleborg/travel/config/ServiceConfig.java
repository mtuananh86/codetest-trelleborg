package org.trelleborg.travel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.trelleborg.travel.db.TripDB;
import org.trelleborg.travel.service.TripProcessor;
import org.trelleborg.travel.service.TripService;
import org.trelleborg.travel.util.TripValidator;

@Configuration
public class ServiceConfig {

    @Bean
    public TripService tripService(TripDB tripDB) {
        return new TripService();
    }

    @Bean
    public TripValidator tripValidator() {
        return new TripValidator();
    }

    @Bean
    public TripProcessor tripProcessor(TripDB db, TripValidator validator, TripService service) {
        return new TripProcessor(service, validator, db);
    }
}
