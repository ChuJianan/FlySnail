package qau.cjn.flysnail.activity;

import qau.cjn.flysnail.adapter.ListActivityAdapter;
import qau.cjn.flysnail.been.Datas;
import qau.cjn.flysnail.util.Path;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.testin.commplatform.TestinAgent;
import com.yunruiinfo.app.flysnail.R;

public class ListMyActivity extends BaseActivity{
	private ListView listView;
	private ListActivityAdapter listActivityAdapter;
	private String aString;
	@Override
	public void onCreate(Bundle savedInstanceState){
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		setContentView(R.layout.activity_list);
		listView=(ListView)findViewById(R.id.listView);
		listView.setCacheColorHint(Color.TRANSPARENT);
		listActivityAdapter=new ListActivityAdapter(this);
		listView.setAdapter(listActivityAdapter);
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
					long arg3) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(ListMyActivity.this,GridActivity.class);
					intent.putExtra("path", Path.npath+Datas.datas[arg2]+"/");
				
				intent.putExtra("nub", arg2);
				
				startActivity(intent);
			}
		});
	}
}
