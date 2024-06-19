package com.example.appreservasprovider.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseHelper extends SQLiteOpenHelper {

	private static final String DATABASE_NAME = "reservas.db";
	private static final int DATABASE_VERSION = 1;

	public DatabaseHelper(Context context) {

		super(context, DATABASE_NAME, null, DATABASE_VERSION);
	}

	@Override
	public void onCreate(SQLiteDatabase db) {
		db.execSQL(CamposDatabase.CREATE_TABLE);
		db.execSQL(EventosDatabase.CREATE_TABLE);
		db.execSQL(HistorialDatabase.CREATE_TABLE);
		db.execSQL(UsuariosDatabase.CREATE_TABLE);
		db.execSQL(ReservasDatabase.CREATE_TABLE);
	}

	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		db.execSQL(CamposDatabase.DROP_TABLE);
		db.execSQL(EventosDatabase.DROP_TABLE);
		db.execSQL(HistorialDatabase.DROP_TABLE);
		db.execSQL(UsuariosDatabase.DROP_TABLE);
		db.execSQL(ReservasDatabase.DROP_TABLE);
		onCreate(db);
	}
	public void insertarRegistroHistorial(SQLiteDatabase db, String usuario, String accion) {
		ContentValues values = new ContentValues();
		values.put(HistorialDatabase.COLUMN_USUARIO, usuario);
		values.put(HistorialDatabase.COLUMN_ACCION, accion);
		values.put(HistorialDatabase.COLUMN_FECHA, getCurrentDateTime()); // Método para obtener la fecha actual

		db.insert(HistorialDatabase.TABLE_CAMPOS, null, values);
	}

	// Método para obtener la fecha y hora actual (puedes ajustarlo según tus necesidades)
	private String getCurrentDateTime() {
		return "2024-06-14 10:00:00";
	}
}
