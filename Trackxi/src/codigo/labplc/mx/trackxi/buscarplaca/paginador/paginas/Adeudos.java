package codigo.labplc.mx.trackxi.buscarplaca.paginador.paginas;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import codigo.labplc.mx.trackxi.R;

public class Adeudos extends View {

	private View view;
	private View view_row;
	private Activity context;
	private LinearLayout container;
	
	
	public Adeudos(Activity context) {
		super(context);
		init(context);
	}

	public Adeudos(Activity context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public Adeudos(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public void init(Activity con) {

		this.context = con;

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.activity_adeudos, null);
		
		TextView adeudos_titulo_main = (TextView)view.findViewById(R.id.adeudos_titulo_main);
		adeudos_titulo_main.setText("Especificaciones del veh’culo");
		container=(LinearLayout)view.findViewById(R.id.adeudos_ll_contenedor);
		


		
		llenarAdeudo("Revista vehicular","concepto",R.drawable.ic_launcher);
		llenarAdeudo("Infracciones","concepto",R.drawable.ic_launcher);
		llenarAdeudo("Vehiculo","concepto",R.drawable.ic_launcher);
		llenarAdeudo("Verificaciones","concepto",R.drawable.ic_launcher);
		llenarAdeudo("Tenencia","concepto",R.drawable.ic_launcher);

	}

	
	public void llenarAdeudo(String titulo, String concepto, int imagen) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view_row = inflater.inflate(R.layout.adeudos_row, null);

		
		
		TextView adeudos_row_titulo = (TextView)view_row.findViewById(R.id.adeudos_row_titulo);
		TextView adeudos_row_descripcion = (TextView)view_row.findViewById(R.id.adeudos_row_descripcion);
		ImageView adeudos_row_iv = (ImageView)view_row.findViewById(R.id.adeudos_row_iv);
		
		adeudos_row_titulo.setText(titulo);
		adeudos_row_descripcion.setText(concepto);
		adeudos_row_iv.setBackgroundResource(imagen);
		container.addView(view_row);
		
	}

	public View getView() {
		return view;
	}

}
