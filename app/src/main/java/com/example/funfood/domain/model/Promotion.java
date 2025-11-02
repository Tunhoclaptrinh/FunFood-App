package com.example.funfood.domain.model;

public class Promotion {
    private int id;
    private String code;
    private String description;
    private String discountType;
    private int discountValue;
    private int minOrderValue;
    private int maxDiscount;
    private String validFrom;
    private String validTo;
    private boolean isActive;

    public Promotion() {}

    public int getId() { return id; }
    public void setId(int id) { this.id = id; }

    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getDiscountType() { return discountType; }
    public void setDiscountType(String discountType) { this.discountType = discountType; }

    public int getDiscountValue() { return discountValue; }
    public void setDiscountValue(int discountValue) { this.discountValue = discountValue; }

    public int getMinOrderValue() { return minOrderValue; }
    public void setMinOrderValue(int minOrderValue) { this.minOrderValue = minOrderValue; }

    public int getMaxDiscount() { return maxDiscount; }
    public void setMaxDiscount(int maxDiscount) { this.maxDiscount = maxDiscount; }

    public String getValidFrom() { return validFrom; }
    public void setValidFrom(String validFrom) { this.validFrom = validFrom; }

    public String getValidTo() { return validTo; }
    public void setValidTo(String validTo) { this.validTo = validTo; }

    public boolean isActive() { return isActive; }
    public void setActive(boolean active) { isActive = active; }
}