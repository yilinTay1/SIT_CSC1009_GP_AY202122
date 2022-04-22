import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class OverseasAPICall {
    static final String SERVICE_URL = "http://apilayer.net/api/live?access_key=%s";
    static final boolean DEBUG = false;
    static double conversionRate= 0;

	public static double getRate(String countryEng) throws IOException{
		String data;
		System.out.println("Calling web service for exchange rates\n");
		data = queryExchangeRates();

		if (DEBUG)
			System.out.println("Service response:");
		if (DEBUG)
			System.out.println(data);

		System.out.println("USD to "+countryEng+" Exchange Rate:");
		System.out.println("1 United States Dollar (USD) = " + parseRate(countryEng, data) +" "+ countryEng);
		conversionRate = parseRate(countryEng, data);

		return conversionRate;
	}

	public static String queryExchangeRates(String... currencyCodes) throws IOException {
		final String CURRENCY_PARAM = "&currencies=%s";

		String urlstring = String.format(SERVICE_URL, Config.getApiKey());

		if (currencyCodes.length > 0) {
			urlstring += String.format(CURRENCY_PARAM, join(currencyCodes));
		}
		URL url = null;
		try {
			url = new URL(urlstring);
		} catch (MalformedURLException ex) {
			System.err.println("Invalid URL: " + urlstring);
			return "";
		}

		HttpURLConnection conn = (HttpURLConnection) url.openConnection();
		int responseCode = conn.getResponseCode();
		if (responseCode != HttpURLConnection.HTTP_OK) {
			System.err.println("Got HTTP Response code: " + responseCode);
			return "";
		}
		BufferedReader reader = new BufferedReader(
				new InputStreamReader(conn.getInputStream()));
		StringBuilder sb = new StringBuilder();
		String line = null;
		while ((line = reader.readLine()) != null)
			sb.append(line);
		reader.close();
		return sb.toString();
	}

	public static String join(String... strings) {
		return java.util.Arrays.stream(strings).collect(Collectors.joining(","));
	}

	public static double parseRate(String currencyCode, String data) {
		String regex = String.format("\"USD%s\":\\s*(\\d*.\\d+)", currencyCode);
		Pattern pattern = Pattern.compile(regex);
		Matcher matcher = pattern.matcher(data);

		if (!matcher.find())
			return 0.0;
		String value = matcher.group(1);
		if (DEBUG)
			System.out.printf("Found USD%s = %s\n", currencyCode, value);
		try {
			return Double.parseDouble(value);
		} catch (NumberFormatException nfe) {
			return 0.0;
		}
	}
}