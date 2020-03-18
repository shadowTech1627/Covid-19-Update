package com.shadowtech.covid_update.utility;

import android.animation.ValueAnimator;
import android.text.TextUtils;

import androidx.databinding.BindingAdapter;

public class DataBindingUtility {

    private static String TAG = DataBindingUtility.class.getSimpleName();

    @BindingAdapter({"data"})
    public static void setSummaryData(androidx.appcompat.widget.AppCompatTextView textView, int data) {
        try {
            if (data == 0) {
                textView.setText("--");
            } else {
                int initValue = 0;
                String initialValue = textView.getText().toString();
                if (!TextUtils.isEmpty(initialValue) && !initialValue.equalsIgnoreCase("--")) {
                    initValue = Integer.parseInt(initialValue);
                }
                ValueAnimator animator = ValueAnimator.ofInt(initValue, data);
                animator.setDuration(2000);
                animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                    public void onAnimationUpdate(ValueAnimator animation) {
                        textView.setText(animation.getAnimatedValue().toString());
                    }
                });
                animator.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
