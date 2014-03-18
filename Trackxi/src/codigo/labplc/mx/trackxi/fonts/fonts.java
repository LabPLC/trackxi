package codigo.labplc.mx.trackxi.fonts;

import android.app.Activity;
import android.graphics.Typeface;

public class fonts {

	public static final int FLAG_ROJO = 1;
	public static final int FLAG_GRIS_CLARO =2;
	public static final int FLAG_GRIS_OBSCURO = 3;
	public static final int FLAG_AMARILLO = 4;
	public static final int FLAG_MAMEY = 5;
	
	
	private Activity activity;
	
	public fonts(Activity activity){
		this.activity= activity;
	}
	
	public  Typeface  getTypeFace(int tipo){
		Typeface tf= null;
		if(tipo==FLAG_GRIS_CLARO){
		 tf = Typeface.createFromAsset(activity.getAssets(),"fonts/HelveticaNeueLTStd-UltLt.otf");
		}else if(tipo==FLAG_GRIS_OBSCURO){
			 tf = Typeface.createFromAsset(activity.getAssets(),"fonts/HelveticaNeueLTStd-UltLtIt.otf");
		}else if(tipo==FLAG_ROJO){
				 tf = Typeface.createFromAsset(activity.getAssets(),"fonts/HelveticaNeueLTStd-Roman.otf");
		}else if(tipo==FLAG_MAMEY){
					 tf = Typeface.createFromAsset(activity.getAssets(),"fonts/HelveticaNeueLTStd-Bd.otf");
		}else if(tipo==FLAG_AMARILLO){
			 tf = Typeface.createFromAsset(activity.getAssets(),"fonts/HelveticaNeueLTStd-BdIt.otf");
		}
		return tf;
	}
	
}
