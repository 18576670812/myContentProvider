package com.hongbowang.mycontentprovider;

import java.util.List;
import java.util.Map;

import com.hongbowang.mycontentprovider.BookProviderMetaData.BookTableMetaData;

import android.net.Uri;
import android.os.Bundle;
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

public class InsertNewBookActivity extends Activity implements OnClickListener {
	private static Button insertButton = null;
	private static Button clearButton = null;

	private static EditText bookName = null;
	private static EditText bookAuthor = null;
	private static EditText bookCreated = null;
	private static EditText bookAbstract = null;
	private static EditText bookContent = null;
	
	private BookUtils mBookUtils = null;
	
	private static final String TAG = "InsertNewBookActivity";
	private static Context mContext = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.insert_layout);
		
		mContext = (Context)this;
		mBookUtils = BookUtils.createBookUtils(mContext);
		
		Intent intent = getIntent();
		Log.d(TAG, "onCreate()... intent: " + intent);
		
		insertButton = (Button)findViewById(R.id.insert);
		clearButton = (Button)findViewById(R.id.clear);
		
		bookName = (EditText)findViewById(R.id.bookname);
		bookAuthor = (EditText)findViewById(R.id.bookauthor);
		bookCreated = (EditText)findViewById(R.id.bookcreated);
		bookAbstract = (EditText)findViewById(R.id.bookabstract);
		bookContent = (EditText)findViewById(R.id.bookcontent);
				
		insertButton.setOnClickListener(this);
		clearButton.setOnClickListener(this);
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
			case R.id.insert:
				{
					Book book = new Book();
					Uri uri = null;
					
					book.setBookName(bookName.getText().toString());
					book.setBookAuthor(bookAuthor.getText().toString());
					book.setPublicTime(bookCreated.getText().toString());
					book.setBookAbstract(bookAbstract.getText().toString());
					book.setBookContent(bookContent.getText().toString());
					
					clearEditText();
					mBookUtils.insert(book);
				}
				break;
				
			case R.id.clear:
				clearEditText();
				break;
				
			default:
				break;
			}
		}
	}
	
	void clearEditText() {
		if(bookName != null){
			bookName.setText("");
		}
		if(bookAuthor != null){
			bookAuthor.setText("");
		}
		if(bookCreated != null){
			bookCreated.setText("");
		}
		if(bookAbstract != null){
			bookAbstract.setText("");
		}
		if(bookContent != null){
			bookContent.setText("");
		}
	}
}
