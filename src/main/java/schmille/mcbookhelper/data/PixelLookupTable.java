package schmille.mcbookhelper.data;

import java.util.HashMap;
import java.util.Map;

public abstract class PixelLookupTable {
   private static Map<Character, Integer> map;

   static  {
       map = new HashMap<>();
       map.put(' ', 3);
       map.put('!',1);
       map.put('\"',3);
       map.put('\'', 1);
       map.put('(',3);
       map.put(')',	3);
       map.put('*',3);
       map.put(',',	1);
       map.put('.',1);
       map.put(':',1);
       map.put(';', 1);
       map.put('<',4);
       map.put('>',4);
       map.put('@',6);
       map.put('I',3);
       map.put('[', 3);
       map.put(']', 3);
       map.put('`',2);
       map.put('f',4);
       map.put('i', 1);
       map.put('k',4);
       map.put('l', 2);
       map.put('t',3);
       map.put('{',3);
       map.put('|', 1);
       map.put('}',3);
       map.put('~',6);
       map.put('\n', 2);
   }

   public static int lookup(char c) {
       if(map.containsKey(c))
           return map.get(c);

       return 5;
   }

   public static int lookup(String s) {
       int out = 0;
       for(char c : s.toCharArray())
           out += lookup(c);

       return out;
   }
}
