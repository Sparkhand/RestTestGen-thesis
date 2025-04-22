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
@Order(5085)
public class create_service_report_20250416145823935{

String baseURL ="http://localhost:8888";

	private void test0() throws JSONException{
		//OPERATION 0
		//Parameter initialization
		Object request0_query_mechanic_code = "CKA";
		Object request0_query_problem_details = "drink-hail awabi";
		Object request0_query_vin = "USD";
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.queryParam("mechanic_code" , request0_query_mechanic_code);
		request0.queryParam("problem_details" , request0_query_problem_details);
		request0.queryParam("vin" , request0_query_vin);
		//Build Response
		Response response0 = request0.when().get(baseURL+"/workshop/api/mechanic/receive_report");
		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_create_service_report_20250416145823935()  throws JSONException{
		test0();
	}
}
