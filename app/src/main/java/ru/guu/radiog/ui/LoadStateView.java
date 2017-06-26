package ru.guu.radiog.ui;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;

import ru.guu.radiog.R;

/**
 * Created by dmitry on 26.06.17.
 */

public class LoadStateView extends FrameLayout {

    private OnReloadListener listener;
    private String header;
    private String description;
    private String emptyHeader;
    private String emptyDescription;
    private ImageButton reloadButton;
    private TextView headerView;
    private TextView descriptionView;
    private ProgressBar progressBar;

    public LoadStateView(Context context) {
        super(context, null);
    }

    public LoadStateView(Context context, AttributeSet attrs) {
        super(context, attrs, 0);

        LayoutInflater inflater = (LayoutInflater)context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.view_load_state, this, true);
        ViewGroup viewGroup = (ViewGroup)getChildAt(0);
        reloadButton = (ImageButton)viewGroup.getChildAt(0);
        headerView = (TextView)viewGroup.getChildAt(1);
        descriptionView = (TextView)viewGroup.getChildAt(2);
        progressBar = (ProgressBar)viewGroup.getChildAt(3);
        reloadButton.setOnClickListener((View v) -> {
            showProgress();
            hideText();
            hideReloadButton();
            if (listener != null)
                listener.onReload();
        });
    }

    public void setOnReloadListener(OnReloadListener listener) {
        this.listener = listener;
    }

    public void setHintHeader(String header) {
        this.header = header;
    }

    public void setHintDescription(String description) {
        this.description = description;
    }

    public void setEmptyHeader(String header) {
        emptyHeader = header;
    }

    public void setEmptyDescription(String description) {
        emptyDescription = description;
    }

    @Deprecated
    public void setText(String header, String description) {
        this.header = header;
        this.description = description;
    }

    private void setErrorHint() {
        headerView.setText(getContext().getString(R.string.state_error));
        descriptionView.setText(getContext().getString(R.string.state_error_description));
    }

    private void setHint() {
        headerView.setText(header);
        descriptionView.setText(description);
    }

    private void setEmptyHint() {
        headerView.setText(emptyHeader);
        descriptionView.setText(emptyDescription);
    }


    private void showReloadButton() {
        reloadButton.setVisibility(VISIBLE);
    }

    private void hideReloadButton() {
        reloadButton.setVisibility(GONE);
    }

    private void hideText() {
        headerView.setVisibility(GONE);
        descriptionView.setVisibility(GONE);
    }

    public void hide() {
        progressBar.setVisibility(GONE);
        reloadButton.setVisibility(GONE);
        headerView.setVisibility(GONE);
        descriptionView.setVisibility(GONE);
    }

    public void hideProgress() {
        progressBar.setVisibility(GONE);
    }

    public void showProgress() {
        progressBar.setVisibility(VISIBLE);
        hideText();
        hideReloadButton();
    }

    public void showHint() {
        setHint();
        hideProgress();
        hideReloadButton();
        headerView.setVisibility(VISIBLE);
        descriptionView.setVisibility(VISIBLE);
    }

    public void showErrorHint() {
        setErrorHint();
        hideProgress();
        showReloadButton();
        headerView.setVisibility(VISIBLE);
        descriptionView.setVisibility(VISIBLE);
    }

    public void showEmptyHint() {
        setEmptyHint();
        hideProgress();
        headerView.setVisibility(VISIBLE);
        descriptionView.setVisibility(VISIBLE);
    }

    public boolean isLoading() {
        return progressBar.getVisibility() == VISIBLE;
    }

    public interface OnReloadListener {
        void onReload();
    }

}
