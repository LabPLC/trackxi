package codigo.labplc.mx.trackxi.tracking;

import java.util.ArrayList;

import com.google.android.gms.maps.model.LatLng;

import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.Trackxi;
import codigo.labplc.mx.trackxi.buscarplaca.paginador.DatosAuto;
import codigo.labplc.mx.trackxi.tracking.map.Mapa_tracking;

/**
 * 
 * @author mikesaurio
 *
 */
public class ServicioGeolocalizacion extends Service implements Runnable{
	/**
	 * Declaración de variables
	 */
	public static DatosAuto taxiActivity;
	private LocationManager mLocationManager;
	private MyLocationListener mLocationListener;
	private double latitud_inicial=19.0f;
	private double longitud_inicial=-99.0f;
	private double  latitud;
	private double  longitud;
	private Location currentLocation = null;
	private boolean isFirstLocation= true;
	private Thread thread;
	ArrayList<String> pointsLat = new ArrayList<String>();
	ArrayList<String> pointsLon = new ArrayList<String>();;

    @Override
    public void onCreate() {
          Toast.makeText(this,"Servicio creado", Toast.LENGTH_SHORT).show();
          super.onCreate();
 
      	mLocationListener = new MyLocationListener();
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    }

    @Override
    public int onStartCommand(Intent intenc, int flags, int idArranque) {
         // Toast.makeText(this,"Servicio arrancado "+ idArranque,Toast.LENGTH_SHORT).show();  
          obtenerSenalGPS();
          return START_STICKY;
    }


	@Override
    public void onDestroy() {
		if (mLocationManager != null)
			if (mLocationListener != null)
				mLocationManager.removeUpdates(mLocationListener);
		
        Toast.makeText(this,"Servicio detenido ",Toast.LENGTH_SHORT).show();
    	    super.onDestroy();
       
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

			if(isFirstLocation){
				latitud_inicial=latitud;
				longitud_inicial=longitud;
				isFirstLocation=false;
				Log.d("***INICIAL***", latitud_inicial+","+longitud_inicial);
		     	showNotification();
			}
			
			pointsLat.add(latitud+"");
			pointsLon.add(longitud+"");

			
			 Intent intent = new Intent("key");
			 intent.putExtra("latitud", pointsLat);
			 intent.putExtra("longitud", pointsLon);
			 getApplicationContext().sendBroadcast(intent);

		}
	}

	
	/**
	 * Hilo de la aplicacion para cargar las cordenadas del usuario
	 */
	public void run() {
		if (mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
			Looper.prepare();
			mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 4000, 0, mLocationListener);
			Looper.loop();
			Looper.myLooper().quit();
		} else {
			taxiActivity.runOnUiThread(new Runnable() {
				@Override
				public void run() {
					Toast.makeText(getApplicationContext(), "GPS apagado inesperadamente", Toast.LENGTH_LONG).show();			
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
			//Log.d("finura",loc.getAccuracy()+"");
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
					Toast.makeText(getApplicationContext(), "GPS apagado inesperadamente", Toast.LENGTH_LONG).show();			
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
	
	
	
	 public void showNotification(){
		    // notification is selected
		    Intent intent_mapa = new Intent(this, Mapa_tracking.class);
		    intent_mapa.putExtra("latitud_inicial", latitud_inicial);
		    intent_mapa.putExtra("longitud_inicial", longitud_inicial);
		    
		    PendingIntent pIntent = PendingIntent.getActivity(this, 0, intent_mapa, PendingIntent.FLAG_UPDATE_CURRENT);

		    // Actions are just fake
		    Notification noti = new Notification.Builder(this)
		        .setContentTitle("Traxi")
		        .setContentText("¿Qué quieres hacer?").setSmallIcon(R.drawable.ic_launcher_icono)
		     //   .setContentIntent(pIntent)
		        .addAction(R.drawable.ic_launcher_map, "viaje", pIntent)
		        .addAction(R.drawable.ic_launcher_verde, "otro", pIntent).build();
		    NotificationManager notificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
		    // hide the notification after its selected
		  //  noti.flags |= Notification.FLAG_AUTO_CANCEL;

		    notificationManager.notify(0, noti);
	    }


}