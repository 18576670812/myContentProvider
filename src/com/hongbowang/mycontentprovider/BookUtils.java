package com.hongbowang.mycontentprovider;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;
import android.widget.Toast;
import android.database.Cursor;
import com.hongbowang.mycontentprovider.BookProviderMetaData.BookTableMetaData;

public class BookUtils {
	public final static String BOOKS = "books";
	public final static String BOOK = "book";
	
	public final static String _ID = "id";
	public final static String NAME = "name";
	public final static String AUTHOR = "author";
	public final static String CREATED = "created";
	public final static String CONTENT = "content";
	public final static String ABSTRACT = "abstract";
	
	private final String TAG = "BookUtils";
	private Context mContext = null;
	private static BookUtils mInstance = null;
	private static final String[] projection = {
		BookTableMetaData._ID,
		BookTableMetaData.BOOK_NAME, 
		BookTableMetaData.BOOK_AUTHOR,
		BookTableMetaData.BOOK_ABSTRACT,
		BookTableMetaData.CREATED_DATE,
		BookTableMetaData.BOOK_CONTENT,
	};
	
	private BookUtils(Context context) {
		mContext = context;
	}
	
	public static BookUtils createBookUtils(Context context){
		if(mInstance != null) {
			return mInstance;
		} else {
			mInstance = new BookUtils(context);
		}
		
		return mInstance;
	}
	
	void insert(Book book) {
		
		ContentValues contentValues = new ContentValues();
		
		contentValues.put(BookTableMetaData.BOOK_NAME, book.getBookName());
		contentValues.put(BookTableMetaData.BOOK_AUTHOR, book.getBookAuthor());
		contentValues.put(BookTableMetaData.CREATED_DATE, book.getPublicTime());
		contentValues.put(BookTableMetaData.BOOK_ABSTRACT, book.getBookAbstract());
		contentValues.put(BookTableMetaData.BOOK_CONTENT, book.getBookContent());
		
		Uri uri = mContext.getContentResolver().insert(BookTableMetaData.CONTENT_URI, contentValues);
		if(uri != null) {
			Toast.makeText(mContext, uri.toString(), Toast.LENGTH_LONG).show();
		}
	}
	
	/* this interface allow user to query book by author or book name, 
	 * if author and book name isn't specified, it's equal to query() */
	Book[] query(String condition, boolean isAuthor) {
		Log.d(TAG, "query()...");
		Cursor c = null;
		String[] selectionArgs = null;
		String selection = null;
		Integer index = 0;
		
		if(condition == null || TextUtils.isEmpty(condition)) {
			Log.w(TAG, "condition is null or empty");
			return null;
		} else if(isAuthor) {
			selection = "author=" + "\'" + condition + "\'";
		} else {
			selection = "name=" + "\'" + condition + "\'";
		}
		
		c = mContext.getContentResolver().query(BookTableMetaData.CONTENT_URI, 
										projection, 
										selection, 
										selectionArgs, 
										BookTableMetaData.DEFAULT_SORT_ORDER);
		
		Log.d(TAG, "c.getCount() = " + c.getCount());
		Book[] books = new Book[c.getCount()];
		
		while(c != null && c.moveToNext()) {
			books[index] = new Book();
			
			books[index].setBookId(c.getInt(c.getColumnIndex(BookTableMetaData._ID)));
			books[index].setBookName(c.getString(c.getColumnIndex(BookTableMetaData.BOOK_NAME)));
			books[index].setBookAuthor(c.getString(c.getColumnIndex(BookTableMetaData.BOOK_AUTHOR)));
			books[index].setPublicTime(c.getString(c.getColumnIndex(BookTableMetaData.CREATED_DATE)));
			books[index].setBookAbstract(c.getString(c.getColumnIndex(BookTableMetaData.BOOK_ABSTRACT)));
			books[index].setBookContent(c.getString(c.getColumnIndex(BookTableMetaData.BOOK_CONTENT)));
			
			index++;
		}
		
		c.close();
		
		return books;
	}
	
	/* query all book record */
	Book[] query() {
		int index = 0;
		Cursor c = mContext.getContentResolver().query(BookTableMetaData.CONTENT_URI, 
										projection, 
										null, 
										null, 
										BookTableMetaData.DEFAULT_SORT_ORDER);
		
		Log.d(TAG, "c.getCount() = " + c.getCount());
		Book[] books = new Book[c.getCount()];
		
		while(c != null && c.moveToNext()) {
			books[index] = new Book();
			
			books[index].setBookName(c.getString(c.getColumnIndex(BookTableMetaData.BOOK_NAME)));
			books[index].setBookAuthor(c.getString(c.getColumnIndex(BookTableMetaData.BOOK_AUTHOR)));
			books[index].setPublicTime(c.getString(c.getColumnIndex(BookTableMetaData.CREATED_DATE)));
			books[index].setBookAbstract(c.getString(c.getColumnIndex(BookTableMetaData.BOOK_ABSTRACT)));
			books[index].setBookContent(c.getString(c.getColumnIndex(BookTableMetaData.BOOK_CONTENT)));
			
			index++;
		}
		
		c.close();
		
		return books;
	}
	
	int update(Book book) {
		Log.d(TAG, "update()... book= " + book);
		String where = BookTableMetaData._ID + "=" + book.getBookId().toString();
		ContentValues contentValues = new ContentValues();
		
		if(!TextUtils.isEmpty(book.getBookName())) {
			contentValues.put(BookTableMetaData.BOOK_NAME, book.getBookName());
		}
		
		if(!TextUtils.isEmpty(book.getBookAuthor())){
			contentValues.put(BookTableMetaData.BOOK_AUTHOR, book.getBookAuthor());
		}
		
		if(!TextUtils.isEmpty(book.getBookAbstract())){
			contentValues.put(BookTableMetaData.BOOK_ABSTRACT, book.getBookAbstract());
		}
		
		if(!TextUtils.isEmpty(book.getPublicTime())){
			contentValues.put(BookTableMetaData.CREATED_DATE, book.getPublicTime());
		}
		
		if(!TextUtils.isEmpty(book.getBookContent())){
			contentValues.put(BookTableMetaData.BOOK_CONTENT, book.getBookContent());
		}
		
		return mContext.getContentResolver().update(BookTableMetaData.CONTENT_URI, 
													contentValues, 
													where, 
													null);
	}
	
	void delete(String condition, boolean isAuthor) {
		Log.d(TAG, "delete()... condition: " + condition + ", isAuthor: " + isAuthor);
		if(condition == null || TextUtils.isEmpty(condition)) {
			mContext.getContentResolver().delete(BookTableMetaData.CONTENT_URI, 
					null, null);
		} else if(isAuthor) {
			String where = "name=" + "\'" + condition + "\'";
			mContext.getContentResolver().delete(BookTableMetaData.CONTENT_URI, 
					where, 
					null);
		} else {
			String where = "author=" + "\'" + condition + "\'";
			mContext.getContentResolver().delete(BookTableMetaData.CONTENT_URI, 
					where, 
					null);
		}
	}
	
	void deleteAll(){
		Log.d(TAG, "deleteAll()...");
		mContext.getContentResolver().delete(BookTableMetaData.CONTENT_URI, null, null);
	}
}