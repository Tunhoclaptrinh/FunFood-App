package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Address; // Dùng tạm model
import java.util.List;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PATCH;
import retrofit2.http.Path;

public interface AddressApi {

    @GET("addresses")
    Call<ApiResponse<List<Address>>> getAddresses();

    @GET("addresses/default")
    Call<ApiResponse<Address>> getDefaultAddress();

    @GET("addresses/{id}")
    Call<ApiResponse<Address>> getAddressDetails(@Path("id") int addressId);

    @POST("addresses")
    Call<ApiResponse<Address>> createAddress(@Body Address address);

    @PUT("addresses/{id}")
    Call<ApiResponse<Address>> updateAddress(@Path("id") int addressId, @Body Address address);

    @PATCH("addresses/{id}/default")
    Call<ApiResponse<Address>> setDefaultAddress(@Path("id") int addressId);

    @DELETE("addresses/{id}")
    Call<ApiResponse<Object>> deleteAddress(@Path("id") int addressId);
}