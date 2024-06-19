package com.example.appreservasprovider;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.appreservasprovider.providers.CamposProvider;
import com.example.appreservasprovider.providers.ReservasProvider;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ReservasActivity extends BaseActivity {

	private EditText fechaTxt, descripcionTxt, usuarioTxt,ubicacionTxt, precioTxt;
	private Button btnIngresar;
	private Calendar calendar;
	private FusedLocationProviderClient fusedLocationProviderClient;
	private static final int REQUEST_LOCATION_PERMISSION = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_reservas);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		descripcionTxt = findViewById(R.id.descripcionTxt);
		fechaTxt = findViewById(R.id.fechaTxt);
		usuarioTxt = findViewById(R.id.usuarioTxt);
		ubicacionTxt = findViewById(R.id.ubicacionTxt);
		precioTxt = findViewById(R.id.precioTxt);
		btnIngresar = findViewById(R.id.btnIngresar);

		calendar = Calendar.getInstance();
		fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


		fechaTxt.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				showDatePickerDialog();
			}
		});

		btnIngresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String descripcion = descripcionTxt.getText().toString();
				String fecha = fechaTxt.getText().toString();
				String usuario = usuarioTxt.getText().toString();
				String ubicacion = ubicacionTxt.getText().toString();
				String precio = precioTxt.getText().toString();

				if (descripcion.isEmpty() || fecha.isEmpty() || usuario.isEmpty() || ubicacion.isEmpty() || precio.isEmpty()) {
					Toast.makeText(ReservasActivity.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
					return;
				}

				ContentValues values = new ContentValues();
				values.put("descripcion", descripcion);
				values.put("fecha", fecha);
				values.put("usuario", usuario);
				values.put("ubicacion", ubicacion);
				values.put("precio", precio);


				Uri newUri = getContentResolver().insert(ReservasProvider.CONTENT_URI, values);

				if (newUri != null) {
					Toast.makeText(ReservasActivity.this, "Reserva registrada exitosamente", Toast.LENGTH_SHORT).show();
					clearFields();
				} else {
					Toast.makeText(ReservasActivity.this, "Error al registrar el campo", Toast.LENGTH_SHORT).show();
				}
			}
		});
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
	private void clearFields() {
		descripcionTxt.setText("");
		fechaTxt.setText("");
		usuarioTxt.setText("");
		ubicacionTxt.setText("");
		precioTxt.setText("");
	}

}