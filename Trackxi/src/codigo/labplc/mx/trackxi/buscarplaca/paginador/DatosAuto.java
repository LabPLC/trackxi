package codigo.labplc.mx.trackxi.buscarplaca.paginador;

import java.util.ArrayList;
import java.util.Calendar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.Trackxi;
import codigo.labplc.mx.trackxi.buscarplaca.bean.AutoBean;
import codigo.labplc.mx.trackxi.buscarplaca.bean.ComentarioBean;
import codigo.labplc.mx.trackxi.configuracion.UserSettingActivity;
import codigo.labplc.mx.trackxi.dialogos.Dialogos;
import codigo.labplc.mx.trackxi.network.NetworkUtils;
import codigo.labplc.mx.trackxi.paginador.Paginador;
import codigo.labplc.mx.trackxi.tracking.ServicioGeolocalizacion;

import com.viewpagerindicator.TitlePageIndicator;

public class DatosAuto extends FragmentActivity{
	
	private int PUNTOS=0;
	private int PUNTOS_APP =100;
	private int PUNTOS_USUARIO =0;
	private int PUNTOS_REVISTA = 50;
	private int PUNTOS_INFRACCIONES = 30;
	private int PUNTOS_TENENCIA = 5;
	private int PUNTOS_VERIFICACION = 5;
	private int PUNTOS_ANIO_VEHICULO = 20;
	private AutoBean autoBean;
	private AlertDialog customDialog= null;	//Creamos el dialogo generico
	private ViewPager pager = null;
	private FragmentPagerAdapterDialog pagerAdapter;
	private  String placa;
	private int imagen_verde = 1;
	private int imagen_rojo = 2;
	private boolean hasRevista=true;
	private float sumaCalificacion =0.0f;
	private boolean entreComentarios=false;

	private static final int RESULT_SETTINGS = 1;
	private LocationManager mLocationManager;
	
	
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.dialogo_datos_correctos);
		
		Bundle bundle = getIntent().getExtras();
		placa = bundle.getString("placa");	
		
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("placa", placa);
		editor.commit();
		
		mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
		
		// Instantiate a ViewPager
		this.pager = (ViewPager) this.findViewById(R.id.pager_dialog);
		autoBean= new AutoBean();
		
		if(!estaEnRevista()){
			 PUNTOS_APP-=PUNTOS_REVISTA;
			 autoBean.setDescripcion_revista(getResources().getString(R.string.sin_revista));
			 autoBean.setImagen_revista(imagen_rojo);
			 hasRevista=false;
		}
		
		datosVehiculo(hasRevista);
		cargaComentarios();
		
		
		
		
		PUNTOS = ((PUNTOS_APP+PUNTOS_USUARIO)/2);
		if(PUNTOS<=20){
			autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_20));
		}else if(PUNTOS<=40 && PUNTOS>20){
			autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_40));
		}else if(PUNTOS<=60 && PUNTOS>40){
			autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_60));
		}else if(PUNTOS<=80 && PUNTOS>60){
			autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_80));
		}else if(PUNTOS>=80){
			autoBean.setDescripcion_calificacion_app(getResources().getString(R.string.texto_calificacion_100));
		}
		autoBean.setCalificacion_final(PUNTOS);
		autoBean.setCalificaion_app(PUNTOS_APP);
		
		
		// Create an adapter with the fragments we show on the ViewPager
		FragmentPagerAdapterDialog adapter = new FragmentPagerAdapterDialog(getSupportFragmentManager());
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_blue), 1,DatosAuto.this,autoBean));
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_red), 2,DatosAuto.this,autoBean));
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources().getColor(R.color.android_darkpink), 3,DatosAuto.this,autoBean));

		this.pager.setAdapter(adapter);

		// Bind the title indicator to the adapter
		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.indicator_dialg);
		titleIndicator.setViewPager(pager);
		
		
		Button dialogo_datos_correctos_btn_iniciar = (Button) findViewById(R.id.dialogo_datos_correctos_btn_iniciar);
		dialogo_datos_correctos_btn_iniciar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (!mLocationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
					new Dialogos().showDialogGPS(DatosAuto.this,"GPS apagado", "ÀDeseas activarlo?");		
				}else{
					
				
					/**
					 * Se inicia el servicio de geolocalizaci—n
					 */
					ServicioGeolocalizacion.taxiActivity = DatosAuto.this;
					startService(new Intent(DatosAuto.this,ServicioGeolocalizacion.class));
					DatosAuto.this.finish();
				}
				
			}
		});
		
	}
	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.trackxi_setting, menu);
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		switch (requestCode) {
		case RESULT_SETTINGS:
			showUserSettings();
			break;
		}
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {

		case R.id.action_settings:
			Intent i = new Intent(this, UserSettingActivity.class);
			startActivityForResult(i, RESULT_SETTINGS);
			break;

		}

		return true;
	}
	private void showUserSettings() {
		SharedPreferences sharedPrefs = PreferenceManager.getDefaultSharedPreferences(this);

		StringBuilder builder = new StringBuilder();

		builder.append("\n Send report:"+ sharedPrefs.getBoolean("prefSendReport", false));

		builder.append("\n Sync Frequency: "+ sharedPrefs.getString("prefSyncFrequency", "NULL"));
	}
	

	/**
	 * carga todos los comentarios de una placa
	 */
	private void cargaComentarios() {
		try{
			  String Sjson=  NetworkUtils.doHttpConnection("http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=getcomentario&placa="+placa);
			  	JSONObject json= (JSONObject) new JSONTokener(Sjson).nextValue();
			      JSONObject json2 = json.getJSONObject("message");
			      JSONObject jsonResponse = new JSONObject(json2.toString());
			      JSONArray cast = jsonResponse.getJSONArray("calificacion");
			      ArrayList<ComentarioBean> arrayComenario= new ArrayList<ComentarioBean>();
			      for (int i=0; i<cast.length(); i++) {
			          	JSONObject oneObject = cast.getJSONObject(i);
						 try {
							 ComentarioBean	  comentarioBean = new ComentarioBean();
							 comentarioBean.setComentario((String) oneObject.getString("comentario"));
							 Float calif =Float.parseFloat((String)oneObject.getString("calificacion"));
							 comentarioBean.setCalificacion(calif);
							 arrayComenario.add(comentarioBean);
							 
							 sumaCalificacion+=calif;
							 entreComentarios=true;
							 
						 } catch (JSONException e) {  
							 e.printStackTrace();
						 }
						 
			      }
			      autoBean.setArrayComentarioBean(arrayComenario);
			      if(entreComentarios){
			    	  float califParcial = (sumaCalificacion/cast.length());
			    	  PUNTOS_USUARIO = (int) (califParcial * 100 /5);
			    	  autoBean.setCalificacion_usuarios(PUNTOS_USUARIO);
			      }else{
			    	  autoBean.setCalificacion_usuarios(0);
			      }
			}catch(JSONException e){
				e.printStackTrace();
			}
	}

	/**
	 * metodo que verifica los datos de adeudos de un taxi
	 */
	private void datosVehiculo(boolean esta_en_revista) {
		try{
			  String Sjson=  NetworkUtils.doHttpConnection("http://dev.datos.labplc.mx/movilidad/vehiculos/"+placa+".json");
			      JSONObject json= (JSONObject) new JSONTokener(Sjson).nextValue();
			      JSONObject json2 = json.getJSONObject("consulta");
			      JSONObject jsonResponse = new JSONObject(json2.toString());
			      JSONObject sys  = jsonResponse.getJSONObject("tenencias");
			      
			    if(sys.getString("tieneadeudos").toString().equals("0")){
			    	autoBean.setDescripcion_tenencia(getResources().getString(R.string.sin_adeudo_tenencia));
			    	autoBean.setImagen_teencia(imagen_verde);
			    }else{
			    	autoBean.setDescripcion_tenencia(getResources().getString(R.string.con_adeudo_tenencia));
			    	autoBean.setImagen_teencia(imagen_rojo);
			    	PUNTOS_APP-=PUNTOS_TENENCIA;
			    }
			    
			    JSONArray cast = jsonResponse.getJSONArray("infracciones");
			    String situacion;
			    boolean hasInfraccion=false;
			      for (int i=0; i<cast.length(); i++) {
			          	JSONObject oneObject = cast.getJSONObject(i);
						 try {
							 situacion = (String) oneObject.getString("situacion");
							 if(!situacion.equals("Pagada")){
								 hasInfraccion=true;
							 }
							
						 } catch (JSONException e) { 
							 e.printStackTrace();
						 }
			      }
			      if(hasInfraccion){
			    	  autoBean.setDescripcion_infracciones(getResources().getString(R.string.tiene_infraccion));
				    	autoBean.setImagen_infraccones(imagen_rojo);
				    	PUNTOS_APP-=PUNTOS_INFRACCIONES;
			      }else{
			    	  autoBean.setDescripcion_infracciones(getResources().getString(R.string.no_tiene_infraccion));
				    	autoBean.setImagen_infraccones(imagen_verde);
			      }
			      
			      JSONArray cast2 = jsonResponse.getJSONArray("verificaciones");
			      if(cast2.length()==0){
			    	  autoBean.setDescripcion_verificacion(getResources().getString(R.string.no_tiene_verificaciones));
					  autoBean.setImagen_verificacion(imagen_rojo);
					  PUNTOS_APP-=PUNTOS_VERIFICACION;
			      }
				      for (int i=0; i<cast2.length(); i++) {
				          	JSONObject oneObject = cast2.getJSONObject(i);
							 try {
								 autoBean.setDescripcion_verificacion(getResources().getString(R.string.tiene_verificaciones)+oneObject.getString("resultado").toString());
								 autoBean.setImagen_verificacion(imagen_verde);
								 
								 
								if(!esta_en_revista){
									 autoBean.setMarca((String) oneObject.getString("marca"));
									 autoBean.setSubmarca((String)  oneObject.getString("submarca"));
									 autoBean.setAnio((String)  oneObject.getString("modelo"));
									 
									 autoBean.setDescripcion_revista(getResources().getString(R.string.sin_revista));
									 autoBean.setImagen_revista(imagen_rojo);
									 
									 Calendar calendar = Calendar.getInstance();
									 int thisYear = calendar.get(Calendar.YEAR);
									 
									 if(thisYear-Integer.parseInt(autoBean.getAnio())<=10){
										 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_nuevo)+" A–o "+autoBean.getAnio());
										 autoBean.setImagen_vehiculo(imagen_verde);
									 }else{
										 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_viejo));
										 autoBean.setImagen_vehiculo(imagen_rojo);
										 PUNTOS_APP-=PUNTOS_ANIO_VEHICULO;
									 }
								}
								
							 } catch (JSONException e) { 
								 e.printStackTrace();
							 }
				      }
				      if(hasInfraccion){
				    	  autoBean.setDescripcion_infracciones(getResources().getString(R.string.tiene_infraccion));
					    	autoBean.setImagen_infraccones(imagen_rojo);
				      }else{
				    	  autoBean.setDescripcion_infracciones(getResources().getString(R.string.no_tiene_infraccion));
					    	autoBean.setImagen_infraccones(imagen_verde);
				      }
			    
			}catch(JSONException e){
				e.printStackTrace();
			}
		
	}

	
	/**
	 * metodo que indica si esta una placa en la revista vehicular
	 * 
	 * @return true (si esta en la revista vehicular)
	 * 		   false (si algo falla o no esta en la revista)
	 */
	private boolean estaEnRevista() {
		try{
		  String Sjson=  NetworkUtils.doHttpConnection("http://mikesaurio.dev.datos.labplc.mx/movilidad/taxis/"+placa+".json");
		    String marca="",submarca="",anio="";
		      JSONObject json= (JSONObject) new JSONTokener(Sjson).nextValue();
		      JSONObject json2 = json.getJSONObject("Taxi");
		      JSONObject jsonResponse = new JSONObject(json2.toString());
		      JSONArray cast = jsonResponse.getJSONArray("concesion");
		      for (int i=0; i<cast.length(); i++) {
		          	JSONObject oneObject = cast.getJSONObject(i);
					 try {
						 marca = (String) oneObject.getString("marca");
						 autoBean.setMarca(marca);
						 submarca = (String)  oneObject.getString("submarca");
						 autoBean.setSubmarca(submarca);
						 anio = (String)  oneObject.getString("anio").replaceAll(" ","");
						 autoBean.setAnio(anio);
						 
						 autoBean.setDescripcion_revista(getResources().getString(R.string.con_revista));
						 autoBean.setImagen_revista(imagen_verde);
						 
						 Calendar calendar = Calendar.getInstance();
						 int thisYear = calendar.get(Calendar.YEAR);
						 
						 if(thisYear-Integer.parseInt(anio)<=10){
							 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_nuevo)+" A–o "+anio);
							 autoBean.setImagen_vehiculo(imagen_verde);
						 }else{
							 autoBean.setDescripcion_vehiculo(getResources().getString(R.string.carro_viejo));
							 autoBean.setImagen_vehiculo(imagen_rojo);
							 PUNTOS_APP-=PUNTOS_ANIO_VEHICULO;
						 }
						 return true;
					 } catch (JSONException e) { return false;}
		      }
		}catch(JSONException e){
			return false;
		}
		return false;
	}

	
	
	
	@Override
	public void onBackPressed() {
		Intent mainIntent = new Intent().setClass(DatosAuto.this, Paginador.class);
		startActivity(mainIntent);
		super.onBackPressed();
	}
	
	
	
}
