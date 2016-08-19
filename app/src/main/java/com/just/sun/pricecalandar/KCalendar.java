package com.just.sun.pricecalandar;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.ViewFlipper;



/**
 * 日历控件
 *
 * @author huangyin
 */
@SuppressWarnings("deprecation")
public class KCalendar extends ViewFlipper implements
        GestureDetector.OnGestureListener {
    //    public static final int COLOR_BG_WEEK_TITLE = Color.parseColor("#ffeeeeee"); // 星期标题背景颜色
    //    public static final int COLOR_TX_WEEK_TITLE = Color.parseColor("#ffcc3333"); // 星期标题文字颜色
    //    public static final int COLOR_TX_THIS_MONTH_DAY = Color.parseColor("#aa564b4b"); // 当前月日历数字颜色
    //    public static final int COLOR_TX_OTHER_MONTH_DAY = Color.parseColor("#ffcccccc"); // 其他月日历数字颜色
    //    public static final int COLOR_TX_THIS_DAY = Color.parseColor("#ff008000"); // 当天日历数字颜色
    //    public static final int COLOR_BG_THIS_DAY = Color.parseColor("#ffcccccc"); // 当天日历背景颜色
    //    public static final int COLOR_BG_CALENDAR = Color.parseColor("#ffeeeeee"); // 日历背景色
    public static final int COLOR_BG_WEEK_TITLE = Color.parseColor("#00000000"); // 星期标题背景颜色
    public static final int COLOR_TX_WEEK_TITLE = Color.parseColor("#879298"); // 星期标题文字颜色
    public static final int COLOR_TX_THIS_MONTH_DAY = Color.parseColor("#aa564b4b"); // 当前月日历数字颜色
    public static final int COLOR_TX_OTHER_MONTH_DAY = Color.parseColor("#ffcccccc"); // 其他月日历数字颜色
    public static final int COLOR_TX_THIS_DAY = Color.parseColor("#ff008000"); // 当天日历数字颜色
    public static final int COLOR_BG_THIS_DAY = Color.parseColor("#ffcccccc"); // 当天日历背景颜色
    public static final int COLOR_BG_CALENDAR = Color.parseColor("#00000000"); // 日历背景色
    public static final int COLOR_TX_PRICE = Color.parseColor("#ff991d");//橙色的价格颜色

    private GestureDetector gd; // 手势监听器
    private Animation push_left_in; // 动画-左进
    private Animation push_left_out; // 动画-左出
    private Animation push_right_in; // 动画-右进
    private Animation push_right_out; // 动画-右出

    private int ROWS_TOTAL = 6; // 日历的行数
    private int COLS_TOTAL = 7; // 日历的列数
    private String[][] dates = new String[6][7];
    private float tb;

    private OnCalendarClickListener onCalendarClickListener; // 日历点击回调
    private OnCalendarDateChangedListener onCalendarDateChangedListener; // 日历点击回调

    private String[] weekday = new String[]{"日", "一", "二", "三", "四", "五", "六"}; // 星期标题

    private int calendarYear; // 日历年份
    private int calendarMonth; // 日历月份
    private Date thisday = new Date(); // 今天
    private Date calendarday; // 日历这个月第一天(1号)

    private LinearLayout firstCalendar; // 第一个日历
    private LinearLayout secondCalendar; // 第二个日历
    private LinearLayout currentCalendar; // 当前显示的日历

    private Map<String, Integer> marksMap = new HashMap<String, Integer>(); // 储存某个日子被标注(Integer)

    private Map<String, Integer> dayBgColorMap = new HashMap<String, Integer>(); // 储存某个日子的背景色


    public List<GroupDeadline> groups = new ArrayList<GroupDeadline>();

    public List<GroupDeadline> getGroups() {
        return groups;
    }

    public void setGroups(List<GroupDeadline> groups) {
        this.groups = groups;
    }

    public KCalendar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public KCalendar(Context context) {
        super(context);
        init();
    }

    private void init() {
        setBackgroundColor(COLOR_BG_CALENDAR);
        // 实例化手势监听器
        gd = new GestureDetector(this);
        // 初始化日历翻动动画
        push_left_in = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_left_in);
        push_left_out = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_left_out);
        push_right_in = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_right_in);
        push_right_out = AnimationUtils.loadAnimation(getContext(),
                R.anim.push_right_out);
        push_left_in.setDuration(300);
        push_left_out.setDuration(300);
        push_right_in.setDuration(300);
        push_right_out.setDuration(300);
        firstCalendar = new LinearLayout(getContext());
        firstCalendar.setOrientation(LinearLayout.VERTICAL);
        firstCalendar.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, -1));
        secondCalendar = new LinearLayout(getContext());
        secondCalendar.setOrientation(LinearLayout.VERTICAL);
        secondCalendar.setLayoutParams(new LinearLayout.LayoutParams(-1, -1));
        // 设置默认日历为第一个日历
        currentCalendar = firstCalendar;
        // 加入ViewFlipper
        addView(firstCalendar);
        addView(secondCalendar);
        // 绘制线条框架
        drawFrame(firstCalendar);
        drawFrame(secondCalendar);
        // 设置日历上的日子(1号)
        calendarYear = thisday.getYear() + 1900;
        calendarMonth = thisday.getMonth();
        calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
        // 填充展示日历
        setCalendarDate();
    }

    private void drawFrame(LinearLayout oneCalendar) {

        // 添加周末线性布局
        LinearLayout title = new LinearLayout(getContext());
        title.setBackgroundColor(COLOR_BG_WEEK_TITLE);
        title.setOrientation(LinearLayout.HORIZONTAL);
        LinearLayout.LayoutParams layout = new LinearLayout.LayoutParams(-1, 0,
                0.5f);
        Resources res = getResources();
        tb = res.getDimension(R.dimen.historyscore_tb);
        layout.setMargins(0, 0, 0, (int) (tb * 1.2));
        title.setLayoutParams(layout);
        oneCalendar.addView(title);

        // 添加周末TextView
        for (int i = 0; i < COLS_TOTAL; i++) {
            TextView view = new TextView(getContext());
            view.setGravity(Gravity.CENTER);
            view.setText(weekday[i]);
            view.setTextColor(COLOR_TX_WEEK_TITLE);
            view.setLayoutParams(new LinearLayout.LayoutParams(0, -1, 1));
            title.addView(view);
        }

        // 添加日期布局
        LinearLayout content = new LinearLayout(getContext());
        content.setOrientation(LinearLayout.VERTICAL);
        content.setLayoutParams(new LinearLayout.LayoutParams(-1, 0, 6f));
        oneCalendar.addView(content);

        // 添加日期
        for (int i = 0; i < ROWS_TOTAL; i++) {
            LinearLayout row = new LinearLayout(getContext());
            row.setOrientation(LinearLayout.HORIZONTAL);
            row.setLayoutParams(new LinearLayout.LayoutParams(
                    LayoutParams.MATCH_PARENT, 0, 1));

            content.addView(row);
            // 绘制日历上的列
            for (int j = 0; j < COLS_TOTAL; j++) {
                LinearLayout col = new LinearLayout(getContext());
                col.setLayoutParams(new LinearLayout.LayoutParams(0,
                        LayoutParams.MATCH_PARENT, 1));
                //给每一个格子添加背景，.9
                col.setBackgroundResource(R.drawable.calendar_day_bg);
                row.addView(col);
                col.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        ViewGroup parent = (ViewGroup) v.getParent();
                        int row = 0, col = 0;

                        for (int i = 0; i < parent.getChildCount(); i++) {
                            if (v.equals(parent.getChildAt(i))) {
                                col = i;
                                break;
                            }
                        }
                        for (int i = 0; i < parent.getChildCount(); i++) {
                            if (v.equals(parent.getChildAt(i))) {
                                col = i;
                                break;
                            }
                        }
                        ViewGroup pparent = (ViewGroup) parent.getParent();
                        for (int i = 0; i < pparent.getChildCount(); i++) {
                            if (parent.equals(pparent.getChildAt(i))) {
                                row = i;
                                break;
                            }
                        }
                        if (onCalendarClickListener != null) {
                            onCalendarClickListener.onCalendarClick(row, col,
                                    dates[row][col]);
                        }
                    }
                });
            }
        }
    }

    /**
     * 填充日历(包含日期、标记、背景等)
     */
    private void setCalendarDate() {
        // 根据日历的日子获取这一天是星期几
        int weekday = calendarday.getDay();
        // 每个月第一天
        int firstDay = 1;
        // 每个月中间号,根据循环会自动++
        int day = firstDay;
        // 每个月的最后一天
        int lastDay = getDateNum(calendarday.getYear(), calendarday.getMonth());
        // 下个月第一天
        int nextMonthDay = 1;
        int lastMonthDay = 1;

        // 填充每一个空格
        for (int i = 0; i < ROWS_TOTAL; i++) {
            for (int j = 0; j < COLS_TOTAL; j++) {
                // 这个月第一天不是礼拜天,则需要绘制上个月的剩余几天
                if (i == 0 && j == 0 && weekday != 0) {
                    int year = 0;
                    int month = 0;
                    int lastMonthDays = 0;
                    // 如果这个月是1月，上一个月就是去年的12月
                    if (calendarday.getMonth() == 0) {
                        year = calendarday.getYear() - 1;
                        month = Calendar.DECEMBER;
                    } else {
                        year = calendarday.getYear();
                        month = calendarday.getMonth() - 1;
                    }
                    // 上个月的最后一天是几号
                    lastMonthDays = getDateNum(year, month);
                    // 第一个格子展示的是几号
                    int firstShowDay = lastMonthDays - weekday + 1;
                    // 上月
                    for (int k = 0; k < weekday; k++) {
                        lastMonthDay = firstShowDay + k;
                        LinearLayout group = getDateView(0, k);
                        group.setGravity(Gravity.CENTER);
                        LinearLayout view = null;
                        if (group.getChildCount() > 0) {
                            view = (LinearLayout) group.getChildAt(0);
                        } else {
                            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, -1);
                            view = new LinearLayout(getContext());
                            view.setLayoutParams(params);
                            view.setGravity(Gravity.CENTER);
                            view.setOrientation(LinearLayout.VERTICAL);
                            //                            view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.calendar_day_item, null);
                            group.addView(view);
                        }
                        //日历中的日期
                        //                        TextView txtvDay = (TextView) view.findViewById(R.id.txtv_day_calendaritem);
                        TextView txtvDay = null;
                        TextView txtvPrice = null;
                        if (view.
                                getChildCount() > 0) {
                            txtvDay = (TextView) view.getChildAt(0);
                            txtvPrice = (TextView) view.getChildAt(1);
                        } else {
                            LinearLayout.LayoutParams paramsDay = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.5f);
                            txtvDay = new TextView(getContext());
                            txtvPrice = new TextView(getContext());
                            txtvDay.setLayoutParams(paramsDay);
                            txtvDay.setGravity(Gravity.CENTER);
                            txtvDay.setTextSize(10);
                            LinearLayout.LayoutParams paramsPrice = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 2f);
                            txtvPrice.setLayoutParams(paramsDay);
                            txtvPrice.setGravity(Gravity.CENTER);
                            txtvPrice.setTextSize(10);
                            view.addView(txtvDay);
                            view.addView(txtvPrice);
                        }
                        txtvDay.setText(Integer.toString(lastMonthDay));
                        txtvDay.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                        txtvPrice.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                        dates[0][k] = format(new Date(year, month, lastMonthDay));
                        // 设置日期背景色
                        if (dayBgColorMap.get(dates[0][k]) != null) {
                            // view.setBackgroundResource(dayBgColorMap
                            // .get(dates[i][j]));
                        } else {
                            //                            view.setBackgroundColor(Color.TRANSPARENT);
                            ((LinearLayout) view.getParent()).setBackgroundResource(R.drawable.calendar_day_bg);
                        }
                        // 设置标记
                        setMarker(group, 0, k);
                    }
                    j = weekday - 1;
                    // 这个月第一天是礼拜天，不用绘制上个月的日期，直接绘制这个月的日期
                } else {
                    LinearLayout group = getDateView(i, j);
                    group.setGravity(Gravity.CENTER);

                    LinearLayout view = null;
                    if (group.
                            getChildCount() > 0) {
                        view = (LinearLayout) group.getChildAt(0);
                    } else {

                        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT, -1);
                        view = new LinearLayout(getContext());
                        view.setLayoutParams(params);
                        view.setGravity(Gravity.CENTER);
                        view.setOrientation(LinearLayout.VERTICAL);
                        group.addView(view);
                        //                        view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.calendar_day_item, null);
                    }

                    // 本月
                    if (day <= lastDay) {
                        dates[i][j] = format(new Date(calendarday.getYear(),
                                calendarday.getMonth(), day));
                        TextView txtvDay = null;
                        TextView txtvPrice = null;
                        if (view.
                                getChildCount() > 0) {
                            txtvDay = (TextView) view.getChildAt(0);
                            txtvPrice = (TextView) view.getChildAt(1);
                        } else {
                            LinearLayout.LayoutParams paramsDay = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.5f);
                            txtvDay = new TextView(getContext());
                            txtvPrice = new TextView(getContext());
                            txtvDay.setLayoutParams(paramsDay);
                            txtvDay.setGravity(Gravity.CENTER);
                            txtvDay.setTextSize(10);
                            LinearLayout.LayoutParams paramsPrice = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 2f);
                            txtvPrice.setLayoutParams(paramsDay);
                            txtvPrice.setGravity(Gravity.CENTER);
                            txtvPrice.setTextSize(10);
                            //                        view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.calendar_day_item, null);
                            view.addView(txtvDay);
                            view.addView(txtvPrice);
                        }

                        txtvPrice.setText("");
                        /**
                         * 获得本月的group
                         */
                        if (groups != null && groups.size() != 0) {
                            List<GroupDeadline> thisMonths = new ArrayList<GroupDeadline>();

                            for (int m = 0; m < groups.size(); m++) {
                                //dates.add(groups.get(i).getDate());
                                int thisyear = getYear(groups.get(m).getDate());
                                int thismonth = getMonth(groups.get(m).getDate());
                                if (thisyear - 1900 == calendarday.getYear()) {
                                    if (thismonth - 1 == calendarday.getMonth()) {
                                        thisMonths.add(groups.get(m));
                                    }
                                }
                            }
                            for (int d = 0; d < thisMonths.size(); d++) {
                                if (day == getDay(thisMonths.get(d).getDate())) {
                                    //                                    if (thisMonths.get(d).getState() == 0) {
                                    //                                        txtvPrice.setText("");
                                    //                                    } else
                                    if (Integer.parseInt(thisMonths.get(d).getStock()) > 0) {

                                        txtvPrice.setText("￥" + thisMonths.get(d).getSell_price_adult());
                                    } else {
                                        txtvPrice.setText("售罄");
                                    }
                                }
                            }
                        }
                        txtvDay.setText(Integer.toString(day));
                        // 当天
                        if (thisday.getDate() == day
                                && thisday.getMonth() == calendarday.getMonth()
                                && thisday.getYear() == calendarday.getYear()) {
                            txtvDay.setText("今天");
                            txtvDay.setTextColor(COLOR_TX_WEEK_TITLE);
                            txtvPrice.setTextColor(COLOR_TX_PRICE);
                            ((LinearLayout) view.getParent()).setBackgroundResource(R.drawable.calendar_day_bg);
                        } else {
                            txtvDay.setTextColor(COLOR_TX_THIS_MONTH_DAY);
                            txtvPrice.setTextColor(COLOR_TX_PRICE);
                            ((LinearLayout) view.getParent()).setBackgroundResource(R.drawable.calendar_day_bg);
                        }
                        // 上面首先设置了一下默认的"当天"背景色，当有特殊需求时，才给当日填充背景色
                        // 设置日期背景色
                        if (dayBgColorMap.get(dates[i][j]) != null) {
                            txtvDay.setTextColor(Color.WHITE);
                            txtvPrice.setTextColor(Color.WHITE);
                            ((LinearLayout) view.getParent()).setBackgroundColor(dayBgColorMap
                                    .get(dates[i][j]));
                        }
                        // 设置标记
                        setMarker(group, i, j);
                        day++;
                        // 下个月
                    } else {
                        TextView txtvDay = null;
                        TextView txtvPrice = null;
                        if (view.
                                getChildCount() > 0) {
                            txtvDay = (TextView) view.getChildAt(0);
                            txtvPrice = (TextView) view.getChildAt(1);
                        } else {
                            LinearLayout.LayoutParams paramsDay = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 1.5f);
                            txtvDay = new TextView(getContext());
                            txtvPrice = new TextView(getContext());
                            txtvDay.setLayoutParams(paramsDay);
                            txtvDay.setGravity(Gravity.CENTER);
                            txtvDay.setTextSize(10);
                            LinearLayout.LayoutParams paramsPrice = new LinearLayout.LayoutParams(
                                    ViewGroup.LayoutParams.MATCH_PARENT, 0, 2f);
                            txtvPrice.setLayoutParams(paramsDay);
                            txtvPrice.setGravity(Gravity.CENTER);
                            txtvPrice.setTextSize(10);
                            //                        view = (LinearLayout) LayoutInflater.from(getContext()).inflate(R.layout.calendar_day_item, null);
                            view.addView(txtvDay);
                            view.addView(txtvPrice);
                        }
                        //                        TextView txtvDay = (TextView) view.findViewById(R.id.txtv_day_calendaritem);
                        if (calendarday.getMonth() == Calendar.DECEMBER) {
                            dates[i][j] = format(new Date(
                                    calendarday.getYear() + 1,
                                    Calendar.JANUARY, nextMonthDay));
                        } else {
                            dates[i][j] = format(new Date(
                                    calendarday.getYear(),
                                    calendarday.getMonth() + 1, nextMonthDay));
                        }
                        txtvDay.setText(Integer.toString(nextMonthDay));
                        txtvDay.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                        txtvPrice.setTextColor(COLOR_TX_OTHER_MONTH_DAY);
                        // 设置日期背景色
                        if (dayBgColorMap.get(dates[i][j]) != null) {
                            // view.setBackgroundResource(dayBgColorMap
                            // .get(dates[i][j]));
                        } else {
                            ((LinearLayout) view.getParent()).setBackgroundResource(R.drawable.calendar_day_bg);
                        }
                        // 设置标记
                        setMarker(group, i, j);
                        nextMonthDay++;
                    }
                }
            }
        }
    }

    /**
     * onClick接口回调
     */
    public interface OnCalendarClickListener {
        void onCalendarClick(int row, int col, String dateFormat);
    }

    /**
     * ondateChange接口回调  翻页
     */
    public interface OnCalendarDateChangedListener {
        void onCalendarDateChanged(int year, int month);
    }

    /**
     * 根据具体的某年某月，展示一个日历
     *
     * @param year
     * @param month
     */
    public void showCalendar(int year, int month) {
        calendarYear = year;
        calendarMonth = month - 1;
        calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
        setCalendarDate();
    }

    /**
     * 根据当前月，展示一个日历
     */
    public void showCalendar() {
        Date now = new Date();
        calendarYear = now.getYear() + 1900;
        calendarMonth = now.getMonth();
        calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
        setCalendarDate();
    }

    /**
     * 下一月日历
     */
    public synchronized void nextMonth() {
        if (groups != null) {
            int thisYear = getCalendarYear();
            int thisMonth = getCalendarMonth();
            List<GroupDate> Months = new ArrayList<GroupDate>();
            //首先找出这一年及以后的月份
            for (int i = 0; i < groups.size(); i++) {
                GroupDeadline g = groups.get(i);
                if (thisYear <= getYear(g.getDate())) {
                    if (/*g.getState() == 1 && */Integer.parseInt(g.getStock()) > 0) {
                        int year = getYear(g.getDate());
                        int month = getMonth(g.getDate());
                        Months.add(new GroupDate(year, month));
                    }
                }
            }
            //如果存在
            if (Months.size() > 0) {
                if (Months.get(Months.size() - 1).getYear() > thisYear ||
                        Months.get(Months.size() - 1).getMonth() > thisMonth) {//大于本月或者是大于本年

                    // 改变日历上下顺序
                    if (currentCalendar == firstCalendar) {
                        currentCalendar = secondCalendar;
                    } else {
                        currentCalendar = firstCalendar;
                    }
                    // 设置动画
                    setInAnimation(push_left_in);
                    setOutAnimation(push_left_out);
                    // 改变日历日期
                    if (calendarMonth == Calendar.DECEMBER) {
                        calendarYear++;
                        calendarMonth = Calendar.JANUARY;
                    } else {
                        calendarMonth++;
                    }
                    calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
                    // 填充日历
                    setCalendarDate();
                    // 下翻到下一月
                    showNext();
                    // 回调
                    if (onCalendarDateChangedListener != null) {
                        onCalendarDateChangedListener.onCalendarDateChanged(calendarYear,
                                calendarMonth + 1);
                    }

                }else{

                    ToastCommon.toastShortShow(getContext(), null, groups.get(groups.size() - 1).getDate() + "之后无团期...");
                }
            } else {
                ToastCommon.toastShortShow(getContext(), null, groups.get(groups.size() - 1).getDate() + "之后无团期...");
            }
        }
    }

    /**
     * 上一月日历
     */
    public synchronized void lastMonth() {
        if (groups != null) {
            int thisYear = getCalendarYear();
            int thisMonth = getCalendarMonth();
            List<GroupDate> Months = new ArrayList<GroupDate>();
            //首先找出这一年的月份
            for (int i = 0; i < groups.size(); i++) {
                GroupDeadline g = groups.get(i);
                if (thisYear >= getYear(g.getDate())) {
                    if (/*g.getState() == 1 && */Integer.parseInt(g.getStock()) > 0) {
                        int year = getYear(g.getDate());
                        int month = getMonth(g.getDate());
                        Months.add(new GroupDate(year, month));
                    }
                }
            }
            //            for (int i = 0; i < Months.size(); i++) {
            if (Months.size() > 0) {
                if (Months.get(0).getYear() < thisYear || Months.get(0).getMonth() < thisMonth) {
                    if (currentCalendar == firstCalendar) {
                        currentCalendar = secondCalendar;
                    } else {
                        currentCalendar = firstCalendar;
                    }
                    setInAnimation(push_right_in);
                    setOutAnimation(push_right_out);
                    if (calendarMonth == Calendar.JANUARY) {
                        calendarYear--;
                        calendarMonth = Calendar.DECEMBER;
                    } else {
                        calendarMonth--;
                    }
                    calendarday = new Date(calendarYear - 1900, calendarMonth, 1);
                    setCalendarDate();
                    showPrevious();
                    if (onCalendarDateChangedListener != null) {
                        onCalendarDateChangedListener.onCalendarDateChanged(calendarYear,
                                calendarMonth + 1);
                    }
                }
            } else {
                ToastCommon.toastShortShow(getContext(), null, groups.get(0).getDate() + "之前无团期...");
            }
        }
    }

    /**
     * 获取日历当前年份
     */
    public int getCalendarYear() {
        return calendarday.getYear() + 1900;
    }

    /**
     * 获取日历当前月份
     */
    public int getCalendarMonth() {
        return calendarday.getMonth() + 1;
    }

    /**
     * 在日历上做一个标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    public void addMark(Date date, int id) {
        addMark(format(date), id);
    }

    /**
     * 在日历上做一个标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    void addMark(String date, int id) {
        marksMap.put(date, id);
        setCalendarDate();
    }

    /**
     * 在日历上做一组标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    public void addMarks(Date[] date, int id) {
        for (int i = 0; i < date.length; i++) {
            marksMap.put(format(date[i]), id);
        }
        setCalendarDate();
    }

    /**
     * 在日历上做一组标记
     *
     * @param date 日期
     * @param id   bitmap res id
     */
    public void addMarks(List<String> date, int id) {
        for (int i = 0; i < date.size(); i++) {
            marksMap.put(date.get(i), id);
        }
        setCalendarDate();
    }

    /**
     * 移除日历上的标记
     */
    public void removeMark(Date date) {
        removeMark(format(date));
    }

    /**
     * 移除日历上的标记
     */
    public void removeMark(String date) {
        marksMap.remove(date);
        setCalendarDate();
    }

    /**
     * 移除日历上的所有标记
     */
    public void removeAllMarks() {
        marksMap.clear();
        setCalendarDate();
    }

    /**
     * 设置日历具体某个日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDayBgColor(Date date, int color) {
        setCalendarDayBgColor(format(date), color);
    }

    /**
     * 设置日历具体某个日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDayBgColor(String date, int color) {
        dayBgColorMap.put(date, color);
        setCalendarDate();
    }

    /**
     * 设置日历一组日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDaysBgColor(List<String> date, int color) {
        for (int i = 0; i < date.size(); i++) {
            dayBgColorMap.put(date.get(i), color);
        }
        setCalendarDate();
    }

    /**
     * 设置日历一组日期的背景色
     *
     * @param date
     * @param color
     */
    public void setCalendarDayBgColor(String[] date, int color) {
        for (int i = 0; i < date.length; i++) {
            dayBgColorMap.put(date[i], color);
        }
        setCalendarDate();
    }

    /**
     * 移除日历具体某个日期的背景色
     *
     * @param date
     */
    public void removeCalendarDayBgColor(Date date) {
        removeCalendarDayBgColor(format(date));
    }

    /**
     * 移除日历具体某个日期的背景色
     *
     * @param date
     */
    public void removeCalendarDayBgColor(String date) {
        dayBgColorMap.remove(date);
        setCalendarDate();
    }

    /**
     * 移除日历具体某个日期的背景色
     */
    public void removeAllBgColor() {
        dayBgColorMap.clear();
        setCalendarDate();
    }

    /**
     * 根据行列号获得包装每一个日子的LinearLayout
     *
     * @param row
     * @param col
     * @return
     */
    public String getDate(int row, int col) {
        return dates[row][col];
    }

    /**
     * 某天是否被标记了
     *
     * @param date
     * @return
     */
    public boolean hasMarked(String date) {
        return marksMap.get(date) == null ? false : true;
    }

    /**
     * 清除所有标记以及背景
     */
    public void clearAll() {
        marksMap.clear();
        dayBgColorMap.clear();
    }

    /***********************************************
     * private methods
     **********************************************/
    // 设置标记
    private void setMarker(LinearLayout group, int i, int j) {
        int childCount = group.getChildCount();
        if (marksMap.get(dates[i][j]) != null) {
            if (childCount < 2) {
                RelativeLayout.LayoutParams params = new RelativeLayout.LayoutParams(
                        (int) (tb * 0.7), (int) (tb * 0.7));
                params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
                params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
                params.setMargins(0, 0, 1, 1);
                ImageView markView = new ImageView(getContext());
                markView.setImageResource(marksMap.get(dates[i][j]));
                markView.setLayoutParams(params);
                markView.setBackgroundResource(R.drawable.calendar_bg_tag);
                group.addView(markView);
            }
        } else {
            if (childCount > 1) {
                group.removeView(group.getChildAt(1));
            }
        }

    }

    /**
     * 计算某年某月有多少天
     *
     * @param year
     * @param month
     * @return
     */
    private int getDateNum(int year, int month) {
        Calendar time = Calendar.getInstance();
        time.clear();
        time.set(Calendar.YEAR, year + 1900);
        time.set(Calendar.MONTH, month);
        return time.getActualMaximum(Calendar.DAY_OF_MONTH);
    }

    /**
     * 根据行列号获得包装每一个日子的RelativeLayout
     *
     * @param row
     * @param col
     * @return
     */
    private LinearLayout getDateView(int row, int col) {
        return (LinearLayout) ((LinearLayout) ((LinearLayout) currentCalendar
                .getChildAt(1)).getChildAt(row)).getChildAt(col);

    }

    /**
     * 将Date转化成字符串->2015-03-03
     */
    private String format(Date d) {
        return addZero(d.getYear() + 1900, 4) + "-"
                + addZero(d.getMonth() + 1, 2) + "-" + addZero(d.getDate(), 2);
    }

    // 2或4
    private static String addZero(int i, int count) {
        if (count == 2) {
            if (i < 10) {
                return "0" + i;
            }
        } else if (count == 4) {
            if (i < 10) {
                return "000" + i;
            } else if (i < 100 && i > 10) {
                return "00" + i;
            } else if (i < 1000 && i > 100) {
                return "0" + i;
            }
        }
        return "" + i;
    }

    private int getDay(String date) {
        return Integer.parseInt(date.substring(
                date.lastIndexOf("-") + 1, date.length()));
    }

    private int getMonth(String date) {
        return Integer.parseInt(date.substring(
                date.indexOf("-") + 1,
                date.lastIndexOf("-")));
    }

    private int getYear(String date) {
        return Integer.parseInt(date.substring(0, date.indexOf("-")));
    }

    /***********************************************
     * Override methods
     **********************************************/
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (gd != null) {
            if (gd.onTouchEvent(ev))
                return true;
        }
        return super.dispatchTouchEvent(ev);
    }

    public boolean onTouchEvent(MotionEvent event) {
        return this.gd.onTouchEvent(event);
    }

    public boolean onDown(MotionEvent e) {
        return false;
    }

    public void onShowPress(MotionEvent e) {
    }

    public boolean onSingleTapUp(MotionEvent e) {
        return false;
    }

    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX,
                            float distanceY) {
        return false;
    }

    public void onLongPress(MotionEvent e) {
    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                           float velocityY) {
        // 向左/上滑动
        if (e1.getX() - e2.getX() > 20) {
            nextMonth();
        }
        // 向右/下滑动
        else if (e1.getX() - e2.getX() < -20) {
            lastMonth();
        }
        return false;
    }

    /***********************************************
     * get/set methods
     **********************************************/

    public OnCalendarClickListener getOnCalendarClickListener() {
        return onCalendarClickListener;
    }

    public void setOnCalendarClickListener(
            OnCalendarClickListener onCalendarClickListener) {
        this.onCalendarClickListener = onCalendarClickListener;
    }

    public OnCalendarDateChangedListener getOnCalendarDateChangedListener() {
        return onCalendarDateChangedListener;
    }

    public void setOnCalendarDateChangedListener(
            OnCalendarDateChangedListener onCalendarDateChangedListener) {
        this.onCalendarDateChangedListener = onCalendarDateChangedListener;
    }

    public Date getThisday() {
        return thisday;
    }

    public void setThisday(Date thisday) {
        this.thisday = thisday;
    }

    public Map<String, Integer> getDayBgColorMap() {
        return dayBgColorMap;
    }

    public void setDayBgColorMap(Map<String, Integer> dayBgColorMap) {
        this.dayBgColorMap = dayBgColorMap;
    }


}