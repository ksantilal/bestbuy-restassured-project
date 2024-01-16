package com.restassured.bestbuy.crudtest;

import com.restassured.bestbuy.model.StorePojo;
import com.restassured.bestbuy.testbase.TestBase;
import com.restassured.bestbuy.utils.TestUtils;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasValue;

public class StoresCRUDTest extends TestBase {
    static String name = TestUtils.getRandomValue() + "Minnetonka1";
    static String type = TestUtils.getRandomValue() + "BigBox1";
    static String address = TestUtils.getRandomValue() + "135131 Ridgedale Dr";
    static String address2 = "";
    static String city = "Hopkins";
    static String state = "MN";
    static String zip = "55305";
    static double lat = 44.969658;
    static double lng = -93.449539;
    static String hours = "Mon: 10-9; Tue: 10-9; Wed: 10-9; Thurs: 10-9; Fri: 10-9; Sat: 10-9; Sun: 10-8";
    static int id;

    @Test
    public void postTest() {
        //Create
        StorePojo storePojo = new StorePojo();
        storePojo.setName(name);
        storePojo.setType(type);
        storePojo.setAddress(address);
        storePojo.setAddress2(address2);
        storePojo.setCity(city);
        storePojo.setState(state);
        storePojo.setZip(zip);
        storePojo.setLat((int) lat);
        storePojo.setLng((int) lng);
        storePojo.setHours(hours);

        Response response = given()
                .contentType(ContentType.JSON)
                .when()
                .body(storePojo)
                .post();
        response.prettyPrint();
        response.then().statusCode(201);


    }
    //Get
    public void getTest(){
        String s1 = "findAll{it.name == '";
        String s2 = "'}.get(0)";

        ValidatableResponse response =
                given()
                        .when()
                        .get("/stores")
                        .then().statusCode(200);
        HashMap<String, Object> studentMap = response.extract()
                .path(s1 + name + s2);
        response.body(s1 + name + s2, hasValue(name));
        id = (int) studentMap.get("id");
    }
    //Put

    public void putTest() {

        int storeIdToUpdate = id;

        // Create a StorePojo with only the updated name
        StorePojo updatedStorePojo = new StorePojo();
        updatedStorePojo.setName(name + "UpdatedName");

        Response response = given()
                .contentType(ContentType.JSON)
                .pathParam("id", storeIdToUpdate)
                .when()
                .body(updatedStorePojo)
                .put("/stores/{id}");

        response.prettyPrint();
        response.then().statusCode(200);
    }
    //Delete
    public void deleteTest(){
        given()
                .pathParam("id", id)
                .when()
                .delete("/{id}")
                .then()
                .statusCode(204);

        given()
                .pathParam("id", id)
                .when()
                .get("/{id}")
                .then()
                .statusCode(404);
    }
}