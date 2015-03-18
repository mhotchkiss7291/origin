// Not much here yet for specific Safeword handling, 
// but may be expanded in the future

import java.util.*;
import java.io.*;
import java.awt.*;

public class RCSafewordException extends Exception{
   String problem = "Unknown";

   RCSafewordException(String s) {
    problem = s;
   }

   public String toString() {
     return "RCSafewordException -->" + problem;
   }
}
