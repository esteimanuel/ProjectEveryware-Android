package nl.avans.glassy.Models;

import android.os.Parcel;
import android.os.Parcelable;

public class Wijk implements Parcelable {
	private String wijkName;
	private String deelnemersCount;
	private String targetPercentage;

	// Constructor
	public Wijk(String wijkName, String deelnemersCount, String targetPercentage) {
		this.wijkName = wijkName;
		this.deelnemersCount = deelnemersCount;
		this.targetPercentage = targetPercentage;
	}

	public String getWijkName() {
		return wijkName;
	}

	public void setWijkName(String wijkName) {
		this.wijkName = wijkName;
	}

	public String getDeelnemersCount() {
		return deelnemersCount;
	}

	public void setDeelnemersCount(String deelnemersCount) {
		this.deelnemersCount = deelnemersCount;
	}

	public String getTargetPercentage() {
		return targetPercentage;
	}

	public void setTargetPercentage(String targetPercentage) {
		this.targetPercentage = targetPercentage;
	}
	
	 // Parcelling part
    public Wijk(Parcel in){
        String[] data = new String[3];

        in.readStringArray(data);
        this.wijkName = data[0];
        this.deelnemersCount = data[1];
        this.targetPercentage = data[2];
    }

	@Override
	public int describeContents() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		// TODO Auto-generated method stub

	}

}
