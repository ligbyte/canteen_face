package com.stkj.common.provider;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import com.stkj.common.core.AppManager;

import java.io.File;

/**
 * 共享文件工具
 */
public class FileProviderHelper {

    public static Uri fromFile(File file) {
        Uri fileUri = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            fileUri = FileProvider.getUriForFile(AppManager.INSTANCE.getApplication(),
                    AppManager.INSTANCE.getApplication().getPackageName() + ".fileprovider",
                    file);
        } else {
            fileUri = Uri.fromFile(file);
        }
        return fileUri;
    }

    public static void addFileReadPermission(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
    }

    public static void addFileWritePermission(Intent intent) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
        }
    }

}
