package regonfinder.application.csv;

import com.google.common.collect.ImmutableSet;
import regonfinder.application.constants.ApplicationConstants;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;

public class CsvWriter {

    private final String NEW_LINE = System.getProperty("line.separator");

    /**
     * Create CSV file header
     * @param writer  File writer
     * @return  Elements of the header
     */
    public Set<String> writeHeader(Writer writer) throws IOException {
        Set<String> header = ImmutableSet.<String>builder()
                .addAll(ApplicationConstants.REPORT_FIELDS)
                .addAll(ApplicationConstants.PKD_REPORT_FIELDS)
                .build();

        final String line = header.stream()
                .reduce("", reduceLine());
        writer.write(line + NEW_LINE);
        return header;
    }

    public void appendMapToFile(Writer writer, final Set<String> headers, final Map<String, String> singleReport) throws IOException {
        final String line = headers.stream()
                .map(header -> singleReport.getOrDefault(header, ""))
                .reduce("", reduceLine());

        writer.write(line + NEW_LINE);
    }

    private BinaryOperator<String> reduceLine() {
        return (oldValue, newValue) -> oldValue + "," + newValue;
    }
}
