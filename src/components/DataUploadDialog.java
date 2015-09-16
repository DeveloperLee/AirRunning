package components;

import com.rondo.airrunning.R;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.widget.TextView;

public class DataUploadDialog extends Dialog {

	public DataUploadDialog(Context context) {
		super(context);
	}
	
	public DataUploadDialog(Context context, int theme){
		super(context,theme);
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	
	public static class Builder{
		
		private Context context;
		private DialogInterface.OnClickListener positive;
		private DialogInterface.OnClickListener negative;
		
		public Builder(Context context){
			this.context = context;
		}
		
		
		public Builder setPosiviteBtnListener(DialogInterface.OnClickListener positive){
			this.positive = positive;
			return this;
		}
		
		public Builder setNegativeBtnListener(DialogInterface.OnClickListener negative){
			this.negative = negative;
			return this;
		}
		
		public DataUploadDialog build(){
			
			LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			
			final DataUploadDialog dialog = new DataUploadDialog(context);
			
			View dialog_view = inflater.inflate(R.layout.layout_dialog,null);
			
			dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
			dialog.setContentView(dialog_view);
			
			
			TextView confirm = (TextView) dialog_view.findViewById(R.id.dialog_confirm);
			confirm.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					positive.onClick(dialog, DialogInterface.BUTTON_POSITIVE);
				}
			});
			
			TextView cancel = (TextView) dialog_view.findViewById(R.id.dialog_cancel);
			cancel.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					negative.onClick(dialog, DialogInterface.BUTTON_NEGATIVE);
				}
			});
			
			return dialog;
		}
	}

}
