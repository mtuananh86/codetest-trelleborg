package org.trelleborg.travel.testing;

import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import org.springframework.beans.factory.annotation.Autowired;

public abstract class BaseCsvFileTest {

    private String context;
    private String resourcePath = "src/test/resources/csv/trips/";

    private CsvMapper csvMapper;



}
