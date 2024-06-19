package com.example.appreservasprovider.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;
import android.util.Log;

import java.util.Objects;

import com.example.appreservasprovider.database.DatabaseHelper;
import com.example.appreservasprovider.database.EventosDatabase;
import com.example.appreservasprovider.database.HistorialDatabase;

public class EventosProvider extends ContentProvider {

	private static final String AUTHORITY = "com.example.appreservasprovider.eventos";
	private static final String BASE_PATH = "eventos";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	private static final int EVENTOS = 1;
	private static final int EVENTO_ID = 2;

	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		uriMatcher.addURI(AUTHORITY, BASE_PATH, EVENTOS);
		uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", EVENTO_ID);
	}

	private SQLiteDatabase database;
	private DatabaseHelper dbHelper;

	@Override
	public boolean onCreate() {
		dbHelper = new DatabaseHelper(getContext());
		database = dbHelper.getWritableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(EventosDatabase.TABLE_EVENTOS);

		int uriType = uriMatcher.match(uri);
		switch (uriType) {
			case EVENTO_ID:
				queryBuilder.appendWhere(EventosDatabase.COLUMN_ID + "=" + uri.getLastPathSegment());
				break;
			case EVENTOS:
				break;
			default:
				throw new IllegalArgumentException("Unknown URI");
		}

		Cursor cursor = queryBuilder.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		cursor.setNotificationUri(getContext().getContentResolver(), uri);
		return cursor;
	}

	@Override
	public Uri insert(Uri uri, ContentValues values) {
		long id = database.insert(EventosDatabase.TABLE_EVENTOS, null, values);
		if (id > 0) {
			Uri newUri = ContentUris.withAppendedId(CONTENT_URI, id);

			// Registro en historial
			String usuario = "admin"; // Puedes obtener el usuario actual aquí
			String accion = "Insertó un nuevo evento";
			dbHelper.insertarRegistroHistorial(database, usuario, accion);

			Objects.requireNonNull(getContext()).getContentResolver().notifyChange(newUri, null);
			return newUri;
		} else {
			Log.e("EventosProvider", "Failed to insert row into " + uri);
			throw new RuntimeException("Failed to insert row into " + uri);
		}
	}

	@Override
	public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
		int rowsUpdated = 0;
		int uriType = uriMatcher.match(uri);
		switch (uriType) {
			case EVENTOS:
				rowsUpdated = database.update(EventosDatabase.TABLE_EVENTOS, values, selection, selectionArgs);
				break;
			case EVENTO_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = database.update(EventosDatabase.TABLE_EVENTOS, values, EventosDatabase.COLUMN_ID + "=" + id, null);
				} else {
					rowsUpdated = database.update(EventosDatabase.TABLE_EVENTOS, values, EventosDatabase.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI");
		}

		if (rowsUpdated > 0) {
			// Registro en historial
			String usuario = "admin"; // Puedes obtener el usuario actual aquí
			String accion = "Actualizó un evento";
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
			case EVENTOS:
				rowsDeleted = database.delete(EventosDatabase.TABLE_EVENTOS, selection, selectionArgs);
				break;
			case EVENTO_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = database.delete(EventosDatabase.TABLE_EVENTOS, EventosDatabase.COLUMN_ID + "=" + id, null);
				} else {
					rowsDeleted = database.delete(EventosDatabase.TABLE_EVENTOS, EventosDatabase.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI");
		}

		if (rowsDeleted > 0) {
			// Registro en historial
			String usuario = "admin"; // Puedes obtener el usuario actual aquí
			String accion = "Eliminó un evento";
			dbHelper.insertarRegistroHistorial(database, usuario, accion);

			getContext().getContentResolver().notifyChange(uri, null);
		}

		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
			case EVENTOS:
				return "vnd.android.cursor.dir/vnd.com.example.appreservasprovider.eventos";
			case EVENTO_ID:
				return "vnd.android.cursor.item/vnd.com.example.appreservasprovider.eventos";
			default:
				throw new IllegalArgumentException("Unknown URI");
		}
	}
}
