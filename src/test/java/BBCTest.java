import io.restassured.response.Response;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static io.restassured.RestAssured.get;
import static org.junit.jupiter.api.Assertions.*;

public class BBCTest {

    private static final String URL = "https://api.punkapi.com/v2/beers";

    private List<Map<String, Object>> getBeersAfterDate(String date) {
        int page = 1;
        List<Map<String, Object>> beers = new ArrayList<>();

        while (true) {
            Response response = get(URL + "?brewed_after=" + date + "&page=" + page + "&per_page=80");
            List<Map<String, Object>> data = response.jsonPath().getList("$");

            if (data.isEmpty()) {
                break;
            }

            beers.addAll(data);
            page++;
        }

        return beers;
    }

    @Test
    public void testAbvValid() {
        List<Map<String, Object>> beers = getBeersAfterDate("12-2015");

        for (Map<String, Object> beer : beers) {
            Object abv = beer.get("abv");
            String valueAsString = String.valueOf(abv);
            assertTrue(abv instanceof Double, "ABV parameter is not of type Double for beer " + beer.get("name"));
            assertNotNull(abv, "ABV parameter is NULL for beer " + beer.get("name"));
            assertFalse(valueAsString.isEmpty(), "ABV parameter is an empty string for beer " + beer.get("name"));
            assertTrue((Double) abv > 4.0, "ABV parameter is less than 4.0 for beer " + beer.get("name"));
        }
    }

    @Test
    public void testNameValid() {
        List<Map<String, Object>> beers = getBeersAfterDate("12-2015");

        for (Map<String, Object> beer : beers) {
            String name = (String) beer.get("name");
            assertNotNull(name, "NAME parameter is NULL for beer ID " + beer.get("id"));
            assertFalse(name.isEmpty(), "NAME parameter is an empty string for beer ID " + beer.get("id"));
        }
    }

    @Test
    public void testIbuValid() {
        List<Map<String, Object>> beers = getBeersAfterDate("12-2015");

        for (Map<String, Object> beer : beers) {
            Object ibu = beer.get("ibu");
            assertNotNull(ibu, "IBU is NULL for beer ID " + beer.get("id"));
            assertTrue(ibu instanceof Integer, "IBU parameter is not an Integer type for beer " + beer.get("name"));
            assertTrue((Integer) ibu >= 0, "IBU is negative for beer " + beer.get("name"));

        }
    }

    @Test
    public void testDescriptionValid() {
        List<Map<String, Object>> beers = getBeersAfterDate("12-2015");

        for (Map<String, Object> beer : beers) {
            String description = (String) beer.get("description");
            assertNotNull(description, "DESCRIPTION is NULL for beer " + beer.get("name"));
            assertFalse(description.isEmpty(), "DESCRIPTION is an empty string for beer " + beer.get("name"));
        }
    }

}
