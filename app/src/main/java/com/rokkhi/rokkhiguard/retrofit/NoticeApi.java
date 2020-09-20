//package com.rokkhi.rokkhiguard.retrofit;
//
//import com.rokkhi.rokkhiguard.Model.api.NoticeModelClass;
//import com.rokkhi.rokkhiguard.StaticData;
//
//import retrofit2.Call;
//import retrofit2.http.GET;
//import retrofit2.http.Headers;
//import retrofit2.http.Query;
//
//public interface NoticeApi {
//
//    @Headers("header: ")
//    @GET(StaticData.getNotice)
//    Call<NoticeModelClass> getNoticeList(
//            @Query("noticeFor") String noticeFor,
//            @Query("buildingId") Integer buildID,
//            @Query("communityId") Integer communityId,
//            @Query("fromDate") String fromDate,
//            @Query("toDate") String toDate
//    );
//}
