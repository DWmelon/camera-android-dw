package com.seriousface.m.myapplication.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridLayout;
import android.widget.GridView;

import com.seriousface.m.myapplication.R;
import com.seriousface.m.myapplication.activity.CameraFaceActivity;
import com.seriousface.m.myapplication.adapter.GridViewAdapter;
import com.seriousface.m.myapplication.constant.Constant;
import com.seriousface.m.myapplication.constant.StatConstant;
import com.umeng.analytics.MobclickAgent;

/**
 * Created by Administrator on 2016/4/14.
 */
public class FaceFragment extends Fragment implements AdapterView.OnItemClickListener{

    View contentView;

    GridView gridLayout;
    GridViewAdapter adapter;

    String pageType;
    public static FaceFragment newInstant(Bundle bundle){
        FaceFragment fragment = new FaceFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        contentView = inflater.inflate(R.layout.fragment_face_content,container,false);
        if(getArguments()!=null){
            pageType = getArguments().getString(Constant.KEY_PIC_PAGE_TYPE);
        }

        initView();
        initData();
        return contentView;
    }

    private void initView(){
        gridLayout = (GridView)contentView.findViewById(R.id.gv_face);
        adapter = new GridViewAdapter(getActivity(),pageType);
        gridLayout.setAdapter(adapter);
        gridLayout.setOnItemClickListener(this);
    }

    private void initData(){

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent i = new Intent(getActivity(), CameraFaceActivity.class);
        i.putExtra(Constant.KEY_PIC_CHOOSE_TYPE, Constant.VALUE_PIC_CHOOSE_TYPE_OFFICIAL);
        int ids = (int)parent.getItemAtPosition(position);
        i.putExtra(Constant.KEY_PIC_CHOOSE_DATA, ids);
        getActivity().startActivity(i);

        if(pageType.equals(Constant.VALUE_PIC_PAGE_TYPE_STAR)){
            MobclickAgent.onEvent(getActivity(), StatConstant.PageTypeChooseOfficialStar);
        }else if(pageType.equals(Constant.VALUE_PIC_PAGE_TYPE_XIONG_BEN)){
            MobclickAgent.onEvent(getActivity(), StatConstant.PageTypeChooseOfficialXB);
        }else if(pageType.equals(Constant.VALUE_PIC_PAGE_TYPE_EMOJI)){
            MobclickAgent.onEvent(getActivity(), StatConstant.PageTypeChooseOfficialEmoji);
        }

        MobclickAgent.onEvent(getActivity(), StatConstant.PageTakePhone);

    }
}
