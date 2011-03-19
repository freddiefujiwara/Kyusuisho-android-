package com.appspot.kyusuisho.activity;

import java.util.Vector;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.appspot.kyusuisho.R;
import com.appspot.kyusuisho.model.Area;

public class Areas extends ActivityBase{
	private static Area area;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.areas);
		area = new Area(getApplicationContext());
                Bundle extras = getIntent().getExtras();
                Vector<String> areas = Area.findAreas(this,area,extras.getString("prefecture"));
                TextView prefecture = (TextView)findViewById(R.id.prefecture);
                prefecture.setText(extras.getString("prefecture"));
                ListView areas_list = (ListView)findViewById(R.id.areas_list);
                areas_list.setAdapter(new ArrayAdapter<String>(
			this, android.R.layout.simple_list_item_1, (String[]) areas.toArray(new String[areas.size()])));
                areas_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
										@Override
										public void onItemClick(
												AdapterView<?> parent, View arg1,
												int position, long arg3) {
					                        ListView areas_list = (ListView) parent;
					                        // クリックされたアイテムを取得します
					                        String item = (String) areas_list.getItemAtPosition(position);
					                        String url = Area.findMapUrl(Areas.this,area,item);
					                        Log.d("Kyusuisho",url);
								startActivity(new Intent(Intent.ACTION_VIEW, Uri
					                                        .parse(url)));
											
										}
                });


	}
}
