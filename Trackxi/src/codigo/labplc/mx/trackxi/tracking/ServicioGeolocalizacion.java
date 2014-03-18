package codigo.labplc.mx.trackxi.tracking;

import java.util.ArrayList;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.os.ResultReceiver;
import android.util.Log;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.buscarplaca.paginador.DatosAuto;
import codigo.labplc.mx.trackxi.panic.MyReceiver;
import codigo.labplc.mx.trackxi.panic.PanicAlert;
import codigo.labplc.mx.trackxi.tracking.map.Mapa_tracking;

/**
 * 
 * @author mikesaurio
 * 
 */
public class ServicioGeolocalizacion extends Service implements Runnable {
	/**
	 * Declaración de variables
	 */
	public static DatosAuto taxiActivity;
	private LocationManager mLocationManager;
	private MyLocationListener mLocationListener;
	private double latitud_inicial = 19.0f;
	private double longitud_inicial = -99.0f;
	private double latitud;
	private double longitud;
	private Location currentLocation = null;
	private boolean isFirstLocation = true;
	private Thread thread;
	ArrayList<String> pointsLat = new ArrayList<String>();
	ArrayList<String> pointsLon = new ArrayList<String>();
	private boolean isFirstTime = true;

	// panic
	private BroadcastReceiver mReceiver;
	private ResultReceiver resultReceiver;
	private static int countStart = -1;
	private Handler handler_time = new Handler();
	private Handler handler_panic = new Handler();
	private String uuid;
	private String telemer;
	private String correoemer;
	private String placa;
	PanicAlert panic;
	public static boolean countTimer = true;
	public boolean isSendMesagge= false;

	@Override
	public void onCreate() {
		Toast.makeText(this, "Servicio creado", Toast.LENGTH_SHORT).show();
		super.onCreate();
		mLocationListener = new MyLocationListener();
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

		// para le panic
		IntentFilter filter = new IntentFilter(Intent.ACTION_SCREEN_ON);
		filter.addAction(Intent.ACTION_SCREEN_OFF);
		mReceiver = new MyReceiver();
		registerReceiver(mReceiver, filter);
	}

/*	@Override
	public int onStartCommand(Intent intent, int flags, int idArranque) {
		// Toast.makeText(this,"Servicio arrancado "+
		// idArranque,Toast.LENGTH_SHORT).show();
		obtenerSenalGPS();

		return START_STICKY;
	}
*/
	@Override
	public void onStart(Intent intent, int startId) {
		//PAnic
		if(isFirstTime){
			obtenerSenalGPS();
			isFirstTime=false;
		}
			try{
					resultReceiver = intent.getParcelableExtra("receiver");
	
					// revisamos si la pantalla esta prendida o apagada y contamos el numero de click al boton de apagado
					boolean screenOn = intent.getBooleanExtra("screen_state", false);
					// si damos más de 4 click al boton de apagado se activa la alarma
					if (countStart >= 4) {
						Log.i("*********", "mas de 4");
						countStart = -1;
						countTimer = true;
						// activamos el mensaje de auxilio
						panic = new PanicAlert(this.getApplicationContext());
						panic.activate();
						
						SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
					           uuid = prefs.getString("uuid", null);
					           telemer = prefs.getString("telemer", null);
					           correoemer = prefs.getString("correoemer", null);
					           placa = prefs.getString("placa", null);
						
					    String mensajeEmer= "Estoy en peligro en taxi placas: "+placa+ " mi ubicacion es "+latitud+","+longitud+" batería: "+ panic.getLevelBattery()+" revisa tu correo";
						panic.sendSMS(telemer,mensajeEmer);
						isSendMesagge=true;
	
					} else {
						countStart += 1;
						Log.i("*********", "else " + countStart + "");
						// incrementamos los click en 1
	
						// contamos 10 segundos si no reiniciamos los contadores
						if (countTimer) {
							countTimer = false;
							handler_time.postDelayed(runnable, 10000);// 10 segundos de espera
						}
					}
			}catch(Exception e){
				Log.d("*****onStart", "vino null");
			}
				
		super.onStart(intent, startId);
	}

	@Override
	public void onDestroy() {
		if (mLocationManager != null)
			if (mLocationListener != null)
				mLocationManager.removeUpdates(mLocationListener);

		Toast.makeText(this, "Servicio detenido ", Toast.LENGTH_SHORT).show();
		super.onDestroy();
		CancelNotification(this, 0);

		// panic
		unregisterReceiver(mReceiver);
	}

	@Override
	public IBinder onBind(Intent intencion) {
		return null;
	}

	/**
	 * handler
	 */
	private Handler handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			// mLocationManager.removeUpdates(mLocationListener);
			updateLocation(currentLocation);
		}
	};

	/**
	 * metodo para actualizar la localización
	 * 
	 * @param currentLocation
	 * @return void
	 */
	public void updateLocation(Location currentLocation) {
		if (currentLocation != null) {
			latitud = Double.parseDouble(currentLocation.getLatitude() + "");
			longitud = Double.parseDouble(currentLocation.getLongitude() + "");

			if (isFirstLocation) {
				latitud_inicial = latitud;
				longitud_inicial = longitud;
				isFirstLocation = false;
				showNotification();
			}

			pointsLat.add(latitud + "");
			pointsLon.add(longitud + "");

			Log.i("******", latitud + "," + longitud);
			
			Intent intent = new Intent("key");
			intent.putExtra("latitud", pointsLat);
			intent.putExtra("longitud", pointsLon);
			getApplicationContext().sendBroadcast(intent);

			if(isSendMesagge){
				handler_panic.postDelayed(runnable_panic,300000);// 1
				isSendMesagge=false;
			}
			
		}
	}

	public boolean yaPasaronMinutos(){
		
		
		return false;
	}
	/**
	 * Hilo de la aplicacion para cargar las cordenadas del usuario
	 */
	public void run() {
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Looper.prepare();
			mLocationManager.requestLocationUpdates(
					LocationManager.GPS_PROVIDER, 4000, 0, mLocationListener);
			Looper.loop();
			Looper.myLooper().quit();
		} else {
			taxiActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"GPS apagado inesperadamente", Toast.LENGTH_LONG)
							.show();
				}
			});
		}
	}

	/**
	 * Metodo para Obtener la señal del GPS
	 */
	private void obtenerSenalGPS() {
		thread = new Thread(this);
		thread.start();
	}

	/**
	 * Metodo para asignar las cordenadas del usuario
	 * */
	private void setCurrentLocation(Location loc) {
		currentLocation = loc;
	}

	/**
	 * Metodo para obtener las cordenadas del GPS
	 */
	private class MyLocationListener implements LocationListener {

		public void onLocationChanged(Location loc) {
			// Log.d("finura",loc.getAccuracy()+"");
			if (loc != null) {
				setCurrentLocation(loc);
				handler.sendEmptyMessage(0);
			}
		}

		/**
		 * metodo que revisa si el GPS esta apagado
		 */
		public void onProviderDisabled(String provider) {
			taxiActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(),
							"GPS apagado inesperadamente", Toast.LENGTH_LONG)
							.show();
				}
			});
		}

		// @Override
		public void onProviderEnabled(String provider) {
		}

		// @Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}

	public void showNotification() {
		// notification is selected
		Intent intent_mapa = new Intent(this, Mapa_tracking.class);
		intent_mapa.putExtra("latitud_inicial", latitud_inicial);
		intent_mapa.putExtra("longitud_inicial", longitud_inicial);
		PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent_mapa,
				PendingIntent.FLAG_UPDATE_CURRENT);

		Intent intent_califica = new Intent(this, Califica_taxi.class);
		PendingIntent pIntent_cal = PendingIntent.getActivity(this, 0,
				intent_califica, PendingIntent.FLAG_UPDATE_CURRENT);

		Notification noti = new Notification.Builder(this)
				.setContentTitle("Traxi").setContentText("¿Qué quieres hacer?")
				.setSmallIcon(R.drawable.ic_launcher_icono)
				// .setContentIntent(pIntent)
				.addAction(R.drawable.ic_launcher_map, "viaje", pIntent)
				.addAction(R.drawable.ic_launcher_fin_viaje, "Finalizar",
						pIntent_cal).build();
		NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		// noti.flags |= Notification.FLAG_AUTO_CANCEL;

		notificationManager.notify(0, noti);
	}

	public static void CancelNotification(Context ctx, int notifyId) {
		String ns = Context.NOTIFICATION_SERVICE;
		NotificationManager nMgr = (NotificationManager) ctx
				.getSystemService(ns);
		nMgr.cancel(notifyId);
	}

	// panic
	 /**
     * hilo que al pasar el tiempo reeinicia los valores
     */
    private Runnable runnable = new Runnable() {
        @Override
        public void run() {
            //reiniciamos los contadores
            countStart = -1;
            countTimer = true;
        }
    };

    
 // panic
 	 /**
      * hilo que al pasar el tiempo reeinicia los valores
      */
     private Runnable runnable_panic = new Runnable() {
         @Override
         public void run() {
 		
				panic.sendMail("TRAXI",
						"Estoy en peligro en taxi placas: "+placa+ " mi ubicacion es "+latitud+","+longitud+" batería: "+ panic.getLevelBattery(),
						"mikesaurio@gmail.com", 
						correoemer);
			
         }
     };

	

	
}