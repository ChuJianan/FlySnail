package qau.cjn.flysnail.util;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.content.Context;

/**
 *  文件夹遍历
 * @author once
 *
 */
public class DirTraversal {

    //no recursion
    public static LinkedList<File> listLinkedFiles(String strPath) {
        LinkedList<File> list = new LinkedList<File>();
        File dir = new File(strPath);
        File file[] = dir.listFiles();
        for (int i = 0; i < file.length; i++) {
            if (file[i].isDirectory())
                list.add(file[i]);
            else
                System.out.println(file[i].getAbsolutePath());
        }
        File tmp;
        while (!list.isEmpty()) {
            tmp = (File) list.removeFirst();
            if (tmp.isDirectory()) {
                file = tmp.listFiles();
                if (file == null)
                    continue;
                for (int i = 0; i < file.length; i++) {
                    if (file[i].isDirectory())
                        list.add(file[i]);
                    else
                        System.out.println(file[i].getAbsolutePath());
                }
            } else {
                System.out.println(tmp.getAbsolutePath());
            }
        }
        return list;
    }

    
    //recursion
    public static ArrayList<File> listFiles(String strPath) {
        return refreshFileList(strPath);
    }

    public static ArrayList<File> refreshFileList(String strPath) {
        ArrayList<File> filelist = new ArrayList<File>();
        File dir = new File(strPath);
        File[] files = dir.listFiles();

        if (files == null)
            return null;
        for (int i = 0; i < files.length; i++) {
            if (files[i].isDirectory()) {
                refreshFileList(files[i].getAbsolutePath());
            } else {
                if(files[i].getName().toLowerCase().endsWith("zip"))
                    filelist.add(files[i]);
            }
        }
        return filelist;
    }
    public static List<String> deepFile(Context ctxDealFile, String path) {
    	List<String> list=new ArrayList<String>();
		try {
			String str[] = ctxDealFile.getAssets().list(path);
			if (str.length > 0) {//如果是目录
				for (String string : str) {
					list.add(string);
//					path = path + "/" + string; 
//				System.out.println("zhoulc:t" + path);
//					// textView.setText(textView.getText()+"t"+path+"t");
//					deepFile(ctxDealFile, path);
//					path = path.substring(0, path.lastIndexOf('/')); 
				}
				
			} 
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return list;
		
		}
}
