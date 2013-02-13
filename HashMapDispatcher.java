package qubole.Dispatcher;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HashMapDispatcher implements IDispatcher {

	/**
	 * dafault columns in an event
	 */
	private int maxEventCols = 2;
	
	/**
	 * each subscriber has a list of hashmaps describing its predicates
	 */
	private Map<String, List<Map<String, List<String>>>> subscriberPredicates = new HashMap<String, List<Map<String, List<String>>>> ();
	
	private Map<String, List<String>> predicateParser(String predicate) throws Exception {
		/**
		 * input format:
		 * (c1 == "in") && (c2 == "home.php" || c2 == "news.php") && (c3 == "nytimes.com" || c3 == "wsj.com")
		 * conjucntion of disjunctions of literals (predicates)
		 * 
		 * output format: a hashmap
		 * example:
		 * c1 => ["in", "hk"],
		 * c3 => ["home.php"],
		 * c4 => ["nytimes.com", "wsj.com"]
		 * c7 => ["6342"]
		 * ...
		 * 
		 * && between hashmap entries
		 * || between hashmap value list entries
		 * 
		 */
		
		Map<String, List<String>> map = new HashMap<String, List<String>>();
		
		predicate = predicate.replaceAll(" ", "").toLowerCase();
		
		String[] clauses = predicate.split("&&");
		
		for(String clause : clauses) {
			if(clause.contains("||")) {	//contains disjunctions of clauses
				String[] subClauses = clause.split("\\|\\|");
				for(String subClause : subClauses) {
					String[] kv = subClause.replaceAll("\\(", "").replaceAll("\\)", "").split("==");
					Pattern pattern = Pattern.compile("^c(\\d+)$");
					Matcher m = pattern.matcher(kv[0]);
					if(m.matches()) {
						int clauseKeyNumber = Integer.parseInt(m.group(1)); 
						if(clauseKeyNumber > this.maxEventCols && clauseKeyNumber <= 0) {
							throw new Exception("Invalid predicate clause key numbering");
						} else {
							if(!map.containsKey(kv[0])) {
								map.put(kv[0], new ArrayList(Arrays.asList(kv[1].replaceAll("\"", ""))));
							} else {
								map.get(kv[0]).add(kv[1].replaceAll("\"", ""));
							}
						}
					} else {
						throw new Exception("Invalid predicate clause key");
					}
				}
			} else {	//only clause
				String[] kv = clause.replaceAll("\\(", "").replaceAll("\\)", "").split("==");
				Pattern pattern = Pattern.compile("^c(\\d+)$");
				Matcher m = pattern.matcher(kv[0]);
				if(m.matches()) {
					int clauseKeyNumber = Integer.parseInt(m.group(1)); 
					if(clauseKeyNumber > this.maxEventCols && clauseKeyNumber <= 0) {
						throw new Exception("Invalid predicate clause key numbering. maxEventCols = " + this.maxEventCols);
					} else {
						map.put(kv[0], new ArrayList(Arrays.asList(kv[1].replaceAll("\"", ""))));
					}
				} else {
					throw new Exception("Invalid predicate clause key");
				}
			}
		}
		
		return map;
	}
	
	public HashMapDispatcher(int maxEventCols) {
		if(maxEventCols > 0) {
			this.maxEventCols = maxEventCols;
		}
	}
	
	@Override
	public void registerSubscriber(String subscriberId, String predicate) throws Exception {
		if(!subscriberPredicates.containsKey(subscriberId)) {
			List<Map<String, List<String>>> list = new ArrayList<Map<String, List<String>>>();
			list.add(predicateParser(predicate));
			subscriberPredicates.put(subscriberId, list);
		} else {
			subscriberPredicates.get(subscriberId).add(predicateParser(predicate));
		}
	}

	private Map<String, String> convertStringArrayToMap(String[] event) {
		Map<String, String> map = new HashMap<String, String>();
		for(int i = 0; i < event.length; i++) {
			map.put("c" + (i+1), event[i]);
		}
		return map;
	}
	
	@Override	
	public List<String> findMatchingIds(String[] event) throws Exception {
		
		List<String> matchingSubscriberIds = new ArrayList<String>();
		
		Map<String, String> eventMap = convertStringArrayToMap(event);
		
		if(subscriberPredicates.size() > 0) {
			/*
			 * List<Map<String, List<String>>>
			 */
			for(String subscriberId : subscriberPredicates.keySet()) {
				List<Map<String, List<String>>> listOfPredicatMaps = subscriberPredicates.get(subscriberId);
				
				/* find if this event match any of the maps */
				for(Map<String, List<String>> predicateMap : listOfPredicatMaps) {
					boolean match = true;
					for(String predicateMapKey : predicateMap.keySet()) {
						//predicateMapKey is of the form c10
						String eventValue = eventMap.get(predicateMapKey);
						if(!predicateMap.get(predicateMapKey).contains(eventValue)) {
							match = false;
							break;
						}
					}
					if(match) { /* remains true */
						matchingSubscriberIds.add(subscriberId);
						break;	/* added, no need to check for further predicateMaps */
					}
				}
			}
		} else {
			throw new Exception("predicate rules not set yet");
		}
		
		return matchingSubscriberIds;
	}
}
