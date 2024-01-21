package org.trelleborg.travel.reporting;

import java.io.IOException;

public interface FileExporter {
    void export(String outputFile) throws IOException;
}
