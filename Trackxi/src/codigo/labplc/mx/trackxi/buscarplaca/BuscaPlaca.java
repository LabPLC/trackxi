package codigo.labplc.mx.trackxi.buscarplaca;

import java.io.IOException;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.buscarplaca.paginador.DatosAuto;
import codigo.labplc.mx.trackxi.dialogos.Dialogos;

public class BuscaPlaca extends View implements SurfaceHolder.Callback {

	private Camera camera;
	private SurfaceView surfaceView;
	private SurfaceHolder surfaceHolder;
	private boolean previewing = false;
	private LayoutInflater controlInflater = null;
	private Button busca_placa_btn_tomarfoto;
	private EditText placa;
	private String Splaca;
	private Activity context;
	private View view;


	public BuscaPlaca(Activity context) {
		super(context);
		init(context);
	}
	public BuscaPlaca(Activity context, AttributeSet attrs) {
		super(context, attrs);
		init(context);
		// TODO Auto-generated constructor stub
	}
	public BuscaPlaca(Activity context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		init(context);
	}

	

	public void init(Activity con){
		
		this.context=con;
	
		LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		view = inflater.inflate(R.layout.activity_busca_placa, null);
		
		context.getWindow().setFormat(PixelFormat.UNKNOWN);
		
	
		
		surfaceView = (SurfaceView) view.findViewById(R.id.camerapreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		controlInflater = LayoutInflater.from(context.getBaseContext());
		busca_placa_btn_tomarfoto =(Button)view.findViewById(R.id.busca_placa_btn_tomarfoto);
		busca_placa_btn_tomarfoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				camera.takePicture(myShutterCallback,
					     myPictureCallback_RAW, myPictureCallback_JPG);
			}
		});
		
	
	
	placa = (EditText)view.findViewById(R.id.inicio_de_trabajo_et_placa);
	placa.addTextChangedListener(new TextWatcher() {
		
		@Override
		public void onTextChanged(CharSequence s, int start, int before, int count) {
			Splaca = placa.getText().toString();

	            if (Splaca.length() ==6) {
	            	//buscando datos del carro en el servidor
	            	try {
	            		//cerramos el teclado
	            		InputMethodManager imm = (InputMethodManager)context.getSystemService(Context.INPUT_METHOD_SERVICE);
	            		imm.hideSoftInputFromWindow(placa.getWindowToken(), 0);
	            		Intent intent= new Intent(context,DatosAuto.class);
	            		intent.putExtra("placa", Splaca);
	            		context.startActivityForResult(intent, 0);
	            		
					} catch (Exception e) {
						new Dialogos().Toast(context.getBaseContext(), "Taxi no valido", Toast.LENGTH_LONG);
	          	    	  placa.setText("");
					}
	            }
			
		}
		
		@Override
		public void beforeTextChanged(CharSequence s, int start, int count,
				int after) {
			// TODO Auto-generated method stub
			
		}
		
		@Override
		public void afterTextChanged(Editable s) {
			// TODO Auto-generated method stub
			
		}
	});
	}
	
	 ShutterCallback myShutterCallback = new ShutterCallback(){

		 @Override
		 public void onShutter() {
		  // TODO Auto-generated method stub
		 
		 }};

		PictureCallback myPictureCallback_RAW = new PictureCallback(){

		 @Override
		 public void onPictureTaken(byte[] arg0, Camera arg1) {
		  // TODO Auto-generated method stub
		 
		 }};

		PictureCallback myPictureCallback_JPG = new PictureCallback(){

		 @Override
		 public void onPictureTaken(byte[] arg0, Camera arg1) {
		  // TODO Auto-generated method stub
		  Bitmap bitmapPicture  = BitmapFactory.decodeByteArray(arg0, 0, arg0.length);
		 }};


	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		if (previewing) {
			camera.stopPreview();
			previewing = false;
		}

		if (camera != null) {
			try {
				camera.setPreviewDisplay(surfaceHolder);
				camera.startPreview();
				previewing = true;
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT)
        {   
            camera.setDisplayOrientation(90);
        }
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {                               
            camera.setDisplayOrientation(180);
        }
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		camera = Camera.open();

		
	}

	@Override
	public void surfaceDestroyed(SurfaceHolder holder) {
		camera.stopPreview();
		camera.release();
		camera = null;
		previewing = false;
	}
	
	public View getView(){
		return view;
	}
}
