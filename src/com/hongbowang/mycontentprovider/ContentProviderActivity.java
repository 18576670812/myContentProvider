package com.hongbowang.mycontentprovider;

import java.util.List;
import java.util.Map;
import com.hongbowang.mycontentprovider.BookProviderMetaData.BookTableMetaData;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.UserHandle;
import android.os.UserManager;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
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
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import android.view.View;

@TargetApi(Build.VERSION_CODES.JELLY_BEAN)
@SuppressLint("NewApi")
public class ContentProviderActivity extends Activity implements OnClickListener {
	private static Button insertButton = null;
	private static Button deleteButton = null;
	private static Button deleteAllButton = null;
	private static Button queryByAuthor = null;
	private static Button queryByName = null;
	
	private static EditText bookName = null;
	private static EditText bookAuthor = null;

	private static final String TAG = "ContentProviderActivity";
	private static Context mContext = null;
	
	private BookUtils mBookUtils = null;
	
	private static LayoutInflater mInflater = null;
	private List<Map<String, Object>> mData = null;
	
	private static final String RECEIVER_FILTER_ACTION = 
			"com.hongbowang.mycontentprovider.MESSAGE_SENT_OVER";
	
	static final int CALL_FORWARD_NOTIFICATION_1 = 0x04;
	static final int CALL_FORWARD_NOTIFICATION_2 = 0x14;		
	
	private final String ACTION_DECLINE_INCOMING_CALL = "Decline";
	private final String ACTION_ANSWER_VOICE_INCOMING_CALL = "Answer";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		
		mContext = (Context)this;
		mBookUtils = BookUtils.createBookUtils(mContext);
		
		Intent intent = getIntent();
		Log.d(TAG, "onCreate()... intent: " + intent);
		
		insertButton = (Button)findViewById(R.id.insert);
		deleteButton = (Button)findViewById(R.id.delete);
		deleteAllButton = (Button)findViewById(R.id.deleteall);
		queryByAuthor = (Button)findViewById(R.id.querybyauthor);
		queryByName = (Button)findViewById(R.id.querybyname);
		
		bookName = (EditText)findViewById(R.id.bookname);
		bookAuthor = (EditText)findViewById(R.id.bookauthor);
		
		insertButton.setOnClickListener(this);
		deleteButton.setOnClickListener(this);
		deleteAllButton.setOnClickListener(this);
		queryByAuthor.setOnClickListener(this);
		queryByName.setOnClickListener(this);
		
		// register broadcast receiver
		IntentFilter mIntentFilter = new IntentFilter(RECEIVER_FILTER_ACTION);
		registerBroadcastReceiver(mReceiver, mIntentFilter);
		
	    NotificationManager mNotificationManager = 
	    		(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
	    for(int i=0; i<1; i++){
			Notification.Builder builder = new Notification.Builder(mContext);
			if(i == 0){
			    builder.setSmallIcon(R.drawable.stat_sys_phone_call_forward);
			} else {
				builder.setSmallIcon(R.drawable.stat_sys_phone_call_on_hold);
			}
            builder.setContentTitle(mContext.getString(R.string.labelCF));
            builder.setContentText(mContext.getString(R.string.sum_cfu_enabled_indicator));
            builder.setShowWhen(false);
            builder.setAutoCancel(true);
			
			Intent in = new Intent(Intent.ACTION_MAIN);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.setClassName("com.android.phone", "com.android.phone.GsmUmtsCallForwardOptions");
            // Put the subId into intent if there is only one sub enabled
            // this, or else no need put this subId and let user select sub.
            intent.putExtra("subscription", i);
            
            PendingIntent contentIntent = PendingIntent.getActivity(
                    mContext, 0, in, PendingIntent.FLAG_UPDATE_CURRENT);
            builder.setContentIntent(contentIntent);
            Notification nfy = builder.build();
            nfy.flags |= Notification.FLAG_SHOW_LIGHTS;
            nfy.defaults |= Notification.DEFAULT_LIGHTS;
            nfy.ledARGB = 0xff00ff00;
            nfy.ledOffMS = 300;
            nfy.ledOnMS = 300;
            nfy.vibrate = new long[]{300,300,300,300};
            if(i == 0) {
                mNotificationManager.notify(
                       CALL_FORWARD_NOTIFICATION_1, nfy);
            } else {
            	mNotificationManager.notify(
                        CALL_FORWARD_NOTIFICATION_2, nfy);
            }
		}
	    
	    Intent intent1 = intent;
	    Intent intent2 = intent;
	    Log.d(TAG, "onNewIntent()... intent1 == intent2? " + (intent1 == intent2));
	}

	@Override
	protected void onNewIntent(Intent intent) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onNewIntent()... intent: " + intent);
	}
	
	BroadcastReceiver mReceiver = new ContentBroadcastReceiver();
	public static class ContentBroadcastReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context arg0, Intent intent) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onReceive()... intent: " + intent);
		}
	};
	
	void registerBroadcastReceiver(BroadcastReceiver receiver, IntentFilter filter) {
		if(receiver != null) {
			registerReceiver(receiver, filter);
		}
	}

	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		Log.d(TAG, "onClick()... v: " + v);
		if(v != null) {
			switch(v.getId()) {
			case R.id.insert:
/*
				//Intent intent = new Intent(Intent.ACTION_DIAL);
				//intent.setData(Uri.fromParts("tel", "*#111#", null));
				//Intent intent = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:*#111#"));
				//mContext.startActivity(intent);
*/
				Intent intent = new Intent();
				intent.setClass(mContext, InsertNewBookActivity.class);
				intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				mContext.sendBroadcast(intent);

			    NotificationManager mNotificationManager = 
			    		(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
			    for(int i=0; i<1; i++){
					Notification.Builder builder = new Notification.Builder(mContext);
					if(i == 0){
					    builder.setSmallIcon(R.drawable.stat_sys_phone_call_forward);
					} else {
						builder.setSmallIcon(R.drawable.stat_sys_phone_call_on_hold);
					}
                    builder.setContentTitle(mContext.getString(R.string.labelCF));
                    builder.setContentText(mContext.getString(R.string.sum_cfu_enabled_indicator));
                    builder.setShowWhen(false);
                    builder.setOngoing(true);
					
					//Intent intent = new Intent(Intent.ACTION_MAIN);
		            //intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		            //intent.setClassName("com.android.phone", "com.android.phone.GsmUmtsCallForwardOptions");
		            // Put the subId into intent if there is only one sub enabled
		            // this, or else no need put this subId and let user select sub.
		            //intent.putExtra("subscription", i);
		            
	                PendingIntent contentIntent = PendingIntent.getActivity(
	                        mContext, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
	                builder.setContentIntent(contentIntent);
	                Notification nfy = builder.build();
	                nfy.flags |= Notification.FLAG_SHOW_LIGHTS;
	                nfy.defaults |= Notification.DEFAULT_LIGHTS;
	                nfy.ledARGB = 0xff00ff00;
	                nfy.ledOffMS = 500;
	                nfy.ledOnMS = 500;
	                if(i == 0) {
	                    mNotificationManager.notify(
	                           CALL_FORWARD_NOTIFICATION_1, nfy);
	                } else {
	                	mNotificationManager.notify(
	                            CALL_FORWARD_NOTIFICATION_2, nfy);
	                }
				}

/*
				for(int i=0; i < 50; i++)
				{
					// 测试通过Notification来启动应用程序
					Notification.Builder builder = new Notification.Builder(mContext);
					builder.setSmallIcon(R.drawable.stat_sys_phone_call_forward);
					builder.setContentTitle(mContext.getString(R.string.labelCF));
                    builder.setContentText(mContext.getString(R.string.sum_cfu_enabled_indicator));
                    builder.setShowWhen(false);
                    builder.setOngoing(true);
                    
					Intent intent = new Intent(Intent.ACTION_DIAL);
					intent.setData(Uri.fromParts("tel", "112", null));
					
					PendingIntent pendingIntent = PendingIntent.getActivity(mContext, 31, intent, PendingIntent.FLAG_UPDATE_CURRENT);
					builder.setContentIntent(pendingIntent);
					builder.setFullScreenIntent(pendingIntent, true);
					//builder.setPriority(Notification.PRIORITY_MAX);
					addAnswerAction(builder);
					addDismissAction(builder);
					Notification notification = builder.build();
					NotificationManager nfm = (NotificationManager) 
							mContext.getSystemService(Context.NOTIFICATION_SERVICE);
					nfm.notify(CALL_FORWARD_NOTIFICATION_1+i, notification);
					
					try{
						Thread.sleep(20);
					} catch (Exception e) {
						// do nothing;
					}
				}
*/
/*
				Uri uri = Uri.fromParts("tel", "888", null);
				Log.d(TAG, "uri = " + uri + "uri.getSchemeSpecificPart= " + uri.getSchemeSpecificPart());
			    String mId = "1";
			    Long id = new Long(mId);
			    Log.d(TAG, "id = " + id);
			    if(id != null) {
			    	Log.d(TAG, "subId = " + id.longValue());
			    } else {
			    	Log.d(TAG, "id == null !!!!");
			    }
*/
				break;
				
			case R.id.delete:
				{
					String bName = bookName.getText().toString();
					String bAuthor = bookAuthor.getText().toString();
					
					clearEditText();
					
					if(TextUtils.isEmpty(bName) && TextUtils.isEmpty(bAuthor)) {
						Toast.makeText(mContext, "No book name and No Author", Toast.LENGTH_LONG).show();
					} else if(!TextUtils.isEmpty(bName)) {
						mBookUtils.delete(bName, false);
					} else {
						mBookUtils.delete(bAuthor, true);
					}
					for(int i=0; i < 50; i++)
					{
						NotificationManager nfm = (NotificationManager) 
								mContext.getSystemService(Context.NOTIFICATION_SERVICE);
						nfm.cancel(CALL_FORWARD_NOTIFICATION_1+i);
					}
				}
				break;
				
			case R.id.deleteall:
				mBookUtils.deleteAll();
				break;
				
			case R.id.querybyname:
				{
					String bName = bookName.getText().toString();
					Book[] books = null;
					
					clearEditText();
					
					if(TextUtils.isEmpty(bName)) {
						books = mBookUtils.query();
					} else {
						books = mBookUtils.query(bName, false);
					}
					
					if(books.length <= 0) {
						Resources res=mContext.getResources(); 
						String str = res.getString(R.string.nomatchedprompt);
						Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();
					} else {
						sendQueryResult(books);
					}
				}
				break;
				
			case R.id.querybyauthor:
				{
					String bAuthor = bookAuthor.getText().toString();
					Book[] books = null;
					
					clearEditText();
					
					if(TextUtils.isEmpty(bAuthor)) {
						books = mBookUtils.query();
					} else if(!TextUtils.isEmpty(bAuthor)) {
						books = mBookUtils.query(bAuthor, true);
					}
					
					if(books.length <= 0) {
						Resources res=mContext.getResources(); 
						String str = res.getString(R.string.nomatchedprompt);
						Toast.makeText(mContext, str, Toast.LENGTH_LONG).show();
					} else {
						sendQueryResult(books);
					}
				}
				break;
				
			default:
				break;
			}
		}
	}
	
	void sendQueryResult(Book[] books){
		Intent intent = new Intent();
		intent.putExtra(BookUtils.BOOKS, books);
		intent.setClass(mContext, MyListAdapter.class);
		intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
		mContext.startActivity(intent);
	}
	
    private PendingIntent createNotificationPendingIntent(Context context, String action) {
        final Intent intent = new Intent(action, null,
                context, NotificationBroadcastReceiver.class);
        return PendingIntent.getBroadcast(context, 0, intent, 0);
    }

    private void addAnswerAction(Notification.Builder builder) {
        Log.i(TAG, "Will show \"answer\" action in the incoming call Notification");

        PendingIntent answerVoicePendingIntent = createNotificationPendingIntent(
                mContext, ACTION_ANSWER_VOICE_INCOMING_CALL);
        builder.addAction(R.drawable.stat_sys_phone_call_forward,
                mContext.getText(R.string.description_target_answer),
                answerVoicePendingIntent);
    }

    private void addDismissAction(Notification.Builder builder) {
        Log.i(TAG, "Will show \"dismiss\" action in the incoming call Notification");

        PendingIntent declinePendingIntent =
                createNotificationPendingIntent(mContext, ACTION_DECLINE_INCOMING_CALL);
        builder.addAction(R.drawable.stat_sys_phone_call_on_hold,
                mContext.getText(R.string.notification_action_dismiss),
                declinePendingIntent);
    }
    
    public static class NotificationBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            Log.i(TAG, "Broadcast from Notification: " + action);
        }
    }
    
	void clearEditText() {
		if(bookName != null){
			bookName.setText("");
		}
		if(bookAuthor != null){
			bookAuthor.setText("");
		}
	}

	@Override
	protected void onDestroy() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onDestroy()...");
		super.onDestroy();
	}

	@Override
	protected void onPause() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onPause()...");
		super.onPause();
	}

	@Override
	protected void onRestart() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onRestart()...");
		super.onRestart();
	}

	@Override
	protected void onResume() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onResume()...");
		try {
			Thread.sleep(1);
		} catch (Exception e) {
			// do nothing;
		}
		super.onResume();
	}

	@Override
	protected void onStart() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStart()...");
		super.onStart();
	}

	@Override
	protected void onStop() {
		// TODO Auto-generated method stub
		Log.d(TAG, "onStop()...");
		super.onStop();
	}

	@SuppressLint("NewApi")
	@Override
	public void recreate() {
		// TODO Auto-generated method stub
		Log.d(TAG, "recreate()...");
		super.recreate();
	}
	
}
