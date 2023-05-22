package tests;

import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.specification.RequestSpecification;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Test;
import payloads.Payloads;
import utilities.RandomValueGenerator;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;

public class CreateRepositoryTests {

    private static final String baseUrl = "https://api.github.com";
    private static final String securityToken = "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";
    private static final String apiEndpoint = "/user/repos";
    private static RequestSpecification requestSpecification;

    @BeforeTest
    public static void createRequestSpecification() {

        requestSpecification = new RequestSpecBuilder().
                setBaseUri(baseUrl).
                setAccept("application/vnd.github+json").
                setContentType(ContentType.JSON).
                addHeader("Authorization", "Bearer " + securityToken).
                build();
    }

    @Test
    public void CreateNewRepositorySuccessfully() {
        String repositoryName = RandomValueGenerator.getRepositoryName();
        //RestAssured.authentication = RestAssured.oauth2(TOKEN);

        given().
                spec(requestSpecification).
                body(Payloads.addNewRepository(repositoryName)).
        when().
                post(apiEndpoint).
        then().
                statusCode(201).
                body("name", equalTo(repositoryName)).
                body("description", equalTo("Creating an example repo")).
                body("private", equalTo(false)).
                body("owner.login", equalTo("ssj4goku"));
    }

    @Test
    public void FailToCreateRepositoryWithInvalidApiEndpoint() {
        String repositoryName = RandomValueGenerator.getRepositoryName();
        //RestAssured.authentication = RestAssured.oauth2(TOKEN);

        given().
                spec(requestSpecification).
                body(Payloads.addNewRepository(repositoryName)).
        when().
                post(apiEndpoint + "invalid").
        then().
                statusCode(404).
                body("message", equalTo("Not Found")).
                body("documentation_url", equalTo("https://docs.github.com/rest"));
    }

    @Test
    public void FailToCreateRepositoryWithBadRequest() {
        String repositoryName = RandomValueGenerator.getRepositoryName();
        RestAssured.baseURI = baseUrl;

        given().
                spec(requestSpecification).
                header("X-GitHub-Api-Version", "2052-11-28").
                body(Payloads.addNewRepository(repositoryName)).
                when().
                post(apiEndpoint).
                then().
                statusCode(400).
                body("message", equalTo("Bad Request")).
                body("documentation_url", equalTo("https://docs.github.com/rest"));
    }

    @Test
    public void FailToCreateRepositoryWithEmptyPayload() {
        //RestAssured.authentication = RestAssured.oauth2(TOKEN);

        given().
                spec(requestSpecification).
                body("{}").
                when().
                post(apiEndpoint).
                then().
                statusCode(422).
                body("message", equalTo("Repository creation failed."));
    }

    @Test
    public void FailToCreateRepositoryWithUnauthorizedUser() {
        String repositoryName = RandomValueGenerator.getRepositoryName();
        RestAssured.baseURI = baseUrl;

        given().
                accept("application/vnd.github+json").
                contentType(ContentType.JSON).
                body(Payloads.addNewRepository(repositoryName)).
        when().
                post(apiEndpoint).
        then().
                statusCode(401).
                body("message", equalTo("Requires authentication"));
    }

}
