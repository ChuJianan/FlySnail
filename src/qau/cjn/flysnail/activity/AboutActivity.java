package qau.cjn.flysnail.activity;

import com.yunruiinfo.app.flysnail.R;

import android.app.Activity;
import android.os.Bundle;
import android.view.MenuItem;
import android.webkit.WebView;
/**
 * about
 * @author Administrator
 *
 */
public class AboutActivity extends Activity {  
	private WebView mWebView;
    @Override  
    protected void onCreate(Bundle savedInstanceState) {  
        super.onCreate(savedInstanceState); 
        setContentView(R.layout.instruction);
        
        getActionBar().setDisplayHomeAsUpEnabled(true);
        
        initView();
        initData();
    }
    
    private void initView() {
    	mWebView = (WebView) findViewById(R.id.webview);
    }
    
    private void initData() {
    	mWebView.loadUrl("file:///android_asset/intro/index.html");
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