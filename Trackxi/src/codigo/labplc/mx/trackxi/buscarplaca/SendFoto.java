package codigo.labplc.mx.trackxi.buscarplaca;


import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.params.ConnManagerParams;
import org.apache.http.entity.mime.HttpMultipartMode;
import org.apache.http.entity.mime.MultipartEntity;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.HttpConnectionParams;
import org.apache.http.params.HttpParams;
import org.apache.http.protocol.BasicHttpContext;
import org.apache.http.protocol.HttpContext;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import codigo.labplc.mx.trackxi.R;

public class SendFoto extends Activity 
{
	   private static ProgressDialog dialog = null;
	/**
	 * 
	 */
	private String foto;
//	 nuevaTareas;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_send_foto);
		Bundle bundle = getIntent().getExtras();
		if(bundle!=null){
			foto = bundle.getString("foto");	

		}
	//	post("http://www.datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=identificaplaca");
       
		 Uploaded nuevaTareas = new Uploaded();
		nuevaTareas.execute(foto);
	}

	public void post(String url) {
	    HttpClient httpClient = new DefaultHttpClient();
	    HttpContext localContext = new BasicHttpContext();
	    HttpPost httpPost = new HttpPost(url);

	    try {
	    	System.setProperty("http.keepAlive", "false");
			StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
			StrictMode.setThreadPolicy(policy);
	        MultipartEntity entity = new MultipartEntity(HttpMultipartMode.BROWSER_COMPATIBLE);

	    	File file = new File(foto);
			entity.addPart("foto", new FileBody(file));

	        httpPost.setEntity(entity);

	        HttpResponse response = httpClient.execute(httpPost, localContext);
	    } catch (IOException e) {
	        e.printStackTrace();
	    }
	}
	/**
	 * clase que envia por post los datos del registro
	 * 
	 * @author mikesaurio
	 * 
	 */

 class Uploaded extends AsyncTask<String, Void, Void> {

		private ProgressDialog pDialog;
		private String miFoto = "";
		private String resultado;
		public static final int HTTP_TIMEOUT = 30 * 1000;
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
				entity.addPart("foto", new FileBody(file));
				System.setProperty("http.keepAlive", "false");
				httppost.setEntity(entity);
				HttpResponse response = null;

				 response = httpclient.execute(httppost, localContext);

			/*	HttpResponse response = httpclient.execute(httppost){
					
					@Override
					public void setAttribute(String id, Object obj) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public Object removeAttribute(String id) {
						// TODO Auto-generated method stub
						return null;
					}
					
					@Override
					public Object getAttribute(String id) {
						// TODO Auto-generated method stub
						return null;
					}
				});*/
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
			pDialog = new ProgressDialog(SendFoto.this);
			pDialog.setMessage("Subiendo la informaci—n, espere.");
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

}
