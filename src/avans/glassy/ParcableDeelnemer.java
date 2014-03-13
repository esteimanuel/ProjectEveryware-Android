package avans.glassy;

import android.os.Parcel;
import android.os.Parcelable;

public class ParcableDeelnemer implements Parcelable {
    private Deelnemer deelnemer;
 
    public Deelnemer getDeelnemer() {
        return deelnemer;
    }
    
    public ParcableDeelnemer(Deelnemer deelnemer) {
        super();
        this.deelnemer = deelnemer;
    }
    
    private ParcableDeelnemer(Parcel in) {
    	deelnemer = new Deelnemer(in.readString(), in.readString(), in.readInt());
    }    
    
    /*
     * you can use hashCode() here.
     */
    @Override
    public int describeContents() {
        return 0;
    }
    
    /*
     * Actual object Serialization/flattening happens here. You need to
     * individually Parcel each property of your object.
     */
    @Override
    public void writeToParcel(Parcel parcel, int flags) {
		parcel.writeString(deelnemer.getVoornaam());
		parcel.writeString(deelnemer.getAchternaam());
		parcel.writeInt(deelnemer.getProfielFoto());
    }
    
    /*
     * Parcelable interface must also have a static field called CREATOR,
     * which is an object implementing the Parcelable.Creator interface.
     * Used to un-marshal or de-serialize object from Parcel.
     */
    public static final Parcelable.Creator<ParcableDeelnemer> CREATOR =
            new Parcelable.Creator<ParcableDeelnemer>() {
        public ParcableDeelnemer createFromParcel(Parcel in) {
            return new ParcableDeelnemer(in);
        }
 
        public ParcableDeelnemer[] newArray(int size) {
            return new ParcableDeelnemer[size];
        }
    };
}
