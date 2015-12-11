package qau.cjn.flysnail.activity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import qau.cjn.flysnail.adapter.GridMainViewAdapter;
import qau.cjn.flysnail.been.Datas;
import qau.cjn.flysnail.been.PicBits;
import qau.cjn.flysnail.util.Comparatorlist;
import qau.cjn.flysnail.util.FileUtil;
import qau.cjn.flysnail.util.Path;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.testin.commplatform.TestinAgent;
import com.yunruiinfo.app.flysnail.R;

public class GridActivity extends BaseActivity{

	private GridView gridView;
	String path;
	int nub;
	private GridMainViewAdapter gridMainViewAdapter;
	private  List<PicBits> pList=new ArrayList<PicBits>();
	private List<String>pathList=new ArrayList<String>();
	FileUtil fileUtil=new FileUtil();
	Comparatorlist comparatorlist=new Comparatorlist();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		setContentView(R.layout.index);
		 path=getIntent().getStringExtra("path");
		 nub=getIntent().getExtras().getInt("nub");
		pathList=fileUtil.filename(Path.lpath+Datas.datas[nub]+"/");
		Collections.sort(pathList, comparatorlist);
		for (int i = 0; i <pathList.size(); i++) {
			PicBits picBits=new PicBits();
			picBits.setName(pathList.get(i));
			pList.add(picBits);
		}
		gridView=(GridView)findViewById(R.id.gridView5);
		gridView.setBackgroundColor(Color.WHITE);
		gridView.setCacheColorHint(Color.TRANSPARENT);
		gridMainViewAdapter=new GridMainViewAdapter(this, pList, path);
		gridView.setAdapter(gridMainViewAdapter);
		gridView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
			Intent intent=new Intent(GridActivity.this,MyGalleryActivity.class);
			intent.putExtra("path", path);
			intent.putExtra("i", arg2);
			intent.putExtra("nub", nub);
			startActivity(intent);
			}
		});
	}
}
