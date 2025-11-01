package com.example.funfood.presentation.auth;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;

import com.example.funfood.data.repository.AuthRepository;
import com.example.funfood.domain.model.User;
import com.example.funfood.util.Resource;

public class LoginViewModel extends AndroidViewModel {

    private final AuthRepository authRepository;
    private final MediatorLiveData<Resource<User>> loginResult = new MediatorLiveData<>();

    public LoginViewModel(@NonNull Application application) {
        super(application);
        authRepository = new AuthRepository(application);
    }

    public void login(String email, String password) {
        LiveData<Resource<User>> source = authRepository.login(email, password);

        loginResult.addSource(source, resource -> {
            loginResult.setValue(resource);
            if (resource.getStatus() != Resource.Status.LOADING) {
                loginResult.removeSource(source);
            }
        });
    }

    public LiveData<Resource<User>> getLoginResult() {
        return loginResult;
    }

    public boolean isLoggedIn() {
        return authRepository.isLoggedIn();
    }
}