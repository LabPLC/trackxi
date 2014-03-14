package codigo.labplc.mx.trackxi.dialogos;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.buscarplaca.bean.AutoBean;
import codigo.labplc.mx.trackxi.buscarplaca.paginador.FragmentPagerAdapterDialog;
import codigo.labplc.mx.trackxi.network.NetworkUtils;
import codigo.labplc.mx.trackxi.paginador.MyFragmentPagerAdapter;
import codigo.labplc.mx.trackxi.paginador.Paginador;
import codigo.labplc.mx.trackxi.paginador.ScreenSlidePageFragment;

import com.viewpagerindicator.TitlePageIndicator;

/**
 * Clase que maneja los di‡logos 
 * @author mikesaurio
 *
 */
public class Dialogos {


	private AlertDialog customDialog= null;	//Creamos el dialogo generico


	public static void Toast(Context context, String text, int duration) {
		Toast.makeText(context, text, duration).show();
	}
	
	public static void Log(Context context, String text, int type) {
		if(type == Log.DEBUG) {
			Log.d(context.getClass().getName().toString(), text);
		} else if(type == Log.ERROR) {
			Log.e(context.getClass().getName().toString(), text);
		} else if(type == Log.INFO) {
			Log.i(context.getClass().getName().toString(), text);
		} else if(type == Log.VERBOSE) {
			Log.v(context.getClass().getName().toString(), text);
		} else if(type == Log.WARN) {
			Log.w(context.getClass().getName().toString(), text);
		}
	}
	
	
	/**
	 * Dialogo que muestra el para que registrarte con datos
	 *
	 * @param Activity (actividad que llama al di‡logo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	public Dialog mostrarParaQue(Activity activity)
    {
		
		AlertDialog.Builder builder = new AlertDialog.Builder(activity);
	    View view = activity.getLayoutInflater().inflate(R.layout.dialogo_paraque, null);
	    builder.setView(view);
	    builder.setCancelable(false);
        //escucha del boton aceptar
        ((Button) view.findViewById(R.id.dialogo_paraque_btnAceptar)).setOnClickListener(new OnClickListener() {
             
            @Override
            public void onClick(View view)
            {
                customDialog.dismiss();    
            }
        });
        return (customDialog=builder.create());// return customDialog;//regresamos el di‡logo
    }   
	
	
	
	
	
	
	
}
