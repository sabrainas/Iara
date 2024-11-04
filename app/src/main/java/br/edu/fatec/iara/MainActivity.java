package br.edu.fatec.iara;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {



    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ImageView splashImage = findViewById(R.id.splash_image);
        splashImage.setAlpha(0f);
        splashImage.animate().alpha(1f).setDuration(2000);

        ObjectAnimator swingAnimator = ObjectAnimator.ofFloat(splashImage, "rotation", -10f, 10f);
        swingAnimator.setDuration(1000);
        swingAnimator.setRepeatCount(ValueAnimator.INFINITE);
        swingAnimator.setRepeatMode(ValueAnimator.REVERSE);
        swingAnimator.start();

        new Handler().postDelayed(() -> {
            Intent intent = new Intent(MainActivity.this, Home.class);
            startActivity(intent);
            finish();
        }, 3000);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }
}