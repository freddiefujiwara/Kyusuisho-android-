package com.appspot.kyusuisho.model;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.Vector;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.appspot.kyusuisho.R;
import com.appspot.kyusuisho.activity.ActivityBase;

import android.util.Log;
import android.util.Xml;
import android.net.Uri;

public class Area extends SQLiteOpenHelper {
	public static class Data{
		public String	   id		  = null;
		public String	   prefecture	  = null;
		public String	   city		  = null;
		public Uri	   map_url	  = null;
		public Uri	   mobile_map_url = null;
		public double	   max_lat	  = 0.0;
		public double	   max_lng	  = 0.0;
		public double	   mid_lat	  = 0.0;
		public double	   mid_lng	  = 0.0;
		public double	   min_lat	  = 0.0;
		public double	   min_lng	  = 0.0;
		public int	   order_value          = 0;
	  
	}
	public Area(Context context) {
		super(context, DATABASE_NAME, null, 1);
	}

	private static HttpClient httpClient = null;
	private static HttpGet	httpGet	= null;


	private static final String DATABASE_NAME = "kyusuisho";

	private static final String CREATE_TABLE = "CREATE TABLE area ("
			+ "id VARCHAR(32) PRIMARY KEY," 
			+ "prefecture VARCHAR(32)," 
			+ "city VARCHAR(32)," 
			+ "map_url TEXT," 
			+ "mobile_map_url TEXT," 
			+ "max_lat DOUBLE," 
			+ "max_lng DOUBLE," 
			+ "mid_lat DOUBLE," 
			+ "mid_lng DOUBLE," 
			+ "min_lat DOUBLE," 
			+ "min_lng DOUBLE," 
			+ "order_value INTEGER" 
			+ ")";


	public static void updateFromAPI(ActivityBase activity,Area db) throws URISyntaxException, ClientProtocolException, IOException, XmlPullParserException, KyusuishoException{
	  String api_url = activity.getString(R.string.config_api_all);
	  URI uri = null;
	  uri = new URI(api_url);
	  httpGet = new HttpGet();
	  httpGet.setURI(uri);
	  HttpResponse resp = null;
	  httpClient = new DefaultHttpClient();
	  resp = httpClient.execute(httpGet);
	  if (400 <= resp.getStatusLine().getStatusCode()) {
		Log.w("Kyusuisho",resp.getStatusLine().toString());
		throw new KyusuishoException("Internal Server Error");
	  }
	  InputStreamReader in = null;
	  in = new InputStreamReader(resp.getEntity().getContent(),"UTF-8");
	  Area.parseList(in,db);
	}
	public static Vector<String> findPrefectures(ActivityBase activity,Area db){
	  Vector<String> list = new Vector<String>(); 
          SQLiteDatabase sqldb = db.getReadableDatabase();
          Cursor cursor = sqldb.query("area", new String[] {"prefecture"}, null, null,"prefecture", null, "order_value");
          while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
          }
          sqldb.close();
          return list;
	}
	public static Vector<String> findAreas(ActivityBase activity,Area db,String prefecture){
	  Vector<String> list = new Vector<String>(); 
          SQLiteDatabase sqldb = db.getReadableDatabase();
          Cursor cursor = sqldb.query("area", new String[] {"city"}, "prefecture = ?",new String[]{prefecture},null, null, "order_value");
          while (cursor.moveToNext()) {
            list.add(cursor.getString(0));
          }
          sqldb.close();
          return list;
	}
	public static String findMapUrl(ActivityBase activity,Area db,String city){
	  String url = "";
          SQLiteDatabase sqldb = db.getReadableDatabase();
          Cursor cursor = sqldb.query("area", new String[] {"map_url"}, "city = ?",new String[]{city},null, null, "order_value");
          while (cursor.moveToNext()) {
            url = cursor.getString(0);
          }
          sqldb.close();
          return url;
	}

	private static void parseList(InputStreamReader in,Area db) throws XmlPullParserException, IOException, KyusuishoException{
	  XmlPullParser parser = Xml.newPullParser();
	  try {
		  parser.setInput(in);
	  } catch (XmlPullParserException e) {
		  StringWriter sw = new StringWriter();
		  PrintWriter pw = new PrintWriter(sw);
		  e.printStackTrace(pw);
		  Log.w("Kyusuisho",sw.toString());
	  }
	  Data area  = null;
	  String xmlTag = null;
	  String value = null;
          SQLiteDatabase sqldb = db.getWritableDatabase();
	  while (true) {
		  int type = -1;
		  type = parser.next();
		  if (XmlPullParser.START_TAG == type) {
			  xmlTag = parser.getName();

			  if(xmlTag.equals("area")){
				  area = new Data();
			  }
		  }
		  else if (XmlPullParser.TEXT == type) {
			value = parser.getText();
		  }
		  else if (XmlPullParser.END_TAG == type) {

			xmlTag = parser.getName();
			if(null != area && xmlTag.equals("area")){
                          String replace = "REPLACE INTO area VALUES ('"
                                       + area.id + "','"
                                       + area.prefecture + "','"
                                       + area.city + "','"
                                       + area.map_url + "','"
                                       + area.mobile_map_url + "','"
                                       + area.max_lat + "','"
                                       + area.max_lng + "','"
                                       + area.mid_lat + "','"
                                       + area.mid_lng + "','"
                                       + area.min_lat + "','"
                                       + area.min_lng + "','"
                                       + area.order_value   + "')";
                          sqldb.execSQL(replace);
			};
			if(null != value && !value.matches("^[\r\n ]*$") && 5 == parser.getDepth()){
			  if(xmlTag.equals("id")){
				 area.id = value;
			  }else if(xmlTag.equals("prefecture")){
				 area.prefecture = value;
			  }else if(xmlTag.equals("city")){
				 area.city	= value;
			  }else if(xmlTag.equals("map_url")){
				 area.map_url = Uri.parse(value);
			  }else if(xmlTag.equals("mobile_map_url")){
				 area.mobile_map_url = Uri.parse(value);
			  }else if(xmlTag.equals("max_lat")){
				 area.max_lat	   = Double.valueOf(value).doubleValue();
			  }else if(xmlTag.equals("max_lng")){
				 area.max_lng	   = Double.valueOf(value).doubleValue();
			  }else if(xmlTag.equals("mid_lat")){
				 area.mid_lat	   = Double.valueOf(value).doubleValue();
			  }else if(xmlTag.equals("mid_lng")){
				 area.mid_lng	   = Double.valueOf(value).doubleValue();
			  }else if(xmlTag.equals("min_lat")){
				 area.min_lat	   = Double.valueOf(value).doubleValue();
			  }else if(xmlTag.equals("min_lng")){
				 area.min_lng	   = Double.valueOf(value).doubleValue();
			  }else if(xmlTag.equals("order")){
				 area.order_value	   = Integer.valueOf(value).intValue();
			  }
			}
		  }
		  else if (type==XmlPullParser.END_DOCUMENT) {
			break;
		  }
	  }
          sqldb.close();
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CREATE_TABLE);
		
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL("DROP TABLE IF EXISTS map");
		onCreate(db);
	}

}
