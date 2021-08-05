package com.divine.dy.app_login;

import com.divine.dy.lib_base.GmtBase;
import com.divine.dy.lib_http.lib.RetrofitUtils;
import com.divine.dy.lib_http.retrofit2.GeneralException;
import com.divine.dy.lib_http.retrofit2.Retrofit2Callback;
import com.divine.dy.lib_http.retrofit2.Retrofit2Helper;
import com.divine.dy.lib_http.retrofit2.Retrofit2Service;
import com.divine.dy.lib_http.retrofit2.RxJava2ResponseTransformer;
import com.divine.dy.lib_http.retrofit2.RxJava2SchedulerTransformer;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.RequestBody;

/**
 * Author: Divine
 * CreateDate: 2021/2/4
 * Describe:
 */
public class LoginModel {
    public void login(String params, Retrofit2Callback callback) {
        CompositeDisposable mCompositeDisposable = new CompositeDisposable();
        Retrofit2Service service = Retrofit2Helper.getInstance().getService();
        String loginServerUrl = GmtBase.serverUrl + LoginBase.loginServerPath;
        RequestBody body = RetrofitUtils.String2RequestBody(params);
        Disposable disposable = service.sendRequest(loginServerUrl, body)
                .compose(RxJava2SchedulerTransformer.getInstance().applySchedulers())
                .compose((ObservableTransformer) RxJava2ResponseTransformer.handleResult())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String o) throws Exception {
                        callback.onSuccess(o);
                    }
                }, new Consumer<GeneralException>() {
                    @Override
                    public void accept(GeneralException throwable) throws Exception {
                        callback.onFailed(throwable.getErrorMessage());
                    }
                });
        mCompositeDisposable.add(disposable);
    }
}
