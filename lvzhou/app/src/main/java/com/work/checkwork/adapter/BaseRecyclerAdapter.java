package com.work.checkwork.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.work.checkwork.adapter.holder.BaseRecyclerHolder;

import java.util.ArrayList;
import java.util.List;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


public abstract class BaseRecyclerAdapter<T, H extends BaseRecyclerHolder<T>> extends RecyclerView.Adapter<BaseRecyclerHolder<T>> {

    private static final String TAG = "BaseRecyclerAdapter";

    private final Context mContext;
    private final LayoutInflater mInflater;

    private ArrayList<T> mDataList;

    //单击事件
    protected onItemClickListener mItemClickListener;
    //长按事件
    protected onItemLongClickListener mItemLongClickListener;

    protected View VIEW_FOOTER;
    protected View VIEW_HEADER;
    protected RecyclerView mRecyclerView;

    //Type
    protected int TYPE_HEADER = 1000;
    protected int TYPE_FOOTER = 1001;

    public BaseRecyclerAdapter(Context context) {
        this(context, null);
    }


    public BaseRecyclerAdapter(Context context, ArrayList<T> dataList) {
        mContext = context.getApplicationContext();
        mInflater = LayoutInflater.from(context);
        mDataList = dataList;
    }

    @Override
    public int getItemCount() {
        int count = (mDataList == null ? 0 : mDataList.size());
        if (VIEW_FOOTER != null) {
            count++;
        }
        if (VIEW_HEADER != null) {
            count++;
        }
        return count;
    }

    @Override
    public BaseRecyclerHolder<T> onCreateViewHolder(ViewGroup parent, int viewType) {
        return onCreateBaseViewHolder(parent, viewType);
    }

    protected abstract H onCreateBaseViewHolder(ViewGroup parent, int viewType);

    @Override
    public void onBindViewHolder(BaseRecyclerHolder holder, int position) {
        onBindBaseViewHolder(((H) holder), position);
    }

    protected abstract void onBindBaseViewHolder(H holder, int position);

    public Context getContext() {
        return mContext;
    }

    public LayoutInflater getInflater() {
        return mInflater;
    }

    public T getItem(int position) {
        return mDataList.get(position);
    }

    /**
     * 处理item的点击事件,因为recycler没有提供单击事件,所以只能自己写了
     */
    public interface onItemClickListener {
        void onItemClick(BaseRecyclerAdapter adapter, View view, int position);
    }

    /**
     * 长按事件
     */
    public interface onItemLongClickListener {
        void onItemLongClick(BaseRecyclerAdapter adapte, View view, int position);
    }

    /**
     * 暴露给外面的设置单击事件
     */
    public void setOnItemClickListener(onItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    /**
     * 暴露给外面的长按事件
     */
    public void setOnItemLongClickListener(onItemLongClickListener onItemLongClickListener) {
        mItemLongClickListener = onItemLongClickListener;
    }

    public ArrayList<T> getDataList() {
        return mDataList;
    }

    public boolean clear() {
        if (mDataList == null) {
            return false;
        } else {
            mDataList.clear();
            return true;
        }
    }

    public void replaceAllItems(ArrayList<T> itemList) {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        } else {
            mDataList.clear();
        }
        if (itemList != null) {
            mDataList.addAll(itemList);
        }
        notifyDataSetChanged();
    }

    public void insert(ArrayList<T> itemList) {
        mDataList.addAll(0, itemList);
        notifyItemRangeInserted(0, itemList.size());
    }

    /**
     * 向指定位置添加元素
     */
    public void addItem(int position, T value) {
        if (position > this.mDataList.size()) {
            position = this.mDataList.size();
        }
        if (position < 0) {
            position = 0;
        }
        /**
         * 使用notifyItemInserted/notifyItemRemoved会有动画效果
         * 而使用notifyDataSetChanged()则没有
         */
        this.mDataList.add(position, value);//在集合中添加这条数据
        notifyItemInserted(position);//通知插入了数据
    }


    public void addItems(final List<T> items) {

        if (items == null) {
            return;
        }

        if (mDataList == null) {
            mDataList = new ArrayList<T>();
        }
        mDataList.addAll(items);

        notifyDataSetChanged();
    }

    /**
     * 移除指定位置元素
     */
    public T removeItem(int position) {
        if (position > mDataList.size() - 1) {
            return null;
        }

        T value = mDataList.remove(position);//所以还需要手动在集合中删除一次
        notifyDataSetChanged();//通知删除了数据,但是没有删除list集合中的数据
        return value;
    }

    /**
     * 移除指定位置元素
     */
    public void removeItem(T t) {
        mDataList.remove(t);
        notifyDataSetChanged();//通知删除了数据,但是没有删除list集合中的数据
    }

    /**
     * 移除所有元素
     */
    public void removeAllItem() {
        if (mDataList != null && mDataList.size() > 0) {
            mDataList.clear();
        }
        notifyDataSetChanged();//通知删除了数据,但是没有删除list集合中的数据
    }


    /**
     * 添加头部
     */

    public void addHeaderView(View headerView) {
        if (haveHeaderView()) {
            throw new IllegalStateException("hearview has already exists!");
        } else {
            //避免出现宽度自适应
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            headerView.setLayoutParams(params);
            VIEW_HEADER = headerView;
            ifGridLayoutManager();
            notifyItemInserted(0);
        }

    }

    /**
     * 删出头部
     */
    public void removeHeaderView() {
        if (haveHeaderView()) {
            VIEW_HEADER = null;
            notifyItemRemoved(0);
        } else {
            throw new IllegalStateException("hearview no longer exists!");
        }
    }


    /**
     * 添加底部
     */
    public void addFooterView(View footerView) {
        if (haveFooterView()) {
            throw new IllegalStateException("footerView has already exists!");
        } else {
            ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            footerView.setLayoutParams(params);
            VIEW_FOOTER = footerView;
            ifGridLayoutManager();
            notifyItemInserted(getItemCount() - 1);
        }
    }

    /**
     * 删出底部
     */
    public void removeFooterView() {
        if (haveFooterView()) {
            VIEW_FOOTER = null;
            notifyItemRemoved(getItemCount() - 1);
        } else {
            throw new IllegalStateException("footerView no longer exists!");
        }
    }

    //适配GridLayout
    public void ifGridLayoutManager() {
        if (mRecyclerView == null) {
            return;
        }
        final RecyclerView.LayoutManager layoutManager = mRecyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            ((GridLayoutManager) layoutManager).setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    return (isHeaderView(position) || isFooterView(position)) ?
                            ((GridLayoutManager) layoutManager).getSpanCount() : 1;
                }
            });
        }
    }

    public boolean haveHeaderView() {
        return VIEW_HEADER != null;
    }

    public boolean haveFooterView() {
        return VIEW_FOOTER != null;
    }

    public boolean isHeaderView(int position) {
        return haveHeaderView() && position == 0;
    }

    public boolean isFooterView(int position) {
        return haveFooterView() && position == getItemCount() - 1;
    }
}

