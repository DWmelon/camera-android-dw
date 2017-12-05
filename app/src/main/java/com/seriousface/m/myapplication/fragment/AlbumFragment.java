package com.seriousface.m.myapplication.fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.seriousface.m.myapplication.R;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by yuyidong on 14-12-22.
 */
public class AlbumFragment extends Fragment implements View.OnClickListener {

    public static AlbumFragment newInstance() {
        AlbumFragment instance = new AlbumFragment();
        return instance;
    }

    private List<String> mList;
    private DisplayImageOptions mOptions;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mList = getPhotosPaths();
        initImageLoader();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.album_frag, container,false);
        initUIAndListener(v);
        return v;
    }

    private void initUIAndListener(View v) {
        ImageView btnBack = (ImageView) v.findViewById(R.id.title_back_btn);
        btnBack.setOnClickListener(this);
        TextView txtTitle = (TextView) v.findViewById(R.id.title_text_title);
        txtTitle.setText("相册");
        GridView gv = (GridView) v.findViewById(R.id.gv_album);
        gv.setAdapter(new MyGridViewAdapter());
        gv.setOnItemClickListener(new MyGridViewItemClick());
    }

    private void initImageLoader() {
        mOptions = new DisplayImageOptions.Builder()
                .showImageForEmptyUri(R.mipmap.ic_launcher)
                .showStubImage(R.mipmap.ic_launcher)
                .cacheInMemory(true)
                .bitmapConfig(Bitmap.Config.RGB_565)
                .build();
    }

    private List<String> getPhotosPaths() {
        if(!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)){
            Toast.makeText(getActivity(),"can not write to device",Toast.LENGTH_LONG).show();
            return new ArrayList<>();
        }
        List<String> list = new ArrayList<String>();
        checkJpegDir();
        File dir = new File(getActivity().getExternalFilesDir(null) + "/face/jpeg/");
        File[] files = dir.listFiles();
        for (File file : files) {
            list.add("file:///" + getActivity().getExternalFilesDir(null) + "/face/jpeg/" + file.getName());
        }
        return list;
    }

    private void checkJpegDir() {
        File dir = new File(getActivity().getExternalFilesDir(null) + "/face/jpeg/");
        if (!dir.exists()) {
            dir.mkdirs();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_back_btn:
                getActivity().getFragmentManager().beginTransaction().replace(R.id.fl_contain, Camera2FaceFragment.newInstance()).commit();
                break;
        }
    }


    class MyGridViewAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Log.i("getView", "position--->" + position);
            ViewHolder vh;
            if (convertView == null) {
                vh = new ViewHolder();
                convertView = getActivity().getLayoutInflater().inflate(R.layout.item_grid, null);
                vh.imageView = (ImageView) convertView.findViewById(R.id.item_gv);
                convertView.setTag(vh);
            }
            vh = (ViewHolder) convertView.getTag();
            Log.i("path", "path--->" + mList.get(position));
            ImageLoader.getInstance().displayImage(mList.get(position), vh.imageView, mOptions);
            return convertView;
        }
    }

    class ViewHolder {
        ImageView imageView;
    }

    class MyGridViewItemClick implements AdapterView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            ShowPhotoFragment showPhotoFragment = ShowPhotoFragment.newInstance();
            Bundle bundle = new Bundle();
            bundle.putInt("current", position);
            showPhotoFragment.setArguments(bundle);
            getActivity().getFragmentManager().beginTransaction().replace(R.id.fl_contain, showPhotoFragment).addToBackStack(null).commit();
        }
    }
}
