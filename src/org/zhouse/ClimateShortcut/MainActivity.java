package org.zhouse.ClimateShortcut;
 
import java.util.ArrayList;

import org.zhouse.libs.ObjectAllDevices;
import org.zhouse.libs.ObjectDevice;
import org.zhouse.libs.ZhouseVeraServer;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;
 
public class MainActivity extends Activity {
	final static String TAG = "MainActivity";
	public static ArrayList<ObjectDevice> deviceList;
    private ListView listView1 ;    
    ZhouseDeviceListAdapter adapter;
    public static Typeface custom_font;
    public static Typeface font;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    	
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        font= Typeface.createFromAsset(getAssets(),"Century.TTF");
        
        LinearLayout deviceLayout=(LinearLayout) findViewById(R.id.deviceLayout);
        LayoutParams deviceLayoutParams=deviceLayout.getLayoutParams();
        
        DisplayMetrics metrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(metrics);
           
        deviceLayoutParams.height=metrics.heightPixels;
        deviceLayoutParams.width= metrics.widthPixels;
        
        custom_font= Typeface.createFromAsset(getAssets(), "GOTHIC.TTF");
   
        deviceList= new ArrayList<ObjectDevice>();

		Log.d(TAG, "Start apki");
        ZhouseVeraServer serwer = null;
        try {
			serwer = new ZhouseVeraServer(getBaseContext());
		} catch (Exception e) {
			Log.d(TAG, "Nie udało się zainicjalizować serwera");
			e.printStackTrace();
		}
		Log.d(TAG, "urzadzenia "+serwer.GetRooms().GetRoomsCount());       
        ArrayList<ObjectDevice> allDevices = serwer.GetDevices().GetAll();
        
        for (int i = 0; i < allDevices.size(); i++) {
        	Log.d(TAG, "i: "+i);
        	ObjectDevice dev=allDevices.get(i);
        	if (dev.GetCategory() == 5 || dev.GetCategory() == 16 || dev.GetCategory() == 17 || dev.GetCategory() == 30) {	
        		deviceList.add(dev);
			}
			
		}
        
    	if (deviceList.size()==0){
    		
    		Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                public void run() {
                    finish();
                }
            }, 100);
    		
            Toast.makeText(getApplicationContext(), 
                    "No climate devices found", Toast.LENGTH_LONG).show();

    	}
        
        listView1 = (ListView)findViewById(R.id.listView1);
        adapter = new ZhouseDeviceListAdapter(this, R.layout.textview, deviceList,listView1);
    
        listView1.setAdapter(adapter);
        
        /**
         * Creating ArrayList of devices
         */    
    }
    
    public static boolean checkIfNumber(String in) {
        
        try {
            Integer.parseInt(in);
        
        } catch (NumberFormatException ex) {
            return false;
        }   
        return true;
    }
    
    
    
    
    
    
    
    
    
    
    
}