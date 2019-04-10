
package com.huxiaozhu.recyclerviewlib.adapter.viewholder;

import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.IdRes;
import androidx.recyclerview.widget.RecyclerView;

/**
 * Created by liuxiaozhu on 2017/4/26.
 * All Rights Reserved by YiZu
 * ViewHoloder的封装
 */

public class BaseViewHolder extends RecyclerView.ViewHolder{
    /**
     * 存储每个item中的子控件（牺牲了一定的内存，但是不用每次都需要findviewbyid）
     */
    private SparseArray<View> mSparseArray = new SparseArray<>();
    /**
     * 用来接收每个item对应的view
     */
    private View mView;

    protected BaseViewHolder(View view) {
        super(view);//必须实现
        this.mView = view;
    }

    public BaseViewHolder(int mLayoutId, ViewGroup parent) {
        this(LayoutInflater.from(parent.getContext()).inflate(mLayoutId, parent, false));
    }

    /**
     * 获取item的view
     *
     * @return
     */
    public View getItemView() {
        return mView;
    }

    /**
     * View中找到viewId对应的控件
     *
     * @param viewId
     * @param <T>
     * @return
     */
    private <T extends View> T findView(@IdRes int viewId) {
        View view = null;
        if (mView != null) {
            view = mView.findViewById(viewId);
        }
        if (view != null) {
            mSparseArray.put(viewId, view);
        }
        return (T) view;
    }

    /**
     * 获取某个控件的view
     *
     * @param viewId
     * @param <T>
     * @return
     */
    public <T extends View> T getView(@IdRes int viewId) {
        View view = mSparseArray.get(viewId);
        if (view != null) {
            return (T) view;
        } else {
            view = findView(viewId);
            return (T) view;
        }
    }
    /**
     * 根据id获取TextView
     *
     * @param viewId
     * @return
     */
    public TextView getTextView(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id获取Imageview
     *
     * @param viewId
     * @return
     */
    public ImageView getImageView(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id获取LinearLayout
     *
     * @param viewId
     * @return
     */
    public LinearLayout getLinearLayout(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id获取RelativeLayout
     *
     * @param viewId
     * @return
     */
    public RelativeLayout getRelativeLayout(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id获取Button
     *
     * @param viewId
     * @return
     */
    public Button getButton(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * 根据id获取EditText
     *
     * @param viewId
     * @return
     */
    public EditText getEditText(@IdRes int viewId) {
        return getView(viewId);
    }

    /**
     * 设置图片的高
     *
     * @param viewId
     * @param height
     */
    public void setImageHeight(@IdRes int viewId, int height) {
        ImageView view = getView(viewId);
        if (view != null) {
            ViewGroup.LayoutParams para = view.getLayoutParams();
            para.height = height;
            view.setLayoutParams(para);
        }
    }

    /**
     * 设置图片的宽
     *
     * @param viewId
     * @param width
     */
    public void setImageWidth(@IdRes int viewId, int width) {
        ImageView view = getView(viewId);
        if (view != null) {
            ViewGroup.LayoutParams para = view.getLayoutParams();
            para.width = width;
            view.setLayoutParams(para);
        }
    }

    public RecyclerView getRecyclerView(@IdRes int viewId) {
        return getView(viewId);
    }
}
