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
@Order(3956)
public class contact_mechanic_20250416145812439{

String baseURL ="http://localhost:8888";

	private void test0() throws JSONException{
		//OPERATION 0
		//Parameter initialization
		JSONObject request0_request_body = new JSONObject();
		Object request0_request_body_number_of_repeats = 2.7516174;
		request0_request_body.put("number_of_repeats" , request0_request_body_number_of_repeats);
		Object request0_request_body_mechanic_api = "5782:858e:027d:f269:d6f0:06c9:ff28:3ac2";
		request0_request_body.put("mechanic_api" , request0_request_body_mechanic_api);
		Object request0_request_body_vin = "2001/01/20";
		request0_request_body.put("vin" , request0_request_body_vin);
		Object request0_request_body_repeat_request_if_failed = false;
		request0_request_body.put("repeat_request_if_failed" , request0_request_body_repeat_request_if_failed);
		Object request0_request_body_problem_details = "cecopexy CM dither";
		request0_request_body.put("problem_details" , request0_request_body_problem_details);
		Object request0_request_body_mechanic_code = "12/2028";
		request0_request_body.put("mechanic_code" , request0_request_body_mechanic_code);
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.contentType(ContentType.JSON).body(request0_request_body.toString());
		//Build Response
		Response response0 = request0.when().post(baseURL+"/workshop/api/merchant/contact_mechanic");
		String response0_response_body = response0.getBody().asString();

		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_contact_mechanic_20250416145812439()  throws JSONException{
		test0();
	}
}
