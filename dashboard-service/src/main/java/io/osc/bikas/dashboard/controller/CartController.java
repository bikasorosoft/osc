package io.osc.bikas.dashboard.controller;

import io.osc.bikas.dashboard.dto.CartDto;
import io.osc.bikas.dashboard.dto.GetCartRequestDto;
import io.osc.bikas.dashboard.dto.ResponseDto;
import io.osc.bikas.dashboard.dto.UpdateCartItemRequestDto;
import io.osc.bikas.dashboard.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/user/cart")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @PostMapping("/increase")
    public ResponseEntity<ResponseDto> increaseItemInCart(@RequestBody UpdateCartItemRequestDto updateCartItemRequestDto) {
        cartService.updateCartItem(updateCartItemRequestDto.userId(), updateCartItemRequestDto.productId(), updateCartItemRequestDto.count());
        return ResponseEntity.ok(new ResponseDto(200, null));
    }

    @PostMapping("/decrease")
    public ResponseEntity<ResponseDto> decreaseItemInCart(@RequestBody UpdateCartItemRequestDto updateCartItemRequestDto) {
        cartService.updateCartItem(updateCartItemRequestDto.userId(), updateCartItemRequestDto.productId(), updateCartItemRequestDto.count()*(-1));
        return ResponseEntity.ok(new ResponseDto(200, null));
    }

    @PostMapping("/remove")
    public ResponseEntity<ResponseDto> removeItemInCart(@RequestBody UpdateCartItemRequestDto updateCartItemRequestDto) {
        cartService.removeCartItem(updateCartItemRequestDto.userId(), updateCartItemRequestDto.productId());
        return ResponseEntity.ok(new ResponseDto(200, null));
    }

    @PostMapping("/view")
    public ResponseEntity<ResponseDto> getCart(@RequestBody GetCartRequestDto getCartRequestDto) {
        CartDto response = cartService.getCart(getCartRequestDto.userId());
        return ResponseEntity.ok(new ResponseDto(200, response));
    }
}
