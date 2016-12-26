package com.dc.hwallpaper;

import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.graphics.Palette;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.dc.hwallpaper.base.BaseActivity;
import com.dc.hwallpaper.handler.MainActivityHandler;
import com.dc.hwallpaper.utils.FileUtils;
import com.dc.hwallpaper.utils.ImageUtil;
import com.dc.hwallpaper.utils.Logger;
import com.dc.hwallpaper.utils.ScreenUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends BaseActivity {
    public final static String TAG = "TakePhotoActivity";
    private final String IMAGE_TYPE = "image/*";
    private final int RANDOM_COLOR_SIZE = 6;//随机颜色下标范围
    private final int TAG_CHOOSE_IMG = 0;
    private View mPicBox;
    private int mScreenWidth;
    private int mScreenHeight;
    private String mPhotoPath;
    private Bitmap mBitmap;
    private final String ACTION_TAKE_PHOTO = CommonDefine.ACTION_TAKE_PHOTO_TO_SHOW_IMG;
    private final String ACTION_CHOOSE_PIC = CommonDefine.ACTION_CHOOSE_PIC_TO_SHOW_IMG;
    private String mAction;
    private ImageView mIvShowPhoto;
    private View mFLayoutIconBox;
    private RecyclerView mRvPanList;
    private List<Integer> mPanList;
    private MainActivityHandler mMainHandler;
    private View mIvShadow;
    private boolean mIsShadowShow;
    private int mCurrentColor;

    @Override
    protected void initVar() {
        mIsShadowShow = true;
        mScreenWidth = ScreenUtils.getScreenWidth(mContext);
        mPanList = new ArrayList<>();
        mMainHandler = new MainActivityHandler(mContext, mScreenWidth);
    }

    @Override
    protected void initView(Bundle savedInstanceState) {
        setSystemBarTranslucent(false, true, false, 0);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        setTitle("");
        mPicBox = findViewById(R.id.f_layout_pic_box);
        mMainHandler.setBoxSize(mPicBox);
        mPicBox.setOnClickListener(new ClickChoosePic());
        mIvShowPhoto = ((ImageView) findViewById(R.id.iv_show_photo));
        mFLayoutIconBox = findViewById(R.id.f_layout_icon_box);
        mRvPanList = ((RecyclerView) findViewById(R.id.rv_pan_list));
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(mRvPanList.getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRvPanList.setLayoutManager(layoutManager);
        mIvShadow = findViewById(R.id.iv_shadow);
    }

    @Override
    protected void initListener() {

    }

    @Override
    protected void loadData() {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode != RESULT_OK) {
            Logger.i("---", "ActivityResult resultCode error");
            return;
        }
        //外界的程序访问ContentProvider所提供数据 可以通过ContentResolver接口
        ContentResolver resolver = getContentResolver();
        //根据tag判断回调类型
        if (requestCode == TAG_CHOOSE_IMG) {//选取图片回调
            try {
                //获得图片的uri
                Uri originalUri = data.getData();
                //有的时候uri获取过来是file路径,所以要进行区分,否则会空指针
                if (originalUri.toString().startsWith("file:///")) {
                    if (ImageUtil.isPicture(originalUri.toString())) {
                        mPhotoPath = originalUri.getPath();
                        mBitmap = BitmapFactory.decodeFile(mPhotoPath);
                        mIvShowPhoto.setImageBitmap(mBitmap);
                    } else {
                        toastGo("您选取的不是图片!");
                    }
                } else {
                    mBitmap = MediaStore.Images.Media.getBitmap(resolver, originalUri);
                    mIvShowPhoto.setImageBitmap(mBitmap);
                    getPanColor();
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        mPhotoPath = FileUtils.getPathFromUri(getApplication(), originalUri);
                    } else {
                        //显得到bitmap图片这里开始的第二部分，获取图片的路径：
                        String[] proj = {MediaStore.Images.Media.DATA};
                        //好像是android多媒体数据库的封装接口，具体的看Android文档
                        Cursor cursor = managedQuery(originalUri, proj, null, null, null);
                        //按我个人理解 这个是获得用户选择的图片的索引值
                        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
                        //将光标移至开头 ，这个很重要，不小心很容易引起越界
                        cursor.moveToFirst();
                        Logger.i(1, "cusor index:" + column_index);
                        Logger.i(1, "cusor get String:" + cursor.getString(0));
                        //最后根据索引值获取图片路径
                        mPhotoPath = cursor.getString(column_index);
                        cursor.close();
                    }
                    Logger.i("选取图片path:" + mPhotoPath);
                    mAction = ACTION_CHOOSE_PIC;
                }

            } catch (IOException e) {
                Logger.i("---err", e.toString());
            }
            Logger.i("选择图片完毕");
        }
    }

    /**
     * 点击事件:选择图片
     */
    private class ClickChoosePic implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            Intent getAlbum = new Intent(Intent.ACTION_GET_CONTENT);
            getAlbum.setType(IMAGE_TYPE);
            startActivityForResult(getAlbum, TAG_CHOOSE_IMG);
        }

    }


    private void setPanList() {
        PanListAdapter adapter = new PanListAdapter(mPanList);
        adapter.setOnItemClickListener(new PanListAdapter.OnItemClickListener() {
            @Override
            public void onClick(View v, int position) {
                setCurrentColor(mPanList.get(position));
            }
        });
        mRvPanList.setAdapter(adapter);
        int index = new Random().nextInt(RANDOM_COLOR_SIZE);
        setCurrentColor(mPanList.get(index));
    }

    private void getPanColor() {
        // 简单快速的实现方法，内部使用AsyncTask
        // 但是可能不是最优的方法(因为有线程的切换)
        // 默认颜色数量(16).
        Palette.generateAsync(mBitmap, new Palette.PaletteAsyncListener() {
            @Override
            public void onGenerated(Palette palette) {
                List<Palette.Swatch> swatches = palette.getSwatches();
                Logger.i(1, "size:" + swatches.size());
                if (mPanList != null) {
                    mPanList.clear();
                    mPanList = new ArrayList<Integer>();
                }
                for (Palette.Swatch swatch : swatches) {
                    mPanList.add(swatch.getRgb());
                }
                setPanList();
            }
        });
    }

    private void setCurrentColor(int color) {
        mCurrentColor = color;
        mFLayoutIconBox.setBackgroundColor(mCurrentColor);
    }

    private void CompositeBitmaps(View pic, View shadow) {
        Bitmap bitmapPic = ImageUtil.getBitmapFromView(pic);
        Bitmap bitmapShadow = ImageUtil.getBitmapFromView(shadow);
        Bitmap newBitmap = Bitmap.createBitmap(1080, 1920, Bitmap.Config.ARGB_8888);

        Canvas canvas = new Canvas(newBitmap);
        canvas.drawColor(mCurrentColor);
        canvas.drawBitmap(bitmapPic, 0, 0, null);
        if (mIsShadowShow) {
            canvas.drawBitmap(bitmapShadow, 0, pic.getHeight(), null);
        }
        canvas.save(Canvas.ALL_SAVE_FLAG);//保存
        canvas.restore();//存储
        bitmapPic.recycle();
        bitmapShadow.recycle();
        String filePath = FileUtils.getFolderPathDC("test");
        String picName = FileUtils.createFileName("test", ".jpg");
        boolean isOk = ImageUtil.savePic(MainActivity.this, newBitmap, filePath + picName, picName);
        Logger.i(1, "isSaveOk:" + isOk);
    }

    @Override
    public void handleMessage(Message msg) {
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {

        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_shadow) {
            mIsShadowShow = mMainHandler.setShadowVisibility(mIvShadow, false);
            if (mIsShadowShow) {
                item.setTitle("隐藏阴影");
            } else {
                item.setTitle("显示阴影");
            }
        } else if (id == R.id.action_save) {
            CompositeBitmaps(mPicBox, mIvShadow);
        }
        return super.onOptionsItemSelected(item);
    }
}
