package cn.sancell.xingqiu.interfaces;

import android.util.Log;
import android.view.View;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;

import cn.sancell.xingqiu.util.XClickUtil;

/**
 * Created by ai11 on 2019/6/4.
 */

@Aspect
public class SingleClickAspect {
    final String TAG = SingleClickAspect.class.getSimpleName();
    private boolean canDoubleClick = false;

    @Around("@annotation(cn.sancell.xingqiu.interfaces.DoubleClick)")
    public void beforeEnableDoubleClcik(JoinPoint joinPoint) throws Throwable {
        canDoubleClick = true;
    }

    private View mLastView;

    @Around("(execution(* butterknife.internal.DebouncingOnClickListener.doClick(..))||execution(* android.view.View.OnClickListener.onClick(..)))  && target(Object) && this(Object)")
    public void OnClickListener(ProceedingJoinPoint joinPoint) throws Throwable {
        Object[] objects = joinPoint.getArgs();
        View view = objects.length == 0 ? null : (View) objects[0];
        Log.e(TAG, "OnClick:" + view);
        if (view != mLastView || canDoubleClick || !XClickUtil.isDoubleClick()) {
            joinPoint.proceed();
            Log.e(TAG, "OnClick1:" + view);
            canDoubleClick = false;
        }
        mLastView = view;
    }
}
