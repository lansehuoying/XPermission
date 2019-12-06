package com.nsitd.permissionlib;

import android.app.Activity;
import android.util.Log;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.util.List;

@Aspect
public class MyPermissionParser  {
    private static final String TAG = "MyPermissionParser";
    Object proceed=null;
    @Pointcut("execution(@com.nsitd.permissionlib.MyPermission * *(..))")
    public void check(){}
    @Around("check()")
    public Object jointPoint(final ProceedingJoinPoint joinPoint) throws Throwable{

        Activity context=(Activity) joinPoint.getThis();
        MethodSignature methodSignature= (MethodSignature) joinPoint.getSignature();
        MyPermission myPermission = methodSignature.getMethod().getAnnotation(MyPermission.class);
        String[] value=myPermission.value();
        int requestCode=myPermission.requestCode();
        for (String s : value) {
            Log.d(TAG, "request permissions: "+s);
        }


        PermissionActivity.startRequestPermission(context, value, requestCode, new PermissionListener() {
            @Override
            public void permissionGranted() {
                try {
                    proceed = joinPoint.proceed();
                } catch (Throwable throwable) {
                    throwable.printStackTrace();
                }
            }

            @Override
            public void permissionRefused(int requestCode, List<String> permissions) {
                for (String permission : permissions) {
                    Log.d(TAG, "permissionRefused: "+permission);
                }

                proceed=null;
            }


        });
        return proceed;


    }


}
