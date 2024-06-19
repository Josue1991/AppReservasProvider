package com.example.appreservasprovider;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.appreservasprovider.contract.EventosContract;
import com.example.appreservasprovider.providers.EventosProvider;

public class Eventos extends BaseActivity {

	private EditText nombreTxt;
	private EditText fechaTxt;
	private EditText emailTxt;
	private EditText telefonoTxt;
	private Button btnIngresar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_eventos);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		nombreTxt = findViewById(R.id.nombreTxt);
		fechaTxt = findViewById(R.id.fechaTxt);
		emailTxt = findViewById(R.id.emailTxt);
		telefonoTxt = findViewById(R.id.telefonoTxt);
		btnIngresar = findViewById(R.id.btnIngresar);

		btnIngresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				insertEvent();
			}
		});
	}
	private void insertEvent() {
		String nombre = nombreTxt.getText().toString().trim();
		String fecha = fechaTxt.getText().toString().trim();
		String email = emailTxt.getText().toString().trim();
		String telefono = telefonoTxt.getText().toString().trim();

		if (nombre.isEmpty() || fecha.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
			Toast.makeText(this, "Por favor, complete todos los campos", Toast.LENGTH_SHORT).show();
			return;
		}

		ContentValues values = new ContentValues();
		values.put(EventosContract.EventosEntry.COLUMN_NOMBRE, nombre);
		values.put(EventosContract.EventosEntry.COLUMN_FECHA, fecha);
		values.put(EventosContract.EventosEntry.COLUMN_EMAIL, email);
		values.put(EventosContract.EventosEntry.COLUMN_TELEFONO, telefono);

		Uri newUri = getContentResolver().insert(EventosProvider.CONTENT_URI, values);

		if (newUri == null) {
			Toast.makeText(this, "Error al registrar el evento", Toast.LENGTH_SHORT).show();
		} else {
			Toast.makeText(this, "Evento registrado con éxito", Toast.LENGTH_SHORT).show();
			clearFields();
		}
	}

	private void clearFields() {
		nombreTxt.setText("");
		fechaTxt.setText("");
		emailTxt.setText("");
		telefonoTxt.setText("");
	}
}