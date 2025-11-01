package com.example.funfood.presentation.base;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class BaseViewModel extends ViewModel {

    protected final MutableLiveData<Boolean> isLoading = new MutableLiveData<>();
    protected final MutableLiveData<String> errorMessage = new MutableLiveData<>();
    protected final MutableLiveData<String> successMessage = new MutableLiveData<>();

    public BaseViewModel() {
        isLoading.setValue(false);
    }

    /**
     * Loading state
     */
    public MutableLiveData<Boolean> getIsLoading() {
        return isLoading;
    }

    protected void setLoading(boolean loading) {
        isLoading.setValue(loading);
    }

    /**
     * Error handling
     */
    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    protected void setError(String message) {
        errorMessage.setValue(message);
    }

    /**
     * Success handling
     */
    public MutableLiveData<String> getSuccessMessage() {
        return successMessage;
    }

    protected void setSuccess(String message) {
        successMessage.setValue(message);
    }

    /**
     * Clear messages
     */
    public void clearMessages() {
        errorMessage.setValue(null);
        successMessage.setValue(null);
    }
}