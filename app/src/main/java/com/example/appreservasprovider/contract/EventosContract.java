package com.example.appreservasprovider.contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class EventosContract {
	public static final String CONTENT_AUTHORITY = "com.example.appreservasprovider";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_EVENTS = "eventos";

	private EventosContract() {}

	public static final class EventosEntry implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EVENTS);

		public static final String CONTENT_LIST_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;

		public static final String TABLE_NAME = "eventos";
		public static final String _ID = BaseColumns._ID;
		public static final String COLUMN_NOMBRE = "nombre";
		public static final String COLUMN_FECHA = "fecha";
		public static final String COLUMN_EMAIL = "email";
		public static final String COLUMN_TELEFONO = "telefono";
	}
}