package com.hongbowang.mycontentprovider;

import android.net.Uri;
import android.provider.BaseColumns;

public class BookProviderMetaData {
	//�ⲿ���壺
	// ����AUTHORITY��������manifest�ļ�������android:authorities����
	public static final String AUTHORITY = "com.hongbowang.mycontentprovider.BookProviderMetaData";
	// ����Database������
	public static final String DATABASE_NAME = "book.db";
	// ����Database�İ汾�����ڸ���
	public static final int DATABASE_VERSION = 1;
	// ����Database�еı�
	public static final String BOOK_TABLE_NAME = "books";
	
	public static final class BookTableMetaData implements BaseColumns{
		private BookTableMetaData() {}
		//�������
		public static final String TABLE_NAME = "books";
		//����CONTENT_URI���ڻ�ȡ���ݿ��еı��Բ����˱�
		public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/books");
		// ����MIME type���˴����ڼ�����
		public static final String CONTENT_TYPE = 
				"vnd.android.cursor.dir/vnd.androidbook.book";
		// ����MIME type���˴����ڲ������еĵ�����¼
		public static final String CONTENT_ITEM_TYPE = 
				"vnd.android.cursor.item/vnd.androidbook.book";
		// ����Ĭ�ϵ�����ʽ
		public static final String DEFAULT_SORT_ORDER = "_id DESC";
		
		// ������е�������
		public static final String BOOK_NAME = "name";
		public static final String BOOK_ABSTRACT = "abstract";
		public static final String BOOK_AUTHOR = "author";
		public static final String CREATED_DATE = "created";
		public static final String BOOK_CONTENT = "content";
	}
}