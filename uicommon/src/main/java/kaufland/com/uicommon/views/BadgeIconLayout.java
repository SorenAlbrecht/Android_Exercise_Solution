package kaufland.com.uicommon.views;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.text.DynamicLayout;
import android.text.Layout;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;
import android.widget.FrameLayout;
import androidx.annotation.DimenRes;
import androidx.annotation.IdRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import kaufland.com.uicommon.R;
import org.androidannotations.annotations.EView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@EView
public class BadgeIconLayout extends FrameLayout {

    public static final int CIRCLE_BADGE_GRAVITY_RIGHT_TOP = 1;

    public static final int CIRCLE_BADGE_GRAVITY_RIGHT_BOTTOM = 2;

    public static final int OVAL_BADGE_GRAVITY_RIGHT_TOP = 3;

    public static final int CIRCLE_BADGE_GRAVITY_RIGHT_CENTER = 6;

    public static final int OVAL_BADGE_GRAVITY_RIGHT_BOTTOM = 4;

    public static final int CIRCLE_BADGE_NO_GRAVITY = 5;

    private int mBadgeGravity;

    private CharSequence mBadgeCount;

    private float mBadgeRadius;

    private float mBadgeBorderWidth;

    private int mReferenceId = -1;

    private float mPaddingRightLeft;

    private float mPaddingTopBottom;

    private float mTextPadding;

    private Paint mBorderStroke = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Paint mFillStroke = new Paint(Paint.ANTI_ALIAS_FLAG);

    private Rect mLocalRect = new Rect();

    private Rect mBadgeRect = new Rect();

    private boolean mBadgeInvisible;

    private TextPaint mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);

    public BadgeIconLayout(@NonNull Context context) {
        super(context);
        initAttributes(null);
    }

    public BadgeIconLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initAttributes(attrs);
    }

    public BadgeIconLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAttributes(attrs);
    }

    public BadgeIconLayout(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initAttributes(attrs);
    }

    private void initAttributes(AttributeSet attrs) {
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.BadgeIconLayout_);

        if (typedArray == null) {
            return;
        }

        mBadgeGravity = typedArray.getInteger(R.styleable.BadgeIconLayout__vBadgeGravity, 0);
        mBadgeCount = mBadgeCount != null ? mBadgeCount : typedArray.getString(R.styleable.BadgeIconLayout__vBadgeCount);
        mBorderStroke.setColor(typedArray.getColor(R.styleable.BadgeIconLayout__vBadgeBorderColor, Color.BLACK));
        mFillStroke.setColor(typedArray.getColor(R.styleable.BadgeIconLayout__vBadgeFillColor, ContextCompat.getColor(getContext(), R.color.kis_white_opacity)));
        mTextPaint.setColor(typedArray.getColor(R.styleable.BadgeIconLayout__vBadgeCountColor, Color.WHITE));
        mTextPaint.setTextSize(typedArray.getDimension(R.styleable.BadgeIconLayout__vBadgeFontSize, dpToPx(8)));
        mBadgeRadius = typedArray.getDimension(R.styleable.BadgeIconLayout__vBadgeRadius, dpToPx(10));
        mPaddingRightLeft = typedArray.getDimension(R.styleable.BadgeIconLayout__vBadgePaddingRightLeft, 0);
        mPaddingTopBottom = typedArray.getDimension(R.styleable.BadgeIconLayout__vBadgePaddingTopBottom, 0);
        mTextPadding = typedArray.getDimension(R.styleable.BadgeIconLayout__vBadgeTextPadding, dpToPx(4));
        mReferenceId = typedArray.getResourceId(R.styleable.BadgeIconLayout__vBadgeDependOnView, -1);

        mBadgeBorderWidth = dpToPx(1);
        if (mFillStroke.getColor() == Color.TRANSPARENT) {
            mBorderStroke.setStyle(Paint.Style.STROKE);
            mBorderStroke.setStrokeWidth(mBadgeBorderWidth);
        }

        mTextPaint.setTextAlign(TextPaint.Align.CENTER);
        //TODO doIt
//        mTextPaint.setTypeface(TypefaceGenerator_.getInstance_(getContext()).getKauflandBold());
        typedArray.recycle();
    }

    public void setBorderColor(int color) {
        mBorderStroke.setColor(color);
    }

    public int getFillColor() {
        return mFillStroke.getColor();
    }

    public void setFillColor(int color) {
        mFillStroke.setColor(color);
    }

    public void setBadgeRadius(int radius) {
        mBadgeRadius = radius;
    }

    public void setBadgeBorderWidth(int borderWidth) {
        mBadgeBorderWidth = borderWidth;
    }

    public void setBadgePaddingRightLeft(float paddingRightLeft) {
        mPaddingRightLeft = paddingRightLeft;
    }

    public void setBadgeFont(Typeface typeface) {
        mTextPaint.setTypeface(typeface);
        invalidate();
    }

    public void setBadgeInnerTextSize(@DimenRes int id) {
        mTextPaint.setTextSize(getResources().getDimensionPixelOffset(id));
        invalidate();
    }

    public void setGravity(int gravity) {

        List<Integer> gravityConstants = new ArrayList<>(Arrays.asList(new Integer[]{CIRCLE_BADGE_GRAVITY_RIGHT_TOP, CIRCLE_BADGE_GRAVITY_RIGHT_BOTTOM, OVAL_BADGE_GRAVITY_RIGHT_TOP, CIRCLE_BADGE_GRAVITY_RIGHT_CENTER, OVAL_BADGE_GRAVITY_RIGHT_BOTTOM, CIRCLE_BADGE_NO_GRAVITY}));
        if (gravityConstants.contains(gravity)) {
            mBadgeGravity = gravity;
        }
    }

    public CharSequence getBadgeCount() {
        return mBadgeCount;
    }

    public void setBadgeCount(CharSequence badgeCount) {
        mBadgeCount = badgeCount;
        invalidate();
    }

    private float dpToPx(int value) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, value, getResources().getDisplayMetrics());
    }

    private void calculateBadgeRect() {
        int textWidth;
        int textHeight;
        Rect textBounds = new Rect();

        if (mBadgeCount != null && !"".equals(mBadgeCount)) {
            mTextPaint.getTextBounds(mBadgeCount.toString(), 0, mBadgeCount.length(), textBounds);
            textWidth = mBadgeCount.length() == 1 ? (int) (textBounds.width() + mTextPadding) * 2 : (int) ((textBounds.width()) + mTextPadding);
            textHeight = (int) (textBounds.height() + mTextPadding);
        } else {
            textWidth = 0;
            textHeight = 0;
        }

        int fullWidth = (int) (2 * mBadgeRadius);
        switch (mBadgeGravity) {
            case CIRCLE_BADGE_GRAVITY_RIGHT_BOTTOM:
                mBadgeRect = new Rect(mLocalRect.right - fullWidth, mLocalRect.bottom - fullWidth, mLocalRect.right, mLocalRect.bottom);
                break;
            case CIRCLE_BADGE_GRAVITY_RIGHT_TOP:
                mBadgeRect = new Rect(mLocalRect.right - fullWidth, mLocalRect.top, mLocalRect.right, mLocalRect.top + fullWidth);
                break;
            case CIRCLE_BADGE_GRAVITY_RIGHT_CENTER:
                mBadgeRect = new Rect(mLocalRect.right - fullWidth, mLocalRect.centerY() - (fullWidth / 2), mLocalRect.right, mLocalRect.centerY() + (fullWidth / 2));
                break;
            case OVAL_BADGE_GRAVITY_RIGHT_BOTTOM:
                mBadgeRect = new Rect((int) (mLocalRect.right - textWidth - mPaddingRightLeft), (int) (mLocalRect.bottom - textHeight - mPaddingTopBottom), (int) (mLocalRect.right - mPaddingRightLeft + mTextPadding), (int) (mLocalRect.bottom - mPaddingTopBottom + mTextPadding));
                break;
            case OVAL_BADGE_GRAVITY_RIGHT_TOP:
                mBadgeRect = new Rect((int) (mLocalRect.right - textWidth - mPaddingRightLeft), (int) (mLocalRect.top + mPaddingTopBottom + mTextPadding), (int) (mLocalRect.right - mPaddingRightLeft + mTextPadding), mLocalRect.top + textWidth);
                break;
            default:
                mBadgeRect = new Rect(mLocalRect);
                break;
        }
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        if (mReferenceId != -1) {
            View child = findViewById(mReferenceId);
            Rect offsetViewBounds = new Rect();
            child.getDrawingRect(offsetViewBounds);
            offsetDescendantRectToMyCoords(child, offsetViewBounds);
            mLocalRect = offsetViewBounds;
        } else {
            getLocalVisibleRect(mLocalRect);
        }
        calculateBadgeRect();
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        super.dispatchDraw(canvas);

        if (mBadgeCount != null && !"".equals(mBadgeCount) && !mBadgeInvisible) {

            if (mBadgeGravity == CIRCLE_BADGE_GRAVITY_RIGHT_BOTTOM || mBadgeGravity == CIRCLE_BADGE_GRAVITY_RIGHT_TOP || mBadgeGravity == CIRCLE_BADGE_NO_GRAVITY || mBadgeGravity == CIRCLE_BADGE_GRAVITY_RIGHT_CENTER) {
                canvas.drawCircle(mBadgeRect.centerX() + mPaddingRightLeft, mBadgeRect.centerY() + mPaddingTopBottom, mBadgeRadius, mBorderStroke);
                canvas.drawCircle(mBadgeRect.centerX() + mPaddingRightLeft, mBadgeRect.centerY() + mPaddingTopBottom, mBadgeRadius - mBadgeBorderWidth, mFillStroke);
                DynamicLayout dynamicLayout = new DynamicLayout(mBadgeCount, mTextPaint, mBadgeRect.width(), Layout.Alignment.ALIGN_NORMAL, 1, 1, true);
                canvas.save();
                canvas.translate(mBadgeRect.centerX() + mPaddingRightLeft, mBadgeRect.centerY() + mPaddingTopBottom - (dynamicLayout.getHeight() / 2));
                dynamicLayout.draw(canvas);
                canvas.restore();
            } else {
                RectF backgroundRect = new RectF(mBadgeRect.left, mBadgeRect.top, mBadgeRect.right, mBadgeRect.bottom);
                RectF borderRect = new RectF(mBadgeRect.left + mBadgeBorderWidth, mBadgeRect.top + mBadgeBorderWidth, mBadgeRect.right - mBadgeBorderWidth, mBadgeRect.bottom - mBadgeBorderWidth);
                canvas.drawRoundRect(backgroundRect, 24, 24, mBorderStroke);
                canvas.drawRoundRect(borderRect, 24, 24, mFillStroke);
                canvas.drawText(mBadgeCount.toString(), backgroundRect.centerX(), backgroundRect.centerY() - ((mTextPaint.descent() + mTextPaint.ascent()) / 2), mTextPaint);
            }
        }
    }

    public void applyBadgeAnimation() {

        ValueAnimator badgeAnimation = ValueAnimator.ofFloat(dpToPx(12), dpToPx(8));
        badgeAnimation.setDuration(300);
        badgeAnimation.addUpdateListener(animation -> {
            mTextPaint.setTextSize((Float) animation.getAnimatedValue());
            requestLayout();
        });
        badgeAnimation.start();
    }

    public void setBadgeVisibility(int visibility) {
        mBadgeInvisible = visibility != VISIBLE;
    }

    public void setReferenceId(@IdRes int referenceId) {
        mReferenceId = referenceId;
        invalidate();
    }
}
