package com.example.prakharsharma.articulate;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.AppCompatMultiAutoCompleteTextView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.Toolbar;

import com.appus.splash.Splash;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class MainActivity extends AppCompatActivity {

    EditText urlLink ;
    AppCompatMultiAutoCompleteTextView articleText ;

    public static String baseUrl,articleContent ;

    //Button genVidButton = findViewById(R.id.generateVideo) ;

    Button genVid ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Splash.Builder splash = new Splash.Builder(this, this.getSupportActionBar()) ;
        splash.setSplashImage(getResources().getDrawable(R.drawable.logo)) ;
        splash.setBackgroundColor(getResources().getColor(R.color.splashbgcolor)) ;
        splash.perform() ;

//        Intent intent = new Intent(MainActivity.this,PlayActivity.class) ;
//        startActivity(intent) ;

        genVid = findViewById(R.id.generateVideo) ;
        //urlLink = findViewById(R.id.urlLink) ;
        articleText = findViewById(R.id.articleText) ;

        genVid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

//                baseUrl = urlLink.getText().toString() ;
                articleContent = articleText.getText().toString() ;

                articleContent = articleContent.replaceAll("\n" , " ");

                Toast.makeText(MainActivity.this, "Contacting server... Please be patient", Toast.LENGTH_SHORT).show();

               final OkHttpClient client = new OkHttpClient.Builder().readTimeout(120, TimeUnit.SECONDS)
                        .connectTimeout(120, TimeUnit.SECONDS).build();

                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("https://google.com")
                        .client(client)
                       .addConverterFactory(ScalarsConverterFactory.create())
                        .build() ;

                Api api = retrofit.create(Api.class) ;


                // Jo de rahe hain uska call
                String toSend = "{ \"content\": \"" + articleContent + "\"}";
                Call<ResponseBody> call = api.getFileContent(toSend) ;

                call.enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                        // input stream to read file - with 8k buffer
                        InputStream input = response.body().byteStream();
                        // Output stream to write file
                        File root = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
                        root.mkdir();
                        File file = new File(root,"downloadedvideo.mp4");
                        OutputStream output = null;
                       Log.v("This dir ", file.getPath());
                        try {
                            output = new FileOutputStream(file);

                            byte data[] = new byte[204800];

                            long total = 0;int count;
                            while ((count = input.read(data)) != -1) {
                                total += count;

                                // writing data to file
                                output.write(data, 0, count);

                            }

                            // flushing output
                            output.flush();

                            // closing streams
                            output.close();
                            input.close();



                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                  //      Log.v("Body",thisFileContent.toString());
                        Toast.makeText(MainActivity.this, "File saved at " + root + "/downloaded.mp4" , Toast.LENGTH_LONG).show();
                        //genVidButton.setBackgroundColor(Integer.parseInt("#0000"));
                        Intent intent = new Intent(MainActivity.this,PlayActivity.class) ;
                        startActivity(intent) ;
                    }

                    @Override
                    public void onFailure(Call<ResponseBody> call, Throwable t) {
                        Toast.makeText(MainActivity.this, "Error receiving file", Toast.LENGTH_SHORT).show();
                        t.printStackTrace();
                        Log.v("Res" , t.getMessage());
                    }
                });
            }
        });
    }

}
