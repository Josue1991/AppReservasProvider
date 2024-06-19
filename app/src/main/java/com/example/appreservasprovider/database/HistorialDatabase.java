package com.example.appreservasprovider.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class HistorialDatabase{
	public static final String TABLE_CAMPOS = "historial";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DESCRIPCION = "descripcion";
	public static final String COLUMN_FECHA = "fecha";
	public static final String COLUMN_USUARIO = "usuario";
	public static final String COLUMN_ACCION = "accion";

	static final String CREATE_TABLE  =
			"CREATE TABLE " + TABLE_CAMPOS + " (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_USUARIO + " TEXT, " +
					COLUMN_DESCRIPCION + " TEXT, " +
					COLUMN_FECHA + " TEXT, " +
					COLUMN_ACCION + " TEXT);";

	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_CAMPOS;

}
