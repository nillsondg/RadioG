package ru.guu.radiog.ui.login;

import ru.guu.radiog.network.model.UserData;

/**
 * Created by dmitry on 22.06.17.
 */

class LoginContract {

    interface LoginView {
        void setPresenter(LoginPresenter presenter);

        void showIncorrectPassword();

        void showError();

        void showProgress(boolean progress);

        void onSuccess(UserData userData);
    }

    interface LoginPresenter {
        void startAuth(String email, String password);
    }

}
