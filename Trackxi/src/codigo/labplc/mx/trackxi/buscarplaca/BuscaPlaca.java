package codigo.labplc.mx.trackxi.buscarplaca;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorMatrix;
import android.graphics.ColorMatrixColorFilter;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.hardware.Camera;
import android.hardware.Camera.PictureCallback;
import android.hardware.Camera.ShutterCallback;
import android.media.CamcorderProfile;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.StrictMode;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.Spanned;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.buscarplaca.paginador.DatosAuto;
import codigo.labplc.mx.trackxi.dialogos.Dialogos;
import codigo.labplc.mx.trackxi.fonts.fonts;
import codigo.labplc.mx.trackxi.network.NetworkUtils;

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
	private String foto;
    private boolean mAutoFocus;
  //  public boolean fotoGirada=false;


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

		foto = Environment.getExternalStorageDirectory() + "/imagen"+ NetworkUtils.getCode() + ".jpg";
		
		
		LayoutInflater inflater = (LayoutInflater)   getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE); 
		view = inflater.inflate(R.layout.activity_busca_placa, null);
		
		context.getWindow().setFormat(PixelFormat.UNKNOWN);
		
		((TextView) view.findViewById(R.id.inicio_de_trabajo_tv_nombre)).setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));	
		((TextView) view.findViewById(R.id.inicio_de_trabajo_tv_nombre)).setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_AMARILLO));
		
		((TextView) view.findViewById(R.id.inicio_de_trabajo_tv_foto)).setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
		((TextView) view.findViewById(R.id.inicio_de_trabajo_tv_foto)).setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_AMARILLO));

		
		surfaceView = (SurfaceView) view.findViewById(R.id.camerapreview);
		surfaceHolder = surfaceView.getHolder();
		surfaceHolder.addCallback(this);
		surfaceHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);
		controlInflater = LayoutInflater.from(context.getBaseContext());
		busca_placa_btn_tomarfoto =(Button)view.findViewById(R.id.busca_placa_btn_tomarfoto);
		busca_placa_btn_tomarfoto.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_AMARILLO));
		
		busca_placa_btn_tomarfoto.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				try{
				camera.takePicture(myShutterCallback,myPictureCallback_RAW, myPictureCallback_JPG);
				
				}catch(Exception e){
					e.printStackTrace();
				}
			}
		});
		
	
	
	placa = (EditText)view.findViewById(R.id.inicio_de_trabajo_et_placa);
	placa.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_GRIS_OBSCURO));
	placa.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	placa.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME|InputType.TYPE_TEXT_FLAG_CAP_CHARACTERS);
	placa.setTypeface(new fonts(context).getTypeFace(fonts.FLAG_ROJO));
	placa.setTextColor(new fonts(context).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
	
	placa.setFilters(new InputFilter[] {
		    new InputFilter() {
		        public CharSequence filter(CharSequence src, int start,
		                int end, Spanned dst, int dstart, int dend) {
		            if(src.equals("")){ // for backspace
		                return src;
		            }
		            if(src.toString().matches("[a,b,m,A,B,M]?")){
		                return src;
		                
		            }else  if(src.toString().matches("[0,1,2,3,4,5,6,7,8,9]*")){
		                return src;
		            }
		            return "";
		        }
		    }
		});
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
	            		Intent intent= new Intent().setClass(context,DatosAuto.class);
	            		intent.putExtra("placa", Splaca);
	            		context.startActivityForResult(intent, 0);
	            		placa.setText("");
	            		context.finish();
	            		placa.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
					} catch (Exception e) {
						new Dialogos().Toast(context.getBaseContext(), "Taxi no valido", Toast.LENGTH_LONG);
	          	    	  placa.setText("");
					}
	            }
	            else{
	            	if(Splaca.length()>=1){
	            		placa.setInputType(InputType.TYPE_CLASS_NUMBER);
	            	}else{
	            		placa.setInputType(InputType.TYPE_TEXT_VARIATION_PERSON_NAME);
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
			 Bitmap bitmapPicture  = toGrayscale(BitmapFactory.decodeByteArray(arg0, 0, arg0.length));	
			 System.gc();
		Matrix mat = new Matrix();
		mat.postRotate(90);
		Bitmap bMapRotate = Bitmap.createBitmap(bitmapPicture, 0, 0,bitmapPicture.getWidth(), bitmapPicture.getHeight(), mat, true);

			int alto = bMapRotate.getHeight()/3;
			Log.d( "***bMapRotateancho", bMapRotate.getWidth()+"");
			Log.d( "***bMapRotatealto", bMapRotate.getHeight()+"");
			Log.d( "***esizedbitmap1inicio", 0+","+alto);
			Log.d( "***esizedbitmap1fin", bMapRotate.getWidth()+","+alto*2);
			Bitmap esizedbitmap1 = Bitmap.createBitmap(bMapRotate,0,alto,bMapRotate.getWidth(),alto*2);
		 
		  try{
			  Log.d("*******************", "TOME LA FOTO");
			        File file = new File(foto);
			        FileOutputStream fOut = new FileOutputStream(file);
			        esizedbitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
			        fOut.flush();
			        fOut.close();
			        Uploaded nuevaTareas = new Uploaded();
					nuevaTareas.execute(foto);
			     }
			    catch (Exception e) {
			        e.printStackTrace();
			        Log.i(null, "Save file error!");
			}
		 }};

	@Override
	public void surfaceChanged(SurfaceHolder holder, int format, int width,
			int height) {
		
		if (previewing) {
			camera.stopPreview();
			previewing = false;
		}
		try {
            CamcorderProfile profile ;

            int numCameras = Camera.getNumberOfCameras();
            if (numCameras > 1) {
            profile = (CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_FRONT,CamcorderProfile.QUALITY_LOW));
            }
            else{
                profile = (CamcorderProfile.get(Camera.CameraInfo.CAMERA_FACING_BACK,CamcorderProfile.QUALITY_LOW));
            }


            Camera.Parameters parameters = camera.getParameters();
            parameters.setPreviewSize(profile.videoFrameWidth, profile.videoFrameHeight);
         /*parameters.setFlashMode(Camera.Parameters.FLASH_MODE_AUTO);
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
            parameters.setSceneMode(Camera.Parameters.SCENE_MODE_AUTO);
            parameters.setWhiteBalance(Camera.Parameters.WHITE_BALANCE_AUTO);
            parameters.setExposureCompensation(0);
            parameters.setPictureFormat(ImageFormat.JPEG);
            parameters.setJpegQuality(100);*/
            camera.setParameters(parameters);
            camera.setPreviewDisplay(holder);
            camera.startPreview();
        }
        catch (IOException e) {
            Log.d("****", "Error setting camera preview: " + e.getMessage());
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
           // fotoGirada=true;
        }
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE)
        {                               
            camera.setDisplayOrientation(180);
           // fotoGirada=false;
        }
	}

	@Override
	public void surfaceCreated(SurfaceHolder holder) {
		if(camera==null){
			 
		camera = Camera.open();
		}
		
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
	
	
	
	class Uploaded extends AsyncTask<String, Void, Void> {

		private ProgressDialog pDialog;
		private String miFoto = "";
		private String resultado;
		public static final int HTTP_TIMEOUT = 60 * 1000;
		@Override
		protected Void doInBackground(String... params) {
			miFoto = (String) params[0];
			
			
			
			try

			{
				HttpContext localContext = new BasicHttpContext();
				   HttpClient httpclient = new DefaultHttpClient();
				 StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				final HttpParams par = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(par, HTTP_TIMEOUT);
				HttpConnectionParams.setSoTimeout(par, HTTP_TIMEOUT);
				ConnManagerParams.setTimeout(par, HTTP_TIMEOUT);
				HttpPost  httppost = new HttpPost("http://codigo.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=identificaplaca");		
				MultipartEntity entity = new MultipartEntity();
			

				File file = new File(miFoto);
				Log.d("FILE*****SIZE", file.length()+"");
				Bitmap myBitmap =BitmapFactory.decodeFile(file.getAbsolutePath());
			/*mike	Matrix mat = new Matrix();
				mat.postRotate(90);
				
				Bitmap bMapRotate = Bitmap.createBitmap(myBitmap, 0, 0,myBitmap.getWidth(), myBitmap.getHeight(), mat, true);
				
				int alto = bMapRotate.getHeight()/3;
				
				Log.d( "***bMapRotateancho", bMapRotate.getWidth()+"");
				Log.d( "***bMapRotatealto", bMapRotate.getHeight()+"");
				Log.d( "***esizedbitmap1inicio", 0+","+alto);
				Log.d( "***esizedbitmap1fin", bMapRotate.getWidth()+","+alto*2);
				Bitmap esizedbitmap1 = Bitmap.createBitmap(bMapRotate,0,alto,bMapRotate.getWidth(),alto*2);*/
		        FileOutputStream fOut = new FileOutputStream(file);
		        myBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fOut);
		        Log.d("FILE*****SIZE2", file.length()+"");
		        fOut.flush();
		        fOut.close();
				
				entity.addPart("foto", new FileBody(file));
				System.setProperty("http.keepAlive", "false");
				httppost.setEntity(entity);
				HttpResponse response = null;
				 response = httpclient.execute(httppost, localContext);
				BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String linea = "";
				String NL = System.getProperty("line.separator");

				while ((linea = in.readLine()) != null) {
					sb.append(linea + NL);
				}
				in.close();
				resultado = sb.toString();
				httpclient = null;
				response = null;
				if (resultado != null) {
					Log.d("*******************zaza", resultado+"");
				} else {
					Log.d("error 202", "Null en la respuesta");
					
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			return null;
		}

		protected void onPreExecute() {
			super.onPreExecute();
			pDialog = new ProgressDialog(context);
			pDialog.setCanceledOnTouchOutside(false);
			pDialog.setMessage("Procesando la foto, espere....");
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setCancelable(true);
		
			pDialog.show();
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			placa.setText(resultado+"");
		//	Dialogos.Toast(context, resultado+"...", Toast.LENGTH_LONG);
			pDialog.dismiss();
		}
	}
	
	/*public static Bitmap createBlackAndWhite(Bitmap src) {
	    int width = src.getWidth();
	    int height = src.getHeight();
	    // create output bitmap
	    Bitmap bmOut = Bitmap.createBitmap(width, height, src.getConfig());
	    // color information
	    int A, R, G, B;
	    int pixel;

	    // scan through all pixels
	    for (int x = 0; x < width; ++x) {
	        for (int y = 0; y < height; ++y) {
	            // get pixel color
	            pixel = src.getPixel(x, y);
	            A = Color.alpha(pixel);
	            R = Color.red(pixel);
	            G = Color.green(pixel);
	            B = Color.blue(pixel);
	            int gray = (int) (0.2989 * R + 0.5870 * G + 0.1140 * B);

	            // use 128 as threshold, above -> white, below -> black
	            if (gray > 128) 
	                gray = 255;
	            else
	                gray = 0;
	            // set new pixel color to output bitmap
	            bmOut.setPixel(x, y, Color.argb(A, gray, gray, gray));
	        }
	    }
	    return bmOut;
	}
*/
public Bitmap toGrayscale(Bitmap bmpOriginal)
    {        
        int width, height;
        height = bmpOriginal.getHeight();
        width = bmpOriginal.getWidth();    
        Bitmap bmpGrayscale = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        Canvas c = new Canvas(bmpGrayscale);
        Paint paint = new Paint();
        ColorMatrix cm = new ColorMatrix();
        cm.setSaturation(0);
        ColorMatrixColorFilter f = new ColorMatrixColorFilter(cm);
        paint.setColorFilter(f);
        c.drawBitmap(bmpOriginal, 0, 0, paint);
        return bmpGrayscale;
    }
	
	
	
}
