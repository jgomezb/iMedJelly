package com.science_o_matic.imedjelly.activity;

import java.io.IOException;
import java.util.Locale;

import android.app.ListActivity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TableLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.stream.JsonReader;
import com.science_o_matic.imedjelly.R;
import com.science_o_matic.imedjelly.adapter.CommentListAdapter;
import com.science_o_matic.imedjelly.data.BeachDescription;
import com.science_o_matic.imedjelly.data.JsonObject;
import com.science_o_matic.imedjelly.data.Table;
import com.science_o_matic.imedjelly.data.TaskLoader;
import com.science_o_matic.imedjelly.util.Util;

public class BeachActivity extends ListActivity implements JsonObject {
	private Context mContext;
	private TaskLoader mLoader = null;
	private BeachDescription mDescription = null;
	private long mId;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = this;
		final Bundle extras = getIntent().getExtras();
		if(extras != null) {
			mId = extras.getLong("id");
			setContentView(R.layout.activity_spinner);
			// Load beach.
			mLoader = new TaskLoader(this) {
				@Override
				protected void onPostExecute(Task[] results){
					String result = getResult(results);
					if (!result.equals(TaskLoader.Task.SUCCESS)) {
						Toast.makeText(mContext, result, Toast.LENGTH_SHORT).show();
					}
					else {
						updateJellyFishStatus();
						showDescription();
					}
				}

			};
			TaskLoader.Task task = mLoader.new Task(mLoader.getApiClient().getBeachData(mId), this);
			mLoader.execute(task);
		}
	}

	private void updateJellyFishStatus() {
		DataSource dataSource = new DataSource(mContext, Table.beach);
		dataSource.openWrite();
		ContentValues values = new ContentValues();
		values.put("jellyFishStatus", mDescription.jellyFishStatus);
		dataSource.update(values, "id = ?", new String[] {Long.toString(mId)});
		dataSource.close();
	}

	public void showDescription() {
		setContentView(R.layout.activity_beach);
		final View header = getLayoutInflater().inflate(R.layout.activity_beach_description, null);
		final ListView view = (ListView) findViewById(android.R.id.list);
		CommentListAdapter listAdapter = new CommentListAdapter(mContext, mDescription.comments);
		updateView(header, mDescription);
		view.addHeaderView(header);
		view.setAdapter(listAdapter);
		// Add comment button.
		OnClickListener commentListener = new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		// Start comment activity.
            	Intent intent = new Intent(getBaseContext(), CommentActivity.class)
            		.putExtra("id", mId);
            	startActivity(intent);
			}
        };
        ImageView addComment = (ImageView) header.findViewById(R.id.comment_add);
        addComment.setOnClickListener(commentListener);
		// Add services button.
		OnClickListener servicesListener = new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		// Start beach activity.
            	Intent intent = new Intent(getBaseContext(), ServicesActivity.class);
            	startActivity(intent);
			}
        };
        TableLayout layout = (TableLayout) header.findViewById(R.id.services);
		layout.setOnClickListener(servicesListener);
		// Add state button.
		OnClickListener stateListener = new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		// Start state activity.
            	Intent intent = new Intent(getBaseContext(), StateActivity.class);
            	startActivity(intent);
			}
        };
        ImageView flagStatus = (ImageView) header.findViewById(R.id.flagStatus);
        ImageView jellyFishStatus = (ImageView) header.findViewById(R.id.jellyFishStatus);
        flagStatus.setOnClickListener(stateListener);
        jellyFishStatus.setOnClickListener(stateListener);
        // Add camera upload button.
        OnClickListener cameraListener = new OnClickListener() {
        	@Override
			public void onClick(View v) {
        		// Start beach activity.
            	Intent intent = new Intent(getBaseContext(), CameraUploadActivity.class)
            		.putExtra("id", mId);
            	startActivity(intent);
			}
        };
        ImageView cameraUpload = (ImageView) header.findViewById(R.id.camera_upload);
        cameraUpload.setOnClickListener(cameraListener);
        // Add jellyfish button.
        OnClickListener jellyFishListener = new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		String s = ((TextView) v).getText().toString();
        		String[] parts = s.split(",");
        		if(parts.length > 0) {
	        		long id = getJellyFishByName(parts[0]);
	        		if(id != -1) {
		        		// Start jellyfish activity.
	                	Intent intent = new Intent(getBaseContext(), JellyFishActivity.class)
	                		.putExtra("id", id);
	                	startActivity(intent);
	        		}
        		}
        	}
        };
        OnTouchListener jellyFishTouchListener = new OnTouchListener() {
        	@Override
        	public boolean onTouch(View v, MotionEvent event) {
        		if (event.getAction() != MotionEvent.ACTION_DOWN) return false;
        		// Get layout.
        		Layout layout = ((TextView) v).getLayout();
        		if (layout != null) {
        			// Map event position.
        			int line = layout.getLineForVertical((int) event.getY());
    				// Get jelly fish name.
    				String s = ((TextView) v).getText().toString();
    				String[] parts = s.split("\n");
    				if (line < parts.length) {
	    				// Get jelly fish index.
	    				long id = getJellyFishByName(parts[line]);
	    				if(id != -1) {
			        		// Start jellyfish activity.
		                	Intent intent = new Intent(getBaseContext(), JellyFishActivity.class)
		                		.putExtra("id", id);
		                	startActivity(intent);
		        		}
    				}
        		}
        		return true;
        	}
        };
        
        /*
        manip.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                Layout layout = ((TextView) v).getLayout();
                int x = (int)event.getX();
                int y = (int)event.getY();
                if (layout!=null){
                    int line = layout.getLineForVertical(y);
                    int offset = layout.getOffsetForHorizontal(line, x);
                    Log.v("index", ""+offset);
                }
                return true;
            }
            */
        
        TextView jellyFish = (TextView) header.findViewById(R.id.jellyFishes);
        //jellyFish.setOnClickListener(jellyFishListener);
        jellyFish.setOnTouchListener(jellyFishTouchListener);
	}
	
	private void setRatingStars(View view, double rating) {
		ImageView star[] = {
			(ImageView) view.findViewById(R.id.firstStar),
			(ImageView) view.findViewById(R.id.secondStar),
			(ImageView) view.findViewById(R.id.thirdStar),
			(ImageView) view.findViewById(R.id.fourthStar),
			(ImageView) view.findViewById(R.id.fifthStar)
		};
		int full = (int) rating;
		for(int i = 0; i < full; i++) {
			star[i].setImageResource(R.drawable.rating_selected_star);
		}
		if((rating-full) > 0) {
			star[full].setImageResource(R.drawable.rating_half_selected_star);
		}
	}
	
	private void setFlagStatus(View view, String flagstatus) {
		ImageView flag = (ImageView) view.findViewById(R.id.flagStatus);
		if (flagstatus.equals("GREEN")) {
			flag.setImageResource(R.drawable.green);
			flag.setContentDescription(getString(R.string.green_flag));
		}
		else if (flagstatus.equals("RED")) {
			flag.setImageResource(R.drawable.red);
			flag.setContentDescription(getString(R.string.red_flag));
		}
		else if (flagstatus.equals("YELLOW")) {
			flag.setImageResource(R.drawable.yellow);
			flag.setContentDescription(getString(R.string.yellow_flag));
		}
	}

	private void setJellyFishStatus(View view, String status) {
		ImageView jellyfishstatus = (ImageView) view.findViewById(R.id.jellyFishStatus);
		if (status.equals("NO_WARNING")) {
			jellyfishstatus.setImageResource(R.drawable.no_warning);
			jellyfishstatus.setContentDescription(getString(R.string.no_warning));
		}
		else if (status.equals("LOW_WARNING")) {
			jellyfishstatus.setImageResource(R.drawable.low_warning);
			jellyfishstatus.setContentDescription(getString(R.string.low_warning));
		}
		else if (status.equals("HIGH_WARNING")) {
			jellyfishstatus.setImageResource(R.drawable.high_warning);
			jellyfishstatus.setContentDescription(getString(R.string.high_warning));
		}
		else if (status.equals("VERY_HIGH_WARNING")) {
			jellyfishstatus.setImageResource(R.drawable.very_high_warning);
			jellyfishstatus.setContentDescription(getString(R.string.very_high_warning));
		}
	}

	public int getResourceId(String name, String type) {
		return Util.getResourceId(mContext, name, type);
	}

	public void updateView(View view, BeachDescription description) {
		if(description.rating != BeachDescription.Invalid) {
			setRatingStars(view, description.rating);
		}
		if(description.name != null) {
			((TextView) view.findViewById(R.id.name)).setText(description.name);
		}
		if(description != null) {
			((TextView) view.findViewById(R.id.description)).setText(description.description);
		}
		if(description.flagStatus != null) {
			setFlagStatus(view, description.flagStatus);
		}
		for(String s: description.services) {
			int viewId = getResourceId(s, "id");
			int drawableId = getResourceId(s, "drawable");
			((ImageView) view.findViewById(viewId)).setImageResource(drawableId);
		}
		if(description.windSpeed != BeachDescription.Invalid) {
			String text = String.valueOf(description.windSpeed);
			((TextView) view.findViewById(R.id.windSpeed)).setText(text);
		}
		if(description.maxUV != BeachDescription.Invalid) {
			String text = String.valueOf(description.maxUV) + " UV";
			((TextView) view.findViewById(R.id.maxUV)).setText(text);
		}
		if(description.flagStatusUpdated != null) {
			((TextView) view.findViewById(R.id.textView3)).setText(description.flagStatusUpdated);
		}
		if(description.jellyFishStatus != null) {
			setJellyFishStatus(view, description.jellyFishStatus);
		}
		if(description.skyStatusCode != null) {
			int id = getResourceId("s" + description.skyStatusCode, "drawable");
			((ImageView) view.findViewById(R.id.skyStatusCode)).setImageResource(id);
		}
		if(description.maxTemperature != BeachDescription.Invalid) {
			String text = String.valueOf(description.maxTemperature);
			((TextView) view.findViewById(R.id.maxTemperature)).setText(text);
		}
		if(description.minTemperature != BeachDescription.Invalid) {
			String text = String.valueOf(description.minTemperature);
			((TextView) view.findViewById(R.id.minTemperature)).setText(text);
		}
		if(description.windDirection != null) {
			String wind = description.windDirection.toLowerCase(Locale.getDefault());
			int id = getResourceId(wind, "drawable");
			((ImageView) view.findViewById(R.id.windDirection)).setImageResource(id);
		}
		if(description.waterTemperature != BeachDescription.Invalid) {
			String text = String.valueOf(description.waterTemperature) + " ºC";
			((TextView) view.findViewById(R.id.waterTemperature)).setText(text);
		}
		if(description.jellyFishes != null) {
			((TextView) view.findViewById(R.id.jellyFishes)).setText(description.jellyFishes);
		}
	}

	public int getJellyFishByName(String name) {
		String[] jellyFishName = getResources().getStringArray(R.array.jellyfish_arrays);
		for(int i=0; i<jellyFishName.length; i++) {
			if(name.equals(jellyFishName[i])) {
				return i;
			}
		}
		return -1;
	}

	@Override
	public void parseJson(JsonReader reader) throws IOException {
		mDescription = new BeachDescription();
		try {
			mDescription.parseJson(reader);
		}
		catch(IOException e) {
			mDescription = null;
			throw e;
		}
	}
}

/*

	public class BeachLoader extends AsyncTask<Void, Integer, String>{
		private Context mContext = null;
		private long mApi_id = -1;
		private ApiRequest mApi = null;
		private HttpGet mRequest = null;
		private String mErrorMsg = null;
		
		BeachDescription mDescription = null;

		public BeachLoader(Context context, long api_id) {
			this.mContext = context;
			this.mApi_id = api_id;
			this.mApi = new ApiRequest(mContext);
			this.mRequest = mApi.getRequest(mApi.getBeachServiceURL(mApi_id));
		}

		@Override
		protected String doInBackground(Void... voids) {
			Log.d(TAG, "doInBackground");
			try {
				// Perform request.
				HttpResponse response = mApi.performRequest(mRequest);
				StatusLine statusLine = response.getStatusLine();
				int statusCode = statusLine.getStatusCode();
				if (statusCode == 200) {
					// Get body.
					HttpEntity entity = response.getEntity();
					InputStream inStream = entity.getContent();
					BufferedInputStream bufferedStream = new BufferedInputStream(inStream);
					InputStreamReader streamReader = new InputStreamReader(bufferedStream, "UTF-8");
					// Parse JSON.
					JsonReader reader = new JsonReader(streamReader);
					mDescription = new BeachDescription();
					try {
						mDescription.parseJson(reader);
					}
					catch(Exception e) {
						mDescription = null;
						mErrorMsg = e.getLocalizedMessage();
					}
					finally {
						reader.close();
					}
				}
				else if (statusCode != 200) {
					String code = String.valueOf(statusCode);
					Log.d(TAG, "Unable to retrieve data: " + code);
					Toast.makeText(mContext, code, Toast.LENGTH_SHORT).show();
				}
			}
			catch (Exception e) {
				Log.d(TAG, e.getLocalizedMessage());
			}
			return null;
		}

		private void setRatingStars(View view, double rating) {
			ImageView star[] = {
				(ImageView) view.findViewById(R.id.firstStar),
				(ImageView) view.findViewById(R.id.secondStar),
				(ImageView) view.findViewById(R.id.thirdStar),
				(ImageView) view.findViewById(R.id.fourthStar),
				(ImageView) view.findViewById(R.id.fifthStar)
			};
			int full = (int) rating;
			for(int i = 0; i < full; i++) {
				star[i].setImageResource(R.drawable.rating_selected_star);
			}
			if((rating-full) > 0) {
				star[full].setImageResource(R.drawable.rating_half_selected_star);
			}
		}
		
		private void setFlagStatus(View view, String flagstatus) {
			ImageView flag = (ImageView) view.findViewById(R.id.flagStatus);
			if (flagstatus.equals("GREEN")) {
				flag.setImageResource(R.drawable.green);
				flag.setContentDescription(getString(R.string.green_flag));
			}
			else if (flagstatus.equals("RED")) {
				flag.setImageResource(R.drawable.red);
				flag.setContentDescription(getString(R.string.red_flag));
			}
			else if (flagstatus.equals("YELLOW")) {
				flag.setImageResource(R.drawable.yellow);
				flag.setContentDescription(getString(R.string.yellow_flag));
			}
		}

		private void setJellyFishStatus(View view, String status) {
			ImageView jellyfishstatus = (ImageView) view.findViewById(R.id.jellyFishStatus);
			if (status.equals("NO_WARNING")) {
				jellyfishstatus.setImageResource(R.drawable.no_warning);
				jellyfishstatus.setContentDescription(getString(R.string.no_warning));
			}
			else if (status.equals("LOW_WARNING")) {
				jellyfishstatus.setImageResource(R.drawable.low_warning);
				jellyfishstatus.setContentDescription(getString(R.string.low_warning));
			}
			else if (status.equals("HIGH_WARNING")) {
				jellyfishstatus.setImageResource(R.drawable.high_warning);
				jellyfishstatus.setContentDescription(getString(R.string.high_warning));
			}
			else if (status.equals("VERY_HIGH_WARNING")) {
				jellyfishstatus.setImageResource(R.drawable.very_high_warning);
				jellyfishstatus.setContentDescription(getString(R.string.very_high_warning));
			}
		}

		public int getResourceId(String name, String type) {
			return Util.getResourceId(mContext, name, type);
		}

		public void updateView(View view, BeachDescription description) {
			if(description.rating != BeachDescription.Invalid) {
				setRatingStars(view, description.rating);
			}
			if(description.name != null) {
				((TextView) view.findViewById(R.id.name)).setText(description.name);
			}
			if(description != null) {
				((TextView) view.findViewById(R.id.description)).setText(description.description);
			}
			if(description.flagStatus != null) {
				setFlagStatus(view, description.flagStatus);
			}
			for(String s: description.services) {
				int viewId = getResourceId(s, "id");
				int drawableId = getResourceId(s, "drawable");
				((ImageView) view.findViewById(viewId)).setImageResource(drawableId);
			}
			if(description.windSpeed != BeachDescription.Invalid) {
				String text = String.valueOf(description.windSpeed);
				((TextView) view.findViewById(R.id.windSpeed)).setText(text);
			}
			if(description.maxUV != BeachDescription.Invalid) {
				String text = String.valueOf(description.maxUV) + " UV";
				((TextView) view.findViewById(R.id.maxUV)).setText(text);
			}
			if(description.jellyFishStatus != null) {
				setJellyFishStatus(view, description.jellyFishStatus);
			}
			if(description.skyStatusCode != null) {
				int id = getResourceId("s" + description.skyStatusCode, "drawable");
				((ImageView) view.findViewById(R.id.skyStatusCode)).setImageResource(id);
			}
			if(description.maxTemperature != BeachDescription.Invalid) {
				String text = String.valueOf(description.maxTemperature);
				((TextView) view.findViewById(R.id.maxTemperature)).setText(text);
			}
			if(description.minTemperature != BeachDescription.Invalid) {
				String text = String.valueOf(description.minTemperature);
				((TextView) view.findViewById(R.id.minTemperature)).setText(text);
			}
			if(description.windDirection != null) {
				String wind = description.windDirection.toLowerCase(Locale.getDefault());
				int id = getResourceId(wind, "drawable");
				((ImageView) view.findViewById(R.id.windDirection)).setImageResource(id);
			}
			if(description.waterTemperature != BeachDescription.Invalid) {
				String text = String.valueOf(description.waterTemperature) + " ºC";
				((TextView) view.findViewById(R.id.waterTemperature)).setText(text);
			}
			if(description.jellyFishes != null) {
				((TextView) view.findViewById(R.id.jellyFishes)).setText(description.jellyFishes);
			}
		}
		
		@Override
		protected void onPostExecute(String results){
			if(mDescription != null) {
				setContentView(R.layout.activity_beach);
				final View header = getLayoutInflater().inflate(R.layout.activity_beach_description, null);
				final ListView view = (ListView) findViewById(android.R.id.list);
				CommentListAdapter listAdapter = new CommentListAdapter(mContext, mDescription.comments);
				updateView(header, mDescription);
				view.addHeaderView(header);
				view.setAdapter(listAdapter);
				// Add comment button.
				OnClickListener commentListener = new OnClickListener() {
		        	@Override
					public void onClick(View v) {
		        		// Start beach activity.
	                	Intent intent = new Intent(getBaseContext(), CommentActivity.class)
	                		.putExtra("api_id", mApi_id);
	                	startActivity(intent);
					}
		        };
		        ImageView addComment = (ImageView) header.findViewById(R.id.comment_add);
		        addComment.setOnClickListener(commentListener);
				// Add services button.
				OnClickListener servicesListener = new OnClickListener() {
		        	@Override
					public void onClick(View v) {
		        		// Start beach activity.
	                	Intent intent = new Intent(getBaseContext(), ServicesActivity.class);
	                	startActivity(intent);
					}
		        };
		        TableLayout layout = (TableLayout) header.findViewById(R.id.services);
				layout.setOnClickListener(servicesListener);
				// Add state button.
				OnClickListener stateListener = new OnClickListener() {
		        	@Override
					public void onClick(View v) {
		        		// Start state activity.
	                	Intent intent = new Intent(getBaseContext(), StateActivity.class);
	                	startActivity(intent);
					}
		        };
		        ImageView flagStatus = (ImageView) header.findViewById(R.id.flagStatus);
		        ImageView jellyFishStatus = (ImageView) header.findViewById(R.id.jellyFishStatus);
		        flagStatus.setOnClickListener(stateListener);
		        jellyFishStatus.setOnClickListener(stateListener);
		        // Add camera upload button.
		        OnClickListener cameraListener = new OnClickListener() {
		        	@Override
					public void onClick(View v) {
		        		// Start beach activity.
	                	Intent intent = new Intent(getBaseContext(), CameraUploadActivity.class)
	                		.putExtra("api_id", mApi_id);
	                	startActivity(intent);
					}
		        };
		        ImageView cameraUpload = (ImageView) header.findViewById(R.id.camera_upload);
		        cameraUpload.setOnClickListener(cameraListener);
		        // Add jellyfish button.
		        OnClickListener jellyFishListener = new OnClickListener() {
		        	@Override
		        	public void onClick(View v) {
		        		String s = ((TextView) v).getText().toString();
		        		String[] parts = s.split(",");
		        		if(parts.length > 0) {
			        		long id = getJellyFishByName(parts[0]);
			        		if(id != -1) {
				        		// Start jellyfish activity.
			                	Intent intent = new Intent(getBaseContext(), JellyFishActivity.class)
			                		.putExtra("id", id);
			                	startActivity(intent);
			        		}
		        		}
		        	}
		        };
		        TextView jellyFish = (TextView) header.findViewById(R.id.jellyFishes);
		        jellyFish.setOnClickListener(jellyFishListener);
			}
			else {
				//String msg = mErrorMsg != null? mErrorMsg: "Invalid beach data";
				String msg = "Invalid beach data";
				Log.d(TAG, msg);
				Toast.makeText(mContext, msg, Toast.LENGTH_LONG).show();
				finish();
			}
		}
	}
 */