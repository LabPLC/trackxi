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
import codigo.labplc.mx.trackxi.buscarplaca.bean.AutoBean;

public class Comentarios extends View {

	private View view;
	private View view_row;
	private Activity context;
	private LinearLayout container;
	private AutoBean autoBean;
	
	
	public Comentarios(Activity context) {
		super(context);
		this.context = context;
	}

	public Comentarios(Activity context, AttributeSet attrs) {
		super(context, attrs);
		this.context = context;
	}

	public Comentarios(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		this.context = context;
	}
	
	public void init(AutoBean autoBean){
		this.autoBean=autoBean;
		init();
	}
	

	public void init() {


		LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view = inflater.inflate(R.layout.activity_adeudos, null);
		
		TextView adeudos_titulo_main = (TextView)view.findViewById(R.id.adeudos_titulo_main);
		adeudos_titulo_main.setText("Comentarios");
		container=(LinearLayout)view.findViewById(R.id.adeudos_ll_contenedor);
		
		
		for(int i = 0;i< autoBean.getArrayComentarioBean().size();i++){
			llenarAdeudo(autoBean.getArrayComentarioBean().get(i).getComentario(),autoBean.getArrayComentarioBean().get(i).getCalificacion());
		}
		


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
