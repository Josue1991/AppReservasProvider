package com.example.appreservasprovider.ClimaAPI;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ClimaService {
	@GET("forecast")
	Call<ClimaResponse> getClimaForecast(
			@Query("lat") double latitude,
			@Query("lon") double longitude,
			@Query("appid") String apiKey
	);
}
