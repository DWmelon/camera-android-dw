package com.seriousface.m.myapplication.adapter;


import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.seriousface.m.myapplication.R;
import com.seriousface.m.myapplication.constant.Constant;

import java.io.InputStream;
import java.util.List;

/**
 * Created by Administrator on 2016/4/15.
 */
public class GridViewAdapter extends BaseAdapter {

    Context context;
    int[] resStarIds = {R.drawable.icon_mingxing_01,R.drawable.icon_mingxing_02,R.drawable.icon_mingxing_03,R.drawable.icon_mingxing_04,R.drawable.icon_mingxing_05,
            R.drawable.icon_mingxing_06,R.drawable.icon_mingxing_07,R.drawable.icon_mingxing_08,R.drawable.icon_mingxing_09,R.drawable.icon_mingxing_10,
            R.drawable.icon_mingxing_11,R.drawable.icon_mingxing_12,R.drawable.icon_mingxing_13,R.drawable.icon_mingxing_14,R.drawable.icon_mingxing_15,
            R.drawable.icon_mingxing_16,R.drawable.icon_mingxing_17,R.drawable.icon_mingxing_18,R.drawable.icon_mingxing_19,R.drawable.icon_mingxing_21,
            R.drawable.icon_mingxing_23,R.drawable.icon_mingxing_24,R.drawable.icon_mingxing_25,R.drawable.icon_mingxing_26,R.drawable.icon_mingxing_27};
    int[] resXBIds ={R.drawable.icon_xiongben_01,R.drawable.icon_xiongben_02,R.drawable.icon_xiongben_03,R.drawable.icon_xiongben_04,R.drawable.icon_xiongben_05,
            R.drawable.icon_xiongben_06,R.drawable.icon_xiongben_07,R.drawable.icon_xiongben_08,R.drawable.icon_xiongben_09,R.drawable.icon_xiongben_10,
            R.drawable.icon_xiongben_11,R.drawable.icon_xiongben_12,R.drawable.icon_xiongben_13,R.drawable.icon_xiongben_14,R.drawable.icon_xiongben_15,
            R.drawable.icon_xiongben_16,R.drawable.icon_xiongben_17,R.drawable.icon_xiongben_18,R.drawable.icon_xiongben_19,R.drawable.icon_xiongben_20,
            R.drawable.icon_xiongben_21,R.drawable.icon_xiongben_22,R.drawable.icon_xiongben_23,R.drawable.icon_xiongben_24,R.drawable.icon_xiongben_25,
            R.drawable.icon_xiongben_26,R.drawable.icon_xiongben_27,R.drawable.icon_xiongben_28,R.drawable.icon_xiongben_29,R.drawable.icon_xiongben_30,
            R.drawable.icon_xiongben_31,R.drawable.icon_xiongben_32,R.drawable.icon_xiongben_33};
    int[] resEMIds ={R.drawable.icon_emoji_1,R.drawable.icon_emoji_2,R.drawable.icon_emoji_3,R.drawable.icon_emoji_4,R.drawable.icon_emoji_5,
            R.drawable.icon_emoji_6,R.drawable.icon_emoji_7,R.drawable.icon_emoji_8,R.drawable.icon_emoji_9,R.drawable.icon_emoji_10,
            R.drawable.icon_emoji_11,R.drawable.icon_emoji_12,R.drawable.icon_emoji_13,R.drawable.icon_emoji_14,R.drawable.icon_emoji_15,
            R.drawable.icon_emoji_16,R.drawable.icon_emoji_17,R.drawable.icon_emoji_18,R.drawable.icon_emoji_19,R.drawable.icon_emoji_20,
            R.drawable.icon_emoji_21,R.drawable.icon_emoji_22,R.drawable.icon_emoji_23,R.drawable.icon_emoji_24,R.drawable.icon_emoji_25,
            R.drawable.icon_emoji_26,R.drawable.icon_emoji_27,R.drawable.icon_emoji_28,R.drawable.icon_emoji_29,R.drawable.icon_emoji_30,
            R.drawable.icon_emoji_31,R.drawable.icon_emoji_32,R.drawable.icon_emoji_33,R.drawable.icon_emoji_34,R.drawable.icon_emoji_35,
            R.drawable.icon_emoji_36,R.drawable.icon_emoji_37,R.drawable.icon_emoji_38,R.drawable.icon_emoji_39,R.drawable.icon_emoji_40,
            R.drawable.icon_emoji_41,R.drawable.icon_emoji_42,R.drawable.icon_emoji_43,R.drawable.icon_emoji_44,R.drawable.icon_emoji_45,
            R.drawable.icon_emoji_46,R.drawable.icon_emoji_47,R.drawable.icon_emoji_48,R.drawable.icon_emoji_49,R.drawable.icon_emoji_50,
            R.drawable.icon_emoji_51,R.drawable.icon_emoji_52,R.drawable.icon_emoji_53,R.drawable.icon_emoji_54,R.drawable.icon_emoji_55,
            R.drawable.icon_emoji_56,R.drawable.icon_emoji_57,R.drawable.icon_emoji_58,R.drawable.icon_emoji_59,R.drawable.icon_emoji_60,
            R.drawable.icon_emoji_61,R.drawable.icon_emoji_62,R.drawable.icon_emoji_63,R.drawable.icon_emoji_64,R.drawable.icon_emoji_65,
            R.drawable.icon_emoji_66,R.drawable.icon_emoji_67,R.drawable.icon_emoji_68,R.drawable.icon_emoji_69,R.drawable.icon_emoji_70,
            R.drawable.icon_emoji_71,R.drawable.icon_emoji_72,R.drawable.icon_emoji_73,R.drawable.icon_emoji_74,R.drawable.icon_emoji_75,
            R.drawable.icon_emoji_76,R.drawable.icon_emoji_77,R.drawable.icon_emoji_78,R.drawable.icon_emoji_79,R.drawable.icon_emoji_80,
            R.drawable.icon_emoji_81,R.drawable.icon_emoji_82,R.drawable.icon_emoji_83,R.drawable.icon_emoji_84,R.drawable.icon_emoji_85,
            R.drawable.icon_emoji_86,R.drawable.icon_emoji_87,R.drawable.icon_emoji_88,R.drawable.icon_emoji_89,R.drawable.icon_emoji_90,
            R.drawable.icon_emoji_91,R.drawable.icon_emoji_92,R.drawable.icon_emoji_93,R.drawable.icon_emoji_94,R.drawable.icon_emoji_95,
            R.drawable.icon_emoji_96,R.drawable.icon_emoji_97,R.drawable.icon_emoji_98,R.drawable.icon_emoji_99,R.drawable.icon_emoji_100,
            R.drawable.icon_emoji_101,R.drawable.icon_emoji_102,R.drawable.icon_emoji_103,R.drawable.icon_emoji_104,R.drawable.icon_emoji_105,
            R.drawable.icon_emoji_106,R.drawable.icon_emoji_107,R.drawable.icon_emoji_108};

    String PageType;

    public GridViewAdapter(Context context,String PageType){
        this.context = context;
        this.PageType = PageType;
    }

    @Override
    public int getCount() {
        if(PageType == Constant.VALUE_PIC_PAGE_TYPE_STAR){
            return resStarIds.length;
        }else if(PageType == Constant.VALUE_PIC_PAGE_TYPE_XIONG_BEN){
            return resXBIds.length;
        }else{
            return resEMIds.length;
        }
    }

    @Override
    public Object getItem(int position) {
        if(PageType == Constant.VALUE_PIC_PAGE_TYPE_STAR){
            return resStarIds[position];
        }else if(PageType == Constant.VALUE_PIC_PAGE_TYPE_XIONG_BEN){
            return resXBIds[position];
        }else{
            return resEMIds[position];
        }
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

        @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Holder holder;
        if(convertView == null){
            View view = LayoutInflater.from(context).inflate(R.layout.adapter_gridview_face,null);
            holder = new Holder();
            holder.ivIcon = (ImageView)view.findViewById(R.id.iv_grid_item);
            holder.llBgView = (LinearLayout)view.findViewById(R.id.ll_grid_view);
            view.setTag(holder);

            convertView = view;
        }else{
            holder = (Holder)convertView.getTag();
        }

        performData(holder, position);

        return convertView;
    }

    private void performData(Holder holder,int position){
        Bitmap bitmap = readBitMap(context,(int) getItem(position));
        holder.ivIcon.setImageBitmap(bitmap);
        holder.llBgView.setBackgroundResource(R.drawable.bg_grid_item);
    }

    public class Holder{
        ImageView ivIcon;
        LinearLayout llBgView;
    }


    private Bitmap readBitMap(Context context, int resId){
        BitmapFactory.Options opt = new BitmapFactory.Options();
        opt.inPreferredConfig = Bitmap.Config.RGB_565;
        opt.inPurgeable = true;
        opt.inInputShareable = true;
//获取资源图片
        InputStream is = context.getResources().openRawResource(resId);
        return BitmapFactory.decodeStream(is,null,opt);
    }

}
