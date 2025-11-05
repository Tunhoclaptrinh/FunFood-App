package com.example.funfood.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.CartApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Cart;
import com.example.funfood.domain.model.CartItem;
import com.example.funfood.util.Resource;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CartRepository {

    private final CartApi cartApi;

    public CartRepository(Context context) {
        this.cartApi = RetrofitClient.getInstance(context).createService(CartApi.class);
    }

    /**
     * Lấy giỏ hàng hiện tại
     */
    public LiveData<Resource<Cart>> getCart() {
        MutableLiveData<Resource<Cart>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        cartApi.getCart().enqueue(new Callback<ApiResponse<Cart>>() {
            @Override
            public void onResponse(Call<ApiResponse<Cart>> call, Response<ApiResponse<Cart>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Cart> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể tải giỏ hàng", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Cart>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Thêm sản phẩm vào giỏ hàng
     */
    public LiveData<Resource<CartItem>> addToCart(int productId, int quantity) {
        MutableLiveData<Resource<CartItem>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        CartApi.AddToCartRequest request = new CartApi.AddToCartRequest(productId, quantity);

        cartApi.addToCart(request).enqueue(new Callback<ApiResponse<CartItem>>() {
            @Override
            public void onResponse(Call<ApiResponse<CartItem>> call, Response<ApiResponse<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<CartItem> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể thêm vào giỏ hàng", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CartItem>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Cập nhật số lượng sản phẩm trong giỏ
     */
    public LiveData<Resource<CartItem>> updateCartItem(int cartItemId, int quantity) {
        MutableLiveData<Resource<CartItem>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        CartApi.UpdateCartItemRequest request = new CartApi.UpdateCartItemRequest(quantity);

        cartApi.updateCartItem(cartItemId, request).enqueue(new Callback<ApiResponse<CartItem>>() {
            @Override
            public void onResponse(Call<ApiResponse<CartItem>> call, Response<ApiResponse<CartItem>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<CartItem> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể cập nhật giỏ hàng", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<CartItem>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Xóa sản phẩm khỏi giỏ
     */
    public LiveData<Resource<Void>> removeFromCart(int cartItemId) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        cartApi.removeFromCart(cartItemId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(null));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể xóa sản phẩm", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Xóa tất cả items của một nhà hàng
     */
    public LiveData<Resource<Void>> removeRestaurantCart(int restaurantId) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        cartApi.removeRestaurantCart(restaurantId).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(null));
                } else {
                    result.setValue(Resource.error("Không thể xóa giỏ hàng", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Xóa toàn bộ giỏ hàng
     */
    public LiveData<Resource<Void>> clearCart() {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        cartApi.clearCart().enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call, Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    result.setValue(Resource.success(null));
                } else {
                    result.setValue(Resource.error("Không thể xóa giỏ hàng", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }
}