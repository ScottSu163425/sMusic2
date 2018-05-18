package com.scott.su.common.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.scott.su.common.interfaces.Judgment;

import java.util.ArrayList;
import java.util.List;


public abstract class BaseRecyclerViewAdapter<E, VH extends BaseRecyclerViewHolder<E>>
        extends RecyclerView.Adapter<VH> {
    public static final int POSITION_NONE = -1;
    private Context mContext;
    private List<E> mDataList;
    private RecyclerView mRecyclerView;
    private LayoutInflater mLayoutInflater;

    private int mSingleSelectedPosition=POSITION_NONE;


    public BaseRecyclerViewAdapter(Context context) {
        mContext = context;
    }

    protected Context getContext() {
        return mContext;
    }


    protected abstract VH onCreateVH(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent, int viewType);

    protected abstract void onBindVH(E entity, @NonNull VH holder, int position);


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);

        this.mRecyclerView = recyclerView;
    }

    @Override
    public void onDetachedFromRecyclerView(RecyclerView recyclerView) {
        super.onDetachedFromRecyclerView(recyclerView);

        this.mRecyclerView = null;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return onCreateVH(getLayoutInflater(), parent, viewType);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        onBindVH(getData(position), holder, position);
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
//            notifyDataSetChanged();
            notifyItemRangeRemoved(0, prevSize);
            notifyItemRangeInserted(0, dataList.size());
        }
    }

    public void addData(@NonNull List<E> dataList) {
        if (dataList == null || dataList.isEmpty()) {
            return;
        }

        int prevSize = getData().size();
        getData().addAll(dataList);

        if (autoNotify()) {
//                        notifyDataSetChanged();
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
            notifyItemInserted(position);
            scrollToPosition(position);
//            notifyDataSetChanged();
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
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, getCount() - position);
//            notifyDataSetChanged();
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
            notifyItemRangeChanged(positionStart, itemCount);
//            notifyDataSetChanged();
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

    public int getSingleSelectedPosition() {
        return mSingleSelectedPosition;
    }

    public void setSingleSelectedPosition(int singleSelectedPosition) {
        if(mSingleSelectedPosition==singleSelectedPosition){
            return;
        }

        if(mSingleSelectedPosition!=POSITION_NONE){
            notifyItemChanged(mSingleSelectedPosition);
        }

        mSingleSelectedPosition = singleSelectedPosition;

        notifyItemChanged(mSingleSelectedPosition);
    }


}
