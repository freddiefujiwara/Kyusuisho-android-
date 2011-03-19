package com.appspot.kyusuisho.activity;

import java.util.Vector;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.appspot.kyusuisho.R;
import com.appspot.kyusuisho.model.Area;

public class Prefectures extends ActivityBase{
	private static Area area;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.prefectures);
		area = new Area(getApplicationContext());
                Vector<String> prefectures = Area.findPrefectures(this,area);
                ListView prefectures_list = (ListView)findViewById(R.id.prefectures_list);
                prefectures_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                 

					@Override
					public void onItemClick(AdapterView<?> parent, View arg1,
							int position, long arg3) {
                        ListView prefectures_list = (ListView) parent;
                        // クリックされたアイテムを取得します
                        String item = (String) prefectures_list.getItemAtPosition(position);
                        Intent intent = new Intent(Prefectures.this, Areas.class);
                        intent.putExtra("prefecture", item);
                        startActivity(intent);
						
					}
                });
                prefectures_list.setAdapter(new ArrayAdapter<String>(
			this, android.R.layout.simple_list_item_1, (String[]) prefectures.toArray(new String[prefectures.size()])));

	}
}
