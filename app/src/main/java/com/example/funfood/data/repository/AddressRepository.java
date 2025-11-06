package com.example.funfood.data.repository;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.funfood.data.remote.RetrofitClient;
import com.example.funfood.data.remote.api.AddressApi;
import com.example.funfood.data.remote.dto.ApiResponse;
import com.example.funfood.domain.model.Address;
import com.example.funfood.util.Resource;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddressRepository {

    private final AddressApi addressApi;

    public AddressRepository(Context context) {
        this.addressApi = RetrofitClient.getInstance(context).createService(AddressApi.class);
    }

    /**
     * Lấy danh sách địa chỉ
     */
    public LiveData<Resource<List<Address>>> getAddresses() {
        MutableLiveData<Resource<List<Address>>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        addressApi.getAddresses().enqueue(new Callback<ApiResponse<List<Address>>>() {
            @Override
            public void onResponse(Call<ApiResponse<List<Address>>> call,
                                   Response<ApiResponse<List<Address>>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<List<Address>> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể tải danh sách địa chỉ", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<List<Address>>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Lấy địa chỉ mặc định
     */
    public LiveData<Resource<Address>> getDefaultAddress() {
        MutableLiveData<Resource<Address>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        addressApi.getDefaultAddress().enqueue(new Callback<ApiResponse<Address>>() {
            @Override
            public void onResponse(Call<ApiResponse<Address>> call,
                                   Response<ApiResponse<Address>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Address> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể tải địa chỉ mặc định", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Address>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Tạo địa chỉ mới
     */
    public LiveData<Resource<Address>> createAddress(String label, String address,
                                                     String recipientName, String recipientPhone,
                                                     double latitude, double longitude,
                                                     String note, boolean isDefault) {
        MutableLiveData<Resource<Address>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        CreateAddressRequest request = new CreateAddressRequest(
                label, address, recipientName, recipientPhone,
                latitude, longitude, note, isDefault
        );

        addressApi.createAddress(request).enqueue(new Callback<ApiResponse<Address>>() {
            @Override
            public void onResponse(Call<ApiResponse<Address>> call,
                                   Response<ApiResponse<Address>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Address> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể tạo địa chỉ", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Address>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Cập nhật địa chỉ
     */
    public LiveData<Resource<Address>> updateAddress(int id, String label, String address,
                                                     String recipientName, String recipientPhone,
                                                     double latitude, double longitude,
                                                     String note) {
        MutableLiveData<Resource<Address>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        UpdateAddressRequest request = new UpdateAddressRequest(
                label, address, recipientName, recipientPhone,
                latitude, longitude, note
        );

        addressApi.updateAddress(id, request).enqueue(new Callback<ApiResponse<Address>>() {
            @Override
            public void onResponse(Call<ApiResponse<Address>> call,
                                   Response<ApiResponse<Address>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Address> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(apiResponse.getData()));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể cập nhật địa chỉ", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Address>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    /**
     * Xóa địa chỉ
     */
    public LiveData<Resource<Void>> deleteAddress(int id) {
        MutableLiveData<Resource<Void>> result = new MutableLiveData<>();
        result.setValue(Resource.loading(null));

        addressApi.deleteAddress(id).enqueue(new Callback<ApiResponse<Void>>() {
            @Override
            public void onResponse(Call<ApiResponse<Void>> call,
                                   Response<ApiResponse<Void>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    ApiResponse<Void> apiResponse = response.body();
                    if (apiResponse.isSuccess()) {
                        result.setValue(Resource.success(null));
                    } else {
                        result.setValue(Resource.error(apiResponse.getMessage(), null));
                    }
                } else {
                    result.setValue(Resource.error("Không thể xóa địa chỉ", null));
                }
            }

            @Override
            public void onFailure(Call<ApiResponse<Void>> call, Throwable t) {
                result.setValue(Resource.error(t.getMessage(), null));
            }
        });

        return result;
    }

    // Request DTOs
    public static class CreateAddressRequest {
        private String label;
        private String address;
        private String recipientName;
        private String recipientPhone;
        private double latitude;
        private double longitude;
        private String note;
        private boolean isDefault;

        public CreateAddressRequest(String label, String address, String recipientName,
                                    String recipientPhone, double latitude, double longitude,
                                    String note, boolean isDefault) {
            this.label = label;
            this.address = address;
            this.recipientName = recipientName;
            this.recipientPhone = recipientPhone;
            this.latitude = latitude;
            this.longitude = longitude;
            this.note = note;
            this.isDefault = isDefault;
        }

        // Getters
        public String getLabel() { return label; }
        public String getAddress() { return address; }
        public String getRecipientName() { return recipientName; }
        public String getRecipientPhone() { return recipientPhone; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public String getNote() { return note; }
        public boolean isDefault() { return isDefault; }
    }

    public static class UpdateAddressRequest {
        private String label;
        private String address;
        private String recipientName;
        private String recipientPhone;
        private double latitude;
        private double longitude;
        private String note;

        public UpdateAddressRequest(String label, String address, String recipientName,
                                    String recipientPhone, double latitude, double longitude,
                                    String note) {
            this.label = label;
            this.address = address;
            this.recipientName = recipientName;
            this.recipientPhone = recipientPhone;
            this.latitude = latitude;
            this.longitude = longitude;
            this.note = note;
        }

        // Getters
        public String getLabel() { return label; }
        public String getAddress() { return address; }
        public String getRecipientName() { return recipientName; }
        public String getRecipientPhone() { return recipientPhone; }
        public double getLatitude() { return latitude; }
        public double getLongitude() { return longitude; }
        public String getNote() { return note; }
    }
}