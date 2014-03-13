package avans.glassy;

public class Deelnemer {
	private String voornaam, achternaam;
	private Integer profielFoto;
	
	public Deelnemer(String sVoornaam, String sAchternaam, Integer dProfielFoto){
		this.voornaam = sVoornaam;
		this.achternaam = sAchternaam;
		this.profielFoto = dProfielFoto;
	}
	
	public String getVoornaam(){
		return voornaam;
	}
	
	public void setVoornaam(String voornaam) {
	      this.voornaam = voornaam;
	}
	
	public String getAchternaam(){
		return achternaam;
	}
	
	public void setAchternaam(String achternaam) {
	      this.achternaam = achternaam;
	}
	
	public Integer getProfielFoto(){
		return profielFoto;
	}
	
	public void setProfielFoto(Integer profielFoto) {
	      this.profielFoto = profielFoto;
	}
}
