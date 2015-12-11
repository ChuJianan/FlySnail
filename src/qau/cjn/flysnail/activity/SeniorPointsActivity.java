package qau.cjn.flysnail.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import qau.cjn.flysnail.graphics.Point;
import qau.cjn.flysnail.graphics.PointsData;
import qau.cjn.flysnail.graphics.SeniorPointsView;
import qau.cjn.flysnail.graphics.SeniorPointsView.OnCompleteListener;
import qau.cjn.flysnail.util.CustomDialog;
import qau.cjn.flysnail.util.DirTraversal;
import qau.cjn.flysnail.util.wutil.StringUtil;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.Toast;

import com.testin.commplatform.TestinAgent;
import com.yunruiinfo.app.flysnail.R;

public class SeniorPointsActivity extends BaseActivity {
	private MediaPlayer soundPool;
	private Toast toast;
	boolean isplay=false;
	private int position;
	CustomDialog.Builder builder ;
	private List<String> list=new ArrayList<String>();
	private SeniorPointsView mPointsView;
	private PointsData mPointsData;
	private Typeface typeface;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		setContentView(R.layout.activity_points_senior);
		builder = new CustomDialog.Builder(this);
		typeface=Typeface.createFromAsset(getAssets(), "font/katong.ttf");
		list=DirTraversal.deepFile(this,"points/senior");
		String path = getIntent().getStringExtra("path");
		position=getIntent().getExtras().getInt("position");
		if (StringUtil.isEmpty(path)) {
			showToast("参数无效");
			finish();
			return;
		}
		mPointsView = (SeniorPointsView) findViewById(R.id.points);
		setPointsData(path, mPointsView);
		mPointsView.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View v, MotionEvent event) {
				// TODO Auto-generated method stub
				if (!mPointsView.checkassist()) {
					showToast("你这步走错了，请退到上一步！");
				}
				return false;
			}
		});
		mPointsView.setOnCompleteListener(new OnCompleteListener(){
			@Override
			public void onComplete(String mPassword) {
				if (mPointsView.verifyPassword(mPassword)) {
					mPointsView.youWin();
//					showToast( "成功啦，真棒！");
					soundPool= truesound();	
					soundPool.start();
					isplay=true;
					if (position==8) {
						position=-1;
					}
						
					soundPool.setOnCompletionListener(new OnCompletionListener() {						
						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							soundPool.release();
							isplay=false;
							builder.setTypeface(typeface);
							builder.setMessage("小朋友，你太棒了！");
							builder.setTitle("过关了");
							builder.setPositiveButton("下一关", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int which) {
									finish();
									Intent intent=new Intent(SeniorPointsActivity.this,SeniorPointsActivity.class);
									intent.putExtra("path", list.get(position+1));
									intent.putExtra("position",position+1);
									startActivity(intent);
								
									dialog.dismiss();
									//设置你的操作事项
									
								}
							});

							builder.setNegativeButton("返回",
									new android.content.DialogInterface.OnClickListener() {
										public void onClick(DialogInterface dialog, int which) {
											dialog.dismiss();
										
											finish();
										}
									});

							builder.create().show();
							
						}
					});		
				}else {
					showToast( "不正确哦，再试一试吧~");
					soundPool= falsePlayer();	
					soundPool.start();
					isplay=true;
					mPointsView.undo();
					//mPointsView.reset();
					soundPool.setOnCompletionListener(new OnCompletionListener() {						
						@Override
						public void onCompletion(MediaPlayer mp) {
							// TODO Auto-generated method stub
							soundPool.release();	
							isplay=false;
						}
					});	
				}
			}
		});
	}
	@Override
	public void onBackPressed() {
		if (!mPointsView.undo()&&!isplay) {
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
	public void setPointsData(String path, SeniorPointsView view) {
		int w = 0, h = 0;
		Bitmap bg = null, ri = null;
		List<Point> kps = new ArrayList<Point>();
		List<Point> aps = new ArrayList<Point>();
		try {
			InputStream in = getAssets().open("points/senior/" + path + "/data.txt");
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
					String pathName = "points/senior/" + path + "/" + line.substring(11);
					bg = BitmapFactory.decodeStream(getAssets().open(pathName));
				} else if (line.startsWith("win=")) { // 正确
					String pathName = "points/senior/" + path + "/" + line.substring(4);
					ri = BitmapFactory.decodeStream(getAssets().open(pathName));
				} else if (line.startsWith("f=")) { //辅助点
					String[] xys = line.substring(2).split(",");
					Point p = new Point(Integer.valueOf(xys[0]), Integer.valueOf(xys[1]));
					p.index = 0;
					aps.add(p);
				}
	        }
	        br.close();
	        
	        mPointsData = new PointsData(480, 480, bg, ri, kps, aps);//标准宽高先定死480
			view.setDatas(mPointsData);
		} catch (IOException e) {
			showToast("功能升级中....");
			finish();
		}
	}

}
