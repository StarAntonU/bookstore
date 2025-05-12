package project.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import project.bookstore.dto.cartitem.CreateCartItemRequestDto;
import project.bookstore.dto.cartitem.UpdateCartItemDto;
import project.bookstore.dto.shoppingcart.ShoppingCartDto;
import project.bookstore.service.ShoppingCartService;

@Tag(name = "ShoppingCart", description = "Endpoints for CRUD operations with shopping cart")
@RestController
@RequestMapping("/cart")
@RequiredArgsConstructor
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "add item", description = "add the item to shopping cart")
    @PostMapping
    public ShoppingCartDto add(@RequestBody @Valid CreateCartItemRequestDto requestDto,
                           Authentication authentication) {
        return shoppingCartService.addItem(requestDto, authentication);
    }

    @Operation(summary = "view items", description = "view all items in the shopping cart")
    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        return shoppingCartService.getShoppingCart(authentication);
    }

    @Operation(summary = "update item",
            description = "update the quantity item in the shopping cart")
    @PutMapping("/items/{cartItemId}")
    public ShoppingCartDto update(@PathVariable Long cartItemId,
                                  @RequestBody @Valid UpdateCartItemDto requestDto,
                                  Authentication authentication) {
        return shoppingCartService.updateCartItem(cartItemId, requestDto, authentication);
    }

    @Operation(summary = "delete item", description = "delete the item in shopping cart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        shoppingCartService.delete(id);
    }
}
