package codigo.labplc.mx.trackxi.buscarplaca.paginador.paginas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;
import codigo.labplc.mx.trackxi.R;

public class Comentarios extends View {

	private View view;
	private View view_row;
	private Activity context;
	private LinearLayout container;
	private int width;
	
	
	public Comentarios(Activity context) {
		super(context);
		init(context);
	}

	public Comentarios(Activity context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
	}

	public Comentarios(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	public void init(Activity con) {

		this.context = con;

		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.activity_adeudos, null);
		
		TextView adeudos_titulo_main = (TextView)view.findViewById(R.id.adeudos_titulo_main);
		adeudos_titulo_main.setText("Comentarios");
		container=(LinearLayout)view.findViewById(R.id.adeudos_ll_contenedor);
		

		WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
		Display display = wm.getDefaultDisplay();
		 
		Point size = new Point();
		display.getSize(size);
		width = size.x;

		
		llenarAdeudo("Patan Horrible, no sabe manejar",2.0f);
		llenarAdeudo("Muy malo",1.75f);
		llenarAdeudo("bolakdskjdhskjdhfskdjhfskjdvksjdcnkjsdncskjdcnsshdbcshcbshdbcks",5.0f);


	}

	
	public void llenarAdeudo( String concepto, float valor) {
		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view_row = inflater.inflate(R.layout.comentarios_row, null);
		
		TextView comentarios_row_tv_descripcion = (TextView)view_row.findViewById(R.id.comentarios_row_tv_descripcion);
		comentarios_row_tv_descripcion.setText(concepto);

		RatingBar comentarios_row_rating=(RatingBar)view_row.findViewById(R.id.comentarios_row_rating);
		comentarios_row_rating.setRating(valor);
		
		container.addView(view_row);
		
	}

	public View getView() {
		return view;
	}

}
