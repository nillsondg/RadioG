package ru.guu.radiog.ui;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.database.ContentObserver;
import android.graphics.Outline;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewOutlineProvider;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.squareup.picasso.Picasso;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import ru.guu.radiog.R;
import ru.guu.radiog.network.ApiFactory;
import ru.guu.radiog.network.model.ItunesSong;
import ru.guu.radiog.network.model.LastFmTrack;

/**
 * control radio stream and audio volume
 */
public class RadioFragment extends Fragment {
    private final String KEY_RADIO_STARTED = "key_radio_started";
    private final String KEY_STREAM_TITLE = "key_stream_title";

    private ToggleButton playButton;
    private boolean buttonPressed = false;
    private boolean started = false;
    private SeekBar seekBar;
    private AudioManager audioManager;
    private ContentObserver mContentObserver;
    private ConstraintLayout mConstraintLayout;
    private ImageView mStreamImage;
    private RadioController mListener;
    private TextView mStreamTitle;
    private ImageButton volumeUpButton;
    private ImageButton volumeDownButton;

    private Disposable mDisposable;
    private Disposable mDisposableItunes;

    public static RadioFragment newInstance() {
        return new RadioFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_radio, container, false);
        mConstraintLayout = (ConstraintLayout)view.findViewById(R.id.constraint_layout);
        mStreamImage = (ImageView)view.findViewById(R.id.stream_image);
        mStreamTitle = (TextView)view.findViewById(R.id.stream_title);
        seekBar = (SeekBar)view.findViewById(R.id.seekBar);
        playButton = (ToggleButton)view.findViewById(R.id.playButton);
        volumeDownButton = (ImageButton)view.findViewById(R.id.buttonVolumeDown);
        volumeUpButton = (ImageButton)view.findViewById(R.id.buttonVolumeUp);

        initVolumeControllers();
        initStreamImageAnimation();
        initPlayButton();

        return view;
    }

    private void initVolumeControllers() {
        audioManager = (AudioManager)getContext().getSystemService(Context.AUDIO_SERVICE);
        seekBar.setMax(audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC));
        seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onStopTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar arg0) {
            }

            @Override
            public void onProgressChanged(SeekBar arg0, int progress, boolean arg2) {
                audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, progress, 0);
            }
        });

        mContentObserver = new VolumeContentObserver(getContext(), new Handler());

        volumeDownButton.setOnClickListener((View v) -> {
            seekBar.setProgress(Math.max(0, seekBar.getProgress() - 1));
        });

        volumeUpButton.setOnClickListener((View v) -> {
            int maxVolume = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC);
            seekBar.setProgress(Math.min(maxVolume, seekBar.getProgress() + 1));
        });
    }

    /**
     * apple music like animation
     */
    private void initStreamImageAnimation() {
        if (Build.VERSION.SDK_INT >= 21) {
            mStreamImage.setClipToOutline(true);
            ViewOutlineProvider viewOutlineProvider = new ViewOutlineProvider() {
                @Override
                public void getOutline(View view, Outline outline) {
                    if (Build.VERSION.SDK_INT >= 21) {
                        outline.setRoundRect(0, 0, mStreamImage.getWidth(), mStreamImage.getHeight(), 16);
                        outline.setAlpha(0.5f);
                    }
                }
            };
            mStreamImage.setOutlineProvider(viewOutlineProvider);
        }
    }

    private void initPlayButton() {
        playButton.setOnCheckedChangeListener((CompoundButton compoundButton, boolean checked) -> {
            buttonPressed = true;
            if (checked && mListener != null && !mListener.isPlaying()) {
                mListener.startRadio();
            } else if (!checked && mListener != null && mListener.isPlaying()) {
                mListener.stopRadio();
            }
        });
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mListener = (RadioController)context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(KEY_RADIO_STARTED, started);
        outState.putCharSequence(KEY_STREAM_TITLE, mStreamTitle.getText());
    }

    @Override
    public void onViewStateRestored(@Nullable Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        if (savedInstanceState != null) {
            started = savedInstanceState.getBoolean(KEY_RADIO_STARTED);
            mStreamTitle.setText(savedInstanceState.getCharSequence(KEY_STREAM_TITLE));
        }
    }


    @Override
    public void onResume() {
        super.onResume();
        if (started) {
            playButton.setChecked(mListener.isPlaying());
            if (mListener.isPlaying()) {
                animateImageUp();
            } else {
                animateImageDown();
            }
            seekBar.setProgress(audioManager.getStreamVolume(AudioManager.STREAM_MUSIC));
        }

        getContext().getContentResolver().registerContentObserver(android.provider.Settings.System.CONTENT_URI, true, mContentObserver);
        findArtImage();
    }

    @Override
    public void onStop() {
        super.onStop();
        getContext().getContentResolver().unregisterContentObserver(mContentObserver);
        if (mDisposable != null)
            mDisposable.dispose();
    }

    public void setStreamTitle(String streamTitle) {
        mStreamTitle.setText(streamTitle);
        findArtImage();
    }

    public void findArtImage() {
        String streamTitle = mStreamTitle.getText().toString();
        if (streamTitle.isEmpty())
            return;
        String[] parts = streamTitle.split("-");
        if (parts.length < 2)
            return;
        String artist = parts[0].trim();
        String track = parts[1].trim();
        findArtImageByItunes(artist, track);

    }

    private void findArtImageByItunes(String artist, String track) {
        mDisposableItunes = ApiFactory.getItunesService().getSongs(artist + " " + track, "song")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getResultCount() > 0) {
                                ItunesSong song = result.getResults().get(0);
                                Picasso.with(getContext()).load(song.getArtworkUrl600()).into(mStreamImage);
                                mListener.updateNotification(artist + " - " + track, song.getArtworkUrl600());
                            } else {
                                findArtImageByLastFm(artist, track);
                            }
                        }
                );
    }

    private void findArtImageByLastFm(String artist, String track) {
        mDisposable = ApiFactory.getLastFmService().getSongs("track.getInfo", getString(R.string.lastfm_api_key), artist, track, "json")
                .subscribeOn(Schedulers.newThread())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(result -> {
                            if (result.getTrack() != null && result.getTrack().getAlbum() != null
                                    && result.getTrack().getAlbum().getImages() != null) {
                                LastFmTrack song = result.getTrack();
                                String artUrl = song.getAlbum().getImages().get(4).getImageUrl();
                                Picasso.with(getContext()).load(artUrl).into(mStreamImage);
                                mListener.updateNotification(artist + " - " + track, artUrl);
                            } else {
                                mStreamImage.setImageDrawable(ContextCompat.getDrawable(getContext(), R.drawable.radiog_logo));
                            }
                        }
                );
    }

    public void onRadioStarted() {
        animateImageUp();
        started = true;
        if (buttonPressed && !playButton.isChecked() && mListener != null) {
            mListener.stopRadio();
        } else {
            playButton.setChecked(true);
        }
        buttonPressed = false;

    }

    private void animateImageUp() {
        if (Build.VERSION.SDK_INT >= 21) {
            ObjectAnimator.ofFloat(mStreamImage, View.TRANSLATION_Z, mStreamImage.getTranslationZ(), 40).start();
        }
        ObjectAnimator.ofFloat(mStreamImage, View.TRANSLATION_Y, mStreamImage.getTranslationY(), -8).start();
        ObjectAnimator.ofFloat(mStreamImage, View.SCALE_X, mStreamImage.getScaleX(), 1.05f).start();
        ObjectAnimator.ofFloat(mStreamImage, View.SCALE_Y, mStreamImage.getScaleY(), 1.05f).start();

    }

    private void animateImageDown() {
        if (Build.VERSION.SDK_INT >= 21) {
            ObjectAnimator.ofFloat(mStreamImage, View.TRANSLATION_Z, mStreamImage.getTranslationZ(), 0).start();
        }
        ObjectAnimator.ofFloat(mStreamImage, View.TRANSLATION_Y, mStreamImage.getTranslationY(), 0).start();
        ObjectAnimator.ofFloat(mStreamImage, View.SCALE_X, mStreamImage.getScaleX(), 1.0f).start();
        ObjectAnimator.ofFloat(mStreamImage, View.SCALE_Y, mStreamImage.getScaleY(), 1.0f).start();

    }

    public void onRadioStopped() {
        animateImageDown();
        if (buttonPressed && playButton.isChecked() && mListener != null) {
            mListener.startRadio();
        } else {
            playButton.setChecked(false);
        }
        buttonPressed = false;
    }

    public void onError() {
        Toast.makeText(getContext(), R.string.error, Toast.LENGTH_SHORT).show();
        playButton.setChecked(false);
    }

    public interface RadioController {
        void startRadio();

        void stopRadio();

        boolean isPlaying();

        void updateNotification(String songName, String artUrl);

        void resetNotification(String songName);
    }

    public class VolumeContentObserver extends ContentObserver {
        private final AudioManager audioManager;

        VolumeContentObserver(Context context, Handler handler) {
            super(handler);
            audioManager = (AudioManager)context.getSystemService(Context.AUDIO_SERVICE);
        }

        @Override
        public boolean deliverSelfNotifications() {
            return false;
        }

        @Override
        public void onChange(boolean selfChange) {
            int currentVolume = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC);
            seekBar.setProgress(currentVolume);
        }
    }
}
