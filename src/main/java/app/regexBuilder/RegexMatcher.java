package app.regexBuilder;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class RegexMatcher {
	
	@Getter
	final RegexBuilder regexBuilder;
	final String content;
	final Pattern pattern;
	final Matcher matcher;
	
	private Boolean currentFind;
	
	public RegexMatcher(RegexBuilder regexBuilder, String content, int flags) {
		this.regexBuilder = regexBuilder;
		this.content = content;
		this.pattern = Pattern.compile(regexBuilder.toString(), flags);
		this.matcher = pattern.matcher(content);	
	}
	
	public RegexMatcher(RegexBuilder regexBuilder, String content) {
		this(regexBuilder, content, 0);
	}
	
	public boolean find() {
		currentFind = matcher.find();
		return currentFind;
	}
	
	public String group() {
		if(!currentFind) {
			throw new RuntimeException("Cannot get groups when matcher didn't find anything yet or anymore, check the find() method first !\n    content = "+content+"\n    pattern = "+pattern);
		}
		return matcher.group();
	}
	
	public String group(String groupName) {
		
		if(!currentFind) {
			throw new RuntimeException("Cannot get groups when matcher didn't find anything yet or anymore, check the find() method first !\n    content = "+content+"\n    pattern = "+pattern);
		}
		try {
			return matcher.group(regexBuilder.findGroupPosition(groupName));
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Float groupAsFloat(String groupName) {
		String content = group(groupName);
		if(content == null) {
			return null;
		}
		return Float.parseFloat(content);
	}
	
	public Integer groupAsInteger(String groupName) {
		String content = group(groupName);
		if(content == null) {
			return null;
		}
		return Integer.parseInt(content);
	}
	
	public RegexMatcher debug() {
		
		for(int i = 0; i < regexBuilder.nodes.size(); i++) {
			RegexBuilder regexClone = regexBuilder.clone();
			for(int j = 0; j < i; j++) {
				regexClone.nodes.remove(regexClone.nodes.size() - 1);
			}
			RegexMatcher cloneMatcher = new RegexMatcher(regexClone, content);
			
			if(cloneMatcher.find()) {
				log.debug("Matching regex test : "+regexClone.compile());
				return cloneMatcher;
			}
		}
		
		return null;

	}
	
	public int start() {
		return matcher.start();
	}
	public int end() {
		return matcher.end();
	}
	
	public Integer start(String groupName) {
		try {
			return matcher.start(regexBuilder.findGroupPosition(groupName));
		}
		catch(Exception e) {
			return null;
		}
	}
	
	public Integer end(String groupName) {
		try {
			return matcher.end(regexBuilder.findGroupPosition(groupName));
		}
		catch(Exception e) {
			return null;
		}
	}
	

	public String replace(String groupName, String replacementString) {
		Integer groupPosition = regexBuilder.findGroupPosition(groupName);
		return content.substring(0, matcher.start(groupPosition))+replacementString+content.substring(matcher.end(groupPosition));
	}

	public Map<String, String> groupsAsMap() {
		Map<String, String> groupsAsMap = new LinkedHashMap<>();
		for(Entry<Integer, String> entry : regexBuilder.getGroupPositions().entrySet()) {
			if(matcher.group(entry.getKey()) != null) {
				groupsAsMap.put(entry.getValue(), matcher.group(entry.getKey()));
			}
		}
		
		return groupsAsMap;
	}
}
