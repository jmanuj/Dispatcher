package qubole.Dispatcher;

public class Driver {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		IDispatcher dispatcher = new HashMapDispatcher(3);
		
		String pred = "(c1 == \"in\") && (c2 == \"home.php\" || c2 ==\"news.php\") && (c3 == \"huffpost.com\")";
		
		try {
			
			for(int i = 0; i < 9000; i++) {
				dispatcher.registerSubscriber(String.valueOf(i), pred);
			}
			
			String[] event = {"in", "blah", "wsj.com"};	//match
			String[] event1 = {"hk", "news.php"};		//no match
			
			long start = System.currentTimeMillis();
			//for(long i = 0; i < 99999999; i++) {
				dispatcher.findMatchingIds(event);
				dispatcher.findMatchingIds(event1);
			//}
			long end = System.currentTimeMillis();
			
			System.out.println(end - start);
			
		} catch(Exception e) {
			System.out.println("ex: " + e.getMessage());
		}
	}

}
