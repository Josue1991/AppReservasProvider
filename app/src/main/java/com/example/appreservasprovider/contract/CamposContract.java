package com.example.appreservasprovider.contract;

import android.content.ContentResolver;
import android.net.Uri;
import android.provider.BaseColumns;

public class CamposContract {
	public static final String CONTENT_AUTHORITY = "com.example.appreservasprovider";
	public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);
	public static final String PATH_EVENTS = "campos";

	private CamposContract() {}

	public static final class CamposEntry implements BaseColumns {
		public static final Uri CONTENT_URI = Uri.withAppendedPath(BASE_CONTENT_URI, PATH_EVENTS);

		public static final String CONTENT_LIST_TYPE =
				ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;
		public static final String CONTENT_ITEM_TYPE =
				ContentResolver.CURSOR_ITEM_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + PATH_EVENTS;

		public static final String TABLE_NAME = "campos";
		public static final String _ID = BaseColumns._ID;
		public static final String COLUMN_NOMBRE = "nombre";
		public static final String COLUMN_DESCRIPCION = "descripcion";
		public static final String COLUMN_ESTADO = "estado";
		public static final String COLUMN_TELEFONO = "telefono";
		public static final String COLUMN_FECHA = "fecha";
	}
}
