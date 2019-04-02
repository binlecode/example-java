import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.net.URI;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 */
public class TestCSV2HtmlV2 {

    /**
     * read in csv file by file name and
     *
     * @param args
     */
    private static final String KEY_DATE = "Date";
    private static final String KEY_DESC = "Description";
    private static final String KEY_VALUE = "Value";

    // for format: 03/31/2007T03:32:12Z
    private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy'T'HH:mm:ssZ");

    private static DateFormat dfo = new SimpleDateFormat("MM/dd/yy hh:mm a z");


    private String buildHtml(List<Map> dataRows) {
        StringBuilder builder = new StringBuilder();
        builder.append("<!DOCTYPE html>")
                .append("<html lang=\"en\">")
                .append("<head><title>Test CSV to Html</title></head>")
                .append("<body>")
                .append("<table>")
                .append("<thead><tr>")
                .append("<th>" + KEY_DATE + "</th>")
                .append("<th>" + KEY_VALUE + "</th>")
                .append("<th>" + KEY_DESC + "</th>")
                .append("</tr></thead>")
                .append("<tbody>");

        for (Map row : dataRows) {
            builder.append("<tr>");
            builder.append("<td>" + row.get(KEY_DATE) + "</td>"); // First column
            builder.append("<td>" + row.get(KEY_VALUE) + "</td>"); // Second column
            builder.append("<td>" + row.get(KEY_DESC) + "</td>"); // Third column
            builder.append("</tr>"); // End of row
        }

        builder.append("</tbody></table></body>")
                .append("</html>");

        String html = builder.toString();
        System.out.println(html);
        return html;
    }

    private void writeToFile(String htmlStr, String htmlFilePath) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(htmlFilePath, false));
        fileWriter.write(htmlStr);
        fileWriter.flush();
    }


    public static void main(String[] args) {

        TestCSV2HtmlV2 tch = new TestCSV2HtmlV2();


        String csvFilePath = "test_csv_2_html.csv";
        String htmlFilePath = "test_csv_2_html.html";


        try {
            BufferedReader fileReader = new BufferedReader(new FileReader(csvFilePath));
            String line;
            String[] tokens;
            List dataRows = new ArrayList<Map>();

            while ((line = fileReader.readLine()) != null) {
                System.out.println(line);

                // tokenize
                tokens = line.split(",");
                Map row = new HashMap<String, String>();
//                row.put(KEY_DATE, tch.parseDate(tokens[0]));
//                row.put(KEY_DESC, tch.parseDescription(tokens[2]));
//                row.put(KEY_VALUE, tch.parseValue(tokens[3]));

                System.out.println(row);
                dataRows.add(row);
            }

            tch.writeToFile(tch.buildHtml(dataRows), htmlFilePath);

            java.awt.Desktop.getDesktop().browse(new URI("file://" + new File(".").getCanonicalPath() + File.separator + htmlFilePath));

        } catch (Exception ex) {
            // do something to report error
            ex.printStackTrace();
        }
    }


    class DataRow {
        Date date;
        String description;
        BigDecimal value;  // for currency values

        private String parseDate(String dateStr) throws ParseException {
            if (dateStr == null) return "";
            dateStr = dateStr.trim();
            if (dateStr.isEmpty()) return "";

            return dfo.format(df.parse(dateStr.replaceAll("Z$", "+0000"))); // translate 'Z' to UTC offset that java can recognize
        }

        private String parseDescription(String descStr) {
            if (descStr == null) return "";
            descStr = descStr.trim();
            if (descStr.isEmpty()) return "";

            // we need do some markup char escaping just in case
            //todo: There is a escapeHtml() method of the StringEscapeUtils class in the Commons LangS library.
            return descStr.replaceAll("&", "&amp;")
                    .replaceAll("<", "&lt;")
                    .replaceAll(">", "&gt;")
                    .replaceAll("\"", "&quot;");
        }

        private String parseValue(String valStr) {
            if (valStr == null) return "";
            valStr = valStr.trim();
            if (valStr.isEmpty()) return "";

            //todo: should we put a locale in getting formatter?
            return NumberFormat.getCurrencyInstance().format(Float.parseFloat(valStr));
        }


        public Date getDate() {
            return date;
        }

        public void setDate(Date date) {
            this.date = date;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public BigDecimal getValue() {
            return value;
        }

        public void setValue(BigDecimal value) {
            this.value = value;
        }


        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;

            DataRow dataRow = (DataRow) o;

            if (date != null ? !date.equals(dataRow.date) : dataRow.date != null) return false;
            return !(description != null ? !description.equals(dataRow.description) : dataRow.description != null);

        }

        /**
         * Override hash code to support hash set on key (date, description)
         */
        @Override
        public int hashCode() {
            int result = date != null ? date.hashCode() : 0;
            result = 31 * result + (description != null ? description.hashCode() : 0);
            return result;
        }
    }


}
