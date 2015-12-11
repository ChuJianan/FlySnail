package qau.cjn.flysnail.activity;

import java.util.ArrayList;
import java.util.List;

import qau.cjn.flysnail.util.FileUtil;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.testin.commplatform.TestinAgent;
import com.yunruiinfo.app.flysnail.R;

public class BumperActivity extends BaseActivity {
	FileUtil fileUtils=new FileUtil();
	ImageView pic1,pic2,pic3,pic4,imgtrue,imgfalse,isbtn;
	Animation shake;
	int i=0;
	MediaPlayer mediaPlayer;
	int[] random=new int[4];
	List<String> list=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		setContentView(R.layout.index_activity);
		pic1=(ImageView)findViewById(R.id.pic1);
		pic2=(ImageView)findViewById(R.id.pic2);
		pic3=(ImageView)findViewById(R.id.pic3);
		pic4=(ImageView)findViewById(R.id.pic4);
		imgtrue=(ImageView)findViewById(R.id.imgtrue);
		imgfalse=(ImageView)findViewById(R.id.imgfalse);
//		list=((Application)getApplicationContext()).getPath();
//		fileUtils.ListFiles(Path.path, list);
		
		for (int i = 0; i < random.length; i++) {
			random[i]=(int) (Math.random()*pathList.size()+0);
		}
		isbtn=null;
		shake = AnimationUtils.loadAnimation(this, R.anim.shake);
		pic1.setImageBitmap(BitmapFactory.decodeFile(pathList.get(random[0])));
		pic2.setImageBitmap(BitmapFactory.decodeFile(pathList.get(random[1])));
		pic3.setImageBitmap(BitmapFactory.decodeFile(pathList.get(random[2])));
		pic4.setImageBitmap(BitmapFactory.decodeFile(pathList.get(random[3])));
//		pic1.setOnTouchListener(onTouchListener);
		pic1.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isbtn=pic1;
				i=0;
				changeLight(pic1, -50);
			}
		});
	pic2.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				isbtn=pic2;
				i=1;
				changeLight(pic2, -50);
			}
		});
	pic3.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			isbtn=pic3;
			i=2;
			changeLight(pic3, -50);
		}
	});
	pic4.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			isbtn=pic4;
			i=3;
			changeLight(pic4, -50);
		}
	});
	imgtrue.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (isbtn!=null) {
				changeLight(isbtn, 50);
				isbtn=null;
				mediaPlayer=truesound();
				mediaPlayer.start();
				Intent intent=new Intent(BumperActivity.this,TrueActivity.class);
				intent.putExtra("path", pathList.get(random[i]));
				startActivity(intent);
				finish();
			}
		}
	});
	imgfalse.setOnClickListener(new OnClickListener() {
		
		@Override
		public void onClick(View v) {
			// TODO Auto-generated method stub
			if (isbtn!=null) {
				mediaPlayer=falsePlayer();
				isbtn.startAnimation(shake);
				mediaPlayer.start();
				changeLight(isbtn, 50);
				isbtn=null;
			}
		}
	});
	}
	public void changeLight(ImageView imageView, int brightness) {
        ColorMatrix cMatrix = new ColorMatrix();
        cMatrix.set(new float[] { 1, 0, 0, 0, brightness, 0, 1, 0, 0,
                        brightness,// 改变亮度
                        0, 0, 1, 0, brightness, 0, 0, 0, 1, 0 });
        imageView.setColorFilter(new ColorMatrixColorFilter(cMatrix));
}
}
