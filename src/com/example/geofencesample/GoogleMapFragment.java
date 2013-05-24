package com.example.geofencesample;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.LocationClient;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * Google map表示クラス
 * 
 */
public class GoogleMapFragment extends Fragment implements GooglePlayServicesClient.ConnectionCallbacks,
		GooglePlayServicesClient.OnConnectionFailedListener, LocationClient.OnAddGeofencesResultListener, LocationListener,
		GoogleMap.OnMapClickListener {

	private static final String TAG = GoogleMapFragment.class.getSimpleName();

	/** google map */
	private GoogleMap mGoogleMap;

	/** ロケーションクライント */
	private LocationClient mLocationClient;

	/** 地図タップ位置の緯度経度 */
	private LatLng mTappedLatlng;

	/** ジオフェンス情報を保持するプリファレンス */
	private SimpleGeofenceStore mGeofenceStore;
	
	/** マップズームサイズ */
	private static final int MAP_ZOOM_SIZE = 14;
	
	/** フェンスの範囲(メートル) */
	private static final int GEO_FENCE_RADIUS = 1000;

	/**
	 * 
	 */
	public GoogleMapFragment() {
		super();
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * android.support.v4.app.Fragment#onCreateView(android.view.LayoutInflater,
	 * android.view.ViewGroup, android.os.Bundle)
	 */
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.fragment_google_maps, container);

		return view;
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see android.support.v4.app.Fragment#onActivityCreated(android.os.Bundle)
	 */
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		try {
			// マップフラグメント取得
			SupportMapFragment fragment = (SupportMapFragment) getActivity().getSupportFragmentManager()
					.findFragmentById(R.id.fragment_map);
			mGoogleMap = fragment.getMap();
			mGoogleMap.setOnMapClickListener(this);

			mGeofenceStore = new SimpleGeofenceStore(getActivity());

			mLocationClient = new LocationClient(getActivity(), this, this);

			// 初期位置を設定(スカイツリー)
			LatLng latlng = new LatLng(35.711096, 139.812302);
			// カメラの位置情報を作成する
			CameraUpdate camera = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latlng).zoom(MAP_ZOOM_SIZE).build());
			// 地図移動
			mGoogleMap.animateCamera(camera);
		} catch (Exception e) {
			Log.d(TAG, e.getMessage());
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.location.LocationClient.OnAddGeofencesResultListener
	 * #onAddGeofencesResult(int, java.lang.String[])
	 */
	@Override
	public void onAddGeofencesResult(int statusCode, String[] arg1) {
		Log.d(TAG, "onAddGeofencesResult");

		// If adding the geocodes was successful
		if (LocationStatusCodes.SUCCESS == statusCode) {
			Log.d(TAG, "SUCCESS");
		}

		for (String string : arg1) {
			Log.d(TAG, string);
		}
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see com.google.android.gms.common.GooglePlayServicesClient.
	 * OnConnectionFailedListener
	 * #onConnectionFailed(com.google.android.gms.common.ConnectionResult)
	 */
	@Override
	public void onConnectionFailed(ConnectionResult arg0) {
		Log.d(TAG, "onConnectionFailed");
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
	 * #onConnected(android.os.Bundle)
	 */
	@Override
	public void onConnected(Bundle arg0) {
		Log.d(TAG, "onConnected");

		// // GPSの位置情報リスナー登録
		// LocationRequest request = LocationRequest.create();
		// // 1秒後毎に計測
		// request.setFastestInterval(1000);
		// request.setInterval(1000);
		// request.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		//
		// mLocationClient.requestLocationUpdates(request, this);

		// ジオフェンス登録
		addGeoFences(mTappedLatlng);
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.common.GooglePlayServicesClient.ConnectionCallbacks
	 * #onDisconnected()
	 */
	@Override
	public void onDisconnected() {
		Log.d(TAG, "onDisconnected");
	}

	/*
	 * (非 Javadoc)
	 * 
	 * @see
	 * com.google.android.gms.location.LocationListener#onLocationChanged(android
	 * .location.Location)
	 */
	@Override
	public void onLocationChanged(Location location) {
		Log.d(TAG, "onLocationChanged");

		// LatLng latlng = new LatLng(location.getLatitude(),
		// location.getLongitude());
		// CameraUpdate camera = CameraUpdateFactory.newCameraPosition(new
		// CameraPosition.Builder().target(latlng).zoom(14).build());
		// 地図移動
		// mGoogleMap.animateCamera(camera);
	}

	@Override
	public void onMapClick(LatLng latlng) {
		Log.d(TAG, "onMapClick");

		mTappedLatlng = latlng;

		String address = getAddress(latlng);

		// ピンを立てる
		MarkerOptions options = new MarkerOptions();
		options.position(latlng);
		options.title(address);
		mGoogleMap.addMarker(options);

		// 地図移動
		CameraUpdate camera = CameraUpdateFactory.newCameraPosition(new CameraPosition.Builder().target(latlng).zoom(MAP_ZOOM_SIZE).build());
		mGoogleMap.animateCamera(camera);

		Circle circle = mGoogleMap.addCircle(new CircleOptions().center(latlng).radius(GEO_FENCE_RADIUS).strokeColor(Color.parseColor("#FF0000"))
				.fillColor(Color.parseColor("#11FF0000")));

		// Connects the client to Google Play services.
		mLocationClient.connect();
	}

	private void addGeoFences(LatLng latlng) {
		// 緯度経度から住所を取得
		String address = getAddress(latlng);
		if (TextUtils.isEmpty(address) == true) {
			return;
		}

		// ジオフェンスリスト
		ArrayList<Geofence> fenceList = new ArrayList<Geofence>();

		// ジオフェンス情報をプリファレンスに保存
		SimpleGeofence geofence = new SimpleGeofence("1", latlng.latitude, latlng.longitude, GEO_FENCE_RADIUS, Geofence.NEVER_EXPIRE,
				(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT), address);
		mGeofenceStore.setGeofence("1", geofence);

		fenceList.add(geofence.toGeofence());

		// ジオフェンスイベント受信インテント
		Intent intent = new Intent(getActivity(), GeofenceService.class);
		intent.putExtra(geofence.getId(), "hogehoge");
		PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);

		// ジオフェンス登録
		mLocationClient.addGeofences(fenceList, pendingIntent, this);

		String strLatitude = String.format("%.2f", latlng.latitude);
		String strLongtitude = String.format("%.2f", latlng.longitude);
		StringBuilder sb = new StringBuilder();
		sb.append(address).append("(").append(strLatitude).append(",").append(strLongtitude).append(")をジオフェンスに登録しました");
		Toast.makeText(getActivity(), sb.toString(), Toast.LENGTH_LONG).show();
	}

	/**
	 * 緯度経度から住所を取得する
	 * 
	 * @param latlng
	 * @return
	 */
	private String getAddress(LatLng latlng) {
		String address = null;

		try {
			Geocoder geoCorder = new Geocoder(getActivity());
			List<Address> addressList = geoCorder.getFromLocation(latlng.latitude, latlng.longitude, 1);
			if (addressList != null && addressList.size() != 0) {
				Address addr = addressList.get(0);
				address = addr.getAdminArea() + addr.getLocality() + addr.getSubLocality() + addr.getThoroughfare()
						+ addr.getSubThoroughfare();
			}
		} catch (IOException e) {
			Log.d(TAG, e.getMessage());
		}

		return address;
	}
}
