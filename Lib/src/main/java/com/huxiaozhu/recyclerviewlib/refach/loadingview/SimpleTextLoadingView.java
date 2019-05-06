package com.huxiaozhu.recyclerviewlib.refach.loadingview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.TextView;

import androidx.annotation.AttrRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.huxiaozhu.recyclerviewlib.R;
import com.huxiaozhu.recyclerviewlib.refach.SDPullToRefreshView;

import static com.huxiaozhu.recyclerviewlib.refach.ISDPullToRefreshView.*;


public class SimpleTextLoadingView extends SDPullToRefreshLoadingView {
    private TextView tv_content;

    public SimpleTextLoadingView(@NonNull Context context) {
        super(context);
        this.init();
    }

    public SimpleTextLoadingView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public SimpleTextLoadingView(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        LayoutInflater.from(this.getContext()).inflate(R.layout.view_simple_text_loading, this, true);
        this.tv_content = (TextView)this.findViewById(R.id.tv_content);
    }

    public TextView getTextView() {
        return this.tv_content;
    }

    public void onStateChanged(State newState, State oldState, SDPullToRefreshView view) {
        switch(newState) {
            case RESET:
            case PULL_TO_REFRESH:
                if (this.getLoadingViewType() == LoadingViewType.HEADER) {
                    this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_pull_to_refresh_header));
                } else if (this.getLoadingViewType() == LoadingViewType.FOOTER) {
                    this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_pull_to_refresh_footer));
                }
                break;
            case RELEASE_TO_REFRESH:
                if (this.getLoadingViewType() == LoadingViewType.HEADER) {
                    this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_release_to_refresh_header));
                } else if (this.getLoadingViewType() == LoadingViewType.FOOTER) {
                    this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_release_to_refresh_footer));
                }
                break;
            case REFRESHING:
                if (this.getLoadingViewType() == LoadingViewType.HEADER) {
                    this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_refreshing_header));
                } else if (this.getLoadingViewType() == LoadingViewType.FOOTER) {
                    this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_refreshing_footer));
                }
                break;
            case REFRESH_SUCCESS:
                if (this.getLoadingViewType() == LoadingViewType.HEADER) {
                    this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_refreshing_success_header));
                } else if (this.getLoadingViewType() == LoadingViewType.FOOTER) {
                    this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_refreshing_success_footer));
                }
                break;
            case REFRESH_FAILURE:
                if (this.getLoadingViewType() == LoadingViewType.HEADER) {
                    this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_refreshing_failure_header));
                } else if (this.getLoadingViewType() == LoadingViewType.FOOTER) {
                    this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_refreshing_failure_footer));
                }
                break;
            case REFRESH_FINISH:
                if (oldState == State.REFRESHING) {
                    if (this.getLoadingViewType() == LoadingViewType.HEADER) {
                        this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_pull_to_refresh_header));
                    } else if (this.getLoadingViewType() == LoadingViewType.FOOTER) {
                        this.getTextView().setText(this.getResources().getString(R.string.lib_ptr_state_pull_to_refresh_footer));
                    }
                }
        }

    }
}

