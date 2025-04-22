package NominalFuzzer;

import static io.restassured.RestAssured.*;
import com.jayway.jsonpath.JsonPath;
import io.restassured.RestAssured;
import io.restassured.specification.RequestSpecification;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.common.mapper.TypeRef;
import java.util.List;
import java.util.Map;
import java.util.HashMap;
import org.json.*;
import org.junit.jupiter.api.*;
//import org.junit.runners.*;

//@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.DEFINED_PORT)
@Order(4191)
public class get_location_20250416145814340{

String baseURL ="http://localhost:8888";

	private void test0() throws JSONException{
		//OPERATION 0
		//Parameter initialization
		Object request0_path_vehicleId = "be8c2d3a-4d1e-499f-92b2-352460ab1e55";
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.pathParam("vehicleId" , request0_path_vehicleId);
		//Build Response
		Response response0 = request0.when().get(baseURL+"/identity/api/v2/vehicle/{vehicleId}/location");
		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_get_location_20250416145814340()  throws JSONException{
		test0();
	}
}
