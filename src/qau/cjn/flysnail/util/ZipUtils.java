package qau.cjn.flysnail.util;

import java.io.*;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.zip.ZipException;

import org.apache.tools.zip.ZipEntry;
import org.apache.tools.zip.ZipFile;

import android.content.Context;
import android.widget.Toast;


/**
 * Java utils 实现的Zip工具
 *
 * @author once
 */
public class ZipUtils {
	private static final int BUFF_SIZE = 1024 * 1024; // 1M Byte
    /**
     * 使用 org.apache.tools.zip.ZipFile 解压文件，它与 java 类库中的
     * java.util.zip.ZipFile 使用方式是一新的，只不过多了设置编码方式的
     * 接口。
     * 
     * 注，apache 没有提供 ZipInputStream 类，所以只能使用它提供的ZipFile
     * 来读取压缩文件。
     * @param archive 压缩包路径
     * @param decompressDir 解压路径
     * @throws IOException
     * @throws FileNotFoundException
     * @throws ZipException
     */
    public static boolean readByApacheZipFile(String archive, String decompressDir,Context mContext)
            throws IOException, FileNotFoundException, ZipException {
        BufferedInputStream bi;
        Toast.makeText(mContext, "正在解压，完成后会自动重启，请不要有任何操作", Toast.LENGTH_LONG).show();
        ZipFile zf = new ZipFile(archive, "GBK");//支持中文

        Enumeration e = zf.getEntries();
        while (e.hasMoreElements()) {
            ZipEntry ze2 = (ZipEntry) e.nextElement();
            String entryName = ze2.getName();
            String path = decompressDir + "/" + entryName;
            if (ze2.isDirectory()) {
                System.out.println("正在创建解压目录 - " + entryName);
                File decompressDirFile = new File(path);
                if (!decompressDirFile.exists()) {
                    decompressDirFile.mkdirs();
                }
            } else {
                System.out.println("正在创建解压文件 - " + entryName);
                String fileDir = path.substring(0, path.lastIndexOf("/"));
                File fileDirFile = new File(fileDir);
                if (!fileDirFile.exists()) {
                    fileDirFile.mkdirs();
                }
                BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(
                        decompressDir + "/" + entryName));

                bi = new BufferedInputStream(zf.getInputStream(ze2));
                byte[] readContent = new byte[1024];
                int readCount = bi.read(readContent);
                while (readCount != -1) {
                    bos.write(readContent, 0, readCount);
                    readCount = bi.read(readContent);
                }
                bos.close();
            }
        }
        zf.close();
		return true;
    }
}
