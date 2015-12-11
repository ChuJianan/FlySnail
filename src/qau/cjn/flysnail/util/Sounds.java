package qau.cjn.flysnail.util;


import com.yunruiinfo.app.flysnail.R;
import android.content.Context;
import android.media.MediaPlayer;

public class Sounds {
public MediaPlayer truesound(Context context){
	
	MediaPlayer mp=MediaPlayer.create(context, R.raw.zhengque);
	mp.stop();
	return mp;
}
public MediaPlayer errorsound(Context context) {
	MediaPlayer mpMediaPlayer=MediaPlayer.create(context, R.raw.cuole);
	mpMediaPlayer.stop();
	return mpMediaPlayer;
}
}
