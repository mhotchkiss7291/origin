// Not much here yet for specific Safeword handling, 
// but may be expanded in the future

import java.util.*;
import java.io.*;
import java.awt.*;

public class RCASafewordException extends Exception{
   String problem = "Unknown";

   RCASafewordException(String s) {
   	problem = s;
   }

   public String toString() {
     return "RCASafewordException -->" + problem;
   }
}
