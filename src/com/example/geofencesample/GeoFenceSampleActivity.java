package com.example.geofencesample;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;

public class GeoFenceSampleActivity extends FragmentActivity {

	private static final String TAG = GeoFenceSampleActivity.class.getSimpleName();

	/*
	 * (非 Javadoc)
	 * @see android.support.v4.app.FragmentActivity#onCreate(android.os.Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		
		try {
			super.onCreate(savedInstanceState);
			setContentView(R.layout.activity_geo_fence_sample);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}		
}
