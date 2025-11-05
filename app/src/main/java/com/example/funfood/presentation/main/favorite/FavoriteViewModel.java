package com.example.funfood.presentation.main.favorite;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
// IMPORT QUAN TRỌNG:
import androidx.lifecycle.Transformations;
// KHÔNG import androidx.lifecycle.switchMap;

import com.example.funfood.data.repository.FavoriteRepository;
import com.example.funfood.domain.model.Favorite;
import com.example.funfood.util.Resource;
import com.example.funfood.util.SingleEvent;

import java.util.List;

public class FavoriteViewModel extends ViewModel {

    private final FavoriteRepository repository;

    // LiveData trigger để làm mới danh sách
    private final MutableLiveData<Boolean> _refreshTrigger = new MutableLiveData<>();

    // LiveData chính chứa danh sách yêu thích
    public final LiveData<Resource<List<Favorite>>> favorites;

    // LiveData cho sự kiện xóa (dùng SingleEvent để tránh lặp)
    private final MutableLiveData<SingleEvent<Resource<Object>>> _removeFavoriteEvent = new MutableLiveData<>();
    public final LiveData<SingleEvent<Resource<Object>>> removeFavoriteEvent = _removeFavoriteEvent;

    public FavoriteViewModel(FavoriteRepository repository) {
        this.repository = repository;

        // --- ĐÂY LÀ CHỖ SỬA LỖI ---
        // Chúng ta sử dụng Transformations.switchMap()
        favorites = Transformations.switchMap(_refreshTrigger, trigger ->
                // Tải trang 1, giới hạn 50 (có thể thay đổi)
                repository.getFavorites(1, 50)
        );
        // --- HẾT CHỖ SỬA LỖI ---

        // Kích hoạt tải dữ liệu lần đầu
        fetchFavorites();
    }

    /**
     * Kích hoạt việc tải lại danh sách
     */
    public void fetchFavorites() {
        _refreshTrigger.setValue(true);
    }

    /**
     * Xóa một mục khỏi danh sách yêu thích
     */
    public void removeFavorite(Favorite favorite) {
        if (favorite == null || favorite.getRestaurant() == null) return;

        int restaurantId = favorite.getRestaurant().getId();

        // Gọi repository với callback
        repository.toggleFavorite(restaurantId, resource -> {
            // Gửi kết quả về Fragment qua SingleEvent
            _removeFavoriteEvent.postValue(new SingleEvent<>(resource));

            // Nếu xóa thành công, tự động tải lại danh sách
            if (resource.getStatus() == Resource.Status.SUCCESS) {
                fetchFavorites();
            }
        });
    }
}