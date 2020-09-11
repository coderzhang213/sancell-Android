package cn.sancell.xingqiu.dialog;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.view.Window;

import androidx.annotation.NonNull;

import com.aigestudio.wheelpicker.WheelPicker;
import com.aigestudio.wheelpicker.widgets.WheelDayPicker;
import com.aigestudio.wheelpicker.widgets.WheelMonthPicker;
import com.aigestudio.wheelpicker.widgets.WheelYearPicker;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import cn.sancell.xingqiu.R;

/**
 * Created by zj on 2019/7/25.
 */
public class InvitationDatePickerDialog extends Dialog implements WheelPicker.OnItemSelectedListener {
    WheelYearPicker wheelYear;
    WheelMonthPicker wheelMonth;
    WheelDayPicker wheelDay;
    WheelPicker wheelHour, wheelMinute;
    private View btn_ok;
    private int year = 0, month = 0, day = 0, hour = 0, minute = 0;
    private OnTimeSelectLinsener mOnTimeSelectLinsener;

    public InvitationDatePickerDialog(@NonNull Context context) {
        super(context, R.style.DaoxilaDialog_Alert);
        initView(context);
    }

    public void setmOnTimeSelectLinsener(OnTimeSelectLinsener mOnTimeSelectLinsener) {
        this.mOnTimeSelectLinsener = mOnTimeSelectLinsener;

    }

    public void setDateInfo(String year, String month, String day, String hour, String minute) {
        if (!TextUtils.isEmpty(year)) {
            this.year = Integer.parseInt(year);
            this.month = Integer.parseInt(month);
            this.day = Integer.parseInt(day);
            this.hour = Integer.parseInt(hour);
            this.minute = Integer.parseInt(minute);
        }
        initData();
    }

    public void initDefaultTime() {
        Calendar calendar = Calendar.getInstance();
        this.year = calendar.get(Calendar.YEAR); //年
        this.month = calendar.get(Calendar.MONTH) + 1;//月
        this.day = calendar.get(Calendar.DAY_OF_MONTH);//日
        this.hour = calendar.get(Calendar.HOUR_OF_DAY);//时
        this.minute = calendar.get(Calendar.MINUTE);//分
        // int second = calendar.get(Calendar.SECOND);//秒
        initData();
    }

    public InvitationDatePickerDialog(@NonNull Context context, int themeResId) {
        super(context, themeResId);
        initView(context);
    }

    private void initView(Context context) {
        setContentView(R.layout.invitation_datepicker_layout);

        wheelYear = findViewById(R.id.main_wheel_year);
        Window window = getWindow();
        //宽度全屏显示
        window.getDecorView().setMinimumWidth(context.getResources().getDisplayMetrics().widthPixels);
        window.getDecorView().setBackgroundColor(Color.GREEN);
        //靠底部显示
        window.setGravity(Gravity.BOTTOM);
        btn_ok = findViewById(R.id.btn_ok);
        btn_ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                if (mOnTimeSelectLinsener != null) {
                    mOnTimeSelectLinsener.onTimeSelectLinsener(wheelYear.getSelectedYear(), wheelMonth.getSelectedMonth(), wheelDay.getCurrentDay(), wheelHour.getSelectedItemPosition(), wheelMinute.getSelectedItemPosition());
                }
            }
        });
        findViewById(R.id.btn_cancel).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dismiss();
            }
        });
    }

    private void initData() {

        String[] hours = new String[24];
        for (int i = 0; i < 24; i++) {
            hours[i] = i + "";
        }
        List<String> list_hour = Arrays.asList(hours);

        String[] minutes = new String[60];
        for (int i = 0; i < 60; i++) {
            minutes[i] = i + "";
        }
        List<String> list_minute = Arrays.asList(minutes);

        wheelYear = findViewById(R.id.main_wheel_year);
        wheelYear.setOnItemSelectedListener(this);
        wheelYear.setYearFrame(2016, 2049);

        wheelMonth = findViewById(R.id.main_wheel_month);
        wheelMonth.setOnItemSelectedListener(this);

        wheelDay = findViewById(R.id.main_wheel_day);
        wheelDay.setOnItemSelectedListener(this);

        wheelHour = findViewById(R.id.main_hour);
        wheelHour.setOnItemSelectedListener(this);
        wheelHour.setData(list_hour);

        wheelMinute = findViewById(R.id.main_minute);
        wheelMinute.setOnItemSelectedListener(this);
        wheelMinute.setData(list_minute);
        if (year != 0) {
            wheelYear.setSelectedYear(year);
            wheelMonth.setSelectedMonth(month);
            updateWheelDayData();
            wheelDay.setSelectedDay(day);
            wheelHour.setSelectedItemPosition(hour);
            wheelMinute.setSelectedItemPosition(minute);
        }
    }

    @Override
    public void onItemSelected(WheelPicker picker, Object data, int position) {
        switch (picker.getId()) {
            case R.id.main_wheel_year:
                wheelYear.setSelectedYear((Integer) data);
                break;
            case R.id.main_wheel_month:
                wheelMonth.setSelectedMonth((Integer) data);
                break;
            case R.id.main_wheel_day:
                wheelDay.setSelectedDay((Integer) data);
                break;
            case R.id.main_hour:
                wheelHour.setSelectedItemPosition(Integer.valueOf(data + ""));
                break;
            case R.id.main_minute:
                wheelMinute.setSelectedItemPosition(Integer.valueOf(data + ""));
                break;
        }

        if (picker.getId() == R.id.main_wheel_year || picker.getId() == R.id.main_wheel_month) {
            updateWheelDayData();
        }
    }

    private void updateWheelDayData() {
        wheelDay.setYearAndMonth(wheelYear.getSelectedYear(), wheelMonth.getSelectedMonth());
    }

    private int formatDate(String date) {
        int resultD = 0;

        if (!TextUtils.isEmpty(date)) {
            if (date.length() > 1 && date.startsWith("0")) {
                resultD = Integer.valueOf(date.substring(1));
            } else {
                resultD = Integer.valueOf(date);
            }
        }
        return resultD;
    }

    public interface OnTimeSelectLinsener {
        void onTimeSelectLinsener(int year, int month, int day, int hour, int minute);
    }
}
