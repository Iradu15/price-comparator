package com.price_comparator.price_comparator.Service;

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
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

@SpringBootTest
@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@AutoConfigureMockMvc
public class ShoppingListServiceTest extends AbstractBaseTest {
    @Autowired
    ShoppingListService shoppingListService;

    @Test
    void testProcessOneShoppingListSuccess() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("Lidl");
        Store store2 = setUpStoreEntity("Kaufland");
        Product product1 = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        Product product2 = setUpProduct("P011", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product1, "ron", 12.5, currentDate.minusDays(1), null);
        setUpProductPrice(store2, product2, "ron", 30.0, currentDate.minusDays(1), null);

        ShoppingList shoppingList = setUpShoppingList(List.of(product1, product2), List.of(2, 1));


        ShoppingListResponseDto expectedResult =
                new ShoppingListResponseDto(List.of(new ShoppingListItemDto(product1.getProductId(),
                2,
                25.0,
                "Lidl"), new ShoppingListItemDto(product2.getProductId(), 1, 30.0, "Kaufland")), 55.0);

        ShoppingListResponseDto res = shoppingListService.processShoppingList(shoppingList);
        assert (res.equals(expectedResult));
    }

    @Test
    void testProcessOneShoppingListSelectCheapestStore() {
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

        ShoppingListResponseDto res = shoppingListService.processShoppingList(shoppingList);
        assert (res.equals(expectedResult));
    }

    @Test
    void testProcessOneShoppingListSelectCheapestStoreWithDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("Lidl");
        Store store2 = setUpStoreEntity("Kaufland");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 12.5, currentDate.minusDays(1), null);
        setUpProductPrice(store2, product, "ron", 15.0, currentDate.minusDays(1), null);
        setUpDiscount(product, store2, 50, currentDate.minusDays(1), currentDate.plusDays(1));

        ShoppingList shoppingList = setUpShoppingList(List.of(product), List.of(2));


        ShoppingListResponseDto expectedResult =
                new ShoppingListResponseDto(List.of(new ShoppingListItemDto(product.getProductId(),
                2,
                15.0,
                "Kaufland")), 15.0);

        ShoppingListResponseDto res = shoppingListService.processShoppingList(shoppingList);
        assert (res.equals(expectedResult));
    }

    @Test
    void testProcessOneShoppingListSelectCheapestStoreWithUnavailableDiscount() {
        LocalDate currentDate = setUpCurrentDate(LocalDate.now()).getCurrentDay();
        Store store = setUpStoreEntity("Lidl");
        Store store2 = setUpStoreEntity("Kaufland");
        Product product = setUpProduct("P001", "lapte", "lactate", "zuzu", 1.0, "l");
        setUpProductPrice(store, product, "ron", 12.5, currentDate.minusDays(1), null);
        setUpProductPrice(store2, product, "ron", 15.0, currentDate.minusDays(1), null);
        setUpDiscount(product, store2, 50, currentDate.minusDays(2), currentDate.minusDays(1));

        ShoppingList shoppingList = setUpShoppingList(List.of(product), List.of(2));


        ShoppingListResponseDto expectedResult =
                new ShoppingListResponseDto(List.of(new ShoppingListItemDto(product.getProductId(),
                2,
                25.0,
                "Lidl")), 25.0);

        ShoppingListResponseDto res = shoppingListService.processShoppingList(shoppingList);
        assert (res.equals(expectedResult));
    }
}

