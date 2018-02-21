package regonfinder.application.bir.client;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.InputSource;
import regonfinder.location.webbrowser.Reports;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import java.io.StringReader;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;

public class ReportParser {

    private final String DATA_MARKUP_NAME = "dane";

    public Map<String, String> parseReport(Reports report) throws Exception {
        Map<String, String> result = new HashMap<>();

        Document generalReport = loadXMLFromString(report.getGeneralReport());
        result.putAll(parseGeneralReport(generalReport));

        Document basicData = loadXMLFromString(report.getBasicData());
        result.putAll(parseBasicData(basicData));

        if (report.hasAdditionalData()) {
            Document additionalData = loadXMLFromString(report.getAdditionalReport());
            result.putAll(parseGeneralReport(additionalData));
        }

        if (!report.getPkdReport().equals("")) {
            Document pkdReport = loadXMLFromString(report.getPkdReport());
            result.putAll(parsePKDReport(pkdReport));
        }

        return result;
    }

    private Document loadXMLFromString(String xml) throws Exception
    {
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        factory.setIgnoringElementContentWhitespace(true);
        DocumentBuilder builder = factory.newDocumentBuilder();
        InputSource is = new InputSource(new StringReader(xml));
        return builder.parse(is);
    }

    private Map<String, String> parseGeneralReport(Document report) {
        Map<String, String> result = new HashMap<>();

        final NodeList dataNodes = report.getElementsByTagName(DATA_MARKUP_NAME);

        for (int i = 0; i < dataNodes.getLength(); i++) {
            Node item = dataNodes.item(i).getChildNodes().item(0);
            while (item != null) {
                String itemName = item.getNodeName();

                result.merge(getFieldName(itemName), item.getTextContent(), mergeValues());
                item = item.getNextSibling();
            }
        }

        return result;
    }

    private Map<String, String> parseBasicData(Document report) {
        Map<String, String> result = new HashMap<>();

        final NodeList dataNodes = report.getElementsByTagName(DATA_MARKUP_NAME);

        for (int i = 0; i < dataNodes.getLength(); i++) {
            Node item = dataNodes.item(i).getChildNodes().item(0);
            while (item != null) {
                result.merge(item.getNodeName(), item.getTextContent(), mergeValues());
                item = item.getNextSibling();
            }
        }

        return result;
    }

    private Map<String, String> parsePKDReport(Document report) {
        Map<String, String> result = new HashMap<>();

        final NodeList dataNodes = report.getElementsByTagName(DATA_MARKUP_NAME);

        for (int i = 0; i < dataNodes.getLength(); i++) {
            Node item = dataNodes.item(i).getChildNodes().item(0);
            while (item != null) {
                String itemName = item.getNodeName();

                result.merge(getFieldName(itemName), item.getTextContent(), mergeValues());

                item = item.getNextSibling();
            }
        }

        return result;
    }

    private String getFieldName(String originalName) {
        return originalName.substring(originalName.indexOf("_") + 1);
    }

    private BiFunction<? super String, ? super String, ? extends String> mergeValues() {
        return (oldValue, newValue) -> oldValue.contains(newValue) ? oldValue : oldValue + " " + newValue;
    }
}
