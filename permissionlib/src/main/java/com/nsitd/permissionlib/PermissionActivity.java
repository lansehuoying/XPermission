package com.nsitd.permissionlib;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionActivity extends Activity {
    private static PermissionListener mPermissionListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_permission);
        String []permissions=getIntent().getStringArrayExtra("permissions");
        int requestCode=getIntent().getIntExtra("requestCode",0);
        if(permissions==null||permissions.length==0){
            mPermissionListener=null;
            finish();
        }
        requestPermission(permissions,requestCode);
    }
    public static void startRequestPermission(Context context,String[] permissions,int requestCode,PermissionListener permissionListener){
        Intent intent=new Intent(context,PermissionActivity.class);
        intent.putExtra("permissions",permissions);
        intent.putExtra("requestCode",requestCode);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        context.startActivity(intent);
        mPermissionListener=permissionListener;
    }
    /**
     * 申请权限
     *
     * @param permissions permission list
     */
    private void requestPermission(String[] permissions,int requestCode) {

        if (PermissionUtil.hasSelfPermissions(this, permissions)) {
            //all permissions granted
            if (mPermissionListener != null) {
                mPermissionListener.permissionGranted();
                mPermissionListener = null;
            }
            finish();
        } else {
            //request permissions
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        //all permissions granted
        if(PermissionUtil.verifyPermissions(grantResults)){
            if(mPermissionListener!=null){
                mPermissionListener.permissionGranted();
            }
        }else {
            List<String> permissionList=new ArrayList<>();
            //拒绝并且选择不再提示
           /* if(PermissionUtil.shouldShowRequestPermissionRationale(PermissionActivity.this,permissions)){
                for (int i=0;i<grantResults.length ;i++) {
                    int grantResult=grantResults[i];
                    if(grantResult!= PackageManager.PERMISSION_GRANTED){
                        permissionList.add(permissions[i]);
                    }
                }
            }*/
            if(mPermissionListener!=null){
                mPermissionListener.permissionRefused(requestCode,permissionList);
            }
        }
        mPermissionListener=null;
        finish();
    }
}
