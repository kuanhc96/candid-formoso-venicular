// Kuan Chen
// Section AA
// Austin Ha
// Part1: Models a vibrating string of a guitar at a certain frequency.
// The vibration is modeled using the Karplus-Strong algorithm.

import java.util.*;

public class GuitarString {
   
   // The Decay Factor that is implemented in the Karplus-Strong Algorithm
   public static final double DECAY_FACTOR = 0.996;
   // The sampling rate used to process the frequency of vibrations
   public static final int SAMPLE_RATE = StdAudio.SAMPLE_RATE;
   
   // Keeps track of the ring buffer of the string, updates as time passes
   private Queue<Double> ringBuffer;
   
   // pre : frequency must be so that SAMPLE_RATE / frequency >= 2;
   //       frequency > 0. If not, throws IllegalArgumentException
   // post: "constructs" the guitar string
   // Parameters needed:
   // double frequency: the fundamental frequency of a given note.
   //                   The string will vibrate at this frequency.  
   public GuitarString(double frequency) {
      int ringBufferSize = (int) Math.round(SAMPLE_RATE / frequency);
      if(ringBufferSize < 2 || frequency <= 0) {
         throw new IllegalArgumentException("Length of Ring Buffer: " + ringBufferSize + " " +
                                             "Fundamental Frequency: " + frequency);
      }
            
      ringBuffer = new LinkedList<Double>();
      for(int i = 0; i < ringBufferSize; i++) {
         ringBuffer.add(0.0);
      }      
   }
   
   // pre : The length of init must be >= 2.
   //       If not, throws IllegalArgumentException.
   // post: Constructs a string with ring buffer values specified by init.
   // Parameters needed:
   // double[] init: The values that are to be inside of the ring buffer.
   public GuitarString(double[] init) {
      if(init.length < 2) {
         throw new IllegalArgumentException("Length of Ring Buffer: " + init.length);
      }
      
      ringBuffer = new LinkedList<Double>();
      for(int i = 0; i < init.length; i++) {
         ringBuffer.add(init[i]);
      }
   }
   
   // post: models the "plucking" of the string. 
   //       Replaces values in the ring buffer with random decimals. 
   public void pluck() {
      Random r = new Random();
      for(int i = 0; i < ringBuffer.size(); i++) {
         ringBuffer.remove();
         ringBuffer.add(r.nextDouble() - 0.5);
      }
   }
   
   // post: modeles the propogation of the string through time.
   //       applied the Karplus-Strong algorithm once, which
   //       takes the average of the first two values in the
   //       ring buffer and adds it to the end of it.
   public void tic() {
      double first = ringBuffer.remove();
      double second = ringBuffer.peek();
      ringBuffer.add(DECAY_FACTOR * 0.5 * (first + second));
   }
   
   // post: returns the first value in the ring buffer.
   public double sample() {
      return ringBuffer.peek();
   }
}