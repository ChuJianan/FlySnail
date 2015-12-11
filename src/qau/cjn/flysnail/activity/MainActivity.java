package qau.cjn.flysnail.activity;

import qau.cjn.flysnail.util.CustomDialog;
import qau.cjn.flysnail.util.Path;
import qau.cjn.flysnail.util.sutil.DownloadDialog;

import com.testin.commplatform.TestinAgent;
import com.yunruiinfo.app.flysnail.R;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Typeface;
/**
 * zhu界面
 * @author Administrator
 *
 */
public class MainActivity extends BaseActivity {

	CustomDialog.Builder builder ;
	boolean isNull=false;
	private Typeface typeface;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		setContentView(R.layout.main);
		builder = new CustomDialog.Builder(this);
		typeface=Typeface.createFromAsset(getAssets(), "font/katong.ttf");
		if (pathList.size()>0) {
			isNull=false;
		}else {
			isNull=true;
		}
        findViews(); //声明控件对象
        
        setListener(); //监听按钮点击方法
    }
    
    private ImageView 
    				img2,
    				img3,
    				
//    				img5,
    				img4,img6;
    
    private void findViews(){
        img2 = (ImageView) findViewById(R.id.img2);
        img3 = (ImageView) findViewById(R.id.img3);
//        img5 = (ImageView) findViewById(R.id.img5);
        img4 = (ImageView) findViewById(R.id.img4);
        img6 = (ImageView) findViewById(R.id.img6);
    }
    
    private void setListener(){
    	if (isNull) {
			img2.setOnClickListener(showdliog);
			img4.setOnClickListener(showdliog);
		}else {
			img2.setOnClickListener(liberaries);
			img4.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent=new Intent(MainActivity.this,BumperActivity.class);
					startActivity(intent);
				}
			});
		}
    	
    	img3.setOnClickListener(games);
//    	img5.setOnClickListener(instruction);
    	img6.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				Intent intent=new Intent(MainActivity.this,InstructionsActivity.class);
				startActivity(intent);
			}
		});
    
    }
    
    
    private Button.OnClickListener games=new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, GameActivity.class);
			startActivity(intent);
		}	
    };
    private Button.OnClickListener showdliog=new Button.OnClickListener(){
  		@Override
  		public void onClick(View v) {
  			builder.setTypeface(typeface);
			builder.setMessage("你需要下载资源包才能使用这个功能，是否下载？");
			builder.setTitle("提示");
			builder.setPositiveButton("现在下载", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
//					finish();	
					DownloadDialog downloadDialog=new DownloadDialog(MainActivity.this, Path.dpath);
					downloadDialog.show();
					dialog.dismiss();
					//设置你的操作事项
					
				}
			});

			builder.setNegativeButton("不需要了",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			builder.create().show();
  		}	
      };
    private Button.OnClickListener liberaries=new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, ListMyActivity.class);
			startActivity(intent);
		}	
    };
    private Button.OnClickListener instruction=new Button.OnClickListener(){
		@Override
		public void onClick(View v) {
			Intent intent=new Intent();
			intent.setClass(MainActivity.this, AboutActivity.class);
			startActivity(intent);
		}	
    };
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// 如果是返回键,直接返回到桌面
		if(keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME){
			builder.setTypeface(typeface);
			builder.setMessage("你确定不再玩会了吗？");
			builder.setTitle("提示");
			builder.setPositiveButton("不玩了", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					finish();				
					dialog.dismiss();
					//设置你的操作事项
					
				}
			});

			builder.setNegativeButton("再玩会",
					new android.content.DialogInterface.OnClickListener() {
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});

			builder.create().show();
		}

		return super.onKeyDown(keyCode, event);
		}

}