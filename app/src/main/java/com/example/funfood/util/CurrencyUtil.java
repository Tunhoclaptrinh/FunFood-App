package com.example.funfood.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Locale;

public class CurrencyUtil {

    private static final Locale VIETNAM_LOCALE = new Locale("vi", "VN");
    private static final NumberFormat currencyFormat = NumberFormat.getInstance(VIETNAM_LOCALE);

    static {
        currencyFormat.setMaximumFractionDigits(0);
        currencyFormat.setMinimumFractionDigits(0);
    }

    /**
     * Format currency (VND)
     * Example: 50000 -> "50,000 đ"
     */
    public static String formatCurrency(int amount) {
        return currencyFormat.format(amount) + " đ";
    }

    public static String formatCurrency(long amount) {
        return currencyFormat.format(amount) + " đ";
    }

    public static String formatCurrency(double amount) {
        return currencyFormat.format(amount) + " đ";
    }

    /**
     * Format currency without symbol
     * Example: 50000 -> "50,000"
     */
    public static String formatNumber(int number) {
        return currencyFormat.format(number);
    }

    public static String formatNumber(long number) {
        return currencyFormat.format(number);
    }

    /**
     * Format discount
     * Example: 10 -> "10%"
     */
    public static String formatDiscount(int discount) {
        return discount + "%";
    }

    /**
     * Calculate discounted price
     */
    public static int calculateDiscountedPrice(int originalPrice, int discountPercent) {
        return originalPrice - (originalPrice * discountPercent / 100);
    }

    /**
     * Format price range
     * Example: (50000, 100000) -> "50,000đ - 100,000đ"
     */
    public static String formatPriceRange(int minPrice, int maxPrice) {
        return formatCurrency(minPrice) + " - " + formatCurrency(maxPrice);
    }
}