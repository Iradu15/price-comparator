package com.price_comparator.price_comparator.Controller;

import com.price_comparator.price_comparator.DTO.ShoppingListItemDto;
import com.price_comparator.price_comparator.DTO.ShoppingListRequestDto;
import com.price_comparator.price_comparator.Model.Product;
import com.price_comparator.price_comparator.Model.ShoppingList;
import com.price_comparator.price_comparator.Model.ShoppingListItem;
import com.price_comparator.price_comparator.Repository.ProductRepository;
import com.price_comparator.price_comparator.Repository.ShoppingListItemRepository;
import com.price_comparator.price_comparator.Repository.ShoppingListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
public class ShoppingListController {
    @Autowired
    ProductRepository productRepository;

    @Autowired
    ShoppingListRepository shoppingListRepository;

    @Autowired
    ShoppingListItemRepository shoppingListItemRepository;

    @PostMapping("createShoppingList")
    public ResponseEntity<String> createShoppingList(@RequestBody ShoppingListRequestDto shoppingListRequestDto) {
        List<ShoppingListItemDto> shoppingListItems = shoppingListRequestDto.items();
        ShoppingList shoppingList = new ShoppingList();
        List<ShoppingListItem> items = new ArrayList<>();


        for (ShoppingListItemDto itemDto: shoppingListItems){
            String productId = itemDto.productId();
            int quantity = itemDto.quantity();

            if (quantity <= 0)
                return ResponseEntity.badRequest().body("Quantity must be > 0");

            if (productId == null || productId.isBlank())
                return ResponseEntity.badRequest().body("Product ID must not be null or empty");

            Optional<Product> product = productRepository.findByProductId(productId);

            if (product.isEmpty()) {
                return ResponseEntity.badRequest().body("Product with id " + productId + " does not exist");
            }

            ShoppingListItem item = new ShoppingListItem(product.get(), shoppingList, quantity);
            items.add(item);
        }

        shoppingList.setShoppingListItems(items);
        shoppingListRepository.save(shoppingList);

        return ResponseEntity.ok("Shopping list created:\n" + shoppingList.toString() + "\n");
    }

    @GetMapping("shoppingLists")
    ResponseEntity<String> getShoppingLists(){
        List<ShoppingList> shoppingLists = shoppingListRepository.findAll();
        return ResponseEntity.ok("Available shopping lists:\n" + shoppingLists.toString());
    }


    @GetMapping("shoppingList/{id}")
    public ResponseEntity<String> listShoppingList(@PathVariable String id) {
        long shoppingListId;

        try {
            shoppingListId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid ID format: " + id);
        }

        return shoppingListRepository.findById(shoppingListId)
                .map(shoppingList -> ResponseEntity.ok("Shopping list with id " + id + ":\n" + shoppingList))
                .orElseGet(() -> ResponseEntity.badRequest().body("Shopping list with id " + id + " does not exist"));
    }


    @DeleteMapping("deleteShoppingList/{id}")
    public ResponseEntity<String> deleteShoppingList(@PathVariable String id) {
        long shoppingListId;

        try {
            shoppingListId = Long.parseLong(id);
        } catch (NumberFormatException e) {
            return ResponseEntity.badRequest().body("Invalid ID format: " + id);
        }

        return shoppingListRepository.findById(shoppingListId)
                .map(shoppingList -> {
                    shoppingListRepository.deleteById(shoppingListId);
                    return ResponseEntity.ok("Shopping list with id " + id + " was deleted successfully");
                })
                .orElseGet(() -> ResponseEntity.badRequest().body("Shopping list with id " + id + " does not exist"));
    }

}
