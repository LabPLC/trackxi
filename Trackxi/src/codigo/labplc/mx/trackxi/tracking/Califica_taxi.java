package codigo.labplc.mx.trackxi.tracking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.RatingBar.OnRatingBarChangeListener;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.network.NetworkUtils;

public class Califica_taxi extends Activity {

	Button calificar_aceptar;
	Button calificar_cancelar;
	EditText comentario;
	RatingBar rank;
	private String Scalificacion;
	private String Scomentario;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_califica_taxi);
		
		comentario = (EditText)findViewById(R.id.dialogo_califica_servicio_et_comentario);
		rank = (RatingBar)findViewById(R.id.dialogo_califica_servicio_ratingBarServicio);
		rank.setOnRatingBarChangeListener(new OnRatingBarChangeListener() {
			

			public void onRatingChanged(RatingBar ratingBar, float rating,
				boolean fromUser) {
	 
				Scalificacion=(String.valueOf(rating));
	 
			}
		});
		calificar_aceptar =(Button)findViewById(R.id.dialogo_califica_servicio_btnAceptar);
		calificar_aceptar.setOnClickListener(new View.OnClickListener() {
			
			

			@Override
			public void onClick(View v) {
				SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
				String placa = prefs.getString("placa", null);
				String id_usuario = prefs.getString("uuid", null);
				Scomentario=comentario.getText().toString().replaceAll(" ", "+");

				
			
				
				String url= "http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addcomentario"
						+"&id_usuario="+id_usuario
						+"&calificacion="+Scalificacion
						+"&comentario="+Scomentario
						+"&placa="+placa;

				NetworkUtils.doHttpConnection(url);
	
				Intent svc = new Intent(Califica_taxi.this, ServicioGeolocalizacion.class);
				stopService(svc);
				Califica_taxi.this.finish();
				
			}
		});
		calificar_cancelar = (Button)findViewById(R.id.dialogo_califica_servicio_btnCancelar);
		calificar_cancelar.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				Intent svc = new Intent(Califica_taxi.this, ServicioGeolocalizacion.class);
				stopService(svc);
				Califica_taxi.this.finish();
			}
		});
		
		
		
	}


}
