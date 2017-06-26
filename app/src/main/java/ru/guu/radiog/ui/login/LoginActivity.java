package ru.guu.radiog.ui.login;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import ru.guu.radiog.R;
import ru.guu.radiog.network.ApiFactory;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        LoginFragment fragment = LoginFragment.newInstance();
        fragment.setPresenter(new LoginPresenter(ApiFactory.getGuuService(), fragment));
        getSupportFragmentManager().beginTransaction().add(R.id.container, fragment).commit();
    }
}

