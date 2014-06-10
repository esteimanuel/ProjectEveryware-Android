package nl.avans.glassy.Models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Deelnemer {
	private String voornaam = "", tussenvoegsel = "", achternaam = "";
	private String fotoLink = "";

	public Deelnemer(JSONObject data) {
		try {
			if (data.get("voornaam") != null) {
				voornaam = (String) data.get("voornaam").toString();
			}
		} catch (JSONException e) {
		}

		try {
			if (data.get("tussenvoegsel") != null) {
				tussenvoegsel = (String) data.get("tussenvoegsel").toString();
			}
		} catch (JSONException e) {
		}

		try {
			if (data.get("achternaam") != null) {
				achternaam = (String) data.get("achternaam").toString();
			}
		} catch (JSONException e) {
		}

		try {
			JSONObject account = data.getJSONObject("account");
			fotoLink = (String) account.get("foto_link").toString();
		} catch (JSONException e) {
			e.printStackTrace();
		}

	}

	public String getVoornaam() {
		return voornaam;
	}

	public String getTussenvoegsel() {
		return tussenvoegsel;
	}

	public String getAchternaam() {
		return achternaam;
	}

	public String getFotoLink() {
		return fotoLink;
	}
}
