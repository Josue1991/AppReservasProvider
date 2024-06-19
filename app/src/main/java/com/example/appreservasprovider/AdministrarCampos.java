package com.example.appreservasprovider;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.appreservasprovider.providers.CamposProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class AdministrarCampos extends BaseActivity {
	private EditText fechaTxt, nombreTxt, descripcionTxt, estadoTxt, telefonoTxt;
	private Button btnIngresar;
	private Calendar calendar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_administrar_campos);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		nombreTxt = findViewById(R.id.nombreTxt);
		descripcionTxt = findViewById(R.id.descripcionTxt);
		estadoTxt = findViewById(R.id.estadoTxt);
		telefonoTxt = findViewById(R.id.telefonoTxt);
		btnIngresar = findViewById(R.id.btnIngresar);
		fechaTxt = findViewById(R.id.fechaTxt);
		calendar = Calendar.getInstance();

		fechaTxt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}
		});

		btnIngresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String nombre = nombreTxt.getText().toString();
				String descripcion = descripcionTxt.getText().toString();
				String estado = estadoTxt.getText().toString();
				String telefono = telefonoTxt.getText().toString();
				String fecha = fechaTxt.getText().toString();

				if (nombre.isEmpty() || descripcion.isEmpty() || estado.isEmpty() || telefono.isEmpty() || fecha.isEmpty()) {
					Toast.makeText(AdministrarCampos.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
					return;
				}

				ContentValues values = new ContentValues();
				values.put("nombre", nombre);
				values.put("descripcion", descripcion);
				values.put("estado", estado);
				values.put("telefono", telefono);
				values.put("fecha", fecha);

				Uri newUri = getContentResolver().insert(CamposProvider.CONTENT_URI, values);

				if (newUri != null) {
					Toast.makeText(AdministrarCampos.this, "Campo registrado exitosamente", Toast.LENGTH_SHORT).show();
					clearFields();
				} else {
					Toast.makeText(AdministrarCampos.this, "Error al registrar el campo", Toast.LENGTH_SHORT).show();
				}
			}
		});

		Button btnShare = findViewById(R.id.btnShare);
		btnShare.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				Intent sendIntent = new Intent();
				sendIntent.setAction(Intent.ACTION_SEND);
				sendIntent.putExtra(Intent.EXTRA_TEXT, "Reserve el campo de Juego:"+descripcionTxt);
				sendIntent.setType("text/plain");

				Intent shareIntent = Intent.createChooser(sendIntent, null);
				startActivity(shareIntent);
			}
		});

	}

	private void clearFields() {
		nombreTxt.setText("");
		descripcionTxt.setText("");
		estadoTxt.setText("");
		telefonoTxt.setText("");
	}

	private void showDatePickerDialog() {
		DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateDateLabel();
			}
		};

		new DatePickerDialog(this, dateSetListener,
				calendar.get(Calendar.YEAR),
				calendar.get(Calendar.MONTH),
				calendar.get(Calendar.DAY_OF_MONTH))
				.show();
	}

	private void updateDateLabel() {
		String myFormat = "dd/MM/yyyy"; // Formato de la fecha
		SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.getDefault());
		fechaTxt.setText(sdf.format(calendar.getTime()));
	}
}