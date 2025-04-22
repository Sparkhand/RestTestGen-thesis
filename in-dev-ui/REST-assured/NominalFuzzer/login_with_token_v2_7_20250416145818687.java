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
@Order(4396)
public class login_with_token_v2_7_20250416145818687{

String baseURL ="http://localhost:8888";

	private void test0() throws JSONException{
		//OPERATION 1
		//Parameter initialization
		JSONObject request1_request_body = new JSONObject();
		Object request1_request_body_email = "test@example.com";
		request1_request_body.put("email" , request1_request_body_email);
		Object request1_request_body_password = "Test!123";
		request1_request_body.put("password" , request1_request_body_password);
		//Build request
 		RequestSpecification request1 = RestAssured.given();
		request1.contentType(ContentType.JSON).body(request1_request_body.toString());
		//Build Response
		Response response1 = request1.when().post(baseURL+"/identity/api/auth/login");
		String response1_response_body = response1.getBody().asString();

		Assertions.assertTrue(response1.getStatusCode()<=299,"StatusCode not 2xx for previous operation.");
		//OPERATION 0
		//Parameter initialization
		JSONObject request0_request_body = new JSONObject();
		Object request0_request_body_email = "sbqiqokq@gmail.com";
		request0_request_body.put("email" , request0_request_body_email);
		Object request0_request_body_token = request1_unknown_token;
		request0_request_body.put("token" , request0_request_body_token);
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.contentType(ContentType.JSON).body(request0_request_body.toString());
		//Build Response
		Response response0 = request0.when().post(baseURL+"/identity/api/auth/v2.7/user/login-with-token");
		String response0_response_body = response0.getBody().asString();

		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_login_with_token_v2_7_20250416145818687()  throws JSONException{
		test0();
	}
}
