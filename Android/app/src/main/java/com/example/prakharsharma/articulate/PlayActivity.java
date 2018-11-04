package com.example.prakharsharma.articulate;

import android.content.Intent;
import android.content.res.Configuration;
import android.net.Uri;
import android.os.StrictMode;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.khizar1556.mkvideoplayer.MKPlayer;

import java.io.File;

public class PlayActivity extends AppCompatActivity {


    String filePath ;
    MKPlayer mkplayer ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        filePath = "file://storage/emulated/0/Download/downloadedvideo.mp4" ;// + getFilesDir() + File.separator + "downloadedvideo" ;
        //Uri videoUri = Uri.parse(filePath) ;
        Log.d("Video Player",filePath) ;

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        File file = new File("/storage/emulated/0/Download/downloadedvideo.mp4");
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.fromFile(file), "video/mp4");
        startActivity(intent);
        finish() ;

//        mkplayer = new MKPlayer(this);
//        mkplayer.play(filePath);
//        mkplayer.setTitle("Articulate Preview");
//
//        mkplayer.setPlayerCallbacks(new MKPlayer.playerCallbacks() {
//            @Override
//            public void onNextClick() {
//                mkplayer.play(filePath);
//            }
//
//            @Override
//            public void onPreviousClick() {
//                mkplayer.play(filePath);
//            }
//        });
//
//        mkplayer.onComplete(new Runnable() {
//            @Override
//            public void run() {
//                mkplayer.stop();
//                finish();
//            }
//        });

    }

//    @Override
//    protected void onPause() {
//        super.onPause();
//        if (mkplayer != null) {
//            mkplayer.onPause();
//        }
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if (mkplayer != null) {
//            mkplayer.onResume();
//        }
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (mkplayer != null) {
//            mkplayer.onDestroy();
//        }
//    }
//
//    @Override
//    public void onConfigurationChanged(Configuration newConfig) {
//        super.onConfigurationChanged(newConfig);
//        if (mkplayer != null) {
//            mkplayer.onConfigurationChanged(newConfig);
//        }
//    }
//
//    @Override
//    public void onBackPressed(){
//        if (mkplayer!=null && mkplayer.onBackPressed()){
//            return;
//        }
//        super.onBackPressed();
//    }
}
