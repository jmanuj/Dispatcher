package qubole.Dispatcher;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IDispatcher dispatcher = new HashMapDispatcher(3);
		
		String pred = "(c1 == \"in\") && (c2 == \"home.php\" || c2 ==\"news.php\") && (c3 == \"huffpost.com\")";
		
		try {
			
			dispatcher.registerSubscriber("123", pred);
			
			String[] event = {"in", "blah", "wsj.com"};	//match
			String[] event1 = {"in", "news.php", "huffpost.com"};		//no match
			
			System.out.println(dispatcher.findMatchingIds(event));
			System.out.println(dispatcher.findMatchingIds(event1));
		} catch(Exception e) {
			System.out.println("ex: " + e.getMessage());
		}
	}

}
