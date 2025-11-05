// Tệp: app/src/main/java/com/example/funfood/presentation/main/favorite/FavoriteViewModel.java

package com.example.funfood.presentation.main.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.Transformations;

import com.example.funfood.data.repository.FavoriteRepository;
import com.example.funfood.domain.model.Favorite;
import com.example.funfood.util.Resource;
import com.example.funfood.util.SingleEvent;

import java.util.List;

public class FavoriteViewModel extends ViewModel {

    private final FavoriteRepository repository;

    private final MutableLiveData<Boolean> _refreshTrigger = new MutableLiveData<>();

    // LiveData chính
    public final LiveData<Resource<List<Favorite>>> favorites;

    private final MutableLiveData<SingleEvent<Resource<Object>>> _removeFavoriteEvent = new MutableLiveData<>();
    public final LiveData<SingleEvent<Resource<Object>>> removeFavoriteEvent = _removeFavoriteEvent;

    // THÊM MỚI: LiveData cho add favorite
    private final MutableLiveData<SingleEvent<Resource<Object>>> _addFavoriteEvent = new MutableLiveData<>();
    public final LiveData<SingleEvent<Resource<Object>>> addFavoriteEvent = _addFavoriteEvent;

    public FavoriteViewModel(FavoriteRepository repository) {
        this.repository = repository;

        favorites = Transformations.switchMap(_refreshTrigger, trigger ->
                repository.getFavorites(1, 50)
        );

        fetchFavorites();
    }

    public void fetchFavorites() {
        _refreshTrigger.setValue(true);
    }

    public void removeFavorite(Favorite favorite) {
        if (favorite == null || favorite.getRestaurant() == null) return;

        int restaurantId = favorite.getRestaurant().getId();

        repository.toggleFavorite(restaurantId, resource -> {
            _removeFavoriteEvent.postValue(new SingleEvent<>(resource));
            if (resource.getStatus() == Resource.Status.SUCCESS) {
                fetchFavorites();
            }
        });
    }

    // THÊM MỚI: Method để thêm vào yêu thích
    public void addFavorite(int restaurantId) {
        repository.toggleFavorite(restaurantId, resource -> {
            _addFavoriteEvent.postValue(new SingleEvent<>(resource));
            if (resource.getStatus() == Resource.Status.SUCCESS) {
                fetchFavorites();
            }
        });
    }
}