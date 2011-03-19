package com.appspot.kyusuisho.activity;

import com.appspot.kyusuisho.R;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Handler;

public class ActivityBase extends Activity {
    public String currentLanguage() {
        String[] availableLanguages = getResources().getStringArray(
                R.array.languages);
        String languageCode = getResources().getConfiguration().locale
                .getLanguage();
        for (String lang : availableLanguages) {
            if (languageCode.equals(lang)) {
                return lang;
            }
        }
        return getResources().getString(R.string.config_default_language);
    }

    public void asyncRequest(final Runnable r) {
        final Handler h = new Handler();
        final Thread t = new Thread(new Runnable() {
            public void run() {
                h.postDelayed(r, 1000);
            }
        });
        t.start();
    }
    public void pageAlert(String title,String message  ){
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle(title);
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int whichButton) {
        	 finish();
        }});
        alert.show();
     }
}
