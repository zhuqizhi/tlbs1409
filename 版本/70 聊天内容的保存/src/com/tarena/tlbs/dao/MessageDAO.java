package com.tarena.tlbs.dao;

import java.util.ArrayList;

import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Message.Type;

import com.tarena.tlbs.util.ExceptionUtil;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

/**
 * 对message表进行insert,delete,modify,query
 * 
 * @author tarena
 * 
 */
public class MessageDAO {
	public MessageDAO(Context context) {
		// 创建表，好处在写MessageDao,看到表结构
		String sql = "create table " 
		+ "if not exists message"
				+ "(_id integer primary key,"
		+ "messageFrom text,"
				+ "messageTo text," 
		+ "messageType text," 
				+ "messageBody text)";
		DbHelper dbHelper = new DbHelper(context);
		dbHelper.getWritableDatabase().execSQL(sql);

	}

	public int insert(Context context, Message message) {
		int id = -1;
		DbHelper dbHelper = null;
		SQLiteDatabase sqLiteDatabase = null;
		try {
			dbHelper = new DbHelper(context);
			sqLiteDatabase = dbHelper.getWritableDatabase();
			ContentValues values = new ContentValues();
			values.put("messageFrom", message.getFrom());
			values.put("messageTo", message.getTo());
			values.put("messageType", message.getType().toString());
			values.put("messageBody", message.getBody());
			id = (int) sqLiteDatabase.insert("message", null, values);
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		} finally {
			dbHelper.close();
			dbHelper = null;

			sqLiteDatabase.close();
			sqLiteDatabase = null;
		}
		return id;
	}

	public ArrayList<Message> query(Context context, String friendUser) {
		ArrayList<Message> list = new ArrayList<Message>();
		DbHelper dbHelper = null;
		SQLiteDatabase sqLiteDatabase = null;
		Cursor cursor = null;
		try {
			dbHelper = new DbHelper(context);
			sqLiteDatabase = dbHelper.getWritableDatabase();
			String[] columns = new String[] { "messageFrom", "messageTo",
					"messageBody", "messageType" };
			String selection = "messageFrom=? or messageTo=?";
			String[] selectionArgs = new String[]
					{ friendUser, friendUser };

			cursor = sqLiteDatabase.query("message", columns, selection,
					selectionArgs, null, null, "_id asc");
			while (cursor.moveToNext()) {
				String from = cursor.getString(cursor
						.getColumnIndex("messageFrom"));
				String to = cursor
						.getString(cursor.getColumnIndex("messageTo"));
				String body = cursor.getString(cursor
						.getColumnIndex("messageBody"));
				String type = cursor.getString(cursor
						.getColumnIndex("messageType"));

				Message message = new Message();
				message.setFrom(from);
				message.setTo(to);
				message.setBody(body);
				if ("chat".equals(type)) {
					message.setType(Type.chat);
				}
				list.add(message);

			}
		} catch (Exception e) {
			ExceptionUtil.handleException(e);
		} finally {
			cursor.close();
			cursor = null;

			sqLiteDatabase.close();
			sqLiteDatabase = null;
			
			dbHelper.close();
			dbHelper = null;
			
		}
		return list;
	}

}
