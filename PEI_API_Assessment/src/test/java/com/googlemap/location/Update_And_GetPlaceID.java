package com.googlemap.location;

import io.restassured.RestAssured;
import io.restassured.path.json.JsonPath;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;

import org.testng.Assert;

import inputFiles.Payload;

public class Update_And_GetPlaceID {

	public static void main(String[] args) {
		// TODO Auto-generated method stub

		
		RestAssured.baseURI="https://rahulshettyacademy.com";
		String response = given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body(Payload.AddPlace())
		.when().post("maps/api/place/add/json")
		.then().statusCode(200).body("scope", equalTo("APP"))
		.header("server", "Apache/2.4.52 (Ubuntu)").extract().response().asString();
		
		System.out.println("The received response is" + response);
		
		JsonPath js = new JsonPath(response);
		String PlaceId =js.getString("place_id");
	
		System.out.println("The received PlaceId is" + PlaceId);
		
		//Update place address
		String newAddress = "81 tola walk, USA";
		given().log().all().queryParam("key", "qaclick123").header("Content-Type","application/json")
		.body("{\r\n"
				+ "\"place_id\":\""+PlaceId+"\",\r\n"
				+ "\"address\":\""+newAddress+"\",\r\n"
				+ "\"key\":\"qaclick123\"\r\n"
				+ "}\r\n"
				+ "")
		.when().put("maps/api/place/update/json")
		.then().statusCode(200).body("msg", equalTo("Address successfully updated"));
		
		//Get the updated Place id 
		String getTresponse =  given().log().all().queryParam("key", "qaclick123")
		 .queryParam("place_id", PlaceId)
		 .when().get("maps/api/place/get/json")
		 .then().statusCode(200).extract().response().asString();
		 
		JsonPath jse = new JsonPath(getTresponse);
		String actualAdress = jse.getString("address");
		System.out.println("The received adrress is" + actualAdress);
		Assert.assertEquals(actualAdress, newAddress);
		
	}

}
