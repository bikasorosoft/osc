package io.osc.bikas.product.data.service;

import io.osc.bikas.product.data.exception.ProductNotFoundException;
import io.osc.bikas.product.data.model.Cart;
import io.osc.bikas.product.data.model.CartProduct;
import io.osc.bikas.product.data.model.Product;
import io.osc.bikas.product.data.repo.CartProductRepository;
import io.osc.bikas.product.data.repo.CartRepository;
import io.osc.bikas.product.data.repo.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;

@Service
@RequiredArgsConstructor
public class CartService {

    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartProductRepository cartProductRepository;

    public Cart findCartByUserId(String userId) {
        return cartRepository.findByUserId(userId).get();
    }

    public void increaseProductInCart(Integer quantity, String userId, String productId) {

        // get or create cart by id
        Cart cart = getOrCreateCartByUserId(userId);

        //find product
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new ProductNotFoundException("Product not found"));

        //get or create cart product
        CartProduct cartProduct = cart.getCartProducts().stream()
                .filter(cp -> cp.getProduct().getProductId().equals(productId))
                .findFirst().
                orElse(new CartProduct(null, product, 0));

        //update quantity
        cartProduct.setQuantity(cartProduct.getQuantity()+quantity);

        //add or update cart product in cart
        cart.getCartProducts().add(cartProduct);
        cartRepository.save(cart);
    }

    public void decreaseProductInCart(Integer count, String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId).get();

        CartProduct cartProduct = cart.getCartProducts().stream()
                .filter(cp -> cp.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

        int newQuantity = cartProduct.getQuantity() - count;

        if(newQuantity <= 0) {
            cart.getCartProducts().remove(cartProduct);
            cartProductRepository.delete(cartProduct);
        } else {
            cartProduct.setQuantity(newQuantity);
        }
        cartRepository.save(cart);
    }

    public void removeItem(String userId, String productId) {
        Cart cart = cartRepository.findByUserId(userId).get();

        CartProduct cartProduct = cart.getCartProducts().stream()
                .filter(cp -> cp.getProduct().getProductId().equals(productId))
                .findFirst()
                .orElseThrow(() -> new ProductNotFoundException("Product not found in cart"));

        cart.getCartProducts().remove(cartProduct);
        cartProductRepository.delete(cartProduct);

        cartRepository.save(cart);
    }

    private Cart getOrCreateCartByUserId(String userId) {
        return cartRepository.findByUserId(userId)
                .orElse(new Cart(null, userId, new HashSet<>()));
    }
}
