package regonfinder.application.csv;

import com.google.common.collect.ImmutableSet;
import regonfinder.application.constants.ApplicationConstants;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.Set;
import java.util.function.BinaryOperator;

public class CsvWriter {

    private final String NEW_LINE = System.getProperty("line.separator");

    /**
     * Create CSV file header
     * @param outputStream  File output stream
     * @return  Elements of the header
     */
    public Set<String> writeHeader(OutputStream outputStream) throws IOException {
        Set<String> header = ImmutableSet.<String>builder()
                .addAll(ApplicationConstants.CUSTOM_HEADER)
                .build();

        final String line = header.stream()
                .reduce("", reduceLine());
        outputStream.write((line + NEW_LINE).getBytes());
        return header;
    }

    public void appendMapToFile(OutputStream outputStream, final Set<String> headers, final Map<String, String> singleReport) throws IOException {
        final String line = headers.stream()
                .map(header -> singleReport.getOrDefault(header, "").replaceAll("\"", "")
                        .replaceAll(",", " "))
                .reduce("", reduceLine());

        outputStream.write((line + NEW_LINE).getBytes());
    }

    private BinaryOperator<String> reduceLine() {
        return (oldValue, newValue) -> oldValue + "," + newValue;
    }
}
