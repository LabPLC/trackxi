package codigo.labplc.mx.trackxi.tracking.map;

import java.util.ArrayList;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.tracking.Califica_taxi;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;

public class Mapa_tracking extends Activity {
	 private GoogleMap map;
	private double latitud=0;
	private double longitud=0;
	private MarkerOptions marker;
	private MarkerOptions marker_taxi;
	private Button mapa_tracking_terminoviaje;
	private String placa;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mapa_tracking);

		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			latitud = bundle.getDouble("latitud_inicial");	
			longitud = bundle.getDouble("longitud_inicial");


		}
		
		mapa_tracking_terminoviaje =(Button)findViewById(R.id.mapa_tracking_terminoviaje);
		mapa_tracking_terminoviaje.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent intent_califica = new Intent(Mapa_tracking.this, Califica_taxi.class);
				startActivity(intent_califica);
				finish();
			}
		});
		
		setUpMapIfNeeded();
		
	}

	
	
	 private void setUpMapIfNeeded() {
			if (map == null) {
				map = ((MapFragment) getFragmentManager().findFragmentById(R.id.mitaxi_trip_map)).getMap();
				if (map != null) {
					if(setUpMap()) {
						initMap();
					}
				}
			}
		}
		
		public void initMap() {
			map.setMyLocationEnabled(false);//quitar circulo azul;
			map.setBuildingsEnabled(true);
			map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
			map.getUiSettings().setZoomControlsEnabled(true); //ZOOM
			map.getUiSettings().setCompassEnabled(true); //COMPASS
			map.getUiSettings().setZoomGesturesEnabled(true); //GESTURES ZOOM
			map.getUiSettings().setRotateGesturesEnabled(true); //ROTATE GESTURES
			map.getUiSettings().setScrollGesturesEnabled(true); //SCROLL GESTURES
			map.getUiSettings().setTiltGesturesEnabled(true); //TILT GESTURES
			
			// create marker
			marker = new MarkerOptions().position(new LatLng(latitud, longitud)).title("Inicio del viaje");
			marker.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
			
			
			CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latitud, longitud)).zoom(21).build();
			map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
			 
			 marker_taxi = new MarkerOptions().position(new LatLng(latitud, longitud)).title("Mi posici—n");
			 marker_taxi.icon(BitmapDescriptorFactory.fromResource(R.drawable.ic_launcher_taxi));
			 
			
			// adding marker
			map.addMarker(marker);
			map.addMarker(marker_taxi);
			 
			 
			
		}
		
		public boolean setUpMap() {
			if (!checkReady()) {
	            return false;
	        } else {
	        	return true;
	        }
		}
	    
		private boolean checkReady() {
	        if (map == null) {
	          //  Log(getApplicationContext(), getString(R.string.map_not_ready), Log.INFO);
	            return false;
	        }
	        return true;
	    }

		/**
		 * manejo de transmiciones
		 */
		private BroadcastReceiver onBroadcast = new BroadcastReceiver() {

			@Override
			public void onReceive(Context ctxt, Intent t) {
				
				//String datos = t.getStringExtra("coordenadas");//obtenemos las coordenadas envidas del servicioGeolocalizaci—n
				 //tokens = datos.split(";");//separamos por tocken
				
				ArrayList<String> pointsLat =	 t.getStringArrayListExtra("latitud");
				ArrayList<String> pointsLon =	 t.getStringArrayListExtra("longitud");
				
				Double latini= Double.parseDouble(pointsLat.get(0));
				Double lonini= Double.parseDouble(pointsLon.get(0));
				Double latfin= Double.parseDouble(pointsLat.get(pointsLat.size()-1));
				Double lonfin= Double.parseDouble(pointsLon.get(pointsLon.size()-1));
				
				
				map.clear();
				marker.position(new LatLng(latini,lonini));
				map.addMarker(marker);
				
				marker_taxi.position(new LatLng(latfin,lonfin));
				map.addMarker(marker_taxi);
				
				CameraPosition cameraPosition = new CameraPosition.Builder().target(new LatLng(latfin, lonfin)).zoom(21).build();
				map.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
				
				

				 for (int i = 0; i < pointsLat.size() - 1; i++) {
					 LatLng src = new LatLng(Double.parseDouble(pointsLat.get(i)),Double.parseDouble(pointsLon.get(i)));
					 LatLng dest = new LatLng(Double.parseDouble(pointsLat.get(i+1)),Double.parseDouble(pointsLon.get(i+1)));
					 Polyline line = map.addPolyline(new PolylineOptions() //mMap is the Map Object
					 .add(new LatLng(src.latitude, src.longitude),
					 new LatLng(dest.latitude,dest.longitude))
					 .width(8).color(Color.BLUE).geodesic(true));
				  }
				//new Dialogos().Toast(getBaseContext(), "zazazazazazaa "+datosLat[i]+", "+tokens[1], Toast.LENGTH_LONG);
			}
		};
		
		@Override
		protected void onPause() {
			unregisterReceiver(onBroadcast);
			super.onPause();
		}

		@Override
		protected void onResume() {
			registerReceiver(onBroadcast, new IntentFilter("key"));
			super.onResume();
		}
		
}
