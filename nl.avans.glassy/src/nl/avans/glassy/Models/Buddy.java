package nl.avans.glassy.Models;

import org.json.JSONException;
import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Buddy implements Parcelable {
	private String tijd_vanaf, tijd_tot, locatie, contact_tel, contact_email;

	public int describeContents() {
		return 0;
	}

	public void writeToParcel(Parcel out, int flags) {
		out.writeString(tijd_vanaf);
		out.writeString(tijd_tot);
		out.writeString(locatie);
		out.writeString(contact_tel);
		out.writeString(contact_email);
	}

	public static final Parcelable.Creator<Buddy> CREATOR = new Parcelable.Creator<Buddy>() {
		public Buddy createFromParcel(Parcel in) {
			return new Buddy(in);
		}

		public Buddy[] newArray(int size) {
			return new Buddy[size];
		}
	};

	private Buddy(Parcel in) {
		this.tijd_vanaf = in.readString();
		this.tijd_tot = in.readString();
		this.locatie = in.readString();
		this.contact_tel = in.readString();
		this.contact_email = in.readString();
	}

	public Buddy(JSONObject buddyData) {
		try {
			if (!buddyData.isNull("tijd_vanaf")) {
				tijd_vanaf = (String) buddyData.get("tijd_vanaf").toString();
			}
		} catch (JSONException e) {
		}

		try {
			if (!buddyData.isNull("tijd_tot")) {
				tijd_tot = (String) buddyData.get("tijd_tot").toString();
			}
		} catch (JSONException e) {
		}

		try {
			if (!buddyData.isNull("locatie")) {
				locatie = (String) buddyData.get("locatie").toString();
			}
		} catch (JSONException e) {
		}

		try {
			if (!buddyData.isNull("contact_tel")) {
				contact_tel = (String) buddyData.get("contact_tel").toString();
			}
		} catch (JSONException e) {
		}

		try {
			if (!buddyData.isNull("contact_email")) {
				contact_email = (String) buddyData.get("contact_email")
						.toString();
			}
		} catch (JSONException e) {
		}
	}

	public String getTijdVanaf() {
		return tijd_vanaf;
	}

	public String getTijdTot() {
		return tijd_tot;
	}

	public String getLocatie() {
		return locatie;
	}

	public String getContactTel() {
		return contact_tel;
	}

	public String getContactEmail() {
		return contact_email;
	}
}