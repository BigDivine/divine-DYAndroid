package com.divine.dy.lib_http.retrofit2;

import io.reactivex.ObservableTransformer;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.functions.Consumer;
import okhttp3.RequestBody;

/**
 * Author: Divine
 * CreateDate: 2021/8/24
 * Describe:
 */
public class Retrofit2IModel {
    public void sendRequestPost(String url, RequestBody body, Retrofit2Callback<String> callback) {
        Disposable disposable = Retrofit2Helper.getInstance()
                .getService()
                .sendRequest(url, body)
                .compose(RxJava2SchedulerTransformer.getInstance().applySchedulers())
                .compose((ObservableTransformer) RxJava2ResponseTransformer.handleResult())
                .subscribe(new Consumer<String>() {
                    @Override
                    public void accept(String o) throws Exception {
                        callback.onSuccess(o);
                    }
                }, new Consumer<Throwable>() {
                    @Override
                    public void accept(Throwable throwable) throws Exception {
                        callback.onSuccess(throwable.getMessage());
                    }
                });
        CompositeDisposable mCompositeDisposable = new CompositeDisposable();
        mCompositeDisposable.add(disposable);
    }
}
