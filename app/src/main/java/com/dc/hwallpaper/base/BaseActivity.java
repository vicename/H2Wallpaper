package com.dc.hwallpaper.base;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.dc.hwallpaper.R;
import com.dc.hwallpaper.utils.CommonUtils;
import com.dc.hwallpaper.utils.ScreenUtils;
import com.dc.hwallpaper.utils.WeakHandler;

/**
 * Created by Li DaChang on 16/12/25.
 * ..-..---.-.--..---.-...-..-....-.
 */

public abstract class BaseActivity extends AppCompatActivity implements WeakHandler.IHandler {
    protected Context mContext;
    public WeakHandler mHandler;
    private View mStatusBarFitter;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = this;
        mHandler = new WeakHandler(this);

        initVar();
        initView(savedInstanceState);
        initListener();
        loadData();
    }

    protected abstract void initVar();

    protected abstract void initView(Bundle savedInstanceState);

    protected abstract void initListener();

    protected abstract void loadData();

    /**
     * 设置系统状态栏/导航栏透明
     * 假如需要的话就在页面onCreate()方法的setContentView之前调用.设置之后需要根据需要在xml里或java代码里调整相关元素的位置例如toolbar
     *
     * @param isOnlyKITKAT         是否仅限KITKAT版本(android4.4/api19/api20).毕竟5.0以上有状态栏变色.
     * @param isNavigationBarTrans 是否设置导航栏透明,毕竟很多情况下不需要导航栏透明
     * @param isFitStatusBar       是否填充状态栏,状态栏默认为透明,假如页面存在toolbar,状态栏会则需要填充
     * @param colorId              颜色id,填充状态栏时候的颜色.如果不填充的话可以随便填.注意是id,不是颜色值
     */
    @TargetApi(Build.VERSION_CODES.KITKAT)
    protected void setSystemBarTranslucent(boolean isOnlyKITKAT, boolean isNavigationBarTrans, boolean isFitStatusBar, int colorId) {
        boolean isVersionFit = false;
        if (isOnlyKITKAT) {
            //如果系统版本等于4.4KITKAT或KITKAT_WATCH
            if (Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT || Build.VERSION.SDK_INT == Build.VERSION_CODES.KITKAT_WATCH) {
                isVersionFit = true;
            }
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                isVersionFit = true;
            }
        }
        if (isVersionFit) {
            //添加状态栏透明标记
            getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            if (isNavigationBarTrans) {
                //添加导航栏透明标记
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
            }
            if (isFitStatusBar) {
                //填充状态栏
                fitStatusBar(colorId);
            }
        }
    }

    /**
     * 用View填充StatusBar
     */
    protected void fitStatusBar() {
        fitStatusBar(R.color.colorPrimary);
    }

    /**
     * 用View填充StatusBar
     */
    protected void fitStatusBar(int resId) {
        // 创建View
        mStatusBarFitter = new View(this);
        LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, ScreenUtils.getStatusHeight(mContext));
        mStatusBarFitter.setBackgroundColor(ContextCompat.getColor(mContext, resId));
        mStatusBarFitter.setLayoutParams(lParams);
        // 获得根视图并把TextView加进去。
        ViewGroup viewGroup = (ViewGroup) getWindow().getDecorView();
        viewGroup.addView(mStatusBarFitter);
    }

    protected void removeStatusBarFitter() {
        if (mStatusBarFitter != null && mStatusBarFitter.getParent() != null) {
            ((ViewGroup) mStatusBarFitter.getParent()).removeView(mStatusBarFitter);
        }
    }

    public void toastGo(final String s) {
        if (!CommonUtils.isInMainThread()) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(BaseActivity.this, s, Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            Toast.makeText(BaseActivity.this, s, Toast.LENGTH_SHORT).show();
        }
    }
}


