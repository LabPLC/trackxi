package codigo.labplc.mx.trackxi.configuracion;

import codigo.labplc.mx.trackxi.R;
import codigo.labplc.mx.trackxi.R.layout;
import codigo.labplc.mx.trackxi.R.menu;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;

public class TrackxiSettingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_trackxi_setting);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.trackxi_setting, menu);
		return true;
	}

}
