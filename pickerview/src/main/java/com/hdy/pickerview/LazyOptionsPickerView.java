package com.hdy.pickerview;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.hdy.pickerview.view.BasePickerView;
import com.hdy.pickerview.view.LazyWheelOptions;

import java.util.ArrayList;

/**
 * Created by Sai on 15/11/22.
 */
public class LazyOptionsPickerView<T> extends BasePickerView implements View.OnClickListener {
    LazyWheelOptions wheelOptions;
    private View btnSubmit, btnCancel;
    private TextView tvTitle;
    private OnOptionsSelectListener optionsSelectListener;
    private static final String TAG_SUBMIT = "submit";
    private static final String TAG_CANCEL = "cancel";
    public LazyOptionsPickerView(Context context) {
        super(context);
        LayoutInflater.from(context).inflate(R.layout.pickerview_options, contentContainer);
        // -----确定和取消按钮
        btnSubmit = findViewById(R.id.btnSubmit);
        btnSubmit.setTag(TAG_SUBMIT);
        btnCancel = findViewById(R.id.btnCancel);
        btnCancel.setTag(TAG_CANCEL);
        btnSubmit.setOnClickListener(this);
        btnCancel.setOnClickListener(this);
        //顶部标题
        tvTitle = (TextView) findViewById(R.id.tvTitle);
        // ----转轮
        final View optionspicker = findViewById(R.id.optionspicker);
        wheelOptions = new LazyWheelOptions(optionspicker);
    }

    public void setLazyOptionsAdapter(LazyOptionsAdapter adapter) {
        wheelOptions.setLazyOptionsAdapter(adapter);
    }
    /**
     * 设置选中的item位置
     * @param option1
     */
    public void setSelectOptions(int option1){
        wheelOptions.setCurrentItems(option1, 0, 0);
    }
    /**
     * 设置选中的item位置
     * @param option1
     * @param option2
     */
    public void setSelectOptions(int option1, int option2){
        wheelOptions.setCurrentItems(option1, option2, 0);
    }
    /**
     * 设置选中的item位置
     * @param option1
     * @param option2
     * @param option3
     */
    public void setSelectOptions(int option1, int option2, int option3){
        wheelOptions.setCurrentItems(option1, option2, option3);
    }
    /**
     * 设置选项的单位
     * @param label1
     */
    public void setLabels(String label1){
        wheelOptions.setLabels(label1, null, null);
    }
    /**
     * 设置选项的单位
     * @param label1
     * @param label2
     */
    public void setLabels(String label1,String label2){
        wheelOptions.setLabels(label1, label2, null);
    }
    /**
     * 设置选项的单位
     * @param label1
     * @param label2
     * @param label3
     */
    public void setLabels(String label1,String label2,String label3){
        wheelOptions.setLabels(label1, label2, label3);
    }
    /**
     * 设置是否循环滚动
     * @param cyclic
     */
    public void setCyclic(boolean cyclic){
        wheelOptions.setCyclic(cyclic);
    }
    public void setCyclic(boolean cyclic1,boolean cyclic2,boolean cyclic3) {
        wheelOptions.setCyclic(cyclic1,cyclic2,cyclic3);
    }


    @Override
    public void onClick(View v)
    {
        String tag=(String) v.getTag();
        if(tag.equals(TAG_CANCEL))
        {
            dismiss();
            return;
        }
        else
        {
            if(optionsSelectListener!=null)
            {
                int[] optionsCurrentItems=wheelOptions.getCurrentItems();
                optionsSelectListener.onOptionsSelect(optionsCurrentItems[0], optionsCurrentItems[1], optionsCurrentItems[2]);
            }
            dismiss();
            return;
        }
    }

    public interface OnOptionsSelectListener {
        public void onOptionsSelect(int options1, int option2, int options3);
    }

    public void setOnoptionsSelectListener(
            OnOptionsSelectListener optionsSelectListener) {
        this.optionsSelectListener = optionsSelectListener;
    }

    public void setTitle(String title){
        tvTitle.setText(title);
    }

    public static abstract class LazyOptionsAdapter<T1, T2, T3> {
        private ArrayList<T1> currentOptions1Datas;
        private ArrayList<T2> currentOptions2Datas;
        private ArrayList<T3> currentOptions3Datas;
        public abstract ArrayList<T1> createOptions1Datas();
        public abstract ArrayList<T2> createOptions2Datas(int position1, T1 t1);
        public abstract ArrayList<T3> createOptions3Datas(int position1, int position2, T1 t1, T2 t2);

        public final ArrayList<T1> getOptions1Datas(){
            currentOptions1Datas = createOptions1Datas();
            return currentOptions1Datas;
        }
        public final ArrayList<T2> getOptions2Datas(int position1, T1 t1){
            currentOptions2Datas = createOptions2Datas(position1, t1);
            return currentOptions2Datas;
        }
        public final ArrayList<T3> getOptions3Datas(int position1, int position2, T1 t1, T2 t2){
            currentOptions3Datas = createOptions3Datas(position1, position2, t1, t2);
            return currentOptions3Datas;
        }

        public final T1 getOptions1Data(int position) {
            return currentOptions1Datas.get(position);
        }

        public final T2 getOptions2Data(int position) {
            return currentOptions2Datas.get(position);
        }

        public final T3 getOptions3Data(int position) {
            return currentOptions3Datas.get(position);
        }

        public T1 getEmpty1Data() {
            return null;
        }

        public T2 getEmpty2Data() {
            return null;
        }

        public T3 getEmpty3Data() {
            return null;
        }
    }
}
