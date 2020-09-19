package com.rokkhi.rokkhiguard.retrofit;


import androidx.lifecycle.MutableLiveData;

import com.rokkhi.rokkhiguard.Model.api.NoticeModelClass;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsRepository {

    private static NewsRepository newsRepository;

    public static NewsRepository getInstance() {
        if (newsRepository == null) {
            newsRepository = new NewsRepository();
        }
        return newsRepository;
    }

    private NoticeApi noticeApi;

    public NewsRepository() {
        noticeApi = RetrofitService.cteateService(NoticeApi.class);
    }

    public MutableLiveData<NoticeModelClass> getNotice(String noticeFor, int buildID, int communityID, String fromDate, String toDate) {
        final MutableLiveData<NoticeModelClass> newsData = new MutableLiveData<>();
        noticeApi.getNoticeList(noticeFor, buildID, communityID, fromDate, toDate).enqueue(new Callback<NoticeModelClass>() {
            @Override
            public void onResponse(Call<NoticeModelClass> call,
                                   Response<NoticeModelClass> response) {
                if (response.isSuccessful()) {

                    newsData.setValue(response.body());
                }
            }

            @Override
            public void onFailure(Call<NoticeModelClass> call, Throwable t) {
                newsData.setValue(null);
            }
        });
        return newsData;
    }
}