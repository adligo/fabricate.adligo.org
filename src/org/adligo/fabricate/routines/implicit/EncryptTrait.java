package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.util.ByteMutant;
import org.adligo.fabricate.models.common.ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_ResultFactory;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * This is a particularly bad encryption decryption algorithm
 * for encrypting password of keystore keys for ssh.
 * It should really only be used for keystore keys
 * which have read only access to some remote system (like github.com).
 * Although if your fairly crafty you can develop your own 
 * encrypt and decrypt and then only use your decrypt at
 * Fabrication time by using the traits xml nodes in your 
 * fabricate.xml file.
 * 
 * It was added for continuous integration servers so that
 * Fabricate wouldn't hang (on a command line dialog) when being run by a 
 * scheduler like Jenkins.
 *    Ok onto the impelemtation details.
 * Encrypt steps
 *    1) count the byte in the UTF-8 password.
 *    2) write a 4 byte sequence of ASCII (not extended)
 *       using the last 5 bits for data of the positive number of UTF-8 characters
 *    3) Shift the bits of the password to the right by the number of characters in the 
 *       sequence, wrap the bits at the right around to the left.
 *    4) Write out the bits as ASCII (not extended) using the 
 *       last 5 bits of each byte for data. The extra spaces are filled with 0s.
 *       
 *    This ensures that the encrypted password is always ASCII (and UTF-8) encoded.
 *       
 * @author scott
 *
 */
public class EncryptTrait extends AbstractRoutine implements I_FabricationRoutine, I_ResultFactory<String>, 
I_InputAware<String> {
  /**
   * This is the implicit name of this trait.
   */
  public static final String NAME = "encryptTrait";
  public static final Set<I_ExpectedRoutineInterface> IMPLEMENTED_INTERFACES = getInterfaces();
  
  private static Set<I_ExpectedRoutineInterface> getInterfaces() {
    Set<I_ExpectedRoutineInterface> toRet = new HashSet<I_ExpectedRoutineInterface>();
    toRet.add(new ExpectedRoutineInterface(I_InputAware.class, String.class));
    toRet.add(new ExpectedRoutineInterface(I_ResultFactory.class, String.class));
    return Collections.unmodifiableSet(toRet);
  }
  private String utf8Password_;
  private String output_;
  /**
   * zero arg constructor
   */
  public EncryptTrait() {}
  
  @SuppressWarnings("boxing")
  @Override
  public void run() {
    byte [] bytes = null;
    try {
      bytes = utf8Password_.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    int len = bytes.length;
    ByteArrayOutputStream baos = new ByteArrayOutputStream();
    try {
      buildNumber(bytes, len, baos);
      List<Boolean> bitsList = new ArrayList<Boolean>();
      for (int i = 0; i < bytes.length; i++) {
        ByteMutant bm = new ByteMutant(bytes[i]);
        for (int j = 0; j < 8; j++) {
          bitsList.add(bm.getSlot(j));
        }
      }
      List<Boolean> newBits = new ArrayList<Boolean>();
      for (int i = 0; i < len; i++) {
        int last = bitsList.size() - 1;
        Boolean lastBit = bitsList.get(last);
        newBits.add(0, lastBit);
        bitsList.remove(last);
      }
      double bits = len * 8;
      for (Boolean bit: bitsList) {
        newBits.add(bit);
      }
      
      double length = bits/5;
      int lengthInt = new Double(length).intValue();
      if (Math.IEEEremainder(bits,5.0) != 0.0) {
        lengthInt++;
      }
      
      int bitCounter = 0;
      for (int i = 0; i < lengthInt; i++) {
        ByteMutant bm = new ByteMutant();
        bm.setSlotZero(false);
        bm.setSlotOne(true);
        bm.setSlotTwo(false);
        
        for (int j = 0; j < 5; j++) {
          Boolean b = null;
          if (bitCounter < newBits.size()) {
            b = newBits.get(bitCounter++);
          }
          if (b != null) {
            bm.setSlot(j + 3, b);
          }
        }
        try {
          baos.write(new byte[] {bm.toByte()});
        } catch (IOException e) {
          throw new RuntimeException(e);
        }
      }
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
    try {
      output_ = baos.toString("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
  }

  public void buildNumber(byte[] bytes, int len, ByteArrayOutputStream baos) throws IOException {
    String bin = Integer.toBinaryString(len);
    
    if (bin.length() < 20) {
      StringBuilder sb = new StringBuilder();
      int diff = 20 - bin.length();
      for (int i = 0; i < diff; i++) {
        sb.append("0");
      }
      bin = sb.toString() + bin;
    } else if (bin.length() > 20) {
      bin = bin.substring(0, 20);
    }
    char [] binChars = bin.toCharArray();
    
    int counter = 0;
    for (int i = 0; i < 4; i++) {
      ByteMutant bm = new ByteMutant();
      bm.setSlotZero(false);
      bm.setSlotOne(true);
      bm.setSlotTwo(false);
      
      for (int j = 0; j < 5; j++) {
        
        char c = binChars[counter++];
        if (c == '0') {
          bm.setSlot(j + 3, false);
        } else {
          bm.setSlot(j + 3, true);
        }
      }
      baos.write(new byte[] {bm.toByte()});
    }
  }

  @Override
  public List<Class<?>> getClassType(Class<?> interfaceClass) {
    List<Class<?>> cts = new ArrayList<Class<?>>();
    cts.add(String.class);
    return Collections.unmodifiableList(cts);
  }
  
  /**
   * Returns a UTF-16 string which represents 
   * ASCII (non extended) characters.
   */
  @Override
  public String getResult() {
    return output_;
  }

  @Override
  public void setInput(String input) {
    utf8Password_ = input;
  }

}
