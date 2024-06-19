package com.example.appreservasprovider;

import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.example.appreservasprovider.providers.UsuariosProvider;

public class AdministrarUsuarios extends BaseActivity {

	private EditText nombreTxt, cedulaTxt, emailTxt, telefonoTxt;
	private Button btnIngresar;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_administrar_usuarios);

		Toolbar toolbar = findViewById(R.id.toolbar);
		setSupportActionBar(toolbar);

		nombreTxt = findViewById(R.id.nombreTxt);
		cedulaTxt = findViewById(R.id.cedulaTxt);
		emailTxt = findViewById(R.id.emailTxt);
		telefonoTxt = findViewById(R.id.telefonoTxt);
		btnIngresar = findViewById(R.id.btnIngresar);

		btnIngresar.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				String nombre = nombreTxt.getText().toString();
				String cedula = cedulaTxt.getText().toString();
				String email = emailTxt.getText().toString();
				String telefono = telefonoTxt.getText().toString();

				if (nombre.isEmpty() || cedula.isEmpty() || email.isEmpty() || telefono.isEmpty()) {
					Toast.makeText(AdministrarUsuarios.this, "Todos los campos son obligatorios", Toast.LENGTH_SHORT).show();
					return;
				}

				ContentValues values = new ContentValues();
				values.put("nombre", nombre);
				values.put("dni", cedula);
				values.put("email", email);
				values.put("telefono", telefono);

				Uri newUri = getContentResolver().insert(UsuariosProvider.CONTENT_URI, values);

				if (newUri != null) {
					Toast.makeText(AdministrarUsuarios.this, "Usuario registrado exitosamente", Toast.LENGTH_SHORT).show();
					clearFields();
				} else {
					Toast.makeText(AdministrarUsuarios.this, "Error al registrar el usuario", Toast.LENGTH_SHORT).show();
				}
			}
		});
	}

	private void clearFields() {
		nombreTxt.setText("");
		cedulaTxt.setText("");
		emailTxt.setText("");
		telefonoTxt.setText("");
	}
}