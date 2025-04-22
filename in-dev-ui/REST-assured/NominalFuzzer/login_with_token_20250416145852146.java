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
@Order(6796)
public class login_with_token_20250416145852146{

String baseURL ="http://localhost:8888";

	private void test0() throws JSONException{
		//OPERATION 2
		//Parameter initialization
		JSONObject request2_request_body = new JSONObject();
		Object request2_request_body_email = "test@example.com";
		request2_request_body.put("email" , request2_request_body_email);
		Object request2_request_body_password = "Test!123";
		request2_request_body.put("password" , request2_request_body_password);
		//Build request
 		RequestSpecification request2 = RestAssured.given();
		request2.contentType(ContentType.JSON).body(request2_request_body.toString());
		//Build Response
		Response response2 = request2.when().post(baseURL+"/identity/api/auth/login");
		String response2_response_body = response2.getBody().asString();

		Assertions.assertTrue(response2.getStatusCode()<=299,"StatusCode not 2xx for previous operation.");
		//OPERATION 1
		//Parameter initialization
		Object request1_path_order_id = 1;
		//Build request
 		RequestSpecification request1 = RestAssured.given();
		request1.pathParam("order_id" , request1_path_order_id);
		//Build Response
		Response response1 = request1.when().get(baseURL+"/workshop/api/shop/orders/{order_id}");
		String response1_response_body = response1.getBody().asString();

		Assertions.assertTrue(response1.getStatusCode()<=299,"StatusCode not 2xx for previous operation.");
		//OPERATION 0
		//Parameter initialization
		JSONObject request0_request_body = new JSONObject();
		Object request0_request_body_email = request1_unknown_email;
		request0_request_body.put("email" , request0_request_body_email);
		Object request0_request_body_token = request2_unknown_token;
		request0_request_body.put("token" , request0_request_body_token);
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.contentType(ContentType.JSON).body(request0_request_body.toString());
		//Build Response
		Response response0 = request0.when().post(baseURL+"/identity/api/auth/v4.0/user/login-with-token");
		String response0_response_body = response0.getBody().asString();

		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_login_with_token_20250416145852146()  throws JSONException{
		test0();
	}
}
