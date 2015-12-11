package qau.cjn.flysnail.libaries;

import java.io.IOException;
import java.io.InputStream;

import android.app.ActionBar;
import android.app.Activity;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.BitmapFactory.Options;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.FloatMath;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;

import com.yunruiinfo.app.flysnail.R;

public class PicLarge extends Activity {
	/** Called when the activity is first created. */
	Bitmap bp;
	private ImageView imageview;
	float scaleWidth;
	float scaleHeight;

	int h;
	boolean num = false;

	Bitmap newBitmap;
	private ActionBar actionBar;
	int bigPosition;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		setContentView(R.layout.gridpic);

		// 添加actionbar
		actionBar = getActionBar();
		actionBar.hide();

		Bundle bundle = new Bundle();
		bundle = this.getIntent().getExtras();
		int position = bundle.getInt("address");
		int len = SmallMappingBig.picMapping.length;
		
		for(int i=0;i<len;i++)
		{
			System.out.println("position:"+position);
			if (position == SmallMappingBig.picMapping[i][0]){
				bigPosition = SmallMappingBig.picMapping[i][1];
				System.out.println("bigposition:"+bigPosition);
			}
			continue;
		}
		

		DisplayMetrics dm = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dm);
		imageview = (ImageView) this.findViewById(R.id.big_picture_iv);
		//bp = BitmapFactory.decodeResource(getResources(), bigPosition);
		bp = decodeSampledBitmapFromResource(getResources(), bigPosition,
				100, 100);
		int width = bp.getWidth();
		int height = bp.getHeight();
		int w = dm.widthPixels;
		int h = dm.heightPixels;
		scaleWidth = (float) (w / width);
		scaleHeight = (float) (w / height);
		//imageview.setImageBitmap(bp);
		
		Matrix matrix=new Matrix();
		 matrix.postScale(scaleWidth,scaleHeight);
		 Bitmap newBitmap=Bitmap.createBitmap(bp, 0, 0, bp.getWidth(), bp.getHeight(), matrix, true);  
	     imageview.setImageBitmap(newBitmap);
	    //imageview.setImageResource(bigPosition);
		imageview.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				actionBar.show();
			}
		});
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		/*
		 * MenuInflater inflater = getMenuInflater();
		 * inflater.inflate(R.menu.piclarge, menu);
		 */

		super.onCreateOptionsMenu(menu);
//		MenuItem musicItem = menu.add(0, Menu.FIRST + 1, 0, "声音按钮").setIcon(R.drawable.laba);
//		musicItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case Menu.FIRST + 1:
			MediaPlayer resPlayer = MediaPlayer.create(this, R.raw.elephont);
			resPlayer.start();
			break;

		default:
			break;
		}
		return false;
	}

	public Bitmap decodeSampledBitmapFromResource(Resources res, int resId,
			int reqWidth, int reqHeight) {
		InputStream is = this.getResources().openRawResource(resId);
		//  第一次解析将inJustDecodeBounds设置为true，来获取图片大小
		final BitmapFactory.Options options = new BitmapFactory.Options();
		options.inJustDecodeBounds = true;
		options.inPurgeable = true;// 设置图片可以被回收
		// BitmapFactory.decodeResource(res, resId, options);
		//  调用上面定义的方法计算inSampleSize值
		options.inSampleSize = calculateInSampleSize(options, reqWidth,
				reqHeight);
		//  使用获取到的inSampleSize值再次解析图片
		options.inJustDecodeBounds = false;
		return BitmapFactory.decodeStream(is, null, options);
	}

	private static int calculateInSampleSize(Options options, int reqWidth,
			int reqHeight) {
		// TODO Auto-generated method stub
		// 源图片的高度和宽度  
		final int height = options.outHeight;
		final int width = options.outWidth;
		int inSampleSize = 4;
		if (height > reqHeight || width > reqWidth) {
			// 计算出实际宽高和目标宽高的比率
			final int heightRatio = Math.round((float) height
					/ (float) reqHeight);
			final int widthRatio = Math.round((float) width / (float) reqWidth);
			//  选择宽和高中最小的比率作为inSampleSize的值，这样可以保证最终图片的宽和高
			//  一定都会大于等于目标的宽和高
			inSampleSize = heightRatio < widthRatio ? heightRatio : widthRatio;
		}
		return inSampleSize;
	}

}