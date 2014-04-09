package codigo.labplc.mx.trackxi;

import org.acra.ACRA;
import org.acra.annotation.ReportsCrashes;
import android.app.Application;
import codigo.labplc.mx.trackxi.log.HockeySender;

@ReportsCrashes(formKey = "traxi", formUri ="http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addlog" )
public class Traxi extends Application{

	@Override
	public void onCreate() {
		 	String  envio="http://datos.labplc.mx/~mikesaurio/taxi.php?act=pasajero&type=addlog";
	    	ACRA.init(this);
	    	HockeySender MySender = new HockeySender(envio);
	        ACRA.getErrorReporter().setReportSender(MySender);
		super.onCreate();
	}

}
