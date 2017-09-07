package com.github.azenhuang.praticedemo.view.edittext;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Layout;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextWatcher;
import android.text.style.LeadingMarginSpan;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewStub;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.azenhuang.praticedemo.R;

import static android.util.TypedValue.COMPLEX_UNIT_PX;


/**
 * Created by zhangmeng on 2017/6/19.
 * a EditText with multiple style
 */

public class MtEditText extends LinearLayout {
    public static final int MTEDITTEXT_TYPE_BASIC = 0;
    public static final int MTEDITTEXT_TYPE_BASIC_CLEAR = 1;
    public static final int MTEDITTEXT_TYPE_BASIC_TITLE = 2;
    public static final int MTEDITTEXT_TYPE_BASIC_CLEAR_TITLE = 3;
    //user MTEDITTEXT_TYPE_BASIC_IMAGE_TITLE ,you need to set your own click listener
    public static final int MTEDITTEXT_TYPE_BASIC_IMAGE_TITLE = 4;
    public static final int MTEDITTEXT_TYPE_BASIC_INSIDE_RIGHT_WARNNING = 5;
    public static final int MTEDITTEXT_TYPE_BASIC_OUTSIDE_RIGHT_WARNNING = 6;
    public static final int MTEDITTEXT_TYPE_MULTILINE = 7;
    public static final int MTEDITTEXT_TYPE_MULTILINE_HINT = 8;
    public static final int MTEDITTEXT_TYPE_MULTILINE_HINT_BORDER = 9;
    public static final int MTEDITTEXT_TYPE_MULTILINE_ALL_HINT = 10;
    public static final int MTEDITTEXT_TYPE_COMMENT = 11;
    public static final int MTEDITTEXT_TYPE_MULTICOMMENT = 12;
    public static final int MTEDITTEXT_TYPE_PAY = 13;
    public static final int MTEDITTEXT_TYPE_BASIC_LARGETEXT = 14;
    private EditText mainContentEditText;
    private ImageView rightImageView;
    private TextView titleTextView;
    private TextView insideRightTextView;
    private TextView outsideRightTextView;
    private TextView insideRightButton;
    private TextView insideLeftTextView;
    private View rightContainer;
    private ViewStub viewStub;
    private RelativeLayout relativeContainer;
    private Context context;
    private int mtedittext_type = MTEDITTEXT_TYPE_BASIC;
    private boolean isPayShow = false;
    private RmbSpan rmbSpan;
    private TextWatcher textWatcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            if (mtedittext_type == MTEDITTEXT_TYPE_PAY && !isPayShow) {
                mainContentEditText.setPadding(0, getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_padding_top), getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_padding_right), getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_padding_top));
                setTextSize(COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_text_size));
            }
        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if (mtedittext_type == MTEDITTEXT_TYPE_BASIC_IMAGE_TITLE || mtedittext_type == MTEDITTEXT_TYPE_BASIC_CLEAR_TITLE || MTEDITTEXT_TYPE_BASIC_CLEAR == mtedittext_type) {
                showRightImageView();
            }
            if (mtedittext_type == MTEDITTEXT_TYPE_PAY) {
                if (!isPayShow) {
                    isPayShow = true;
                    setRMBIcon(s);
                } else {
                    setSelection(getLength());
                }

                if (getLength() == 0) {
                    isPayShow = false;
                    setTextSize(COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_hint_text_size));
                    mainContentEditText.setPadding(0, getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_hint_padding_top), getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_padding_right), getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_hint_padding_top));
                }
            }

        }
    };

    public MtEditText(Context context) {
        super(context);
        this.context = context;
        initView(context, null);
    }

    public MtEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initView(context, attrs);
    }

    public MtEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.context = context;
        initView(context, attrs);
    }

    private void initAttr(AttributeSet attrs) {
        addEditextView(attrs);
        TypedArray typeArray = context.obtainStyledAttributes(attrs, R.styleable.MtEditText);
        mtedittext_type = typeArray.getInt(R.styleable.MtEditText_edittexttype, MTEDITTEXT_TYPE_BASIC);
        setMtEditTextType(mtedittext_type);
        typeArray.recycle();
    }

    private void addEditextView(AttributeSet attrs) {
        mainContentEditText = new EditText(context, attrs);
        mainContentEditText.setSingleLine();
        mainContentEditText.setId(R.id.mtedittext_main_content);
        setTextSize(COMPLEX_UNIT_PX, context.getResources().getDimensionPixelSize(R.dimen.commonui_edittext_textsize));
        setTextColor(R.color.commonui_editext_text_color);
        mainContentEditText.setBackgroundColor(Color.TRANSPARENT);
        mainContentEditText.setPadding(getResources().getDimensionPixelSize(R.dimen.commonui_edittext_padding_left), getResources().getDimensionPixelSize(R.dimen.commonui_edittext_padding_right),
                getResources().getDimensionPixelSize(R.dimen.commonui_edittext_padding_right), getResources().getDimensionPixelSize(R.dimen.commonui_edittext_padding_right));
        final int index = relativeContainer.indexOfChild(viewStub);
        relativeContainer.removeViewInLayout(viewStub);
        relativeContainer.addView(mainContentEditText, index, viewStub.getLayoutParams());
        mainContentEditText.addTextChangedListener(textWatcher);
    }


    private void initView(Context context, AttributeSet attrs) {
        setOrientation(VERTICAL);
        View rootView = LayoutInflater.from(context).inflate(R.layout.commonui_editext, this, true);
        rightContainer = rootView.findViewById(R.id.mtedittext_right_container);
        relativeContainer = (RelativeLayout) rootView.findViewById(R.id.mteditext_relative_container);
        viewStub = (ViewStub) rootView.findViewById(R.id.mtedittext_main_content);
//        mainContentEditText=(EditText) rootView.findViewById(R.id.mtedittext_main_content);
        rightImageView = (ImageView) rootView.findViewById(R.id.mtedittext_right_view);
        titleTextView = (TextView) rootView.findViewById(R.id.mtedittext_title);
        insideRightTextView = (TextView) rootView.findViewById(R.id.mtedittext_inside_right_textview);
        outsideRightTextView = (TextView) rootView.findViewById(R.id.mtedittext_outside_right_textview);
        insideLeftTextView = (TextView) rootView.findViewById(R.id.mtedittext_inside_left_textview);
        insideRightButton = (TextView) rootView.findViewById(R.id.mtedittext_inside_right_button);

        if (null != attrs) {
            initAttr(attrs);
        }

    }

    /**
     * if MTEDITTEXT_TYPE_* above cannot satisfy your need ,you can set your own style or connect me zhangmenng11@mietuan.com
     *
     * @param type is one of MTEDITTEXT_TYPE_*
     */
    public void setMtEditTextType(int type) {
        mtedittext_type = type;
        switch (type) {
            case MTEDITTEXT_TYPE_BASIC:
                //do nothing
                break;
            case MTEDITTEXT_TYPE_BASIC_CLEAR:
                setClearUsage();
                break;
            case MTEDITTEXT_TYPE_BASIC_TITLE:
                setTitleUsage();
                break;
            case MTEDITTEXT_TYPE_BASIC_CLEAR_TITLE:
                setTitleClearUsage();
                break;
            case MTEDITTEXT_TYPE_BASIC_IMAGE_TITLE:
                setTitleImageUsage();
                break;
            case MTEDITTEXT_TYPE_BASIC_INSIDE_RIGHT_WARNNING:
                setInsideRightTextView();
                break;
            case MTEDITTEXT_TYPE_BASIC_OUTSIDE_RIGHT_WARNNING:
                setOutSideRightTextView();
                break;

            case MTEDITTEXT_TYPE_MULTILINE:
                setMultiLineEditText();
                break;
            case MTEDITTEXT_TYPE_MULTILINE_HINT:
                setMultiLineWithHintEditText();
                break;
            case MTEDITTEXT_TYPE_MULTILINE_HINT_BORDER:
                setBordereEditText();
                break;
            case MTEDITTEXT_TYPE_MULTILINE_ALL_HINT:
                setAllHintEditText();
                break;
            case MTEDITTEXT_TYPE_COMMENT:
                setCommentEditText();
                break;
            case MTEDITTEXT_TYPE_MULTICOMMENT:
                setMultiLineCommentEditText();
                break;
            case MTEDITTEXT_TYPE_PAY:
                setPayEditText();
                break;
            case MTEDITTEXT_TYPE_BASIC_LARGETEXT:
                setBasicLargeEditText();
                break;
            default:
                break;

        }
    }


    public void setRelativeContainer(int resourceId) {
        relativeContainer.setBackgroundResource(resourceId);
    }

    /**
     * set EditText's hint in MtEditText
     *
     * @param text in EditText
     */
    public void setHint(String text) {
        mainContentEditText.setHint(text);
    }

    public void setGravity(int gravity) {
        mainContentEditText.setGravity(gravity);
    }

    /**
     * @param size unit is sp not dp or px
     */
    public void setTextSize(float size) {
        mainContentEditText.setTextSize(size);
    }

    /**
     * @param unit The desired dimension unit.
     * @param size The desired size in the given units.
     */
    public void setTextSize(int unit, int size) {
        mainContentEditText.setTextSize(unit, size);
    }


    public int getLength() {
        return mainContentEditText.length();
    }

    /**
     * @param size Selection size
     */
    public void setSelection(int size) {
        mainContentEditText.setSelection(size);
    }

    /**
     * @param type EditText input type
     */
    public void setInputType(int type) {
        mainContentEditText.setInputType(type);
    }


    public void setFilters(InputFilter[] filters) {
        mainContentEditText.setFilters(filters);
    }

    /**
     * @param color editext text color,R.id.color
     */
    public void setTextColor(int color) {
        mainContentEditText.setTextColor(ContextCompat.getColor(context, color));
    }

    public void setTextWatcher(TextWatcher textWatcher) {
        mainContentEditText.addTextChangedListener(textWatcher);
    }

    public void setRMBIcon(Editable text) {
        if (rmbSpan == null) {
            rmbSpan = new RmbSpan();
        }
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        builder.setSpan(rmbSpan, 0, 0, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        mainContentEditText.setText(builder, TextView.BufferType.SPANNABLE);
    }

    /**
     * return EditText content in MtEditText
     */
    public String getText() {
        Editable editable = mainContentEditText.getText();
        if (editable != null)
            return editable.toString();
        return "";
    }

    /**
     * set EditText's MaxLines in MtEditText
     *
     * @param lineNumber max lineNumber
     */
    public void setMaxLines(int lineNumber) {
        mainContentEditText.setMaxLines(lineNumber);
    }

    /**
     * set EditText's Lines in MtEditText
     *
     * @param lineNumber set lineNumber in EditText
     */
    public void setLines(int lineNumber) {
        enableMultiLine();
        mainContentEditText.setLines(lineNumber);

    }

    public void enableMultiLine() {
        mainContentEditText.setInputType(InputType.TYPE_TEXT_FLAG_MULTI_LINE);
        mainContentEditText.setGravity(Gravity.TOP);
        mainContentEditText.setSingleLine(false);
        //水平滚动设置为False
        mainContentEditText.setHorizontallyScrolling(false);
    }

    /**
     * set EditText's MinLines in MtEditText
     *
     * @param lineNumber set min lines in MtEditText
     */
    public void setMinLines(int lineNumber) {
        mainContentEditText.setMinLines(lineNumber);
    }

    /**
     * @param resourceId editext Background Resource
     */
    public void setEditTextBackgroundResource(int resourceId) {
        mainContentEditText.setBackgroundResource(resourceId);
    }

    /**
     * set right imageview in MtEditText,if you do not set it it will not show
     *
     * @param drawable right imageview in editext
     */
    public void setRightImageView(Drawable drawable) {
        showRightImageView();
        rightImageView.setImageDrawable(drawable);
    }

    public void showRightImageView() {
        rightContainer.setVisibility(VISIBLE);
        rightImageView.setVisibility(VISIBLE);
    }

    /**
     * set right imageview in MtEditText,if you do not set it it will not show
     *
     * @param resourceId right imageview in editext
     */
    public void setRightImageView(int resourceId) {
//        showRightImageView();
        rightImageView.setImageResource(resourceId);
        //clear imageview
        if (R.drawable.commonui_editext_clear == resourceId) {
            rightImageView.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    //清空文本编辑框
                    mainContentEditText.setText("");
                    rightImageView.setVisibility(GONE);
                }
            });
        }
    }

    /**
     * set title textview in MtEditText,if you do not set it it will not show
     *
     * @param title title in MtEditText
     */
    public void setTitle(String title) {
        titleTextView.setVisibility(VISIBLE);
        titleTextView.setText(title);
    }

    /**
     * set text color of textview in MtEditText
     *
     * @param colorResource reource color of textview in MtEditText in
     */
    public void setTitleColor(int colorResource) {
        titleTextView.setTextColor(ContextCompat.getColor(context, colorResource));
    }

    /**
     * set Inside Right textview in MtEditText,if you do not set it it will not show
     *
     * @param text right button text
     */
    public void setInsideRightButtonText(String text) {
        insideRightButton.setVisibility(VISIBLE);
        insideRightButton.setText(text);
        insideRightButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                if (context.getString(R.string.commonui_editext_anonymous).equals(insideRightButton.getText())) {
                    insideRightButton.setText(context.getString(R.string.commonui_editext_anonymoused));
                } else if (context.getString(R.string.commonui_editext_anonymoused).equals(insideRightButton.getText())) {
                    insideRightButton.setText(context.getString(R.string.commonui_editext_anonymous));
                }
            }
        });
    }

    public void setInsideRightButtonTextColor(int colorResource) {
        insideRightButton.setTextColor(ContextCompat.getColor(context, colorResource));
    }


    /**
     * set Inside Right textview in MtEditText,you can Listener the click event
     *
     * @param clickListener right button set onclick listener
     */
    public void setInsideRightButtonOnclick(OnClickListener clickListener) {
        insideRightButton.setOnClickListener(clickListener);
    }

    /**
     * set Inside Right textview in MtEditText,if you do not set it it will not show
     *
     * @param recourceId recourceId
     */
    public void setInsideRightBackground(int recourceId) {
        insideRightButton.setVisibility(VISIBLE);
        insideRightButton.setBackgroundResource(recourceId);
    }

    /**
     * set Inside Right textview(designed as inside warning) in MtEditText,if you do not set it it will not show
     *
     * @param textSizeSp textSize in unit sp
     */
    public void setInsideRightTextSize(int textSizeSp) {
        showInsideRightView();
        insideRightTextView.setTextSize(textSizeSp);
    }

    public void showInsideRightView() {
        rightContainer.setVisibility(VISIBLE);
        insideRightTextView.setVisibility(VISIBLE);
    }

    /**
     * set Inside Right textview in MtEditText,if you do not set it it will not show
     *
     * @param text text in Right
     */
    public void setInsideRightText(String text) {
        showInsideRightView();
        insideRightTextView.setText(text);
    }

    /**
     * set text color of Inside Right textview in MtEditText
     */
    public void setInsideRightTextColor(int colorResource) {
        insideRightTextView.setTextColor(ContextCompat.getColor(context, colorResource));
    }

    /**
     * set text color of Inside Right textview in MtEditText
     */
    public void setInsideRightTextBackGround(int resourceid) {
        insideRightTextView.setBackgroundResource(resourceid);
    }

    /**
     * set title textview in MtEditText(designed as outside warning),if you do not set it it will not show
     */
    public void setOutsideRightText(String text) {
        showOutsideRightView();
        outsideRightTextView.setText(text);
    }

    public void showOutsideRightView() {
        outsideRightTextView.setVisibility(VISIBLE);
    }

    /**
     * set text color of Outside Right textview in MtEditText
     */
    public void setOutsideRightTextColor(int colorReource) {
        outsideRightTextView.setTextColor(ContextCompat.getColor(context, colorReource));
    }

    /**
     * if you set thie listener ,the the default clean icon will not work.
     */
    public void setRightImageViewOnclickListener(OnClickListener clickListener) {
        rightImageView.setOnClickListener(clickListener);
    }

    public void setInsideLeftText(String text) {
        insideLeftTextView.setVisibility(VISIBLE);
        insideLeftTextView.setText(text);
    }

    private void setClearUsage() {
        setRightImageView(R.drawable.commonui_editext_clear);
    }

    private void setTitleUsage() {
        setTitle(context.getString(R.string.commonui_editext_title));
    }

    private void setTitleClearUsage() {
        setClearUsage();
        setTitleUsage();
    }

    private void setTitleImageUsage() {
        setRightImageView(R.drawable.commonui_action_bar_ic_arrow_right);
        setTitleUsage();
    }

    private void setBasicLargeEditText() {
        setHint(context.getString(R.string.commonui_editext_hint));
        setLines(10);
    }

    private void setPayEditText() {
        setTitle(context.getString(R.string.commonui_editext_pay_title));
        setTitleColor(R.color.commonui_editext_pay_text_color);
        setGravity(Gravity.RIGHT);
        setInputType(InputType.TYPE_CLASS_NUMBER | InputType.TYPE_NUMBER_FLAG_DECIMAL | InputType.TYPE_NUMBER_FLAG_SIGNED);
        mainContentEditText.setPadding(0, getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_hint_padding_top), getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_padding_right), getResources().getDimensionPixelSize(R.dimen.commonui_edittext_pay_hint_padding_top));
        setTextColor(R.color.commonui_editext_warning_color);
        setHint(context.getString(R.string.commonui_editext_pay_hint));
        setFilters(new InputFilter[]{new CashierInputFilter()});
        setSelection(getLength());
    }

    private void setMultiLineCommentEditText() {
        enableMultiLine();
        //below code the same as method setCommentEditText
        setHint(context.getString(R.string.commonui_editext_comment));
        setRelativeContainer(R.drawable.commonui_edittext_container_style);
        setEditTextBackgroundResource(R.drawable.commonui_edittext_comment_style);
    }

    private void setCommentEditText() {
        setHint(context.getString(R.string.commonui_editext_comment));
        setRelativeContainer(R.drawable.commonui_edittext_container_style);
        setEditTextBackgroundResource(R.drawable.commonui_edittext_comment_style);
    }

    private void setAllHintEditText() {
        setLines(4);
        setHint(context.getString(R.string.commonui_editext_hint));
        setInsideLeftText(context.getString(R.string.commonui_editext_left_behind_hint));
        setInsideRightButtonText(context.getString(R.string.commonui_editext_anonymous));
        setInsideRightButtonTextColor(R.color.commonui_editext_text_color);
        setInsideRightBackground(R.drawable.commonui_edittext_right_inside_button_selector);

    }

    private void setMultiLineEditText() {
        setLines(4);
    }

    private void setBordereEditText() {
        setMultiLineWithHintEditText();
        setBackgroundResource(R.drawable.commonui_edittext_cornor);
    }

    private void setMultiLineWithHintEditText() {
        setLines(4);
        setHint(context.getString(R.string.commonui_editext_hint));
        setInsideRightButtonText(context.getString(R.string.commonui_editext_right_below_hint));
        setInsideRightTextColor(R.color.commonui_editext_hint_text_color);
    }


    private void setInsideRightTextView() {
        setTitle(context.getString(R.string.commonui_editext_title));
        setInsideRightText(context.getString(R.string.commonui_editext_warning));
    }

    private void setOutSideRightTextView() {
        setOutsideRightText(context.getString(R.string.commonui_editext_outside_warning));
    }

    class RmbSpan implements LeadingMarginSpan {

        Drawable d = context.getResources().getDrawable(R.drawable.commonui_rmb_icon);

        RmbSpan() {
            d.setBounds(0, 0, d.getIntrinsicWidth(), d.getIntrinsicHeight());
        }

        @Override
        public int getLeadingMargin(boolean first) {
            return d.getIntrinsicWidth();
        }

        @Override
        public void drawLeadingMargin(Canvas canvas, Paint p, int x, int dir, int top, int baseline, int bottom, CharSequence text, int start, int end, boolean first, Layout layout) {
            canvas.save();
            x = (int) layout.getLineLeft(0);
            int transY = bottom - d.getBounds().bottom;
            transY -= p.getFontMetricsInt().descent;
            canvas.translate(x, transY);
            d.draw(canvas);
            canvas.restore();
        }
    }
}
