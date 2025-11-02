package com.example.funfood.presentation.main.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.funfood.domain.model.Category;
import com.example.funfood.domain.model.Promotion;
import com.example.funfood.domain.model.Restaurant;

import java.util.Arrays;
import java.util.List;

public class HomeViewModel extends ViewModel {

    // Giả sử bạn đã inject repositories
    // private final CategoryRepository categoryRepository;
    // private final PromotionRepository promotionRepository;
    // private final RestaurantRepository restaurantRepository;

    private final MutableLiveData<List<Category>> _categories = new MutableLiveData<>();
    public final LiveData<List<Category>> categories = _categories;

    private final MutableLiveData<List<Promotion>> _promotions = new MutableLiveData<>();
    public final LiveData<List<Promotion>> promotions = _promotions;

    private final MutableLiveData<List<Restaurant>> _restaurants = new MutableLiveData<>();
    public final LiveData<List<Restaurant>> restaurants = _restaurants;

    // public HomeViewModel(CategoryRepository categoryRepository, ...) {
    //     this.categoryRepository = categoryRepository;
    //     ...
    // }

    public HomeViewModel() {
        // Constructor (sẽ được thay thế bằng DI)
        fetchData();
    }

    public void fetchData() {
        fetchCategories();
        fetchPromotions();
        fetchRestaurants();
    }

    private void fetchCategories() {
        // TODO: Gọi từ Repository
        // Tạm thời dùng dữ liệu giả (mock data)
        List<Category> mockCategories = Arrays.asList(
                new Category("1", "Cơm", "url_to_image"),
                new Category("2", "Phở", "url_to_image"),
                new Category("3", "Bún", "url_to_image"),
                new Category("4", "Đồ uống", "url_to_image"),
                new Category("5", "Tráng miệng", "url_to_image")
        );
        _categories.setValue(mockCategories);
    }

    private void fetchPromotions() {
        // TODO: Gọi từ Repository
        List<Promotion> mockPromotions = Arrays.asList(
                new Promotion("p1", "url_promo_1", "Khuyến mãi 1"),
                new Promotion("p2", "url_promo_2", "Khuyến mãi 2")
        );
        _promotions.setValue(mockPromotions);
    }

    private void fetchRestaurants() {
        // TODO: Gọi từ Repository
        List<Restaurant> mockRestaurants = Arrays.asList(
                new Restaurant("r1", "Nhà hàng Phở Thìn", "13 Lò Đúc, Hai Bà Trưng", 4.5, "url_img_1", 10.0),
                new Restaurant("r2", "Bún chả Hàng Mành", "1 Hàng Mành, Hoàn Kiếm", 4.8, "url_img_2", 5.0),
                new Restaurant("r3", "Cơm tấm Cali", "234 Nguyễn Thị Minh Khai, Q3", 4.2, "url_img_3", 2.5)
        );
        _restaurants.setValue(mockRestaurants);
    }

    // Giả lập models (Bạn nên dùng file model thật từ domain.model)
    // Các class này nên nằm ở file riêng trong domain/model/
    // public class Category {
    //     String id, name, imageUrl;
    //     public Category(String id, String name, String imageUrl) { /*...*/ }
    // }
    // public class Promotion {
    //     String id, imageUrl, description;
    //     public Promotion(String id, String imageUrl, String description) { /*...*/ }
    // }
    // public class Restaurant {
    //     String id, name, address;
    //     double rating;
    //     String imageUrl;
    //     double distance; // Thêm khoảng cách
    //     public Restaurant(String id, String name, String address, double rating, String imageUrl, double distance) { /*...*/ }
    // }
}