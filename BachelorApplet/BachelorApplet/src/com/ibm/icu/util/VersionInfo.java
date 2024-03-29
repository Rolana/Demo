/*
 *******************************************************************************
 * Copyright (C) 1996-2003, International Business Machines Corporation and    *
 * others. All Rights Reserved.                                                *
 *******************************************************************************
 *
 * $Source: /cvsroot/doubletype/doubletype/src/com/ibm/icu/util/VersionInfo.java,v $ 
 * $Date: 2004/01/17 21:28:03 $ 
 * $Revision: 1.1 $
 *
 * jitterbug 1741
 *****************************************************************************************
 */

package com.ibm.icu.util;

import java.util.HashMap;

/**
 * Class to store version numbers of the form major.minor.milli.micro.
 * @author synwee
 * @stable ICU 2.6
 */
@SuppressWarnings("all")
public final class VersionInfo
{
    // public data members -------------------------------------------------
        
    /**
     * Unicode 1.0 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_1_0;
    /**
     * Unicode 1.0.1 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_1_0_1;
    /**
     * Unicode 1.1.0 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_1_1_0;
    /**
     * Unicode 1.1.5 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_1_1_5;
    /**
     * Unicode 2.0 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_2_0;        
    /**
     * Unicode 2.1.2 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_2_1_2;
    /**
     * Unicode 2.1.5 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_2_1_5;
    /**
     * Unicode 2.1.8 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_2_1_8;
    /**
     * Unicode 2.1.9 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_2_1_9;
    /**
     * Unicode 3.0 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_3_0;
    /**
     * Unicode 3.0.1 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_3_0_1;
    /**
     * Unicode 3.1.0 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_3_1_0;
    /**
     * Unicode 3.1.1 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_3_1_1;
    /**
     * Unicode 3.2 version
     * @stable ICU 2.6
     */
    public static final VersionInfo UNICODE_3_2;
    
    /**
     * Unicode 4.0 version
     * @draft ICU 2.6
     */
    public static final VersionInfo UNICODE_4_0;
    
    /**
     * ICU4J current release version
     * @draft ICU 2.2
     */
    public static final VersionInfo ICU_VERSION;
    
    // public methods ------------------------------------------------------
        
    /**
     * Returns an instance of VersionInfo with the argument version.
     * @param version version String in the format of "major.minor.milli.micro"
     *                or "major.minor.milli" or "major.minor" or "major",
     *                where major, minor, milli, micro are non-negative numbers
     *                <= 255. If the trailing version numbers are 
     *                not specified they are taken as 0s. E.g. Version "3.1" is
     *                equivalent to "3.1.0.0".
     * @return an instance of VersionInfo with the argument version.
     * @exception throws an IllegalArgumentException when the argument version 
     *                is not in the right format
     * @stable ICU 2.6
     */
    public static VersionInfo getInstance(String version)
    {
        int length  = version.length();
        int array[] = {0, 0, 0, 0};
        int count   = 0;
        int index   = 0;
        
        while (count < 4 && index < length) {
            char c = version.charAt(index);
            if (c == '.') {
                count ++;
            }
            else {
                c -= '0';
                if (c < 0 || c > 9) {
                    throw new IllegalArgumentException(INVALID_VERSION_NUMBER_);
                }
                array[count] *= 10;
                array[count] += c;
            }
            index ++;
        }
        if (index != length) {
            throw new IllegalArgumentException(
                                               "Invalid version number: String '" + version + "' exceeds version format");
        }
        for (int i = 0; i < 4; i ++) {
            if (array[i] < 0 || array[i] > 255) {
                throw new IllegalArgumentException(INVALID_VERSION_NUMBER_);
            }
        }
        
        return getInstance(array[0], array[1], array[2], array[3]);
    }
 
    /** 
     * Returns an instance of VersionInfo with the argument version.
     * @param major major version, non-negative number <= 255.
     * @param minor minor version, non-negative number <= 255.
     * @param milli milli version, non-negative number <= 255.
     * @param micro micro version, non-negative number <= 255.
     * @exception throws an IllegalArgumentException when either arguments are
     *                                     negative or > 255 
     * @stable ICU 2.6
     */
    public static VersionInfo getInstance(int major, int minor, int milli, 
                                          int micro)
    {
        // checks if it is in the hashmap
        // else
        if (major < 0 || major > 255 || minor < 0 || minor > 255 || 
            milli < 0 || milli > 255 || micro < 0 || micro > 255) {
            throw new IllegalArgumentException(INVALID_VERSION_NUMBER_);
        }
        int     version = getInt(major, minor, milli, micro);
        Integer key     = new Integer(version);
        Object  result  = MAP_.get(key);
        if (result == null) {
            result = new VersionInfo(version);
            MAP_.put(key, result);
        }
        return (VersionInfo)result;
    }
    
    /** 
     * Returns an instance of VersionInfo with the argument version.
     * Equivalent to getInstance(major, minor, milli, 0).
     * @param major major version, non-negative number <= 255.
     * @param minor minor version, non-negative number <= 255.
     * @param milli milli version, non-negative number <= 255.
     * @exception throws an IllegalArgumentException when either arguments are
     *                                     negative or > 255 
     * @stable ICU 2.6
     */
    public static VersionInfo getInstance(int major, int minor, int milli)
    {
        return getInstance(major, minor, milli, 0);
    }
    
    /** 
     * Returns an instance of VersionInfo with the argument version.
     * Equivalent to getInstance(major, minor, 0, 0).
     * @param major major version, non-negative number <= 255.
     * @param minor minor version, non-negative number <= 255.
     * @exception throws an IllegalArgumentException when either arguments are
     *                                     negative or > 255 
     * @stable ICU 2.6
     */
    public static VersionInfo getInstance(int major, int minor)
    {
        return getInstance(major, minor, 0, 0);
    }
    
    /** 
     * Returns an instance of VersionInfo with the argument version.
     * Equivalent to getInstance(major, 0, 0, 0).
     * @param major major version, non-negative number <= 255.
     * @exception throws an IllegalArgumentException when either arguments are
     *                                     negative or > 255 
     * @stable ICU 2.6
     */
    public static VersionInfo getInstance(int major)
    {
        return getInstance(major, 0, 0, 0);
    }
 
    /** 
     * Returns the String representative of VersionInfo in the format of 
     * "major.minor.milli.micro"   
     * @return String representative of VersionInfo
     * @stable ICU 2.6
     */
    public String toString()
    {
        StringBuffer result = new StringBuffer(7);
        result.append(getMajor());
        result.append('.');
        result.append(getMinor());
        result.append('.');
        result.append(getMilli());
        result.append('.');
        result.append(getMicro());
        return result.toString();
    }
    
    /** 
     * Returns the major version number
     * @return the major version number    
     * @stable ICU 2.6
     */
    public int getMajor()
    {
        return (m_version_ >> 24) & LAST_BYTE_MASK_ ;
    }
 
    /** 
     * Returns the minor version number
     * @return the minor version number    
     * @stable ICU 2.6
     */
    public int getMinor()
    {
        return (m_version_ >> 16) & LAST_BYTE_MASK_ ;
    }
 
    /** 
     * Returns the milli version number
     * @return the milli version number    
     * @stable ICU 2.6
     */
    public int getMilli()
    {
        return (m_version_ >> 8) & LAST_BYTE_MASK_ ;
    }
 
    /** 
     * Returns the micro version number
     * @return the micro version number
     * @stable ICU 2.6    
     */
    public int getMicro()
    {
        return m_version_ & LAST_BYTE_MASK_ ;
    }
 
    /**
     * Checks if this version information is equals to the argument version
     * @param other object to be compared
     * @return true if other is equals to this object's version information, 
     *         false otherwise
     * @stable ICU 2.6
     */
    public boolean equals(Object other)
    {
        return other == this;
    }
 
    /**
     * Compares other with this VersionInfo. 
     * @param other VersionInfo to be compared
     * @return 0 if the argument is a VersionInfo object that has version 
     *           information equals to this object. 
     *           Less than 0 if the argument is a VersionInfo object that has 
     *           version information greater than this object. 
     *           Greater than 0 if the argument is a VersionInfo object that 
     *           has version information less than this object.
     * @stable ICU 2.6
     */
    public int compareTo(VersionInfo other)
    {
        return m_version_ - other.m_version_;
    }
   
    // private data members ----------------------------------------------
    
    /**
     * Version number stored as a byte for each of the major, minor, milli and
     * micro numbers in the 32 bit int.
     * Most significant for the major and the least significant contains the 
     * micro numbers.
     */
    private int m_version_;
    /**
     * Map of singletons
     */
    private static final HashMap MAP_ = new HashMap();
    /**
     * Last byte mask
     */
    private static final int LAST_BYTE_MASK_ = 0xFF;
    /**
     * Error statement string
     */
    private static final String INVALID_VERSION_NUMBER_ = 
        "Invalid version number: Version number may be negative or greater than 255";
        
    // static declaration ------------------------------------------------
    
    /**
     * Initialize versions only after MAP_ has been created
     */
    static {
        UNICODE_1_0   = getInstance(1, 0, 0, 0);
        UNICODE_1_0_1 = getInstance(1, 0, 1, 0);
        UNICODE_1_1_0 = getInstance(1, 1, 0, 0);
        UNICODE_1_1_5 = getInstance(1, 1, 5, 0);
        UNICODE_2_0   = getInstance(2, 0, 0, 0);        
        UNICODE_2_1_2 = getInstance(2, 1, 2, 0);
        UNICODE_2_1_5 = getInstance(2, 1, 5, 0);
        UNICODE_2_1_8 = getInstance(2, 1, 8, 0);
        UNICODE_2_1_9 = getInstance(2, 1, 9, 0);
        UNICODE_3_0   = getInstance(3, 0, 0, 0);
        UNICODE_3_0_1 = getInstance(3, 0, 1, 0);
        UNICODE_3_1_0 = getInstance(3, 1, 0, 0);
        UNICODE_3_1_1 = getInstance(3, 1, 1, 0);
        UNICODE_3_2   = getInstance(3, 2, 0, 0);
        UNICODE_4_0   = getInstance(4, 0, 0, 0);
        ICU_VERSION = getInstance(2, 6, 1, 0);
    }
    
    // private constructor -----------------------------------------------
    
    /**
     * Constructor with int 
     * @param compactversion a 32 bit int with each byte representing a number
     */
    private VersionInfo(int compactversion) 
    {
        m_version_ = compactversion;    
    }
    
    /**
     * Gets the int from the version numbers
     * @param major non-negative version number
     * @param minor non-negativeversion number
     * @param milli non-negativeversion number
     * @param micro non-negativeversion number
     */
    private static int getInt(int major, int minor, int milli, int micro) 
    {
        return (major << 24) | (minor << 16) | (milli << 8) | micro;
    }
}
