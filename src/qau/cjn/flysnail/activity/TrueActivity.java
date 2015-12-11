package qau.cjn.flysnail.activity;

import qau.cjn.flysnail.util.CustomDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;

import com.testin.commplatform.TestinAgent;
import com.yunruiinfo.app.flysnail.R;

public class TrueActivity extends BaseActivity{
	private Typeface typeface;
	CustomDialog.Builder builder ;
	ImageView imageView;
	BumperActivity bumperActivity=new BumperActivity();
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		setContentView(R.layout.truelagerpic);
		String pathString=getIntent().getStringExtra("path");
		imageView=(ImageView)findViewById(R.id.imagetrue);
		builder = new CustomDialog.Builder(this);
		typeface=Typeface.createFromAsset(getAssets(), "font/katong.ttf");
		imageView.setImageBitmap(BitmapFactory.decodeFile(pathString));
		}
	@Override
	public void onBackPressed() {
		
			
			builder.setTypeface(typeface);
			builder.setMessage("太棒了，进行下一关吧");
			builder.setTitle("提示");
			builder.setPositiveButton("下一关", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					bumperActivity.finish();
					Intent intent=new Intent(TrueActivity.this,BumperActivity.class);
				
					startActivity(intent);
					finish();		
					dialog.dismiss();
					//设置你的操作事项
					
				}
			});

			builder.setNegativeButton("返回",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							
							finish();
							dialog.dismiss();
						}
					});

			builder.create().show();
	}
}
