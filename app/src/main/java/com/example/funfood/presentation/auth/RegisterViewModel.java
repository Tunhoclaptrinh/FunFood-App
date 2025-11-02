package com.example.funfood.presentation.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.funfood.data.repository.AuthRepository;
import com.example.funfood.domain.model.User;
import com.example.funfood.util.Resource;

public class RegisterViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MediatorLiveData<Resource<User>> registerResult = new MediatorLiveData<>();

    public RegisterViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public void register(String email, String password, String name, String phone, String address) {
        LiveData<Resource<User>> source = authRepository.register(email, password, name, phone, address);

        registerResult.addSource(source, resource -> {
            registerResult.setValue(resource);
            if (resource.getStatus() != Resource.Status.LOADING) {
                registerResult.removeSource(source);
            }
        });
    }

    public LiveData<Resource<User>> getRegisterResult() {
        return registerResult;
    }
}