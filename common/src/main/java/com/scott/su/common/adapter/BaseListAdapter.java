package com.scott.su.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;

import com.scott.su.common.interfaces.Judgment;

import java.util.ArrayList;
import java.util.List;

/**
 * 描述: 一般RecyclerView列表适配器基类
 * 作者: su
 * 日期: 2017/10/31 15:39
 */

public abstract class BaseListAdapter<E, VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {
    public static final int POSITION_NONE = -1;
    private Context mContext;
    private List<E> mDataList;
    private RecyclerView mRecyclerView;
    private LayoutInflater mLayoutInflater;

    protected Context getContext() {
        return mContext;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.mRecyclerView = recyclerView;
        this.mContext = recyclerView.getContext();
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        this.mRecyclerView = null;
    }

    @Override
    public int getItemCount() {
        return getCount();
    }

    protected boolean autoNotify() {
        return true;
    }

    public int getCount() {
        return getData().size();
    }

    public List<E> getData() {
        if (mDataList == null) {
            mDataList = new ArrayList<>();
        }
        return mDataList;
    }

    public E getData(int position) {
        if (!isLegalPositionForGetting(position)) {
            return null;
        }

        return getData().get(position);
    }

    public void setData(@NonNull List<E> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        int prevSize = getData().size();
        getData().clear();
        getData().addAll(dataList);

        if (autoNotify()) {
            notifyDataSetChanged();
            //            notifyItemRangeRemoved(0, prevSize);
            //            notifyItemRangeInserted(0, dataList.size());
        }
    }

    public void addData(@NonNull List<E> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        int prevSize = getData().size();
        getData().addAll(dataList);

        if (autoNotify()) {
            //            notifyDataSetChanged();
            notifyItemRangeInserted(prevSize, dataList.size());
        }
    }

    public boolean addData(@NonNull E data) {
        return addData(data, getItemCount());
    }

    public boolean addData(@NonNull E data, int position) {
        if (!isLegalPositionForAdding(position)) {
            return false;
        }

        getData().add(position, data);

        if (autoNotify() && isRecyclerViewAttached()) {
            //            notifyItemInserted(position);
            //            scrollToPosition(position);
            notifyDataSetChanged();
        }
        return true;
    }

    public boolean updateData(@NonNull E data, int position) {
        if (!isLegalPositionForGetting(position)) {
            return false;
        }

        getData().set(position, data);

        if (autoNotify() && isRecyclerViewAttached()) {
            notifyItemChanged(position);
            scrollToPosition(position);
        }

        return true;
    }

    public boolean removeData(@NonNull E data) {
        return removeData(getDataPosition(data));
    }

    public boolean removeData(int position) {
        if (!isLegalPositionForGetting(position)) {
            return false;
        }

        getData().remove(position);

        if (autoNotify() && isRecyclerViewAttached()) {
            //            notifyItemRemoved(position);
            //            notifyItemRangeChanged(position, getCount() - position);
            // TODO: 2017/11/8
            notifyDataSetChanged();
        }

        return true;
    }

    public boolean removeData(int positionStart, int itemCount) {
        if (!isLegalPositionForGetting(positionStart)
                || (positionStart + itemCount) > getCount()) {
            return false;
        }

        List<E> removeList = new ArrayList<>();

        for (int i = itemCount - 1; i >= 0; i--) {
            removeList.add(getData(positionStart + i));
        }

        getData().removeAll(removeList);

        if (autoNotify() && isRecyclerViewAttached()) {
            //            notifyItemRangeChanged(positionStart, itemCount);
            notifyDataSetChanged();
        }

        return true;
    }

    public boolean isExist(@NonNull Judgment<E> judgment) {
        if (isEmpty()) {
            return false;
        }

        return query(judgment).size() > 0;
    }

    public ArrayList<E> query(@NonNull Judgment<E> judgment) {
        ArrayList<E> result = new ArrayList<>();

        if (isEmpty()) {
            return result;
        }

        int n = getItemCount();
        for (int i = 0; i < n; i++) {
            E entity = getData(i);
            if (judgment.test(entity)) {
                result.add(entity);
            }
        }
        return result;
    }

    public void clear() {
        if (isEmpty()) {
            return;
        }

        int count = getItemCount();
        getData().clear();

        if (autoNotify() && isRecyclerViewAttached()) {
            notifyItemRangeRemoved(0, count);
        }
    }

    public int getDataPosition(@NonNull E data) {
        if (isEmpty()) {
            return POSITION_NONE;
        }

        int n = getItemCount();
        for (int i = 0; i < n; i++) {
            E entity = getData().get(i);
            if (entity == data) {
                return i;
            }
        }
        return POSITION_NONE;
    }

    public boolean isEmpty() {
        return getItemCount() == 0;
    }

    public boolean scrollToPosition(int position) {
        if (!isLegalPositionForGetting(position)) {
            return false;
        }

        if (!isRecyclerViewAttached()) {
            return false;
        }

        getRecyclerView().smoothScrollToPosition(position);
        return true;
    }

    public boolean isRecyclerViewAttached() {
        return getRecyclerView() != null;
    }

    public boolean isLegalPositionForGetting(int position) {
        if (isEmpty()) {
            return position == 0;
        } else {
            return (position >= 0 && position < getItemCount());
        }
    }

    public boolean isLegalPositionForAdding(int position) {
        if (isEmpty()) {
            return position == 0;
        } else {
            return (position >= 0 && position <= getItemCount());
        }
    }

    public int getLastPosition() {
        return isEmpty() ? 0 : getItemCount() - 1;
    }

    public RecyclerView getRecyclerView() {
        return mRecyclerView;
    }

    public LayoutInflater getLayoutInflater() {
        if (mLayoutInflater == null) {
            mLayoutInflater = LayoutInflater.from(mContext);
        }
        return mLayoutInflater;
    }
}
