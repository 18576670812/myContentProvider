package com.hongbowang.mycontentprovider;

import java.util.HashMap;
import com.hongbowang.mycontentprovider.BookProviderMetaData.BookTableMetaData;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

public class BookProvider extends ContentProvider {
	private static final String TAG = "BookProvider";
	// 定义projectoin map，为表的列属性取别名。
	private static HashMap<String, String> sBooksProjectionMap;
	static {
		sBooksProjectionMap = new HashMap<String, String>();
		sBooksProjectionMap.put(BookTableMetaData._ID, 
								BookTableMetaData._ID);
		sBooksProjectionMap.put(BookTableMetaData.BOOK_NAME, 
								BookTableMetaData.BOOK_NAME);
		sBooksProjectionMap.put(BookTableMetaData.BOOK_ABSTRACT, 
								BookTableMetaData.BOOK_ABSTRACT);
		sBooksProjectionMap.put(BookTableMetaData.BOOK_AUTHOR, 
								BookTableMetaData.BOOK_AUTHOR);
		sBooksProjectionMap.put(BookTableMetaData.CREATED_DATE, 
								BookTableMetaData.CREATED_DATE);
		sBooksProjectionMap.put(BookTableMetaData.BOOK_CONTENT, 
								BookTableMetaData.BOOK_CONTENT);
	}
	
	private static final UriMatcher sUriMatcher;
	private static final int INCOMING_BOOK_COLLECTION_URI_INDICATOR = 1;
	private static final int INCOMING_SINGLE_BOOK_URI_INDICATOR = 2;
	//定义UriMatcher：用于判断操作的uri是否为我们的contentprovider所支持。
	static {
		sUriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		sUriMatcher.addURI(BookProviderMetaData.AUTHORITY, 
				BookProviderMetaData.BOOK_TABLE_NAME, 
				INCOMING_BOOK_COLLECTION_URI_INDICATOR);
		sUriMatcher.addURI(BookProviderMetaData.AUTHORITY, 
				BookProviderMetaData.BOOK_TABLE_NAME + "/#", 
				INCOMING_SINGLE_BOOK_URI_INDICATOR);
	}
	
	// 定义一个数据库操作的帮助类，以便更容易的操作数据库
	private static class DatabaseHelper extends SQLiteOpenHelper {
		private static final String TAG = "DatabaseHelper";
		private static final String createTableSentence = "CREATE TABLE " + BookTableMetaData.TABLE_NAME
				+ " (" + BookTableMetaData._ID + " INTEGER PRIMARY KEY, "
				+ BookTableMetaData.BOOK_NAME + " TEXT, "
				+ BookTableMetaData.BOOK_AUTHOR + " TEXT, "
				+ BookTableMetaData.BOOK_ABSTRACT + " TEXT, "
				+ BookTableMetaData.CREATED_DATE + " TEXT, "
				+ BookTableMetaData.BOOK_CONTENT + " TEXT"
				+ ");";
		// 定义databasehelper的构造函数，必须有该构造函数
		DatabaseHelper (Context context) {
			this(context, BookProviderMetaData.DATABASE_NAME);
			Log.d(TAG, "DatabaseHelper constructor");
		}
		
		DatabaseHelper(Context context, String databaseName, int databaseVersion) {
			super(context, databaseName, null, databaseVersion);
			Log.d(TAG, "DatabaseHelper constructor");
		}
		
		DatabaseHelper(Context context, String databaseName) {
			this(context, databaseName, BookProviderMetaData.DATABASE_VERSION);
			Log.d(TAG, "DatabaseHelper constructor");
		}

		/* 重写或者覆盖SQLiteOpenHelper的oncreate函数，创建数据库中的表.
		 * 该函数是第一次创建数据库的时候执行，实际上是在第一次得到SQLiteDatabase对象的时候
		 * 才会调用这个方法
		 * */
		@Override
		public void onCreate(SQLiteDatabase db) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onCreate()");
			db.execSQL(createTableSentence);
		}
		
		// 重写或者覆盖SQLiteOpenHelper的onUpgrade函数，用于升级数据库的版本
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			// TODO Auto-generated method stub
			Log.d(TAG, "onUpgrade()");
			// 如果表存在，先做一个删除操作，然后再创建
			db.execSQL("DROP TABLE IF EXIST " + BookTableMetaData.TABLE_NAME);
			onCreate(db);
		}
	}
	
	private DatabaseHelper mOpenHelper;

	/* 我们自己定义的contentprovider需要重写contentprovider的delete函数
	*	uri：database的CONTENT_URI
	*	where：如果为数据库操作的where子句，如BookTableMetaData.BOOK_NAME="Celine"
	**/
	@Override
	public int delete(Uri uri, String where, String[] whereArgs) {
		// TODO Auto-generated method stub
		Log.d(TAG, "delete() uri: " + uri);
		Log.d(TAG, "where: " + where);
		// 通过database的帮助类获取可写的数据库
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		// 判断当前的uri是否为我们所支持
		switch(sUriMatcher.match(uri)) {
		case INCOMING_BOOK_COLLECTION_URI_INDICATOR:
			count = db.delete(BookTableMetaData.TABLE_NAME,
					where, whereArgs);
			break;
			
		case INCOMING_SINGLE_BOOK_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			Log.d(TAG, "rowId: " + rowId);
			count = db.delete(BookTableMetaData.TABLE_NAME, 
					BookTableMetaData._ID + "=" + rowId + (!TextUtils.isEmpty(where)? " AND (" + where + ')' : ""), 
					whereArgs);
			break;
			
		default:
			throw new IllegalArgumentException("unknown uri");
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		Log.d(TAG, "count: " + count);
		return count;
	}

	@Override
	public String getType(Uri uri) {
		// TODO Auto-generated method stub
		Log.d(TAG, "getType() uri: " + uri);
		switch(sUriMatcher.match(uri)) {
		case INCOMING_BOOK_COLLECTION_URI_INDICATOR:
			return BookTableMetaData.CONTENT_TYPE;
			
		case INCOMING_SINGLE_BOOK_URI_INDICATOR:
			return BookTableMetaData.CONTENT_ITEM_TYPE;
			
		default:
			throw new IllegalArgumentException("unknown uri");
		}
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		// TODO Auto-generated method stub
		Log.d(TAG, "insert() uri: " + uri + ", values: " + values);
		if(sUriMatcher.match(uri) != INCOMING_BOOK_COLLECTION_URI_INDICATOR) {
			throw new IllegalArgumentException("unknown uri");
		}
		
		ContentValues val;
		if(values != null) {
			val = new ContentValues(values);
		} else {
			val = new ContentValues();
		}
		
		long now = Long.valueOf(System.currentTimeMillis());
		if(val.containsKey(BookTableMetaData.CREATED_DATE) == false) {
			val.put(BookTableMetaData.CREATED_DATE, now);
		}
		
		if(val.containsKey(BookTableMetaData.BOOK_CONTENT) == false) {
			val.put(BookTableMetaData.BOOK_CONTENT, "No Content");
		}
		
		if(val.containsKey(BookTableMetaData.BOOK_NAME) == false) {
			throw new SQLException("failed to insert row because no book name");
		}
		
		if(val.containsKey(BookTableMetaData.BOOK_ABSTRACT) == false) {
			val.put(BookTableMetaData.BOOK_ABSTRACT, "No Abstract");
		}
		
		if(val.containsKey(BookTableMetaData.BOOK_AUTHOR) == false) {
			val.put(BookTableMetaData.BOOK_AUTHOR, "unknown author");
		}
		
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		Long rowId = db.insert(BookTableMetaData.TABLE_NAME,
				BookTableMetaData.BOOK_NAME, val);
		Log.d(TAG, "rowId: " + rowId);
		if(rowId > 0) {
			Uri insertedBookUri = ContentUris.withAppendedId(BookTableMetaData.CONTENT_URI, rowId);
			Log.d(TAG, "insertedBookUri: " + insertedBookUri);
			getContext().getContentResolver().notifyChange(insertedBookUri, null);
			return insertedBookUri;
		}
		
		throw new SQLException("Failed to insert row into " + uri);
	}

	@Override
	public boolean onCreate() {
		// TODO Auto-generated method stub
		mOpenHelper = new DatabaseHelper(getContext());
		Log.d(TAG, "onCreate() mOpenHelper: " + mOpenHelper);
		if(mOpenHelper != null) {
			return true;
		} else {
			return false;
		}
	}

	/*
	 * uri: CONTENT_URI
	 * projection: 查询哪些列属性
	 * selection：where子句
	 * selectionArgs：为前面where子句的占位符所对应的参数
	 * sortOrder：查询结果的排序方式
	 * */
	@Override
	public Cursor query(Uri uri, String[] projection, String selection,
			String[] selectionArgs, String sortOrder) {
		// TODO Auto-generated method stub
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		Log.d(TAG, "query() uri: " + uri + ", qb: " + qb);
		Log.d(TAG, "selection: " + selection);
		Log.d(TAG, "sUriMatcher.match(uri): " + sUriMatcher.match(uri));
		switch(sUriMatcher.match(uri)) {
		case INCOMING_BOOK_COLLECTION_URI_INDICATOR:
			qb.setTables(BookTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sBooksProjectionMap);
			break;
			
		case INCOMING_SINGLE_BOOK_URI_INDICATOR:
			qb.setTables(BookTableMetaData.TABLE_NAME);
			qb.setProjectionMap(sBooksProjectionMap);
			qb.appendWhere(BookTableMetaData._ID + "=" 
							+ uri.getPathSegments().get(1));
			break;
			
		default:
			throw new IllegalArgumentException("unknown uri");
		}
		
		String orderBy;
		if(TextUtils.isEmpty(sortOrder)) {
			orderBy = BookTableMetaData.DEFAULT_SORT_ORDER;
		} else {
			orderBy = sortOrder;
		}
		
		SQLiteDatabase db = mOpenHelper.getReadableDatabase();
		Cursor c = qb.query(db, projection, selection, selectionArgs, null, null, orderBy);
		Log.d(TAG, "cursor: " + c);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection,
			String[] selectionArgs) {
		// TODO Auto-generated method stub
		SQLiteDatabase db = mOpenHelper.getWritableDatabase();
		int count = 0;
		Log.d(TAG, "update() uri: " + uri + ", db: " + db);
		switch(sUriMatcher.match(uri)) {
		case INCOMING_BOOK_COLLECTION_URI_INDICATOR:
			count = db.update(BookTableMetaData.TABLE_NAME, values, selection, selectionArgs);
			break;
			
		case INCOMING_SINGLE_BOOK_URI_INDICATOR:
			String rowId = uri.getPathSegments().get(1);
			count = db.update(BookTableMetaData.TABLE_NAME, 
					values, 
					BookTableMetaData._ID + "=" + rowId + (!TextUtils.isEmpty(selection)? " AND (" + selection + ')' : ""), 
					selectionArgs);
			break;
			
		default:
			throw new IllegalArgumentException("unknown uri");
		}
		
		getContext().getContentResolver().notifyChange(uri, null);
		Log.d(TAG, "count: " + count);
		return count;
	}
}