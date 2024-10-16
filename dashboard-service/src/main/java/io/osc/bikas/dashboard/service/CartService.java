package io.osc.bikas.dashboard.service;

import io.osc.bikas.dashboard.dto.CartDto;
import io.osc.bikas.dashboard.dto.CartItemDto;
import io.osc.bikas.dashboard.dto.ProductDto;
import io.osc.bikas.dashboard.grpc.GrpcCartDataServiceClient;
import io.osc.bikas.dashboard.grpc.GrpcProductDataServiceClient;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartService {

    private final GrpcCartDataServiceClient cartDataServiceClient;
    private final GrpcProductDataServiceClient productDataServiceClient;

    public void updateCartItem(String userId, String productId, Integer count) {
        cartDataServiceClient.updateCartItem(userId, productId, count);
    }

    public void removeCartItem(String userId, String productId) {
        cartDataServiceClient.removeItemFromCart(userId, productId);
    }

    public CartDto getCart(String userId) {

        //get user cart details bu user id from cart data service
        List<CartItemDto> cartItems = cartDataServiceClient.getCartItemsDetail(userId);

        List<String> productIdList = cartItems.stream().map(CartItemDto::getProductId).collect(Collectors.toList());
        Map<String, ProductDto> productDtoMap = productDataServiceClient.getAllProductById(productIdList).stream()
                .collect(Collectors.toMap(ProductDto::productId, productDto -> productDto));

        int totalQuantity = 0;
        double totalPrice = 0D;

        for(CartItemDto item: cartItems) {
            var product = productDtoMap.get(item.getProductId());
            item.setUserId(userId);
            item.setProductName(product.productName());
            item.setPrice(product.productPrice());
            item.setCategoryId(String.valueOf(product.productId().charAt(0)));
            totalQuantity+=item.getQuantity();
            totalPrice += product.productPrice() * item.getQuantity();
        }

        return new CartDto(cartItems, totalQuantity, totalPrice);

    }


}
