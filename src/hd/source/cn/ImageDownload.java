package hd.source.cn;

import hd.utils.cn.LogUtil;
import hd.utils.cn.Utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * 图片下载
 * 
 * @author xudl(Wisea)
 * 
 */
public class ImageDownload {
    public static final String TAG = "ImageDownload";

    /**
     * 下载图片
     * 
     * @param fileName
     *            文件名
     * @param urlString
     *            url地址
     * @return
     */
    public static File download(File file, String urlString) {
        HttpURLConnection urlConnection;
        InputStream inputStream = null;
        FileOutputStream fileOutPutStream = null;
        try {
            final URL url = new URL(urlString);
            urlConnection = (HttpURLConnection) url.openConnection();
            inputStream = urlConnection.getInputStream();
            byte[] bytes = new byte[inputStream.available()];
            int read = inputStream.read(bytes);
            fileOutPutStream = new FileOutputStream(file);
            fileOutPutStream.write(bytes, 0, read);
            fileOutPutStream.flush();
        } catch (Exception e) {
            LogUtil.e(TAG, "Error in download", e);
        } finally {
            try {
                fileOutPutStream.close();
                inputStream.close();
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        return file;
    }
}
