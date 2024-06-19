package com.example.appreservasprovider;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class BaseActivity extends AppCompatActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	}
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle item selection.
		if (item.getItemId() == R.id.home) {
			abrirActivityPrincipal();
			return true;
		}
		if (item.getItemId() == R.id.campos) {
			abrirActivityCampos();
			return true;
		}
		if (item.getItemId() == R.id.usuarios) {
			abrirActivityUsuarios();
			return true;
		}
		if (item.getItemId() == R.id.eventos) {
			abrirActivityEventos();
			return true;
		}
		if (item.getItemId() == R.id.historial) {
			abrirActivityHistorial();
			return true;
		}
		if (item.getItemId() == R.id.reservas) {
			abrirActivityReservas();
			return true;
		}
		return super.onOptionsItemSelected(item);
	}

	public void abrirActivityCampos()
	{
		Intent intent = new Intent(BaseActivity.this, AdministrarCampos.class);
		startActivity(intent);
	}
	public void abrirActivityUsuarios()
	{
		Intent intent = new Intent(BaseActivity.this, AdministrarUsuarios.class);
		startActivity(intent);
	}
	public void abrirActivityEventos()
	{
		Intent intent = new Intent(BaseActivity.this, Eventos.class);
		startActivity(intent);
	}
	public void abrirActivityHistorial()
	{
		Intent intent = new Intent(BaseActivity.this, Historial.class);
		startActivity(intent);
	}
	public void abrirActivityPrincipal()
	{
		Intent intent = new Intent(BaseActivity.this, MainActivity.class);
		startActivity(intent);
	}
	public void abrirActivityReservas()
	{
		Intent intent = new Intent(BaseActivity.this, ReservasActivity.class);
		startActivity(intent);
	}

}