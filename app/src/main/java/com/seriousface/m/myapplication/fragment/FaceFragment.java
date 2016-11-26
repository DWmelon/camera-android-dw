package com.seriousface.m.myapplication.fragment;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.seriousface.m.myapplication.R;
import com.seriousface.m.myapplication.activity.Camera2FaceActivity;
import com.seriousface.m.myapplication.view.DividerGridItemDecoration;
import com.seriousface.m.myapplication.activity.CameraFaceActivity;
import com.seriousface.m.myapplication.adapter.FaceAdapter;
import com.seriousface.m.myapplication.constant.Constant;
import com.seriousface.m.myapplication.constant.StatConstant;
import com.umeng.analytics.MobclickAgent;


/**
 * Created by Administrator on 2016/4/14.
 */
public class FaceFragment extends Fragment{

    private View contentView;

    private RecyclerView mRvFace;
    private FaceAdapter faceAdapter;
    private String pageType;
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
        mRvFace = (RecyclerView)contentView.findViewById(R.id.rv_face);

        mRvFace.setLayoutManager(new StaggeredGridLayoutManager(3,StaggeredGridLayoutManager.VERTICAL));
        faceAdapter = new FaceAdapter(getActivity(),pageType);
        mRvFace.setAdapter(faceAdapter);

        mRvFace.addItemDecoration(new DividerGridItemDecoration(getActivity()));

    }

    private void initData(){
        faceAdapter.setListener(new FaceAdapter.FaceListener() {
            @Override
            public void onItemClick(View view, int redId) {
                Intent i = null;
                if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
                    // your code using Camera API here - is between 1-20
                    i = new Intent(getActivity(), CameraFaceActivity.class);
                } else if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    // your code using Camera2 API here - is api 21 or higher
                    i = new Intent(getActivity(), Camera2FaceActivity.class);
                }

                i.putExtra(Constant.KEY_PIC_CHOOSE_TYPE, Constant.VALUE_PIC_CHOOSE_TYPE_OFFICIAL);
                i.putExtra(Constant.KEY_PIC_CHOOSE_DATA, redId);
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
        });
    }

}

