package com.just.sun.pricecalandar;

import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.widget.TextView;

import java.util.List;

public class PriceCanlendarActivity extends AppCompatActivity {
    private KCalendar calendar;
    private TextView txtv_calendar_month;
    List<GroupDeadline> groups;
    String date = null;// 设置默认选中的日期  格式为 “2015-09-18” 标准DATE格式

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_price_canlendar);
        if (savedInstanceState != null) {
            groups = (List<GroupDeadline>) savedInstanceState.getSerializable("groups");
        }
        txtv_calendar_month = (TextView) findViewById(R.id.txtv_calendar_month);
        calendar = (KCalendar) findViewById(R.id.calendar);
        Intent intent = getIntent();
        groups = (List<GroupDeadline>) intent.getSerializableExtra("proDetail");
        //设置团期
        calendar.setGroups(groups);
        calendar.showCalendar();
        if (TextUtils.isEmpty(date) && groups.size() > 0) {
            date = groups.get(0).getDate();//获取第一天的
            int years = Integer.parseInt(date.substring(0,
                    date.indexOf("-")));
            int month = Integer.parseInt(date.substring(
                    date.indexOf("-") + 1, date.lastIndexOf("-")));
            txtv_calendar_month.setText(years + "年" + month + "月");
            calendar.showCalendar(years, month);//设置初始日子
        }


        //监听所选中的日期
        calendar.setOnCalendarClickListener(new KCalendar.OnCalendarClickListener() {

            public void onCalendarClick(int row, int col, String dateFormat) {

                int month = Integer.parseInt(dateFormat.substring(
                        dateFormat.indexOf("-") + 1,
                        dateFormat.lastIndexOf("-")));

                if (calendar.getCalendarMonth() - month == 1//跨年跳转
                        || calendar.getCalendarMonth() - month == -11) {
                    calendar.lastMonth();

                } else if (month - calendar.getCalendarMonth() == 1 //跨年跳转
                        || month - calendar.getCalendarMonth() == -11) {
                    calendar.nextMonth();

                } else {
                    //                    date = dateFormat;//最后返回给全局 date
                    for (int i = 0; i < groups.size(); i++) {
                        groups.get(i).getDate();
                        int stock = Integer.parseInt(groups.get(i).getStock());
                        Log.i("SHF", "dateFormat--->" + dateFormat + "date--->" + groups.get(i).getDate() + "peopleNumCur--->" + "stock--->" + stock);
                        if (dateFormat.equals(groups.get(i).getDate())
                                && (stock) > 0) {

                            //设置背景色
                            calendar.removeAllBgColor();
                            calendar.setCalendarDayBgColor(dateFormat,
                                    Color.parseColor("#45BDEF"));

                        } else if (date.equals(groups.get(i).getDate())
                                && (stock) > 0) {
                            ToastCommon.toastShortShow(PriceCanlendarActivity.this, null, "此团期剩余空位不足，请选择其他团期或减少参团人数");
                        }
                    }
                }
            }
        });
        calendar.setOnCalendarDateChangedListener(new KCalendar.OnCalendarDateChangedListener() {
            @Override
            public void onCalendarDateChanged(int year, int month) {
                txtv_calendar_month.setText(year + "年" + month + "月");
            }
        });
    }
}
