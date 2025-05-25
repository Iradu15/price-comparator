package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.AbstractBaseTest;
import com.price_comparator.price_comparator.Model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class AlertsControllerTest extends AbstractBaseTest {
    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void testCreateAlertSuccess() {
        setUpCurrentDate(LocalDate.now());
        setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        String requestBody = """
                {
                    "productId": "P001",
                    "targetPrice": 12.40
                }
                """;

        assertThat(mockMvcTester.post()
                .uri("/createAlert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).hasStatusOk();
    }

    @Test
    void testCreateAlertWrongNotExistentProductId() {
        setUpCurrentDate(LocalDate.now());
        setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        String requestBody = """
                {
                    "productId": "P002",
                    "targetPrice": 12.40
                }
                """;

        assertThat(mockMvcTester.post()
                .uri("/createAlert")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).hasStatus(400);
    }

    @Test
    void testListAlerts() {
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpAlert(product, 12.3);

        assertThat(mockMvcTester.get().uri("/alerts")).hasStatusOk();
    }

    @Test
    void testDeleteAlertSuccess() {
        Product product = setUpProduct("P100", "faina", "panificatie", "bucovina", 1.0, "kg");
        Long alertId = setUpAlert(product, 9.99).getId();

        assertThat(mockMvcTester.delete().uri("/deleteAlert/" + alertId)).hasStatusOk().hasBodyTextEqualTo(
                "Alert with id " + alertId + " was deleted successfully");
    }

    @Test
    void testDeleteAlertNonExistentId() {
        long nonExistentId = 9999L;

        assertThat(mockMvcTester.delete().uri("/deleteAlert/" + nonExistentId)).hasStatus(400).hasBodyTextEqualTo(
                "Alert with id " + nonExistentId + " does not exist");
    }

    @Test
    void testDeleteAlertInvalidIdFormat() {
        String invalidId = "abc";

        assertThat(mockMvcTester.delete().uri("/deleteAlert/" + invalidId)).hasStatus(400).hasBodyTextEqualTo(
                "Invalid alert ID format: " + invalidId);
    }
}
