package com.appspot.kyusuisho.android.activity;

import java.util.Vector;

import android.content.Context;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;

import com.appspot.kyusuisho.android.R;
import com.appspot.kyusuisho.android.model.Area;

public class Prefectures extends ActivityBase implements LocationListener {
	private static Area area;
	private LocationManager locationManager;
	private Button currentLocationButton;

	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prefectures);
		area = new Area(getApplicationContext());
		currentLocationButton = (Button) findViewById(R.id.current_location);
		currentLocationButton.setOnClickListener(new View.OnClickListener() {
			public void onClick(View view) {
				currentLocationButton.setEnabled(false);
				locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
				// ローケーション取得条件の設定
				Criteria criteria = new Criteria();
				criteria.setAccuracy(Criteria.ACCURACY_COARSE);
				criteria.setPowerRequirement(Criteria.POWER_LOW);
				criteria.setSpeedRequired(false);
				criteria.setAltitudeRequired(false);
				criteria.setBearingRequired(false);
				criteria.setCostAllowed(false);
				locationManager.requestLocationUpdates(
						locationManager.getBestProvider(criteria, true), 1000,
						1, Prefectures.this);
			}
		});
		Vector<String> prefectures = Area.findPrefectures(this, area);
		ListView prefectures_list = (ListView) findViewById(R.id.prefectures_list);
		prefectures_list
				.setOnItemClickListener(new AdapterView.OnItemClickListener() {

					@Override
					public void onItemClick(AdapterView<?> parent, View arg1,
							int position, long arg3) {
						ListView prefectures_list = (ListView) parent;
						String item = (String) prefectures_list
								.getItemAtPosition(position);
						Intent intent = new Intent(Prefectures.this,
								Areas.class);
						intent.putExtra("prefecture", item);
						startActivity(intent);

					}
				});
		prefectures_list.setAdapter(new ArrayAdapter<String>(this,
				android.R.layout.simple_list_item_1, (String[]) prefectures
						.toArray(new String[prefectures.size()])));

	}

	@Override
	public void onLocationChanged(Location location) {
		Log.v("----------", "----------");
		Log.v("Latitude", String.valueOf(location.getLatitude()));
		Log.v("Longitude", String.valueOf(location.getLongitude()));
		Log.v("Accuracy", String.valueOf(location.getAccuracy()));
		Log.v("Altitude", String.valueOf(location.getAltitude()));
		Log.v("Time", String.valueOf(location.getTime()));
		Log.v("Speed", String.valueOf(location.getSpeed()));
		Log.v("Bearing", String.valueOf(location.getBearing()));
		String url = Area.findNearestMapUrl(this, area, location.getLatitude(),
				location.getLongitude());
		locationManager.removeUpdates(this);
		currentLocationButton.setEnabled(true);
		moveNearest(url);
	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		switch (status) {
		case LocationProvider.AVAILABLE: // GPSが利用可能となった
			Log.v("Status", "AVAILABLE");
			break;
		case LocationProvider.OUT_OF_SERVICE: // GPSが利用不可となった
			Log.v("Status", "OUT_OF_SERVICE");
			break;
		case LocationProvider.TEMPORARILY_UNAVAILABLE: // GPSが一時的に利用不可となった
			Log.v("Status", "TEMPORARILY_UNAVAILABLE");
			break;
		}

	}

	private void moveNearest(String url) {
		startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));

	}

}
