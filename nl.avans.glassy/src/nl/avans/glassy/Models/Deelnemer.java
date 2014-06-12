package nl.avans.glassy.Models;

import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

public class Deelnemer {
	private class Buddy {
		private String tijd_vanaf = "", tijd_tot = "", locatie = "",
				contact_tel = "", contact_email = "";

		public Buddy(JSONObject buddyData) {

		}

		public String tijd_vanaf() {
			return tijd_vanaf;
		}

		public String tijd_tot() {
			return tijd_tot;
		}

		public String locatie() {
			return locatie;
		}

		public String contact_tel() {
			return contact_tel;
		}

		public String contact_email() {
			return contact_email;
		}
	}

	private String voornaam = "", tussenvoegsel = "", achternaam = "";
	private String fotoLink = "";
	private Buddy buddyData;
	private Boolean isBuddy = false;

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

		try {
			JSONObject buddy = data.getJSONObject("buddy");
			isBuddy = true;
			buddyData = new Buddy(buddy);
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

	public Boolean isBuddy() {
		return isBuddy;
	}
	
	public Buddy getBuddy(){
		return buddyData;
	}
}
