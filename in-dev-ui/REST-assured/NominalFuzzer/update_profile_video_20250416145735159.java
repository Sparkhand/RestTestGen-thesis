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
@Order(1635)
public class update_profile_video_20250416145735159{

String baseURL ="http://localhost:8888";

	private void test0() throws JSONException{
		//OPERATION 0
		//Parameter initialization
		Object request0_path_video_id = 10;
		JSONObject request0_request_body = new JSONObject();
		Object request0_request_body_id = null;
		request0_request_body.put("id" , request0_request_body_id);
		Object request0_request_body_videoName = "MC09 2512 8720 74NK T1MG H7YN T15";
		request0_request_body.put("videoName" , request0_request_body_videoName);
		Object request0_request_body_video_url = null;
		request0_request_body.put("video_url" , request0_request_body_video_url);
		Object request0_request_body_conversion_params = null;
		request0_request_body.put("conversion_params" , request0_request_body_conversion_params);
		//Build request
 		RequestSpecification request0 = RestAssured.given();
		request0.pathParam("video_id" , request0_path_video_id);
		request0.contentType(ContentType.JSON).body(request0_request_body.toString());
		//Build Response
		Response response0 = request0.when().put(baseURL+"/identity/api/v2/user/videos/{video_id}");
		Assertions.assertFalse(response0.getStatusCode()>=500,"StatusCode 5xx: The test sequence was not executed successfully.");
	}
	@Test
	public void test_update_profile_video_20250416145735159()  throws JSONException{
		test0();
	}
}
