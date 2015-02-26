package org.zhouse.ClimateShortcut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

public class WindowThermostat extends Activity {
    /** Called when the activity is first created. */
	static final String TAG="ZhouseWindowThermostat";	
	private int heatLevel=0;
	private int coolLevel=0;	
	private TextView heatLvlTxt;
	private TextView coolLvlTxt;
	private String fanMode="";
	private String mode="";
	private String initFanMode;
	private String initMode;
	private int initHeatTemp;
	private int initCoolTemp;
	private String DeviceId;
	
	@Override
    public void onCreate(Bundle savedInstanceState) 
    	{
        super.onCreate(savedInstanceState);
        
		WindowManager.LayoutParams lp = getWindow().getAttributes();  
		lp.dimAmount =0.5f;
		getWindow().setAttributes(lp);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND,WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		        
        setContentView(R.layout.window_danfoss);
        
        //Finding Views
        TextView item_name = (TextView)findViewById(R.id.textView1);
        TextView heat_txt = (TextView)findViewById(R.id.heat_txt);
        TextView cool_txt = (TextView)findViewById(R.id.cool_txt);
        TextView fanmode_txt = (TextView)findViewById(R.id.fanmode_txt);
        TextView mode_txt = (TextView)findViewById(R.id.mode_txt);
        TextView temperature_txt = (TextView)findViewById(R.id.thermostat_temperature);
        ToggleButton ecoMode = (ToggleButton)findViewById(R.id.ecoMode);
        RadioButton radioFanOn = (RadioButton)findViewById(R.id.radio_fan_on);
        RadioButton radioFanCycle = (RadioButton)findViewById(R.id.radio_fan_cycle);
        RadioButton radioFanAuto = (RadioButton)findViewById(R.id.radio_fan_auto);
        RadioButton radioModeAuto = (RadioButton)findViewById(R.id.radio_mode_auto);
        RadioButton radioModeHeat = (RadioButton)findViewById(R.id.radio_mode_heat);
        RadioButton radioModeCool = (RadioButton)findViewById(R.id.radio_mode_cool);
        RadioButton radioModeOff = (RadioButton)findViewById(R.id.radio_mode_off);
        Button buttonHeatPlus = (Button)findViewById(R.id.thermostat_heat_plus);
        Button buttonHeatMinus = (Button)findViewById(R.id.thermostat_heat_minus);
        Button buttonCoolPlus = (Button)findViewById(R.id.thermostat_cool_plus);
        Button buttonCoolMinus = (Button)findViewById(R.id.thermostat_cool_minus);
        Button buttonSet = (Button)findViewById(R.id.danfoss_set);

        heatLvlTxt = (TextView)findViewById(R.id.thermostat_heat_txt);
        coolLvlTxt = (TextView)findViewById(R.id.thermostat_cool_txt);
        
        //Setting typefaces        
        item_name.setTypeface(MainActivity.custom_font);
        heatLvlTxt.setTypeface(MainActivity.custom_font);
        coolLvlTxt.setTypeface(MainActivity.custom_font);
        heat_txt.setTypeface(MainActivity.custom_font);
        cool_txt.setTypeface(MainActivity.custom_font);
        temperature_txt.setTypeface(MainActivity.custom_font);
        ecoMode.setTypeface(MainActivity.custom_font);
        fanmode_txt.setTypeface(MainActivity.custom_font);
        mode_txt.setTypeface(MainActivity.custom_font);
        radioFanOn.setTypeface(MainActivity.custom_font);
        radioFanCycle.setTypeface(MainActivity.custom_font);
        radioFanAuto.setTypeface(MainActivity.custom_font);
        radioModeAuto.setTypeface(MainActivity.custom_font);
        radioModeHeat.setTypeface(MainActivity.custom_font);
        radioModeCool.setTypeface(MainActivity.custom_font);
        radioModeOff.setTypeface(MainActivity.custom_font);
                
        //Getting and setting values
        Intent myIntent = getIntent();
        item_name.setText(myIntent.getStringExtra("ITEM_NAME"));
        
    	//String[] serch={, DEVICE_HVACMODE, DEVICE_FANMODE, , , DEVICE_HVACSTATE};

        String[] values=myIntent.getStringExtra("ITEM_VALUE").split(",");
           if (values.length==6){
//        	   temperature_txt.setText(values[0]+"°"+ConnectionService.veraServer.GetTemperatureUnit());
        	   if (WindowDanfoss.checkIfNumber(values[3])) heatLevel=Integer.parseInt(values[3]);
        	   if (WindowDanfoss.checkIfNumber(values[4])) coolLevel=Integer.parseInt(values[4]);
        	   initHeatTemp=heatLevel;
       		   initCoolTemp=coolLevel;
        	   initMode=values[1];
        	   initFanMode=values[2];
        	   
        	   heatLvlTxt.setText(values[3]+"°");
        	   coolLvlTxt.setText(values[4]+"°");

        	   if (values[1].contains("Off")) radioModeOff.setChecked(true);
        	   else if (values[1].contains("AutoChangeOver")) radioModeAuto.setChecked(true);
        	   else if (values[1].contains("CoolOn")) radioModeCool.setChecked(true);
        	   else if (values[1].contains("HeatOn")) radioModeHeat.setChecked(true);
        	   
        	   if (values[2].contains("Auto")) radioFanAuto.setChecked(true);
        	   else if (values[2].contains("ContinuousOn")) radioFanOn.setChecked(true);
        	   else if (values[2].contains("PeriodicOn")) radioFanCycle.setChecked(true);
  	         }
        
           
        //on click listeners
        buttonCoolPlus.setOnClickListener(onBtnClick);
        buttonCoolMinus.setOnClickListener(onBtnClick);
        buttonHeatPlus.setOnClickListener(onBtnClick);
        buttonHeatMinus.setOnClickListener(onBtnClick);
        buttonSet.setOnClickListener(onBtnClick);
        ecoMode.setOnClickListener(onBtnClick);
        radioFanOn.setOnClickListener(onBtnClick);
        radioFanCycle.setOnClickListener(onBtnClick);
        radioFanAuto.setOnClickListener(onBtnClick);
        radioModeAuto.setOnClickListener(onBtnClick);
        radioModeHeat.setOnClickListener(onBtnClick);
        radioModeCool.setOnClickListener(onBtnClick);
        radioModeOff.setOnClickListener(onBtnClick);

        DeviceId=Integer.toString(myIntent.getIntExtra("ITEM_MIOSDEVICEID",0));
    	}
	
    private OnClickListener onBtnClick = new OnClickListener(){
        public void onClick(View v){
            if (v.getId() == R.id.thermostat_heat_plus) {
				if (heatLevel<99) heatLevel++;
				heatLvlTxt.setText(Integer.toString(heatLevel)+"°");
			} else if (v.getId() == R.id.thermostat_heat_minus) {
				if (heatLevel>4) heatLevel--;
				heatLvlTxt.setText(Integer.toString(heatLevel)+"°");
			} else if (v.getId() == R.id.thermostat_cool_plus) {
				if (coolLevel<99) coolLevel++;
				coolLvlTxt.setText(Integer.toString(coolLevel)+"°");
			} else if (v.getId() == R.id.thermostat_cool_minus) {
				if (coolLevel>4) coolLevel--;
				coolLvlTxt.setText(Integer.toString(coolLevel)+"°");
			} else if (v.getId() == R.id.radio_fan_on) {
				fanMode="ContinuousOn";
			} else if (v.getId() == R.id.radio_fan_cycle) {
				fanMode="PeriodicOn";
			} else if (v.getId() == R.id.radio_fan_auto) {
				fanMode="Auto";
			} else if (v.getId() == R.id.radio_mode_auto) {
				mode="AutoChangeOver";
			} else if (v.getId() == R.id.radio_mode_heat) {
				mode="HeatOn";
			} else if (v.getId() == R.id.radio_mode_cool) {
				mode="CoolOn";
			} else if (v.getId() == R.id.radio_mode_off) {
				mode="Off";
			} else if (v.getId() == R.id.ecoMode) {
//				try {
//				if (((ToggleButton)v).isChecked()) VeramenagerActivity.mService.veraSend("thermostatSetEcoMode:EnergySavingsMode:"+DeviceId);
//					else VeramenagerActivity.mService.veraSend("thermostatSetEcoMode:Normal:"+DeviceId);

//				} catch (RemoteException e) {
					// TODO Auto-generated catch block
//					e.printStackTrace();
//				}
				finish();
			} else if (v.getId() == R.id.danfoss_set) {
				ArrayList<String> requests=new ArrayList<String>();
				if (initHeatTemp!=heatLevel) requests.add("thermostatSetHeatPoint:"+Integer.toString(heatLevel)+":"+DeviceId);
				if (initCoolTemp!=coolLevel) requests.add("thermostatSetCoolPoint:"+Integer.toString(coolLevel)+":"+DeviceId);
				if (!initMode.contains(mode)) requests.add("thermostatSetMode:"+mode+":"+DeviceId);
				if (!initFanMode.contains(fanMode)) requests.add("thermostatSetFanMode:"+fanMode+":"+DeviceId);
				if (requests.size()>0){
//					try {
//						VeramenagerActivity.mService.veraSendMulti(requests.toArray(new String[requests.size()]));
//					} catch (RemoteException e) {
//						// TODO Auto-generated catch block
//						e.printStackTrace();
//					}
				}
				finish();
			}
        }
    };
    
}