package avd.nk.com.apsaravideodemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import avd.nk.com.apsaravideodemo.widget.LiveRoomView;

/**
 * Created by Nikou Karter.
 *
 *
 */
public class MainActivity extends AppCompatActivity {

    //private String pullPath = "http://player.alicdn.com/video/aliyunmedia.mp4";
    //private String pullPath = "http://files.puxiansheng.net/record/study/video/2018-10-01/c32c580794bec2221e1922a244c47542.mp4";
    private String pullPath = "rtmp://live.puxiansheng.net/ceshi/5?auth_key=1540976383-0-0-6965b01147ca6ca096d301ab689e1736";
    private String pushPath = "rtmp://tl.puxiansheng.net/ceshi/5?auth_key=1540976383-0-0-b5697328dbef690be772968289eb63db";

    private EditText inputPullUrl;
    private EditText inputPushUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        checkPermission();
        initView();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    private void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (this.checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.RECORD_AUDIO) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED ||
                    this.checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS,
                        Manifest.permission.CAMERA}, 0);
            }
        }
    }

    private void initView() {
        inputPullUrl = findViewById(R.id.inputPullUrl);
        inputPullUrl.setText(pullPath);

        inputPushUrl = findViewById(R.id.inputPushUrl);
        inputPushUrl.setText(pushPath);

        findViewById(R.id.playBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PlayActivity.class);
                intent.putExtra("pullPath", getPullPath());
                startActivity(intent);
            }
        });

        findViewById(R.id.pushBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, PushActivity.class);
                intent.putExtra("pushPath", getPushPath());
                startActivity(intent);
            }
        });

        findViewById(R.id.liveRoomBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, LiveRoomActivity.class);
                intent.putExtra("pullPath", getPullPath());
                startActivity(intent);
            }
        });
    }

    private String getPushPath(){
        return inputPushUrl.getText().toString();
    }

    private String getPullPath(){
        return inputPullUrl.getText().toString();
    }
}
