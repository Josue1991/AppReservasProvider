package com.example.appreservasprovider.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.appreservasprovider.database.DatabaseHelper;
import com.example.appreservasprovider.database.HistorialDatabase;

public class HistorialProvider extends ContentProvider {

	public static final String AUTHORITY = "com.example.appreservasprovider.historial";
	private static final String BASE_PATH = "historial";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	private static final int HISTORIAL = 1;
	private static final int HISTORIAL_ID = 2;

	private static final UriMatcher uriMatcher;
	static {
		uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
		uriMatcher.addURI(AUTHORITY, "historial", HISTORIAL);
		uriMatcher.addURI(AUTHORITY, "historial/#", HISTORIAL_ID);
	}

	private SQLiteDatabase database;

	@Override
	public boolean onCreate() {
		Context context = getContext();
		DatabaseHelper dbHelper = new DatabaseHelper(context);
		database = dbHelper.getWritableDatabase();
		return database != null;
	}

	@Nullable
	@Override
	public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
		long rowID = database.insert(HistorialDatabase.TABLE_CAMPOS, "", values);
		if (rowID > 0) {
			Uri _uri = ContentUris.withAppendedId(CONTENT_URI, rowID);
			getContext().getContentResolver().notifyChange(_uri, null);
			return _uri;
		}
		throw new SQLException("Failed to add a record into " + uri);
	}

	@Nullable
	@Override
	public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
		SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
		qb.setTables(HistorialDatabase.TABLE_CAMPOS);

		switch (uriMatcher.match(uri)) {
			case HISTORIAL:
				break;
			case HISTORIAL_ID:
				qb.appendWhere(HistorialDatabase.COLUMN_ID + "=" + uri.getPathSegments().get(1));
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		Cursor c = qb.query(database, projection, selection, selectionArgs, null, null, sortOrder);
		c.setNotificationUri(getContext().getContentResolver(), uri);
		return c;
	}

	@Override
	public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
		int count;
		switch (uriMatcher.match(uri)) {
			case HISTORIAL:
				count = database.update(HistorialDatabase.TABLE_CAMPOS, values, selection, selectionArgs);
				break;
			case HISTORIAL_ID:
				count = database.update(HistorialDatabase.TABLE_CAMPOS, values, HistorialDatabase.COLUMN_ID + "=" + uri.getPathSegments().get(1) +
						(!selection.isEmpty() ? " AND (" + selection + ')' : ""), selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Override
	public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
		int count;
		switch (uriMatcher.match(uri)) {
			case HISTORIAL:
				count = database.delete(HistorialDatabase.TABLE_CAMPOS, selection, selectionArgs);
				break;
			case HISTORIAL_ID:
				count = database.delete(HistorialDatabase.TABLE_CAMPOS, HistorialDatabase.COLUMN_ID + "=" + uri.getPathSegments().get(1) +
						(!selection.isEmpty() ? " AND (" + selection + ')' : ""), selectionArgs);
				break;
			default:
				throw new IllegalArgumentException("Unknown URI " + uri);
		}

		getContext().getContentResolver().notifyChange(uri, null);
		return count;
	}

	@Nullable
	@Override
	public String getType(@NonNull Uri uri) {
		switch (uriMatcher.match(uri)) {
			case HISTORIAL:
				return "vnd.android.cursor.dir/vnd.example.historial";
			case HISTORIAL_ID:
				return "vnd.android.cursor.item/vnd.example.historial";
			default:
				throw new IllegalArgumentException("Unsupported URI: " + uri);
		}
	}
}
