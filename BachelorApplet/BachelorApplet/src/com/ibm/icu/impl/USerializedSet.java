/*
 *******************************************************************************
 *   Copyright (C) 2002-2003, International Business Machines
 *   Corporation and others.  All Rights Reserved.
 *******************************************************************************
 *
 * $Source: /cvsroot/doubletype/doubletype/src/com/ibm/icu/impl/USerializedSet.java,v $ 
 * $Date: 2004/01/17 21:28:03 $ 
 * $Revision: 1.1 $
 *
 *****************************************************************************************
*/

package com.ibm.icu.impl;
/**
 * @version 	1.1
 * @author     Markus W. Scherer
 * Ram: Add documentation, remove unwanted methods, improve coverage.
 */

/**
 * Simple class for handling serialized USet/UnicodeSet structures
 * without object creation. See ICU4C icu/source/common/uset.c.
 *
 * @internal
 */
public final class USerializedSet {
    /**
     * Fill in the given serialized set object.
     * @param src pointer to start of array
     * @param srcLength length of array
     * @return true if the given array is valid, otherwise false
     * @draft ICU 2.4
     */
    public final boolean getSet(char src[], int srcStart) {
        // leave most argument checking up to Java exceptions
        array=null;
        arrayOffset=bmpLength=length=0;

        length=src[srcStart++];
        
        
        if((length&0x8000) >0) {
            /* there are supplementary values */
            length&=0x7fff;
            if(src.length<(srcStart+1+length)) {
                length=0;
               throw new IndexOutOfBoundsException();
            }
            bmpLength=src[srcStart++];
        } else {
            /* only BMP values */
            if(src.length<(srcStart+length)) {
                length=0;
                throw new IndexOutOfBoundsException();
            }
            bmpLength=length;
        }
        array = new char[length];
        System.arraycopy(src,srcStart,array,0,length);
        //arrayOffset=srcStart;
        return true;
    }
    
    /**
     * Set the USerializedSet to contain the given character (and nothing
     * else).
     * @draft ICU 2.4
     */
	public final void setToOne(int c) {
	    if( 0x10ffff<c) {
	        return;
	    }

	    if(c<0xffff) {
	        bmpLength=length=2;
	        array[0]=(char)c;
	        array[1]=(char)(c+1);
	    } else if(c==0xffff) {
	        bmpLength=1;
	        length=3;
	        array[0]=0xffff;
	        array[1]=1;
	        array[2]=0;
	    } else if(c<0x10ffff) {
	        bmpLength=0;
	        length=4;
	        array[0]=(char)(c>>16);
	        array[1]=(char)c;
	        ++c;
	        array[2]=(char)(c>>16);
	        array[3]=(char)c;
	    } else /* c==0x10ffff */ {
	        bmpLength=0;
	        length=2;
	        array[0]=0x10;
	        array[1]=0xffff;
	    }
	}
	
    /**
     * Returns a range of characters contained in the given serialized
     * set.
     * @param set the serialized set
     * @param rangeIndex a non-negative integer in the range 0..
     * uset_getSerializedRangeCount(set)-1
     * @param pStart pointer to variable to receive first character
     * in range, inclusive
     * @param pEnd pointer to variable to receive last character in range,
     * inclusive
     * @return true if rangeIndex is valid, otherwise false
     * @draft ICU 2.4
     */
	public final boolean getRange( int rangeIndex,int[] range) {
	    if( rangeIndex<0) {
	        return false;
	    }
	    if(array==null){
			array = new char[8];
		}
        if(range==null || range.length <2){
            throw new IllegalArgumentException();
        }
        rangeIndex*=2; /* address start/limit pairs */
	    if(rangeIndex<bmpLength) {
	        range[0]=array[rangeIndex++];
	        if(rangeIndex<bmpLength) {
	            range[1]=array[rangeIndex];
	        } else if(rangeIndex<length) {
	            range[1]=(((int)array[rangeIndex])<<16)|array[rangeIndex+1];
	        } else {
	            range[1]=0x110000;
	        }
            range[1]-=1;
	        return true;
	    } else {
	        rangeIndex-=bmpLength;
	        rangeIndex*=2; /* address pairs of pairs of units */
	        length-=bmpLength;
	        if(rangeIndex<length) {
	            int offset=arrayOffset+bmpLength;
	            range[0]=(((int)array[offset+rangeIndex])<<16)|array[offset+rangeIndex+1];
	            rangeIndex+=2;
	            if(rangeIndex<length) {
	                range[1]=(((int)array[offset+rangeIndex])<<16)|array[offset+rangeIndex+1];
	            } else {
	                range[1]=0x110000;
	            }
                range[1]-=1;
	            return true;
	        } else {
	            return false;
	        }
	    }
	}
    
    /**
     * Returns true if the given USerializedSet contains the given
     * character.
     * @param set the serialized set
     * @return true if set contains c
     * @draft ICU 2.4
     */
	public final boolean contains(int c) {
	
	    if(c>0x10ffff) {
	        return false;
	    }
	    
	    if(c<=0xffff) {
	    	int i;
	        /* find c in the BMP part */
	        for(i=0; i<bmpLength && (char)c>=array[i]; ++i) {}
	        return (boolean)((i&1) != 0);
	    } else {
	    	int i;
	        /* find c in the supplementary part */
	        char high=(char)(c>>16), low=(char)c;
	        for(i=bmpLength;
	            i<length && (high>array[i] || (high==array[i] && low>=array[i+1]));
	            i+=2) {}
	
	        /* count pairs of 16-bit units even per BMP and check if the number of pairs is odd */
	        return (boolean)(((i+bmpLength)&2)!=0);
	    }
	}
    /**
     * Returns the number of disjoint ranges of characters contained in
     * the given serialized set.  Ignores any strings contained in the
     * set.
     * @param set the serialized set
     * @return a non-negative integer counting the character ranges
     * contained in set
     * @draft ICU 2.4
     */
	public final int countRanges() {
	    return (bmpLength+(length-bmpLength)/2+1)/2;
	}
    
    private char array[] = new char[8];
    private int arrayOffset, bmpLength, length;
}