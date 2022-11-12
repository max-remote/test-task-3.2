package com.maks.testtask3_2.ui;

import android.os.Bundle;
import android.os.Handler;
import android.view.Gravity;
import android.view.inputmethod.EditorInfo;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import com.maks.testtask3_2.R;
import com.maks.testtask3_2.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

public class MainActivity extends AppCompatActivity {

    private final Picture picture = new Picture();
    private final Handler handler = new Handler();
    private ActivityMainBinding binding;
    private String url;
    private ImageView image;
    private MainViewModel mainViewModel;
    static final long DELAY_TIME = 2000L;

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
         * Загружает картинку и делает проверку try catch на ввод(null)
         */
        private void loadPicture() {
            String messageWhenNull = MainActivity.this.getString(R.string.toast_message_null);
            String messageWhenIncorrect = MainActivity.this.getString(R.string.toast_message_incorrect);

            url = binding.inputEditText.getText().toString();
            image = binding.pictureImageView;
            try {
                Picasso.get().load(url).into(image);
                checkRightLink(messageWhenIncorrect);
            } catch (Exception e) {
                doToast(messageWhenNull);
            }
        }

        /**
         * Этот метод проверят загрузилась ли картинка в imageView или нет:
         * если в течении DELAY_TIME загрузка не произошла (по причине неправильной ссылки, опечатки,
         * неверного ввода и тд), выбросится toastMessage.
         *
         * @param toastMessage сообщение, которое выбросится, если картинка не загрузится
         */
        private void checkRightLink(String toastMessage) {
            handler.postDelayed(() -> {
                if (image.getDrawable() == null) {
                    doToast(toastMessage);
                } else {
                    Picasso.get().load(url).into(image);
                }
            }, DELAY_TIME);
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
