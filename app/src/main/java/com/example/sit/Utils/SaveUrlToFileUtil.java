package com.example.sit.Utils;

import android.graphics.Bitmap;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class SaveUrlToFileUtil {
    public static File getFile(Bitmap bitmapDate) {
        ByteArrayOutputStream bos = null;
        BufferedOutputStream bs = null;
        FileOutputStream fos = null;
        File file = null;
        try {
            bos = new ByteArrayOutputStream();
            bitmapDate.compress(Bitmap.CompressFormat.JPEG, 100, bos);
            String path = BitmapUtil.getAboSdPath() + "/FileTemp/";
            File dirFile = new File(path);
            if (!dirFile.exists()){
                dirFile.mkdirs();
            }
            file = new File(path + BitmapUtil.createShortUUID());
            fos = new FileOutputStream(file);
            bs = new BufferedOutputStream(fos);
            bs.write(bos.toByteArray());

        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bos != null){
                try {
                    bos.flush();
                    bos.close();
                    fos.close();
                    bs.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file;
    }
}
