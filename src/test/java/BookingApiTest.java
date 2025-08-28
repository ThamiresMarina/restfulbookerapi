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

        var response = given()
            .contentType(ContentType.JSON)
            .body(authPayload)
        .when()
            .post("/auth")
        .then()
            .statusCode(200)
            .body("token", notNullValue())
            .extract().response();

        String token = response.jsonPath().getString("token");

        System.out.println(" Token gerado: " + token);
        System.out.println(" Resposta completa:\n" + response.asPrettyString());
    }

    @Test
    public void testCriarBooking() throws IOException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String createBookingPayload = lerJson("src/test/resources/CriarBooking.json");

        var response = given()
            .contentType(ContentType.JSON)
            .body(createBookingPayload)
        .when()
            .post("/booking")
        .then()
            .statusCode(200)
            .body("booking.firstname", equalTo("Thamires"))
            .extract().response();

        int bookingId = response.jsonPath().getInt("bookingid");

        System.out.println(" Booking criado com sucesso!");
        System.out.println(" Detalhes da reserva:\n" + response.asPrettyString());
        System.out.println(" ID da reserva: " + bookingId);
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
        var response = given()
            .contentType(ContentType.JSON)
            .cookie("token", token)
            .body(updateBookingPayload)
        .when()
            .put("/booking/" + bookingId)
        .then()
            .statusCode(200)
            .body("lastname", equalTo("Marina"))
            .body("totalprice", equalTo(200))
            .extract().response();

        System.out.println(" Booking atualizado com sucesso!");
        System.out.println(" Detalhes da reserva atualizada:\n" + response.asPrettyString());
    }

    @Test
    public void testDeletarBooking() throws IOException {
        RestAssured.baseURI = "https://restful-booker.herokuapp.com";

        String authPayload = lerJson("src/test/resources/Auth.json");

        // 1 - Gerar token
        String token = given()
            .contentType(ContentType.JSON)
            .body(authPayload)
        .when()
            .post("/auth")
        .then()
            .statusCode(200)
            .extract().path("token");

        // 2 - Criar booking temporário para deletar
        String createBookingPayload = lerJson("src/test/resources/CriarBooking.json");

        int bookingId = given()
            .contentType(ContentType.JSON)
            .body(createBookingPayload)
        .when()
            .post("/booking")
        .then()
            .statusCode(200)
            .extract().path("bookingid");

        // 3 - Deletar o booking
        var response = given()
            .cookie("token", token)
        .when()
            .delete("/booking/" + bookingId)
        .then()
            .statusCode(201) // sucesso no Restful Booker
            .extract().response();

        System.out.println(" Booking deletado com sucesso!");
        System.out.println(" Resposta da deleção:\n" + response.asPrettyString());
        System.out.println(" ID deletado: " + bookingId);
    }
}


