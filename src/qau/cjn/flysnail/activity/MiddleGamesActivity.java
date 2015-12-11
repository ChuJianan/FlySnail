package qau.cjn.flysnail.activity;

import java.util.ArrayList;
import java.util.List;


import com.yunruiinfo.app.flysnail.R;

import android.app.ListActivity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

public class MiddleGamesActivity extends ListActivity {
	private static String[] applicationNames = new String[] { "1. 台灯", "2. 灯泡",
			"3. 衣服", "4. 白菜", "5. 马", "6. 公鸡", "7. 蘑菇", "8. 苹果", "9. 鱼" };

	private static int[] resIds = new int[] { R.drawable.m1taideng,
			R.drawable.m2dengpao, R.drawable.m3yifu, R.drawable.m4baicai,
			R.drawable.m5ma, R.drawable.m6gongji, R.drawable.m7mogu,
			R.drawable.m8pingguo, R.drawable.m9yu };

	String inflater = Context.LAYOUT_INFLATER_SERVICE;
	LayoutInflater layoutInflater;
	private RatingAdapter raAdapter;

	private class RatingAdapter extends BaseAdapter {
		private Context context;

		public RatingAdapter(Context context) {
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
			return position;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			LinearLayout linearLayout = (LinearLayout) layoutInflater.inflate(
					R.layout.ratingmiddlegames, null);
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
			path = "1taideng";
			break;
		case 2:
			path = "2dengpao";
			break;
		case 3:
			path = "3yifu";
			break;
		case 4:
			path = "4baicai";
			break;
		case 5:
			path = "5ma";
			break;
		case 6:
			path = "6gongji";
			break;
		case 7:
			path = "7mogu";
			break;
		case 8:
			path = "8pingguo";
			break;
		case 9:
			path = "9yu";
			break;
		default:
			break;
		}
		bundle.putString("path", path);
		intent.setClass(this, PointsActivity.class);
		intent.putExtras(bundle);
		intent.putExtra("position", position);
		startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<View> viewList = new ArrayList<View>();
		viewList.add(getLayoutInflater().inflate(R.layout.ratingmiddlegames,
				null));
		raAdapter = new RatingAdapter(this);
		setListAdapter(raAdapter);

	}
}
