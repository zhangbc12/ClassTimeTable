package cs.hku.classtimetable;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.widget.ImageView;
import android.widget.TextView;

public class SplashActivity extends AppCompatActivity {
    private ImageView img_logo, img_bottom;
    private TextView tv_name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        Utils.blackIconStatusBar(SplashActivity.this, R.color.light_background);

        tv_name = findViewById(R.id.tv_name);
        img_logo = findViewById(R.id.img_logo);
        img_bottom = findViewById(R.id.img_bottom);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, MainActivity.class);
                ActivityOptions  options = ActivityOptions.makeSceneTransitionAnimation(SplashActivity.this,
                        Pair.create(img_logo, "logo"),
                        Pair.create(img_bottom, "img_tree"),
                        Pair.create(tv_name, "logo_text"));
                startActivity(intent, options.toBundle());
            }
        }, 2000);

    }
}