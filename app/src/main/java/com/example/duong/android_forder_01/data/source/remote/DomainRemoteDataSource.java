package com.example.duong.android_forder_01.data.source.remote;

import com.example.duong.android_forder_01.data.model.CollectionDomain;
import com.example.duong.android_forder_01.data.model.Domain;
import com.example.duong.android_forder_01.data.model.DomainResponse;
import com.example.duong.android_forder_01.data.model.User;
import com.example.duong.android_forder_01.data.source.DomainDataSource;
import com.example.duong.android_forder_01.data.source.GetDataCallback;
import com.example.duong.android_forder_01.service.API;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.example.duong.android_forder_01.utils.Const.ConstantApi.PARAM_DOMAIN_ID;
import static com.example.duong.android_forder_01.utils.Const.ConstantApi.PARAM_USER_EMAIL;
import static com.example.duong.android_forder_01.utils.Const.ConstantApi.PARAM_USER_TOKEN;

/**
 * Created by Vinh on 27/02/2017.
 */
public class DomainRemoteDataSource implements DomainDataSource {
    public static DomainRemoteDataSource sDomainRemoteDataSource;

    private DomainRemoteDataSource() {
    }

    public static DomainRemoteDataSource getInstance() {
        if (sDomainRemoteDataSource == null) {
            sDomainRemoteDataSource = new DomainRemoteDataSource();
        }
        return sDomainRemoteDataSource;
    }

    @Override
    public void getDatasDomain(User user, final GetDataCallback<Domain> getDataCallback) {
        if (getDataCallback == null && user == null) return;
        Map<String, String> params = new HashMap<>();
        params.put(PARAM_USER_EMAIL, user.getUserName());
        params.put(PARAM_USER_TOKEN, user.getAuthenticationToken());
        API.getDomain(params, new Callback<DomainResponse>() {
                @Override
                public void onResponse(Call<DomainResponse> call,
                                       Response<DomainResponse> response) {
                    if (response == null && response.body().getDomainList() == null) {
                        getDataCallback.onNotAvailable();
                        return;
                    }
                    List<Domain> list = new ArrayList<>();
                    for (CollectionDomain collectionDomain : response.body().getDomainList()) {
                        list.add(collectionDomain.getDomain());
                    }
                    getDataCallback.onLoaded(list);
                }

                @Override
                public void onFailure(Call<DomainResponse> call, Throwable t) {
                    getDataCallback.onNotAvailable();
                }
            }
        );
    }

    @Override
    public void getDatasPrivateDomainInfor(User user, final GetDataCallback getDataCallback) {
        if (getDataCallback == null && user == null) return;
        //TODO: Load private domain
    }

    @Override
    public void getDatasPublicDomainInfor(User user, final GetDataCallback getDataCallback) {
        if (getDataCallback == null && user == null) return;
        //TODO: Load public domain
    }
}
