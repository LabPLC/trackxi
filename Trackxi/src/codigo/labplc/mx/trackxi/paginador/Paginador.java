package codigo.labplc.mx.trackxi.paginador;

import android.app.ActionBar;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;
import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.fonts.fonts;

import com.viewpagerindicator.CirclePageIndicator;

/**
 * 
 * @author amatellanes
 * 
 */
public class Paginador extends FragmentActivity {

	/**
	 * The pager widget, which handles animation and allows swiping horizontally
	 * to access previous and next pages.
	 */
	ViewPager pager = null;

	/**
	 * The pager adapter, which provides the pages to the view pager widget.
	 */
	MyFragmentPagerAdapter pagerAdapter;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		this.setContentView(R.layout.main);

		
	
	     final ActionBar ab = getActionBar();
	     ab.setDisplayShowHomeEnabled(false);
	     ab.setDisplayShowTitleEnabled(false);     
	     final LayoutInflater inflater = (LayoutInflater)getSystemService("layout_inflater");
	     View view = inflater.inflate(R.layout.abs_layout,null);   
	     ((TextView) view.findViewById(R.id.abs_layout_tv_titulo)).setTypeface(new fonts(Paginador.this).getTypeFace(fonts.FLAG_GRIS_CLARO));
	     ab.setDisplayShowCustomEnabled(true);
	     
	     ab.setCustomView(view,new ActionBar.LayoutParams(
	    	        ViewGroup.LayoutParams.MATCH_PARENT,
	    	        ViewGroup.LayoutParams.MATCH_PARENT));
	     ab.setCustomView(view);

	     
	     
		ActionBar.LayoutParams params = new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.MATCH_PARENT, Gravity.CENTER);
		
		// Instantiate a ViewPager
		this.pager = (ViewPager) this.findViewById(R.id.pager);

		// Set a custom animation
		// this.pager.setPageTransformer(true, new ZoomOutPageTransformer());

		// Create an adapter with the fragments we show on the ViewPager
		MyFragmentPagerAdapter adapter = new MyFragmentPagerAdapter(getSupportFragmentManager());
		adapter.addFragment(ScreenSlidePageFragment.newInstance(getResources().getColor(R.color.android_blue), 1,Paginador.this));
		/*adapter.addFragment(ScreenSlidePageFragment.newInstance(getResources()
				.getColor(R.color.android_red), 2,Paginador.this));*/
		this.pager.setAdapter(adapter);

		// Bind the title indicator to the adapter
		CirclePageIndicator titleIndicator = (CirclePageIndicator) findViewById(R.id.indicator);
		titleIndicator.setBackgroundColor(Color.WHITE);
		titleIndicator.setViewPager(pager);

	}

	@Override
	public void onBackPressed() {

		// Return to previous page when we press back button
		if (this.pager.getCurrentItem() == 0)
			super.onBackPressed();
		else
			this.pager.setCurrentItem(this.pager.getCurrentItem() - 1);

	}
	 public void clickEvent(View v) {
	        if (v.getId() == R.id.abs_layout_iv_menu) {
	            showPopup(v);
	        }

	       
	    }
	
	 public void showPopup(View v) {
		    PopupMenu popup = new PopupMenu(Paginador.this, v);
		    MenuInflater inflater = popup.getMenuInflater();
		    inflater.inflate(R.menu.popup, popup.getMenu());
		    popup.show();
		}
}
