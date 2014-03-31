package codigo.labplc.mx.trackxi.buscarplaca.paginador.paginas;

import android.app.Activity;
import android.content.Context;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.RatingBar;
import android.widget.TextView;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.buscarplaca.bean.AutoBean;
import codigo.labplc.mx.trackxi.fonts.fonts;

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
		adeudos_titulo_main.setText(getResources().getString(R.string.titulo_tres_comentarios));
		adeudos_titulo_main.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
		adeudos_titulo_main.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		container=(LinearLayout)view.findViewById(R.id.adeudos_ll_contenedor);
		
		
		for(int i = 0;i< autoBean.getArrayComentarioBean().size();i++){
		llenarComentario(autoBean.getArrayComentarioBean().get(i).getComentario(),autoBean.getArrayComentarioBean().get(i).getCalificacion(),i);
		}
		


	}

	
	public void llenarComentario( String concepto, float valor,int i) {
	final	LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		view_row = inflater.inflate(R.layout.comentarios_row, null);
		
	final	TextView comentarios_row_tv_descripcion = (TextView)view_row.findViewById(R.id.comentarios_row_tv_descripcion);
	comentarios_row_tv_descripcion.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_MAMEY));
	comentarios_row_tv_descripcion.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	comentarios_row_tv_descripcion.setText(concepto);

		
	final ImageView rating1_comentarios = (ImageView)view_row.findViewById(R.id.rating1_comentarios);
	rating1_comentarios.setTag(i+"img1");
	final ImageView rating2_comentarios = (ImageView)view_row.findViewById(R.id.rating2_comentarios);
	rating1_comentarios.setTag(i+"img2");
	final ImageView rating3_comentarios = (ImageView)view_row.findViewById(R.id.rating3_comentarios);
	rating1_comentarios.setTag(i+"img3");
	final ImageView rating4_comentarios = (ImageView)view_row.findViewById(R.id.rating4_comentarios);
	rating1_comentarios.setTag(i+"img4");
	final ImageView rating5_comentarios = (ImageView)view_row.findViewById(R.id.rating5_comentarios);
	rating1_comentarios.setTag(i+"img5");
		
		
		if(valor==0.5){
		
		rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
		}
		if(valor==1.0){
			
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		}
		if(valor==1.5){
			
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
			}
		if(valor==2.0){
			
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			}
		if(valor==2.5){
		
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
			}
		if(valor==3.0){
			
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));

			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			}
		if(valor==3.5){
		
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		
			rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
			}
		if(valor==4.0){
		
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		
			rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			}
		if(valor==4.5){
		
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		
			rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		
			rating5_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star3));
			}
		if(valor==5.0){
			
			rating1_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			
			rating2_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			
			rating3_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
		
			rating4_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			
			rating5_comentarios.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_launcher_star2));
			}
	
		
		
		
		
		/*final	RatingBar comentarios_row_rating=(RatingBar)view_row.findViewById(R.id.comentarios_row_rating);
			comentarios_row_rating.setTag(i);
			comentarios_row_rating.setId(i);
			comentarios_row_rating.setRating(valor);
			comentarios_row_rating.setEnabled(false);*/
		
		container.addView(view_row,i);
		
	}

	public View getView() {
		return view;
	}

}
