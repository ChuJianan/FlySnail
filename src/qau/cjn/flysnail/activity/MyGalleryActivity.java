package qau.cjn.flysnail.activity;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import qau.cjn.flysnail.been.Datas;
import qau.cjn.flysnail.been.PicBits;
import qau.cjn.flysnail.util.Comparatorlist;
import qau.cjn.flysnail.util.FileUtil;
import qau.cjn.flysnail.util.Path;
import qau.cjn.flysnail.util.StringUtil;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.animation.AnimationUtils;
import android.widget.Gallery.LayoutParams;
import android.widget.ImageSwitcher;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewSwitcher;

import com.testin.commplatform.TestinAgent;
import com.yunruiinfo.app.flysnail.R;

public class MyGalleryActivity extends BaseActivity implements
		ViewSwitcher.ViewFactory {
	private  List<PicBits> pList=new ArrayList<PicBits>(); 
	private  List<PicBits> mList=new ArrayList<PicBits>(); 
	private List<String>pathList=new ArrayList<String>();
	private String path;
	private ImageSwitcher mSwitcher;
	Comparatorlist comparatorlist=new Comparatorlist();
	ArrayList<Drawable> bitmaps=new ArrayList<Drawable>();
	private int mPosition = 0;
	private GestureDetector mGestureDetector;
	Toast myToast;
	MediaPlayer mediaPlayer;
	Typeface typeface;
	TextView textView;
	ImageView soundplay;
	String spath;
	boolean isplay=false;
	FileUtil fileUtil=new FileUtil();
	private static final float HORIZONTAL_SCROLL_DISTANCE = 10f;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
//		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.gallerymain);		
		String path=getIntent().getStringExtra("path");
		int arg=getIntent().getExtras().getInt("i");
		int nub=getIntent().getExtras().getInt("nub");
		typeface=Typeface.createFromAsset(getAssets(), "font/katong.ttf");
		path=Path.path+Datas.datas[nub]+"/";
		pathList=fileUtil.filename(path);
		Collections.sort(pathList, comparatorlist);
		for (int i = 0; i <pathList.size(); i++) {
			PicBits picBits=new PicBits();
			picBits.setName(pathList.get(i));
			mList.add(picBits);
		}
		for (int i = 0; i <pathList.size(); i++) {
			PicBits picBits=new PicBits();
			picBits.setName(path+pathList.get(i));
			pList.add(picBits);
		}
//		getAllFiles(path);
//		getAllFilesname(path);
		mytoast();

		mPosition=arg;
				 
		mSwitcher = (ImageSwitcher) findViewById(R.id.imageSwitch);
		textView=(TextView)findViewById(R.id.name);
		soundplay=(ImageView)findViewById(R.id.soundplay);
		mSwitcher.setFactory(this);
		textView.setTextSize(30);
		textView.setTypeface(typeface);
		soundplay.setVisibility(View.INVISIBLE);
		setupOnTouchListeners(findViewById(R.id.rootview));
		mSwitcher.setImageDrawable(Drawable.createFromPath(pList.get(mPosition).getName()));
		spath=fileUtil.searchFile(StringUtil.string(mList.get(mPosition).getName())+".mp3",
				new File(Path.spath));
		
		soundplay();
		mSwitcher.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				textView.setText(StringUtil.string(mList.get(mPosition).getName()));
				textView.setVisibility(View.VISIBLE);
			}
		});
	}
	private void soundplay(){
	if (spath!=null) {
		soundplay.setVisibility(View.VISIBLE);
		soundplay.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				mediaPlayer=sound(spath);
				mediaPlayer.start();
				isplay=true;
//				mediaPlayer.setOnCompletionListener(new OnCompletionListener(){
//
//					@Override
//					public void onCompletion(MediaPlayer mp) {
//						// TODO Auto-generated method stub
//						mediaPlayer.release();
//					}
//					});
			}
		});
	}else {
	}
}
	private void setupOnTouchListeners(View rootView) {
		mGestureDetector = new GestureDetector(this, new MyGestureListener());

		OnTouchListener rootListener = new OnTouchListener() {
			public boolean onTouch(View v, MotionEvent event) {
				mGestureDetector.onTouchEvent(event);
				return true;
			}
		};

		rootView.setOnTouchListener(rootListener);
	}

	public void onPause() {
		super.onPause();
		
	}

	private class MyGestureListener extends
			GestureDetector.SimpleOnGestureListener {

		@Override
		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
				float velocityY) {
			if (spath!=null&&isplay) {
				mediaPlayer.stop();
				mediaPlayer.release();
				isplay=false;
			}
			
			textView.setVisibility(View.INVISIBLE);
			soundplay.setVisibility(View.INVISIBLE);
			if (Math.abs(velocityY) <= Math.abs(velocityX)
					&& Math.abs(velocityX) > HORIZONTAL_SCROLL_DISTANCE) {
				//
				System.out.println(velocityX);
				if (velocityX > 0) {
					if (mPosition > 0) {
					
						//
						mSwitcher.setInAnimation(AnimationUtils.loadAnimation(
								MyGalleryActivity.this, android.R.anim.fade_in));
						mSwitcher
								.setOutAnimation(AnimationUtils.loadAnimation(
										MyGalleryActivity.this,
										android.R.anim.fade_out));
						mSwitcher.setImageDrawable(Drawable.createFromPath(pList.get(--mPosition).getName()));
						spath=fileUtil.searchFile(StringUtil.string(mList.get(mPosition).getName())+".mp3",
								new File(Path.spath));
						
						soundplay();
					}
					else {
						;
						//
						mSwitcher.setInAnimation(AnimationUtils.loadAnimation(
								MyGalleryActivity.this, android.R.anim.fade_in));
						mSwitcher
								.setOutAnimation(AnimationUtils.loadAnimation(
										MyGalleryActivity.this,
										android.R.anim.fade_out));
						mSwitcher.setImageDrawable(Drawable.createFromPath(pList.get(mPosition=pList.size()-1).getName()));
//						mPosition--;
						spath=fileUtil.searchFile(StringUtil.string(mList.get(mPosition).getName())+".mp3",
								new File(Path.spath));
						
						soundplay();
					}
				} else {
					if (mPosition < (pList.size() - 1)) {
						

						mSwitcher.setInAnimation(AnimationUtils.loadAnimation(
								MyGalleryActivity.this, android.R.anim.fade_in));
						mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
								MyGalleryActivity.this, android.R.anim.fade_out));
						mSwitcher.setImageDrawable(Drawable.createFromPath(pList.get(++mPosition).getName()));
						spath=fileUtil.searchFile(StringUtil.string(mList.get(mPosition).getName())+".mp3",
								new File(Path.spath));
						
						soundplay();
					} else if (mPosition == (pList.size() - 1)) {

						mSwitcher.setInAnimation(AnimationUtils.loadAnimation(
								MyGalleryActivity.this, android.R.anim.fade_in));
						mSwitcher.setOutAnimation(AnimationUtils.loadAnimation(
								MyGalleryActivity.this, android.R.anim.fade_out));
						mSwitcher.setImageDrawable(Drawable.createFromPath(pList.get(mPosition=0).getName()));
						spath=fileUtil.searchFile(StringUtil.string(mList.get(mPosition).getName())+".mp3",
								new File(Path.spath));
						
						soundplay();
					}
				}
			}

			return true;
		}

	}


	private List<PicBits> getAllFiles(String path){
		File file=new File(path);
		File[] subFile = file.listFiles();
	    if(subFile!= null){  
	    	for (int i = 0; i < subFile.length; i++) {
		    	PicBits picBits = new PicBits();	       	        
		        picBits.setName(subFile[i].toString());
		        pList.add(picBits);
	        }
	    }else {
			
		}
		return pList;   
	}
	private List<PicBits> getAllFilesname(String path){
		File file=new File(path);
		File[] subFile = file.listFiles();
	    if(subFile!= null){  
	    	for (int i = 0; i < subFile.length; i++) {
		    	PicBits picBits = new PicBits();	       	        
		        picBits.setName(subFile[i].getName());
		        mList.add(picBits);
	        }
	    }else {
			
		}
		return mList;   
	}
	private void mytoast(){
		
		LayoutInflater inflater =this.getLayoutInflater();
        View myToastView = inflater.inflate(R.layout.mytoast, (ViewGroup) findViewById(R.id.myToast));
        TextView toastText = (TextView) myToastView.findViewById(R.id.toastText);
        toastText.setText("在页面空白的地方滑动会切换图片，点击图片会出现名字，希望小朋友好好学习哦！");
        toastText.setTypeface(typeface);
        toastText.setTextColor(Color.RED);
        toastText.setTextSize(18);
        myToast = new Toast(getApplicationContext());
        myToast.setGravity(Gravity.TOP, 0, 80);
        myToast.setDuration(10);
        myToast.setView(myToastView);
        myToast.show();
	}
	private MediaPlayer sound(String path){
		MediaPlayer mpPlayer=new MediaPlayer();
		try {
			mpPlayer.setDataSource(path);
			mpPlayer.prepare();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mpPlayer;
	}
	@Override
	public View makeView() {
		ImageView i = new ImageView(this);
		i.setBackgroundColor(0xFF000000);
		i.setScaleType(ImageView.ScaleType.FIT_XY);
		i.setLayoutParams(new ImageSwitcher.LayoutParams(
				LayoutParams.FILL_PARENT, LayoutParams.FILL_PARENT));
		return i;
	}
	@Override
	public void onBackPressed() {
		
			super.onBackPressed();
			if (spath!=null&&isplay) {
				mediaPlayer.stop();
				mediaPlayer.release();
				isplay=false;
			}
			finish();
		
	}
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果是返回键,直接返回到桌面
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME){
			if (spath!=null&&isplay) {
				mediaPlayer.stop();
				mediaPlayer.release();
				isplay=false;
			}
		}

		return super.onKeyDown(keyCode, event);
		}

}