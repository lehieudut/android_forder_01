package com.framgia.forder.screen.shopmanagement;

import com.framgia.forder.data.model.ShopManagement;
import com.framgia.forder.data.model.User;
import com.framgia.forder.data.source.ShopRepository;
import com.framgia.forder.data.source.UserRepository;
import com.framgia.forder.data.source.remote.api.error.BaseException;
import com.framgia.forder.data.source.remote.api.error.SafetyError;
import com.framgia.forder.data.source.remote.api.response.BaseResponse;
import java.util.List;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Listens to user actions from the UI ({@link ShopManagementFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class ShopManagementPresenter implements ShopManagementContract.Presenter {
    private static final String TAG = ShopManagementPresenter.class.getName();

    private final ShopManagementContract.ViewModel mViewModel;
    private final CompositeSubscription mCompositeSubscription;
    private final ShopRepository mShopRepository;
    private final User user;

    ShopManagementPresenter(ShopManagementContract.ViewModel viewModel,
            UserRepository userRepository, ShopRepository shopRepository) {
        mViewModel = viewModel;
        mShopRepository = shopRepository;
        mCompositeSubscription = new CompositeSubscription();
        user = userRepository.getUser();
        getListShopManagement();
    }

    @Override
    public void onStart() {
    }

    @Override
    public void onStop() {
        mCompositeSubscription.clear();
    }

    @Override
    public void getListShopManagement() {
        if (user == null) {
            return;
        }
        Subscription subscription = mShopRepository.getListShopManagement(user.getId())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<ShopManagement>>() {
                    @Override
                    public void call(List<ShopManagement> shopManagements) {
                        mViewModel.onGetListShopManagementSuccess(shopManagements);
                    }
                }, new SafetyError() {
                    @Override
                    public void onSafetyError(BaseException error) {
                        mViewModel.onShowMessageError(error);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void requestChangeStatusShop(int shopId, String status) {
        Subscription subscription =
                mShopRepository.requestChangeStatusShopManagement(shopId, status)
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Action1<BaseResponse>() {
                            @Override
                            public void call(BaseResponse baseResponse) {
                                mViewModel.onChangeStatusShopSuccess();
                            }
                        }, new SafetyError() {
                            @Override
                            public void onSafetyError(BaseException error) {
                                mViewModel.onShowMessageError(error);
                            }
                        });
        mCompositeSubscription.add(subscription);
    }
}
