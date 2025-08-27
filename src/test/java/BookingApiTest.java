import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import org.junit.jupiter.api.Test;

import io.restassured.RestAssured;
import static io.restassured.RestAssured.given;
import io.restassured.http.ContentType;

public class BookingApiTest {

    // Método utilitário para ler arquivos JSON
    private String lerJson(String caminho) throws IOException {
        return new String(Files.readAllBytes(Paths.get(caminho)));
    }

    @Test
    public void testGerarToken() throws IOException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String authPayload = lerJson("src/test/resources/Auth.json");

        String token = given()
            .contentType(ContentType.JSON)
            .body(authPayload)
        .when()
            .post("/auth")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .extract().path("token");

        System.out.println("🔑 Token gerado: " + token);
    }

    @Test
    public void testCriarBooking() throws IOException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String createBookingPayload = lerJson("src/test/resources/CriarBooking.json");

        int bookingId = given()
            .contentType(ContentType.JSON)
            .body(createBookingPayload)
        .when()
            .post("/booking")
        .then()
            .statusCode(200)
            .body("booking.firstname", equalTo("Thamires"))
            .extract().path("bookingid");

        System.out.println(" Booking criado com ID: " + bookingId);
    }

    @Test
    public void testAtualizarBooking() throws IOException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String authPayload = lerJson("src/test/resources/Auth.json");
        String updateBookingPayload = lerJson("src/test/resources/AtualizarBooking.json");

        // 1 - Gerar token
        String token = given()
            .contentType(ContentType.JSON)
            .body(authPayload)
        .when()
            .post("/auth")
        .then()
            .statusCode(200)
            .extract().path("token");

        // 2 - Criar booking temporário para atualizar
        String createBookingPayload = lerJson("src/test/resources/CriarBooking.json");

        int bookingId = given()
            .contentType(ContentType.JSON)
            .body(createBookingPayload)
        .when()
            .post("/booking")
        .then()
            .statusCode(200)
            .extract().path("bookingid");

        // 3 - Atualizar o booking
        given()
            .contentType(ContentType.JSON)
            .cookie("token", token)
            .body(updateBookingPayload)
        .when()
            .put("/booking/" + bookingId)
        .then()
            .statusCode(200)
            .body("lastname", equalTo("Marina"))
            .body("totalprice", equalTo(200));

        System.out.println("✏️ Booking atualizado com ID: " + bookingId);
    }
}

