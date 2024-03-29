package nl.avans.glassy.Utils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.Arrays;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpDelete;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONTokener;

import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

public class ApiCommunicator extends AsyncTask<String, JSONObject, JSONObject> {

	private static String BASE_URL = "http://glassy-api.avans-project.nl/api/";
	private static final String[] ACCEPTED_REQUEST_METHODS = new String[] {
			"GET", "POST", "PUT", "DELETE" };
	private HttpClient httpClient = new DefaultHttpClient();

	private Context context;

	public ApiCommunicator(Context context) {
		this.context = context;
	}

	/**
	 * makes a httpgetrequest and gives a httpresponse
	 * 
	 * @param String
	 *            url
	 * @return HttpResponse response
	 */
	private HttpResponse prepareAndExecuteGetRequest(String url) {

		try {

			System.out.println("GET!");
			HttpGet request = new HttpGet(url);
			return httpClient.execute(request);

		} catch (Exception e) {

			return null;
		}
	}

	/**
	 * makes a httppostrequet and gives a httpresponse
	 * 
	 * @param String
	 *            url
	 * @param JSONObject
	 *            body
	 * @return HttpResponse response
	 */
	private HttpResponse prepareAndExecutePostRequest(String url,
			JSONObject body) {

		try {

			System.out.println("POST!");
			HttpPost request = new HttpPost(url);
			request.setEntity(new StringEntity(body.toString(), "UTF8"));
			request.setHeader("Content-type", "application/json");

			return httpClient.execute(request);

		} catch (Exception e) {

			// TODO: handle exception
			return null;
		}
	}

	private HttpResponse prepareAndExecutePutRequest(String url, JSONObject body) {

		try {

			System.out.println("PUT!");
			HttpPut request = new HttpPut(url);
			request.setEntity(new StringEntity(body.toString(), "UTF8"));
			request.setHeader("Content-type", "application/json");

			return httpClient.execute(request);

		} catch (Exception e) {

			// TODO: handle exception
			return null;
		}
	}

	private HttpResponse prepareAndExecuteDeleteRequest(String url) {

		try {

			System.out.println("DELETE!");
			HttpDelete request = new HttpDelete(url);

			return httpClient.execute(request);

		} catch (Exception e) {

			// TODO: handle exception
			return null;
		}
	}

	@Override
	protected JSONObject doInBackground(String... arg0) {

		// get the parameters
		String method = arg0[0].toUpperCase();
		String url = BASE_URL + arg0[1];
		JSONObject requestBody = null;

		try {
			requestBody = new JSONObject(arg0[2]); // there might be a body in
													// the parameter array

		} catch (Exception e) {

			System.out.println("No requestbody given");
		}

		if (!Arrays.asList(ACCEPTED_REQUEST_METHODS).contains(method)) { // validate
																			// method

			System.out.println("Non supported http method found: " + method);
			System.out.println("please use: GET, POST, PUT or DELETE");

			return null;
		}

		return executeRequest(method, url, requestBody); // doejeding
	}

	/**
	 * prepares a request for the given method and url.
	 * 
	 * @param String
	 *            method
	 * @param String
	 *            url
	 * @param JSONObject
	 *            requestBody
	 * @return JSONObject responseBody
	 */
	private JSONObject executeRequest(String method, String url,
			JSONObject requestBody) {

		HttpResponse response = null;

		if (method.equals("GET")) {

			response = prepareAndExecuteGetRequest(url);

		} else if (method.equals("POST")) {

			response = prepareAndExecutePostRequest(url, requestBody);

		} else if (method.equals("PUT")) {

			response = prepareAndExecutePutRequest(url, requestBody);

		} else if (method.equals("DELETE")) {

			response = prepareAndExecuteDeleteRequest(url);
		}

		return parseHttpResponse(response);
	}

	/**
	 * this function tries to parse a given HttpResponse.
	 * 
	 * @param HttpResponse
	 *            response
	 * @return JSONObject responseBody
	 */
	protected JSONObject parseHttpResponse(HttpResponse response) {

		if (response == null) {

			System.out.println("No HttpResponse");
			return null;
		}

		StringBuilder builder = new StringBuilder();

		try {

			BufferedReader reader = new BufferedReader(new InputStreamReader(
					response.getEntity().getContent(), "UTF-8"));

			String line = null;
			while ((line = reader.readLine()) != null) {
				builder.append(line).append("\n");
			}

			Log.i("parse http response", builder.toString());

			JSONObject retval = (JSONObject) new JSONTokener(builder.toString())
					.nextValue();

			return retval;

		} catch (ClassCastException cce) {

			try {

				JSONObject retval = new JSONObject();
				retval.put("entries",
						(JSONArray) new JSONTokener(builder.toString())
								.nextValue());
				return retval;

			} catch (Exception e) {

				e.printStackTrace();
				return null;
			}

		} catch (Exception e) {

			e.printStackTrace();
			return null;
		}
	}

	protected Context getContext() {

		return this.context;
	}
}
