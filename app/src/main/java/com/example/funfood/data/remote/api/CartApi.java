package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Cart;
import com.example.funfood.domain.model.CartItem;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface CartApi {

    @GET("cart")
    Call<ApiResponse<Cart>> getCart();

    @POST("cart")
    Call<ApiResponse<CartItem>> addToCart(@Body AddToCartRequest request);

    @PUT("cart/{id}")
    Call<ApiResponse<CartItem>> updateCartItem(
            @Path("id") int cartItemId,
            @Body UpdateCartItemRequest request
    );

    @DELETE("cart/{id}")
    Call<ApiResponse<Void>> removeFromCart(@Path("id") int cartItemId);

    @DELETE("cart/restaurant/{restaurantId}")
    Call<ApiResponse<Void>> removeRestaurantCart(@Path("restaurantId") int restaurantId);

    @DELETE("cart")
    Call<ApiResponse<Void>> clearCart();

    @POST("cart/sync")
    Call<ApiResponse<CartSyncResponse>> syncCart(@Body CartSyncRequest request);

    // Request DTOs
    class AddToCartRequest {
        private int productId;
        private int quantity;

        public AddToCartRequest(int productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public int getProductId() { return productId; }
        public int getQuantity() { return quantity; }
    }

    class UpdateCartItemRequest {
        private int quantity;

        public UpdateCartItemRequest(int quantity) {
            this.quantity = quantity;
        }

        public int getQuantity() { return quantity; }
    }

    class CartSyncRequest {
        private java.util.List<AddToCartRequest> items;

        public CartSyncRequest(java.util.List<AddToCartRequest> items) {
            this.items = items;
        }

        public java.util.List<AddToCartRequest> getItems() { return items; }
    }

    class CartSyncResponse {
        private int synced;
        private int errors;

        public int getSynced() { return synced; }
        public int getErrors() { return errors; }
    }
}