package io.osc.bikas.product.controller;

import io.osc.bikas.product.model.Cart;
import io.osc.bikas.product.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/view")
    public ResponseEntity<Response> findCartByUserId(@RequestParam(name = "userId") String userId) {
        Cart cart = cartService.findCartByUserId(userId);
        return ResponseEntity.ok(new Response(200, Map.of("cart", cart)));
    }

//    payload = {"count": 2,"userId": "user001", "prodId": "K01"}
    @PostMapping("/increase")
    public ResponseEntity<Response> increaseProductInCart(@RequestParam(name = "count") Integer count,
                                                          @RequestParam(name = "userId") String userId,
                                                          @RequestParam(name = "prodId") String productId) {
        cartService.increaseProductInCart(count, userId, productId);
        return ResponseEntity.ok(new Response(200, null));
    }

    @PostMapping("/decrease")
    public ResponseEntity<Response> decreaseProductInCart(@RequestParam(name = "count") Integer count,
                                                          @RequestParam(name = "userId") String userId,
                                                          @RequestParam(name = "prodId") String productId) {
        cartService.decreaseProductInCart(count, userId, productId);
        return ResponseEntity.ok(new Response(200, null));
    }

    @PostMapping("/remove")
    public ResponseEntity<Response> removeItem(@RequestParam(name = "userId") String userId,
                                               @RequestParam(name = "prodId") String productId) {
        cartService.removeItem(userId, productId);
        return ResponseEntity.ok(new Response(200, null));
    }

}
