package com.restassured.bestbuy.crudtest;


import com.restassured.bestbuy.model.ProductPojo;
import com.restassured.bestbuy.utils.TestUtils;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.response.ValidatableResponse;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import java.util.HashMap;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.hasValue;

public class ProductsCRUDTest {

    @BeforeClass
    public void inIt()
    {
        RestAssured.baseURI = "http://localhost";
        RestAssured.port = 3030;
        RestAssured.basePath = "/products";                                                     //endpoint
    }

    static String name = "Duracell - AAA Batteries (8-Pack)";                                //Declare and initialise values then only can set
    static String type =  "HardGood" ;
    static double price =  5.49;
    static String upc = "041333424019";                                                     //Unique Product Code
    static int shipping =0;
    static String description = "Compatible with select electronic devices; AAA size; DURALOCK Power Preserve technology; 4-pack";
    static String manufacturer = "Duracell" ;
    static String model = "MN2400B4Z";
    static String url = "http://www.bestbuy.com/site/duracell-aaa-batteries-4-pack/43900.p?id=1051384074145&skuId=43900&cmp=RMXCC";
    static String image = "http://img.bbystatic.com/BestBuy_US/images/products/4390/43900_sa.jpg";
    static  long id = 43900;
    @Test
    public  void createPost()
    {
        ProductPojo productPojo = new ProductPojo();
        productPojo.setName(TestUtils.getRandomValue() + name);                                             //If initialised only, setters method can be used
        productPojo.setType(TestUtils.getRandomValue() + type);
        productPojo.setPrice( price);
        productPojo.setUpc(Long.valueOf(upc));
        productPojo.setShipping(shipping);
        productPojo.setDescription(description);
        productPojo.setManufacturer(manufacturer);
        productPojo.setModel(model);
        productPojo.setUrl(url);
        productPojo.setId(id);

        Response response =
                given()
                        .contentType(ContentType.JSON)
                        .when()
                        .body(productPojo)                                  //pojo is the payload which is body used for Post,Put,Patch
                        .post();
        response.prettyPrint();
        //  id=response.then().extract().path("id");
        // response.then().log().all();
        response.then().statusCode(400);
    }
    @Test
    public void getTest(){
        String s1 = "findAll{it.name == '";
        String s2 = "'}.get(0)";

        ValidatableResponse response =
                given()
                        .when()
                        .get("/products")
                        .then().statusCode(200);
        HashMap<String, Object> studentMap = response.extract()
                .path(s1 + name + s2);
        response.body(s1 + name + s2, hasValue(name));
        id = (int) studentMap.get("id");
    }
    @Test
    public void patchUpdateId()
    {
        Response response =
                given()
                        .when()
                        .patch("/products" + id);
        response.then().statusCode(400);
    }
    @Test
    public void deleteId()
    {
        given()                                                        //In delete no response
                .when()
                .delete("/" + id)
                .then()
                .statusCode(404);

        given()
                .when()
                .get("/" + id)
                .then()
                .statusCode(404);
    }
}