package xyz.migoo.report.html;

/**
* @author xiaomi
 */
public class RecordStore {
	
	private String expected;
	private String actual;
	private String result;
	private int pass ;
	private int fail ;

	private static RecordStore uniqueInstance = new RecordStore();

	public static RecordStore getInstance() {
		return uniqueInstance;
	}

	public String getExpected() {
		return expected;
	}

	public void setExpected(String expected) {
		this.expected = expected;
	}

	public String getActual() {
		return actual;
	}

	public void setActual(String actual) {
		this.actual = actual;
	}

	public String getResult() {
		return result;
	}

	public void setResult(String result) {
		this.result = result;
	}

	public int getPass() {
		return pass;
	}

	public void setPass(int pass) {
		this.pass = this.pass + pass;
	}

	public int getFail() {
		return fail;
	}

	public void setFail(int fail) {
		this.fail = this.fail + fail;
	}
}
