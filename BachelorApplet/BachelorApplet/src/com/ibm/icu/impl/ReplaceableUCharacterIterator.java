/*
 *******************************************************************************
 * Copyright (C) 1996-2000, International Business Machines Corporation and    *
 * others. All Rights Reserved.                                                *
 *******************************************************************************
 *
 * $Source: /cvsroot/doubletype/doubletype/src/com/ibm/icu/impl/ReplaceableUCharacterIterator.java,v $ 
 * $Date: 2004/01/17 21:28:03 $ 
 * $Revision: 1.1 $
 *
 *******************************************************************************
 */
package com.ibm.icu.impl;

import com.ibm.icu.text.*;
import com.ibm.icu.text.Replaceable;
import com.ibm.icu.text.ReplaceableString;
import com.ibm.icu.text.UTF16;    

/**
 * DLF docs must define behavior when Replaceable is mutated underneath
 * the iterator.
 *
 * This and ICUCharacterIterator share some code, maybe they should share
 * an implementation, or the common state and implementation should be
 * moved up into UCharacterIterator.
 *
 * What are first, last, and getBeginIndex doing here?!?!?!
 */
public class ReplaceableUCharacterIterator extends UCharacterIterator {

    // public constructor ------------------------------------------------------
    
    /**
     * Public constructor
     * @param replacable text which the iterator will be based on
     */
    public ReplaceableUCharacterIterator(Replaceable replaceable){
        if(replaceable==null){
            throw new IllegalArgumentException();
        }
        this.replaceable  = replaceable;
        this.currentIndex = 0;
    }
    
    /**
     * Public constructor
     * @param str text which the iterator will be based on
     */
    public ReplaceableUCharacterIterator(String str){
        if(str==null){
            throw new IllegalArgumentException();
        }
        this.replaceable  = new ReplaceableString(str);
        this.currentIndex = 0;
    }
    
    /**
     * Public constructor
     * @param buf buffer of text on which the iterator will be based
     */
    public ReplaceableUCharacterIterator(StringBuffer buf){
        if(buf==null){
            throw new IllegalArgumentException();
        }
        this.replaceable  = new ReplaceableString(buf);
        this.currentIndex = 0;
    }
    
    // public methods ----------------------------------------------------------
    
    /**
     * Creates a copy of this iterator, does not clone the underlying 
     * <code>Replaceable</code>object
     * @return copy of this iterator
     */
    public Object clone(){
		try {
		  return super.clone();
		} catch (CloneNotSupportedException e) {
		    return null; // never invoked
		}
    }
    
    /**
     * Returns the current UTF16 character.
     * @return current UTF16 character
     */
    public int current(){
        if (currentIndex < replaceable.length()) {
            return replaceable.charAt(currentIndex);
        }
        return DONE;
    }
    
    /**
     * Returns the current codepoint
     * @return current codepoint
     */
    public int currentCodePoint(){
        // cannot use charAt due to it different 
        // behaviour when index is pointing at a
        // trail surrogate, check for surrogates
         
        int ch = current();
        if(UTF16.isLeadSurrogate((char)ch)){
            // advance the index to get the next code point
            next();
            // due to post increment semantics current() after next() 
            // actually returns the next char which is what we want
            int ch2 = current();
            // current should never change the current index so back off
            previous();
            
            if(UTF16.isTrailSurrogate((char)ch2)){
                // we found a surrogate pair
                return UCharacterProperty.getRawSupplementary(
                                                         (char)ch,(char)ch2
                                                             );
            }
        }
        return ch;
    }
    
    /**
     * Returns the length of the text
     * @return length of the text
     */
    public int getLength(){
        return replaceable.length();
    }
    
    /**
     * Gets the current currentIndex in text.
     * @return current currentIndex in text.
     */
    public int getIndex(){
        return currentIndex;
    }
        
    /**
     * Returns next UTF16 character and increments the iterator's currentIndex by 1. 
     * If the resulting currentIndex is greater or equal to the text length, the 
     * currentIndex is reset to the text length and a value of DONECODEPOINT is 
     * returned. 
     * @return next UTF16 character in text or DONE if the new currentIndex is off the 
     *         end of the text range.
     */
    public int next(){
        if (currentIndex < replaceable.length()) {
            return replaceable.charAt(currentIndex++);
        }
        return DONE;
    }
    
                
    /**
     * Returns previous UTF16 character and decrements the iterator's currentIndex by 
     * 1. 
     * If the resulting currentIndex is less than 0, the currentIndex is reset to 0 and a 
     * value of DONECODEPOINT is returned. 
     * @return next UTF16 character in text or DONE if the new currentIndex is off the 
     *         start of the text range.
     */
    public int previous(){
        if (currentIndex > 0) {
            return replaceable.charAt(--currentIndex);
        }
        return DONE;
    }

    /**
     * <p>Sets the currentIndex to the specified currentIndex in the text and returns that 
     * single UTF16 character at currentIndex. 
     * This assumes the text is stored as 16-bit code units.</p>
     * @param currentIndex the currentIndex within the text. 
     * @exception IllegalArgumentException is thrown if an invalid currentIndex is 
     *            supplied. i.e. currentIndex is out of bounds.
     * @return the character at the specified currentIndex or DONE if the specified 
     *         currentIndex is equal to the end of the text.
     */
    public void setIndex(int currentIndex) throws IndexOutOfBoundsException{
        if (currentIndex < 0 || currentIndex > replaceable.length()) {
            throw new IndexOutOfBoundsException();
        }
        this.currentIndex = currentIndex;
    }
    
    public int getText(char[] fillIn, int offset){
    	int length = replaceable.length();
        if(offset < 0 || offset + length > fillIn.length){
            throw new IndexOutOfBoundsException(Integer.toString(length));
        }
        replaceable.getChars(0,length,fillIn,offset);
        return length;
    }       
        
    // private data members ----------------------------------------------------
    
    /**
     * Replacable object
     */
    private Replaceable replaceable;
    /**
     * Current currentIndex
     */
    private int currentIndex;

}
