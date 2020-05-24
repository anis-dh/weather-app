package com.scofield.weather.WeatherApp.view;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import com.scofield.weather.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Alignment;
import com.vaadin.ui.Button;
import com.vaadin.ui.HorizontalLayout;
import com.vaadin.ui.Image;
import com.vaadin.ui.Label;
import com.vaadin.ui.NativeSelect;
import com.vaadin.ui.Notification;
import com.vaadin.ui.TextField;
import com.vaadin.ui.UI;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;

@SpringUI(path = "")
public class MainView extends UI {

	@Autowired
	private WeatherService weatherService;
	private VerticalLayout mainLayout;
	private NativeSelect<String> unitSelect;
	private TextField cityTextField;
	private Button showWeatherButton;
	private Label currentLocationLabel;
	private Label currentTemperatureLabel;
	private Label weatherDescriptionLabel;
	private Label weatherMinTemperatureLabel;
	private Label weatherMaxTemperatureLabel;
	private Label pressureLabel;
	private Label humidityLabel;
	private Label windSpeedLabel;
	private Label sunRiseLabel;
	private Label sunSetLabel;
//	private ExternalResource weatherIcon;
	private Image weatherIcon;
	private HorizontalLayout dashBoardMain;
	private HorizontalLayout mainDescriptionLayout;
	private VerticalLayout descriptionLayout;
	private VerticalLayout detailsLayout;

	@Override
	protected void init(VaadinRequest request) {

		setUpLayout();
		setHeader();
		setLogo();
		setUpForm();
		dashBoardTitle();
		dadhBoardDescription();

		showWeatherButton.addClickListener(event -> {
			if (!cityTextField.getValue().equals("")) {
				try {
					updateUI();
				} catch (JSONException e) {
					e.printStackTrace();
				}
			} else {
				Notification.show("Please enter a city name.");
			}
		});

	}

	public void setUpLayout() {

		weatherDescriptionLabel = new Label("");
		weatherMinTemperatureLabel = new Label("");
		weatherMaxTemperatureLabel = new Label("");
		pressureLabel = new Label("");
		humidityLabel = new Label("");
		windSpeedLabel = new Label("");
		sunRiseLabel = new Label("");
		sunSetLabel = new Label("");
		weatherIcon = new Image();
		mainLayout = new VerticalLayout();
		mainLayout.setWidth("100%");
		mainLayout.setMargin(true);
		mainLayout.setSpacing(true);
		mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		setContent(mainLayout);

	}

	private void setHeader() {

		HorizontalLayout headerLayout = new HorizontalLayout();
		headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		Label title = new Label("Weather info:");
		title.addStyleName(ValoTheme.LABEL_H1);
		title.addStyleName(ValoTheme.LABEL_BOLD);
		title.addStyleName(ValoTheme.LABEL_COLORED);

		headerLayout.addComponent(title);

		mainLayout.addComponent(headerLayout);
	}

	private void setLogo() {

		HorizontalLayout logoLayout = new HorizontalLayout();
		logoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		Image icon = new Image(null, new ClassResource("/icons/weather_icon.png"));
		icon.setWidth("125px");
		icon.setHeight("125px");

		logoLayout.addComponents(icon);

		mainLayout.addComponents(logoLayout);
	}

	private void setUpForm() {

		HorizontalLayout formLayout = new HorizontalLayout();
		formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		formLayout.setSpacing(true);
		formLayout.setMargin(true);

		unitSelect = new NativeSelect<>();
		unitSelect.setWidth("30px");
		ArrayList<String> units = new ArrayList<>();
		units.add("C");
		units.add("F");
		unitSelect.setItems(units);
		unitSelect.setValue(units.get(0));

		cityTextField = new TextField();
		cityTextField.setWidth("50%");

		HorizontalLayout searchLayout = new HorizontalLayout();
		showWeatherButton = new Button("Search");
		showWeatherButton.setIcon(VaadinIcons.SEARCH);

		formLayout.addComponents(cityTextField);
		searchLayout.addComponents(showWeatherButton, unitSelect);
		mainLayout.addComponents(formLayout, searchLayout);

	}

	private void dashBoardTitle() {

		dashBoardMain = new HorizontalLayout();
		dashBoardMain.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		currentLocationLabel = new Label(" ");
		currentLocationLabel.addStyleName(ValoTheme.LABEL_H2);
		currentLocationLabel.addStyleName(ValoTheme.LABEL_LIGHT);

		currentTemperatureLabel = new Label(" ");
		currentTemperatureLabel.addStyleName(ValoTheme.LABEL_H1);
		currentTemperatureLabel.addStyleName(ValoTheme.LABEL_BOLD);
		currentTemperatureLabel.addStyleName(ValoTheme.LABEL_LIGHT);

	}

	private void dadhBoardDescription() {

		mainDescriptionLayout = new HorizontalLayout();
		mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		descriptionLayout = new VerticalLayout();
		descriptionLayout.addStyleName(ValoTheme.FORMLAYOUT_LIGHT);
		descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
		weatherDescriptionLabel.addStyleName(ValoTheme.LABEL_H3);

		descriptionLayout.addComponents(weatherDescriptionLabel, weatherMinTemperatureLabel,
				weatherMaxTemperatureLabel);

		detailsLayout = new VerticalLayout();
		detailsLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

		detailsLayout.addComponents(pressureLabel, humidityLabel, windSpeedLabel, sunRiseLabel, sunSetLabel);

	}

	private void updateUI() throws JSONException {

		String cityName = cityTextField.getValue();
		String defaultUnit;
		String tempUnit;
		String windUnit;

		if (unitSelect.getValue().equals("C")) {
			defaultUnit = "metric";
			unitSelect.setValue("C");
			tempUnit = "\u00b0" + "C";
			windUnit = " m/s";
		} else {
			defaultUnit = "imperial";
			unitSelect.setValue("F");
			tempUnit = "\u00b0" + "F";
			windUnit = " m/h";
		}

		weatherService.setCityName(cityName);
		weatherService.setUnit(defaultUnit);

		currentLocationLabel.setValue("Currently in: " + cityName);

		JSONObject weatherObject = weatherService.returnMainObject();
		int temperature = weatherObject.getInt("temp");

		currentTemperatureLabel.setValue(temperature + tempUnit);

		int minTemperature = weatherObject.getInt("temp_min");
		int maxTemperature = weatherObject.getInt("temp_max");
		int pressure = weatherObject.getInt("pressure");
		int humidity = weatherObject.getInt("humidity");

		JSONObject windObject = weatherService.returnWindObject();
		double windSpeed = windObject.getDouble("speed");

		JSONObject systemObject = weatherService.returnSunSet();
		long sunRise = systemObject.getLong("sunrise") * 1000;
		long sunSet = systemObject.getLong("sunset") * 1000;

		String iconCode = "";
		String description = "";
		JSONArray weatherInfo = weatherService.returnWeatherArray();

		weatherIcon.setSource(new ExternalResource("http://openweathermap.org/img/wn/" + iconCode + ".png"));

		for (int i = 0; i < weatherInfo.length(); i++) {
			JSONObject weatherInfoObject = weatherInfo.getJSONObject(i);
			description = weatherInfoObject.getString("description");
			iconCode = weatherInfoObject.getString("icon");
		}

		dashBoardMain.addComponents(currentLocationLabel, weatherIcon, currentTemperatureLabel);
		mainLayout.addComponents(dashBoardMain);

		weatherDescriptionLabel.setValue(description);
		weatherMinTemperatureLabel.setValue("Min: " + String.valueOf(minTemperature) + tempUnit);
		weatherMaxTemperatureLabel.setValue("Max: " + String.valueOf(maxTemperature) + tempUnit);
		pressureLabel.setValue("Pressure: " + String.valueOf(pressure) + " hpa");
		humidityLabel.setValue("Humidity: " + String.valueOf(humidity) + " %");
		windSpeedLabel.setValue("Wind: " + String.valueOf(windSpeed) + windUnit);
		sunRiseLabel.setValue("Surise: " + convertTime(sunRise));
		sunSetLabel.setValue("Sunset: " + convertTime(sunSet));

		mainDescriptionLayout.addComponents(descriptionLayout, detailsLayout);
		mainLayout.addComponents(mainDescriptionLayout);

	}

	private String convertTime(long time) {

		SimpleDateFormat dateFormat = new SimpleDateFormat("hh.mm aa");
		return dateFormat.format(new Date(time));
	}

}
