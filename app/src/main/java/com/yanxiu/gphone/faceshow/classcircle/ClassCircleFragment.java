package com.yanxiu.gphone.faceshow.classcircle;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.SimpleItemAnimator;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.yanxiu.gphone.faceshow.R;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseActivity;
import com.yanxiu.gphone.faceshow.base.FaceShowBaseFragment;
import com.yanxiu.gphone.faceshow.classcircle.activity.SendClassCircleActivity;
import com.yanxiu.gphone.faceshow.classcircle.adapter.ClassCircleAdapter;
import com.yanxiu.gphone.faceshow.classcircle.dialog.ClassCircleDialog;
import com.yanxiu.gphone.faceshow.classcircle.mock.MockUtil;
import com.yanxiu.gphone.faceshow.classcircle.response.ClassCircleMock;
import com.yanxiu.gphone.faceshow.customview.LoadMoreRecyclerView;
import com.yanxiu.gphone.faceshow.customview.PublicLoadLayout;
import com.yanxiu.gphone.faceshow.customview.SizeChangeCallbackView;
import com.yanxiu.gphone.faceshow.homepage.activity.MainActivity;
import com.yanxiu.gphone.faceshow.login.UserInfo;
import com.yanxiu.gphone.faceshow.permission.OnPermissionCallback;
import com.yanxiu.gphone.faceshow.util.ClassCircleTimeUtils;
import com.yanxiu.gphone.faceshow.util.FileUtil;
import com.yanxiu.gphone.faceshow.util.Logger;
import com.yanxiu.gphone.faceshow.util.ScreenUtils;
import com.yanxiu.gphone.faceshow.util.ToastUtil;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

/**
 * 首页 “班级圈”Fragment
 */
public class ClassCircleFragment extends FaceShowBaseFragment implements LoadMoreRecyclerView.LoadMoreListener, View.OnClickListener, ClassCircleAdapter.onCommentClickListener, View.OnLongClickListener, View.OnKeyListener, ClassCircleAdapter.onThumbClickListener, SwipeRefreshLayout.OnRefreshListener {

    private static final int REQUEST_CODE_ALBUM=0x000;
    private static final int REQUEST_CODE_CAMERA=0x001;

    private LoadMoreRecyclerView mClassCircleRecycleView;
    private ClassCircleAdapter mClassCircleAdapter;
    private RelativeLayout mCommentLayout;
    private EditText mCommentView;
    private SizeChangeCallbackView mAdjustPanView;
    private ImageView mFunctionView;
    private TextView mTitleView;
    private View mTopView;
    private int mCommentPosition=-1;
    private int mVisibility=4;
    private int mHeight;
    private String mCameraPath;
    private ClassCircleDialog mClassCircleDialog;
    private SwipeRefreshLayout mRefreshView;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        PublicLoadLayout rootView = new PublicLoadLayout(getContext());
        rootView.setContentView(R.layout.fragment_classcircle);
        initView(rootView);
        listener();
        initData();
        return rootView;
    }

    private void initView(View rootView) {
        mTopView = rootView.findViewById(R.id.il_title);
        ImageView mBackView = (ImageView) rootView.findViewById(R.id.title_layout_left_img);
        mBackView.setVisibility(View.INVISIBLE);
        mTitleView = (TextView) rootView.findViewById(R.id.title_layout_title);
        mFunctionView = (ImageView) rootView.findViewById(R.id.title_layout_right_img);
        mFunctionView.setVisibility(View.VISIBLE);

        mCommentLayout = (RelativeLayout) rootView.findViewById(R.id.ll_edit);
        mCommentView = (EditText) rootView.findViewById(R.id.ed_comment);
        mAdjustPanView = (SizeChangeCallbackView) rootView.findViewById(R.id.sc_adjustpan);
        mClassCircleRecycleView = (LoadMoreRecyclerView) rootView.findViewById(R.id.lm_class_circle);
        mClassCircleRecycleView.setLayoutManager(new LinearLayoutManager(getContext()));
        mClassCircleAdapter = new ClassCircleAdapter(getContext());
        mClassCircleRecycleView.setAdapter(mClassCircleAdapter);

        mRefreshView= (SwipeRefreshLayout) rootView.findViewById(R.id.srl_refresh);
    }

    private void listener() {
        mClassCircleRecycleView.setLoadMoreListener(ClassCircleFragment.this);
        mClassCircleAdapter.setCommentClickListener(ClassCircleFragment.this);
        mClassCircleAdapter.setThumbClickListener(ClassCircleFragment.this);
        mFunctionView.setOnClickListener(ClassCircleFragment.this);
        mFunctionView.setOnLongClickListener(ClassCircleFragment.this);
        mCommentView.setOnKeyListener(ClassCircleFragment.this);
        mRefreshView.setOnRefreshListener(ClassCircleFragment.this);
    }

    private void initData() {
        mTopView.setBackgroundColor(Color.parseColor("#e6ffffff"));
        mTitleView.setText(R.string.classcircle);
        mFunctionView.setBackgroundResource(R.mipmap.ic_launcher);
        mClassCircleRecycleView.getItemAnimator().setChangeDuration(0);
        mClassCircleRecycleView.setLoadMoreEnable(true);
        mClassCircleAdapter.setData(MockUtil.getClassCircleMockList());
        mRefreshView.setProgressViewOffset(false, ScreenUtils.dpToPxInt(getContext(),44),ScreenUtils.dpToPxInt(getContext(),100));
    }

    @Override
    public void onRefresh() {
        final Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                timer.cancel();
            }
        },2000);
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            mRefreshView.setRefreshing(false);
            mClassCircleRecycleView.setLoadMoreEnable(true);
        }
    };

    @Override
    public void onLoadMore(LoadMoreRecyclerView refreshLayout) {
        ToastUtil.showToast(getContext(),"加载更多");
        final Timer timer=new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                handler.sendEmptyMessage(0);
                timer.cancel();
            }
        },2000);
    }

    @Override
    public void onLoadmoreComplte() {

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.title_layout_right_img:
                showDialog();
                break;
        }
    }

    private void showDialog(){
        if (mClassCircleDialog==null) {
            mClassCircleDialog = new ClassCircleDialog(getContext());
            mClassCircleDialog.setClickListener(new ClassCircleDialog.OnViewClickListener() {
                @Override
                public void onAlbumClick() {
                    FaceShowBaseActivity.requestWriteAndReadPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                            Intent intent = new Intent();
                            intent.setType("image/*");
                            intent.setAction(Intent.ACTION_GET_CONTENT);
                            startActivityForResult(intent, REQUEST_CODE_ALBUM);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            ToastUtil.showToast(getContext(),R.string.no_storage_permissions);
                        }
                    });
                }

                @Override
                public void onCameraClick() {
                    FaceShowBaseActivity.requestCameraPermission(new OnPermissionCallback() {
                        @Override
                        public void onPermissionsGranted(@Nullable List<String> deniedPermissions) {
                            mCameraPath = FileUtil.getImageCatchPath(System.currentTimeMillis()+".jpg");
                            Intent intent = new Intent();
                            intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
                            intent.addCategory(Intent.CATEGORY_DEFAULT);
                            File file = new File(mCameraPath);
                            if (file.exists()) {
                                try {
                                    file.createNewFile();
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                            }
                            Uri uri = Uri.fromFile(file);
                            intent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                            startActivityForResult(intent, REQUEST_CODE_CAMERA);
                        }

                        @Override
                        public void onPermissionsDenied(@Nullable List<String> deniedPermissions) {
                            ToastUtil.showToast(getContext(),R.string.no_camera_permissions);
                        }
                    });
                }
            });
        }
        mClassCircleDialog.show();
    }

    @Override
    public void commentClick(final int position, ClassCircleMock mock, ClassCircleMock.Comment comment, boolean isCommentMaster) {
        Toast.makeText(getContext(), "评论"+position, Toast.LENGTH_SHORT).show();

        Logger.d("onSizeChanged","commentClick");
        mCommentPosition=position;
        mCommentLayout.setVisibility(View.VISIBLE);
        mCommentView.setFocusable(true);
        mCommentView.clearFocus();
        mCommentView.requestFocus();
        if (mVisibility==View.VISIBLE){
            setScroll(position,mHeight);
        }
        mAdjustPanView.setViewSizeChangedCallback(new SizeChangeCallbackView.onViewSizeChangedCallback() {
            @Override
            public void sizeChanged(int visibility, int height) {

                Logger.d("onSizeChanged","visibility  "+visibility);
                mVisibility=visibility;
                if (visibility == View.VISIBLE) {
                    mHeight=height;
                    ((MainActivity) getActivity()).setBottomVisibility(View.GONE);
                    setScroll(position,height);
                } else {
                    ((MainActivity) getActivity()).setBottomVisibility(View.VISIBLE);
                }
            }
        });
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(mCommentView,0);
    }

    /**
     * 将选中item滚动到可见位置
     * */
    private void setScroll(final int position, final int height){
        Logger.d("mClassCircleRecycleView","adapter  position  "+position);
        mClassCircleRecycleView.scrollToPosition(position);
//        ((LinearLayoutManager)mClassCircleRecycleView.getLayoutManager()).scrollToPositionWithOffset(position,0);
        ClassCircleTimeUtils.creat().start(new ClassCircleTimeUtils.onTimeUplistener() {
            @Override
            public void onTimeUp() {
                setSrcollBy(position, height);
            }
        });
    }

    /**
     * 检查选中item位置进行微调
     * */
    private void setSrcollBy(final int position, final int height){
        int visibleIndex=((LinearLayoutManager)mClassCircleRecycleView.getLayoutManager()).findFirstVisibleItemPosition();
        int n = position - visibleIndex;
        Logger.d("mClassCircleRecycleView","visibile position  "+visibleIndex);
        Logger.d("mClassCircleRecycleView","position  "+n);
        if ( 0 <= n && n < mClassCircleRecycleView.getChildCount()) {
            int top = mClassCircleRecycleView.getChildAt(n).getTop();
            int bottom=mClassCircleRecycleView.getChildAt(n).getBottom();

            Logger.d("mClassCircleRecycleView","top "+top);
            Logger.d("mClassCircleRecycleView","bottom "+bottom);
            Logger.d("mClassCircleRecycleView","height "+height);

            final int heightMove=bottom-height;
            if (heightMove!=0&&bottom!=height) {
                Logger.d("mClassCircleRecycleView","heightMoves "+heightMove);
                mClassCircleRecycleView.post(new Runnable() {
                    @Override
                    public void run() {
                        mClassCircleRecycleView.scrollBy(0, heightMove);
                    }
                });
            }
        }
        Logger.d("mClassCircleRecycleView"," ");
    }

    @Override
    public void commentFinish() {
        mVisibility=View.INVISIBLE;
        mAdjustPanView.setViewSizeChangedCallback(null);
        mCommentLayout.setVisibility(View.GONE);
        InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mAdjustPanView.getWindowToken(), 0);
        ((MainActivity) getActivity()).setBottomVisibility(View.VISIBLE);
        mCommentPosition=-1;
    }

    @Override
    public boolean onLongClick(View v) {
        SendClassCircleActivity.LuanchActivity(getContext(),SendClassCircleActivity.TYPE_TEXT,null);
        return true;
    }

    @Override
    public boolean onKey(View v, int keyCode, KeyEvent event) {
        if (keyCode== event.getKeyCode()&&event.getAction()==KeyEvent.ACTION_UP){
            String comment=mCommentView.getText().toString();
            ClassCircleMock mock=mClassCircleAdapter.getDataFromPosition(mCommentPosition);
            ClassCircleMock.Comment commentMock=mock.new Comment();
            commentMock.content=comment;
            commentMock.userId=UserInfo.getInstance().getInfo().getUserId();
            commentMock.userName=UserInfo.getInstance().getInfo().getUserName();
            mock.comments.add(commentMock);
            mClassCircleAdapter.notifyItemChanged(mCommentPosition);
            mCommentView.setText("");
            commentFinish();
            return true;
        }
        return false;
    }

    @Override
    public void thumbClick(int position, ClassCircleMock mock) {
        Toast.makeText(getContext(), "点赞"+position, Toast.LENGTH_SHORT).show();
        ClassCircleMock.ThumbUp thumbUp=mock.new ThumbUp();
        thumbUp.userName= UserInfo.getInstance().getInfo().getUserName();
        thumbUp.userId=UserInfo.getInstance().getInfo().getUserId();
        mock.thumbs.add(thumbUp);
        mClassCircleAdapter.notifyItemChanged(position);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case REQUEST_CODE_ALBUM:
                if (data!=null) {
                    Uri uri = data.getData();
                    String path=FileUtil.getRealFilePath(getContext(),uri);
                    if (!TextUtils.isEmpty(path)){
                        startIntent(path);
                    }
                }
                break;
            case REQUEST_CODE_CAMERA:
                if (mCameraPath!=null){
                    startIntent(mCameraPath);
                }
                break;
        }
    }

    private void startIntent(String path){
        ArrayList<String> strings=new ArrayList<>();
        strings.add(path);
        SendClassCircleActivity.LuanchActivity(getContext(),SendClassCircleActivity.TYPE_IMAGE,strings);
    }

}
