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

public class EditBookActivity extends Activity implements OnClickListener {
	private static Button updateButton = null;
	private static Button clearButton = null;

	private static EditText bookName = null;
	private static EditText bookAuthor = null;
	private static EditText bookCreated = null;
	private static EditText bookAbstract = null;
	private static EditText bookContent = null;
	
	private static final String TAG = "InsertNewBookActivity";
	private static Context mContext = null;
	private BookUtils mBookUtils = null;
	private Book mBook = null;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_layout);
		
		mContext = (Context)this;
		mBookUtils = BookUtils.createBookUtils(mContext);
		
		Intent intent = getIntent();
		
		Log.d(TAG, "onCreate()... intent: " + intent);
		
		mBook = (Book)intent.getParcelableExtra(BookUtils.BOOK);
		
		updateButton = (Button)findViewById(R.id.update);
		clearButton = (Button)findViewById(R.id.clear);
		
		bookName = (EditText)findViewById(R.id.bookname);
		bookName.setText(mBook.getBookName());
		
		bookAuthor = (EditText)findViewById(R.id.bookauthor);
		bookAuthor.setText(mBook.getBookAuthor());
		
		bookCreated = (EditText)findViewById(R.id.bookcreated);
		bookCreated.setText(mBook.getPublicTime());
		
		bookAbstract = (EditText)findViewById(R.id.bookabstract);
		bookAbstract.setText(mBook.getBookAbstract());
		
		bookContent = (EditText)findViewById(R.id.bookcontent);
		bookContent.setText(mBook.getBookContent());
		
		updateButton.setOnClickListener(this);
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
			case R.id.update:
				{
					Book book = new Book();
					
					book.setBookId(mBook.getBookId());
					book.setBookName(bookName.getText().toString());
					book.setBookAuthor(bookAuthor.getText().toString());
					book.setPublicTime(bookCreated.getText().toString());
					book.setBookAbstract(bookAbstract.getText().toString());
					book.setBookContent(bookContent.getText().toString());
					
					clearEditText();
					mBookUtils.update(book);
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
