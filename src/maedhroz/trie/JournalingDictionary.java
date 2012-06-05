package maedhroz.trie;

import java.io.File;
import java.io.FileWriter;
import java.util.Formatter;
import java.util.Scanner;
import java.util.Set;

public class JournalingDictionary implements Dictionary {
	
	private final Dictionary dictionary;
	private final Formatter journalWriter;

	public JournalingDictionary(Dictionary dictionary, String path) throws Exception {
		this.dictionary = dictionary;
		
		File journalFile = new File(path);
		
		if (!journalFile.createNewFile()) {
			replayJournal(path);
		}
		
		this.journalWriter = new Formatter(new FileWriter(journalFile, true));
	}
	
	private void replayJournal(String path) throws Exception {
		Scanner journalReader = new Scanner(new File(path));
		
		try {
			while (journalReader.hasNextLine()) {
				String phrase = journalReader.nextLine().trim();
				this.dictionary.insert(phrase);
			} 
		} finally {		
			journalReader.close();
		}
	}

	public boolean insert(String phrase) {
		boolean isNewPhrase = this.dictionary.insert(phrase);
		
		if (isNewPhrase) {
			this.journalWriter.format("%s\n", phrase);
		}
		
		return isNewPhrase;
	}

	public boolean contains(String phrase) {
		return this.dictionary.contains(phrase);
	}

	public Set<String> findCompletions(String phrase) {
		return this.dictionary.findCompletions(phrase);
	}
	
	public void close() {
		this.journalWriter.close();
	}

	@Override
	public String toString() {
		return this.dictionary.toString();
	}
}
