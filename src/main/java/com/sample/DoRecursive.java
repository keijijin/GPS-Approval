package com.sample;

public class DoRecursive {

	static void doRecursive(int i, char[][] arr, String outputString){
	    if(i == arr.length) {
	        System.out.println(outputString);
	        return;
	    }
	    String delim = (outputString.length()>0) ? "," : "";
	    for(int j = 0; j < arr[i].length; j++){
	        doRecursive(i+1, arr, outputString + delim + arr[i][j]);
	    }
	}
	
	public static void main(String[] args) {
		char[][] arr = new char[][]{
		    {'a','b','c'},
		    {'l','m','n'},
		    {'x','y','z'}
		};
		doRecursive(0, arr, "");
	}
}