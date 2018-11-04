package com.example.prakharsharma.articulate;

import com.example.prakharsharma.articulate.MainActivity;
import com.google.gson.JsonObject;

import org.json.JSONObject;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Url;

/**
 * Created by Belal on 10/2/2017.
 */

public interface Api {

//    String BASE_URL = "http://192.168.101.18:8081/";
    //String articleText = MainActivity.articleContent ;
    @POST("http://192.168.101.18:3001/")
    Call<ResponseBody> getFileContent(@Body String body);




}