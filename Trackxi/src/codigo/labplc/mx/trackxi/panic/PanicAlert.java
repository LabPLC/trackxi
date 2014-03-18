package codigo.labplc.mx.trackxi.panic;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Vibrator;
import android.telephony.SmsManager;
import android.util.Log;

public class PanicAlert {

	Context context;
	int levelBattery = 0;
	
	public PanicAlert(Context context) {
       this.context=context;
    }

    public void activate() {
    	Vibrator v = (Vibrator) context.getSystemService(Context.VIBRATOR_SERVICE);
    	v.vibrate(3000);
    	context.registerReceiver(this.mBatInfoReceiver, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
    	
    }
    
    private BroadcastReceiver mBatInfoReceiver = new BroadcastReceiver(){
        @Override
        public void onReceive(Context ctxt, Intent intent) {
        
        	levelBattery = intent.getIntExtra(BatteryManager.EXTRA_LEVEL, 0);
        }
      };

      
     public int getLevelBattery(){
    	 return levelBattery;
     }
     
     
   //---sends an SMS message to another device---

     public void sendSMS(String phoneNumber, String message)
     {
         SmsManager sms = SmsManager.getDefault();
         sms.sendTextMessage(phoneNumber, null, message, null, null);
      }

     public void sendMail(String cabecera,String mensaje,String correoRemitente,String correoDestino ){
    	 try {   
             GMailSender sender = new GMailSender(correoRemitente, "M41k154ur10");
             sender.sendMail(cabecera, mensaje,correoRemitente, correoDestino);  
         } catch (Exception e) {   
             Log.e("SendMail", e.getMessage(), e);   
         } 
     }
}
