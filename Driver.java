package qubole.Dispatcher;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IDispatcher dispatcher = new HashMapDispatcher(3);
		
		String pred1 = "(c1 == \"in\") && (c3 == \"wsj.com\")";
		String pred2 = "(c1 == \"in\") && (c3 == \"wsjh.com\" || c3 == \"nytimes.com\")";
		String pred3 = "(c1 == \"in\") && (c2 == \"home.php\" || c2 ==\"news.php\") && (c3 == \"huffpost.com\")";
		
		try {
			dispatcher.registerSubscriber("123", pred1);
			dispatcher.registerSubscriber("234", pred2);
			dispatcher.registerSubscriber("234", pred3);
			
			String[] event = {"in", "blah", "wsj.com"};
			
			System.out.println(dispatcher.findMatchingIds(event));
			
		} catch(Exception e) {
			System.out.println("ex: " + e.getMessage());
		}
	}

}
