package example.leo.com.androiddemo.utils;

import java.io.FileInputStream;
import java.io.FileOutputStream;

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
            while(len > 0){
                sb.append(new String(buffer,0,len));

                //继续将数据放到buffer中
                len = fis.read(buffer);
            }
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sb.toString();
    }
}
