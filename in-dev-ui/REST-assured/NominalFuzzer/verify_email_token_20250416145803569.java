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
@Order(3141)
public class verify_email_token_20250416145803569{

String baseURL ="http://localhost:8888";

	private void test0() throws JSONException{
		//OPERATION 0
		//Parameter initialization
		JSONObject request0_request_body = new JSONObject();
		Object request0_request_body_old_email = "Einar.Swaniawski@example.com";
		request0_request_body.put("old_email" , request0_request_body_old_email);
		Object request0_request_body_new_email = "Danielle.Ankunding@example.com";
		request0_request_body.put("new_email" , request0_request_body_new_email);
		Object request0_request_body_token = "T9O2s6i3C7o2E8l7X5Y4";
		request0_request_body.put("token" , request0_request_body_token);
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.contentType(ContentType.JSON).body(request0_request_body.toString());
		//Build Response
		Response response0 = request0.when().post(baseURL+"/identity/api/v2/user/verify-email-token");
		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_verify_email_token_20250416145803569()  throws JSONException{
		test0();
	}
}
