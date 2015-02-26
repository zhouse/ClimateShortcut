package org.zhouse.ClimateShortcut;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.view.View;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;


public class WindowDanfoss extends Activity {
    /** Called when the activity is first created. */
	static final String TAG="ZhouseWindowDanfoss";	
	int tempLevel=0;
	TextView templvltxt;
	
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
        templvltxt = (TextView)findViewById(R.id.temp_lvl_txt);

        //Setting typefaces        
        item_name.setTypeface(MainActivity.custom_font);
        templvltxt.setTypeface(MainActivity.custom_font);
                
        //Getting and setting values
        Intent myIntent = getIntent();
        
        item_name.setText(myIntent.getStringExtra("ITEM_NAME"));
                
        String inValue=myIntent.getStringExtra("ITEM_VALUE");
        if (checkIfNumber(inValue)) {
        	tempLevel=Integer.parseInt(inValue);
    //        templvltxt.setText(myIntent.getStringExtra("ITEM_VALUE")+"°"+ConnectionService.veraServer.GetTemperatureUnit());
        } else {
        	tempLevel=0;
  //          templvltxt.setText("0°"+ConnectionService.veraServer.GetTemperatureUnit());        	
        }        
        
        //on click listeners
        Button buttonPlus = (Button)findViewById(R.id.danfoss_plus);
        Button buttonMinus = (Button)findViewById(R.id.danfoss_minus);
        Button buttonSet = (Button)findViewById(R.id.danfoss_set);
        buttonPlus.setOnClickListener(onDanfossBtnClick);
        buttonMinus.setOnClickListener(onDanfossBtnClick);
        buttonSet.setOnClickListener(onDanfossBtnClick);
    	}
	
    private OnClickListener onDanfossBtnClick = new OnClickListener(){
        public void onClick(View v){
            if (v.getId() == R.id.danfoss_plus) {
				if (tempLevel<100) tempLevel++;
//				templvltxt.setText(Integer.toString(tempLevel)+"°"+ConnectionService.veraServer.GetTemperatureUnit());
			} else if (v.getId() == R.id.danfoss_minus) {
				if (tempLevel>4) tempLevel--;
	//			templvltxt.setText(Integer.toString(tempLevel)+"°"+ConnectionService.veraServer.GetTemperatureUnit());
			} else if (v.getId() == R.id.danfoss_set) {
				Intent intent = getIntent();
				int DeviceId= intent.getIntExtra("ITEM_MIOSDEVICEID", 0);
//				try {
//					VeramenagerActivity.mService.veraSend("danfossSet:"+Integer.toString(tempLevel)+":"+DeviceId);
//				} catch (RemoteException e) {
//					e.printStackTrace();
//				}
				finish();
			}
        }
    };    

    public static boolean checkIfNumber(String in) {
        
        try {
            Integer.parseInt(in);
        
        } catch (NumberFormatException ex) {
            return false;
        }   
        return true;
    }
}