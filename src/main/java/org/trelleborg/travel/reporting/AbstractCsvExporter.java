package org.trelleborg.travel.reporting;

import com.fasterxml.jackson.core.StreamWriteFeature;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SequenceWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public abstract class AbstractCsvExporter implements FileExporter {
    protected CsvMapper csvMapper;

    public AbstractCsvExporter(CsvMapper csvMapper) {
        this.csvMapper = csvMapper;
    }

    protected ObjectWriter getWriter(CsvSchema schema) {
        return csvMapper.writer()
                .with(schema)
                .without(StreamWriteFeature.AUTO_CLOSE_CONTENT)
                .without(StreamWriteFeature.AUTO_CLOSE_TARGET);
    }

    @Override
    public void export(String outputFile) throws IOException {
        var file = new File(outputFile);

        var columnNames = getColumnNames();

        var columns = IntStream.range(0, columnNames.length)
                .mapToObj(i -> new CsvSchema.Column(i, columnNames[i]))
                .collect(Collectors.toList());

        var schema = CsvSchema.builder()
                .addColumns(columns)
                .setUseHeader(true)
                .build();

        if (file.exists()) {
            file.delete();
        }

        var csvWriter = getWriter(schema);
        try(OutputStream stream = new FileOutputStream(outputFile , true);) {
            var writer = csvWriter.writeValues(stream);
            doExport(writer);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    protected abstract String[] getColumnNames();
    protected abstract void doExport(SequenceWriter writer) throws IOException;
}
