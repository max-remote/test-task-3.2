package com.maks.testtask3_2.ui;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.maks.testtask3_2.R;
import com.maks.testtask3_2.databinding.ActivityMainBinding;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    private final Picture picture = new Picture();
    private ActivityMainBinding binding;
    private String url;
    private ImageView image;
    private Bitmap bitmap;
    private MainViewModel mainViewModel;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        mainViewModel = new ViewModelProvider(this).get(MainViewModel.class);
        if (mainViewModel.getImage() != null) {
            binding.pictureImageView.setImageDrawable(mainViewModel.getImage());
        }

        picture.loadPictureBySearchButton();
        picture.loadPictureByVirtualEnterKey();
    }

    private class Picture {

        /**
         * загрузка изображения по нажатию иконки поиска в конце строки поиска
         */
        private void loadPictureBySearchButton() {
            binding.inputLayout.setEndIconOnClickListener(v -> loadPicture());
        }

        /**
         * загрузка изображения по нажатию клавиши enter на виртуальной клавиатуре
         */
        private void loadPictureByVirtualEnterKey() {
            binding.inputEditText.setOnEditorActionListener((v, actionId, event) -> {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    loadPicture();
                    return true;
                }
                return false;
            });
        }

        /**
         * Загружает картинку через работу с Bitmap. Загрузка по URL во вспомогательном потоке,
         * отрисовка в главном
         */
        private void loadPicture() {
            String messageWhenNull = MainActivity.this.getString(R.string.toast_message);
            url = binding.inputEditText.getText().toString();
            image = binding.pictureImageView;

            new Thread(() -> {
                try {
                    bitmap = BitmapFactory.decodeStream(new URL(url).openConnection().getInputStream());
                    runOnUiThread(() -> image.setImageBitmap(bitmap));
                } catch (Exception e) {
                    runOnUiThread(() -> doToast(messageWhenNull));
                }
            }).start();
        }

        private void doToast(String message) {
            Toast toast = Toast.makeText(MainActivity.this, message, Toast.LENGTH_LONG);
            toast.setGravity(Gravity.CENTER_HORIZONTAL, 0, 0);
            toast.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mainViewModel.setImage(binding.pictureImageView.getDrawable());
    }
}
