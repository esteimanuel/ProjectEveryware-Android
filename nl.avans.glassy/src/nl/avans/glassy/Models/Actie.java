package nl.avans.glassy.Models;

import nl.avans.glassy.Threads.ActieManager;
import nl.avans.glassy.Threads.ActieTask;

import org.json.JSONObject;

import android.os.Parcel;
import android.os.Parcelable;

public class Actie implements Parcelable {
	private JSONObject actieJSON;

	// The Thread that will be used to download the image for this ImageView
	private ActieTask mDownloadThread;

	private int wijk_id;
	private int target;
	private int aantal_huishoudens;
	private int actie_id = -1;
	private String wijk_naam;

	public Actie() {

	}

	public int getWijk_id() {
		return wijk_id;
	}

	public void setWijk_id(int wijk_id) {
		this.wijk_id = wijk_id;
	}

	public int getTarget() {
		return target;
	}

	public void setTarget(int target) {
		this.target = target;
	}

	public int getAantal_huishoudens() {
		return aantal_huishoudens;
	}

	public void setAantal_huishoudens(int aantal_huishoudens) {
		this.aantal_huishoudens = aantal_huishoudens;
	}

	public String getWijk_naam() {
		return wijk_naam;
	}

	public void setWijk_naam(String wijk_naam) {
		this.wijk_naam = wijk_naam;
	}

	public void startDecode() {
		// Starts decoding the JSON
		mDownloadThread = ActieManager.startDecode(this);
	}

	public void setActieJSON(JSONObject jActie) {
		actieJSON = jActie;
	}

	public JSONObject getActieJSON() {
		return actieJSON;
	}

	private Actie(Parcel in) {
		wijk_id = in.readInt();
		target = in.readInt();
		aantal_huishoudens = in.readInt();
		wijk_naam = in.readString();
	}

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(wijk_id);
		dest.writeInt(target);
		dest.writeInt(aantal_huishoudens);
		dest.writeString(wijk_naam);

	}

	public static final Parcelable.Creator<Actie> CREATOR = new Parcelable.Creator<Actie>() {
		public Actie createFromParcel(Parcel in) {
			return new Actie(in);
		}

		public Actie[] newArray(int size) {
			return new Actie[size];
		}
	};
}
