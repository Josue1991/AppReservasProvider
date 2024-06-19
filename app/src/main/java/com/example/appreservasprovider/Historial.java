package com.example.appreservasprovider;

import android.annotation.SuppressLint;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.example.appreservasprovider.database.HistorialDatabase;
import com.example.appreservasprovider.providers.HistorialProvider;

public class Historial extends BaseActivity {

	private TableLayout tableLayout;
	private EditText nombreTxt;
	private Button btnIngresar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_historial);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		// Initialize TableLayout
		tableLayout = findViewById(R.id.tableLayout);
		nombreTxt = findViewById(R.id.nombreTxt);

		btnIngresar = findViewById(R.id.btnBuscar);

		btnIngresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				if (tableLayout != null) {
					consultarHistorial();
				} else {
					// Log or handle the case where tableLayout is null
					Log.e("Historial", "TableLayout is null");
				}
			}
		});
	}

	private void consultarHistorial() {
		// Obtener el nombre a buscar
		String nombre = nombreTxt.getText().toString(); // Aquí puedes obtener el nombre desde un EditText u otro componente

		// Definir la URI del contenido del proveedor de historial
		Uri uri = HistorialProvider.CONTENT_URI;

		// Definir las columnas que queremos consultar
		String[] projection = {
				HistorialDatabase.COLUMN_ID,
				HistorialDatabase.COLUMN_DESCRIPCION,
				HistorialDatabase.COLUMN_FECHA,
				HistorialDatabase.COLUMN_USUARIO,
				HistorialDatabase.COLUMN_ACCION
		};

		// Definir la cláusula WHERE
		String selection = HistorialDatabase.COLUMN_USUARIO + "=?";
		String[] selectionArgs = { nombre };

		// Realizar la consulta utilizando el ContentResolver y obtener el Cursor
		Cursor cursor = getContentResolver().query(uri, projection, selection, selectionArgs, null);
		tableLayout.removeAllViews();

		if (cursor != null) {
			Log.d("Historial", "Número de registros: " + cursor.getCount());
			if (cursor.moveToFirst()) {
				do {
					@SuppressLint("Range") int id = cursor.getInt(cursor.getColumnIndex(HistorialDatabase.COLUMN_ID));
					@SuppressLint("Range") String descripcion = cursor.getString(cursor.getColumnIndex(HistorialDatabase.COLUMN_DESCRIPCION));
					@SuppressLint("Range") String fecha = cursor.getString(cursor.getColumnIndex(HistorialDatabase.COLUMN_FECHA));
					@SuppressLint("Range") String usuario = cursor.getString(cursor.getColumnIndex(HistorialDatabase.COLUMN_USUARIO));
					@SuppressLint("Range") String accion = cursor.getString(cursor.getColumnIndex(HistorialDatabase.COLUMN_ACCION));

					Log.d("Historial", "Registro: ID=" + id + ", Descripción=" + descripcion + ", Fecha=" + fecha + ", Usuario=" + usuario + ", Acción=" + accion);

					TableRow row = new TableRow(this);

					TextView idTextView = new TextView(this);
					idTextView.setText(String.valueOf(id));
					row.addView(idTextView);

					TextView descripcionTextView = new TextView(this);
					descripcionTextView.setText(descripcion);
					row.addView(descripcionTextView);

					TextView fechaTextView = new TextView(this);
					fechaTextView.setText(fecha);
					row.addView(fechaTextView);

					TextView usuarioTextView = new TextView(this);
					usuarioTextView.setText(usuario);
					row.addView(usuarioTextView);

					TextView accionTextView = new TextView(this);
					accionTextView.setText(accion);
					row.addView(accionTextView);

					tableLayout.addView(row);

				} while (cursor.moveToNext());
			} else {
				Log.d("Historial", "No se encontraron registros para el usuario: " + nombre);
				TableRow row = new TableRow(this);
				TextView noResultsTextView = new TextView(this);
				noResultsTextView.setText("No se encontraron resultados.");
				row.addView(noResultsTextView);
				tableLayout.addView(row);
			}
			cursor.close();
		} else {
			Log.e("Historial", "Cursor es null");
			TableRow row = new TableRow(this);
			TextView noResultsTextView = new TextView(this);
			noResultsTextView.setText("No se encontraron resultados.");
			row.addView(noResultsTextView);
			tableLayout.addView(row);
		}
	}
}
