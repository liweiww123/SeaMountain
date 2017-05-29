package com.play.zv.seamountain.view;

import android.content.Context;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.gyf.barlibrary.ImmersionBar;
import com.orhanobut.logger.Logger;
import com.play.zv.seamountain.R;
import com.play.zv.seamountain.api.AvjsoupApi.Magnet;
import com.play.zv.seamountain.api.AvjsoupApi.Star;
import com.play.zv.seamountain.db.AvDataHelper;
import com.play.zv.seamountain.widget.ToastUtils;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by Zv on 2017/05/27.
 */

public class AvDetilsActivity extends AppCompatActivity {
    private TextView mcensored;
    private TextView mruntime;
    private TextView avname;
    private ImageView avcover;
    private TextView avnum;
    private Context mContext;
    public String mAvnum;
    public static final String AVNUM = "av_num";
    private LinearLayout linearLayout;
    private LinearLayout megnetlinearLayout;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.av_detail_activity);
        mContext = getApplicationContext();
        avcover = (ImageView) findViewById(R.id.avcover);
        avnum = (TextView) findViewById(R.id.avnum);
        mcensored = (TextView) findViewById(R.id.censored);
        mruntime = (TextView) findViewById(R.id.runtime);
        avname = (TextView) findViewById(R.id.avname);
        linearLayout = (LinearLayout) findViewById(R.id.starlayout);
        megnetlinearLayout = (LinearLayout) findViewById(R.id.megnetLayout);

        //设置透明状态栏
        ImmersionBar.
                with(this)
                .statusBarDarkFont(true)
                .init();

        parseIntent();

        String avCover = AvDataHelper.findMovie(mAvnum, "cover", mContext);

        Glide.with(mContext).load(avCover).centerCrop().
                diskCacheStrategy(DiskCacheStrategy.SOURCE).into(avcover);
        Logger.d(avCover);
        avnum.setText(mAvnum);

        String avName = AvDataHelper.findMovie(mAvnum, "title", mContext);
        avname.setText(avName);
        mcensored.setText(AvDataHelper.findMovie(mAvnum, "censored", mContext));
        mruntime.setText(AvDataHelper.findMovie(mAvnum, "runtime", mContext));

        //多个starlist
        String starsname = AvDataHelper.findMovie(mAvnum, "stars", mContext);
        if (starsname != null) {
            List<String> starsnames = Arrays.asList(starsname.split(","));

            List<Star> stars = new ArrayList<>();
            if (starsnames != null) {
                for (String avstarname : starsnames) {
                    Star star = AvDataHelper.findstar(avstarname.trim(), mContext);
                    Logger.d(avstarname.trim());
                    stars.add(star);
                }
            }
            if (stars.size() != 0) {
                for (int i = 0; i < stars.size(); i++) {
                    int height = dip2px(mContext, 50);
                    int width = dip2px(mContext, 50);
                    ImageView imageView = new ImageView(this);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(height, width);
                    layoutParams.setMargins(10, 10, 10, 10);
                    imageView.setLayoutParams(layoutParams);
                    final String starnametest = stars.get(i).getName();
                    imageView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            ToastUtils.showToast(mContext, starnametest);
                        }
                    });
                    Glide.with(mContext).load(stars.get(i).getImage()).centerCrop().
                            diskCacheStrategy(DiskCacheStrategy.SOURCE).
                            bitmapTransform(new CropCircleTransformation(mContext)).
                            into(imageView);
                    linearLayout.addView(imageView);

                }
            }
        }
        List<Magnet> magnetList = AvDataHelper.findmagnet(mAvnum, mContext);
        //多个磁力list
        if (magnetList != null) {
            //addLinearLayout(magnetList);
            addCard(magnetList);
        }
    }

    private void parseIntent() {
        mAvnum = getIntent().getStringExtra(AVNUM);
    }

    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }


    /**
     * 动态添加线性布局
     */
    private void addLinearLayout(List<Magnet> magnets) {
        //initMissionList：存储几条测试数据
        for (int i = 0; i < magnets.size(); i++) {
            //实例化一个LinearLayout
            LinearLayout linearLayout = new LinearLayout(this);
            //设置LinearLayout属性(宽和高)
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, dip2px(mContext, 50));

            //将以上的属性赋给LinearLayout
            linearLayout.setLayoutParams(layoutParams);
            //实例化一个TextView
            TextView tv = new TextView(this);
            //设置宽高以及权重
            LinearLayout.LayoutParams tvParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
            //设置textview垂直居中
            tvParams.gravity = Gravity.CENTER_VERTICAL;
            tv.setLayoutParams(tvParams);
            tv.setTextSize(14);
            tv.setText(magnets.get(i).getMagnetUrl().toString().trim());


            linearLayout.addView(tv);


            megnetlinearLayout.addView(linearLayout);
        }

    }

    /**
     * 动态添加线性布局
     */
    private void addCard(List<Magnet> magnets) {
        //initMissionList：存储几条测试数据
        for (int i = 0; i < magnets.size(); i++) {
            CardView cardView = new CardView(mContext);
            CardView.LayoutParams layoutParams = new CardView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, dip2px(mContext, 50));
            layoutParams.setMargins(dip2px(mContext, 5), dip2px(mContext, 5), dip2px(mContext, 5), dip2px(mContext, 5));
            cardView.setLayoutParams(layoutParams
            );

            megnetlinearLayout.addView(cardView);
        }

    }
}