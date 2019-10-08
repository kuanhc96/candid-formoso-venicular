// Kuan Chen
// Section AA
// Austin Ha
// Part2: Keeps track of 37 vibrating strings, each of which represents a note
// on the keyboard.      

public class Guitar37 implements Guitar {
   
   // defines the keyboard. contains information of which keys can be played.
   public static final String KEYBOARD = "q2we4r5ty7u8i9op-[=zxdcfvgbnjmk,.;/' ";
   
   // pitch and index of the keys on the keyboard are related by pitch + 24 = index
   public static final int PITCH_CONVERSION = 24;
   
   // frequency of concert-A
   public static final int CONCERT_A = 440;

   // keeps track of the 37 keys.
   private GuitarString[] strings;
   // keeps track of the "time," or the number of times a string is plucked.
   private int time;
   
   // post: constructs a guitar with 37 strings
   //       the fundamental frequency of each string of the guitar
   //       is given by the formula 440 * 2 ^ ((i - PITCH_CONVERSION) / 12.0))
   //       where i is the ith key on the keyboard
   public Guitar37() {
      strings = new GuitarString[KEYBOARD.length()];
      for(int i = 0; i < strings.length; i++){
         strings[i] = new GuitarString(CONCERT_A * Math.pow(2,(i - PITCH_CONVERSION) / 12.0));
      }
      
      time = 0; // when the guitar is first constructed, time = 0
   }
   
   // pre : pitch must be an integer
   // post: plays the string corresponding to the note that pitch represents
   // Parameters needed:
   // int pitch: the pitch of a certain note.
   //            pitch of 0 corresponds to concert-A.
   public void playNote(int pitch) {
      pitch += PITCH_CONVERSION;
      if(pitch >= KEYBOARD.indexOf(KEYBOARD.charAt(0)) && 
         pitch <= KEYBOARD.indexOf(KEYBOARD.charAt(KEYBOARD.length() -1))) {
         strings[pitch].pluck();
      }
   }
   
   // pre : key must be a char.
   // post: determines whether the key is contained in the keyboard. 
   //       if not, returns false.
   // Parameters needed:
   // char key: the key of interest 
   public boolean hasString(char key) {
      return KEYBOARD.indexOf(key) != -1;
   }
   
   // pre : key must be a char contained in the keyboard.
   //       if not, throws IllegalArgumentException.
   // post: "plucks" the string corresponding to the given key. 
   public void pluck(char key) {
      if(!hasString(key)) {
         throw new IllegalArgumentException("Key Not Contained In Keyboard");
      }
      
      strings[KEYBOARD.indexOf(key)].pluck();
   }
   
   // post: sums the first value of each string's ring buffer.    
   public double sample() {
      double sum = 0;
      for(int i = 0; i < strings.length; i++) {
         sum += strings[i].sample();
      }
      
      return sum;
   }
   
   // post: increments the time by one second.
   //       apllies the Karplus-Strong algorithm 
   //       to the ring buffers of every string 
   public void tic() {
      time++;
      for(int i = 0; i < strings.length; i++) {
         strings[i].tic();
      }
   }
   
   // post: returns the time that the guitar has progressed to. 
   public int time() {
      return time;
   }
}