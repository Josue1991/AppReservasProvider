package com.example.appreservasprovider.ClimaAPI;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class ClimaResponse {
	@SerializedName("list")
	public List<ClimaList> lista;

	public static class ClimaList {
		@SerializedName("dt_txt")
		public String fecha;

		@SerializedName("weather")
		public List<Clima> clima;
	}

	public static class Clima {
		@SerializedName("main")
		public String main;
	}
}
