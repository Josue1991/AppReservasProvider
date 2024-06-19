package com.example.appreservasprovider.database;

public class ReservasDatabase {
	public static final String TABLE_RESERVAS = "reservas";
	public static final String COLUMN_ID = "_id";
	public static final String COLUMN_DESCRIPCION = "descripcion";
	public static final String COLUMN_USUARIO = "usuario";
	public static final String COLUMN_FECHA = "fecha";
	public static final String COLUMN_UBICACION = "ubicacion";
	public static final String COLUMN_PRECIO = "precio";

	static final String CREATE_TABLE  =
			"CREATE TABLE " + TABLE_RESERVAS + " (" +
					COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					COLUMN_DESCRIPCION + " TEXT, " +
					COLUMN_USUARIO + " TEXT, " +
					COLUMN_FECHA + " TEXT, " +
					COLUMN_UBICACION + " TEXT, " +
					COLUMN_PRECIO + " TEXT);";
	public static final String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_RESERVAS;

}
