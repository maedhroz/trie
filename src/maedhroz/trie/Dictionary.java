package maedhroz.trie;

import java.util.Set;

public interface Dictionary {

	boolean insert(String phrase);

	boolean contains(String phrase);

	Set<String> findCompletions(String phrase);
	
	void close();
}
