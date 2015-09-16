package db;

import java.util.List;

import model.ARLocation;
import model.RunObj;
import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

public class DBHelper {
	
	private DBOpenHelper helper;
	
	public DBHelper(Context context){
		helper = new DBOpenHelper(context);
	}
	
	public boolean insertRun(RunObj run,int run_id){
		SQLiteDatabase db = helper.getWritableDatabase();
		ContentValues values = new ContentValues();
		values.put(DBOpenHelper.RUN_ID,run_id);
		values.put(DBOpenHelper.DISTANCE, run.getDistance());
		values.put(DBOpenHelper.DURATION, run.getDuration());
		values.put(DBOpenHelper.TIME, run.getTime());
		values.put(DBOpenHelper.START_ADDR, run.getStartAddr());
		values.put(DBOpenHelper.FINISH_ADDR, run.getFinishAddr());
		if(db.insert(DBOpenHelper.RUN_TABLE, null, values) == -1){
			return false;
		}
		List<ARLocation> locations = run.getLocations();
		db.beginTransaction();
		for(ARLocation location:locations){
		  values.clear();
		  values.put(DBOpenHelper.RUN_ID, run_id);
		  values.put(DBOpenHelper.LATITUDE, location.getLatitude());
		  values.put(DBOpenHelper.LONGTITUDE, location.getLongtitude());
		  values.put(DBOpenHelper.TIME, location.getTime());
		  values.put(DBOpenHelper.ADDRESS, location.getAddr());
		  values.put(DBOpenHelper.CITY, location.getCity());
		  if(db.insert(DBOpenHelper.LOCATION_TABLE, null, values) == -1){
			  db.endTransaction();
			  return false;
		  }
		}
		db.close();
		return true;
	}
	
	public void a(){
		SQLiteDatabase db = helper.getWritableDatabase();
		db.close();
	}

}
