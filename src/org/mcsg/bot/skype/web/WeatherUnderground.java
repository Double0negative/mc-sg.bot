package org.mcsg.bot.skype.web;

import org.mcsg.bot.skype.util.Settings;
import org.mcsg.bot.skype.util.StringUtils;

import com.google.gson.Gson;

public class WeatherUnderground {


	private static final String ZIP_URL = "http://api.wunderground.com/api/{0}/geolookup/q/{1}.json";
	private static final String CONDITIONS_URL_ZIP = "http://api.wunderground.com/api/{0}/conditions/q/{1}.json";
	private static final String CONDITIONS_URL_CITY = "http://api.wunderground.com/api/{0}/conditions/q/{1}/{2}.json";
	private static final String FCSTHOUR_URL_ZIP = "http://api.wunderground.com/api/{0}/hourly/q/{1}.json";
	private static final String FCSTHOUR_URL_CITY = "http://api.wunderground.com/api/{0}/hourly/q/{1}/{2}.json";
	private static final String FCSTDAY_URL_ZIP = "http://api.wunderground.com/api/{0}/forecast10day/q/{1}.json";
	private static final String FCSTDAY_URL_CITY = "http://api.wunderground.com/api/{0}/forcast10day/q/{1}/{2}.json";
	private static final String ALERTS_URL_ZIP = "http://api.wunderground.com/api/{0}/alerts/q/{1}.json";
	private static final String ALERTS_URL_CITY = "http://api.wunderground.com/api/{0}/alerts/q/{1}/{2}.json";


	public class ZipQuery{
		public Location location;
		public Error error;

		public class Location {
			public String country;
			public String state;
			public String city;
		}

		public class Error{
			String type;
			String description;
		}
	}

	public class WeatherConditions { 
		public CurrentObservation current_observation;
		public Response response;

		public class CurrentObservation {
			public ObservationLocation observation_location;
			public DisplayLocation display_location;

			public String station_id;
			public String weather;
			public String teampature_string;
			public String temp_f;
			public String temp_c;
			public String relative_humidity;
			public String wind_string;
			public String wind_dir;
			public String wind_degrees;
			public String wind_mph;
			public String wind_gust_mph;
			public String wind_kph;
			public String wind_gust_kph;
			public String feelslike_f;
			public String feelslike_c;

			public class DisplayLocation {
				public String full;
			}

			public class ObservationLocation {
				public String full;
				public String city;
				public String state;
				public String elevation;
			}
		}

		public class Response { 
			public Error error;

			public class Error{
				public String type;
				public String description;
			}
		}
	}

	public class ForecastHour {
		public HourlyForecast [] hourly_forecast;

		public class HourlyForecast {
			public FCTTIME FCTTIME;
			public Temp temp;
			public WindSpeed wspd;
			public WindDir wdir;
			public String wx;
			public String humidity;
			public String pop;


			public class WindSpeed { 
				public String english;
				public String metric;
			}

			public class WindDir {
				public String dir;
			}

			public class Temp {
				public String english;
				public String metric;
			}

			public class FCTTIME {
				public String weekday_name;
				public String civil;
			}
		}
	}



	public class ForecastDay {
		public Forecast forecast;

		public class Forecast { 
			public TxtForcast  txt_forecast;

			public class TxtForcast {
				public Day [] forecastday;


				public class Day {
					public int period;
					public String title;
					public String fcttext;
					public String fcttext_metric;
					public int pop;
				}
			}
		}

	}
	
	public class AlertInfo {
		public Alert[] alerts;
		public class Alert {
			public String description;
			public String expires;
			
		}
	}

	public static ZipQuery queryZipCode(String code) throws Exception{
		String json = WebClient.request(StringUtils.replaceVars(ZIP_URL, getKey(), code));
		return new Gson().fromJson(json, ZipQuery.class);
	}

	public static WeatherConditions weatherQuery(String loc, String city) throws Exception { 
		String json = WebClient.request(StringUtils.replaceVars(CONDITIONS_URL_CITY, getKey(), loc, city));
		return new Gson().fromJson(json, WeatherConditions.class);
	}

	public static WeatherConditions weatherQuery(String zip) throws Exception { 
		String json = WebClient.request(StringUtils.replaceVars(CONDITIONS_URL_ZIP, getKey(), zip));
		return new Gson().fromJson(json, WeatherConditions.class);
	}

	public static ForecastDay forecastDay(String zip) throws Exception{
		String json = WebClient.request(StringUtils.replaceVars(FCSTDAY_URL_ZIP, getKey(), zip));
		return new Gson().fromJson(json, ForecastDay.class);
	}

	public static ForecastDay forecastDay(String loc, String city) throws Exception{
		String json = WebClient.request(StringUtils.replaceVars(FCSTDAY_URL_CITY, getKey(), loc, city));
		return new Gson().fromJson(json, ForecastDay.class);
	}

	public static ForecastHour forecastHour(String zip) throws Exception{
		String json = WebClient.request(StringUtils.replaceVars(FCSTHOUR_URL_ZIP, getKey(), zip));
		return new Gson().fromJson(json, ForecastHour.class);
	}

	public static ForecastHour forecastHour(String loc, String city) throws Exception{
		String json = WebClient.request(StringUtils.replaceVars(FCSTHOUR_URL_CITY, getKey(), loc, city));
		return new Gson().fromJson(json, ForecastHour.class);
	}
	
	public static AlertInfo getAlerts(String zip) throws Exception{
		String json = WebClient.request(StringUtils.replaceVars(ALERTS_URL_ZIP, getKey(), zip));
		return new Gson().fromJson(json, AlertInfo.class);
	}

	public static AlertInfo getAlerts(String loc, String city) throws Exception{
		String json = WebClient.request(StringUtils.replaceVars(ALERTS_URL_CITY, getKey(), loc, city));
		return new Gson().fromJson(json, AlertInfo.class);
	}


	private static String getKey(){
		return Settings.Root.Weather.API_KEY;
	}




}
