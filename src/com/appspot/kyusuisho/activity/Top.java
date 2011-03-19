package com.appspot.kyusuisho.activity;

import java.io.IOException;
import java.net.URISyntaxException;

import org.apache.http.client.ClientProtocolException;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Intent;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import com.appspot.kyusuisho.R;
import com.appspot.kyusuisho.model.Area;
import com.appspot.kyusuisho.model.KyusuishoException;

public class Top extends ActivityBase implements Animation.AnimationListener {
	private static Area area;
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		area = new Area(getApplicationContext());
		setContentView(R.layout.top);
		ImageView img = (ImageView) findViewById(R.id.kyusuisho_logo);
		Animation animation = AnimationUtils.loadAnimation(this, R.anim.top);
		animation.setAnimationListener(this);
		img.startAnimation(animation);
	}

	public void onAnimationEnd(Animation arg0) {

        asyncRequest(new Runnable() {
          public void run() {
            try {
				Area.updateFromAPI(Top.this,Top.area);
				Intent intent = new Intent(Top.this, Prefectures.class);
				intent.setAction(Intent.ACTION_VIEW);
                startActivityForResult(intent, 0);
			} catch (ClientProtocolException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (URISyntaxException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (XmlPullParserException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (KyusuishoException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
         finish();
          }
        });

	}

	public void onAnimationRepeat(Animation animation) {
		// TODO Auto-generated method stub

	}

	public void onAnimationStart(Animation animation) {
		// TODO Auto-generated method stub

	}
}
