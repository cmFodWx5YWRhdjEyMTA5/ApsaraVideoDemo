package avd.nk.com.apsaravideodemo;

import android.app.Application;

import com.alivc.player.AliVcMediaPlayer;
import com.alivc.player.VcPlayerLog;
import com.squareup.leakcanary.LeakCanary;

/**
 * Created by Nikou Karter.
 *
 *
 */
public class DemoApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();

        VcPlayerLog.enableLog();
        AliVcMediaPlayer.init(getApplicationContext());

        //设置保存密码。此密码如果更换，则之前保存的视频无法播放
        //AliyunDownloadConfig config = new AliyunDownloadConfig();
        //config.setSecretImagePath(Environment.getExternalStorageDirectory().getAbsolutePath()+"/aliyun/encryptedApp.dat");
        //config.setDownloadPassword("123456789");

        //设置保存路径。请确保有SD卡访问权限。
        //config.setDownloadDir(Environment.getExternalStorageDirectory().getAbsolutePath()+"/test_save/");

        //设置同时下载个数
        //config.setMaxNums(2);

        //AliyunDownloadManager.getInstance(this).setDownloadConfig(config);

        initLeakCanary();
        initGlide();
    }

    private void initGlide() {

    }

    private void initLeakCanary() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
    }
}
