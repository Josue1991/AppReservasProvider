package com.example.appreservasprovider.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appreservasprovider.database.DatabaseHelper;
import com.example.appreservasprovider.database.ReservasDatabase;

import java.util.Objects;

public class ReservasProvider  extends ContentProvider {
	private static final String AUTHORITY = "com.example.appreservasprovider.reservas";
	private static final String BASE_PATH = "reservas";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	private static final int CAMPOS = 1;
	private static final int CAMPOS_ID = 2;

	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		uriMatcher.addURI(AUTHORITY, BASE_PATH, CAMPOS);
		uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CAMPOS_ID);
	}

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	@Override
	public boolean onCreate() {
		Context context = getContext();
		dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return true;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, ContentValues values) {
		long id = database.insert(ReservasDatabase.TABLE_RESERVAS, null, values);
		if (id > 0) {
			// Registro en historial
			String usuario = "admin"; // Puedes obtener el usuario actual aquí
			String accion = "Insertó una nueva reserva";
			dbHelper.insertarRegistroHistorial(database, usuario, accion);

			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);
			Objects.requireNonNull(getContext()).getContentResolver().notifyChange(newUri, null);
			return newUri;
		} else {
			throw new RuntimeException("Failed to insert row into " + uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int rowsUpdated = 0;
		int uriType = uriMatcher.match(uri);
		switch (uriType) {
			case CAMPOS:
				rowsUpdated = database.update(ReservasDatabase.TABLE_RESERVAS, values, selection, selectionArgs);
				break;
			case CAMPOS_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = database.update(ReservasDatabase.TABLE_RESERVAS, values, ReservasDatabase.COLUMN_ID + "=" + id, null);
				} else {
					rowsUpdated = database.update(ReservasDatabase.TABLE_RESERVAS, values, ReservasDatabase.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI");
		}

		if (rowsUpdated > 0) {
			// Registro en historial
			String usuario = "admin"; // Puedes obtener el usuario actual aquí
			String accion = "Actualizó un campo";
			dbHelper.insertarRegistroHistorial(database, usuario, accion);

			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rowsUpdated;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsDeleted = 0;
		int uriType = uriMatcher.match(uri);
		switch (uriType) {
			case CAMPOS:
				rowsDeleted = database.delete(ReservasDatabase.TABLE_RESERVAS, selection, selectionArgs);
				break;
			case CAMPOS_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = database.delete(ReservasDatabase.TABLE_RESERVAS, ReservasDatabase.COLUMN_ID + "=" + id, null);
				} else {
					rowsDeleted = database.delete(ReservasDatabase.TABLE_RESERVAS, ReservasDatabase.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI");
		}

		if (rowsDeleted > 0) {
			// Registro en historial
			String usuario = "admin"; // Puedes obtener el usuario actual aquí
			String accion = "Eliminó un campo";
			dbHelper.insertarRegistroHistorial(database, usuario, accion);

			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rowsDeleted;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(ReservasDatabase.TABLE_RESERVAS);

		int uriType = uriMatcher.match(uri);
		switch (uriType) {
			case CAMPOS_ID:
				queryBuilder.appendWhere(ReservasDatabase.COLUMN_ID + "=" + uri.getLastPathSegment());
				break;
			case CAMPOS:
				break;
			default:
				throw new IllegalArgumentException("Unknown URI");
		}

		Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(Objects.requireNonNull(getContext()).getContentResolver(), uri);
		return cursor;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
			case CAMPOS:
				return "vnd.android.cursor.dir/vnd.com.example.appreservasprovider.reservas";
			case CAMPOS_ID:
				return "vnd.android.cursor.item/vnd.com.example.appreservasprovider.reservas";
			default:
				throw new IllegalArgumentException("Unknown URI");
		}
	}
}