package com.framgia.forder.screen.shopinfo.productshopinfo;

import com.framgia.forder.data.model.Product;
import com.framgia.forder.data.source.ProductRepository;
import com.framgia.forder.data.source.remote.api.error.BaseException;
import com.framgia.forder.data.source.remote.api.error.SafetyError;
import com.framgia.forder.data.source.remote.api.response.BaseResponse;
import java.util.List;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import rx.subscriptions.CompositeSubscription;

/**
 * Listens to user actions from the UI ({@link ProductShopInfoFragment}), retrieves the data and
 * updates
 * the UI as required.
 */
final class ProductShopInfoPresenter implements ProductShopInfoContract.Presenter {
    private static final String TAG = ProductShopInfoPresenter.class.getName();

    private final ProductShopInfoContract.ViewModel mViewModel;
    private final CompositeSubscription mCompositeSubscription;
    private final ProductRepository mProductRepository;

    ProductShopInfoPresenter(ProductShopInfoContract.ViewModel viewModel,
            ProductRepository productRepository) {
        mViewModel = viewModel;
        mProductRepository = productRepository;
        mCompositeSubscription = new CompositeSubscription();
    }

    @Override
    public void onStart() {
        mProductRepository.openTransaction();
    }

    @Override
    public void onStop() {
        mCompositeSubscription.clear();
        mProductRepository.closeTransaction();
    }

    @Override
    public void getListAllProductShopInformation(int shopId) {
        Subscription subscription = mProductRepository.getListProductInShopInformation(shopId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewModel.onShowProgressBar();
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mViewModel.onHideProgressBar();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<List<Product>>() {
                    @Override
                    public void call(List<Product> products) {
                        mViewModel.onGetListAllProductShopInformationSuccess(products);
                    }
                }, new SafetyError() {
                    @Override
                    public void onSafetyError(BaseException error) {
                        mViewModel.onGetListAllProductShopInformationError(error);
                    }
                });
        mCompositeSubscription.add(subscription);
    }

    @Override
    public void deleteProduct(int productId, int shopId) {
        Subscription subscription = mProductRepository.requestDeleteProductInShop(productId, shopId)
                .subscribeOn(Schedulers.io())
                .doOnSubscribe(new Action0() {
                    @Override
                    public void call() {
                        mViewModel.onShowProgressDialog();
                    }
                })
                .doAfterTerminate(new Action0() {
                    @Override
                    public void call() {
                        mViewModel.onHideProgressDialog();
                    }
                })
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Action1<BaseResponse>() {
                    @Override
                    public void call(BaseResponse baseRespone) {
                        mViewModel.onDeleteProductSuccess();
                    }
                }, new SafetyError() {
                    @Override
                    public void onSafetyError(BaseException error) {
                        mViewModel.onErrorMessage(error);
                    }
                });
        mCompositeSubscription.add(subscription);
    }
}
