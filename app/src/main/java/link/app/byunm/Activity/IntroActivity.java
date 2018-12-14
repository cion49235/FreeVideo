package link.app.byunm.Activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.ActivityNotFoundException;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.admixer.AdAdapter;
import com.admixer.AdMixerManager;
import com.admixer.CustomPopup;
import com.admixer.CustomPopupListener;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.client.ClientProtocolException;
import gun0912.tedadhelper.TedAdHelper;
import gun0912.tedadhelper.backpress.OnBackPressListener;
import gun0912.tedadhelper.backpress.TedBackPressDialog;
import gun0912.tedadhelper.banner.OnBannerAdListener;
import gun0912.tedadhelper.banner.TedAdBanner;
import link.app.byunm.R;
import link.app.byunm.Util.PreferenceUtil;
import link.app.byunm.data.Favorite_DBopenHelper;
import link.app.byunm.data.Main_Data;

import static android.os.Build.VERSION.SDK_INT;


public class IntroActivity extends Activity implements AdapterView.OnItemClickListener, AbsListView.OnScrollListener, CustomPopupListener, View.OnClickListener {
	public Handler handler;
	public Context context;
	private String num;
	private ArrayList<Main_Data> list;
	private LinearLayout layout_progress;
	private Favorite_DBopenHelper favorite_mydb;
	private GridView listview_main;
	private MainAdapter main_adapter;
	private int current_position = 0;
	private LinearLayout layout_nodata;
	private boolean retry_alert = false;
	private Main_ParseAsync main_parseAsync = null;
	private Button bt_favorite, bt_review;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_intro);
		context = this;
		PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, false);
		retry_alert = true;
		init_ui();
		num = "1512";
		list = new ArrayList<Main_Data>();
		list.clear();
		start_index = 1;
		addBannerView();
		adstatus_async = new Adstatus_Async();
		adstatus_async.execute();

		AdMixerManager.getInstance().setAdapterDefaultAppCode(AdAdapter.ADAPTER_ADMIXER, "3nuxhxka");
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

	private void init_ui() {
		bt_favorite = (Button)findViewById(R.id.bt_favorite);
		bt_favorite.setOnClickListener(this);
		bt_review = (Button)findViewById(R.id.bt_review);
		bt_review.setOnClickListener(this);
		layout_nodata = (LinearLayout) findViewById(R.id.layout_nodata);
		layout_progress = (LinearLayout) findViewById(R.id.layout_progress);
		listview_main = (GridView) findViewById(R.id.listview_main);
		favorite_mydb = new Favorite_DBopenHelper(this);
	}

	public void displaylist() {
		main_parseAsync = new Main_ParseAsync();
		main_parseAsync.execute();
		if (SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			listview_main.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
		}
		listview_main.setOnItemClickListener(this);
		listview_main.setOnScrollListener(this);
	}

	@Override
	public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
		Main_Data main_data = (Main_Data) main_adapter.getItem(position);
		PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_URL, main_data.portal);
		Intent intent = new Intent(context, BrowserActivity.class);
		intent.putExtra("portal", main_data.portal);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
		startActivity(intent);
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
						 int visibleItemCount, int totalItemCount) {
		/*if(view == listview_main){
			if(totalItemCount != 0 && firstVisibleItem  > 1 ){
				listview_main.setFastScrollEnabled(true);
			}else{
				listview_main.setFastScrollEnabled(false);
			}
		}*/
	}

	@Override
	public void onClick(View view) {
		if(view == bt_favorite){
			Intent intent = new Intent(this, FavoriteActivity.class);
			intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_SINGLE_TOP);
			startActivity(intent);
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

	private Adstatus_Async adstatus_async = null;
	public class Adstatus_Async extends AsyncTask<String, Integer, String> {
		int ad_id;
		String ad_status;
		String ad_time;
		String ad_time2;
		String version;
		HttpURLConnection localHttpURLConnection;
		public Adstatus_Async(){
		}
		@Override
		protected String doInBackground(String... params) {
			String sTag;
			try{
				String str = "http://cion49235.cafe24.com/xml_ad_status/linkapp_byunm/ad_status.php";
				localHttpURLConnection = (HttpURLConnection)new URL(str).openConnection();
				localHttpURLConnection.setFollowRedirects(true);
				localHttpURLConnection.setConnectTimeout(15000);
				localHttpURLConnection.setReadTimeout(15000);
				localHttpURLConnection.setRequestMethod("GET");
				localHttpURLConnection.connect();
				InputStream inputStream = new URL(str).openStream();
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xpp = factory.newPullParser();
				xpp.setInput(inputStream, "EUC-KR");
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {
					}else if (eventType == XmlPullParser.END_DOCUMENT) {
					}else if (eventType == XmlPullParser.START_TAG){
						sTag = xpp.getName();
						if(sTag.equals("Ad")){
							ad_id = Integer.parseInt(xpp.getAttributeValue(null, "ad_id") + "");
						}else if(sTag.equals("ad_status")){
							ad_status = xpp.nextText()+"";
							PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_AD_STATUS, ad_status);
							Log.i("dsu", "ad_status : " + ad_status);
						}else if(sTag.equals("ad_time")){
							ad_time = xpp.nextText()+"";
							PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_AD_TIME, ad_time);
							Log.i("dsu", "ad_time : " + ad_time);
						}else if(sTag.equals("ad_time2")){
							ad_time2 = xpp.nextText()+"";
							PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_AD_TIME2, ad_time2);
							Log.i("dsu", "ad_time2 : " + ad_time2);
						}else if(sTag.equals("version")){
							version = xpp.nextText()+"";
							PreferenceUtil.setStringSharedData(context, PreferenceUtil.PREF_VERSION, version);
							Log.i("dsu", "version : " + version);
						}
					} else if (eventType == XmlPullParser.END_TAG){
						sTag = xpp.getName();
						if(sTag.equals("Finish")){
						}
					} else if (eventType == XmlPullParser.TEXT) {
					}
					eventType = xpp.next();
				}
			}
			catch (SocketTimeoutException localSocketTimeoutException)
			{
			}
			catch (ClientProtocolException localClientProtocolException)
			{
			}
			catch (IOException localIOException)
			{
			}
			catch (Resources.NotFoundException localNotFoundException)
			{
			}
			catch (NullPointerException NullPointerException)
			{
			}
			catch (Exception e)
			{
			}
			return ad_status;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			version_check();
		}
		@Override
		protected void onPostExecute(String ad_status) {
			super.onPostExecute(ad_status);
		}
		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}

	int versionCode;
	private void version_check(){
		PackageInfo pi=null;
		try {
			pi = getPackageManager().getPackageInfo(getPackageName(), 0);
			versionCode = pi.versionCode;
		} catch (PackageManager.NameNotFoundException e) {
		} catch (NullPointerException e){
		} catch (Exception e){
		}
		if ( (versionCode < Integer.parseInt(PreferenceUtil.getStringSharedData(context, PreferenceUtil.PREF_VERSION, "1"))) && (versionCode > 0) ) {
			AlertDialog.Builder builder = new AlertDialog.Builder(context);
			builder.setTitle(context.getString(R.string.alert_update_01));
			builder.setIcon(R.mipmap.ic_launcher);
			builder.setCancelable(true);
			builder.setMessage(context.getString(R.string.alert_update_02));
			builder.setPositiveButton(context.getString(R.string.alert_update_03), new DialogInterface.OnClickListener(){
				public void onClick(DialogInterface dialog, int whichButton){
					String packageName = "";
					try {
						PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
						packageName = getPackageName();
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
					} catch (PackageManager.NameNotFoundException e) {
					} catch (ActivityNotFoundException e) {
						startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
					}
				}
			});
			AlertDialog myAlertDialog = builder.create();
			myAlertDialog.show();
		} else {
			handler = new Handler();
			handler.postDelayed(runnable, 2000);
		}
	}

	public class Main_ParseAsync extends AsyncTask<String, Integer, String> {
		String Response;
		Main_Data main_data;
		ArrayList<Main_Data> menuItems = new ArrayList<Main_Data>();
		String i;
		int _id;
		String id;
		String title;
		String portal;
		String thumb;
		String sprit_title[];

		public Main_ParseAsync() {
		}

		@Override
		protected String doInBackground(String... params) {
			String sTag;
			try {
				String str = context.getString(R.string.cion_url) + i + ".php?view=" + num;
//				Log.i("dsu", "URL : " + str);
				HttpURLConnection localHttpURLConnection = (HttpURLConnection) new URL(str).openConnection();
				HttpURLConnection.setFollowRedirects(false);
				localHttpURLConnection.setConnectTimeout(15000);
				localHttpURLConnection.setReadTimeout(15000);
				localHttpURLConnection.setRequestMethod("GET");
				localHttpURLConnection.connect();
				InputStream inputStream = new URL(str).openStream();
				XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
				XmlPullParser xpp = factory.newPullParser();
				xpp.setInput(inputStream, "EUC-KR");
				int eventType = xpp.getEventType();
				while (eventType != XmlPullParser.END_DOCUMENT) {
					if (eventType == XmlPullParser.START_DOCUMENT) {
					} else if (eventType == XmlPullParser.END_DOCUMENT) {
					} else if (eventType == XmlPullParser.START_TAG) {
						sTag = xpp.getName();
						if (sTag.equals("Content")) {
							main_data = new Main_Data();
							_id = Integer.parseInt(xpp.getAttributeValue(null, "id") + "");
						} else if (sTag.equals("videoid")) {
							Response = xpp.nextText() + "";
						} else if (sTag.equals("subject")) {
							title = xpp.nextText() + "";
							sprit_title = title.split("-");
						} else if (sTag.equals("portal")) {
							portal = xpp.nextText() + "";
						} else if (sTag.equals("thumb")) {
							thumb = xpp.nextText() + "";
						}
					} else if (eventType == XmlPullParser.END_TAG) {
						sTag = xpp.getName();
						if (sTag.equals("Content")) {
							main_data._id = _id;
							main_data.id = Response;
							main_data.title = title;
							main_data.portal = portal;
							main_data.category = context.getString(R.string.app_name);
							main_data.thumb = thumb;
							list.add(main_data);
						}
					} else if (eventType == XmlPullParser.TEXT) {
					}
					eventType = xpp.next();
				}
			} catch (SocketTimeoutException localSocketTimeoutException) {
			} catch (ClientProtocolException localClientProtocolException) {
			} catch (IOException localIOException) {
			} catch (Resources.NotFoundException localNotFoundException) {
			} catch (NullPointerException NullPointerException) {
			} catch (Exception e) {
			}
			return Response;
		}

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			i = "6";
			layout_progress.setVisibility(View.VISIBLE);
		}

		@Override
		protected void onPostExecute(String Response) {
			super.onPostExecute(Response);
//			Log.i("dsu", "Response : " + Response);
			layout_progress.setVisibility(View.GONE);
			try {
				if (Response != null) {
					for (int i = 0; ; i++) {
						if (i >= list.size()) {
//							while (i > list.size()-1){
							main_adapter = new MainAdapter(context, menuItems, listview_main);
							listview_main.setAdapter(main_adapter);
							listview_main.setFocusable(true);
							listview_main.setSelected(true);
							listview_main.setSelection(current_position);
							if (listview_main.getCount() == 0) {
								layout_nodata.setVisibility(View.VISIBLE);
							} else {
								layout_nodata.setVisibility(View.GONE);
							}
							return;
						}
						menuItems.add(list.get(i));
					}
				} else {
					layout_nodata.setVisibility(View.VISIBLE);
					Retry_AlertShow(context.getString(R.string.sub6_txt8));
				}
			} catch (NullPointerException e) {
			}
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
		}
	}

	public static boolean loadingMore = true;
	public static boolean exeFlag;
	public static int start_index;
	public void Retry_AlertShow(String msg) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setCancelable(false);
		builder.setMessage(msg);
		builder.setInverseBackgroundForced(true);
		builder.setNeutralButton(context.getString(R.string.txt_main_activity14), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				current_position = 0;
				loadingMore = true;
				exeFlag = false;
				main_parseAsync = new Main_ParseAsync();
				main_parseAsync.execute();
				dialog.dismiss();
			}
		});
		builder.setNegativeButton(context.getString(R.string.txt_main_activity13), new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				dialog.dismiss();
				finish();
			}
		});
		AlertDialog myAlertDialog = builder.create();
		if (retry_alert) myAlertDialog.show();
	}

	public class MainAdapter extends BaseAdapter {
		public Context context;
		public int _id = -1;
		public String id = "empty";
		public Cursor cursor;
		public ImageButton bt_favorite;
		public ArrayList<Main_Data> list;
		public GridView listview_main;

		public MainAdapter(Context context, ArrayList<Main_Data> list, GridView listview_main) {
			this.context = context;
			this.list = list;
			this.listview_main = listview_main;
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
			try {
				if (view == null) {
					LayoutInflater layoutInflater = LayoutInflater.from(context);
					view = layoutInflater.inflate(R.layout.main_activity_listrow, parent, false);
					ViewHolder holder = new ViewHolder();
					holder.img_imageurl = (ImageView)view.findViewById(R.id.img_imageurl);
					holder.txt_title = (TextView) view.findViewById(R.id.txt_title);
					holder.bt_favorite = (Button) view.findViewById(R.id.bt_favorite);
					holder.bt_favorite.setFocusable(false);
					holder.bt_favorite.setSelected(false);
					view.setTag(holder);
				}
				final ViewHolder holder = (ViewHolder) view.getTag();
				BitmapFactory.Options dimensions = new BitmapFactory.Options();
				dimensions.inJustDecodeBounds = true;
				Bitmap bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.empty, dimensions);
				int height = dimensions.outHeight;
				int width =  dimensions.outWidth;
				Picasso.with(context)
						.load(list.get(position).thumb)
						.placeholder(R.drawable.empty)
						.error(R.drawable.empty)
						.into(holder.img_imageurl);

				holder.txt_title.setText(list.get(position).title);

				favorite_db(context, list.get(position).id, holder.bt_favorite);
				holder.bt_favorite.setOnClickListener(new View.OnClickListener() {
					@Override
					public void onClick(View view) {
						insert_delete_favorite_db(context, list.get(position).id, list.get(position).title, list.get(position).thumb,list.get(position).portal,holder.bt_favorite);
						datasetchanged();
					}
				});


			} catch (Exception e) {
			}
			return view;
		}
	}

	private void datasetchanged() {
		if (main_adapter != null) {
			main_adapter.notifyDataSetChanged();
		}
	}

	public void favorite_db(Context context, String get_id, Button bt_favorite) {
		try {
			Cursor cursor = favorite_mydb.getReadableDatabase().rawQuery(
					"select * from favorite_list where id = '" + get_id + "'", null);
			if (null != cursor && cursor.moveToFirst()) {
				id = cursor.getString(cursor.getColumnIndex("id"));
				_id = cursor.getInt(cursor.getColumnIndex("_id"));
			} else {
				id = "empty";
				_id = -1;
			}
			if (id.equals("empty")) {
				bt_favorite.setText(context.getString(R.string.bt_favorite_select));
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_pressed);
			} else {
				bt_favorite.setText(context.getString(R.string.bt_favorite_de_select));
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_normal);
			}
		} catch (Exception e) {
		} finally {
			close_favorite_db();
		}
	}

	public void insert_delete_favorite_db(Context context, String get_id, String title, String thumbnail_hq,String portal, Button bt_favorite) {
		try {
			Cursor cursor = favorite_mydb.getReadableDatabase().rawQuery(
					"select * from favorite_list where id = '" + get_id + "'", null);
			if (null != cursor && cursor.moveToFirst()) {
				id = cursor.getString(cursor.getColumnIndex("id"));
				_id = cursor.getInt(cursor.getColumnIndex("_id"));
			} else {
				id = "empty";
				_id = -1;
			}
			if (id.equals("empty")) {
				bt_favorite.setText(context.getString(R.string.bt_favorite_select));
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_pressed);
				ContentValues cv = new ContentValues();
				cv.put("id", get_id);
				cv.put("title", title);
				cv.put("thumbnail_hq", thumbnail_hq);
				cv.put("portal", portal);
				favorite_mydb.getWritableDatabase().insert("favorite_list", null, cv);
				Toast.makeText(context, context.getString(R.string.txt_favorite_03), Toast.LENGTH_SHORT).show();
			} else {
				bt_favorite.setText(context.getString(R.string.bt_favorite_de_select));
				bt_favorite.setBackgroundResource(R.drawable.bg_favorite_normal);
				favorite_mydb.getWritableDatabase().delete("favorite_list", "_id" + "=" + _id, null);
				Toast.makeText(context, context.getString(R.string.txt_favorite_04), Toast.LENGTH_SHORT).show();
			}

		} finally {
			close_favorite_db();
		}
	}

	private void close_favorite_db() {
		if (favorite_mydb != null)
			favorite_mydb.close();
	}

	public int _id = -1;
	String id = "empty";

	private class ViewHolder {
		public ImageView img_imageurl;
		public TextView txt_title;
		public Button bt_favorite;
	}

	@Override
	protected void onRestart() {
		super.onRestart();
		if(main_adapter != null){
			main_adapter.notifyDataSetChanged();
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		retry_alert = false;
		if (handler != null) {
			handler.removeCallbacks(runnable);
		}

		current_position = 0;
		start_index = 1;
		loadingMore = true;
		exeFlag = false;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

	@Override
	protected void onPause() {
		super.onPause();
	}

	@Override
	protected void onStart() {
		super.onStart();
	}

	Runnable runnable = new Runnable() {
		@Override
		public void run() {
			displaylist();
		}
	};

	@Override
	public void onCloseCustomPopup(String arg0) {
	}

	@Override
	public void onHasNoCustomPopup() {
	}

	@Override
	public void onShowCustomPopup(String arg0) {
	}

	@Override
	public void onStartedCustomPopup() {
	}

	@Override
	public void onWillCloseCustomPopup(String arg0) {
	}

	@Override
	public void onWillShowCustomPopup(String arg0) {
	}

	/*@Override
	public void onBackPressed() {
		super.onBackPressed();
	}*/

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if(keyCode == KeyEvent.KEYCODE_BACK){
            TedBackPressDialog.startDialog(this, getString(R.string.app_name), context.getString(R.string.facebook_native_key), context.getString(R.string.admob_banner_key), new Integer[]{TedAdHelper.AD_ADMOB,TedAdHelper.AD_FACEBOOK }, TedAdHelper.ADMOB_NATIVE_AD_TYPE.BANNER, true, new OnBackPressListener() {
                @Override
                public void onReviewClick() {
                    String packageName = "";
                    try {
                        PackageInfo packageInfo = getPackageManager().getPackageInfo(getPackageName(), 0);
                        packageName = getPackageName();
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + packageName)));
                    } catch (PackageManager.NameNotFoundException e) {
                    } catch (ActivityNotFoundException e) {
                        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + packageName)));
                    }
                }
                @Override
                public void onFinish() {
                    PreferenceUtil.setBooleanSharedData(context, PreferenceUtil.PREF_AD_VIEW, true);
                    finish();
                }

                @Override
                public void onError(String errorMessage) {
                }

                @Override
                public void onLoaded(int adType) {
                }

                @Override
                public void onAdClicked(int adType) {
                }
            }, new TedAdHelper.ImageProvider() {
                @Override
                public void onProvideImage(ImageView imageView, String imageUrl) {
                    Glide.with(IntroActivity.this).load(imageUrl).into(imageView);
                }
            });

			return false;
		}
		return super.onKeyDown(keyCode, event);
	}

	private boolean flag;
}
