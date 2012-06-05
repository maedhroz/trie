package maedhroz.trie;

import java.util.Collections;
import java.util.Map.Entry;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.atomic.AtomicBoolean;

import com.google.common.collect.MapMaker;
import com.google.common.collect.Sets;

public class Trie implements Dictionary {

	private final AtomicBoolean terminus = new AtomicBoolean(false);
	
	private final ConcurrentMap<Character, Trie> nextLevel = 
		new MapMaker().concurrencyLevel(Runtime.getRuntime().availableProcessors()).makeMap();
	
	public boolean insert(String phrase) {
		return insert(phrase, 0);
	}
	
	private boolean insert(String phrase, int position) {
		if (position < phrase.length()) {
			Character firstCharacter = Character.valueOf(phrase.charAt(position));
			
			if (!this.nextLevel.containsKey(firstCharacter)) {
				this.nextLevel.putIfAbsent(firstCharacter, new Trie());
			}
			
			Trie nextCharacter = this.nextLevel.get(firstCharacter);
			
			int next = position + 1;
			
			if (phrase.length() == next && !nextCharacter.isTerminus()) {
				nextCharacter.markTerminus();
				return true;
			} else {
				return nextCharacter.insert(phrase, next);
			}
		}
		
		return false;
	}
	
	public boolean contains(String phrase) {
		return contains(phrase, 0);
	}
	
	public boolean contains(String phrase, int position) {
		if (position < phrase.length()) {
			Character firstCharacter = Character.valueOf(phrase.charAt(position));
			
			if (this.nextLevel.containsKey(firstCharacter)) {
				Trie nextCharacter = this.nextLevel.get(firstCharacter);
				
				int next = position + 1;
				
				if (phrase.length() == next && nextCharacter.isTerminus()) {
					return true;
				}
				
				return nextCharacter.contains(phrase, next);
			}
		}
		
		return false;
	}
	
	public Set<String> findCompletions(String phrase) {
		if (phrase.isEmpty()) { return Collections.emptySet(); }
		
		Set<String> completions = Sets.newHashSetWithExpectedSize(4);		
		Trie lastNode = findLastNodeFor(phrase);		
		
		if (lastNode == null) { return Collections.emptySet(); }
		
		StringBuilder buffer = new StringBuilder(phrase);		
		lastNode.findCompletions(completions, buffer);
		return completions;
	}
	
	private void findCompletions(Set<String> completions, StringBuilder buffer) {
		if (isTerminus()) { completions.add(buffer.toString()); }
		
		for (Entry<Character, Trie> node : this.nextLevel.entrySet()) {
			buffer.append(node.getKey());
			node.getValue().findCompletions(completions, buffer);
			buffer.deleteCharAt(buffer.length() - 1);
		}
	}
	
	private Trie findLastNodeFor(String phrase) {
		if (!phrase.isEmpty()) {
			Character firstCharacter = Character.valueOf(phrase.charAt(0));
			
			if (this.nextLevel.containsKey(firstCharacter)) {
				Trie nextCharacter = this.nextLevel.get(firstCharacter);
				
				if (phrase.length() == 1) {
					return nextCharacter;
				}
				
				return nextCharacter.findLastNodeFor(phrase.substring(1));
			}
		}
		
		return null;
	}
	
	private void markTerminus() {
		this.terminus.set(true);
	}
	
	private boolean isTerminus() {
		return this.terminus.get();
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("Trie [terminus=").append(isTerminus()).append(", ");
		
		if (this.nextLevel != null) {
			builder.append("nextLevel=").append(this.nextLevel.keySet());
		}
		
		builder.append("]");
		return builder.toString();
	}

	public void close() {}
}
