package com.divine.dy.lib_widget.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

/**
 * Author: Divine
 * CreateDate: 2021/9/1
 * Describe:
 */
public class ItemDecorationGrid extends RecyclerView.ItemDecoration {
    private Context mContext;
    private int decorationColor = -1;
    private int width = 30;
    private boolean showCellLine = true;

    public ItemDecorationGrid(Context mContext) {
        this.mContext = mContext;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        outRect.left = width / 2;
        outRect.top = width / 2;
        outRect.right = width / 2;
        outRect.bottom = width / 2;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        int position = parent.getChildAdapterPosition(view);
        int orientation = getOrientation(parent);
        int spanCount = getSpanCount(parent);//列数,如果是水平滚动，就是代表行数
        if (orientation == RecyclerView.VERTICAL) {
            if (!showCellLine) {
                outRect.left = 0;
                outRect.right = 0;
            }
            if (layoutManager instanceof StaggeredGridLayoutManager) {
                setVerticalOffsetsStaggered(outRect, view, position, spanCount);
            } else if (layoutManager instanceof GridLayoutManager) {
                setVerticalOffsetsGrid(outRect, position, spanCount);
            }
        } else if (orientation == RecyclerView.HORIZONTAL) {
            if (!showCellLine) {
                outRect.top = 0;
                outRect.bottom = 0;
            }
            setHorizontalOffsets(outRect, position, spanCount);
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        Paint mPaint = new Paint();
        if (decorationColor == -1) {
            mPaint.setColor(mContext.getResources().getColor(android.R.color.transparent));
        } else {
            mPaint.setColor(mContext.getResources().getColor(decorationColor));
        }
        int childCount = parent.getChildCount();
        int orientation = getOrientation(parent);
        int spanCount = getSpanCount(parent);//列数,如果是水平滚动，就是代表行数
        if (spanCount != -1) {
            for (int i = 0; i < childCount; i++) {
                View child = parent.getChildAt(i);
                if (orientation == RecyclerView.VERTICAL) {
                    drawVerticalLine(c, child, mPaint, i, spanCount);
                } else if (orientation == RecyclerView.HORIZONTAL) {
                    drawHorizontalLine(c, child, mPaint, i, spanCount);
                }
            }
        }
    }

    private void setVerticalOffsetsStaggered(Rect outRect, View view, int position, int spanCount) {
        if (position < spanCount) {
            if (position == 0) {
                outRect.left = 0;
            }
            if (position == spanCount - 1) {
                outRect.right = 0;
            }
            outRect.top = 0;
        } else {
            if (view != null) {
                StaggeredGridLayoutManager.LayoutParams layoutParams = (StaggeredGridLayoutManager.LayoutParams) view.getLayoutParams();
                int i = layoutParams.getSpanIndex();
                int spanOver = i % spanCount;
                if (spanOver == 0) {
                    //第一列
                    outRect.left = 0;
                }
                if (spanOver == spanCount - 1) {
                    //最后一列
                    outRect.right = 0;
                }
            }
        }
    }

    private void setVerticalOffsetsGrid(Rect outRect, int position, int spanCount) {
        int spanOver = position % spanCount;
        if (position < spanCount) {
            //第一行
            outRect.top = 0;
        }
        if (spanOver == 0) {
            //第一列
            outRect.left = 0;
        }
    }

    private void setHorizontalOffsets(Rect outRect, int position, int spanCount) {
        int spanOver = position % spanCount;
        if (spanOver == 0) {
            //第一行
            outRect.top = 0;
        }
        if (position < spanCount) {
            //第一列
            outRect.left = 0;
        }
    }

    private void drawHorizontalLine(Canvas c, View child, Paint mPaint, int position, int spanCount) {
        if (position >= spanCount) {
            //不是第一列
            //画竖线
            c.drawRect(child.getLeft(), child.getTop(), child.getLeft() - width, child.getBottom(), mPaint);
        }
        if (showCellLine && position % spanCount != 0) {
            //不是第一行
            //画横线
            c.drawRect(child.getLeft(), child.getTop(), child.getRight() + width, child.getTop() - width, mPaint);
        }
    }

    private void drawVerticalLine(Canvas c, View child, Paint mPaint, int position, int spanCount) {
        if (position >= spanCount) {
            //不是第一行
            //画横线
            c.drawRect(child.getLeft(), child.getTop(), child.getRight(), child.getTop() - width, mPaint);
        }
        if (showCellLine && position % spanCount != 0) {
            //不是第一列
            //画竖线
            c.drawRect(child.getLeft(), child.getTop(), child.getLeft() - width, child.getBottom() + width, mPaint);
        }
    }

    private int getSpanCount(RecyclerView parent) {
        int spanCount = -1;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            spanCount = gridLayoutManager.getSpanCount();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            spanCount = staggeredGridLayoutManager.getSpanCount();
        }
        return spanCount;
    }

    private int getOrientation(RecyclerView parent) {
        int orientation = RecyclerView.VERTICAL;
        RecyclerView.LayoutManager layoutManager = parent.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            orientation = gridLayoutManager.getOrientation();
        } else if (layoutManager instanceof StaggeredGridLayoutManager) {
            StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
            orientation = staggeredGridLayoutManager.getOrientation();
        }
        return orientation;
    }

    /**
     * 设置分割线颜色
     *
     * @param colorResId
     */
    public void setDecorationColor(int colorResId) {
        this.decorationColor = decorationColor;
    }

    /**
     * 设置分割线宽度
     *
     * @param width
     */
    public void setWidth(int width) {
        this.width = width;
    }

    /**
     * 是否显示行内item的分割线
     *
     * @param showCellLine
     */
    public void setShowCellLine(boolean showCellLine) {
        this.showCellLine = showCellLine;
    }
}
