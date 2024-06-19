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

import androidx.annotation.NonNull;

import com.example.appreservasprovider.contract.UsuariosContract;

import com.example.appreservasprovider.database.UsuariosDatabase;
import com.example.appreservasprovider.database.DatabaseHelper;
import com.example.appreservasprovider.database.EventosDatabase;
import com.example.appreservasprovider.database.UsuariosDatabase;

import java.util.Objects;

public class UsuariosProvider extends ContentProvider {

	private static final String AUTHORITY = "com.example.appreservasprovider.usuarios";
	private static final String BASE_PATH = "usuarios";
	public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + BASE_PATH);

	private static final int CAMPOS = 1;
	private static final int CAMPOS_ID = 2;

	private static final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

	static {
		uriMatcher.addURI(AUTHORITY, BASE_PATH, CAMPOS);
		uriMatcher.addURI(AUTHORITY, BASE_PATH + "/#", CAMPOS_ID);
	}

	private SQLiteDatabase database;

	@Override
	public boolean onCreate() {
		DatabaseHelper helper = new DatabaseHelper(getContext());
		database = helper.getWritableDatabase();
		return true;
	}

	@Override
	public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
		SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
		queryBuilder.setTables(UsuariosDatabase.TABLE_CAMPOS);

		int uriType = uriMatcher.match(uri);
		switch (uriType) {
			case CAMPOS_ID:
				queryBuilder.appendWhere(UsuariosDatabase.COLUMN_ID + "=" + uri.getLastPathSegment());
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
	public Uri insert(@NonNull Uri uri, ContentValues values) {
		long id = database.insert(UsuariosDatabase.TABLE_CAMPOS, null, values);
		if (id > 0) {
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
				rowsUpdated = database.update(UsuariosDatabase.TABLE_CAMPOS, values, selection, selectionArgs);
				break;
			case CAMPOS_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsUpdated = database.update(UsuariosDatabase.TABLE_CAMPOS, values, UsuariosDatabase.COLUMN_ID + "=" + id, null);
				} else {
					rowsUpdated = database.update(UsuariosDatabase.TABLE_CAMPOS, values, UsuariosDatabase.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsUpdated;
	}

	@Override
	public int delete(Uri uri, String selection, String[] selectionArgs) {
		int rowsDeleted = 0;
		int uriType = uriMatcher.match(uri);
		switch (uriType) {
			case CAMPOS:
				rowsDeleted = database.delete(UsuariosDatabase.TABLE_CAMPOS, selection, selectionArgs);
				break;
			case CAMPOS_ID:
				String id = uri.getLastPathSegment();
				if (TextUtils.isEmpty(selection)) {
					rowsDeleted = database.delete(UsuariosDatabase.TABLE_CAMPOS, UsuariosDatabase.COLUMN_ID + "=" + id, null);
				} else {
					rowsDeleted = database.delete(UsuariosDatabase.TABLE_CAMPOS, UsuariosDatabase.COLUMN_ID + "=" + id + " and " + selection, selectionArgs);
				}
				break;
			default:
				throw new IllegalArgumentException("Unknown URI");
		}
		getContext().getContentResolver().notifyChange(uri, null);
		return rowsDeleted;
	}

	@Override
	public String getType(Uri uri) {
		switch (uriMatcher.match(uri)) {
			case CAMPOS:
				return "vnd.android.cursor.dir/vnd.com.example.appreservasprovider.usuarios";
			case CAMPOS_ID:
				return "vnd.android.cursor.item/vnd.com.example.appreservasprovider.usuarios";
			default:
				throw new IllegalArgumentException("Unknown URI");
		}
	}
}