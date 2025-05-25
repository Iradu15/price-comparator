package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.AbstractBaseTest;
import com.price_comparator.price_comparator.DTO.ShoppingListItemDto;
import com.price_comparator.price_comparator.DTO.ShoppingListResponseDto;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ShoppingList;
import com.price_comparator.price_comparator.Model.Store;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.assertj.MockMvcTester;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class ShoppingListControllerTest extends AbstractBaseTest {
    @Autowired
    MockMvcTester mockMvcTester;

    @Test
    void testCreateShoppingListSuccess() {
        setUpCurrentDate(LocalDate.now());
        setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProduct("P011", "lapte", "lactate", "zuzu", 1.0, "l");
        String requestBody = """
                 {
                    "items": [
                      { "productId": "P001", "quantity": 1 },
                      { "productId": "P011", "quantity": 2 }
                    ]
                 }
                """;

        assertThat(mockMvcTester.post()
                .uri("/createShoppingList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).hasStatusOk();
    }

    @Test
    void testCreateShoppingListWrongNotExistentProductId() {
        setUpCurrentDate(LocalDate.now());
        setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        String requestBody = """
                {
                    "items": [
                      { "productId": "P001", "quantity": 1 },
                      { "productId": "P011", "quantity": 2 }
                    ]
                 }
                """;

        assertThat(mockMvcTester.post()
                .uri("/createShoppingList")
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestBody)).hasStatus(400);
    }

    @Test
    void testListShoppingLists() {
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpShoppingList(List.of(product), List.of(1));
        assertThat(mockMvcTester.get().uri("/shoppingLists")).hasStatusOk();
    }

    @Test
    void testDeleteShoppingListSuccess() {
        Product product = setUpProduct("P100", "faina", "panificatie", "bucovina", 1.0, "kg");
        ShoppingList shoppingList = setUpShoppingList(List.of(product), List.of(1));

        assertThat(mockMvcTester.delete().uri("/deleteShoppingList/" + shoppingList.getId())).hasStatusOk()
                .hasBodyTextEqualTo("Shopping list with id " + shoppingList.getId() + " was deleted successfully");
    }

    @Test
    void testDeleteShoppingListNonExistentId() {
        long nonExistentId = 9999L;

        assertThat(mockMvcTester.delete().uri("/deleteShoppingList/" + nonExistentId)).hasStatus(400)
                .hasBodyTextEqualTo("Shopping list with id " + nonExistentId + " does not exist");
    }

    @Test
    void testDeleteShoppingListInvalidIdFormat() {
        String invalidId = "abc";

        assertThat(mockMvcTester.delete().uri("/deleteShoppingList/" + invalidId)).hasStatus(400).hasBodyTextEqualTo(
                "Invalid ID format: " + invalidId);
    }

    @Test
    void testProcessShoppingListSucess() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("Lidl");
        Store store2 = setUpStoreEntity("Kaufland");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 12.5, currentDate.minusDays(1), null);
        setUpProductPrice(store2, product, "ron", 15.0, currentDate.minusDays(1), null);

        ShoppingList shoppingList = setUpShoppingList(List.of(product), List.of(2));

        ShoppingListResponseDto expectedResult =
                new ShoppingListResponseDto(List.of(new ShoppingListItemDto(product.getProductId(),
                2,
                25.0,
                "Lidl")), 25.0);

        assertThat(mockMvcTester.get().uri("/processShoppingList/" + shoppingList.getId())).hasStatusOk()
                .hasBodyTextEqualTo("Shopping List management:\n" + expectedResult);
    }

    @Test
    void testProcessShoppingListInexistentId() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("Lidl");
        Store store2 = setUpStoreEntity("Kaufland");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 12.5, currentDate.minusDays(1), null);
        setUpProductPrice(store2, product, "ron", 15.0, currentDate.minusDays(1), null);

        setUpShoppingList(List.of(product), List.of(2));

        long id = 999L;
        assertThat(mockMvcTester.get().uri("/processShoppingList/" + id)).hasStatus(400).hasBodyTextEqualTo(
                "Shopping list with id " + id + " does not exist");
    }

    @Test
    void testProcessShoppingListInvalidFormatId() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("Lidl");
        Store store2 = setUpStoreEntity("Kaufland");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 12.5, currentDate.minusDays(1), null);
        setUpProductPrice(store2, product, "ron", 15.0, currentDate.minusDays(1), null);

        setUpShoppingList(List.of(product), List.of(2));

        String id = "abc";
        assertThat(mockMvcTester.get().uri("/processShoppingList/" + id)).hasStatus(400).hasBodyTextEqualTo(
                "Invalid ID format: " + id);
    }
}


