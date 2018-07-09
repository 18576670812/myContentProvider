package com.hongbowang.mycontentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public class BookProviderMetaData {
	//外部定义：
	// 定义AUTHORITY：用于在manifest文件中声明android:authorities属性
	public static final String AUTHORITY = "com.hongbowang.mycontentprovider.BookProviderMetaData";
	// 定义Database的名字
	public static final String DATABASE_NAME = "book.db";
	// 定义Database的版本，用于更新
	public static final int DATABASE_VERSION = 1;
	// 定义Database中的表
	public static final String BOOK_TABLE_NAME = "books";
	
	public static final class BookTableMetaData implements BaseColumns{
		private BookTableMetaData() {}
		//定义表名
		public static final String TABLE_NAME = "books";
		//定义CONTENT_URI用于获取数据库中的表，以操作此表
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/books");
		// 定义MIME type，此处用于集操作
		public static final String CONTENT_TYPE = 
				"vnd.android.cursor.dir/vnd.androidbook.book";
		// 定义MIME type，此处用于操作表中的单个记录
		public static final String CONTENT_ITEM_TYPE = 
				"vnd.android.cursor.item/vnd.androidbook.book";
		// 定义默认的排序方式
		public static final String DEFAULT_SORT_ORDER = "_id DESC";
		
		// 定义表中的列属性
		public static final String BOOK_NAME = "name";
		public static final String BOOK_ABSTRACT = "abstract";
		public static final String BOOK_AUTHOR = "author";
		public static final String CREATED_DATE = "created";
		public static final String BOOK_CONTENT = "content";
	}
}