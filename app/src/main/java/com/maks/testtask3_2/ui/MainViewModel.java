package com.maks.testtask3_2.ui;

import android.graphics.drawable.Drawable;
import androidx.lifecycle.ViewModel;

public class MainViewModel extends ViewModel {

    private Drawable image;

    public Drawable getImage() {
        return image;
    }

    public void setImage(Drawable image) {
        this.image = image;
    }
}
