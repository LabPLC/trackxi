package codigo.labplc.mx.trackxi.buscarplaca.paginador;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.app.AlertDialog;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.buscarplaca.bean.AutoBean;
import codigo.labplc.mx.trackxi.network.NetworkUtils;
import codigo.labplc.mx.trackxi.paginador.MyFragmentPagerAdapter;
import codigo.labplc.mx.trackxi.paginador.Paginador;
import codigo.labplc.mx.trackxi.paginador.ScreenSlidePageFragment;

import com.viewpagerindicator.TitlePageIndicator;

public class DatosAuto extends FragmentActivity{
	
	private AutoBean autoBean;
	private AlertDialog customDialog= null;	//Creamos el dialogo generico
	ViewPager pager = null;
	FragmentPagerAdapterDialog pagerAdapter;
	String placa;
	
	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.dialogo_datos_correctos);
		
		Bundle bundle = getIntent().getExtras();
		placa = bundle.getString("placa");	
		
		
		// Instantiate a ViewPager
		this.pager = (ViewPager) this.findViewById(R.id.pager_dialog);

		// Set a custom animation
		// this.pager.setPageTransformer(true, new ZoomOutPageTransformer());

		// Create an adapter with the fragments we show on the ViewPager
		FragmentPagerAdapterDialog adapter = new FragmentPagerAdapterDialog(
				getSupportFragmentManager());
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources()
				.getColor(R.color.android_blue), 1,DatosAuto.this));
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources()
				.getColor(R.color.android_red), 2,DatosAuto.this));
		adapter.addFragment(ScreenSlidePageFragmentDialog.newInstance(getResources()
				.getColor(R.color.android_darkpink), 3,DatosAuto.this));

		this.pager.setAdapter(adapter);

		// Bind the title indicator to the adapter
		TitlePageIndicator titleIndicator = (TitlePageIndicator) findViewById(R.id.indicator_dialg);
		titleIndicator.setViewPager(pager);
		
		
			//mostrarDAtosConcesion(placa);
		
	}

	@Override
	public void onBackPressed() {

		// Return to previous page when we press back button
		if (this.pager.getCurrentItem() == 0)
			super.onBackPressed();
		else
			this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

	}
}
