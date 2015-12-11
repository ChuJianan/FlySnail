package qau.cjn.flysnail.activity;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import qau.cjn.flysnail.application.Application;

import com.testin.commplatform.TestinAgent;
import com.yunruiinfo.app.flysnail.R;

import android.app.Activity;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Bundle;
import android.view.MenuItem;

public class BaseActivity extends Activity {
	public List<String> pathList=new ArrayList<String>();
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		TestinAgent.onError(this);
		TestinAgent.postBaseData(this);
		pathList=((Application)getApplicationContext()).getPath();
		if (getActionBar() != null) {
			getActionBar().setDisplayHomeAsUpEnabled(true);
		}
		
	}
	public  MediaPlayer truesound(){
		MediaPlayer mp = MediaPlayer.create(this, R.raw.zhengque);
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mp;
	}
	public MediaPlayer falsePlayer(){
		MediaPlayer mp=MediaPlayer.create(this, R.raw.cuole);
		try {
			mp.prepare();
		} catch (IllegalStateException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return mp;
	}
	@Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case android.R.id.home:
			
			finish();
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
    }
}
