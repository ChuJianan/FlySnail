package qau.cjn.flysnail.adapter;

import java.util.ArrayList;
import java.util.List;

import qau.cjn.flysnail.been.Datas;
import qau.cjn.flysnail.util.FileUtil;
import qau.cjn.flysnail.util.Path;

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

public class ListActivityAdapter extends BaseAdapter{
	private Context context;
	private LayoutInflater mContainer;
	FileUtil fileUtil=new FileUtil();
	 List<String> list=new ArrayList<String>();
	static class GridItemView { //�Զ�����ͼ
        public ImageView pic;
        public TextView name;
    }
    public  ListActivityAdapter(Context context){
    	this.context=context;
    }

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		 GridItemView gridItemView;
		 mContainer = LayoutInflater.from(context);
		 if(convertView == null) {
	            convertView = mContainer.inflate(R.layout.list_item, null);
	            gridItemView = new GridItemView();
	            gridItemView.pic = (ImageView) convertView.findViewById(R.id.img);
	            gridItemView.name = (TextView) convertView.findViewById(R.id.title);
	            convertView.setTag(gridItemView);
	        } else {
	            gridItemView = (GridItemView) convertView.getTag();
	        }	 
		 gridItemView.name.setText(Datas.datas[position]);
		 list=fileUtil.filename(Path.lpath+Datas.datas[position]+"/");
		 Bitmap bm=BitmapFactory.decodeFile(Path.lpath+Datas.datas[position]+"/"+list.get(0));
		 gridItemView.pic.setImageBitmap(bm);
		 list.clear();
		return convertView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return Datas.datas.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return Path.npath+Datas.datas[position]+"/";
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return 0;
	}
}
