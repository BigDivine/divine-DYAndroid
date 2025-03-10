package com.divine.yang.widget.old;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.View;

import com.divine.yang.widget.R;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;


/**
 * GridLayoutManager 分割线
 * author: Divine
 * <p>
 * date: 2018/11/28
 */
public class ItemDecorationHome extends RecyclerView.ItemDecoration {
    private static final int[] ATTRS = new int[]{android.R.attr.listDivider};
    private Drawable mDivider;
    private int spanCount = -1;
    //height width 单位为px
    private int width = 1;
    private Paint paint;

    public ItemDecorationHome(Context context) {
        init(context, width, R.color.back_divider);
    }

    public ItemDecorationHome(Context context, int width) {
        init(context, width, R.color.back_divider);
    }

    public ItemDecorationHome(Context context, int width, int color) {
        init(context, width, color);
    }

    private void init(Context context, int width, int color) {
        this.width = width;
        paint = new Paint();
        paint.setColor(context.getResources().getColor(color));
        final TypedArray a = context.obtainStyledAttributes(ATTRS);
        mDivider = a.getDrawable(0);
        a.recycle();
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        drawHorizontal(c, parent);
        drawVertical(c, parent);
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view);
        int childCount = parent.getAdapter().getItemCount();
        int spanCount = getSpanCount(parent);
        int drawRight = width;
        int drawBottom = width;
        int drawLeft = width;
        int drawTop = width;
        if (isLastRaw(parent, position, spanCount, childCount)) {
            drawBottom = 0;
        }
        if (isFirstRaw(parent, position, spanCount, childCount)) {
            drawTop = 0;
        }
        if (isLastColum(parent, position, spanCount, childCount)) {
            drawRight = 0;
        }
        if (isFirstColum(parent, position, spanCount, childCount)) {
            drawLeft = 0;
        }
        outRect.set(drawLeft, drawTop, drawRight, drawBottom);
    }

    private void drawHorizontal(Canvas c, RecyclerView parent) {
        spanCount = getSpanCount(parent);

        int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);
            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int left = child.getLeft() - params.leftMargin;
            final int right = child.getRight() + params.rightMargin;
            final int top = child.getBottom() + params.bottomMargin;
            //            int bottom = top + mDivider.getIntrinsicHeight();
            int bottom = top + width * 2;
            if (isLastRaw(parent, i, spanCount, childCount)) {
                // 如果是最后一行，则不需要绘制底部
                bottom = top;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    private void drawVertical(Canvas c, RecyclerView parent) {
        spanCount = getSpanCount(parent);

        final int childCount = parent.getChildCount();
        for (int i = 0; i < childCount; i++) {
            final View child = parent.getChildAt(i);

            final RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) child
                    .getLayoutParams();
            final int top = child.getTop() - params.topMargin;
            final int bottom = child.getBottom() + params.bottomMargin;
            final int left = child.getRight() + params.rightMargin;
            //            int right = left + mDivider.getIntrinsicWidth();
            int right = left + width * 2;

            if (isLastColum(parent, i, spanCount, childCount)) {
                // 如果是最后一列，则不需要绘制右边
                right = left;
            }
            mDivider.setBounds(left, top, right, bottom);
            mDivider.draw(c);
            c.drawRect(left, top, right, bottom, paint);
        }
    }

    private int getSpanCount(RecyclerView parent) {
        // 列数
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            spanCount = ((GridLayoutManager) layoutManager).getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            spanCount = ((StaggeredGridLayoutManager) layoutManager).getSpanCount();
        }
        return spanCount;
    }

    private boolean isLastRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            if (childCount % spanCount == 0) {
                childCount = childCount - spanCount;
            } else {
                childCount = childCount - childCount % spanCount;
            }
            // 如果是最后一行，则不需要绘制底部
            return pos >= childCount;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一行，则不需要绘制底部
                return pos >= childCount;
            } else {
                // 如果是最后一行，则不需要绘制底部
                return (pos + 1) % spanCount == 0;
            }
        }
        return false;
    }

    private boolean isFirstRaw(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // 如果是第一行，则不需要绘制底部
            return pos < spanCount;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            // StaggeredGridLayoutManager 且纵向滚动
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是第一行，则不需要绘制底部
                return pos < spanCount;
            } else {
                // 如果是第一行，则不需要绘制底部
                return (pos + 1) % spanCount == 1;
            }
        }
        return false;
    }

    private boolean isLastColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            // 如果是最后一列，则不需要绘制右边
            return (pos + 1) % spanCount == 0;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager)
                    .getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                // 如果是最后一列，则不需要绘制右边
                return (pos + 1) % spanCount == 0;
            } else {
                childCount = childCount - childCount % spanCount;
                // 如果是最后一列，则不需要绘制右边
                return pos >= childCount;
            }
        }
        return false;
    }

    private boolean isFirstColum(RecyclerView parent, int pos, int spanCount, int childCount) {
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            return (pos + 1) % spanCount == 1;
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            int orientation = ((StaggeredGridLayoutManager) layoutManager).getOrientation();
            if (orientation == StaggeredGridLayoutManager.VERTICAL) {
                return (pos + 1) % spanCount == 1;
            } else {
                return pos < spanCount;
            }
        }
        return false;
    }
}
