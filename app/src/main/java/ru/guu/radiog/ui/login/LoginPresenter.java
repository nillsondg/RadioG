package ru.guu.radiog.ui.login;

import android.support.annotation.NonNull;
import android.util.Log;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import ru.guu.radiog.network.GuuApiService;

import static com.google.gson.internal.$Gson$Preconditions.checkNotNull;

/**
 * Created by dmitry on 22.06.17.
 */

public class LoginPresenter implements LoginContract.LoginPresenter {
    private final String LOG_TAG = LoginPresenter.class.getSimpleName();

    private final LoginContract.LoginView mView;
    private final GuuApiService mApiService;

    LoginPresenter(@NonNull GuuApiService apiService,
                   @NonNull LoginContract.LoginView view) {
        mApiService = checkNotNull(apiService);
        mView = checkNotNull(view);
        mView.setPresenter(this);
    }


    public void startAuth(String email, String password) {
        mApiService.authGuu(email, password).subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(result -> {
            if (!result.isOk()) {
                mView.showIncorrectPassword();
                return;
            }
            mView.onSuccess(result.getData());
        }, this::onError, () -> mView.showProgress(false));
    }

    private void onError(Throwable error) {
        Log.e(LOG_TAG, "" + error.getMessage());
        mView.showError();
    }
}
