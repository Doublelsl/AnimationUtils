package xxx.xxx

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class AnimationUtils {

    /**
     * 弹窗弹出动画
     * @param view
     */
    public static void dialogEnterAnim(View view){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.5f, 1.0f,1.1f,1, 0.9f, 1);
        scaleX.setDuration(500);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.5f, 1.0f,1.1f,1, 0.9f, 1);
        scaleY.setDuration(500);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX,scaleY);
        animatorSet.start();
    }
    /**
     * 弹窗消失动画
     * @param view
     */
    public static void dialogExitAnim(View view){
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 1f,0.5f, 0f);
        scaleX.setDuration(300);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 1f,0.5f, 0f);
        scaleY.setDuration(300);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX,scaleY);
        animatorSet.start();
    }

    /**
     * 顺序播放View动画
     * @param animatorListener
     * @param views
     */
    public static void starAnim(Animator.AnimatorListener animatorListener,View... views){
        int childCount = views.length;
        AnimatorSet animatorSet = new AnimatorSet();
        AnimatorSet[] animatorSets = new AnimatorSet[childCount];
        for (int i = 0; i < childCount; i++) {
            View view = views[i];
            view.setScaleX(0.8f);
            view.setScaleY(0.8f);
            ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", 0.8f, 1f);
            scaleX.setDuration(500);
            ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", 0.8f, 1f);
            scaleY.setDuration(500);
            AnimatorSet animatorSet1 = new AnimatorSet();
            animatorSet1.playTogether(scaleX,scaleY);
            animatorSets[i]=animatorSet1;
        }
        animatorSet.addListener(animatorListener);
        animatorSet.playSequentially(animatorSets);
        animatorSet.start();

    }

    /**
     * 高度动画(可根据需求修改传入参数)
     * @param view
     * @param height
     */
    public static void starAnimShowHeight(View view, float height) {
        ValueAnimator va = ValueAnimator.ofInt(0, (int)height);

        va.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                //获取当前的height值
                int h =(Integer)valueAnimator.getAnimatedValue();
                //动态更新view的高度
                view.getLayoutParams().height = h;
                view.requestLayout();
            }
        });
        va.setDuration(500);
        va.start();
    }

    /**
     * 控件逐渐透明，最后消失动画
     * @param view
     */
    public static void lockAnim(View view) {
        //创建动画
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        //动画持续时间
        alphaAnimation.setDuration(1000);
        //动画停留在结束的位置
        alphaAnimation.setFillAfter(true);
        //开启动画
        view.startAnimation(alphaAnimation);
        alphaAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    /**
     * 自动划动效果
     * @param view
     * @param startY
     * @param endY
     */
    public static void moveEvent(View view, int startY, int endY){
        int clickX = TDevice.getScreenWidth(view.getContext());
        ValueAnimator anim = ValueAnimator.ofInt(startY, endY);
        anim.setDuration(800);
        final MotionEvent event =
                MotionEvent.obtain(SystemClock.uptimeMillis(), SystemClock.uptimeMillis(), MotionEvent.ACTION_DOWN, clickX, startY, 0);
        view.dispatchTouchEvent(event);
        anim.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                int y = (int) animation.getAnimatedValue();
                event.setLocation(clickX, y);
                event.setAction(MotionEvent.ACTION_MOVE);
                view.dispatchTouchEvent(event);
            }
        });
        anim.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                event.setAction(MotionEvent.ACTION_UP);
                view.dispatchTouchEvent(event);
                event.recycle();
            }
        });
        anim.start();
    }
}
