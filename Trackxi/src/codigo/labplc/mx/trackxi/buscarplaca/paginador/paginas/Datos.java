package codigo.labplc.mx.trackxi.buscarplaca.paginador.paginas;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.util.EventLogTags.Description;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.buscarplaca.bean.AutoBean;
import codigo.labplc.mx.trackxi.buscarplaca.paginador.paginas.termometro.ThermometerView;
import codigo.labplc.mx.trackxi.network.NetworkUtils;

public class Datos extends View {

	private TextView marca,submarca,modelo,descripcion;
	private LinearLayout container,container_usuario;
	private View view;
	private Activity context;
	private AutoBean autoBean;
	
	
	public Datos(Activity context) {
		super(context);
		this.context=context;
	}
	public Datos(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context=context;
	}
	public Datos(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context=context;
	}

	public void init(AutoBean autoBean){
		this.autoBean=autoBean;
		init();
	}
	
	
	public void init(){

		
		LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		view = inflater.inflate(R.layout.activity_datos, null);
		
		marca = (TextView)view.findViewById(R.id.datos_tv_marca);
		submarca = (TextView)view.findViewById(R.id.datos_tv_submarca);
		modelo = (TextView)view.findViewById(R.id.datos_tv_modelo);
		descripcion =(TextView)view.findViewById(R.id.datos_tv_descripcion);
		container = (LinearLayout)view.findViewById(R.id.Thermometer_Container);
		container_usuario = (LinearLayout)view.findViewById(R.id.Thermometer_Container_usuarios);
	
		marca.append(autoBean.getMarca());
		submarca.append(autoBean.getSubmarca());
		modelo.append(autoBean.getAnio());
		
		descripcion.setText(autoBean.getDescripcion_calificacion_app());
		if(autoBean.getCalificacion_final()<=40){
			descripcion.setBackgroundColor(Color.rgb(0xFF, 0x44, 0x44));
		}else if(autoBean.getCalificacion_final()>40 && autoBean.getCalificacion_final()<80){
			descripcion.setBackgroundColor(getResources().getColor(R.color.generic_amarillo));
		}else if(autoBean.getCalificacion_final()>=80){
			descripcion.setBackgroundColor(Color.rgb(0x99, 0xCC, 0x00));
		}
		
		crearTermometro();

		
	}

	
	public void crearTermometro(){

			final ThermometerView thermometer = new ThermometerView(context);
			thermometer.setThermometerProgress(autoBean.getCalificaion_app());
	        container.addView(thermometer);
	        final ThermometerView thermometer_usuario = new ThermometerView(context);
	        thermometer_usuario.setThermometerProgress(autoBean.getCalificacion_usuarios());
	        container_usuario.addView(thermometer_usuario);
	        
		
	}
	
	public View getView(){
		return view;
	}

}
