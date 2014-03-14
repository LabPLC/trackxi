package codigo.labplc.mx.trackxi.buscarchofer;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import codigo.labplc.mx.trackxi.R;

public class BuscarChofer extends View{

	View view;
	Activity context;
	AutoCompleteTextView nombre;
	
	public BuscarChofer(Activity context) {
		super(context);
		init(context);
	}
	public BuscarChofer(Activity context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}
	public BuscarChofer(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	

	public void init(Activity con){
		
		this.context=con;
	
		LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		view = inflater.inflate(R.layout.activity_buscar_chofer, null);
		
		nombre=(AutoCompleteTextView)view.findViewById(R.id.inicio_de_trabajo_et_placa);
		
		String[] nombres = {"Pedro","Pablo","mike","hugo"};
		// Creamos el adaptador para el AutoCompleteTextView 
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, android.R.layout.simple_list_item_1, nombres);
		nombre.setAdapter(adapter);
		
		
		
		
		
	}

	public View getView(){
		return view;
	}
}
