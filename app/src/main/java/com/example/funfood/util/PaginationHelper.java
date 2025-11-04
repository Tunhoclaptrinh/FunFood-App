package com.example.funfood.util;

import android.content.Context;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.funfood.R;

public class PaginationHelper {

    /**
     * Listener for page changes
     */
    public interface OnPageChangeListener {
        void onPageChanged(int page);
    }

    /**
     * Calculate page info text
     */
    public static String getPageInfo(int currentPage, int totalPages, int totalItems) {
        if (totalPages == 0) {
            return "Không có dữ liệu";
        }
        return String.format("Trang %d / %d (%d kết quả)", currentPage, totalPages, totalItems);
    }

    /**
     * Calculate page info with range
     */
    public static String getPageInfoWithRange(int currentPage, int itemsPerPage, int totalItems) {
        if (totalItems == 0) {
            return "Không có dữ liệu";
        }

        int start = (currentPage - 1) * itemsPerPage + 1;
        int end = Math.min(currentPage * itemsPerPage, totalItems);

        return String.format("Hiển thị %d-%d trên tổng %d kết quả", start, end, totalItems);
    }

    /**
     * Generate page numbers to display
     * Shows: [1] ... [4] [5] [6] ... [10]
     */
    public static int[] getPageNumbersToDisplay(int currentPage, int totalPages, int maxVisible) {
        if (totalPages <= maxVisible) {
            // Show all pages
            int[] pages = new int[totalPages];
            for (int i = 0; i < totalPages; i++) {
                pages[i] = i + 1;
            }
            return pages;
        }

        // Calculate range around current page
        int halfVisible = maxVisible / 2;
        int startPage = Math.max(1, currentPage - halfVisible);
        int endPage = Math.min(totalPages, startPage + maxVisible - 1);

        // Adjust if we're near the end
        if (endPage - startPage < maxVisible - 1) {
            startPage = Math.max(1, endPage - maxVisible + 1);
        }

        // Create array with page numbers and ellipsis markers (-1)
        java.util.List<Integer> pageList = new java.util.ArrayList<>();

        // Always show first page
        if (startPage > 1) {
            pageList.add(1);
            if (startPage > 2) {
                pageList.add(-1); // Ellipsis
            }
        }

        // Show middle pages
        for (int i = startPage; i <= endPage; i++) {
            pageList.add(i);
        }

        // Always show last page
        if (endPage < totalPages) {
            if (endPage < totalPages - 1) {
                pageList.add(-1); // Ellipsis
            }
            pageList.add(totalPages);
        }

        // Convert to array
        int[] pages = new int[pageList.size()];
        for (int i = 0; i < pageList.size(); i++) {
            pages[i] = pageList.get(i);
        }

        return pages;
    }

    /**
     * Create page number views dynamically
     */
    public static void setupPageNumbers(Context context, LinearLayout container,
                                        int currentPage, int totalPages, int maxVisible,
                                        OnPageChangeListener listener) {
        container.removeAllViews();

        if (totalPages <= 0) return;

        int[] pages = getPageNumbersToDisplay(currentPage, totalPages, maxVisible);

        for (int pageNum : pages) {
            if (pageNum == -1) {
                // Add ellipsis
                TextView ellipsis = createEllipsis(context);
                container.addView(ellipsis);
            } else {
                // Add page number button
                TextView pageButton = createPageButton(context, pageNum, pageNum == currentPage);
                pageButton.setOnClickListener(v -> {
                    if (listener != null) {
                        listener.onPageChanged(pageNum);
                    }
                });
                container.addView(pageButton);
            }
        }
    }

    /**
     * Create page number button
     */
    private static TextView createPageButton(Context context, int pageNum, boolean isSelected) {
        TextView textView = new TextView(context);

        int size = (int) (40 * context.getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(4, 0, 4, 0);
        textView.setLayoutParams(params);

        textView.setText(String.valueOf(pageNum));
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(14);
        textView.setClickable(true);
        textView.setFocusable(true);

        if (isSelected) {
            textView.setBackgroundResource(R.drawable.bg_page_number_selected);
            textView.setTextColor(ContextCompat.getColor(context, R.color.white));
        } else {
            textView.setBackgroundResource(R.drawable.bg_page_number_unselected);
            textView.setTextColor(ContextCompat.getColor(context, R.color.text_primary));
        }

        return textView;
    }

    /**
     * Create ellipsis view
     */
    private static TextView createEllipsis(Context context) {
        TextView textView = new TextView(context);

        int size = (int) (40 * context.getResources().getDisplayMetrics().density);
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(size, size);
        params.setMargins(4, 0, 4, 0);
        textView.setLayoutParams(params);

        textView.setText("...");
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(14);
        textView.setTextColor(ContextCompat.getColor(context, R.color.text_hint));

        return textView;
    }

    /**
     * Scroll to top smoothly
     */
    public static void scrollToTop(View view) {
        if (view != null) {
            view.post(() -> {
                if (view.getParent() instanceof androidx.core.widget.NestedScrollView) {
                    ((androidx.core.widget.NestedScrollView) view.getParent())
                            .smoothScrollTo(0, 0);
                } else if (view instanceof androidx.recyclerview.widget.RecyclerView) {
                    ((androidx.recyclerview.widget.RecyclerView) view)
                            .smoothScrollToPosition(0);
                }
            });
        }
    }
}