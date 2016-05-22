package kf.qf.com.hwdemowuziqi;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    private  String TAG = "ActivitMainy";
    PackageManager pm ;
    private  String name = "com.sohu.newsclient";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo mw = wifiManager.getConnectionInfo();
        Log.d("11111111111111111111111", mw.getSSID() + "");

    }

    public  void btn(View view) {
       /* WifiManager wifiManager = (WifiManager) getSystemService(WIFI_SERVICE);
        WifiInfo mw = wifiManager.getConnectionInfo();
        Log.d("11111111111111111111111",mw.getSSID()+"");*/
        pm = this.getPackageManager();
        // 查询所有已经安装的应用程序
        List<ApplicationInfo> listAppcations = pm.getInstalledApplications(PackageManager.GET_UNINSTALLED_PACKAGES);
        Collections.sort(listAppcations, new ApplicationInfo.DisplayNameComparator(pm));// 排序

        // 保存所有正在运行的包名 以及它所在的进程信息
        Map<String, ActivityManager.RunningAppProcessInfo> pgkProcessAppMap = new HashMap<String, ActivityManager.RunningAppProcessInfo>();

        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        // 通过调用ActivityManager的getRunningAppProcesses()方法获得系统里所有正在运行的进程
        List<ActivityManager.RunningAppProcessInfo> appProcessList = mActivityManager
                .getRunningAppProcesses();

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcessList) {
             if (name.equals(appProcess.processName)){
                 Log.i(TAG, "processName: " + appProcess);
                 Log.i(TAG, "process: " + appProcess.processName+appProcess.pid);
             }
          /*  int pid = appProcess.pid; // pid
            String processName = appProcess.processName; // 进程名
            pid: " + pidLog.i(TAG, "processName: " + processName + " );*/

        }
    }
}
