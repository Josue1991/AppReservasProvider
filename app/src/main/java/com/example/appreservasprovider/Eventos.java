package com.example.appreservasprovider;

import android.app.DatePickerDialog;
import android.content.ContentValues;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.Toolbar;

import com.example.appreservasprovider.ClimaAPI.ClimaAPI;
import com.example.appreservasprovider.ClimaAPI.ClimaResponse;
import com.example.appreservasprovider.ClimaAPI.ClimaService;
import com.example.appreservasprovider.contract.EventosContract;
import com.example.appreservasprovider.providers.EventosProvider;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Eventos extends BaseActivity {

	private EditText nombreTxt;
	private EditText fechaTxt;
	private EditText emailTxt;
	private EditText telefonoTxt;
	private Button btnIngresar;
	private Calendar calendar;
	private static final String API_KEY = "6382f49acb70f27dcefe351bb3dc529c";


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

	private void showDatePickerDialog() {
		DatePickerDialog.OnDateSetListener dateSetListener = new DatePickerDialog.OnDateSetListener() {
			@Override
			public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
				calendar.set(Calendar.YEAR, year);
				calendar.set(Calendar.MONTH, monthOfYear);
				calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
				updateDateLabel();
				fetchWeather();
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
		nombreTxt.setText("");
		fechaTxt.setText("");
		emailTxt.setText("");
		telefonoTxt.setText("");
	}

	private void fetchWeather() {
		double latitude = 40.7128; // Ejemplo: latitud de Nueva York
		double longitude = -74.0060; // Ejemplo: longitud de Nueva York
		String selectedDate = fechaTxt.getText().toString();

		ClimaService apiService = ClimaAPI.getClient().create(ClimaService.class);
		Call<ClimaResponse> call = apiService.getClimaForecast(latitude, longitude, API_KEY);

		call.enqueue(new Callback<ClimaResponse>() {
			@Override
			public void onResponse(Call<ClimaResponse> call, Response<ClimaResponse> response) {
				if (response.isSuccessful() && response.body() != null) {
					ClimaResponse climaResponse = response.body();
					boolean willRain = false;

					for (ClimaResponse.ClimaList climaList : climaResponse.lista) {
						if (climaList.fecha.contains(selectedDate)) {
							for (ClimaResponse.Clima weather : climaList.clima) {
								if (weather.main.equalsIgnoreCase("Rain")) {
									willRain = true;
									break;
								}
							}
						}
					}

					String message = willRain ? "Lluvioso" : "Despejado";
					mostrarMensajeClima(message);

				} else {
					Log.e("API Error", "Response error: " + response.code());
				}
			}

			@Override
			public void onFailure(Call<ClimaResponse> call, Throwable t) {
				Log.e("API Error", "Request failed: " + t.getMessage());
			}
		});
	}

	private void mostrarMensajeClima(String mensaje){
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Información del Clima");
		builder.setMessage("El dia de mañana estará: " + mensaje + ", Desea reservar?");

		// Botón positivo (Aceptar)
		builder.setPositiveButton("Si, reservar", (dialog, which) -> {
			Toast.makeText(Eventos.this, "Botón Aceptar clickeado", Toast.LENGTH_SHORT).show();
		});

		builder.setNegativeButton("No", (dialog, which) -> {
			Toast.makeText(Eventos.this, "Registro no ingresado", Toast.LENGTH_SHORT).show();
			clearFields();
		});

		AlertDialog dialog = builder.create();
		dialog.show();
	}
}