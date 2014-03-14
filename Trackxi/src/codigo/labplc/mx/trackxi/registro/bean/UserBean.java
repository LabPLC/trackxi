package codigo.labplc.mx.trackxi.registro.bean;

/*
 * clase bean que maneja los datos del usuario
 */
public class UserBean {
	private String nombre;//nombre de usuario
	private String correo; //correo del usuario
	private String tel; //telefono del usuario
	private String telemergencia; //telefono de emergencia
	private String UUID; //llave del usuario
	private String correoemergencia; //correo de emergencia
	private String foto;//foto del usuario
	private String os = "2"; //1 IOS 2 android
	public String getFoto() {
		return foto;
	}
	public void setFoto(String foto) {
		this.foto = foto;
	}
	public String getNombre() {
		return nombre;
	}
	public void setNombre(String nombre) {
		this.nombre = nombre;
	}
	public String getCorreo() {
		return correo;
	}
	public void setCorreo(String correo) {
		this.correo = correo;
	}
	public String getTel() {
		return tel;
	}
	public void setTel(String tel) {
		this.tel = tel;
	}
	public String getTelemergencia() {
		return telemergencia;
	}
	public void setTelemergencia(String telemergencia) {
		this.telemergencia = telemergencia;
	}
	public String getUUID() {
		return UUID;
	}
	public void setUUID(String uUID) {
		UUID = uUID;
	}
	public String getCorreoemergencia() {
		return correoemergencia;
	}
	public void setCorreoemergencia(String correoemergencia) {
		this.correoemergencia = correoemergencia;
	}
	public String getOs() {
		return os;
	}
	public void setOs(String os) {
		this.os = os;
	}
	
	
}