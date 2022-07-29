package ce326.hw1;

/*
> NAME: Lefkopoulou Eleni Maria
> AEM : 2557
> HW  : 1
> DATE: 24/02/2020
 */




/**
 *
 * @author linux
 */
public class Trie {
    TrieNode root;//the root of trie
    public static final String alphabet = "abcdefghijklmnopqrstuvwxyz";
    int pos =0;
    int pre = 0;
    //method 1.Trie
    public Trie(String []words) {
        boolean check;
        root = new TrieNode();
        for (String word : words) {
            if (word != null) {
                check = add(word);
            }
            else{
                break;
            }
        }
    }
    //method 2.add
    public boolean add(String word){
        boolean isAtTree ;   
        isAtTree = contains(word);
        if(isAtTree == false){
            //add the word at tree
            TrieNode p = root;
            for(int i=0; i<word.length(); i++){
                char letter = word.charAt(i);     
                int index = letter-'a';
                if(p.children[index] == null){
                    TrieNode temp = new TrieNode();
                    p.children[index] = temp;
                    p = temp;
                }else{
                    p=p.children[index];
                }
            }
            p.isTerminal=true;
            return true;
        }
        return false;
    }
    public TrieNode searching(String word){
        TrieNode p = root;
        for(int i=0; i<word.length(); i++){
            char letter= word.charAt(i);
            int index = letter-'a';
            if(p.children[index] != null){
                p = p.children[index];
            }else{
                return null;
            }
        }
        if(p==root){
            return null;
        }
        return p;
    }
    //method 3.contains
    public boolean contains(String word){
       //search if the word is at tree
        boolean isAtTree = false; 
        TrieNode p = searching(word);
        if(p==null){
            return false;
        }else{
            if(p.isTerminal)
              return true;
        }
        return false;
    }
    //method 4.size
    public int size(){
       int sizeof = 0;
       sizeof = count(root,sizeof);
       return  sizeof;
    }
    public int count(TrieNode root,int sizeof){
        TrieNode counting = root;
        for(int i =0; i < counting.children.length; i++){
            if(counting.children[i] != null){
                if(counting.children[i].isTerminal == true){
                    sizeof ++;
                }
                sizeof = count(counting.children[i],sizeof);   
            }
        }
        return sizeof;
    }
    //method 5.differByOne
    public String[] differByOne(String word){
        int number = size();
        String[] wordsByOne ;
        wordsByOne = new String[number];
        String wordnew = "";
        wordsDifferByOne(root,wordnew,wordsByOne,word);
        if(pos == 0){
            wordsByOne = null;
        }     
        return wordsByOne;  
    }
       
    public void wordsDifferByOne(TrieNode root,String newWord,String[]array,String word){
        String wordNew = newWord;
        int differ = 0;
        for(int i =0; i < root.children.length; i++){
            if(root.children[i] != null){
                char c = alphabet.charAt(i);
                wordNew = wordNew + c;
                if(root.children[i].isTerminal == true){
                    if(wordNew.length() == word.length()){
                        for(int j =0; j<word.length(); j++){
                        if(wordNew.charAt(j) != word.charAt(j)){
                            differ++;
                        }
                        }
                        if(differ == 1){
                            array[pos] = wordNew;
                            pos = pos+1;
                        }
                        differ = 0;
                    }
                }
                wordsDifferByOne(root.children[i],wordNew,array,word);  
            }
            wordNew =newWord;
        }
        return ;
    
    }     
    //method 6.differBy
    public String[] differBy(String word, int max){
        int number = size();
        String[] wordsBy ;
        wordsBy = new String[number];
        String wordnew = "";
        wordsDifferBy(root,wordnew,wordsBy,word,max);
        if(pos == 0){
            wordsBy = null;
        }
        return wordsBy;    
    }
    public void wordsDifferBy(TrieNode root,String newWord,String[]array,String word,int how){
        String wordNew = newWord;
        int differ = 0;
        for(int i =0; i < root.children.length; i++){
            if(root.children[i] != null){
                char c = alphabet.charAt(i);
                wordNew = wordNew + c;
                if(root.children[i].isTerminal == true){
                        if(wordNew.length() == word.length()){
                            for(int j =0; j<word.length(); j++){
                                if(wordNew.charAt(j) != word.charAt(j)){
                                differ++;
                            }
                        }
                        if((differ <= how)&&(differ >0)){
                            array[pos] = wordNew;
                            pos = pos+1;
                        }
                        differ = 0;
                                }
                }
                wordsDifferBy(root.children[i],wordNew,array,word,how); 
            }
            wordNew =newWord;
        }
    }
    //method 7. toString 
    @Override
    public String toString() {
       String preorderS= " ";
       int sizeof =size();
       preorderS= preorder(root,preorderS,sizeof);
       return preorderS;
    }
    public String  preorder(TrieNode root,String preorderS,int sizeof){
        TrieNode preNode = root;
        for(int i =0; i < preNode.children.length; i++){
            if(preNode.children[i] != null){
                char c = alphabet.charAt(i);
                preorderS =preorderS + c;
                if(preNode.children[i].isTerminal == true){
                    preorderS=preorderS+"!";
                    pre++;
                }
                if(pre != sizeof){
                    preorderS=preorderS+" ";
                }
                preorderS = preorder(preNode.children[i],preorderS,sizeof);   
            }
        }
        return preorderS ;
    }
    //method 8. toDotString
    public String toDotString(){
       String dotString= "graph Trie { ";
       dotString = dotString + root.hashCode();
       char oups = '"';
       dotString = dotString + " [label=" + oups + "ROOT" + oups +  " ,shape=circle, color=black] ";
       dotString= dotTrie(root,dotString);
       dotString = dotString + "}";
       return dotString; 
    }
    public String dotTrie(TrieNode root,String dotString){
        TrieNode dotNode = root;
        for(int i =0; i < dotNode.children.length; i++){
            if(dotNode.children[i] != null){
                dotString = dotString + root.hashCode() + " -- " + dotNode.children[i].hashCode()+" ";
                char letter = alphabet.charAt(i);
                dotString = dotString + dotNode.children[i].hashCode();
                char oups = '"';
                dotString = dotString + " [label=" + oups + letter + oups +  " ,shape=circle, ";
                if(dotNode.children[i].isTerminal == true){
                    dotString = dotString+"color=red]  ";
                }
                else{
                   dotString = dotString+"color=black]  ";
                }
                dotString = dotTrie(dotNode.children[i],dotString);   
            }
        }
        return dotString ;
    }
    //method 9. wordsOfPrefix
    public String[] wordsOfPrefix(String prefix){
        int number = size();
        String[] wordsPrefix ;
        wordsPrefix = new String[number];
        String word = "";
        wordsWithPrefix(root,word,wordsPrefix,prefix);
        if(pos == 0){
            wordsPrefix = null;
        }
        return wordsPrefix;
    }
   
    public void wordsWithPrefix(TrieNode root,String word,String[]array,String prefix){
        String newWord = word;
        boolean start;
        for(int i =0; i < root.children.length; i++){
            if(root.children[i] != null){
                char c = alphabet.charAt(i);
                newWord = newWord + c;
                if(root.children[i].isTerminal == true){
                   start = newWord.startsWith(prefix);
                   if(start == true){
                     array[pos] = newWord;
                     pos = pos+1;
                   }  
                }
                wordsWithPrefix(root.children[i],newWord,array,prefix); 
            }
            newWord = word;
        }
    }     
    
}
