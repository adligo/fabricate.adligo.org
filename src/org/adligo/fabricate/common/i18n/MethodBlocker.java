package org.adligo.fabricate.common.i18n;

import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

/**
 * This class will block method calls based on the stack.
 * However since there is no stack in GWT, it always allows 
 * the method execution to continue on the first time, then 
 * it start blocking using the stack.
 * 
 * To use it instantiate it and put the checkAllowed()
 * method in the method you want to block.
 * 
 * I have not though this through as a security precaution, 
 * it is simply a way to make fairly sure
 * only a few callers can execute specific methods.
 *   ie ASM could potentially change class names, of the caller or something along those lines to circumvent this  
 *    protection, assuming it was loaded into memory.
 *    
 * @author scott
 *
 */
public class MethodBlocker {
	
	//keep ordered for testing
	private Set<String> allowedCallerNames_ = new TreeSet<String>();
	private Class<?> blockingClass_;
	private String blockedMethodName_;
	/**
	 * since this must work in GWT no AtomicBoolean
	 */
	private volatile boolean called = false;
	/**
	 * note these are passed and created as strings to
	 * keep Circular dependencies from getting created
	 * @param allowedCallersClassNames
	 */
	public MethodBlocker(Class<?> blockingClass, String methodName, Collection<String> allowedCallersClassNames) {
		if (blockingClass == null) {
			throw new IllegalArgumentException();
		}
		if (methodName == null || methodName.trim().length() == 0 ) {
			throw new IllegalArgumentException();
		}
		if (allowedCallersClassNames == null) {
			throw new IllegalArgumentException();
		}
		if (allowedCallersClassNames.isEmpty()) {
			throw new IllegalArgumentException();
		}
		
		
		allowedCallersClassNames.remove(null);
		allowedCallerNames_.addAll(allowedCallersClassNames);
		blockingClass_ = blockingClass;
		blockedMethodName_ = methodName;
		
	}
	
	
	public synchronized void checkAllowed() {
		if (called == false) {
			//allow anyone on the first time
			called = true;
			return;
		}
		//on subsequent invocations, require class to be in the approved call list.
		final Exception x = new Exception();
		x.fillInStackTrace();
		StackTraceElement [] trace = x.getStackTrace();
		
		StackTraceElement callingClassE = trace[2];
		String callingClassName = callingClassE.getClassName();
		if (!allowedCallerNames_.contains(callingClassName)) {
			throw new IllegalStateException(
					blockingClass_ + "." + blockedMethodName_ + 
					System.lineSeparator() +
					allowedCallerNames_);
		}
	}
}
