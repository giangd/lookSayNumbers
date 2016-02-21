import processing.core.*; 
import processing.data.*; 
import processing.event.*; 
import processing.opengl.*; 

import java.util.HashMap; 
import java.util.ArrayList; 
import java.io.File; 
import java.io.BufferedReader; 
import java.io.PrintWriter; 
import java.io.InputStream; 
import java.io.OutputStream; 
import java.io.IOException; 

public class lookSayNumbers extends PApplet {

//works with ONLY lowercased letters (no spaces or punctuation)

public void setup() {
  //println((int)'a',(int)'b',(int)'z');
  //println((char)97);

  encryptMessage();
  decryptMessage();
  // println(engToCode(1,"Hey there how are you?"));
}

public void encryptMessage() {
  String[] toEncryptList = loadStrings("toEncrypt.txt");
  String toEncrypt = toEncryptList[0];
  String[] encrypted = {engToCode(1,toEncrypt)};
  saveStrings("encrypted.txt", encrypted);
}

public void decryptMessage() {
  String[] toDecryptList = loadStrings("toDecrypt.txt");
  String toDecrypt = toDecryptList[0];
  String[] decrypted = {codeToEng(1,toDecrypt)};
  saveStrings("decrypted.txt", decrypted);
}

public String encrypt(int numRepeat, String tempInput) { //for encrypting multiple times, too hard to make it recursive so i just made this
  String bothput = tempInput;
  for (int i = 0; i < numRepeat; i++) {
    bothput = encryptFunction(bothput);
  }
  return bothput;
}

public String decrypt(int numRepeat, String tempInput) {
  String bothput = tempInput;
  for (int i = 0; i < numRepeat; i++) {
    bothput = decryptFunction(bothput);
  }
  return bothput;
}

public String encryptFunction(String input) { //dont use this one use the other one, which can encrypt multiple times
  Character originalChar = ' ';
  int numsRepeat = 0;
  String output = new String();

  for (int i = 0; i < input.length(); i++) {
    numsRepeat = 0;
    originalChar = input.charAt(i);

    if (i+1 < input.length() && input.charAt(i) == input.charAt(i+1)) {
      while (i+1 < input.length() && input.charAt(i) == input.charAt(i+1)) {
        numsRepeat++;
        i++;
      }
      numsRepeat++;
      output += Integer.toString(numsRepeat) + originalChar;
    } else {
      output += "1" + originalChar;
    }
  }

  return output;
}

public String decryptFunction(String input) {
  Character originalChar = ' ';
  int numsRepeat = 0;
  String output = new String();

  for (int i = 0; i < input.length()-1; i+=2) { //i+=2 is important
    numsRepeat = Character.getNumericValue(input.charAt(i));
    originalChar = input.charAt(i+1);

    for (int j = 0; j < numsRepeat; j++) {
      output += originalChar;
    }
  }

  return output;
}

public String engToCode(int numTimes, String input) {
  String noFormatInput = new String();  //deletes spaces and punctuation, and changes capitals to lowercase
  for (int i = 0; i < input.length(); i++) {
    if (input.charAt(i) != ' ' && Character.isLetter(input.charAt(i))) {
      if (Character.isUpperCase(input.charAt(i))) {
        noFormatInput += Character.toLowerCase(input.charAt(i));
      } else {
        noFormatInput += input.charAt(i);
      }
    }
  }


  String output = new String();
  String unicodeThingy = new String();
  String partialCode = new String();

  for (int i = 0; i < noFormatInput.length(); i++) {
    partialCode = String.valueOf(((int)noFormatInput.charAt(i) - 96));
    if (partialCode.length() == 1) {
      unicodeThingy = "0" + partialCode; //so that every alphabet letter gets 2 digits
    } else {
      unicodeThingy = partialCode;
    }
    output+=unicodeThingy;
  }

  return encrypt(numTimes, output);
}

public String codeToEng(int numTimes, String input) {
  String partialDecrypted = decrypt(numTimes, input); //still not unicode
  int unicodeThingy = 0;
  String output = new String();

  for (int i = 0; i < partialDecrypted.length(); i+=2) {
    unicodeThingy = Integer.parseInt(""+partialDecrypted.charAt(i)+partialDecrypted.charAt(i+1)) + 96; //concatenates two char and makes those into an int and adds 96 to make it unicode (it was 1-26 a-z before)
    output += (char)unicodeThingy;
  }

  return output;
}

  static public void main(String[] passedArgs) {
    String[] appletArgs = new String[] { "lookSayNumbers" };
    if (passedArgs != null) {
      PApplet.main(concat(appletArgs, passedArgs));
    } else {
      PApplet.main(appletArgs);
    }
  }
}
