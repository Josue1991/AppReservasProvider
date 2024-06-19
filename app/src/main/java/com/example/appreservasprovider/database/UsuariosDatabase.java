package com.example.appreservasprovider.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class UsuariosDatabase {
	public static final String TABLE_CAMPOS = "usuarios";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NOMBRE = "nombre";
	public static final String COLUMN_DNI = "dni";
	public static final String COLUMN_EMAIL = "email";
	public static final String COLUMN_PHONE = "telefono";

	static final String CREATE_TABLE  =
			"CREATE TABLE " + TABLE_CAMPOS + " (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_NOMBRE + " TEXT, " +
					COLUMN_DNI + " TEXT, " +
					COLUMN_EMAIL + " TEXT, " +
					COLUMN_PHONE + " TEXT);";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_CAMPOS;

}
