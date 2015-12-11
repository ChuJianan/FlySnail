package qau.cjn.flysnail.activity;

import java.util.ArrayList;
import java.util.List;


import com.yunruiinfo.app.flysnail.R;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;

public class SeniorGamesActivity extends ListActivity {
	private static String[] applicationNames = new String[] { "1. 树枝迷宫",
			"2. 蜻蜓迷宫", "3. 花瓣迷宫", "4. 胡萝卜迷宫", "5. 叶子迷宫", "6. 手掌迷宫", "7. 管道迷宫",
			"8. 树洞迷宫", "9. 围墙迷宫" };

	private static int[] resIds = new int[] { R.drawable.branch,
			R.drawable.flytiger, R.drawable.follower, R.drawable.huluobo,
			R.drawable.leaf, R.drawable.palm, R.drawable.pipe,
			R.drawable.shudong, R.drawable.wall };

	String inflater = Context.LAYOUT_INFLATER_SERVICE;
	LayoutInflater layoutInflater;
	private NewAdapter newAdapter;

	private class NewAdapter extends BaseAdapter {
		private Context context;

		public NewAdapter(Context context) {
			this.context = context;
			layoutInflater = (LayoutInflater) context
					.getSystemService(inflater);
		}

		@Override
		public int getCount() {
			return applicationNames.length;
		}

		@Override
		public Object getItem(int position) {
			return applicationNames[position];
		}

		@Override
		public long getItemId(int position) {
			return position = position + 1;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
					R.layout.ratingseniorgames, null);
			ImageView ivLogo = (ImageView) linearLayout
					.findViewById(R.id.ivLogo);
			TextView tvApplicationName = ((TextView) linearLayout
					.findViewById(R.id.tvApplicationName));
			ivLogo.setImageResource(resIds[position]);
			tvApplicationName.setText(applicationNames[position]);
			return linearLayout;
		}
	}

	@Override
	protected void onListItemClick(ListView l, View view, final int position,
			long id) {
		Intent intent = new Intent();
		Bundle bundle = new Bundle();
		String path = "";
		switch (position + 1) {
		case 1:
			path = "1shuzhi";
			break;
		case 2:
			path = "2qingting";
			break;
		case 3:
			path = "3huaban";
			break;
		case 4:
			path = "4huluobo";
			break;
		case 5:
			path = "5yezi";
			break;
		case 6:
			path = "6shouzhang";
			break;
		case 7:
			path = "7guandao";
			break;
		case 8:
			path = "8shudong";
			break;
		case 9:
			path = "9weiqiang";
			break;
		default:
			break;
		}
		bundle.putString("path", path);
		intent.setClass(this, SeniorPointsActivity.class);
		intent.putExtras(bundle);
		intent.putExtra("position", position);
		startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<View> viewList = new ArrayList<View>();
		viewList.add(getLayoutInflater().inflate(R.layout.ratingseniorgames,
				null));
		newAdapter = new NewAdapter(this);
		setListAdapter(newAdapter);

	}
}
