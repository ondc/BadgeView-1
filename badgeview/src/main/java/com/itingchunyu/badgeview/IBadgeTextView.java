/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.itingchunyu.badgeview;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.TabWidget;
import android.widget.TextView;

/**
 * Created by liyanxi on 16/9/22.
 * 使用详解:
 * 1:可当TextView使用,在xml中直接引用(局限:仅限文本控件)
 * 2:可设置到任意View,作为目标小红点使用,设置目标View:setTargetView();
 */
public class IBadgeTextView extends TextView implements IBadgeViewImpl {

    /**
     * 红点
     */
    private IBadgeView mBaseBadgeView;

    public IBadgeTextView(Context context) {
        this(context, null);
    }

    public IBadgeTextView(Context context, AttributeSet attrs) {
        this(context, attrs,-1);
    }

    public IBadgeTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mBaseBadgeView = new IBadgeView(this, context,attrs);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mBaseBadgeView.onSizeChanged(w, h);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mBaseBadgeView.draw(canvas);
    }

    @Override
    public IBadgeView setBadgeCount(int count) {
        return mBaseBadgeView.setCount(count);
    }

    @Override
    public IBadgeView setBadgeShown(boolean isShowBadge) {
        return mBaseBadgeView.setShown(isShowBadge);
    }

    @Override
    public IBadgeView setBadgeColor(int color) {
        return mBaseBadgeView.setBackgroundColor(color);
    }

    @Override
    public IBadgeView setmDefaultTopPadding(int mDefaultTopPadding) {
        return mBaseBadgeView.setmDefaultTopPadding(mDefaultTopPadding);
    }

    @Override
    public IBadgeView setmDefaultRightPadding(int mDefaultRightPadding) {
        return mBaseBadgeView.setmDefaultRightPadding(mDefaultRightPadding);
    }

    /*
     * Attach the BadgeView to the TabWidget
     *
     * @param target the TabWidget to attach the BadgeView
     *
     * @param tabIndex index of the tab
     */
    public void setTargetView(TabWidget target, int tabIndex) {
        View tabView = target.getChildTabViewAt(tabIndex);
        setTargetView(tabView);
    }

    /*
     * Attach the BadgeView to the target view
     *
     * @param target the view to attach the BadgeView
     */
    public void setTargetView(View target) {
        if (getParent() != null) {
            ((ViewGroup) getParent()).removeView(this);
        }

        if (target == null) {
            return;
        }

        if (target.getParent() instanceof FrameLayout) {
            ((FrameLayout) target.getParent()).addView(this);

        } else if (target.getParent() instanceof ViewGroup) {
            // use a new FrameLayout container for adding badge view
            ViewGroup parentContainer = (ViewGroup) target.getParent();
            int groupIndex = parentContainer.indexOfChild(target);
            parentContainer.removeView(target);

            FrameLayout badgeContainer = new FrameLayout(getContext());
            ViewGroup.LayoutParams parentLayoutParams = target.getLayoutParams();

            badgeContainer.setLayoutParams(parentLayoutParams);
            target.setLayoutParams(new ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));

            parentContainer.addView(badgeContainer, groupIndex, parentLayoutParams);
            badgeContainer.addView(target);

            badgeContainer.addView(this);
        } else if (target.getParent() == null) {
            Log.e(getClass().getSimpleName(), "ParentView is needed");
        }

    }

}
