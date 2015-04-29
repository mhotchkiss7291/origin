package com.studytrails.tutorials.springinjectionmap;

import java.util.Map;

public class TelephoneDirectory {

	private Map<String, Integer> directoryMap;

	public Map<String, Integer> getDirectoryMap() {
		return directoryMap;
	}

	public void setDirectoryMap(Map<String, Integer> directoryMap) {
		this.directoryMap = directoryMap;
	}

}