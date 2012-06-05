package maedhroz.trie;

import java.io.File;
import java.util.Scanner;

public class TrieMemoryTest {

	public static void main(String[] args) throws Exception {
		Trie trie = new Trie();
		
		Scanner wordReader = new Scanner(new File("lowercase_words.txt"));
		
		try {
			while (wordReader.hasNextLine()) {
				String nextWord = wordReader.nextLine();
				trie.insert(nextWord);
			} 
		} finally {		
			wordReader.close();
		}
		
		Runtime.getRuntime().gc();
		Runtime.getRuntime().gc();
		Runtime.getRuntime().gc();
		
		long totalMemoryInMB = Runtime.getRuntime().totalMemory() / (1024L * 1024L);
		System.out.println("After inserting ~58,000 words, the JVM is using " + totalMemoryInMB + " MB");
	}
}
