//package org.techtown.flo
//
//import com.google.gson.annotations.SerializedName
//
//data class AuthResponse(
//    @SerializedName(value = "isSuccess") val isSuccess: Boolean,
//    @SerializedName(value = "code") val code: Int,
//    @SerializedName(value = "message") val message: String,
//    @SerializedName(value = "result") val result: Result?
//    // null 처리를 해야 회원가입 api 했을 때 알아서 null 처리된다
//)
//
//data class Result(
//    @SerializedName(value = "userIdx") var userIdx: Int,
//    @SerializedName(value = "jwt") var jwt: String
//)
