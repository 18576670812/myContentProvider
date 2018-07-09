package com.hongbowang.mycontentprovider;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.hongbowang.mycontentprovider.BookProviderMetaData.BookTableMetaData;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Parcelable;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class MyListAdapter extends ListActivity {	
	private static List<Map<String, Object>> mData = null;
	private MyExtendBaseAdapter mAdapter = null;
	private Context mContext = null;
	
	private final static String TAG = "MyListAdapter";
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		mContext = (Context)this;
		Intent mIntent = getIntent();
		Log.d(TAG, "onCreate(), mIntent=" + mIntent);
		
		mData = getDataFromIntent(mIntent);
		mAdapter = new MyExtendBaseAdapter(this);
		setListAdapter(mAdapter);
	}
	
	private List<Map<String, Object>> getDataFromIntent(Intent intent) {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		Parcelable[] books = (Parcelable[])intent.getParcelableArrayExtra(BookUtils.BOOKS);
		Book book = null;
		
		for(int i=0; i<books.length; i++) {
			if(books[i] instanceof Book){
				map = new HashMap<String, Object>();
				book = (Book)books[i];
				
				map.put(BookUtils._ID, book.getBookId());
				map.put(BookUtils.NAME, book.getBookName());
				map.put(BookUtils.AUTHOR, book.getBookAuthor());
				map.put(BookUtils.CREATED, book.getPublicTime());
				map.put(BookUtils.ABSTRACT, book.getBookAbstract());
				map.put(BookUtils.CONTENT, book.getBookContent());
			
				list.add(map);
			}
		}
		
		return list;
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		// TODO Auto-generated method stub
		Log.d(TAG, "item position:" + position + ", id:" + id);
		ViewHolder holder = (ViewHolder)v.getTag();
		showDialog(holder);
	}
	
	public void showDialog(final ViewHolder holder){
		Resources res = getResources();
		boolean noInfo = true;
		if(holder == null){
			return;
		}
		
		if(noInfo) {
			new AlertDialog.Builder(this)
				.setTitle(res.getString(R.string.operation_confirm))
				.setMessage(res.getString(R.string.confirm_delete))
				.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
					@Override                
					public void onClick(DialogInterface dialog, int which) {
						//delete this book record
						delete((String)holder.bookName.getText());
					}
				})
				.setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
					@Override                
					public void onClick(DialogInterface dialog, int which) {
						Log.d(TAG, "do nothing");
					}
				})
			.show();
		}
	}
	
	void delete(String condition) {
		Log.d(TAG, "delete()... condition: " + condition);
		String where = "name=" + "\'" + condition + "\'";
		getContentResolver().delete(BookTableMetaData.CONTENT_URI, 
									where, 
									null);
	}

	public final class ViewHolder {
		TextView bookName;
		TextView bookAuthor;
		TextView bookCreated;
		TextView bookAbstract;
		Button infoBtn;
		Button editBtn;
	}
	
	public class MyExtendBaseAdapter extends BaseAdapter {
		private LayoutInflater mInflater = null;
		
		public MyExtendBaseAdapter(Context context) {
			this.mInflater = LayoutInflater.from(context);
		}

		@Override
		public int getCount() {
			// TODO Auto-generated method stub
			return mData.size();
		}

		@Override
		public Object getItem(int arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public View getView(int arg0, View arg1, ViewGroup arg2) {
			// TODO Auto-generated method stub
			Resources res = getResources();
			Book book = new Book();
			ViewHolder holder = new ViewHolder();
			int index = arg0;
			View convertView = arg1;
			
			Log.d(TAG, "getView(),index = " + index + ", mData= " + mData);
			if(convertView == null) {
				convertView = mInflater.inflate(R.layout.books_list, null);
				
				holder.bookName = (TextView)convertView.findViewById(R.id.name);
				holder.bookAuthor = (TextView)convertView.findViewById(R.id.author);
				holder.bookCreated = (TextView)convertView.findViewById(R.id.created);
				holder.bookAbstract = (TextView)convertView.findViewById(R.id.bookinfo);
				holder.infoBtn = (Button)convertView.findViewById(R.id.contentbtn);
				holder.editBtn = (Button)convertView.findViewById(R.id.editbtn);
				
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder)convertView.getTag();
			}
						
			holder.bookName.setText(res.getString(R.string.book_name) + ": " 
										+ (String)mData.get(index).get(BookUtils.NAME));
			holder.bookAuthor.setText(res.getString(R.string.book_author) + ": " 
										+ (String)mData.get(index).get(BookUtils.AUTHOR));
			holder.bookCreated.setText(res.getString(R.string.book_created) + ": " 
										+ (String)mData.get(index).get(BookUtils.CREATED));
			holder.bookAbstract.setText((String)mData.get(index).get(BookUtils.ABSTRACT));
			
			{
				book.setBookId((Integer)mData.get(index).get(BookUtils._ID));
				book.setBookAbstract((String)mData.get(index).get(BookUtils.ABSTRACT));
				book.setBookAuthor((String)mData.get(index).get(BookUtils.AUTHOR));
				book.setBookContent((String)mData.get(index).get(BookUtils.CONTENT));
				book.setBookName((String)mData.get(index).get(BookUtils.NAME));
				book.setPublicTime((String)mData.get(index).get(BookUtils.CREATED));
			}
			
			holder.infoBtn.setTag(book.getBookContent());
			holder.infoBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(mContext, ShowBookContent.class);
					
					Bundle bundle = new Bundle();
					bundle.putString(BookUtils.CONTENT,(String)v.getTag());
					
					intent.putExtras(bundle);
					mContext.startActivity(intent);
				}
			});
			
			holder.editBtn.setTag(book);
			holder.editBtn.setOnClickListener(new View.OnClickListener() {
				@Override
				public void onClick(View v) {
					// TODO Auto-generated method stub
					Intent intent = new Intent();
					
					intent.putExtra(BookUtils.BOOK, (Book)v.getTag());
					intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
					intent.setClass(mContext, ShowBookContent.class);

					mContext.startActivity(intent);
				}
			});
			
			return convertView;
		}
		
	}
}