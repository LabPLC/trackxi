package codigo.labplc.mx.trackxi;

import java.util.Timer;
import java.util.TimerTask;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.Window;
import codigo.labplc.mx.trackxi.paginador.Paginador;
import codigo.labplc.mx.trackxi.registro.MitaxiRegisterManuallyActivity;

public class Trackxi extends Activity {

	 private static final long SPLASH_SCREEN_DELAY = 1000;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//solicitamos las preferencias del usuario para saber si esta registrado
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
		String uuid = prefs.getString("uuid", null);
		//si aun no tiene datos guardados en preferencias lo registramos
		if(uuid == null){
			iniciarSplash(2);
		}else{ //si ya se registro de muestra splash y luego la activity para buscar placas
			iniciarSplash(1);
		}
	}
	


	/**
	 * Muestra la pantalla splash e inicia la actividad principal
	 * 
	 */
	public void iniciarSplash(final int flag){
		 setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
	      requestWindowFeature(Window.FEATURE_NO_TITLE);
	        setContentView(R.layout.splash_screen_trackxi);
	        TimerTask task = new TimerTask() {
	            @Override
	            public void run() {
	                //Inicia la actividad de revisado de placas
	            	if(flag==1){
	            		Intent mainIntent = new Intent().setClass(Trackxi.this, Paginador.class);
	            		 startActivity(mainIntent);
	            	}else if(flag==2){
	    				Intent intentManually = new Intent(Trackxi.this, MitaxiRegisterManuallyActivity.class);
	    				startActivity(intentManually);
	    				overridePendingTransition(0,0);
	    				
	            	}
	                finish();
	            }
	        };
	        
	        Timer timer = new Timer();
	        timer.schedule(task, SPLASH_SCREEN_DELAY);
	    }
	
	
}
