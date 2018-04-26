package com.scott.su.common.fragment;

import android.os.Bundle;
import android.support.annotation.DrawableRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.scott.su.common.R;


public abstract class BaseTitleFragment extends BaseFragment {
    private static int sDefaultLeftIconRes = R.drawable.ic_arrow_back_white_24dp;
    private Toolbar mToolbar;
    private TextView mTextViewTitle;


    private final View.OnClickListener mDefaultLeftIconClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            getActivity().onBackPressed();
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        if (viewRoot == null) {
            viewRoot = inflater.inflate(R.layout.frament_base_title, container, false);

            FrameLayout frameLayout = viewRoot.findViewById(R.id.fl_content_base_title);
            frameLayout.addView(provideContentView(inflater, container, savedInstanceState),
                    new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
                            ViewGroup.LayoutParams.MATCH_PARENT));

            mTextViewTitle = viewRoot.findViewById(R.id.tv_title);

            mToolbar = viewRoot.findViewById(R.id.toolbar);
            mToolbar.setTitle("");
            mToolbar.inflateMenu(R.menu.empty);
            mToolbar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem item) {
                    return BaseTitleFragment.this.onMenuItemClick(item.getItemId(),
                            (String) item.getTitle());
                }
            });
        }
        return viewRoot;
    }

    public static void setDefaultLeftIconRes(@DrawableRes int res) {
        sDefaultLeftIconRes = res;
    }

    /**
     * 子类重写，处理菜单项点击事件
     *
     * @param id
     * @param text
     * @return
     */
    protected boolean onMenuItemClick(int id, String text) {
        return false;
    }

    /**
     * 居左显示标题文本
     *
     * @param title
     */
    protected void showTitleTextLeft(String title) {
        mTextViewTitle.setText(title);

        Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) mTextViewTitle.getLayoutParams();
        layoutParams.gravity = Gravity.START;
    }

    /**
     * 居中显示标题文本
     *
     * @param title
     */
    protected void showTitleTextCenter(String title) {
        mTextViewTitle.setText(title);

        Toolbar.LayoutParams layoutParams = (Toolbar.LayoutParams) mTextViewTitle.getLayoutParams();
        layoutParams.gravity = Gravity.CENTER;
    }

    /**
     * 显示左侧图标
     */
    protected void showTitleIconLeft() {
        showTitleIconLeft(sDefaultLeftIconRes, mDefaultLeftIconClickListener);
    }

    /**
     * 显示左侧图标
     *
     * @param icon
     * @param listener
     */
    protected void showTitleIconLeft(@Nullable Integer icon, @Nullable View.OnClickListener listener) {
        mToolbar.setNavigationIcon(icon == null ? sDefaultLeftIconRes : icon);
        mToolbar.setNavigationOnClickListener(listener == null ? mDefaultLeftIconClickListener
                : listener);
    }

    /**
     * 添加右侧菜单项(文字）
     *
     * @param id
     * @param text
     * @return
     */
    protected boolean addMenuItemText(int id, String text) {
        return addMenuItem(id, text, null, true, true);
    }

    /**
     * 添加右侧菜单项(图标）
     *
     * @param id
     * @param icon
     * @return
     */
    protected boolean addMenuItemIcon(int id, @DrawableRes int icon) {
        return addMenuItem(id, "", icon, true, true);
    }

    /**
     * 添加右侧菜单项
     *
     * @param id             菜单项id
     * @param text           菜单项文本
     * @param icon           null表示不显示图标，显示text
     * @param showAsAction   false将收缩至“更多”
     * @param replaceIfExist id已存在时是否覆盖
     * @return
     */
    protected boolean addMenuItem(int id, String text, @Nullable @DrawableRes Integer icon,
                                  boolean showAsAction, boolean replaceIfExist) {
        Menu menu = mToolbar.getMenu();

        if (isMenuItemExist(id)) {
            if (replaceIfExist) {
                menu.removeItem(id);
            } else {
                return false;
            }
        }

        MenuItem item = menu.add(0, id, menu.size(), text);
        if (icon != null) {
            item.setIcon(icon);
        }

        item.setShowAsAction(showAsAction ? MenuItem.SHOW_AS_ACTION_ALWAYS : MenuItem.SHOW_AS_ACTION_NEVER);

        return true;
    }

    private boolean isMenuItemExist(int id) {
        Menu menu = mToolbar.getMenu();

        int n = menu.size();
        if (n == 0) {
            return false;
        }

        for (int i = 0; i < n; i++) {
            if (menu.getItem(i).getItemId() == id) {
                return true;
            }
        }

        return false;
    }


}
