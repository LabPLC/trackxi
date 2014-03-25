package codigo.labplc.mx.trackxi.tracking;

import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.view.Menu;

public class Activity_null extends Activity {

	public static int cuenta=0;
	public static int cuenta_dest=0;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		Log.d("********************nueva Actividad", "Create"+cuenta);
		ServicioGeolocalizacion.setPanicoActivado(true);
	this.finish();
		
	}

@Override
	protected void onDestroy() {
	if(cuenta>0){
	Log.d("********************nueva Actividad", "adios"+cuenta);
	ServicioGeolocalizacion.setPanicoActivado(false);
	cuenta = 0;
	}else{
		cuenta+=1;
	}
		super.onDestroy();
	}
	

}
