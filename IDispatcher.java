package qubole.Dispatcher;

import java.util.List;

public interface IDispatcher {
	public void registerSubscriber(String subscriberId, String predicate) throws Exception;
	public List<String> findMatchingIds(String[] event) throws Exception;
	
	public void printTest();
}
