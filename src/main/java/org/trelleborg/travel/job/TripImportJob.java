package org.trelleborg.travel.job;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import lombok.extern.slf4j.Slf4j;
import org.trelleborg.travel.exception.ValidationException;
import org.trelleborg.travel.model.Touch;
import org.trelleborg.travel.service.TripProcessor;

import java.io.File;
import java.io.IOException;

@Slf4j
public class TripImportJob {
    private CsvMapper csvMapper;
    private TripProcessor tripProcessor;
    private CsvSchema schema;

    public TripImportJob(TripProcessor tripProcessor, CsvMapper csvMapper) {
        this.csvMapper = csvMapper;
        this.tripProcessor = tripProcessor;
        this.schema = csvMapper.schemaFor(Touch.class)
                .withHeader();
    }

    public void run(String file) throws IOException {
        var iterator = csvMapper.readerFor(Touch.class)
                .with(schema)
                .<Touch>readValues(new File(file));

        while (iterator.hasNext()) {
            try {
                var touch = iterator.next();
                tripProcessor.importTrip(touch);
            } catch (ValidationException ex) {
                log.warn(ex.getMessage());
            }
        }
    }
}
