import static com.jayway.restassured.RestAssured.get;

import org.json.JSONArray;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.jayway.restassured.response.Response;


public class RestTest {

    @Test
    public void getRequestFindCapital() throws JSONException {

        //make get request to fetch capital of norway
        Response resp = get("http://restcountries.eu/rest/v1/name/norway");

        //Fetching response in JSON
        JSONArray jsonResponse = new JSONArray(resp.asString());

        //Fetching value of capital parameter
        String capital = jsonResponse.getJSONObject(0).getString("capital");

        // Print "Oslo"`
        System.out.println(capital);

        //Asserting that capital of Norway is Oslo
        Assert.assertEquals(capital, "Oslo");
    }

    public static void main(String args[]) {

        RestTest rt = new RestTest();

        try {
            rt.getRequestFindCapital();
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }
}