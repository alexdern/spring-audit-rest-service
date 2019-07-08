package ru.alexdern.spring.audit;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.HttpStatus;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;
import ru.alexdern.spring.audit.dto.AuditEventDto;

import java.util.HashMap;
import java.util.Map;

import static io.restassured.RestAssured.given;
import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = RANDOM_PORT)
public class IntegrationTests {

    @LocalServerPort
    private int port;

    @Autowired
    ObjectMapper objectMapper;

    @Value("${api.http.auth.token}")
    private String token;

    private AuditEventDto testDataDto;

    @Before
    public void setup() {
        RestAssured.port = this.port;

        testDataDto = AuditEventDto.builder()
                .login("TEST").user_id(1L).company_id(1L)
                .event_type("TEST")
                .client_agent("IntegrationTests")
                .timestamp("2019-01-01T11:45:05.123Z[GMT]")
                .build();
    }


    @Test
    public void testGetJournal() throws Exception {
        given()
                .accept(ContentType.JSON)
        .when()
                .get("/api/audit/journal")
        .then()
                .assertThat()
                .statusCode(HttpStatus.SC_FORBIDDEN);
    }

    @Test
    public void testGetJournalWithAuth() throws Exception {
        given()
                .header("X-API-KEY", token)
                .accept(ContentType.JSON)
        .when()
                .get("/api/audit/journal")
        .then()
                .assertThat()
                .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testSave() throws Exception {
        given()
                .contentType(ContentType.JSON)
                .body(testDataDto)
        .when()
                .post("/api/audit/collector")
        .then()
                .statusCode(403);
    }

    @Test
    public void testSaveWithAuth() throws Exception {
        given()
                .header("X-API-KEY", token)
                .contentType(ContentType.JSON)
                .body(testDataDto)
        .when()
                .post("/api/audit/collector")
        .then()
                .statusCode(201);
    }


    @Test
    public void testGetJournalFilterWithAuth() throws Exception {
        //Map<String,String> paramsMap = new HashMap<>();
        given()
                .header("X-API-KEY", token)
                .accept(ContentType.JSON)
                //.param("q", "")
                .when()
                    .get("/api/audit/journal/filter")
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK);
    }

    @Test
    public void testGetJournalSearchWithAuth() throws Exception {
        given()
                .header("X-API-KEY", token)
                .accept(ContentType.JSON)
                .param("q", "eventType:LOGIN")
                .when()
                    .get("/api/audit/journal/search")
                .then()
                    .assertThat()
                    .statusCode(HttpStatus.SC_OK);
    }


}
