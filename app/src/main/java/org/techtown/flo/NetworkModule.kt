//package org.techtown.flo
//
//import retrofit2.Retrofit
//import retrofit2.converter.gson.GsonConverterFactory
//
//const val BASE_URL = "https://edu-api-test.softsquared.com"
//
//fun getRetrofit(): Retrofit {
//    val retrofit = Retrofit.Builder().baseUrl(BASE_URL)
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
//
//    // *** 만약 BASE_URL com 뒤에 / 붙였다면 @POST 쓸 때 /users 슬레쉬 떼어야 함
//    return retrofit
//}