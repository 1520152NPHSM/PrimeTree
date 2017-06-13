package backendServer.clientDatabaseAccess.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**This class represents a user with all userdata the client has in his database
 * It does not have fields exclusively relevant for the bIT Kleinanzeigen project
 * 
 * @author Florian Kutz
 * */
@Entity
@Table(name = "employee")
public class EmployeeData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;

	@Column(name="Vorname")
	private String vorname;

	@Column(name="Nachname")
	private String nachname;

	@Column(name="Login")
	private String login;

	@ManyToOne
	private Location Standort;

	@Column(name="SkillLevel")
	private String skillLevel;

	@Column(name="Foto")
	private String foto;
	
	private String eMail;
	
	private String telefonNummer;

	public String geteMail() {
		return eMail;
	}

	public void seteMail(String eMail) {
		this.eMail = eMail;
	}

	public String getTelefonNummer() {
		return telefonNummer;
	}

	public void setTelefonNummer(String telefonNummer) {
		this.telefonNummer = telefonNummer;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getVorname() {
		return vorname;
	}

	public void setVorname(String vorname) {
		this.vorname = vorname;
	}

	public String getNachname() {
		return nachname;
	}

	public void setNachname(String nachname) {
		this.nachname = nachname;
	}

	public String getLogin() {
		return login;
	}

	public void setLogin(String login) {
		this.login = login;
	}

	public Location getStandort() {
		return Standort;
	}

	public void setStandort(Location standort) {
		Standort = standort;
	}

	public String getSkillLevel() {
		return skillLevel;
	}

	public void setSkillLevel(String skillLevel) {
		this.skillLevel = skillLevel;
	}

	public String getFoto() {
		return foto;
	}

	public void setFoto(String foto) {
		this.foto = foto;
	}
	
	

}
