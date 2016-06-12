package com.xbp.retiofixtest;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;
import data.ApiService;
import data.GetIpInfoResponse;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;
import retrofit.RxJavaCallAdapterFactory;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {

    private static final String ENDPOINF="http://ip.taobao.com";
    @Bind(R.id.tv_content) TextView mTvContent;
    @Bind(R.id.progress_bar) ProgressBar mProgressBar;
    @Bind(R.id.Get)Button button;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

    }

    @OnClick(R.id.Get)
    public void getData(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ENDPOINF)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .build();
        ApiService apiService=retrofit.create(ApiService.class);
        apiService.getIpInfo("63.223.108.42")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<GetIpInfoResponse>() {
                    @Override
                    public void onCompleted() {
                        mProgressBar.setVisibility(View.GONE);
                    }

                    @Override
                    public void onError(Throwable e) {
                        mProgressBar.setVisibility(View.GONE);
                        mTvContent.setText(e.getMessage());
                    }

                    @Override
                    public void onNext(GetIpInfoResponse getIpInfoResponse) {
                        mTvContent.setText(getIpInfoResponse.data.country);
                    }
                });

    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
    ButterKnife.unbind(this);
    }
}
