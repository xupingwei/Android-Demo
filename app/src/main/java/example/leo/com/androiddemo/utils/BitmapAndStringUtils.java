package example.leo.com.androiddemo.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.utils
 * @ClassName: BitmapAndStringUtils
 * @Description: java类作用描述
 * @Author: wanglintao
 * @CreateDate: 2019/1/4 15:14
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/4 15:14
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class BitmapAndStringUtils {

    /**
     * 图片转成string
     *
     * @param bitmap
     * @return
     */
    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] appicon = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(appicon, Base64.DEFAULT);

    }

    /**
     * string转成bitmap
     *
     * @param st
     */
    public static Bitmap convertStringToIcon(String st) {
        // OutputStream out;
        Bitmap bitmap = null;
        try {
            // out = new FileOutputStream("/sdcard/aa.jpg");
            byte[] bitmapArray;
            bitmapArray = Base64.decode(st, Base64.DEFAULT);
            bitmap =
                    BitmapFactory.decodeByteArray(bitmapArray, 0,
                            bitmapArray.length);
            // bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);
            return bitmap;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 图片文件转换为指定编码的字符串
     *
     * @param imgFile  图片文件
     */
    public static String file2String(File imgFile) {
        InputStream in = null;
        byte[] data = null;
        //读取图片字节数组
        try{
            in = new FileInputStream(imgFile);
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (IOException e){
            e.printStackTrace();
        }
        //对字节数组Base64编码
        String result = Base64.encode(data,Base64.DEFAULT).toString();
        return result;//返回Base64编码过的字节数组字符串
    }
}
