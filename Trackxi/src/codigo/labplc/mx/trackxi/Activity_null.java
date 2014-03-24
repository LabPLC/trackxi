package codigo.labplc.mx.trackxi;

import codigo.labplc.mx.trackxi.tracking.ServicioGeolocalizacion;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class Activity_null extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		ServicioGeolocalizacion.panicoActivado=false;
		this.finish();
	}

	

}
