package qau.cjn.flysnail.util.sutil;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.zip.ZipException;

import qau.cjn.flysnail.activity.MainActivity;
import qau.cjn.flysnail.activity.SplashActivity;
import qau.cjn.flysnail.util.Path;
import qau.cjn.flysnail.util.ZipUtils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.yunruiinfo.app.flysnail.R;

/***
 * dialog文件下载
 * @author spring sky <br>
 * QQ ：840950105
 */
public class DownloadDialog extends Dialog implements
		android.view.View.OnClickListener {
	private static final int DOWNLOAD_PREPARE = 0;
	private static final int DOWNLOAD_WORK = 1;
	private static final int DOWNLOAD_OK = 2;
	private static final int DOWNLOAD_ERROR = 3;
	private static final String TAG = "IndexActivity";
	private Context mContext;

	private Button bt;
	private ProgressBar pb;
	/** 下载过程中不能点击 */
	private boolean isClick = false;
	private boolean downloadOk = false;
	private TextView tv;
	/**
	 * 下载的url
	 */
	private String url = null;
	private String filePath;

	/**
	 * 文件大小
	 */
	int fileSize = 0;

	/**
	 * 下载的大小
	 */
	int downloadSize = 0;

	/**
	 * handler
	 */
	private Handler handler = new Handler() {

		@Override
		public void handleMessage(Message msg) {
			switch (msg.what) {
			case DOWNLOAD_PREPARE:
				Toast.makeText(mContext, "准备下载", Toast.LENGTH_SHORT).show();
				pb.setVisibility(ProgressBar.VISIBLE);
				Log.e(TAG, "文件大小:" + fileSize);
				pb.setMax(fileSize);
				break;
			case DOWNLOAD_WORK:
				Log.e(TAG, "已经下载:" + downloadSize);
				pb.setProgress(downloadSize);
				int res = (downloadSize/ fileSize);
				tv.setText("正在下载...");
				bt.setText(FileUtil.FormetFileSize(downloadSize) + "/"
						+ FileUtil.FormetFileSize(fileSize));
				break;
			case DOWNLOAD_OK:
				downloadOk = true;
				tv.setText("下载完成");
				bt.setText("请点击进行解压缩");
				downloadSize = 0;
				fileSize = 0;
				Toast.makeText(mContext, "下载成功", Toast.LENGTH_SHORT).show();
				break;
			case DOWNLOAD_ERROR:
				downloadSize = 0;
				fileSize = 0;
				Toast.makeText(mContext, "下载失败", Toast.LENGTH_SHORT).show();
				break;
			}
			super.handleMessage(msg);
		}
	};

	private ImageView imageView;

	public DownloadDialog(Context context, String url) {

		super(context,R.style.Dialog);
		mContext = context;
		this.url = url;
		filePath = FileUtil.getPath(mContext, url);
	}

	@Override
	public void cancel() {
		super.cancel();
	}

	/**
	 * 下载文件
	 */
	private void downloadFile() {
		try {
			URL u = new URL(url);
			URLConnection conn = u.openConnection();
			InputStream is = conn.getInputStream();
			fileSize = conn.getContentLength();
			if (fileSize < 1 || is == null) {
				sendMessage(DOWNLOAD_ERROR);
			} else {
				sendMessage(DOWNLOAD_PREPARE);
				FileOutputStream fos = new FileOutputStream(filePath);
				byte[] bytes = new byte[1024];
				int len = -1;
				while ((len = is.read(bytes)) != -1) {
					fos.write(bytes, 0, len);
					fos.flush();
					downloadSize += len;
					sendMessage(DOWNLOAD_WORK);
				}
				sendMessage(DOWNLOAD_OK);
				is.close();
				fos.close();
			}
		} catch (Exception e) {
			sendMessage(DOWNLOAD_ERROR);
			e.printStackTrace();
		}
	}
	/***
	 * 得到文件的路径
	 * 
	 * @return
	 */
	public String getFilePath() {
		return filePath;
	}
	private void init() {
		bt = (Button) this.findViewById(R.id.down_bt);
		bt.setOnClickListener(this);
		tv = (TextView) this.findViewById(R.id.down_tv);
		bt.setText("开始下载");
		pb = (ProgressBar) this.findViewById(R.id.down_pb);
	}


	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.down_bt:
			if (isClick) {
				// 启动一个线程下载文件
				Thread thread = new Thread(new Runnable() {
					@Override
					public void run() {
						downloadFile();
					}
				});
				thread.start();
				isClick = false;
			}

			if (downloadOk) // 下载完成后 ，把图片显示在ImageView上面
			{
				
				try {				
					isClick=ZipUtils.readByApacheZipFile(filePath, Path.gpath,mContext);
					
				} catch (FileNotFoundException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (ZipException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				Toast.makeText(mContext, "正在重启，请稍后...", Toast.LENGTH_SHORT).show();
			}
			if (isClick&&downloadOk) {
				Intent intent=new Intent((Activity)mContext,SplashActivity.class);
				mContext.startActivity(intent);
				((Activity) mContext).finish();
			}
			break;
		default:
			break;
		}
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.dialog_down_layout);
		init();
	}

	/**
	 * @param what
	 */
	private void sendMessage(int what) {
		Message m = new Message();
		m.what = what;
		handler.sendMessage(m);
	}

	public void setImageView(ImageView imageView) {
		this.imageView = imageView;
	}

	@Override
	public void show() {
		isClick = true;
		downloadOk = false;
		super.show();
	}

}
