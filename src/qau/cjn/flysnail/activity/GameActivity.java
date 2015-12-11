package qau.cjn.flysnail.activity;


import com.yunruiinfo.app.flysnail.R;

import android.app.TabActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.TabHost;
/**
 * 点连线
 * @author Administrator
 *
 */
public class GameActivity extends TabActivity implements OnClickListener {

	@Override
	public void onClick(View view) {
		// getTabHost().setCurrentTab(2);
		getTabHost().setCurrentTabByTag("tab3");
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		requestWindowFeature(Window.FEATURE_ACTION_BAR);
		super.onCreate(savedInstanceState);
		// setContentView(R.layout.tabmain);
		getActionBar().setDisplayHomeAsUpEnabled(true);
		TabHost tabHost = getTabHost();
		tabHost.setup();
		/*
		 * LayoutInflater.from(this).inflate(R.layout.main,
		 * tabHost.getTabContentView(), true);
		 */
		LayoutInflater.from(this).inflate(R.layout.tabmain,
				tabHost.getTabContentView(), true);
		// tabHost.addTab(tabHost.newTabSpec("tab1").setIndicator("小游戏")
		// .setContent(new Intent(this, Instruction.class)));
		tabHost.addTab(tabHost
				.newTabSpec("tab2")
				.setIndicator("初级", getResources().getDrawable(R.drawable.icon))
				.setContent(new Intent(this, PrimaryGamesActivity.class)));
		tabHost.addTab(tabHost
				.newTabSpec("tab3")
				.setIndicator("中级", getResources().getDrawable(R.drawable.icon))
				.setContent(new Intent(this, MiddleGamesActivity.class)));
		tabHost.addTab(tabHost.newTabSpec("tab4").setIndicator("高级")
				.setContent(new Intent(this, SeniorGamesActivity.class)));

		tabHost.setCurrentTab(0);
		// Button button = (Button) findViewById(R.id.button);
		// button.setOnClickListener(this);
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