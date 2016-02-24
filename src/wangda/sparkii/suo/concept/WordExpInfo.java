package wangda.sparkii.suo.concept;

public class WordExpInfo {// 如果要网络传输这个类，可以将其序列化： implements Serializable
	private String name;
	private String expId;
	private String expUser;
	private String exp;

	public WordExpInfo(String name, String expId, String expUser, String exp) {
		super();
		this.name = name;
		this.expId = expId;
		this.expUser = expUser;
		this.exp = exp;
	}

	public WordExpInfo() {
		super();
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getExpId() {
		return expId;
	}

	public void setExpId(String expId) {
		this.expId = expId;
	}

	public String getExpUser() {
		return expUser;
	}

	public void setExpUser(String expUser) {
		this.expUser = expUser;
	}

	public String getExp() {
		return exp;
	}

	public void setExp(String exp) {
		this.exp = exp;
	}

	@Override
	public String toString() {
		return "WordExpInfo [name=" + name + ", expId=" + expId + ", expUser="
				+ expUser + ", exp=" + exp + "]";
	}

}
