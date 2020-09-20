//package com.rokkhi.rokkhiguard.retrofit;
//
//import androidx.lifecycle.LiveData;
//import androidx.lifecycle.MutableLiveData;
//import androidx.lifecycle.ViewModel;
//
//import com.rokkhi.rokkhiguard.Model.api.NoticeModelClass;
//
//
//public class NoticeViewModel extends ViewModel {
//
//    private MutableLiveData<NoticeModelClass> mutableLiveData;
//    private NewsRepository newsRepository;
//
//    public void init(String noticeFor, int buildID, int communityID, String fromDate, String toDate){
//        if (mutableLiveData == null){
//            newsRepository = NewsRepository.getInstance();
//
//            mutableLiveData = newsRepository.getNotice(noticeFor, buildID,communityID,fromDate,toDate);
//
//        }
//
//    }
//
//    public LiveData<NoticeModelClass> getNewsRepository() {
//        return mutableLiveData;
//    }
//
//}