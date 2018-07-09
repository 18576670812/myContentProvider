package com.hongbowang.mycontentprovider;

import java.util.List;
import java.util.Map;

import com.hongbowang.mycontentprovider.BookProviderMetaData.BookTableMetaData;

import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Resources;
import android.database.Cursor;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.view.View;

public class ShowBookContent extends Activity implements View.OnClickListener {
	private TextView content = null;
	private Button mButton = null;
	private static final String TAG = "ContentProviderActivity";
	private static Context mContext = null;
	
	public final static int SHOW_CONTENT = 1;
	public final static int GET_MORE_CONTENT = 2;
	
	private HandlerThread mHandlerThread = null;
	private MyHandler mHandler;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.content_layout);
		
		mContext = (Context)this;
		Intent intent = getIntent();
		Log.d(TAG, "onCreate()... intent: " + intent);
		
		mButton = (Button)findViewById(R.id.querybyauthor);
		content = (TextView)findViewById(R.id.content);
		content.setText(intent.getExtras().getString(BookUtils.CONTENT));
		
		mHandlerThread = new HandlerThread("showcontent");
		mHandlerThread.start();
		
		mHandler = new MyHandler(mHandlerThread.getLooper());
		//String content = intent.getExtras().getString(MyListAdapter.CONTENT);
		//Message msg = mHandler.obtainMessage(SHOW_CONTENT, content);
		
		//msg.sendToTarget();
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onNewIntent()... intent: " + intent);
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onClick()... v: " + v);
		if(v != null) {
			switch(v.getId()) {
			default:
				break;
			}
		}
	}
	
	class MyHandler extends Handler {
		private final String TAG = "MyHandler";
		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			switch(msg.what){
			case SHOW_CONTENT:
				break;
				
			case GET_MORE_CONTENT:
				break;
				
			default:
				break;
			}
		}
		
		public MyHandler() {}
		
		public MyHandler(Looper looper) {
			super(looper);
		}
	};
}
