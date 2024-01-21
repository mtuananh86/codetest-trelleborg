package org.trelleborg.travel.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.trelleborg.travel.db.MemoryTripDB;
import org.trelleborg.travel.db.TripDB;

@Configuration
public class DatabaseConfig {

    @Bean
    public TripDB tripDB() {
        return new MemoryTripDB();
    }

}
