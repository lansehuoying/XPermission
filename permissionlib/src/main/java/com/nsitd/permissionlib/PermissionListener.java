package com.nsitd.permissionlib;

import java.util.List;

public interface PermissionListener {
    void permissionGranted();
    void permissionRefused(int requestCode, List<String> permissions);

}
