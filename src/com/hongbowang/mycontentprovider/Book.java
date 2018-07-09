package com.hongbowang.mycontentprovider;

import android.os.Parcel;
import android.os.Parcelable;

public class Book implements Parcelable {
	private String bookName = null;
	private String bookAuthor = null;
	private String bookAbstract = null;
	private String bookContent = null;
	private String publicTime = null;
	private Integer bookId = -1; // this is used to save book's uri in content provider
	
	String getBookName(){
		return bookName;
	}
	
	void setBookName(String name){
		if(name != null) {
			bookName = name;
		}
	}
	
	String getBookAuthor(){
		return bookAuthor;
	}
	
	void setBookAuthor(String author){
		if(author != null) {
			bookAuthor = author;
		}
	}
	
	String getPublicTime(){
		return publicTime;
	}
	
	void setPublicTime(String publicTime){
		if(publicTime != null) {
			this.publicTime = publicTime;
		}
	}
	
	String getBookAbstract(){
		return bookAbstract;
	}
	
	void setBookAbstract(String bookAbstract){
		if(bookAbstract != null) {
			this.bookAbstract = bookAbstract;
		}
	}
	
	String getBookContent(){
		return bookContent;
	}
	
	void setBookContent(String content){
		if(content != null) {
			bookContent = content;
		}
	}
	
	Integer getBookId(){
		return bookId;
	}
	
	void setBookId(Integer id){
		bookId = id;
	}
	
	public static final Parcelable.Creator<Book> CREATOR = new Creator<Book>() {
		public Book createFromParcel(Parcel source){
			Book mBook = new Book();
			mBook.bookName = source.readString();
			mBook.bookAuthor = source.readString();
			mBook.publicTime = source.readString();
			mBook.bookAbstract = source.readString();
			mBook.bookContent = source.readString();
			
			return mBook;
		}

		@Override
		public Book[] newArray(int size) {
			// TODO Auto-generated method stub
			return new Book[size];
		}
	};
	
	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	@Override
	public void writeToParcel(Parcel arg0, int arg1) {
		// TODO Auto-generated method stub
		arg0.writeString(bookName);
		arg0.writeString(bookAuthor);
		arg0.writeString(publicTime);
		arg0.writeString(bookAbstract);
		arg0.writeString(bookContent);
	}
}