package org.trans;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.Map.Entry;
import java.util.Set;



public class WordTranslate {
	
	static LinkedHashSet<String> words = new LinkedHashSet<>();
	static LinkedHashMap<String,String> french = new LinkedHashMap<>();
	static LinkedHashMap<String,Integer> translation = new LinkedHashMap<>();
	
	public static void getEnglishWords() throws IOException {
		FileReader fin = new FileReader("C:\\Users\\DELL\\Downloads\\TranslateWords Challenge\\find_words.txt");
		BufferedReader bin = new BufferedReader(fin);
		String line = null;
		while((line = bin.readLine()) != null) {
			words.add(line);
		}
		bin.close();
		fin.close();
	}
	
	public static void getFrenchWords() throws IOException {
		FileReader f = new FileReader("C:\\Users\\DELL\\Downloads\\TranslateWords Challenge\\french_dictionary.csv");
		BufferedReader bin = new BufferedReader(f);
		String line = "";
		while((line = bin.readLine()) != null) {
			String[] value = line.split(",");
			if(value.length > 2) {
				french.put(value[0],value[1] + "," + value[2]);
			}
			else {
				french.put(value[0],value[1]);
			}
		}
		
		bin.close();
	}
	
	public static void translate() throws IOException {
		FileReader f = new FileReader("C:\\Users\\DELL\\Downloads\\TranslateWords Challenge\\t8.shakespeare.txt");
		BufferedReader bin = new BufferedReader(f);
		File file = new File("C:\\Users\\DELL\\Desktop\\t8.shakespeare.translated.txt");
		file.createNewFile();
		FileWriter fout = new FileWriter(file);
		BufferedWriter bout = new BufferedWriter(fout);
		String line = null;
		String temp = "";
		String regex = "[^A-Za-z0-9_']";
		while((line = bin.readLine()) != null) {
			temp = line;
			String[] values = line.split(regex);
			for(String word : values) {
				String tempWord = word;
				word = word.toLowerCase();
				if(french.containsKey(word)) {
					if(! translation.containsKey(french.get(word))) {
						temp = temp.replace(tempWord,french.get(word));
						int i = 1;
						translation.put(french.get(word),i);
					}
					else {
						temp = temp.replace(tempWord,french.get(word));
						int i = translation.get(french.get(word)) + 1;
						translation.replace(french.get(word),i);
					}
				}
			}
			bout.write(temp + "\n");
		}
		bout.close();
		bin.close();
	}
	
	public static void writeFrequency() throws IOException {
		File f = new File("C:\\Users\\DELL\\Desktop\\frequency.csv");
		f.createNewFile();
		Set<String> keySet = translation.keySet();
		FileWriter fout1 = new FileWriter(f);
		BufferedWriter bout1 = new BufferedWriter(fout1);
		String header = "English Word" + "," + "French Word" + "," + "Frequency";
		bout1.write(header + "\n");
		Set<Entry<String, String>> entrySet = french.entrySet();
		for(Entry<String,String> entry : entrySet) {
			if(keySet.contains(entry.getValue())) {
				String line = entry.getKey() + "," + entry.getValue() + "," + translation.get(entry.getValue());
				bout1.write(line + "\n");
			}
		}
		bout1.close();
		fout1.close();
	}

	public static void main(String[] args) throws IOException {
		long startTime = System.currentTimeMillis();
		WordTranslate.getEnglishWords();
		WordTranslate.getFrenchWords();
		WordTranslate.translate();
		WordTranslate.writeFrequency();
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		long min = (totalTime/1000) / 60;
		long sec = (totalTime/1000) % 60;
		long aftMem = Runtime.getRuntime().totalMemory() - Runtime.getRuntime().freeMemory();
		long mem = (aftMem) / (1024 * 1024);
		File timeMem = new File("C:\\Users\\DELL\\Desktop\\performance.txt");
		timeMem.createNewFile();
		String line = "Time to process: " + min + " minutes " + sec + " seconds"
		               + "\n" + "Memory used: " + mem + " MB";
		FileWriter fw = new FileWriter(timeMem);
		BufferedWriter bw = new BufferedWriter(fw);
		bw.write(line);
		bw.close();
		fw.close();
	}

}
