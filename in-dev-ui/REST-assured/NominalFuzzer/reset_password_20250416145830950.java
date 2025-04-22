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
@Order(5339)
public class reset_password_20250416145830950{

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
		Object request0_request_body_email = request1_unknown_email;
		request0_request_body.put("email" , request0_request_body_email);
		Object request0_request_body_password = null;
		request0_request_body.put("password" , request0_request_body_password);
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.contentType(ContentType.JSON).body(request0_request_body.toString());
		//Build Response
		Response response0 = request0.when().post(baseURL+"/identity/api/v2/user/reset-password");
		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_reset_password_20250416145830950()  throws JSONException{
		test0();
	}
}
