package qubole.Dispatcher;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IDispatcher dispatcher = new HashMapDispatcher(3);
		
		String pred = "(c1 == \"in\") && (c2 == \"home.php\" || c2 ==\"news1.php\") && (c3 == \"huffpost.com\")";
		
		try {
			
			dispatcher.registerSubscriber("12345", pred);
			
			String[] event = {"in", "news.php", "huffpost.com"};		//no match
			long start = System.currentTimeMillis();
			System.out.println(dispatcher.findMatchingIds(event));
			long end = System.currentTimeMillis();
			
			System.out.println(end - start);
		} catch(Exception e) {
			System.out.println("ex: " + e.getMessage());
		}
	}

}
