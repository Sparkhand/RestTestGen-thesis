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
@Order(3415)
public class get_location_20250416145806345{

String baseURL ="http://localhost:8888";

	private void test0() throws JSONException{
		//OPERATION 0
		//Parameter initialization
		Object request0_path_vehicleId = "f8edf066-d1a5-4482-be81-d5c21bec1b74";
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.pathParam("vehicleId" , request0_path_vehicleId);
		//Build Response
		Response response0 = request0.when().get(baseURL+"/identity/api/v2/vehicle/{vehicleId}/location");
		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_get_location_20250416145806345()  throws JSONException{
		test0();
	}
}
