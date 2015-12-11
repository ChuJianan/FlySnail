package qau.cjn.flysnail.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import qau.cjn.flysnail.graphics.Point;
import qau.cjn.flysnail.graphics.PointsData;
import qau.cjn.flysnail.graphics.PrimaryPointsView;
import qau.cjn.flysnail.util.wutil.StringUtil;

import com.yunruiinfo.app.flysnail.R;
import com.testin.commplatform.TestinAgent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.widget.Toast;

public class PrimaryPointsActivity extends BaseActivity {
	private Toast toast;
	private PrimaryPointsView mPointsView;
	private PointsData mPointsData;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		setContentView(R.layout.activity_points_primary);
		
		String path = getIntent().getStringExtra("path");
		if (StringUtil.isEmpty(path)) {
			showToast("参数无效");
			finish();
			return;
		}
		mPointsView = (PrimaryPointsView) findViewById(R.id.points);
		setPointsData(path, mPointsView);
	}
	//直接返回
	@Override
	public void onBackPressed() {
		if (!mPointsView.undo()) {
			super.onBackPressed();
		}
	}
	private void showToast(CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
		} else {
			toast.setText(message);
		}
		toast.show();
	}
	public void setPointsData(String path, PrimaryPointsView view) {
		int w = 0, h = 0;
		Bitmap bg = null, ri = null;
		List<Point> kps = new ArrayList<Point>();
		try {
			InputStream in = getAssets().open("points/primary/" + path + "/data.txt");
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
	        for (String line = br.readLine(); line != null; line = br.readLine()) {
	        	if (line.startsWith("w=")) { // 标准宽度
					w = Integer.valueOf(line.substring(2));
				} else if (line.startsWith("h=")) { // 标准高度
					h = Integer.valueOf(line.substring(2));
				} else if (line.startsWith("g=")){ // 关键点
					String[] xys = line.substring(2).split(",");
					Point p = new Point(Integer.valueOf(xys[0]), Integer.valueOf(xys[1]));
					int key = Integer.valueOf(xys[2]);
					p.index = key;
					kps.add(p);
				} else if (line.startsWith("background=")) { // 背景
					String pathName = "points/primary/" + path + "/" + line.substring(11);
					bg = BitmapFactory.decodeStream(getAssets().open(pathName));
				} else if (line.startsWith("win=")) { // 正确
					String pathName = "points/primary/" + path + "/" + line.substring(4);
					ri = BitmapFactory.decodeStream(getAssets().open(pathName));
				}
	        }
	        br.close();
	        
	        mPointsData = new PointsData(w, h, bg, ri, kps);
			view.setDatas(mPointsData);
		} catch (IOException e) {
			showToast("功能升级中....");
			finish();
		}
	}

}
