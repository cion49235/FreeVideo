package link.app.byunm.Activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.text.Spannable;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admixer.AdAdapter;
import com.admixer.AdMixerManager;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import gun0912.tedadhelper.TedAdHelper;
import gun0912.tedadhelper.banner.OnBannerAdListener;
import gun0912.tedadhelper.banner.TedAdBanner;
import link.app.byunm.R;
import link.app.byunm.Util.PreferenceUtil;
import link.app.byunm.data.Favorite_DBopenHelper;
import link.app.byunm.data.Favorite_Data;

public class FavoriteActivity extends Activity implements OnClickListener,OnItemClickListener, OnScrollListener {
	public static Context context;
	private ArrayList<Favorite_Data> list;
	private LinearLayout layout_listview_main, layout_nodata;
	private GridView listview;
	private ActivityAdapter adapter;
	private ImageButton btnLeft;
	private TextView main_title;
	private Favorite_DBopenHelper favorite_mydb;
	private Button bt_favorite, bt_review;
	private com.google.android.gms.ads.AdView adView;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_favorite);
		context = this;
		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "3nuxhxka");
		init_ui();
		set_titlebar();
		list = new ArrayList<Favorite_Data>();
		list.clear();
		displaylist();
		addBannerView();

	}

	private RelativeLayout ad_layout;
	private void addBannerView(){
		ad_layout = (RelativeLayout)findViewById(R.id.ad_layout);
		TedAdBanner.showBanner(ad_layout, context.getString(R.string.facebook_banner_key), context.getString(R.string.admob_banner_key), TedAdHelper.AD_ADMOB, new OnBannerAdListener() {
			@Override
			public void onError(String errorMessage) {
			}

			@Override
			public void onLoaded(int adType) {
			}

			@Override
			public void onAdClicked(int adType) {

			}

			@Override
			public void onFacebookAdCreated(com.facebook.ads.AdView facebookBanner) {
			}
		});
	}
	

	@Override
	protected void onPause() {
		super.onPause();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if(adView != null){
			adView.destroy();
		}
	}
	
	private void init_ui(){
		favorite_mydb = new Favorite_DBopenHelper(this);
		layout_listview_main = (LinearLayout)findViewById(R.id.layout_listview_main);
		layout_nodata = (LinearLayout)findViewById(R.id.layout_nodata);
		bt_favorite = (Button)findViewById(R.id.bt_favorite);
		bt_favorite.setVisibility(View.INVISIBLE);
		bt_review = (Button)findViewById(R.id.bt_review);
		bt_review.setOnClickListener(this);
		listview = (GridView)findViewById(R.id.listview);
		listview.setOnScrollListener(this);
		listview.setOnItemClickListener(this);
		listview.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);

	}
	
	private void set_titlebar(){
		btnLeft = (ImageButton)findViewById(R.id.btnLeft);
		btnLeft.setOnClickListener(this);
		main_title = (TextView)findViewById(R.id.main_title);
		main_title.setSingleLine();
		main_title.setText(context.getString(R.string.bt_favorite));
		main_title.setSingleLine();
	}
	
	private void displaylist(){
		bind_favorite_db(list);
		adapter = new ActivityAdapter();
		listview.setAdapter(adapter);
		if(adapter.getCount() > 0){
			layout_nodata.setVisibility(View.GONE);
		}else{
			layout_nodata.setVisibility(View.VISIBLE);
		}
	}

	public void bind_favorite_db(ArrayList<Favorite_Data> list){
		try{
			Cursor cursor = favorite_mydb.getReadableDatabase().rawQuery("select * from favorite_list order by _id desc", null);
			while(cursor.moveToNext()){
				list.add(new Favorite_Data(cursor.getInt(0), cursor.getString(1), cursor.getString(2), cursor.getString(3), cursor.getString(4), cursor.getString(5), cursor.getString(6)));
			}
		}catch (Exception e) {
		}finally{
			close_favorite_db();
		}
	}

	private void close_favorite_db(){
		if(favorite_mydb != null)
			favorite_mydb.close();
	}
	
	@Override
	public void onClick(View view) {
		if(view == btnLeft){
			onBackPressed();
		}else if(view == bt_review){
			String packageName = "";
			try {
				PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(),0);
				packageName = getPackageName();
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
			} catch (PackageManager.NameNotFoundException e){
			} catch (ActivityNotFoundException e) {
				startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
			}
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> arg0, View view, int position, long arg3) {
		Favorite_Data favorite_data = (Favorite_Data)adapter.getItem(position);
		Intent intent = new Intent(context, BrowserActivity.class);
//		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		intent.putExtra("portal", favorite_data.getPortal());
		startActivity(intent);
	}
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if(scrollState == OnScrollListener.SCROLL_STATE_FLING){
			listview.setFastScrollEnabled(true);
		}else{
			listview.setFastScrollEnabled(false);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
	}

	
	public class ActivityAdapter extends BaseAdapter {
		public ActivityAdapter() {
		}
		@Override
		public int getCount() {
			return list.size();
		}

		@Override
		public Object getItem(int position) {
			return list.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View view, ViewGroup parent) {
			try{
				if(view == null){	
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					view = layoutInflater.inflate(R.layout.favorite_activity_listrow, parent, false);
					ViewHolder holder = new ViewHolder();

					holder.img_favorite_imageurl = (ImageView)view.findViewById(R.id.img_favorite_imageurl);
					holder.txt_title = (TextView)view.findViewById(R.id.txt_title);
					holder.bt_favorite_del = (Button) view.findViewById(R.id.bt_favorite_del);
					holder.bt_favorite_del.setFocusable(false);
					holder.bt_favorite_del.setSelected(false);
					view.setTag(holder);
				}
				
				final ViewHolder holder = (ViewHolder)view.getTag();

				Picasso.with(context)
						.load(list.get(position).getThumbnail_hq())
						.placeholder(R.drawable.empty)
						.error(R.drawable.empty)
						.into(holder.img_favorite_imageurl);
				
				holder.txt_title.setText(list.get(position).getTitle());
				holder.bt_favorite_del.setOnClickListener(new OnClickListener() {
					@Override
					public void onClick(View view) {
						AlertShow_CCM_Activity_Favorite(context.getString(R.string.bt_favorite_del21), list.get(position).get_id());
					}
				});
			}catch (Exception e) {
			}
			return view;
		}
	}

	public void delete_ccm_favorite_db(int _id){
		try{
			favorite_mydb.getWritableDatabase().delete("favorite_list", "_id" + "=" +_id, null);
		}catch (Exception e) {
		}finally{
			close_favorite_db();
		}
	}
	
	public void AlertShow_CCM_Activity_Favorite(String msg, final int id) {
        AlertDialog.Builder alert_internet_status = new AlertDialog.Builder(context);
        alert_internet_status.setCancelable(true);
        alert_internet_status.setMessage(msg);
        alert_internet_status.setPositiveButton(context.getString(R.string.frg_ccm_19),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                    	Toast.makeText(context, context.getString(R.string.frg_ccm_22), Toast.LENGTH_SHORT).show();
						delete_ccm_favorite_db(id);
						list = new ArrayList<Favorite_Data>();
						list.clear();
						displaylist();
                    }
                });
        alert_internet_status.setNegativeButton(context.getString(R.string.frg_ccm_20),
       		 new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
        alert_internet_status.show();
	}
	
	private class ViewHolder {
		public ImageView img_favorite_imageurl;
		public TextView txt_title;
		public Button bt_favorite_del;
	}
	
	private void setTextViewColorPartial(TextView view, String fulltext, String subtext, int color) {
		try{
			view.setText(fulltext, TextView.BufferType.SPANNABLE);
			Spannable str = (Spannable) view.getText();
			int i = fulltext.indexOf(subtext);
			str.setSpan(new ForegroundColorSpan(color), i, i + subtext.length(), Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
		}catch (IndexOutOfBoundsException e) {
		}
	}
	
	private void datasetchanged(){
		if(adapter != null){
			adapter.notifyDataSetChanged();
		}
	}
}
