import java.util.*;

public class GrammarSolver {
   private Map<String, String[]> map;
   private Random rand;
   
   /**
   This is the constructor for the map for grammar. If the list of strings rules is empty, throw
   IllegalArgument. We initialize a new tree map and create a for loop repeating for the size of the list,
   taking in each line of the file and splitting it along the "::=". If there is more than one part, that means
   it wasn't split between non terminals and termins so illegal argument is thrown. We create a string for nonterminals
   and set it to first part and terminals to second part. If there is already a nonTerminal, illegal argument is thrown.
   Otherwise we assign the nonTerminals to the key and the terminals to the values.
   @param is the list of Strings named rules
   */
   public GrammarSolver (List<String> rules) {
      if(rules == null || rules.isEmpty())
        throw new IllegalArgumentException("List of rules is empty");
      
      map = new TreeMap<>();//initialize map
      rand = new Random();
      for(int i = 0; i < rules.size(); i++) {
         String line = rules.get(i); //take in line
         String[] parts = line.split("::=");//split the line into two parts at the ::=
         if(parts.length != 2){
            throw new IllegalArgumentException("Didn't split over ::=");
         }
         String nonTerminal = parts[0].trim(); //nonTerminal is first part of the line and trim it
         if(map.containsKey(nonTerminal)){
            throw new IllegalArgumentException("Duplicate rules");
         }
         String[] ruleParts = parts[1].split("\\|"); //ruleParts is the terminal side of the line and split all of it using |
         
         map.put(nonTerminal, ruleParts); // non-terminal, rules
      }
   }
   /**
   This method tells us whether or not the map contains the nonTerminal value.
   If it does it returns true and false otherwise, throwing and IllegalArgumentException
   if symbol is empty
   @param the String symbol we are searching if is a nonTerminal
   @return truw or false whether symbol is a nonTerminal.
   */
   public boolean contains(String symbol){
      if(symbol == null || symbol.length()==0) throw new IllegalArgumentException("Symbol is empty");
      return map.containsKey(symbol); //if not there false. if there true.
   }
   /**
   @return alphebetical order of the nonTerminals
   */
   public Set<String> getSymbols() {
      return new TreeSet<String>(map.keySet());
   }
   
   /**
   In ther generate method, we take in a symbol and if its empty an IllegalArgument is thrown.
   Our base case is that the map doesn't contain symbol as a nonTerminal, meaning we have reached
   a terminal that will no longer be repeated using recurssion. Otherwise, using recurssion, we make use of rand
   oject from java.util and take in an array of strings that gives values of the nonTerminal symbol.
   An index is created to randomize the choice of options in nonTerminal and chosenRule picks it.
   Nextly, an array of words is created, splitting them by commas using \\s+.
   Using a for each loop, we take our result and recurse through generate adding to our result until base case is hit
   which we trim and return
   @return the string of which is our result from recurrsion giving a randomized list of words
   @param the nonTerminal symbol we are intaking to generate terminals from
   */
   public String generate(String sym) {
      if(sym == null || sym.length()==0) throw new IllegalArgumentException("Empty String");
      if(!map.containsKey(sym)){ //base case if no key exists (its a terminal)
         return sym;
      }
      else{
         String[] options = map.get(sym);//gets what key points too
         int index = rand.nextInt(options.length);//randomizes which it picks
         String chosenRule = options[index];//our chosen word
         
         String[] words = chosenRule.split("\\s+");
         String result = "";
         for(String word : words){
            result += generate(word) + " ";
         }
         
         return result.trim();
         
      }
   }
}