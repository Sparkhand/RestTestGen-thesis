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
@Order(280)
public class get_recent_posts_20250416145659494{

String baseURL ="http://localhost:8888";

	private void test0() throws JSONException{
		//OPERATION 0
		//Parameter initialization
		Object request0_query_offset = 1895562238;
		Object request0_query_limit = null;
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.queryParam("offset" , request0_query_offset);
		request0.queryParam("limit" , request0_query_limit);
		//Build Response
		Response response0 = request0.when().get(baseURL+"/community/api/v2/community/posts/recent");
		String response0_response_body = response0.getBody().asString();

		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_get_recent_posts_20250416145659494()  throws JSONException{
		test0();
	}
}
