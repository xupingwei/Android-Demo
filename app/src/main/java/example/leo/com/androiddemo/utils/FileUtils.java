package example.leo.com.androiddemo.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import example.leo.com.androiddemo.R;

/**
 * @ProjectName: AndroidDemo
 * @Package: example.leo.com.androiddemo.utils
 * @ClassName: FileUtils
 * @Description: 文件操作工具类
 * @Author: wanglintao
 * @CreateDate: 2019/1/3 17:34
 * @UpdateUser: 更新者
 * @UpdateDate: 2019/1/3 17:34
 * @UpdateRemark: 更新说明
 * @Version: 1.0
 */
public class FileUtils {
    /**
     * 文档类型
     */
    public static final int TYPE_DOC = 0;
    /**
     * apk类型
     */
    public static final int TYPE_APK = 1;
    /**
     * 压缩包类型
     */
    public static final int TYPE_ZIP = 2;


    //向指定的文件中写入指定的数据
    public static void writeFileData(String filename, String content) {
        try {
            FileOutputStream fos = new FileOutputStream(filename);//获得FileOutputStream
            //将要写入的字符串转换为byte数组
            byte[] bytes = content.getBytes();
            //将byte数组写入文件
            fos.write(bytes);
            //关闭文件输出流
            fos.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //打开指定文件，读取其数据，返回字符串对象
    public static String readFileData(String fileName) {
        StringBuilder sb = new StringBuilder("");
        try {
            FileInputStream fis = new FileInputStream(fileName);
            //获取文件长度
            int lenght = fis.available();
            byte[] buffer = new byte[lenght];
            int len = fis.read(buffer);
            //读取文件内容
            while (len > 0) {
                sb.append(new String(buffer, 0, len));

                //继续将数据放到buffer中
                len = fis.read(buffer);
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 判断文件是否存在
     *
     * @param path 文件的路径
     * @return
     */
    public static boolean isExists(String path) {
        File file = new File(path);
        return file.exists();
    }

    public static int getFileType(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".doc") || path.endsWith(".docx") || path.endsWith(".xls") || path.endsWith(".xlsx")
                || path.endsWith(".ppt") || path.endsWith(".pptx")) {
            return TYPE_DOC;
        } else if (path.endsWith(".apk")) {
            return TYPE_APK;
        } else if (path.endsWith(".zip") || path.endsWith(".rar") || path.endsWith(".tar") || path.endsWith(".gz")) {
            return TYPE_ZIP;
        } else {
            return -1;
        }
    }


    /**
     * 通过文件名获取文件图标
     */
    public static int getFileIconByPath(String path) {
        path = path.toLowerCase();
        int iconId = R.mipmap.ic_launcher_round;
        if (path.endsWith(".txt")) {
            iconId = R.mipmap.ic_launcher_round;
        } else if (path.endsWith(".doc") || path.endsWith(".docx")) {
            iconId = R.mipmap.ic_launcher_round;
        } else if (path.endsWith(".xls") || path.endsWith(".xlsx")) {
            iconId = R.mipmap.ic_launcher_round;
        } else if (path.endsWith(".ppt") || path.endsWith(".pptx")) {
            iconId = R.mipmap.ic_launcher_round;
        } else if (path.endsWith(".xml")) {
            iconId = R.mipmap.ic_launcher_round;
        } else if (path.endsWith(".htm") || path.endsWith(".html")) {
            iconId = R.mipmap.ic_launcher_round;
        }
        return iconId;
    }

    /**
     * 是否是图片文件
     */
    public static boolean isPicFile(String path) {
        path = path.toLowerCase();
        if (path.endsWith(".jpg") || path.endsWith(".jpeg") || path.endsWith(".png")) {
            return true;
        }
        return false;
    }


    /**
     * 判断SD卡是否挂载
     */
    public static boolean isSDCardAvailable() {
        if (Environment.MEDIA_MOUNTED.equals(Environment
                .getExternalStorageState())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 从文件的全名得到文件的拓展名
     *
     * @param filename
     * @return
     */
    public static String getExtFromFilename(String filename) {
        int dotPosition = filename.lastIndexOf('.');
        if (dotPosition != -1) {
            return filename.substring(dotPosition + 1, filename.length());
        }
        return "";
    }

    /**
     * 读取文件的修改时间
     *
     * @param f
     * @return
     */
    public static String getModifiedTime(File f) {
        Calendar cal = Calendar.getInstance();
        long time = f.lastModified();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        cal.setTimeInMillis(time);
        // System.out.println("修改时间[2] " + formatter.format(cal.getTime()));
        // 输出：修改时间[2] 2009-08-17 10:32:38
        return formatter.format(cal.getTime());
    }


}
