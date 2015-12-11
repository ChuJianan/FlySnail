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

public class PrimaryGamesActivity extends ListActivity {
	private static String[] applicationNames = new String[] { "1. 四点连线",
			"2. 九点连线", "3. 三十六点连线", "4. 六十四点连线" };

	private static int[] resIds = new int[] { R.drawable.fourpoints,
			R.drawable.ninepoints, R.drawable.thitysixpoints,
			R.drawable.sixtyfourpoints };

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
					R.layout.ratingprimarygames, null);
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
			path = "1four";
			break;
		case 2:
			path = "2nine";
			break;
		case 3:
			path = "3thirtysix";
			break;
		case 4:
			path = "4sixtyfour";
			break;
		}
		bundle.putString("path", path);
		intent.setClass(this, PrimaryPointsActivity.class);
		intent.putExtras(bundle);
		startActivity(intent);
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		List<View> viewList = new ArrayList<View>();
		viewList.add(getLayoutInflater().inflate(R.layout.ratingprimarygames,
				null));
		raAdapter = new RatingAdapter(this);
		setListAdapter(raAdapter);

	}
}
