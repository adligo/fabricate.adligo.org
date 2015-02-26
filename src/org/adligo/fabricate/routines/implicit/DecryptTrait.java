package org.adligo.fabricate.routines.implicit;

import org.adligo.fabricate.common.util.ByteMutant;
import org.adligo.fabricate.models.common.ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_ExpectedRoutineInterface;
import org.adligo.fabricate.models.common.I_FabricationRoutine;
import org.adligo.fabricate.routines.AbstractRoutine;
import org.adligo.fabricate.routines.I_GenericTypeAware;
import org.adligo.fabricate.routines.I_InputAware;
import org.adligo.fabricate.routines.I_OutputProducer;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class DecryptTrait extends AbstractRoutine implements I_FabricationRoutine, 
  I_OutputProducer<String>, I_InputAware<String> {
  /**
   * This is the implicit name of this trait.
   */
  public static final String NAME = "decryptTrait";
  public static final Set<I_ExpectedRoutineInterface> IMPLEMENTED_INTERFACES = getInterfaces();
  
  private static Set<I_ExpectedRoutineInterface> getInterfaces() {
    Set<I_ExpectedRoutineInterface> toRet = new HashSet<I_ExpectedRoutineInterface>();
    toRet.add(new ExpectedRoutineInterface(I_InputAware.class, String.class));
    toRet.add(new ExpectedRoutineInterface(I_OutputProducer.class, String.class));
    return Collections.unmodifiableSet(toRet);
  }
  
  private String utf8Password_;
  private String output_;
  /**
   * zero arg constructor
   */
  public DecryptTrait() {}
  
  @SuppressWarnings("boxing")
  @Override
  public void run() {
    byte [] bytes = null;
    if (utf8Password_.length() <= 5) {
      throw new RuntimeException("to short");
    }
    try {
      bytes = utf8Password_.getBytes("UTF-8");
    } catch (UnsupportedEncodingException e) {
      throw new RuntimeException(e);
    }
    try {
      int readLength = readNumber(bytes);
      List<Boolean> bitsList = new ArrayList<Boolean>();
      int total = readLength * 8;
      int lessThanTotalCounter = 0;
      for (int i = 4; i < bytes.length; i++) {
        ByteMutant bm = new ByteMutant(bytes[i]);
        for (int j = 3; j < 8; j++) {
          if (total > lessThanTotalCounter++) {
            bitsList.add(bm.getSlot(j));
          } else {
            break;
          }
        }
      }
      
      List<Boolean> newList = new ArrayList<Boolean>();
      for (int i = 0; i < readLength; i++) {
        boolean first = bitsList.remove(0);
        newList.add(first);
      }
      bitsList.addAll(newList);
      
      ByteArrayOutputStream baos = new ByteArrayOutputStream();
      for (int i = 0; i < bitsList.size();) {
        ByteMutant bm = new ByteMutant();
        for (int j = 0; j < 8; j++) {
          boolean bit = bitsList.get(i++);
          bm.setSlot(j, bit);
        }
        baos.write(new byte[] {bm.toByte()});
      }
      output_ = baos.toString("UTF-8"); 
     
    } catch (IOException x) {
      throw new RuntimeException(x);
    }
  }

  /**
   * Returns a UTF-16 string which represents 
   * ASCII (non extended) characters.
   */
  @Override
  public String getOutput() {
    return output_;
  }

  @Override
  public void setInput(String input) {
    utf8Password_ = input;
  }

  private int readNumber(byte[] bytes)  {
    
    StringBuilder binString = new StringBuilder();
    for (int i = 0; i < 4; i++) {
      ByteMutant bm = new ByteMutant(bytes[i]);
      
      for (int j = 3; j < 8; j++) {
        boolean b = bm.getSlot(j);
        if (b) {
          binString.append("1");
        } else {
          binString.append("0");
        }
      }
    }
    String result = binString.toString();
    return Integer.parseInt(result, 2);
  }

  @Override
  public List<Class<?>> getClassType(Class<?> interfaceClass) {
    List<Class<?>> cts = new ArrayList<Class<?>>();
    cts.add(String.class);
    return Collections.unmodifiableList(cts);
  }
}
