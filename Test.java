package qubole.Dispatcher;

public class Test {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String x = "(c1==\"in\") || (c2)";
		if(x.contains("||")) {
			System.out.println("y");
		} else {
			System.out.println("n");
		}
		
	}

}
