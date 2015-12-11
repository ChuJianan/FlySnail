package qau.cjn.flysnail.adapter;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

import qau.cjn.flysnail.been.PicBits;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.yunruiinfo.app.flysnail.R;

public class GridMainViewAdapter extends BaseAdapter{
	private Context mContext;
	List<PicBits> picBits=new ArrayList<PicBits>();
	private String path;
//	 private List<ReaderMenu> mMenus = new ArrayList<ReaderMenu>();    //��ݼ���
	    private LayoutInflater mContainer;  //��ͼ����
//	    public class ReaderMenu {
//	    	public int icon;
//	    	public String name;
//	    	public String value;
//	    	public int id;
//	    	public ReaderMenu(String name, String value, int icon, int id) {
//	    		this.icon = icon;
//	    		this.value = value;
//	    		this.name = name;
//	    		this.id = id;
//	    	}
//	    }
	    static class GridItemView { //�Զ�����ͼ
	        public ImageView pic;
	        public TextView name;
	    }
	public GridMainViewAdapter(Context mContext,List<PicBits> picBits ,String path){
		this.mContext=mContext;
		this.picBits=picBits;
		this.path=path;
	}
	@Override
	public View getView(int position, View convertView, ViewGroup arg2) {
		// TODO Auto-generated method stub
		 GridItemView gridItemView;
		 mContainer = LayoutInflater.from(mContext);
		 if(convertView == null) {
	            convertView = mContainer.inflate(R.layout.main_list, null);
	            gridItemView = new GridItemView();
	            gridItemView.pic = (ImageView) convertView.findViewById(R.id.pic);
	            gridItemView.name = (TextView) convertView.findViewById(R.id.name);
	            convertView.setTag(gridItemView);
	        } else {
	            gridItemView = (GridItemView) convertView.getTag();
	        }
		 
	        PicBits menu = picBits.get(position);
	        String nameString=null;
	        String split = ".";
	        StringTokenizer token = new StringTokenizer(menu.getName(), split);
	        while (token.hasMoreTokens()) {
	        	nameString=token.nextToken();
	        	break;
	        	}	        
	        gridItemView.name.setText(nameString);
	        Bitmap bitmap=BitmapFactory.decodeFile(path+menu.getName());
	        gridItemView.pic.setImageBitmap(bitmap);

	        return convertView;
		
	}
	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return picBits.size();
	}

	@Override
	public Object getItem(int arg0) {
		// TODO Auto-generated method stub
		return picBits.get(arg0);
	}

	@Override
	public long getItemId(int arg0) {
		// TODO Auto-generated method stub
		return 0;
	}



}
