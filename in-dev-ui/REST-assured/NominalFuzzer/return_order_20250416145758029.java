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
@Order(2781)
public class return_order_20250416145758029{

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
		Object request0_query_order_id = request1_unknown_order_id;
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.queryParam("order_id" , request0_query_order_id);
		//Build Response
		Response response0 = request0.when().post(baseURL+"/workshop/api/shop/orders/return_order");
		String response0_response_body = response0.getBody().asString();

		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_return_order_20250416145758029()  throws JSONException{
		test0();
	}
}
