package io.osc.bikas.dashboard.service;

import io.osc.bikas.dashboard.dto.CartDto;
import io.osc.bikas.dashboard.dto.CartItemQuantityDto;
import io.osc.bikas.dashboard.dto.CartItemDto;
import io.osc.bikas.dashboard.dto.ProductDto;
import io.osc.bikas.dashboard.grpc.GrpcCartDataServiceClient;
import io.osc.bikas.dashboard.grpc.GrpcProductDataServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final GrpcCartDataServiceClient cartDataServiceClient;
    private final GrpcProductDataServiceClient productDataServiceClient;

    private final ProductService productService;

    public void updateCartItem(String userId, String productId, Integer count) {
        cartDataServiceClient.updateCartItem(userId, productId, count);
    }

    public void removeCartItem(String userId, String productId) {
        cartDataServiceClient.removeItemFromCart(userId, productId);
    }

    public CartDto getCartById(String userId) {

        //get user cart details bu user id from cart data service
        List<CartItemQuantityDto> cartItemsCount = cartDataServiceClient.getCartItemsDetail(userId);

        List<String> productIdList = cartItemsCount.stream()
                .map(CartItemQuantityDto::getProductId)
                .collect(Collectors.toList());

        Map<String, ProductDto> productMap = productService.getAllProductById(productIdList)
                .stream().collect(Collectors.toMap(
                        ProductDto::productId,
                        value -> value ));

        List<CartItemDto> cartItemDtos = new ArrayList<>();

        for(CartItemQuantityDto cartItemCountDto: cartItemsCount) {
            //update cart item details
            ProductDto productDto = productMap.get(cartItemCountDto.getProductId());
            cartItemDtos.add(new CartItemDto(userId, cartItemCountDto.getQuantity(), productDto));
        }

        return new CartDto(cartItemDtos);

    }

//    private CartItemDto generateCartItemDto(String userId, Integer quantity, ProductDto productDto) {
//        return CartItemDto.builder()
//                .userId(userId)
//                .productId(productDto.productId())
//                .categoryId(productDto.categoryId())
//                .productName(productDto.productName())
//                .price(productDto.productPrice())
//                .quantity(quantity)
//                .build();
//    }
}
