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
@Order(628)
public class mechanic_signup_20250416145712038{

String baseURL ="http://localhost:8888";

	private void test0() throws JSONException{
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
		Object request0_request_body_name = request1_unknown_name;
		request0_request_body.put("name" , request0_request_body_name);
		Object request0_request_body_email = "mslqkpunc@gmail.com";
		request0_request_body.put("email" , request0_request_body_email);
		Object request0_request_body_number = null;
		request0_request_body.put("number" , request0_request_body_number);
		Object request0_request_body_password = "bRID169b\"HNTDo6TT";
		request0_request_body.put("password" , request0_request_body_password);
		Object request0_request_body_mechanic_code = "1";
		request0_request_body.put("mechanic_code" , request0_request_body_mechanic_code);
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.contentType(ContentType.JSON).body(request0_request_body.toString());
		//Build Response
		Response response0 = request0.when().post(baseURL+"/workshop/api/mechanic/signup");
		String response0_response_body = response0.getBody().asString();

		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_mechanic_signup_20250416145712038()  throws JSONException{
		test0();
	}
}
