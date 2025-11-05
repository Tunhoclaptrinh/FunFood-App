package com.example.funfood.data.remote.api;

import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.data.repository.AddressRepository;
import com.example.funfood.domain.model.Address;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;

public interface AddressApi {

    @GET("addresses")
    Call<ApiResponse<List<Address>>> getAddresses();

    @GET("addresses/default")
    Call<ApiResponse<Address>> getDefaultAddress();

    @GET("addresses/{id}")
    Call<ApiResponse<Address>> getAddressById(@Path("id") int id);

    @POST("addresses")
    Call<ApiResponse<Address>> createAddress(
            @Body AddressRepository.CreateAddressRequest request
    );

    @PUT("addresses/{id}")
    Call<ApiResponse<Address>> updateAddress(
            @Path("id") int id,
            @Body AddressRepository.UpdateAddressRequest request
    );

    @PATCH("addresses/{id}/default")
    Call<ApiResponse<Address>> setDefaultAddress(@Path("id") int id);

    @DELETE("addresses/{id}")
    Call<ApiResponse<Void>> deleteAddress(@Path("id") int id);

    @DELETE("addresses")
    Call<ApiResponse<Void>> deleteAllAddresses();
}