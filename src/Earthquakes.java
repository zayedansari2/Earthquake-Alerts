import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;


public class Earthquakes {

    private static final String EARTHQUAKE_URL = "https://earthquake.usgs.gov/earthquakes/feed/v1.0/summary/all_hour.geojson";

    public static void main(String[] args) {
        try {
            earthquakeReader();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void earthquakeReader() {
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new URL(EARTHQUAKE_URL).openStream()))) {
            JSONParser jsonParser = new JSONParser();
            JSONObject jsonObject = (JSONObject) jsonParser.parse(br);

            JSONArray features = (JSONArray) jsonObject.get("features");

            System.out.println("Total Earthquakes: " + features.size());
            System.out.println("=====================================");

            for (int i = 0; i < features.size(); i++) {
                JSONObject eqObject = (JSONObject) features.get(i);
                JSONObject properties = (JSONObject) eqObject.get("properties");

                String place = (String) properties.get("place");
                double mag = ((Number) properties.get("mag")).doubleValue();
                String magType = (String) properties.get("magType");
                String eqType = (String) properties.get("type");

                System.out.printf("%d. Location: %s%n", i + 1, place);
                System.out.printf("   Magnitude: %.2f %s%n", mag, magType);
                System.out.printf("   Type: %s%n", eqType);
                System.out.println("-------------------------------------");
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
