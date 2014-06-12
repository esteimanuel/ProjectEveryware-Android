package nl.avans.glassy.Models;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Deelnemer implements Parcelable  {
	
	private String voornaam = "", tussenvoegsel = "", achternaam = "";
	private String fotoLink = "";
	private Buddy buddyData;
	private Boolean isBuddy = false;
	
	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(voornaam);
		out.writeString(tussenvoegsel);
		out.writeString(achternaam);
		out.writeString(fotoLink);
		out.writeInt(isBuddy ? 1 : 0);
		out.writeParcelable(buddyData, flags);
	}

	public static final Parcelable.Creator<Deelnemer> CREATOR = new Parcelable.Creator<Deelnemer>() {
		public Deelnemer createFromParcel(Parcel in) {
			return new Deelnemer(in);
		}

		public Deelnemer[] newArray(int size) {
			return new Deelnemer[size];
		}
	};

	private Deelnemer(Parcel in) {
		this.voornaam = in.readString();
		this.tussenvoegsel = in.readString();
		this.achternaam = in.readString();
		this.fotoLink = in.readString();
        this.isBuddy = (in.readInt() == 1);
		this.buddyData = in.readParcelable(this.getClass().getClassLoader());
	}

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

	public Buddy getBuddy() {
		return buddyData;
	}
}
