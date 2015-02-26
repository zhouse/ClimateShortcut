package org.zhouse.ClimateShortcut;

import java.util.ArrayList;

import org.zhouse.libs.ListViewHelper;
import org.zhouse.libs.ObjectDevice;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.AdapterView.OnItemClickListener;

public class ZhouseDeviceListAdapter extends ArrayAdapter<ObjectDevice> {
	static final String TAG="ZhouseDeviceList";
	
	public ArrayList<ListViewHelper> helpersList;
	public ArrayList<ObjectDevice> deviceList;
	

	Context mainContext;
    
    public ZhouseDeviceListAdapter(Context context, int textViewResourceId, ArrayList<ObjectDevice> list, ListView lista) {
    	super(context, textViewResourceId);
    	mainContext=context;
    	
    	/**
    	 * Convert ArrayList<ObjectDevice> to ArrayList<ListViewHelper>
    	 */
    	helpersList = new ArrayList<ListViewHelper>();
    	for (int i = 0; i < list.size(); i++) {
    		ListViewHelper myHelper= new ListViewHelper(list.get(i).GetID(), list.get(i).GetCategory(), list.get(i).GetRoom(), list.get(i).GetName());
    		myHelper.SetVariables(list.get(i).GetVariables());
    		helpersList.add(myHelper);
		}
        lista.setCacheColorHint(0);
        lista.setOnItemClickListener(deviceClick);
    	
    }

//*******************************
//
//      GET VIEW FUNCTIONS
//
//*******************************  
    
    public int getViewTypeCount() {
        return 15;
    }
    
    public int getItemViewType(int position) {		
		return helpersList.get(position).GetViewType();
    }
    
	@Override
	public int getCount() {
		return helpersList.size();
	}


    
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;
		int deviceImage;
		holder=new ViewHolder();
		String[] values;
		String device_value="";
		int category_number=0;
		String device_name="";
		
		if (helpersList.get(position).GetCategory() == 30) {
			deviceImage = R.drawable.danfoss_icon_active;
		} else if(helpersList.get(position).GetCategory() == 16){
			deviceImage = R.drawable.humi_sensor_active;			
		} else if(helpersList.get(position).GetCategory() == 17){
			deviceImage = R.drawable.temp_sensor_active;
		} else {
			deviceImage = R.drawable.thermostat_icon_active;
		}
		
		category_number=helpersList.get(position).GetCategory();
		device_name=helpersList.get(position).GetName();
		
    	
		switch (getItemViewType(position)){
		case 0:			
			
		    if (convertView == null || convertView.getTag()==null) {
				convertView = View.inflate(mainContext, R.layout.child_row, null);
				holder.devicePositionUnits = (TextView)convertView.findViewById(R.id.textStateUnits);
				holder.devicePosition=(TextView)convertView.findViewById(R.id.textState);
				holder.deviceImagePosition=(ImageView)convertView.findViewById(R.id.imageState);
				holder.devicePosition.setTypeface(MainActivity.custom_font);
				holder.deviceName =(TextView)convertView.findViewById(R.id.grp_child);
				holder.deviceName.setTypeface(MainActivity.custom_font);
				
				convertView.setTag(holder);
		    } else {
		        holder = (ViewHolder) convertView.getTag();
		    }
		    
		    holder.deviceName.setCompoundDrawablesWithIntrinsicBounds(mainContext.getResources().getDrawable(deviceImage), null, null, null);
		    holder.deviceName.setText(device_name);
		    holder.deviceName.setTypeface(MainActivity.custom_font);
	
		    int stateImage=0;
		    String stateString="";
		    String stateStringUnits="";
		    String value=device_value;
			value=value.replaceAll("\\s","");
    
		    switch (category_number){
				case 16:		if (value!="") {stateString=value; stateStringUnits="%";}  break;
				case 5:
				case 30:
				case 17: 	if (value!="") stateString=value+"°"; break;
		    }
		    		    
		    holder.devicePositionUnits.setVisibility(View.VISIBLE);
		    holder.devicePosition.setVisibility(View.VISIBLE);
		    holder.deviceImagePosition.setVisibility(View.VISIBLE);

		    if (stateString!="") {
		    	holder.devicePosition.setText(stateString); 
		    	holder.devicePosition.setVisibility(View.VISIBLE);
		    	} else holder.devicePosition.setVisibility(View.GONE);
		    
		    if (stateStringUnits!="") {
		    	holder.devicePositionUnits.setText(stateStringUnits);
		    	holder.devicePositionUnits.setVisibility(View.VISIBLE);
		    	} else holder.devicePositionUnits.setVisibility(View.GONE);

		    if (stateImage!=0) {
		    	holder.deviceImagePosition.setImageDrawable(mainContext.getResources().getDrawable(stateImage)); 
		    	holder.deviceImagePosition.setVisibility(View.VISIBLE);
			} else holder.deviceImagePosition.setVisibility(View.GONE);

		    break;
		
		
			
		
		case 5: //Thermostat
		    if (convertView == null || convertView.getTag()==null) {
				convertView = View.inflate(mainContext, R.layout.child_row_thermostat, null);
				holder.devicePosition=(TextView)convertView.findViewById(R.id.textState);
				holder.astronomicSet=(TextView)convertView.findViewById(R.id.statePosition);
				holder.weatherDescription=(TextView)convertView.findViewById(R.id.stateDescription);
				holder.deviceName =(TextView)convertView.findViewById(R.id.grp_child);

				holder.devicePosition.setTypeface(MainActivity.custom_font);
				holder.deviceName.setTypeface(MainActivity.custom_font);
				holder.weatherDescription.setTypeface(MainActivity.custom_font);
				holder.astronomicSet.setTypeface(MainActivity.custom_font);
				convertView.setTag(holder);
		    } else {
		        holder = (ViewHolder) convertView.getTag();
		    }
		    
	        String[] stateValues=device_value.split(",");
	        Log.d(TAG,device_value);
	           if (stateValues.length==6){
//	        	   holder.devicePosition.setText(stateValues[0]+"°"+ConnectionService.veraServer.GetTemperatureUnit());
//	        	   holder.astronomicSet.setText(stateValues[3]+"°\n"+stateValues[4]+"°\n"+stateValues[1]+"\n"+stateValues[2]+"\n"+stateValues[5]);
	  	         }
		
		    holder.deviceName.setText(device_name);

		    break;
		    
		

		
			}
        return convertView;
    }
    
    
    static class ViewHolder{
       	@Override
		public String toString() {
			return "aaa "+deviceName.getText();       		
		}
		ImageView deviceImage;
       	TextView deviceName;
       	TextView devicePositionUnits;
       	TextView devicePosition;
       	TextView weatherDescription;
       	ImageView deviceImagePosition;
       	TextView astronomicSet;
       	TextView astronomicRise;
       	TextView astronomicSunSet;
       	TextView astronomicSunRise;
       	TextView astronomicMoonSet;
       	TextView astronomicMoonRise;    	
       	TextView chronosUp;
       	TextView chronosHash;
       	TextView chronosStar;
    }
        

//*******************************
//
//      ON CLICK ROUTINES
//
//*******************************      

    private OnItemClickListener deviceClick = new OnItemClickListener(){

		public void onItemClick(AdapterView<?> arg0, View arg1, int arg2,
				long arg3) {
			Log.d(TAG, "clicked: "+helpersList.get(arg2).GetName()+" cat: "+helpersList.get(arg2).GetCategory());

			Intent intent=new Intent();
			intent.putExtra("ITEM_NAME", helpersList.get(arg2).GetName());   		
			intent.putExtra("ITEM_DEVICEID", helpersList.get(arg2).GetId());
			intent.putExtra("ITEM_MIOSDEVICEID", helpersList.get(arg2).GetId());
			String value;

			
			if (helpersList.get(arg2).GetCategory()==30){
				value=helpersList.get(arg2).GetVariable("heatsp");
				intent.putExtra("ITEM_VALUE", value);
				intent.setClass(mainContext, WindowDanfoss.class);
//				intent.setClass(mainContext, WindowThermostat.class);
		   		((Activity) mainContext).startActivityForResult(intent, 33);			
			} else 	if (helpersList.get(arg2).GetCategory()==5){
				value=helpersList.get(arg2).GetVariable("heatsp");
				value=helpersList.get(arg2).GetVariable("temperature")+","+helpersList.get(arg2).GetVariable("mode")+","
	    				   +helpersList.get(arg2).GetVariable("fanmode")+","+helpersList.get(arg2).GetVariable("heatsp")+","
	    				   +helpersList.get(arg2).GetVariable("coolsp")+","+helpersList.get(arg2).GetVariable("hvacstate");
				intent.putExtra("ITEM_VALUE", value);
//				intent.setClass(mainContext, WindowDanfoss.class);
				intent.setClass(mainContext, WindowThermostat.class);
		   		((Activity) mainContext).startActivityForResult(intent, 33);			
			} else {
//				value=helpersList.get(arg2).GetVariable("status");
//	        	for(int i=0;i<MainActivity.deviceList.size();i++){
//	        		if(MainActivity.deviceList.get(i).GetID()==helpersList.get(arg2).GetId()) MainActivity.deviceList.get(i).ChangeVariable("status", value);
//	        	}				
			}
		}
    };


}
