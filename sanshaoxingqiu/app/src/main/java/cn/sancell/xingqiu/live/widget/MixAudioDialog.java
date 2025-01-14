package cn.sancell.xingqiu.live.widget;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.PopupWindow;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;


import java.io.File;

import cn.sancell.xingqiu.R;
import cn.sancell.xingqiu.live.utils.VcloudFileUtils;

/** 伴音配置对话框
 * Created by hzzhujinbo on 2016/7/20.
 */
public class MixAudioDialog extends PopupWindow {

    public static final String AUDIO_MIX_ACTION = "AudioMix";
    public static final String AUDIO_MIX_VOLUME_ACTION = "AudioMixVolume";
    private boolean mAudioMixOn = false;
    private boolean mAudioMixPause = false;

    private Button startAudioMixBtn, pauseResumeAudioMixBtn, stopAudioMixBtn;
    private Spinner mixAudioFileSpinner;
    private SeekBar mixAudioVolumeBar;
    private TextView mixAudioVolumeTV;

    private int m_mixAudioVolumeProgress = 20;
    private int m_mixAudioVolume = 0;

    private Intent mIntentAudioMix = new Intent(AUDIO_MIX_ACTION);
    private Intent mIntentAudioMixVolume = new Intent(AUDIO_MIX_VOLUME_ACTION);

    private static final String[] mixAudioFileArray = new File(VcloudFileUtils.getMp3FileDir()).list();

    //    private static final String[] mixAudioFileArray = {
//            "yueyunpengchangwuhuanzhige.mp3",
//            "najigejianbingzouba.mp3",
//    };
    private ArrayAdapter<String> adapter;
    private String mixAudioFile;
    private Context mContext;

    public MixAudioDialog(Context context) {

        mContext = context;
        initDialog(context);
    }

    public MixAudioDialog(Context context, Intent intent){
        mContext = context;
        initDialog(context);
        handleIntent(intent);
    }

    /**
     *  从Intent 恢复 MixAudioDialog
     *  add by hzzhukunkun
     * @param intent
     */
    private void handleIntent(Intent intent) {
        if(intent != null){
            int state = intent.getIntExtra("state", 4);
            int volume = intent.getIntExtra("volume", 2);
            String filePath = intent.getStringExtra("filePath");
            switch (state){
                case 1:
                case 2:
                    startAudioMixBtn.callOnClick();
                    break;
                case 3:
                    startAudioMixBtn.callOnClick();
                    mAudioMixPause = false;
                    pauseResumeAudioMixBtn.callOnClick();
                    break;
                case 4:
                    break;
            }
            mixAudioVolumeBar.setProgress(volume*10);
            int index = adapter.getPosition(filePath);
            if(index!=-1){
                mixAudioFileSpinner.setSelection(index);
            }
        }
    }

    private void initDialog(Context context) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View contextView = inflater.inflate(R.layout.mix_audio_layout,null);
        contextView.setBackgroundColor(0xaafff);
        initView(contextView);
        this.setContentView(contextView);
        this.setWidth(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setHeight(ViewGroup.LayoutParams.WRAP_CONTENT);
        this.setFocusable(true);
        this.setTouchable(true);
        this.setOutsideTouchable(true);
        // 刷新状态
        this.update();
        // 实例化一个ColorDrawable颜色为半透明
        ColorDrawable dw = new ColorDrawable(0000000000);
        // 点back键和其他地方使其消失,设置了这个才能触发OnDismisslistener ，设置其他控件变化等操作
        this.setBackgroundDrawable(dw);
        this.setOnDismissListener(new OnDismissListener() {
            @Override
            public void onDismiss() {
                mIntentAudioMix.putExtra("AudioMixMSG", 4);
                mContext.sendBroadcast(mIntentAudioMix);
            }
        });
        // 设置SelectPicPopupWindow弹出窗体动画效果
        this.setAnimationStyle(R.style.AnimationPreview);

    }

    private void initView(View view){
        startAudioMixBtn = (Button) view.findViewById(R.id.StartAudioMixBtn);
        if(!mAudioMixOn)
        {
            startAudioMixBtn.setEnabled(true);
        }
        else
        {
            startAudioMixBtn.setEnabled(false);
        }

        startAudioMixBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                mIntentAudioMix.putExtra("AudioMixMSG", 1);
                mContext.sendBroadcast(mIntentAudioMix);
                mAudioMixOn = true;
                pauseResumeAudioMixBtn.setText("暂停");
                startAudioMixBtn.setEnabled(false);
                stopAudioMixBtn.setEnabled(true);
                pauseResumeAudioMixBtn.setEnabled(true);
            }

        });

        pauseResumeAudioMixBtn = (Button) view.findViewById(R.id.PauseResumeAudioMixBtn);
        if(mAudioMixOn && !mAudioMixPause)
        {
            pauseResumeAudioMixBtn.setText("暂停");
            pauseResumeAudioMixBtn.setEnabled(true);
        }
        else if(mAudioMixOn && mAudioMixPause)
        {
            pauseResumeAudioMixBtn.setText("继续");
            pauseResumeAudioMixBtn.setEnabled(true);
        }
        else
        {
            pauseResumeAudioMixBtn.setText("继续");
            pauseResumeAudioMixBtn.setEnabled(false);
        }

        pauseResumeAudioMixBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                if(!mAudioMixPause)
                {
                    mIntentAudioMix.putExtra("AudioMixMSG", 3);
                    mContext.sendBroadcast(mIntentAudioMix);
                    mAudioMixPause = true;
                    pauseResumeAudioMixBtn.setText("继续");
                    pauseResumeAudioMixBtn.setEnabled(true);
                }
                else
                {
                    mIntentAudioMix.putExtra("AudioMixMSG", 2);
                    mContext.sendBroadcast(mIntentAudioMix);
                    mAudioMixPause = false;
                    pauseResumeAudioMixBtn.setText("暂停");
                    pauseResumeAudioMixBtn.setEnabled(true);
                }
            }

        });

        stopAudioMixBtn = (Button) view.findViewById(R.id.StopAudioMixBtn);
        if(mAudioMixOn)
        {
            stopAudioMixBtn.setEnabled(true);
        }
        else
        {
            stopAudioMixBtn.setEnabled(false);
        }
        stopAudioMixBtn.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v)
            {
                mIntentAudioMix.putExtra("AudioMixMSG", 4);
                mContext.sendBroadcast(mIntentAudioMix);
                mAudioMixOn = false;
                mAudioMixPause = false;
                startAudioMixBtn.setEnabled(true);
                pauseResumeAudioMixBtn.setEnabled(false);
                pauseResumeAudioMixBtn.setText("继续");
                stopAudioMixBtn.setEnabled(false);
            }

        });


        initMixAudioFileSpinner(view);

        mixAudioVolumeBar = (SeekBar)view.findViewById(R.id.mixAudioVolumeBar);
        mixAudioVolumeBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                m_mixAudioVolumeProgress = progress;
                m_mixAudioVolume = progress/10;//0-9
                mixAudioVolumeTV.setText(String.valueOf(progress) + "%");

                mIntentAudioMixVolume.putExtra("AudioMixVolumeMSG", m_mixAudioVolume);
                mContext.sendBroadcast(mIntentAudioMixVolume);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });

        mixAudioVolumeTV = (TextView)view.findViewById(R.id.mixAudioVolumeTV);
        if(mAudioMixOn)
        {
            mixAudioVolumeTV.setText(String.valueOf(m_mixAudioVolumeProgress) + "%");
            mIntentAudioMixVolume.putExtra("AudioMixVolumeMSG", m_mixAudioVolume);
            mixAudioVolumeBar.setProgress(m_mixAudioVolumeProgress);
        }
        else
        {
            mixAudioVolumeTV.setText("20%");
            mIntentAudioMixVolume.putExtra("AudioMixVolumeMSG", 20/10);
            mixAudioVolumeBar.setProgress(20);
        }
    }

    private void initMixAudioFileSpinner(View view)
    {
        mixAudioFileSpinner = (Spinner)view.findViewById(R.id.mixAudioFileSpinner);
        if(Build.VERSION.SDK_INT >=11)
        {
            adapter = new ArrayAdapter<String>(mContext, android.R.layout.simple_spinner_item, mixAudioFileArray);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_item);
            mixAudioFileSpinner.setAdapter(adapter);
            mixAudioFileSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                public void onItemSelected(AdapterView<?> arg0, View arg1, int arg2, long arg3) {
                    mixAudioFile = mixAudioFileArray[arg2];
                    mIntentAudioMix.putExtra("AudioMixFilePathMSG", mixAudioFile);
                    mContext.sendBroadcast(mIntentAudioMix);
                }

                public void onNothingSelected(AdapterView<?> arg0) {}
            });
            mixAudioFileSpinner.setVisibility(View.VISIBLE);
            mixAudioFileSpinner.setSelection(0);
        }else{
            mixAudioFileSpinner.setVisibility(View.GONE);
        }
    }

}
