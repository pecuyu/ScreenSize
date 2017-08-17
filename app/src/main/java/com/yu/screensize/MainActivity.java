package com.yu.screensize;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import java.lang.reflect.Method;

public class MainActivity extends AppCompatActivity {

    private TextView tvWidth,tvHeight,tvDpi,tvDensity,tvRealWidth,tvRealHeight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //全屏
//        getWindow().setFlags(WindowManager.LayoutParams. FLAG_FULLSCREEN ,
//                WindowManager.LayoutParams. FLAG_FULLSCREEN);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            // 透明状态栏与导航栏
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        }
        // 隐藏actionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().hide();
        }
        setContentView(R.layout.activity_main);

        tvWidth = (TextView) findViewById(R.id.id_tv_width_size);
        tvHeight = (TextView) findViewById(R.id.id_tv_height_size);
        tvDpi = (TextView) findViewById(R.id.id_tv_dpi_size);
        tvDensity = (TextView) findViewById(R.id.id_tv_density_size);
        tvRealWidth = (TextView) findViewById(R.id.id_tv_real_width_size);
        tvRealHeight = (TextView) findViewById(R.id.id_tv_real_height_size);
        getScreenSize();
    }

    void getScreenSize() {
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int widthPixels = metrics.widthPixels;
        int heightPixels = metrics.heightPixels;
        int densityDpi = metrics.densityDpi;
        float density = metrics.density;
        LogUtil.e("TAG", "widthPixels=" + widthPixels + ", heightPixels=" + heightPixels +
                ", densityDpi=" + densityDpi + ",density=" + density);
        tvWidth.setText("widthPixels=" + widthPixels);
        tvHeight.setText("heightPixels=" + (heightPixels + getNavigationBarHeight()));
        tvDpi.setText("densityDpi=" + densityDpi);
        tvDensity.setText("density=" + density);

        float realW = px2mm(widthPixels);
        float realH = px2mm(heightPixels + getNavigationBarHeight());
        tvRealWidth.setText("realWidth=" + realW + "mm");
        tvRealHeight.setText("realHeight=" + realH + "mm");
        LogUtil.e("TAG", "realW=" + realW + ",realH=" + realH);
        // realW=61.000126,realH=108.444664
    }

    public void refetchSize(View view) {
        getScreenSize();
        int nvbar = getNavigationBarHeight();
        Log.e("TAG", "nvbar=" + nvbar);

    }

//    private int getNavigationBarHeight(Context context) {
//        int result = 0;
//        int id = context.getResources().getIdentifier("config_showNavigationBar", "bool", "android");
//        if (id != 0) {
//            int resourceId = context.getResources().getIdentifier("navigation_bar_height", "dimen", "android");
//            return context.getResources().getDimensionPixelSize(resourceId);
//        }
//        return 0;
//    }

    private int getStatusBarHeight() {
        Resources resources = getResources();
        int resourceId = resources.getIdentifier("status_bar_height", "dimen", "android");
        int height = resources.getDimensionPixelSize(resourceId);
        Log.v("TAG", "Status height:" + height);
        return height;
    }


    /**
     * 获取虚拟功能键高度
     */
    public int getNavigationBarHeight() {
        int height = 0;
        WindowManager windowManager = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics outMetrics = new DisplayMetrics();
        try {
            Class c = Class.forName("android.view.Display");
            Method method = c.getMethod("getRealMetrics", DisplayMetrics.class);
            method.invoke(display, outMetrics);
            height = outMetrics.heightPixels - windowManager.getDefaultDisplay().getHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return height;
    }

    //转换dp为px
    public int dp2px(Context context, int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getResources().getDisplayMetrics());
    }

    //转换px为dp
    public int sp2px(Context context, int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResources().getDisplayMetrics());
    }

  //  px =mm *metrics.xdpi *(1.0f/25.4f)
  //  mm=px/(metrics.xdpi *(1.0f/25.4f))
    public float px2mm(int px) {
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        float mm = px / (metrics.xdpi * (1.0f / 25.4f));
        return mm;
    }

    public void calcDistance(View view) {
        startActivity(new Intent(this,DistanceActivity.class));
    }
}
