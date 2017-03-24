package com.github.azenhuang.praticedemo.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.os.Build;
import android.util.AttributeSet;
import android.util.SparseIntArray;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewGroup;

import com.github.azenhuang.praticedemo.R;


/**
 * Created by huangyongzheng on 9/21/16.
 */

public class TagsLayout extends ViewGroup {
    public static final int MAX_WIDTH_PARENT = -1;
    public static final int MAX_WIDTH_DEVICE = -2;
    public static final int MAX_WIDTH_UNSPECIFIC = -3;

    public static final int ALIGN_TOP = -1;
    public static final int ALIGN_BOTTOM = -2;
    public static final int ALIGN_CENTER = -3;

    /**
     * 自适应多少行
     */
    public static final int MAX_ROW_COUNT_ADJUST = -1;

    /**
     * 同一行中所有的标签最长宽度限制
     */
    private int maxWidth = MAX_WIDTH_UNSPECIFIC;
    private float maxWidthScale = 1;
    /**
     * 每一行中child的对齐方式
     */
    private int rowAlign;

    /**
     * 最多允许行数
     */
    private int maxRowCount;

    private int actualLines;

    //每行相邻child的水平间距
    private int horizontalSpace;
    //行间距
    private int verticalSpace;

    /**
     * 每行最后一个child的index
     */
    private SparseIntArray lineLastChildIndexMap = new SparseIntArray();

    /**
     * 记录每行的child当中最大的高度
     */
    private SparseIntArray lineMaxHeightMap = new SparseIntArray();

    private int deviceWidth;

    public TagsLayout(Context context) {
        this(context, null);
    }

    public TagsLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TagsLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs, defStyleAttr, 0);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public TagsLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        deviceWidth = context.getResources().getDisplayMetrics().widthPixels;
        initFromAttributes(context, attrs, defStyleAttr, defStyleRes);

    }

    private void initFromAttributes(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        final TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.TagsLayout, defStyleAttr, defStyleRes);
        if (typedArray.hasValue(R.styleable.TagsLayout_maxWidth)) {
            maxWidth = typedArray.getLayoutDimension(R.styleable.TagsLayout_maxWidth, "maxWidth");
        } else {
            maxWidth = MAX_WIDTH_UNSPECIFIC;
        }
        maxWidthScale = typedArray.getFloat(R.styleable.TagsLayout_maxWidthScale, 1);
        rowAlign = typedArray.getInt(R.styleable.TagsLayout_rowAlign, ALIGN_CENTER);
        maxRowCount = typedArray.getInt(R.styleable.TagsLayout_maxRowCount, 1);
//        horizontalSpace = typedArray.getDimensionPixelSize(R.styleable.TagsLayout_horizontalSpace, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 5, context.getResources().getDisplayMetrics()));
        verticalSpace = typedArray.getDimensionPixelSize(R.styleable.TagsLayout_verticalSpace, 0);
        horizontalSpace = typedArray.getDimensionPixelSize(R.styleable.TagsLayout_horizontalSpace, 0);

        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int count = getChildCount();
        int horizontalPadding = getPaddingLeft() + getPaddingRight();
        int verticalPadding = getPaddingTop() + getPaddingBottom();
        int specWidthSize = MeasureSpec.getSize(widthMeasureSpec);
        int specHeightSize = MeasureSpec.getSize(heightMeasureSpec);
        int specWidthMode = MeasureSpec.getMode(widthMeasureSpec);
        int specHeightMode = MeasureSpec.getMode(heightMeasureSpec);


        float maxScaledWidth;
        if (maxWidth >= 0) {
            maxScaledWidth = maxWidth * maxWidthScale;
        } else if (maxWidth == MAX_WIDTH_PARENT) {
            maxScaledWidth = specWidthSize * maxWidthScale;
        } else if (maxWidth == MAX_WIDTH_DEVICE) {
            maxScaledWidth = deviceWidth * maxWidthScale;
        } else {
            maxScaledWidth = specWidthSize;
        }
        //一行的最大宽度
        int maxRowWidth = (int) (specWidthMode == MeasureSpec.UNSPECIFIED ? (1 << 30 - 1) : Math.min(maxScaledWidth, specWidthSize) - horizontalPadding);
        //layout最大高度限制
        int maxTotalHeight = specHeightMode == MeasureSpec.UNSPECIFIED ? (1 << 30 - 1) : specHeightSize - verticalPadding;

        int lineMaxHeight = 0;
        int lineMaxWidth = 0;

        int realWidth = 0;
        int realHeight = 0;

        boolean isTagsOverflow = false;
        int childState = 0;

        int currentRow = 1;
        for (int i = 0; i < count; i++) {
            final View child = getChildAt(i);

            if (child.getVisibility() == GONE) {
                continue;
            }

            LayoutParams childParams = (LayoutParams) child.getLayoutParams();

            if (isTagsOverflow) {
                childParams.visible = false;
            } else {
                // Measure the child.
                measureChild(child, MeasureSpec.makeMeasureSpec(maxRowWidth, MeasureSpec.AT_MOST),
                        MeasureSpec.makeMeasureSpec(maxTotalHeight - realHeight, MeasureSpec.AT_MOST));

                int childHeight = child.getMeasuredHeight() + childParams.topMargin + childParams.bottomMargin;
                int childWidth = child.getMeasuredWidth() + childParams.leftMargin + childParams.rightMargin;
                if (isInRowRange(currentRow)) { //行数符合最大行数限制
                    //先判断是否能在当前行放下该child
                    //判断child的高度放在当前行是否满足要求
                    if ((childHeight > lineMaxHeight) && (realHeight - lineMaxHeight + childHeight > maxTotalHeight)) {
                        //加上本行的高度已经超出layout的高度限制，因此不满足高度要求
                        childParams.visible = false;
                        isTagsOverflow = true;
                    } else { //满足放在当前行的高度要求
                        if (isChildInMaxWidthLimit(child.getMeasuredWidth(), childParams, maxRowWidth)) {
                            //child宽度（含左右间距）在最大宽度限制之内
                            int tempWidth = childWidth + (lineMaxWidth == 0 ? 0 : lineMaxWidth + horizontalSpace);
                            if (tempWidth > maxRowWidth) { //child放在当前行，宽度超出
                                //判断是否能放在下一行
                                if (isInRowRange(currentRow + 1)) { //新的下一行
                                    int tempRealHeight = realHeight + childHeight + verticalSpace;
                                    //满足放在新一行的高度限制，由于已在最大宽度限制内，因此宽度必满足要求
                                    if (tempRealHeight <= maxTotalHeight) {
                                        //child放在新的一行
                                        childParams.visible = true;

                                        currentRow++;
                                        lineMaxWidth = childWidth;
                                        lineMaxHeight = childHeight;

                                        lineLastChildIndexMap.put(currentRow, i);
                                        lineMaxHeightMap.put(currentRow, lineMaxHeight);

                                        realWidth = Math.max(realWidth, lineMaxWidth);
                                        realHeight = tempRealHeight;

                                        childState = combineMeasuredStates(childState, child.getMeasuredState());
                                    } else {
                                        childParams.visible = false;
                                        isTagsOverflow = true;
                                    }

                                } else { //没有下一行可以放
                                    childParams.visible = false;
                                    isTagsOverflow = true;
                                }

                            } else { //child宽度符合放在当前行，放置child
                                childParams.visible = true;
                                lineMaxWidth = tempWidth;
                                realWidth = Math.max(realWidth, lineMaxWidth);
                                if (childHeight > lineMaxHeight) {
                                    realHeight = realHeight - lineMaxHeight + childHeight;
                                    lineMaxHeight = childHeight;
                                    lineMaxHeightMap.put(currentRow, lineMaxHeight);
                                }
                                lineLastChildIndexMap.put(currentRow, i);

                                childState = combineMeasuredStates(childState, child.getMeasuredState());
                            }
                        } else { //该child宽度已超出最长宽度
                            childParams.visible = false;
                            isTagsOverflow = true;
                        }
                    }

                } else {
                    childParams.visible = false;
                    isTagsOverflow = true;
                }

            }

        }

        actualLines = currentRow;

        // Check against our minimum height and width
        int width = Math.max(realWidth + horizontalPadding, getSuggestedMinimumWidth());
        int height = Math.max(realHeight + verticalPadding, getSuggestedMinimumHeight());

        // Report our final dimensions.
        setMeasuredDimension(resolveSizeAndState(width, widthMeasureSpec, childState),
                resolveSizeAndState(height, heightMeasureSpec, childState << MEASURED_HEIGHT_STATE_SHIFT));
    }

    private boolean isInRowRange(int currentRow) {
        return maxRowCount < 0 ? true : currentRow <= maxRowCount;
    }


    //child宽度是否在最大宽度范围内
    private boolean isChildInMaxWidthLimit(int childWidth, LayoutParams childParams, int maxTagsWidth) {
        return childWidth + childParams.leftMargin + childParams.rightMargin <= maxTagsWidth;
    }

    @Override
    protected boolean drawChild(Canvas canvas, View child, long drawingTime) {
        LayoutParams params = (LayoutParams) child.getLayoutParams();

        if (child.getVisibility() == GONE || !params.visible) {
            return true;
        }
        return super.drawChild(canvas, child, drawingTime);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curLeft = getPaddingLeft();
        int curTop;
        int currentRow = 1;
        int verticalOffset = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() == GONE || !params.visible) {
                continue;
            }

            if (i > lineLastChildIndexMap.get(currentRow)) {
                if (currentRow < actualLines) {
                    verticalOffset += lineMaxHeightMap.get(currentRow) + verticalSpace;
                    curLeft = getPaddingLeft();
                    currentRow++;
                } else {
                    break;
                }

            }

            int childWidth = child.getMeasuredWidth();
            int childHeight = child.getMeasuredHeight();
            curLeft += params.leftMargin;
            switch (rowAlign) {
                case ALIGN_TOP:
                    curTop = verticalOffset + params.topMargin;
                    break;

                case ALIGN_BOTTOM:
//                    curTop = verticalOffset + height - getPaddingBottom() - params.bottomMargin - childHeight;
                    curTop = verticalOffset + lineMaxHeightMap.get(currentRow) - childHeight - params.bottomMargin;
                    break;

                case ALIGN_CENTER:
                default:
//                    int holdHeight = height - getPaddingTop() - getPaddingBottom();
//                    curTop = (holdHeight - childHeight) / 2;
                    int childDifHeight = lineMaxHeightMap.get(currentRow) - childHeight - params.topMargin - params.bottomMargin;
                    curTop = verticalOffset + childDifHeight / 2;
                    break;
            }

            //do the layout
            child.layout(curLeft, curTop, curLeft + childWidth, curTop + childHeight);

            curLeft += childWidth + params.rightMargin + horizontalSpace;

        }
    }

    /**
     * 设定最大宽度,可以使用具体数值(大于等于0,单位px),也可以使用相对的数值{@link #MAX_WIDTH_PARENT}, {@link #MAX_WIDTH_DEVICE}
     * , {@link #MAX_WIDTH_UNSPECIFIC}
     *
     * @param size
     */
    public void setMaxWidth(int size) {
        setMaxWidth(TypedValue.COMPLEX_UNIT_PX, size);
    }

    /**
     * 设定最大宽度,可以使用具体数值(大于等于0,单位{@link TypedValue}),
     * 也可以使用相对的数值{@link #MAX_WIDTH_PARENT}, {@link #MAX_WIDTH_DEVICE}, {@link #MAX_WIDTH_UNSPECIFIC}
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     */
    public void setMaxWidth(int unit, int size) {
        int pixelsSize;
        if (size >= 0) {
            Context c = getContext();
            Resources r;
            if (c == null) {
                r = Resources.getSystem();
            } else {
                r = c.getResources();

            }
            pixelsSize = (int) TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
        } else if (size == MAX_WIDTH_PARENT || size == MAX_WIDTH_DEVICE) {
            pixelsSize = size;
        } else {
            pixelsSize = MAX_WIDTH_UNSPECIFIC;
        }
        if (pixelsSize != maxWidth) {
            maxWidth = pixelsSize;
            requestLayout();
        }
    }

    public void setMaxWidthScale(float scale) {
        if (maxWidthScale != scale) {
            maxWidthScale = scale;
            requestLayout();
        }
    }

    /**
     * 设定一行中相邻child的水平间距,大于等于0,单位px
     *
     * @param horizontalSize
     */
    public void setHorizontalSpace(int horizontalSize) {
        setHorizontalSpace(TypedValue.COMPLEX_UNIT_PX, horizontalSize);
    }

    /**
     * 设定一行中相邻child的水平间距,大于等于0,单位{@link TypedValue}
     *
     * @param unit           The desired dimension unit.
     * @param horizontalSize The desired size in the given units.
     */
    public void setHorizontalSpace(int unit, final int horizontalSize) {
        int pixelsSize;
        if (horizontalSize >= 0) {
            Context c = getContext();
            Resources r;
            if (c == null) {
                r = Resources.getSystem();
            } else {
                r = c.getResources();

            }
            pixelsSize = (int) TypedValue.applyDimension(unit, horizontalSize, r.getDisplayMetrics());
            if (pixelsSize != horizontalSize) {
                this.horizontalSpace = pixelsSize;
                requestLayout();
            }
        }

    }

    /**
     * 设定行间距,大于等于0,单位px
     *
     * @param verticalSize
     */
    public void setVerticalSpace(int verticalSize) {
        setVerticalSpace(TypedValue.COMPLEX_UNIT_PX, verticalSize);
    }

    /**
     * 设定行间距,大于等于0,单位{@link TypedValue}
     *
     * @param unit         The desired dimension unit.
     * @param verticalSize The desired size in the given units.
     */
    public void setVerticalSpace(int unit, final int verticalSize) {
        int pixelsSize;
        if (verticalSize >= 0) {
            Context c = getContext();
            Resources r;
            if (c == null) {
                r = Resources.getSystem();
            } else {
                r = c.getResources();

            }
            pixelsSize = (int) TypedValue.applyDimension(unit, verticalSize, r.getDisplayMetrics());
            if (pixelsSize != verticalSize) {
                this.verticalSpace = pixelsSize;
                requestLayout();
            }
        }
    }

    /**
     * 设置每行的对齐方式
     *
     * @param align {@link #ALIGN_TOP}, {@link #ALIGN_BOTTOM}, {@link #ALIGN_CENTER}
     */
    public void setRowAlign(int align) {
        if (align != rowAlign && align <= ALIGN_TOP && align >= ALIGN_CENTER) {
            rowAlign = align;
            requestLayout();
        }
    }

    /**
     * 设置最大行数
     *
     * @param count >= 0,或者 {@link #MAX_ROW_COUNT_ADJUST}
     */
    public void setMaxRowCount(int count) {
        if (count != maxRowCount && count >= MAX_ROW_COUNT_ADJUST) {
            maxRowCount = count;
            requestLayout();
        }
    }

    @Override
    protected LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
    }

    @Override
    protected LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    public static class LayoutParams extends MarginLayoutParams {
        public boolean visible = true;

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
