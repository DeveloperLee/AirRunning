package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class DBOpenHelper extends SQLiteOpenHelper {
	
	
	private static final String DB_NAME ="ar.db";
	private static final int DB_VERSION = 1;
	
	public static final String LOCATION_TABLE = "location";
    public static final String RUN_TABLE = "run";
    public static final String LOCATION_ID = "location_id";
    public static final String LATITUDE = "_latitude";
    public static final String LONGTITUDE = "_longtitude";
    public static final String ADDRESS = "_addr";
    public static final String TIME = "_time";
    public static final String CITY = "_city";
    public static final String RUN_ID = "run_id";
    public static final String DURATION = "_duration";
    public static final String DISTANCE = "distance";
    public static final String START_ADDR = "_startAddr";
    public static final String FINISH_ADDR = "_finishAddr";
    
    public static final String BLUETOOTH_DEVICE_TABLE = "devices";
    public static final String DEVICE_NAME = "device_name";
    public static final String DEVICE_ADDR = "device_addr";
    public static final String BOND_STATE = "bond_state";
    public static final String DEVICE_CLASS = "device_class";

	public DBOpenHelper(Context context) {
		super(context,DB_NAME,null,DB_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL("CREATE TABLE IF NOT EXISTS "+RUN_TABLE+
				"("+RUN_ID+" INTEGER PRIMARY KEY,"+
				 DURATION+" VARCHAR NOT NULL,"+
				 DISTANCE+" DOUBLE NOT NULL,"+
				 TIME+" VARCHAR NOT NULL,"+
				 START_ADDR+" VARCHAR,"+
				 FINISH_ADDR+" VARCHAR);");
		db.execSQL("CREATE TABLE IF NOT EXISTS "+LOCATION_TABLE+
				"("+LOCATION_ID+" INTEGER PRIMARY KEY AUTOINCREMENT,"+
				LATITUDE+" DOUBLE NOT NULL,"+
				LONGTITUDE+" DOUBLE NOT NULL,"+
				ADDRESS+" VARCHAR,"+
				TIME+" VARCHAR,"+
				CITY+" VARCHAR,"+
				RUN_ID+" INTEGER NOT NULL, FOREIGN KEY ("+RUN_ID+") REFERENCES "+RUN_TABLE+"("+RUN_ID+"));");
		Log.i("DB_FINISH", "123123");
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        
	}

}
