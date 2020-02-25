package web;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.net.URI;
import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 * A testing class that parses a csv file and reformat the output to an html file.
 * <p>
 * The file format is a simple comma separated file with 4 columns. Any of the fields may contain
 * an empty value. The file format is specified below:
 * <p>
 * Position  Name         Description
 * 1        Date         A date field, with the format of 01/31/2007T23:59:59Z where
 * the 'T' separates the date from the time and the 'Z' indicates the
 * timezone. The T and the Z are always present. The time is a 24-
 * hour time format, with midnight being the 0 hour. The month,
 * day, hour, minute and second fields will all have leading zeros
 * to fill the number to 2 characters.
 * 2        Row number   A number field, starting at 1 and increments by 1 for each new row.
 * 3        Description  A basic description of the row.
 * 4        Value        The value of the row, expressed as a simple decimal value. The
 * decimal value may or may not have a leading zero (e.g. 0.5 or .5).
 * <p>
 * An example of the csv file is like below:
 * <p>
 * 03/31/2007T03:32:12Z,1,Walget's Widgets,50
 * 04/02/2006T14:23:47Z,2,BMart Bubbles,5.5
 * 07/18/2006T13:53:23Z,3,Kgreen Kandies,.23
 * 12/24/2006T22:00:08Z,4,Santa Snacks,2.61
 * <p>
 * The output should be an html file with a table of parsed data.
 * The rows that are displayed should be unique based on the date and description.
 * <p>
 * The UI layout is described below:
 * Position  Source Position  Name          Description
 * 1         1               Date          A date field with the format of 1/31/07 12:59 PM
 * EDT where the day, month, hour, minute fields do
 * not have leading zeros. The year field should be a 2
 * digit field with leading zeros. The time is a 12-hour
 * format and should also include an AM/PM marker.
 * Finally, the 'EDT' is a variable string that signifies
 * the local time zone.
 * 2         4               Value         A monetary field that displays values in U.S.
 * Dollars. The value should have leading and trailing
 * zeros when appropriate, e.g. $0.50. For values
 * greater than 1000, there should be a comma at each
 * 1000th place, e.g. $1,000.00 or $1,000,000.00.
 * 3         3               Description   A basic description of the row.
 * <p>
 * With the sample csv data above, the output data table should be similar to:
 * <p>
 * Date                 	Value	 Description
 * 03/30/07 11:32 PM EDT	$50.00 	 Walget's Widgets
 * 04/02/06 10:23 AM EDT	$5.50	 BMart Bubbles
 * 07/18/06 09:53 AM EDT	$0.23	 Kgreen Kandies
 * 12/24/06 05:00 PM EST	$2.61	 Santa Snacks
 * <p>
 * Usage:
 * Assuming the source csv file is: test_csv_2_html.csv, run command in terminal:
 * <p>
 * java TestCSV2Html test_csv_2_html.csv
 * <p>
 * If this is run in a desktop environment with default browser support, then a browser will be opened
 * to render the generated html file. Otherwise the generated html file can be manually opened.
 * <p>
 * Check {@link TestCSV2Html#popUsage} method for details of usage.
 */
public class TestCSV2Html {

    private static final String KEY_DATE = "Date";
    private static final String KEY_DESC = "Description";
    private static final String KEY_VALUE = "Value";

    // for format: 03/31/2007T03:32:12Z
    private static DateFormat df = new SimpleDateFormat("MM/dd/yyyy'T'HH:mm:ssZ");
    // for format: 03/30/07 11:32 PM EDT
    private static DateFormat dfo = new SimpleDateFormat("MM/dd/yy hh:mm a z");


    /**
     * This is the main routine of parsing input csv file, reformat and render to an html file table.
     *
     * @param csvFilePath  optional, if not given a sample file will be used
     * @param htmlFilePath optional, if not given the source csv file name (without .csv) will be used
     *                     <p>
     *                     This file doesn't handle IO or parsing related exceptions internally, but simply bubbles to main()
     *                     and terminates the processing.
     */
    public void run(String csvFPath, String htmlFPath) {
        String csvFilePath = csvFPath;
        if (csvFilePath == null || csvFilePath.isEmpty()) {
            csvFilePath = "test_csv_2_html.csv";
        }

        String htmlFilePath = htmlFPath;
        if (htmlFilePath == null || htmlFilePath.isEmpty()) {
            htmlFilePath = csvFilePath.replaceAll("\\.csv$", ".html");
        }

        System.out.println("parsing csv file: " + csvFilePath + " and output html file: " + htmlFilePath);

        try {
            // use a hash map to remove duplicate rows by key on hash code (date, description)
            Map<String, Map> dataRows = new HashMap<String, Map>();

            BufferedReader fileReader = new BufferedReader(new FileReader(csvFilePath));
            String[] tokens;
            String line;
            while ((line = fileReader.readLine()) != null) {
                System.out.println(line);  // debug

                // tokenize
                tokens = line.split(",");

                Map<String, String> row = new HashMap<String, String>();
                row.put(KEY_DATE, parseDate(tokens[0]));
                row.put(KEY_DESC, parseDescription(tokens[2]));
                row.put(KEY_VALUE, parseValue(tokens[3]));

                System.out.println(row);  // debug
                dataRows.put(row.get(KEY_DATE) + row.get(KEY_DESC), row);
            }

            Collection<Map> uniqueDataRows = dataRows.values();

            writeToFile(buildHtml(uniqueDataRows), htmlFilePath);

            String htmlFileFullPath = new java.io.File(".").getCanonicalPath() + File.separator + htmlFilePath;

            renderInBrowser(htmlFileFullPath);

        } catch (Exception ex) {
            //todo: define some explicit error handling logic here, such as error message construction
            ex.printStackTrace();
        }
    }

    private void renderInBrowser(String filePath) {
        try {
            java.awt.Desktop.getDesktop().browse(new URI("file://" + filePath));
        } catch (Exception ex) {
            System.out.print("Not able to get a desktop browser handler, please open file manually: " + filePath + "\n\n");
        }
    }

    private String parseDate(String dateStr) throws ParseException {
        if (dateStr == null) {
            return "";
        }
        dateStr = dateStr.trim();
        if (dateStr.isEmpty()) {
            return "";
        }

        return dfo.format(df.parse(dateStr.replaceAll("Z$", "+0000"))); // translate 'Z' to UTC offset that java can recognize
    }

    private String parseDescription(String descStr) {
        if (descStr == null) {
            return "";
        }
        descStr = descStr.trim();
        if (descStr.isEmpty()) {
            return "";
        }

        // add some very basic markup char escaping just in case
        //todo: There is a escapeHtml() method of the StringEscapeUtils class in the Commons LangS library.
        return descStr.replaceAll("&", "&amp;")
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;");
    }

    private String parseValue(String valStr) throws NumberFormatException {
        if (valStr == null) {
            return "";
        }
        valStr = valStr.trim();
        if (valStr.isEmpty()) {
            return "";
        }

        //todo: we can put a specific locale in getting formatter if needed
        return NumberFormat.getCurrencyInstance().format(Float.parseFloat(valStr));
    }

    private String buildHtml(Collection<Map> dataRows) {
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
        System.out.println(html);  // debug
        return html;
    }

    private void writeToFile(String htmlStr, String htmlFilePath) throws IOException {
        BufferedWriter fileWriter = new BufferedWriter(new FileWriter(htmlFilePath, false));
        fileWriter.write(htmlStr);
        fileWriter.flush();
    }

    public static void main(String[] args) {
        TestCSV2Html tch = new TestCSV2Html();

        if (args.length == 0) {
            tch.run(null, null);
        } else if (args.length == 1) {
            tch.run(args[0], null);
        } else if (args.length == 2) {
            tch.run(args[0], args[1]);
        } else {
            popUsage();
        }
    }

    private static void popUsage() {
        System.out.println("Usage: java TestCSV2Html [<csv-file-name>] [<html-file-name>]");
        System.out.println("Default sample file will be used if csv file name is not given");
    }
}
