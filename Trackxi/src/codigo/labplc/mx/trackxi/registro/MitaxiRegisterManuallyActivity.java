package codigo.labplc.mx.trackxi.registro;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;

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

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.dialogos.Dialogos;
import codigo.labplc.mx.trackxi.expresionesregulares.RegularExpressions;
import codigo.labplc.mx.trackxi.network.NetworkUtils;
import codigo.labplc.mx.trackxi.registro.bean.UserBean;
import codigo.labplc.mx.trackxi.registro.validador.EditTextValidator;

public class MitaxiRegisterManuallyActivity extends Activity implements
		OnClickListener {

	private EditText etInfousername;
	private EditText etInfouseremail;
	private EditText etInfousertelemergency;
	private EditText etInfousermailemergency;
	private ImageView txtparaque;
	private ImageView userfoto;
	private String foto;
	private UserBean user;

	private boolean[] listHasErrorEditText = { false, false, false, false };

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_mitaxi_register_manually);

		initUI();

	}

	/**
	 * metodo que inicia la vista
	 */
	public void initUI() {
		// creamos la ruta de la foto con codigo unico
		foto = Environment.getExternalStorageDirectory() + "/imagen"
				+ getCode() + ".jpg";

		etInfousername = (EditText) findViewById(R.id.mitaxiregistermanually_et_infousername);
		etInfouseremail = (EditText) findViewById(R.id.mitaxiregistermanually_et_infouseremail);
		etInfousertelemergency = (EditText) findViewById(R.id.mitaxiregistermanually_et_telemer);
		etInfousermailemergency = (EditText) findViewById(R.id.mitaxiregistermanually_et_correoemer);

		userfoto = (ImageView) findViewById(R.id.mitaxiregistermanually_im_fotousuario);
		userfoto.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				Uri output = Uri.fromFile(new File(foto));
				intent.putExtra(MediaStore.EXTRA_OUTPUT, output);
				startActivityForResult(intent, 1); // 1 para la camara, 2 para
													// la galeria
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

		etInfousername
				.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfousername, listHasErrorEditText,
						0));
		etInfouseremail
				.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfouseremail,
						listHasErrorEditText, 1));
		etInfousertelemergency
				.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfousertelemergency,
						listHasErrorEditText, 2));
		etInfousermailemergency
				.addTextChangedListener(new EditTextValidator().new CurrencyTextWatcher(
						getBaseContext(), etInfousermailemergency,
						listHasErrorEditText, 3));
		findViewById(R.id.mitaxiregistermanually_btn_ok).setOnClickListener(
				this);
		findViewById(R.id.mitaxiregistermanually_btn_cancel)
				.setOnClickListener(this);
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.mitaxiregistermanually_btn_ok:
			if (!isAnyEditTextEmpty()) {
				if (!hasErrorEditText()) {
					if (NetworkUtils.isNetworkConnectionOk(getBaseContext())) {
						try {
							saveUserInfo();
						} catch (JSONException e) {
							Dialogos.Toast(getApplicationContext(),
									"error fatal :(", Toast.LENGTH_LONG);
						}
					} else {
						Dialogos.Toast(getApplicationContext(),
								getString(R.string.no_internet_connection),
								Toast.LENGTH_LONG);
					}
				} else {
					Dialogos.Toast(getApplicationContext(),
							getString(R.string.edittext_wrong_info),
							Toast.LENGTH_LONG);
				}
			} else {
				Dialogos.Toast(getApplicationContext(),
						getString(R.string.edittext_emtpy), Toast.LENGTH_LONG);
			}
			break;

		case R.id.mitaxiregistermanually_btn_cancel:

			finish();
			break;
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
		user.setTel(etInfousertelemergency.getText().toString());
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
		editor.putString("telemer", user.getTel());
		editor.putString("correoemer", user.getCorreoemergencia());
		editor.putString("uuid", user.getUUID());
		editor.commit();

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

	/**
	 * Metodo privado que genera un codigo unico segun la hora y fecha del
	 * sistema
	 * 
	 * @return photoCode
	 * */
	@SuppressLint("SimpleDateFormat")
	private String getCode() {
		SimpleDateFormat dateFormat = new SimpleDateFormat("yyyymmddhhmmss");
		String date = dateFormat.format(new Date());
		String photoCode = "pic_" + date;
		return photoCode;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == 1) {
			File file = new File(foto);
			if (file.exists()) {
				Bitmap myBitmap = BitmapFactory.decodeFile(file
						.getAbsolutePath());
				Matrix mat = new Matrix();
				mat.postRotate(-90);
				Bitmap bMapRotate = Bitmap.createBitmap(myBitmap, 0, 0,
						myBitmap.getWidth(), myBitmap.getHeight(), mat, true);
				userfoto.setImageBitmap(bMapRotate);
			}

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

		@SuppressWarnings("deprecation")
		@Override
		protected Void doInBackground(String... params) {
			miFoto = (String) params[0];
			try

			{

				System.setProperty("http.keepAlive", "false");
				HttpClient httpclient = new DefaultHttpClient();
				StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
						.permitAll().build();
				StrictMode.setThreadPolicy(policy);
				final HttpParams par = httpclient.getParams();
				HttpConnectionParams.setConnectionTimeout(par, HTTP_TIMEOUT);
				HttpConnectionParams.setSoTimeout(par, HTTP_TIMEOUT);
				ConnManagerParams.setTimeout(par, HTTP_TIMEOUT);
				HttpPost httppost = new HttpPost(
						"http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addpost");
				MultipartEntity entity = new MultipartEntity();
				entity.addPart("nombre", new StringBody(user.getNombre() + ""));
				entity.addPart("correo", new StringBody(user.getCorreo() + ""));
				entity.addPart("telemer", new StringBody(user.getTel() + ""));
				entity.addPart("os", new StringBody(user.getOs() + ""));
				entity.addPart("correoemer",
						new StringBody(user.getCorreoemergencia() + ""));

				File file = new File(miFoto);
				entity.addPart("foto", new FileBody(file));

				System.setProperty("http.keepAlive", "false");
				httppost.setEntity(entity);
				HttpResponse response = httpclient.execute(httppost);
				BufferedReader in = new BufferedReader(new InputStreamReader(
						response.getEntity().getContent()));
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
					String errorJson = "";
					String successsJson = "";
					String pk_user = "";

					JSONObject json = (JSONObject) new JSONTokener(resultado)
							.nextValue();
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
						Dialogos.Toast(getApplicationContext(),"Ya existe el correo",
								Toast.LENGTH_LONG);
					}
				} else {
					Dialogos.Toast(getApplicationContext(),
							getString(R.string.transaction_wrong),
							Toast.LENGTH_LONG);
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
		}

	}
}