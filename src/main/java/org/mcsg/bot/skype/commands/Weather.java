package org.mcsg.bot.skype.commands;

import java.util.HashMap;

import org.mcsg.bot.skype.ChatManager;
import org.mcsg.bot.skype.Settings;
import org.mcsg.bot.skype.util.Arguments;
import org.mcsg.bot.skype.util.StringUtils;
import org.mcsg.bot.skype.web.WeatherUnderground;
import org.mcsg.bot.skype.web.WeatherUnderground.AlertInfo.Alert;
import org.mcsg.bot.skype.web.WeatherUnderground.ForecastDay;
import org.mcsg.bot.skype.web.WeatherUnderground.ForecastDay.Forecast.TxtForcast.Day;
import org.mcsg.bot.skype.web.WeatherUnderground.AlertInfo;
import org.mcsg.bot.skype.web.WeatherUnderground.ForecastHour;
import org.mcsg.bot.skype.web.WeatherUnderground.WeatherConditions;
import org.mcsg.bot.skype.web.WeatherUnderground.ForecastHour.HourlyForecast;
import org.mcsg.bot.skype.web.WeatherUnderground.WeatherConditions.CurrentObservation;

import com.skype.Chat;
import com.skype.User;

public class Weather implements SubCommand{

	private static final String CONDITIONS_FORMAT = "Current weather in {0}. {1}F/{2}C, {3}, Wind from {4} at {5}mph/{6}kph gusting to {7}mph/{8}kpm. Humidity, {9}. Feels like {10}F/{11}C";
	private static final String CONDITIONS_SHORT = "Current weather in {0}. {1}F/{2}C, {3}, Wind from {4} at {5}mph/{6}kph. Humidity, {7}";
	private static final String HOURLY_FORMAT = "{0}. {1} {2}F/{3}C, Winds at {4}Mph/{5}kph from {6}. Chance of rain {7}%";
	private static final String DAY_FORMAT = "{0}. {1}. Chance of rain {2}%";
	private static final String ALERTS_FORMAT = "ALERT. A {0} is in effect until {1}";
	
	private static int count = 0;
	private static Rate rate;

	@Override
	public void execute(Chat chat, User sender, String[] args) throws Exception {
		if(rate == null){
			rate = new Rate(); //simple rate limiting. 10 queries per minute.
			rate.start(); 	//Limit to 10 per 2 minutes to make sure we dont overlap minutes and get limited 
		}
		if(count > 8){
			chat.send("Weather rate limit exceeded. Try again in a couple minutes");
			return;
		} 
		
		count++;
		
		Arguments arge = new Arguments(args, "day/d", "hourly/hour/h", "metric/m", "number/n args");
		HashMap<String, String > swi = arge.getSwitches();
		args = arge.getArgs();
		WeatherConditions conditions;
		ForecastDay day = null;
		ForecastHour hour = null;
		AlertInfo alerts = null;

		if(args.length == 1){
			try { 
				Integer.parseInt(args[0]);
			} catch (Exception e){
				ChatManager.chat(chat, "Single argument must be a ZIP code");
				return;
			}

			conditions = WeatherUnderground.weatherQuery(args[0]);
			alerts = WeatherUnderground.getAlerts(args[0]);
			if(swi.containsKey("day"))
				day = WeatherUnderground.forecastDay(args[0]);
			if(swi.containsKey("hourly"))
				hour = WeatherUnderground.forecastHour(args[0]);
		} else {
			conditions = WeatherUnderground.weatherQuery(args[1], args[0]);
			alerts = WeatherUnderground.getAlerts(args[1], args[0]);
			if(swi.containsKey("day"))
				day = WeatherUnderground.forecastDay(args[1], args[0]);
			if(swi.containsKey("hourly"))
				hour = WeatherUnderground.forecastHour(args[0], args[0]);
		}

		if(conditions == null || conditions.response == null){
			ChatManager.chat(chat, "Failed to get weather");
		}
		else if(conditions.response.error != null){
			ChatManager.chat(chat, "Failed to get weather: "+conditions.response.error.description);
		} else {
			if(day == null && hour == null){
				CurrentObservation cur = conditions.current_observation;
				String weather = StringUtils.replaceVars(CONDITIONS_FORMAT, cur.display_location.full, cur.temp_f, cur.temp_c, cur.weather, 
						cur.wind_dir, cur.wind_mph, cur.wind_kph, cur.wind_gust_mph, cur.wind_gust_kph, cur.relative_humidity, cur.feelslike_f, cur.feelslike_c);
				ChatManager.chat(chat, weather);
			} else {
				CurrentObservation cur = conditions.current_observation;
				String weather = StringUtils.replaceVars(CONDITIONS_SHORT, cur.display_location.full, cur.temp_f, cur.temp_c, cur.weather, 
						cur.wind_dir, cur.wind_mph, cur.wind_kph, cur.relative_humidity);
				ChatManager.chat(chat, weather);

				int i = (swi.containsKey("number")) ? Integer.parseInt(swi.get("number")) : Settings.Root.Weather.max_forecast;

				if(hour != null){
					for(int a = 0; a < i; a++){
						HourlyForecast fcst = hour.hourly_forecast[a];
						String hourw = StringUtils.replaceVars(HOURLY_FORMAT, fcst.FCTTIME.civil, fcst.wx, fcst.temp.english, fcst.temp.metric, 
								fcst.wspd.english, fcst.wspd.metric, fcst.wdir.dir, fcst.pop);
						ChatManager.chat(chat, hourw);
					}
				}
				if(day != null){
					for(int a = 0; a < i; a++){
						Day fcst = day.forecast.txt_forecast.forecastday[a];
						String hourw;
						if(swi.containsKey("metric"))
							hourw = StringUtils.replaceVars(DAY_FORMAT, fcst.title, fcst.fcttext_metric, fcst.pop);
						else
							hourw = StringUtils.replaceVars(DAY_FORMAT, fcst.title, fcst.fcttext, fcst.pop);
						ChatManager.chat(chat, hourw);
					}
				}
			}

			if(alerts != null){
				for(Alert alert : alerts.alerts){
					ChatManager.chat(chat,  sender, StringUtils.replaceVars(ALERTS_FORMAT, alert.description, alert.expires));
				}
			}
		}

	}
	
	class Rate extends Thread{
		public void run(){
			while(true){
				count = 0;
				try { sleep(300000); } catch (Exception e){}
			}
		}
	}

	@Override
	public String getHelp() {
		return "Get the current weather in your area";
	}

	@Override
	public String getUsage() {
		return "weather <city> <state | country>";
	}

  @Override
  public String getCommand() {
    return "weather";
  }

  @Override
  public String[] getAliases() {
    return a("w");
  }

}
