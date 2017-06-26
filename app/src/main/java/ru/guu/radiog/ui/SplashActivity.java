package ru.guu.radiog.ui;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

import ru.guu.radiog.RadioPreferenceManager;
import ru.guu.radiog.ui.login.LoginActivity;

/**
 * start screen with switching logic
 * Created by dmitry on 21.06.17.
 */

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onResume() {
        super.onResume();

        RadioPreferenceManager preferenceManager = RadioPreferenceManager.newInstance(this);
        Intent intent;
        if (preferenceManager.getSingedIn()) {
            intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        } else {
            intent = new Intent(this, LoginActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            finish();
        }
    }
}
