package qau.cjn.flysnail.activity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

import qau.cjn.flysnail.graphics.Point;
import qau.cjn.flysnail.graphics.PointsData;
import qau.cjn.flysnail.graphics.PointsView;
import qau.cjn.flysnail.graphics.PointsView.OnCompleteListener;
import qau.cjn.flysnail.util.CustomDialog;
import qau.cjn.flysnail.util.DirTraversal;
import qau.cjn.flysnail.util.Sounds;
import qau.cjn.flysnail.util.wutil.StringUtil;

import com.yunruiinfo.app.flysnail.R;
import com.testin.commplatform.TestinAgent;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnCompletionListener;
import android.media.SoundPool;
import android.media.SoundPool.OnLoadCompleteListener;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceActivity.Header;
import android.text.style.SuperscriptSpan;
import android.view.View;
import android.widget.Toast;

public class PointsActivity extends BaseActivity {
	private MediaPlayer soundPool;
	boolean isplay=false;
	private Toast toast;
	private int position;
	CustomDialog.Builder builder ;
	private List<String> list=new ArrayList<String>();
	private PointsView mPointsView;
	private PointsData mPointsData; 
	private Typeface typeface;
	Sounds sounds=new Sounds();
	private boolean isWin;// 是否已提示连点正确
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		setContentView(R.layout.activity_points);
		builder = new CustomDialog.Builder(this);
		typeface=Typeface.createFromAsset(getAssets(), "font/katong.ttf");
		list=DirTraversal.deepFile(this,"points/middle");
		position=getIntent().getExtras().getInt("position");
		String path = getIntent().getStringExtra("path");
		if (StringUtil.isEmpty(path)) {
			showToast("参数无效");
			finish();
			return;
		}
		mPointsView = (PointsView) findViewById(R.id.points);
		setPointsData(path, mPointsView);
		mPointsView.setOnCompleteListener(new OnCompleteListener(){
			@Override
			public void onComplete(String mPassword) {
				if (mPointsView.verifyPassword(mPassword)) {
					//mPointsView.youWin();
					if (!isWin) {
						showToast( "连接正确，再点一下看看吧~");	
						
					}
					isWin = true;
				}else {
					//mPointsView.clearSelectedPoints();
					showToast( "不正确哦，再试一试吧~");
					soundPool=falsePlayer();
					soundPool.start();
					isplay=true;
					mPointsView.reset();
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
		mPointsView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View arg0) {
				if (isWin) {
					soundPool= truesound();	
					soundPool.start();
					isplay=true;
					mPointsView.youWin();			
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
									if (position==8) {
										position=-1;
									}
									finish();
									Intent intent=new Intent(PointsActivity.this,PointsActivity.class);
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
	public void setPointsData(String path, PointsView view) {
		int w = 0, h = 0;
		Bitmap bg = null, ri = null;
		List<Point> kps = new ArrayList<Point>();
		try {
			InputStream in = getAssets().open("points/middle/" + path + "/data.txt");
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
					String pathName = "points/middle/" + path + "/" + line.substring(11);
					bg = BitmapFactory.decodeStream(getAssets().open(pathName));
				} else if (line.startsWith("win=")) { // 正确
					String pathName = "points/middle/" + path + "/" + line.substring(4);
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
