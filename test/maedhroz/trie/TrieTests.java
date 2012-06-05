package maedhroz.trie;

import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.google.common.collect.ImmutableSet;

public class TrieTests {

	@Test
	public void shouldReturnTrueOnInsertion() {
		String singleWordPhrase = "hello";
		Dictionary trie = new Trie();
		Assert.assertTrue(trie.insert(singleWordPhrase));
	}
	
	@Test
	public void shouldReturnFalseOnDuplicateInsertion() {
		String singleWordPhrase = "hello";
		Dictionary trie = new Trie();
		trie.insert(singleWordPhrase);
		Assert.assertFalse(trie.insert(singleWordPhrase));
	}
	
	@Test
	public void shouldContainSingleWordPhraseAfterInsertion() {
		String singleWordPhrase = "hello";
		Dictionary trie = new Trie();
		trie.insert(singleWordPhrase);
		Assert.assertTrue(trie.contains(singleWordPhrase));
	}
	
	@Test
	public void shouldNotContainSingleWordPhraseAfterInsertingSuperPhrase() {
		Dictionary trie = new Trie();
		trie.insert("hellos");
		Assert.assertFalse(trie.contains("hello"));
	}
	
	@Test
	public void shouldContainSingleWordPhraseAfterInsertingPhraseAndSuperPhrase() {
		Dictionary trie = new Trie();
		trie.insert("hellos");
		trie.insert("hello");
		Assert.assertTrue(trie.contains("hello"));
	}
	
	@Test
	public void shouldNotContainSingleWordPhraseAfterNoInsertions() {
		String singleWordPhrase = "hello";
		Dictionary trie = new Trie();
		Assert.assertFalse(trie.contains(singleWordPhrase));
	}
	
	@Test
	public void shouldContainMultiWordPhraseAfterInsertion() {
		String multiWordPhrase = "hello world";
		Dictionary trie = new Trie();
		trie.insert(multiWordPhrase);
		Assert.assertTrue(trie.contains(multiWordPhrase));
	}
	
	@Test
	public void shouldIgnoreShortSubPhrase() {
		Dictionary trie = new Trie();
		Set<String> actual = trie.findCompletions("ap");
		Set<String> expected = ImmutableSet.of();
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void shouldCompletePartialSingleWordPhrase() {
		Dictionary trie = new Trie();		
		trie.insert("apple");		
		Set<String> actual = trie.findCompletions("app");
		Set<String> expected = ImmutableSet.of("apple");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void shouldCompletePartialMultiWordPhrase() {
		Dictionary trie = new Trie();
		trie.insert("apple cart");		
		Set<String> actual = trie.findCompletions("app");
		Set<String> expected = ImmutableSet.of("apple cart");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void shouldCompleteFullSingleWordPhrase() {
		Dictionary trie = new Trie();
		trie.insert("apple");		
		Set<String> actual = trie.findCompletions("apple");
		Set<String> expected = ImmutableSet.of("apple");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void shouldReturnNoCompletionsWithNoPartialPhrase() {
		Dictionary trie = new Trie();	
		Set<String> actual = trie.findCompletions("apple");
		Set<String> expected = ImmutableSet.of();
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void shouldCompleteFullMultiWordPhrase() {
		Dictionary trie = new Trie();
		trie.insert("apple cart");
		Set<String> actual = trie.findCompletions("apple cart");
		Set<String> expected = ImmutableSet.of("apple cart");
		Assert.assertEquals(expected, actual);
	}
	
	@Test
	public void shouldFindMultipleCompletionsForPartialSingleWordPhrase() {
		Dictionary trie = new Trie();		
		trie.insert("apple");
		trie.insert("apple cart");
		trie.insert("apples");
		trie.insert("apple cider");
		trie.insert("apple wine");
		Set<String> actual = trie.findCompletions("app");
		Set<String> expected = ImmutableSet.of("apple", "apple cart", "apples", "apple cider", "apple wine");
		Assert.assertEquals(expected, actual);
	}
}
