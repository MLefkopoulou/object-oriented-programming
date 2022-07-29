/*
> NAME: Lefkopoulou Eleni Maria
> AEM : 2557
> HW  : 1
> DATE: 24/02/2020
 */
package ce326.hw1;

/**
 *
 * @author linux
 */
public class TrieNode {
    private static final int ALPHABET_SIZE = 26;
    TrieNode[] children ;
    boolean isTerminal;
    public TrieNode() {
        this.children = new TrieNode[ALPHABET_SIZE];
        isTerminal = false;
    }


}
