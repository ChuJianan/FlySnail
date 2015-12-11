package qau.cjn.flysnail.activity;

import java.io.File;
import java.io.IOException;
import java.util.zip.ZipException;

import qau.cjn.flysnail.util.ZipUtils;

import com.testin.commplatform.TestinAgent;
import com.yunruiinfo.app.flysnail.R;

import android.content.res.Resources.NotFoundException;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

public class GalleryActivity extends BaseActivity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		setContentView(R.layout.activity_gallery);
		
		Toast.makeText(GalleryActivity.this, "unziping...", Toast.LENGTH_LONG).show();
		outZip();
	}
	
	private void outZip() {
		final Handler handler = new Handler() {
			@Override
			public void handleMessage(Message msg) {
				if (msg.what == 1) {
					Toast.makeText(GalleryActivity.this, "unzip success", Toast.LENGTH_LONG).show();
				}
			}
		};
		new Thread() {
			@Override
			public void run() {
				//Message msg = new Message();
				String folderPath = "/sdcard/tmpf/";
//				try {
//					//ZipUtils.upZipFile(getResources().openRawResource(R.raw.gallery), folderPath);
//				} catch (ZipException e) {
//					e.printStackTrace();
//				} catch (NotFoundException e) {
//					e.printStackTrace();
//				} catch (IOException e) {
//					e.printStackTrace();
//				}
				handler.sendEmptyMessage(1);
			}
		}.start();
	}
}
