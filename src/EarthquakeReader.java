import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

public class EarthquakeReader {
    private static final String EARTHQUAKE_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";

    public static void earthquakeReader() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(EARTHQUAKE_URL).openStream()))) {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(br);
            JSONArray features = (JSONArray) jsonObject.get("features");

            System.out.println("=====================================");
            System.out.println("        Earthquake Report");
            System.out.println("=====================================");
            System.out.println("Total Earthquakes: " + features.size());
            System.out.println("-------------------------------------");

            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss z");
            sdf.setTimeZone(TimeZone.getTimeZone("UTC"));

            for (int i = 0; i < features.size(); i++) {
                JSONObject eqObject = (JSONObject) features.get(i);
                JSONObject properties = (JSONObject) eqObject.get("properties");
                JSONObject geometry = (JSONObject) eqObject.get("geometry");
                JSONArray coordinates = (JSONArray) geometry.get("coordinates");

                String place = (String) properties.get("place");
                double mag = properties.get("mag") != null ? ((Number) properties.get("mag")).doubleValue() : 0.0;
                String magType = (String) properties.getOrDefault("magType", "N/A");
                String eqType = (String) properties.getOrDefault("type", "N/A");
                long timeMillis = properties.get("time") != null ? (long) properties.get("time") : 0;
                String formattedTime = timeMillis > 0 ? sdf.format(new Date(timeMillis)) : "Unknown";
                int tsunami = properties.get("tsunami") != null ? ((Number) properties.get("tsunami")).intValue() : 0;
                double depth = coordinates.size() > 2 ? ((Number) coordinates.get(2)).doubleValue() : 0.0;

                System.out.printf("%d. Location: %s%n", i + 1, place);
                System.out.printf("   Magnitude: %.2f %s%n", mag, magType);
                System.out.printf("   Type: %s%n", eqType);
                System.out.printf("   Depth: %.2f km%n", depth);
                System.out.printf("   Time: %s%n", formattedTime);
                System.out.printf("   Tsunami Warning: %s%n", tsunami == 1 ? "Yes" : "No");
                System.out.println("-------------------------------------");
            }
        } catch (Exception ex) {
            System.err.println("Error fetching earthquake data: " + ex.getMessage());
            ex.printStackTrace();
        }
    }

    public static void main(String[] args) {
        earthquakeReader();
    }
}
