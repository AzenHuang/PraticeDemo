package com.github.azenhuang.praticedemo.layout;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
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
    private int maxLines;

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
        maxWidth = typedArray.getLayoutDimension(R.styleable.TagsLayout_maxWidth, "maxWidth");
        maxWidthScale = typedArray.getFloat(R.styleable.TagsLayout_maxWidthScale, 1);
        rowAlign = typedArray.getInt(R.styleable.TagsLayout_rowAlign, ALIGN_CENTER);
        maxLines = typedArray.getInt(R.styleable.TagsLayout_maxRowCount, MAX_ROW_COUNT_ADJUST);
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
        int specWith = MeasureSpec.getSize(widthMeasureSpec);
        int specHeight = MeasureSpec.getSize(heightMeasureSpec);

        int maxTagsWidth;
        //获取所有tag合起来的最大宽度
        if (maxWidth >= 0) {
            maxTagsWidth = Math.min((int) (maxWidth * maxWidthScale), specWith) - horizontalPadding;
        } else if (maxWidth == MAX_WIDTH_PARENT) {
            maxTagsWidth = Math.min((int) (specWith * maxWidthScale), specWith) - horizontalPadding;
        } else if (maxWidth == MAX_WIDTH_DEVICE) {
            maxTagsWidth = Math.min((int) (deviceWidth * maxWidthScale), specWith) - horizontalPadding;
        } else {
            maxTagsWidth = specWith - horizontalPadding;
        }

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
            // Measure the child.
            measureChild(child, widthMeasureSpec, heightMeasureSpec);

            if (isTagsOverflow) {
                childParams.visible = false;
            } else {
                int childHeight = child.getMeasuredHeight() + childParams.topMargin + childParams.bottomMargin;
                int childWidth = child.getMeasuredWidth() + childParams.leftMargin + childParams.rightMargin;
                if (isInLineRange(currentRow)) {
                    if ((childHeight > lineMaxHeight) && (realHeight - lineMaxHeight + childHeight > specHeight - verticalPadding)) {
                        //加上本行的高度已经超出layout的高度限制
                        childParams.visible = false;
                        isTagsOverflow = true;
                    } else {
                        if (checkChildWidth(child.getMeasuredWidth(), childParams, maxTagsWidth)) {
                            int tempWidth = lineMaxWidth + childWidth;
                            if (tempWidth > maxTagsWidth) {
                                if (currentRow == maxLines) { //没有下一行可以放
                                    childParams.visible = false;
                                    isTagsOverflow = true;
                                } else { //放到新的一行
                                    currentRow++;
                                    childParams.visible = true;
                                    lineMaxWidth = childWidth;
                                    lineMaxHeight = childHeight;

                                    lineLastChildIndexMap.put(currentRow, i);
                                    lineMaxHeightMap.put(currentRow, lineMaxHeight);

                                    realWidth = Math.max(realWidth, lineMaxWidth);
                                    realHeight += childHeight + verticalSpace;

                                    childState = combineMeasuredStates(childState, child.getMeasuredState());
                                }

                            } else { //放在既有的一行
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
                            continue;
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

    private boolean isInLineRange(int currentRow) {
        return maxLines < 0 ? true : currentRow <= maxLines;
    }

    //检查child宽度是否在最大宽度范围内
    private boolean checkChildWidth(int childWidth, LayoutParams childParams, int maxTagsWidth) {
        if (childWidth + childParams.leftMargin + childParams.rightMargin > maxTagsWidth) {
            childParams.visible = false;
            return false;
        } else {
            return true;
        }
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        final int count = getChildCount();
        int curLeft = getPaddingLeft();
        int curTop;
        int currentLine = 1;
        int verticalOffset = getPaddingTop();
        for (int i = 0; i < count; i++) {
            View child = getChildAt(i);
            LayoutParams params = (LayoutParams) child.getLayoutParams();

            if (child.getVisibility() == GONE || !params.visible) {
                continue;
            }
            if (i > lineLastChildIndexMap.get(currentLine)) {
                if (currentLine < actualLines) {
                    verticalOffset += lineMaxHeightMap.get(currentLine) + verticalSpace;
                    curLeft = getPaddingLeft();
                    currentLine++;
                } else{
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
                    curTop = verticalOffset + lineMaxHeightMap.get(currentLine) - childHeight - params.bottomMargin;
                    break;

                case ALIGN_CENTER:
                default:
//                    int holdHeight = height - getPaddingTop() - getPaddingBottom();
//                    curTop = (holdHeight - childHeight) / 2;
                    int childDifHeight = lineMaxHeightMap.get(currentLine) - childHeight - params.topMargin - params.bottomMargin;
                    curTop = verticalOffset + childDifHeight/2;
                    break;
            }

            //do the layout
            child.layout(curLeft, curTop, curLeft + childWidth, curTop + childHeight);

            curLeft += childWidth + params.rightMargin;

        }
    }

    /**
     * 设定最大宽度,可以使用具体数值(大于等于0,单位dp),也可以使用相对的数值{@link #MAX_WIDTH_PARENT}, {@link #MAX_WIDTH_DEVICE}
     * , {@link #MAX_WIDTH_UNSPECIFIC}
     *
     * @param size
     */
    public void setMaxWidth(int size) {
        setMaxWidth(TypedValue.COMPLEX_UNIT_DIP, size);
    }

    /**
     * 设定最大宽度,可以使用具体数值(大于等于0,单位{@link TypedValue}),
     * 也可以使用相对的数值{@link #MAX_WIDTH_PARENT}, {@link #MAX_WIDTH_DEVICE}, {@link #MAX_WIDTH_UNSPECIFIC}
     *
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     */
    public void setMaxWidth(int unit, int size) {
        if (size >= 0) {
            Context c = getContext();
            Resources r;
            if (c == null) {
                r = Resources.getSystem();
            } else {
                r = c.getResources();

            }
            maxWidth = (int) TypedValue.applyDimension(unit, size, r.getDisplayMetrics());
        } else if (size == MAX_WIDTH_PARENT || size == MAX_WIDTH_DEVICE) {
            maxWidth = size;
        } else {
            maxWidth = MAX_WIDTH_UNSPECIFIC;
        }
        requestLayout();
    }

    public void setMaxWidthScale(float scale) {
        maxWidthScale = scale;
        requestLayout();
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
