package com.framgia.forder.screen.login;

import android.content.DialogInterface;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v7.app.AppCompatDelegate;
import com.framgia.forder.R;
import com.framgia.forder.data.event.NetWorkStateEvent;
import com.framgia.forder.data.source.UserRepository;
import com.framgia.forder.data.source.local.UserLocalDataSource;
import com.framgia.forder.data.source.local.sharedprf.SharedPrefsApi;
import com.framgia.forder.data.source.local.sharedprf.SharedPrefsImpl;
import com.framgia.forder.data.source.remote.UserRemoteDataSource;
import com.framgia.forder.data.source.remote.api.ConnectivityReceiver;
import com.framgia.forder.data.source.remote.api.service.FOrderServiceClient;
import com.framgia.forder.databinding.ActivityLoginBinding;
import com.framgia.forder.screen.BaseActivity;
import com.framgia.forder.utils.navigator.Navigator;
import com.framgia.forder.widgets.dialog.DialogManager;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import static com.framgia.forder.screen.splash.SplashActivity.PARAMS;

/**
 * Login Screen.
 */
public class LoginActivity extends BaseActivity {

    private LoginContract.ViewModel mViewModel;
    private DialogManager mDialogManager;
    private boolean mIsConnected;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mDialogManager = new DialogManager(this);
        String params = getIntent().getExtras().getString(PARAMS);
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);
        SharedPrefsApi prefsApi = new SharedPrefsImpl(getApplicationContext());
        Navigator navigator = new Navigator(this);
        DialogManager dialogManager = new DialogManager(this);
        mViewModel = new LoginViewModel(getApplicationContext(), getApplication(), navigator,
                dialogManager, params);
        UserRepository userRepository =
                new UserRepository(new UserRemoteDataSource(FOrderServiceClient.getInstance()),
                        new UserLocalDataSource(prefsApi));
        LoginContract.Presenter presenter = new LoginPresenter(mViewModel, userRepository);
        mViewModel.setPresenter(presenter);
        ActivityLoginBinding binding =
                DataBindingUtil.setContentView(this, R.layout.activity_login);
        binding.setViewModel((LoginViewModel) mViewModel);
    }

    @Override
    protected void onStart() {
        super.onStart();
        mViewModel.onStart();
    }

    @Override
    protected void onStop() {
        mViewModel.onStop();
        super.onStop();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onMessageEvent(NetWorkStateEvent event) {
        checkConnection(event.isConnected());
    }

    private void checkConnection(boolean isConnected) {
        if (!isConnected) {
            mDialogManager.dialogwithNoTitleOneButton(R.string.sorry_not_connect_to_internet,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            refreshConnection();
                        }
                    });
            mDialogManager.show();
        }
    }

    private void refreshConnection() {
        mIsConnected = ConnectivityReceiver.isConnected(this);
        if (!mIsConnected) {
            mDialogManager.dialogwithNoTitleOneButton(R.string.sorry_not_connect_to_internet,
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            refreshConnection();
                        }
                    });
            mDialogManager.show();
        } else {
            mDialogManager.dismiss();
        }
    }
}
