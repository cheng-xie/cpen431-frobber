package me.lise.CPEN431.Frobber.test.helper;

public class TestResult {
	public enum TestStatus {
		PASSED, FAILED, UNDECIDED;
		public String format() {
			switch(this) {
			case PASSED:
				return "Passed";
			case FAILED:
				return "Failed";
			case UNDECIDED:
				return "Undecided";
			default:
				return "";
			}
		}
	}

	public TestStatus status;
	String msg;
	public TestResult(TestStatus status, String msg) {
		this.status = status;
		this.msg = msg;
	}
	public static TestResult passed(String msg) {
		return new TestResult(TestStatus.PASSED, msg);
	}
	public static TestResult failed(String msg) {
		return new TestResult(TestStatus.FAILED, msg);
	}
	public static TestResult undecided(String msg) {
		return new TestResult(TestStatus.UNDECIDED, msg);
	}
	public String format() {
		return String.format("Test %s with msg: <%s>", this.status.format(), this.msg);
	}
}
