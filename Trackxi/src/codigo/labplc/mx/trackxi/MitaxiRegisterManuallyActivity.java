

package codigo.labplc.mx.trackxi;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.ContactsContract;
import android.provider.ContactsContract.Contacts;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.R.id;
import codigo.labplc.mx.trackxi.R.layout;
import codigo.labplc.mx.trackxi.R.string;
import codigo.labplc.mx.trackxi.dialogos.Dialogos;
import codigo.labplc.mx.trackxi.expresionesregulares.RegularExpressions;
import codigo.labplc.mx.trackxi.facebook.FacebookLogin;
import codigo.labplc.mx.trackxi.facebook.FacebookLogin.OnLoginFacebookListener;
import codigo.labplc.mx.trackxi.fonts.fonts;
import codigo.labplc.mx.trackxi.network.NetworkUtils;
import codigo.labplc.mx.trackxi.paginador.Paginador;
import codigo.labplc.mx.trackxi.registro.bean.UserBean;
import codigo.labplc.mx.trackxi.registro.validador.EditTextValidator;

public class MitaxiRegisterManuallyActivity extends Activity {

	private int RESULT_LOAD_IMAGE = 1;
	private int RESULT_LOAD_FOTO = 2;
	private int RESULT_LOAD_CONTACT = 3;

	private AlertDialog customDialog = null; // Creamos el dialogo generico

	private EditText etInfousername;
	private EditText etInfouseremail;
	private EditText etInfousertelemergency;
	private EditText etInfousermailemergency;
	private ImageView txtparaque;
	private ImageView userfoto;
	private String foto;
	private UserBean user;
	private boolean hasFoto = false;
	String origen;
	
	private FacebookLogin facebookLogin;
	private Button btnLogin;

	private boolean[] listHasErrorEditText = { false, false, false, false };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_register_manually);
		
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			origen = bundle.getString("origen");	

		}
		

		initUI();

	}

	/**
	 * metodo que inicia la vista
	 */
	public void initUI() {
		// creamos la ruta de la foto con codigo unico
		//foto = Environment.getExternalStorageDirectory() + "/imagen"+ getCode() + ".jpg";


		TextView mitaxiregistermanually_tv_label= (TextView)findViewById(R.id.mitaxiregistermanually_tv_label);
		 mitaxiregistermanually_tv_label.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		 mitaxiregistermanually_tv_label.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_ROJO));
		 
		 TextView mitaxiregistermanually_tv_emergencia= (TextView)findViewById(R.id.mitaxiregistermanually_tv_emergencia);
		 mitaxiregistermanually_tv_emergencia.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		 mitaxiregistermanually_tv_emergencia.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_ROJO));
		 
		
		
		etInfousername = (EditText) findViewById(R.id.mitaxiregistermanually_et_infousername);
		etInfousername.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		etInfousername.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));
		
		etInfouseremail = (EditText) findViewById(R.id.mitaxiregistermanually_et_infouseremail);
		etInfouseremail.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		etInfouseremail.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));

		
		etInfousertelemergency = (EditText) findViewById(R.id.mitaxiregistermanually_et_telemer);
		etInfousertelemergency.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		etInfousertelemergency.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));

		
		etInfousermailemergency = (EditText) findViewById(R.id.mitaxiregistermanually_et_correoemer);
		etInfousermailemergency.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_ROJO));
		etInfousermailemergency.setTextColor(new fonts(this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));


		facebookLogin = new FacebookLogin(MitaxiRegisterManuallyActivity.this);
		//btnLogin.setPublishPermissions(Arrays.asList("publish_stream","read_stream"));
		btnLogin =(Button)findViewById(R.id.mitaxiregistermanually_btn_facebook);
		btnLogin.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View view) {
			
				facebookLogin.loginFacebook();
				facebookLogin.setOnLoginFacebookListener(new OnLoginFacebookListener() {
					@Override
					public void onLoginFacebook(boolean status) {
						loginFacebook(status);
					}
				});
			}
		});
		
		
		
		Button contacto_emer = (Button) findViewById(R.id.mitaxiregistermanually_btn_contactos);
		contacto_emer.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(Intent.ACTION_PICK,Contacts.CONTENT_URI);
				startActivityForResult(intent, RESULT_LOAD_CONTACT);
			}
		});

		userfoto = (ImageView) findViewById(R.id.mitaxiregistermanually_im_fotousuario);
		userfoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				origenDeLaImagen().show();

			}
		});

		txtparaque = (ImageView) findViewById(R.id.mitaxiregistermanually_im_info);
		txtparaque.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {

				new Dialogos().mostrarParaQue(
						MitaxiRegisterManuallyActivity.this).show();
			}
		});

		etInfousername.setTag(RegularExpressions.KEY_IS_STRING);
		etInfouseremail.setTag(RegularExpressions.KEY_IS_EMAIL);
		etInfousertelemergency.setTag(RegularExpressions.KEY_IS_NUMBER);
		etInfousermailemergency.setTag(RegularExpressions.KEY_IS_EMAIL);

		etInfousername.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfousername, listHasErrorEditText,0));
		etInfouseremail.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfouseremail,listHasErrorEditText, 1));
		etInfousertelemergency.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfousertelemergency,listHasErrorEditText, 2));
		etInfousermailemergency.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfousermailemergency,listHasErrorEditText, 3));
		
	Button mitaxiregistermanually_btn_ok =(Button)findViewById(R.id.mitaxiregistermanually_btn_ok);
	mitaxiregistermanually_btn_ok.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_AMARILLO));
	mitaxiregistermanually_btn_ok.setOnClickListener(new View.OnClickListener() {
		
		@Override
		public void onClick(View v) {
			if (!isAnyEditTextEmpty() && hasFoto) {
				if (!hasErrorEditText()) {
					if (NetworkUtils.isNetworkConnectionOk(getBaseContext())) {
						try {
							saveUserInfo();
						} catch (JSONException e) {
							Log.d("error 301", "error fatal :(");
							
						}
					} else {
						Log.d("error 302", getString(R.string.no_internet_connection));
						
					}
				} else {
					Toast.makeText(getApplicationContext(),getString(R.string.edittext_wrong_info),Toast.LENGTH_LONG).show();

				}
			} else {
				Toast.makeText(getApplicationContext(),getString(R.string.edittext_emtpy),Toast.LENGTH_LONG).show();

			}
		}
	});
		Button mitaxiregistermanually_btn_cancel=(Button)findViewById(R.id.mitaxiregistermanually_btn_cancel);
		mitaxiregistermanually_btn_cancel.setTypeface(new fonts(this).getTypeFace(fonts.FLAG_AMARILLO));
		mitaxiregistermanually_btn_cancel.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				finish();
			}
		});

		
		if(origen.equals("menu")){
			llenarCampos();
			mitaxiregistermanually_btn_ok.setText("Actualizar");
			hasFoto = true;
		}
		
	}

	public void llenarCampos(){
		SharedPreferences prefs = getSharedPreferences("MisPreferenciasTrackxi",Context.MODE_PRIVATE);
		etInfousername.setText(prefs.getString("nombre", null));
		etInfousername.setEnabled(false);
		etInfouseremail.setText(prefs.getString("correo", null));
		etInfouseremail.setEnabled(false);
		etInfousertelemergency.setText(prefs.getString("telemer", null));
		etInfousermailemergency.setText(prefs.getString("correoemer", null));
		foto = prefs.getString("foto", null);
		File file = new File(foto);
		if (file.exists()) {
			Bitmap myBitmap = BitmapFactory.decodeFile(file
					.getAbsolutePath());
			Matrix mat = new Matrix();
			Bitmap bMapRotate = Bitmap.createBitmap(myBitmap, 0, 0,
					myBitmap.getWidth(), myBitmap.getHeight(), mat, true);
			userfoto.setImageBitmap(bMapRotate);
			
		}
		
	}

	public boolean hasErrorEditText() {
		for (boolean hasError : listHasErrorEditText)
			if (hasError)
				return true;
		return false;
	}

	public void saveUserInfo() throws JSONException {

		user = new UserBean();
		user.setNombre(etInfousername.getText().toString().replaceAll(" ", "+"));
		user.setCorreo(etInfouseremail.getText().toString());
		user.setTelemergencia(etInfousertelemergency.getText().toString());
		user.setCorreoemergencia(etInfousermailemergency.getText().toString());
		user.setFoto(foto);
		Upload nuevaTarea = new Upload();
		nuevaTarea.execute(foto);

	}

	/**
	 * Guarda las preferencias del usuario en la aplicación
	 * 
	 * @param (user) bean que contiene los datos del usuario
	 * @return void
	 */
	public void savePreferences(UserBean user) {

		SharedPreferences prefs = getSharedPreferences(
				"MisPreferenciasTrackxi", Context.MODE_PRIVATE);
		SharedPreferences.Editor editor = prefs.edit();
		editor.putString("nombre", user.getNombre());
		editor.putString("correo", user.getCorreo());
		editor.putString("telemer", user.getTelemergencia());
		editor.putString("correoemer", user.getCorreoemergencia());
		editor.putString("uuid", user.getUUID());
		editor.putString("foto", user.getFoto());
		editor.commit();

		Intent mainIntent = new Intent().setClass(
				MitaxiRegisterManuallyActivity.this, Paginador.class);
		startActivity(mainIntent);

		MitaxiRegisterManuallyActivity.this.finish();

	}

	/**
	 * Verifica si alguno de los campos {@link EditText} con la información del
	 * usuario está vacio.
	 * 
	 * @return (boolean) <b>true</b> si esta vacio <b>false</b> si no esta vacio
	 */
	public boolean isAnyEditTextEmpty() {
		boolean empty = false;
		if (EditTextValidator.isEditTextEmpty(etInfousername))
			empty = true;
		if (EditTextValidator.isEditTextEmpty(etInfouseremail))
			empty = true;
		if (EditTextValidator.isEditTextEmpty(etInfousertelemergency))
			empty = true;
		if (EditTextValidator.isEditTextEmpty(etInfousermailemergency))
			empty = true;
		return empty;
	}

	

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	
		if (requestCode == RESULT_LOAD_FOTO) {
			File file = new File(foto);
			if (file.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(file
						.getAbsolutePath());
				Matrix mat = new Matrix();
				mat.postRotate(-90);
				Bitmap bMapRotate = Bitmap.createBitmap(myBitmap, 0, 0,
						myBitmap.getWidth(), myBitmap.getHeight(), mat, true);
				userfoto.setImageBitmap(bMapRotate);
				hasFoto = true;
			}

		}
		if (requestCode == RESULT_LOAD_IMAGE && resultCode == RESULT_OK	&& null != data) {
	 			Uri imageUri = data.getData();
	 			  			Bitmap myBitmap;
	 			  			try {
	 			  				myBitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
	 			  				userfoto.setImageBitmap(myBitmap);
	 			  				BitmapDrawable drawable = (BitmapDrawable) userfoto.getDrawable();
	 			  				Bitmap bitmap_prev = drawable.getBitmap();
	 			  				hasFoto = true;
	 			  			    try{
	 			  			        File file = new File(foto);
	 			  			        FileOutputStream fOut = new FileOutputStream(file);
	 			  			        bitmap_prev.compress(Bitmap.CompressFormat.JPEG, 50, fOut);
	 			  			        fOut.flush();
	 			  			        fOut.close();}
	 			  			    catch (Exception e) {
	 			  			        e.printStackTrace();
	 			  			        Log.i(null, "Save file error!");
	 			  			}
	 			  				
	 			  			} catch (FileNotFoundException e) {
	 			  				e.printStackTrace();
	 			  			} catch (IOException e) {
	 			  				e.printStackTrace();
	 			  			}
			
		}
		if (requestCode == RESULT_LOAD_CONTACT) {
			getContactInfo(data);
		}
	}

	/**
	 * clase que envia por post los datos del registro
	 * 
	 * @author mikesaurio
	 * 
	 */
	class Upload extends AsyncTask<String, Void, Void> {

		private ProgressDialog pDialog;
		private String miFoto = "";
		private String resultado;
		public static final int HTTP_TIMEOUT = 30 * 1000;

		@Override
		protected Void doInBackground(String... params) {
			miFoto = (String) params[0];
			try

			{

				System.setProperty("http.keepAlive", "false");
				HttpClient httpclient = new DefaultHttpClient();
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
				StrictMode.setThreadPolicy(policy);
				final HttpParams par = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(par, HTTP_TIMEOUT);
				HttpConnectionParams.setSoTimeout(par, HTTP_TIMEOUT);
				ConnManagerParams.setTimeout(par, HTTP_TIMEOUT);
				HttpPost httppost;
				if(origen.equals("menu")){
				 httppost = new HttpPost("http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=updatepost");
				//	 httppost = new HttpPost("http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=identificaplaca");
				}else{
				 httppost = new HttpPost("http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addpost");
				}
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("nombre", new StringBody(user.getNombre() + ""));
				entity.addPart("correo", new StringBody(user.getCorreo() + ""));
				entity.addPart("telemer",new StringBody(user.getTelemergencia() + ""));
				entity.addPart("os", new StringBody(user.getOs() + ""));
				entity.addPart("correoemer",new StringBody(user.getCorreoemergencia() + ""));

				File file = new File(miFoto);
				entity.addPart("foto", new FileBody(file));
				System.setProperty("http.keepAlive", "false");
				httppost.setEntity(entity);
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
				StringBuffer sb = new StringBuffer("");
				String linea = "";
				String NL = System.getProperty("line.separator");

				while ((linea = in.readLine()) != null) {
					sb.append(linea + NL);
				}
				in.close();
				resultado = sb.toString();
				Log.d("*******************", resultado+"");
				
				
				httpclient = null;
				response = null;
				if (resultado != null) {
					String errorJson = "";
					String successsJson = "";
					String pk_user = "";

					JSONObject json = (JSONObject) new JSONTokener(resultado).nextValue();

					JSONObject json2 = json.getJSONObject("message");
					try {
						errorJson = (String) json2.get("error");
					} catch (JSONException e) {
						errorJson = null;
					}
					try {
						successsJson = (String) json2.get("success");
						pk_user = (String) json2.get("id");
					} catch (JSONException e) {
						successsJson = null;
						pk_user = null;
					}

					if (pk_user != null) {
						user.setUUID(pk_user);// agregamos el UUID del usuario
						savePreferences(user); // guardamos todo en preferencias

					} else if (errorJson != null) {
						Log.d("error 201", resultado+"");
						
					}
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
			pDialog = new ProgressDialog(MitaxiRegisterManuallyActivity.this);
			pDialog.setMessage("Subiendo la información, espere.");
			pDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			pDialog.dismiss();
			this.cancel(true);
		}

	}

	/**
	 * Dialogo para que el usuario reporte una anomalia en el taxi o el chofer
	 * 
	 * @param Activity
	 *            (actividad que llama al diálogo)
	 * @return Dialog (regresa el dialogo creado)
	 **/
	public Dialog origenDeLaImagen() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		View view = getLayoutInflater().inflate(
				R.layout.dialogo_tipo_de_imagen, null);
		builder.setView(view);
		builder.setCancelable(true);
		//fuentes
((Button) view.findViewById(R.id.dialogo_tipo_de_imagen_btnCancelar)).setTypeface(new fonts(MitaxiRegisterManuallyActivity.this).getTypeFace(fonts.FLAG_AMARILLO));
((Button) view.findViewById(R.id.dialogo_tipo_de_imagen_btnAceptar)).setTypeface(new fonts(MitaxiRegisterManuallyActivity.this).getTypeFace(fonts.FLAG_AMARILLO));
((TextView) view.findViewById(R.id.dialogo_tipo_de_imagen_tv_texto)).setTypeface(new fonts(MitaxiRegisterManuallyActivity.this).getTypeFace(fonts.FLAG_ROJO));
((TextView) view.findViewById(R.id.dialogo_tipo_de_imagen_tv_texto)).setTextColor(new fonts(MitaxiRegisterManuallyActivity.this).getColorTypeFace(fonts.FLAG_GRIS_OBSCURO));


		// escucha del boton aceptar
		((Button) view.findViewById(R.id.dialogo_tipo_de_imagen_btnCancelar)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						foto = Environment.getExternalStorageDirectory() + "/imagen"+ NetworkUtils.getCode() + ".jpg";
						// Camara
						Intent intent = new Intent(	MediaStore.ACTION_IMAGE_CAPTURE);
						Uri output = Uri.fromFile(new File(foto));
						intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
						startActivityForResult(intent, RESULT_LOAD_FOTO); 
						customDialog.dismiss(); // cerramos el diálogo
					}
				});

		// escucha del boton cancelar
		((Button) view.findViewById(R.id.dialogo_tipo_de_imagen_btnAceptar)).setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View view) {
						foto = Environment.getExternalStorageDirectory() + "/imagen"+ NetworkUtils.getCode() + ".jpg";
						// Galeria
						Intent i = new Intent(Intent.ACTION_PICK,android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
						startActivityForResult(i, RESULT_LOAD_IMAGE);

						customDialog.dismiss(); // cerramos el diálogo
					}

				});
		return (customDialog = builder.create());
	}
	
	
	public void getContactInfo(Intent intent)
	{
		try{
			  Cursor   cursor =  managedQuery(intent.getData(), null, null, null, null);      
			  if(!cursor.isClosed()&&cursor!=null){
			   while (cursor.moveToNext()) 
			   {           
			       String contactId = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));
			       String name = cursor.getString(cursor.getColumnIndexOrThrow(ContactsContract.Contacts.DISPLAY_NAME)); 
			       String hasPhone = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER));
		
			       if ( hasPhone.equalsIgnoreCase("1"))
			           hasPhone = "true";
			       else
			           hasPhone = "false" ;
			       if (Boolean.parseBoolean(hasPhone)) 
			       {
			        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = "+ contactId,null, null);
			        while (phones.moveToNext()) 
			        {
			          String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
			          etInfousertelemergency.setText(phoneNumber.replaceAll(" ", ""));
			          break;
			        }
			        phones.close();
			       }
		
			       // Find Email Addresses
			       Cursor emails = getContentResolver().query(ContactsContract.CommonDataKinds.Email.CONTENT_URI,null,ContactsContract.CommonDataKinds.Email.CONTACT_ID + " = " + contactId,null, null);
			       while (emails.moveToNext()) 
			       {
			        String emailAddress = emails.getString(emails.getColumnIndex(ContactsContract.CommonDataKinds.Email.DATA));
			        etInfousermailemergency.setText(emailAddress);
			        break;
			       }
			       
			       emails.close();
			       break;
			  }  
			  }
		}catch(Exception e){
			
		}
	}
	/**
	 * Login in to Facebook
	 * 
	 * @param status
	 */
	public void loginFacebook(boolean status) {
		Toast.makeText(getApplicationContext(), "..."+status, Toast.LENGTH_SHORT).show();
		if(status) {
			Toast.makeText(getApplicationContext(), "Welcome!! :D", Toast.LENGTH_SHORT).show();
			
		//	ImageView ivUserImageProfile = (ImageView) findViewById(R.id.iv_UserImageProfile);
		//	facebookLogin.loadImageProfileToImageView(facebookLogin.getUserId(), ivUserImageProfile);
			
	//		TextView tvUserName = (TextView) findViewById(R.id.tv_UserName);
		//	TextView tvUserId = (TextView) findViewById(R.id.tv_UserId);
			
	//		tvUserName.setText(facebookLogin.getUserName());
	//		tvUserId.setText("ID: " + facebookLogin.getUserId());
			btnLogin.setText(facebookLogin.getUserName()+"");
			btnLogin.setEnabled(false);
			
		//	getListOfFriends(facebookLogin.getUserId());
			
		} else {
			Toast.makeText(getApplicationContext(), "Algo falló al conectar con facebook", Toast.LENGTH_SHORT).show();
		}
	}
	
}