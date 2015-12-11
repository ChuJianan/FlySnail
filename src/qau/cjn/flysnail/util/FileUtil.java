package qau.cjn.flysnail.util;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class FileUtil {
	  private final static int PIC_FILE_SIZE_MIN = 5;
	  
	  private final static int PIC_FILE_SIZE_MAX = 300;
private List<String> list=new ArrayList<String>();
	public List<String> filename(String path){
		File file=new File(path);
		File[] subFile = file.listFiles();
		 if(subFile!= null){  
		    	for (int i = 0; i < subFile.length; i++) {
			    
			        list.add(subFile[i].getName());
		        }
		    	
		    }
		 return list;
	}
	public void setGallery(){
		String path = Environment.getExternalStorageDirectory().getPath();
		File file=new File(path+"/.Gallery");
		
		 if (!file.exists()) {
			 file.mkdirs();
			
			  }
	}

	  public String searchFile(String keyword,File filepath)
	  { 
		  File[] files = filepath.listFiles();
	    
	       if (Environment.getExternalStorageState().equals(
	                  Environment.MEDIA_MOUNTED))
	       {
	        
	          
	          if (files.length > 0)
	          {
	                  for (File file : files)
	                  {
	                   if (file.isDirectory())
	                   {
	                 
	                    if(file.canRead()){
	                     searchFile(keyword,file); 
	                    }
	      }
	                   else {  
	                     
	                              try {   
	                               if (file.getName().indexOf(keyword) > -1||file.getName().indexOf(keyword.toUpperCase()) > -1) 
	                               {   
	                                 return file.getPath();
	                                   }   
	                             } catch(Exception e) {   

	                              }   
	                     }
	                  }
	       }
	      }
	       return null;
	  }
	  String path = Environment.getExternalStorageDirectory().toString()+ "/.Gallery/date/"; 
	    List<String> lstPath=null; 
	    public void ListFiles(String path2, List<String> lstPaths){
	      File file = new File(path2); 
	      if(file == null)
	        return;
	      if(file.isDirectory() && path.length() > 4) {       
	      String reName = path.substring(0, 4);
	      if(reName.equals("/sys") || reName.equals("/tmp") ||
	            reName.equals("/pro"))
	          return;
	      }
	      File[] fs = file.listFiles();
	      if(fs==null)
	        return;
	    for(File f : fs){
	      if(f==null)
	        continue;
	      String fName = f.getName(); 
	      String htx =fName.substring(fName.lastIndexOf(".") + 1,
	      fName.length()).toLowerCase();
	      if(htx.equals("png") || htx.equals("jpg") ||
	          htx.equals("gif") || htx.equals("bmp")){
	        if(fileSizeValidity(f.getPath())){
	          lstPaths.add(f.getPath());
	          Log.v("***PIC_FILE***", fName);
	        }
	      } else {
	          //Log.v("***PIC_PATH***", f.getPath());
	      }
	      path = f.getAbsolutePath();
	      if(f.isDirectory() == true) {
	        ListFiles(path, lstPaths);      
	      }
	      lstPath=lstPaths;
	     /* ImagePath pa=new ImagePath(); 
	      pa.setImagepath(lstPath);*/
	   }
	   }
	    private boolean fileSizeValidity(String path) {
	        File f = new File(path);
	        if(f.exists()) {
	          int cur = 0;
	          FileInputStream fis = null;
	        try {
	          fis = new FileInputStream(f);
	          cur = fis.available()/1000;
	        } catch (FileNotFoundException e) {
	          e.printStackTrace();
	        } catch (IOException e) {
	          e.printStackTrace();
	        } finally {
	          try {
	            fis.close();            
	          } catch (IOException e) {
	            e.printStackTrace();
	          }
	        }
	    
	        if(cur>=PIC_FILE_SIZE_MIN && cur<=PIC_FILE_SIZE_MAX)
	          return true;
	        }
	        return false;
	      }
}
