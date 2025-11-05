package com.example.funfood.domain.model;

import com.google.gson.annotations.SerializedName;

public class OrderItem {

    // Các trường này dựa trên API (POST /api/orders)
    @SerializedName("productId")
    private int productId;

    @SerializedName("quantity")
    private int quantity;

    // API (GET /api/orders/:id) sẽ trả về chi tiết sản phẩm
    // bằng cách "expand" (mở rộng)
    @SerializedName("product")
    private Product product;

    // --- Getters ---

    public int getProductId() {
        // Nếu object product tồn tại, lấy id từ nó
        if (product != null) {
            return product.getId();
        }
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }

    /**
     * Phương thức này dùng để sửa lỗi "cannot find symbol".
     * Nó sẽ lấy tên từ object "product" lồng bên trong.
     */
    public String getProductName() {
        if (product != null) {
            return product.getName();
        }
        // Trả về chuỗi rỗng nếu không có thông tin sản phẩm
        return "Sản phẩm không xác định";
    }

    /**
     * Lấy giá của sản phẩm tại thời điểm đặt hàng (nếu API hỗ trợ)
     * Hoặc lấy giá hiện tại của sản phẩm.
     */
    public double getPrice() {
        if (product != null) {
            // Trả về giá đã giảm (nếu có)
            return product.getDiscountedPrice();
        }
        return 0;
    }

    public String getImage() {
        if (product != null) {
            return product.getImage();
        }
        return null;
    }

    public Product getProduct() {
        return product;
    }
}