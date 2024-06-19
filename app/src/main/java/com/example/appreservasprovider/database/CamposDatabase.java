package com.example.appreservasprovider.database;

public class CamposDatabase {

	public static final String TABLE_CAMPOS = "campos";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_NAME = "nombre";
	public static final String COLUMN_DESCRIPCION = "descripcion";
	public static final String COLUMN_ESTADO = "estado";
	public static final String COLUMN_PHONE = "telefono";
	public static final String COLUMN_FECHA = "fecha";

	static final String CREATE_TABLE  =
			"CREATE TABLE " + TABLE_CAMPOS + " (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_NAME + " TEXT, " +
					COLUMN_DESCRIPCION + " TEXT, " +
					COLUMN_ESTADO + " TEXT, " +
					COLUMN_FECHA + " TEXT, " +
					COLUMN_PHONE + " TEXT);";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_CAMPOS;

}
