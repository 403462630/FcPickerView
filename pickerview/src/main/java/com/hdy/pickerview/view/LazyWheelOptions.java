package com.hdy.pickerview.view;

import android.view.View;


import com.hdy.pickerview.LazyOptionsPickerView;
import com.hdy.pickerview.R;
import com.hdy.pickerview.adapter.ArrayWheelAdapter;
import com.hdy.pickerview.lib.WheelView;
import com.hdy.pickerview.listener.OnItemSelectedListener;

import java.util.ArrayList;

public class LazyWheelOptions<T1, T2, T3> {
	private View view;
	private WheelView wv_option1;
	private WheelView wv_option2;
	private WheelView wv_option3;

	private ArrayList<T1> mOptions1Items;
	private ArrayList<T2> mOptions2Items;
	private ArrayList<T3> mOptions3Items;

	private boolean linkage;
    private OnItemSelectedListener wheelListener_option1;
    private OnItemSelectedListener wheelListener_option2;
	private LazyOptionsPickerView.LazyOptionsAdapter<T1, T2, T3> lazyOptionsAdapter;

	public View getView() {
		return view;
	}

	public void setView(View view) {
		this.view = view;
	}

	public LazyWheelOptions(View view) {
		super();
		this.view = view;
		setView(view);

	}

	public void setLazyOptionsAdapter(LazyOptionsPickerView.LazyOptionsAdapter adapter) {
		this.lazyOptionsAdapter = adapter;

		this.mOptions1Items = lazyOptionsAdapter.getOptions1Datas();
		this.mOptions2Items = lazyOptionsAdapter.getOptions2Datas(0, mOptions1Items.get(0));
		this.mOptions3Items = lazyOptionsAdapter.getOptions3Datas(0, 0, mOptions1Items.get(0), mOptions2Items.get(0));
		int len = ArrayWheelAdapter.DEFAULT_LENGTH;
		if (this.mOptions3Items == null)
			len = 8;
		if (this.mOptions2Items == null)
			len = 12;
		// 选项1
		wv_option1 = (WheelView) view.findViewById(R.id.options1);
		wv_option1.setAdapter(new ArrayWheelAdapter(mOptions1Items, len));// 设置显示数据
		wv_option1.setCurrentItem(0);// 初始化时显示的数据
		// 选项2
		wv_option2 = (WheelView) view.findViewById(R.id.options2);
		if (mOptions2Items != null) {
			if (mOptions2Items.size() < 2) {
				wv_option2.setCyclic(false);
			} else {
				wv_option2.setCyclic(true);
			}
			wv_option2.setAdapter(new ArrayWheelAdapter(mOptions2Items));// 设置显示数据
		}
		wv_option2.setCurrentItem(wv_option1.getCurrentItem());// 初始化时显示的数据
		// 选项3
		wv_option3 = (WheelView) view.findViewById(R.id.options3);
		if (mOptions3Items != null) {
			if (mOptions3Items.size() < 2) {
				wv_option3.setCyclic(false);
			} else {
				wv_option3.setCyclic(true);
			}
			wv_option3.setAdapter(new ArrayWheelAdapter(mOptions3Items));// 设置显示数据
		}

		wv_option3.setCurrentItem(wv_option3.getCurrentItem());// 初始化时显示的数据

		init(true);
	}

	public void init(boolean linkage) {
        this.linkage = linkage;

		int textSize = 16;

		wv_option1.setTextSize(textSize);
		wv_option2.setTextSize(textSize);
		wv_option3.setTextSize(textSize);

		if (this.mOptions2Items == null)
			wv_option2.setVisibility(View.GONE);
		if (this.mOptions3Items == null)
			wv_option3.setVisibility(View.GONE);

		// 联动监听器
        wheelListener_option1 = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				int opt2Select = 0;
				mOptions2Items = lazyOptionsAdapter.getOptions2Datas(index, mOptions1Items.get(index));
				if (mOptions2Items.isEmpty()) {
					mOptions2Items.add(lazyOptionsAdapter.getEmpty2Data());
				}
				if (mOptions2Items != null) {
                    opt2Select = wv_option2.getCurrentItem();//上一个opt2的选中位置
					//新opt2的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                    opt2Select = opt2Select >= mOptions2Items.size() - 1 ? mOptions2Items.size() - 1 : opt2Select;

					if (mOptions2Items.size() < 2) {
						wv_option2.setCyclic(false);
					} else {
						wv_option2.setCyclic(true);
					}
					wv_option2.setAdapter(new ArrayWheelAdapter((ArrayList) mOptions2Items));
					wv_option2.setCurrentItem(opt2Select);
				}
				if (mOptions3Items != null) {
                    wheelListener_option2.onItemSelected(opt2Select);
				}
			}
		};
        wheelListener_option2 = new OnItemSelectedListener() {

			@Override
			public void onItemSelected(int index) {
				mOptions3Items = lazyOptionsAdapter.getOptions3Datas(wv_option1.getCurrentItem(), index, mOptions1Items.get(wv_option1.getCurrentItem()), mOptions2Items.get(index));
				if (mOptions3Items.isEmpty()) {
					mOptions3Items.add(lazyOptionsAdapter.getEmpty3Data());
				}
				if (mOptions3Items != null) {
					int opt3 = wv_option3.getCurrentItem();//上一个opt3的选中位置
                    //新opt3的位置，判断如果旧位置没有超过数据范围，则沿用旧位置，否则选中最后一项
                    opt3 = opt3 >= mOptions3Items.size() - 1 ? mOptions3Items.size() - 1 : opt3;
					if (mOptions3Items.size() < 2) {
						wv_option3.setCyclic(false);
					} else {
						wv_option3.setCyclic(true);
					}
					wv_option3.setAdapter(new ArrayWheelAdapter((ArrayList) mOptions3Items));
					wv_option3.setCurrentItem(opt3);

				}
			}
		};

//		// 添加联动监听
		if (linkage)
			wv_option1.setOnItemSelectedListener(wheelListener_option1);
		if (linkage)
			wv_option2.setOnItemSelectedListener(wheelListener_option2);
	}

	/**
	 * 设置选项的单位
	 * 
	 * @param label1
	 * @param label2
	 * @param label3
	 */
	public void setLabels(String label1, String label2, String label3) {
		if (label1 != null)
			wv_option1.setLabel(label1);
		if (label2 != null)
			wv_option2.setLabel(label2);
		if (label3 != null)
			wv_option3.setLabel(label3);
	}

	/**
	 * 设置是否循环滚动
	 * 
	 * @param cyclic
	 */
	public void setCyclic(boolean cyclic) {
		wv_option1.setCyclic(cyclic);
		wv_option2.setCyclic(cyclic);
		wv_option3.setCyclic(cyclic);
	}

	/**
	 * 分别设置第一二三级是否循环滚动
	 *
	 * @param cyclic1,cyclic2,cyclic3
	 */
	public void setCyclic(boolean cyclic1,boolean cyclic2,boolean cyclic3) {
        wv_option1.setCyclic(cyclic1);
        wv_option2.setCyclic(cyclic2);
        wv_option3.setCyclic(cyclic3);
	}
    /**
     * 设置第二级是否循环滚动
     *
     * @param cyclic
     */
    public void setOption2Cyclic(boolean cyclic) {
        wv_option2.setCyclic(cyclic);
    }
/**
     * 设置第三级是否循环滚动
     *
     * @param cyclic
     */
    public void setOption3Cyclic(boolean cyclic) {
        wv_option3.setCyclic(cyclic);
    }

	/**
	 * 返回当前选中的结果对应的位置数组 因为支持三级联动效果，分三个级别索引，0，1，2
	 * 
	 * @return
	 */
	public int[] getCurrentItems() {
		int[] currentItems = new int[3];
		currentItems[0] = wv_option1.getCurrentItem();
		currentItems[1] = wv_option2.getCurrentItem();
		currentItems[2] = wv_option3.getCurrentItem();
		return currentItems;
	}

	public void setCurrentItems(int option1, int option2, int option3) {
        if(linkage){
            itemSelected(option1, option2, option3);
        }
        wv_option1.setCurrentItem(option1);
        wv_option2.setCurrentItem(option2);
        wv_option3.setCurrentItem(option3);
	}

	private void itemSelected(int opt1Select, int opt2Select, int opt3Select) {
		if (mOptions2Items != null) {
			wv_option2.setAdapter(new ArrayWheelAdapter(lazyOptionsAdapter.getOptions2Datas(opt1Select, mOptions1Items.get(opt1Select))));
			wv_option2.setCurrentItem(opt2Select);
		}
		if (mOptions3Items != null) {
			wv_option3.setAdapter(new ArrayWheelAdapter(lazyOptionsAdapter.getOptions3Datas(opt1Select, opt2Select, mOptions1Items.get(opt1Select), mOptions2Items.get(opt2Select))));
			wv_option3.setCurrentItem(opt3Select);
		}
	}


}
