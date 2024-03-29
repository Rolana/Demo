/*
 *******************************************************************************
 * Copyright (C) 1996-2003, International Business Machines Corporation and    *
 * others. All Rights Reserved.                                                *
 *******************************************************************************
 *
 * $Source: /cvsroot/doubletype/doubletype/src/com/ibm/icu/impl/Utility.java,v $
 * $Date: 2004/01/17 21:28:03 $
 * $Revision: 1.1 $
 *
 *****************************************************************************************
 */
package com.ibm.icu.impl;
import com.ibm.icu.lang.*;
import com.ibm.icu.text.*;
import com.ibm.icu.impl.UCharacterProperty;

public final class Utility {

    private static final char APOSTROPHE = '\'';
    private static final char BACKSLASH  = '\\';
    private static final int MAGIC_UNSIGNED = 0x80000000;

    /**
     * Convenience utility to compare two Object[]s.
     * Ought to be in System
     */
    public final static boolean arrayEquals(Object[] source, Object target) {
        if (source == null) return (target == null);
        if (!(target instanceof Object[])) return false;
        Object[] targ = (Object[]) target;
        return (source.length == targ.length
                && arrayRegionMatches(source, 0, targ, 0, source.length));
    }

    /**
     * Convenience utility to compare two int[]s
     * Ought to be in System
     */
    ///CLOVER:OFF
    public final static boolean arrayEquals(int[] source, Object target) {
        if (source == null) return (target == null);
        if (!(target instanceof int[])) return false;
        int[] targ = (int[]) target;
        return (source.length == targ.length
                && arrayRegionMatches(source, 0, targ, 0, source.length));
    }
    ///CLOVER:ON

    /**
     * Convenience utility to compare two double[]s
     * Ought to be in System
     */
    ///CLOVER:OFF
    public final static boolean arrayEquals(double[] source, Object target) {
        if (source == null) return (target == null);
        if (!(target instanceof double[])) return false;
        double[] targ = (double[]) target;
        return (source.length == targ.length
                && arrayRegionMatches(source, 0, targ, 0, source.length));
    }
    ///CLOVER:ON

    /**
     * Convenience utility to compare two Object[]s
     * Ought to be in System
     */
    public final static boolean arrayEquals(Object source, Object target) {
        if (source == null) return (target == null);
        // for some reason, the correct arrayEquals is not being called
        // so do it by hand for now.
        if (source instanceof Object[])
            return(arrayEquals((Object[]) source,target));
        if (source instanceof int[])
            return(arrayEquals((int[]) source,target));
        if (source instanceof double[])
            return(arrayEquals((int[]) source,target));
        return source.equals(target);
    }

    /**
     * Convenience utility to compare two Object[]s
     * Ought to be in System.
     * @param len the length to compare.
     * The start indices and start+len must be valid.
     */
    public final static boolean arrayRegionMatches(Object[] source, int sourceStart,
                                            Object[] target, int targetStart,
                                            int len)
    {
        int sourceEnd = sourceStart + len;
        int delta = targetStart - sourceStart;
        for (int i = sourceStart; i < sourceEnd; i++) {
            if (!arrayEquals(source[i],target[i + delta]))
            return false;
        }
        return true;
    }

    /**
     * Convenience utility to compare two Object[]s
     * Ought to be in System.
     * @param len the length to compare.
     * The start indices and start+len must be valid.
     */
    public final static boolean arrayRegionMatches(char[] source, int sourceStart,
                                            char[] target, int targetStart,
                                            int len)
    {
        int sourceEnd = sourceStart + len;
        int delta = targetStart - sourceStart;
        for (int i = sourceStart; i < sourceEnd; i++) {
            if (source[i]!=target[i + delta])
            return false;
        }
        return true;
    }

    /**
     * Convenience utility to compare two int[]s.
     * @param len the length to compare.
     * The start indices and start+len must be valid.
     * Ought to be in System
     */
    ///CLOVER:OFF
    public final static boolean arrayRegionMatches(int[] source, int sourceStart,
                                            int[] target, int targetStart,
                                            int len)
    {
        int sourceEnd = sourceStart + len;
        int delta = targetStart - sourceStart;
        for (int i = sourceStart; i < sourceEnd; i++) {
            if (source[i] != target[i + delta])
            return false;
        }
        return true;
    }
    ///CLOVER:ON

    /**
     * Convenience utility to compare two arrays of doubles.
     * @param len the length to compare.
     * The start indices and start+len must be valid.
     * Ought to be in System
     */
    ///CLOVER:OFF
    public final static boolean arrayRegionMatches(double[] source, int sourceStart,
                                            double[] target, int targetStart,
                                            int len)
    {
        int sourceEnd = sourceStart + len;
        int delta = targetStart - sourceStart;
        for (int i = sourceStart; i < sourceEnd; i++) {
            if (source[i] != target[i + delta])
            return false;
        }
        return true;
    }
    ///CLOVER:ON

    /**
     * Convenience utility. Does null checks on objects, then calls equals.
     */
    public final static boolean objectEquals(Object source, Object target) {
    if (source == null)
            return (target == null);
    else
            return source.equals(target);
    }

    /**
     * The ESCAPE character is used during run-length encoding.  It signals
     * a run of identical chars.
     */
    private static final char ESCAPE = '\uA5A5';

    /**
     * The ESCAPE_BYTE character is used during run-length encoding.  It signals
     * a run of identical bytes.
     */
    static final byte ESCAPE_BYTE = (byte)0xA5;

    /**
     * Construct a string representing an int array.  Use run-length encoding.
     * A character represents itself, unless it is the ESCAPE character.  Then
     * the following notations are possible:
     *   ESCAPE ESCAPE   ESCAPE literal
     *   ESCAPE n c      n instances of character c
     * Since an encoded run occupies 3 characters, we only encode runs of 4 or
     * more characters.  Thus we have n > 0 and n != ESCAPE and n <= 0xFFFF.
     * If we encounter a run where n == ESCAPE, we represent this as:
     *   c ESCAPE n-1 c
     * The ESCAPE value is chosen so as not to collide with commonly
     * seen values.
     */
    ///CLOVER:OFF
    static public final String arrayToRLEString(int[] a) {
        StringBuffer buffer = new StringBuffer();

        appendInt(buffer, a.length);
        int runValue = a[0];
        int runLength = 1;
        for (int i=1; i<a.length; ++i) {
            int s = a[i];
            if (s == runValue && runLength < 0xFFFF) {
                ++runLength;
            } else {
                encodeRun(buffer, runValue, runLength);
                runValue = s;
                runLength = 1;
            }
        }
        encodeRun(buffer, runValue, runLength);
        return buffer.toString();
    }
    ///CLOVER:ON

    /**
     * Construct a string representing a short array.  Use run-length encoding.
     * A character represents itself, unless it is the ESCAPE character.  Then
     * the following notations are possible:
     *   ESCAPE ESCAPE   ESCAPE literal
     *   ESCAPE n c      n instances of character c
     * Since an encoded run occupies 3 characters, we only encode runs of 4 or
     * more characters.  Thus we have n > 0 and n != ESCAPE and n <= 0xFFFF.
     * If we encounter a run where n == ESCAPE, we represent this as:
     *   c ESCAPE n-1 c
     * The ESCAPE value is chosen so as not to collide with commonly
     * seen values.
     */
    ///CLOVER:OFF
    static public final String arrayToRLEString(short[] a) {
        StringBuffer buffer = new StringBuffer();
        // for (int i=0; i<a.length; ++i) buffer.append((char) a[i]);
        buffer.append((char) (a.length >> 16));
        buffer.append((char) a.length);
        short runValue = a[0];
        int runLength = 1;
        for (int i=1; i<a.length; ++i) {
            short s = a[i];
            if (s == runValue && runLength < 0xFFFF) ++runLength;
            else {
            encodeRun(buffer, runValue, runLength);
            runValue = s;
            runLength = 1;
            }
        }
        encodeRun(buffer, runValue, runLength);
        return buffer.toString();
    }
    ///CLOVER:ON

    /**
     * Construct a string representing a char array.  Use run-length encoding.
     * A character represents itself, unless it is the ESCAPE character.  Then
     * the following notations are possible:
     *   ESCAPE ESCAPE   ESCAPE literal
     *   ESCAPE n c      n instances of character c
     * Since an encoded run occupies 3 characters, we only encode runs of 4 or
     * more characters.  Thus we have n > 0 and n != ESCAPE and n <= 0xFFFF.
     * If we encounter a run where n == ESCAPE, we represent this as:
     *   c ESCAPE n-1 c
     * The ESCAPE value is chosen so as not to collide with commonly
     * seen values.
     */
    static public final String arrayToRLEString(char[] a) {
        StringBuffer buffer = new StringBuffer();
        buffer.append((char) (a.length >> 16));
        buffer.append((char) a.length);
        char runValue = a[0];
        int runLength = 1;
        for (int i=1; i<a.length; ++i) {
            char s = a[i];
            if (s == runValue && runLength < 0xFFFF) ++runLength;
            else {
            encodeRun(buffer, (short)runValue, runLength);
            runValue = s;
            runLength = 1;
            }
        }
        encodeRun(buffer, (short)runValue, runLength);
        return buffer.toString();
    }

    /**
     * Construct a string representing a byte array.  Use run-length encoding.
     * Two bytes are packed into a single char, with a single extra zero byte at
     * the end if needed.  A byte represents itself, unless it is the
     * ESCAPE_BYTE.  Then the following notations are possible:
     *   ESCAPE_BYTE ESCAPE_BYTE   ESCAPE_BYTE literal
     *   ESCAPE_BYTE n b           n instances of byte b
     * Since an encoded run occupies 3 bytes, we only encode runs of 4 or
     * more bytes.  Thus we have n > 0 and n != ESCAPE_BYTE and n <= 0xFF.
     * If we encounter a run where n == ESCAPE_BYTE, we represent this as:
     *   b ESCAPE_BYTE n-1 b
     * The ESCAPE_BYTE value is chosen so as not to collide with commonly
     * seen values.
     */
    static public final String arrayToRLEString(byte[] a) {
        StringBuffer buffer = new StringBuffer();
        buffer.append((char) (a.length >> 16));
        buffer.append((char) a.length);
        byte runValue = a[0];
        int runLength = 1;
        byte[] state = new byte[2];
        for (int i=1; i<a.length; ++i) {
            byte b = a[i];
            if (b == runValue && runLength < 0xFF) ++runLength;
            else {
                encodeRun(buffer, runValue, runLength, state);
                runValue = b;
                runLength = 1;
            }
        }
        encodeRun(buffer, runValue, runLength, state);

        // We must save the final byte, if there is one, by padding
        // an extra zero.
        if (state[0] != 0) appendEncodedByte(buffer, (byte)0, state);

        return buffer.toString();
    }

    /**
     * Encode a run, possibly a degenerate run (of < 4 values).
     * @param length The length of the run; must be > 0 && <= 0xFFFF.
     */
    ///CLOVER:OFF
    private static final void encodeRun(StringBuffer buffer, int value, int length) {
        if (length < 4) {
            for (int j=0; j<length; ++j) {
                if (value == ESCAPE) {
                    appendInt(buffer, value);
                }
                appendInt(buffer, value);
            }
        }
        else {
            if (length == (int) ESCAPE) {
                if (value == (int) ESCAPE) {
                    appendInt(buffer, ESCAPE);
                }
                appendInt(buffer, value);
                --length;
            }
            appendInt(buffer, ESCAPE);
            appendInt(buffer, length);
            appendInt(buffer, value); // Don't need to escape this value
        }
    }
    ///CLOVER:ON
    
    ///CLOVER:OFF
    private static final void appendInt(StringBuffer buffer, int value) {
        buffer.append((char)(value >>> 16));
        buffer.append((char)(value & 0xFFFF));
    }
    ///CLOVER:ON

    /**
     * Encode a run, possibly a degenerate run (of < 4 values).
     * @param length The length of the run; must be > 0 && <= 0xFFFF.
     */
    private static final void encodeRun(StringBuffer buffer, short value, int length) {
        if (length < 4) {
            for (int j=0; j<length; ++j) {
                if (value == (int) ESCAPE) buffer.append(ESCAPE);
                buffer.append((char) value);
            }
        }
        else {
            if (length == (int) ESCAPE) {
                if (value == (int) ESCAPE) buffer.append(ESCAPE);
                buffer.append((char) value);
                --length;
            }
            buffer.append(ESCAPE);
            buffer.append((char) length);
            buffer.append((char) value); // Don't need to escape this value
        }
    }

    /**
     * Encode a run, possibly a degenerate run (of < 4 values).
     * @param length The length of the run; must be > 0 && <= 0xFF.
     */
    private static final void encodeRun(StringBuffer buffer, byte value, int length,
                    byte[] state) {
        if (length < 4) {
            for (int j=0; j<length; ++j) {
                if (value == ESCAPE_BYTE) appendEncodedByte(buffer, ESCAPE_BYTE, state);
                appendEncodedByte(buffer, value, state);
            }
        }
        else {
            if (length == ESCAPE_BYTE) {
            if (value == ESCAPE_BYTE) appendEncodedByte(buffer, ESCAPE_BYTE, state);
            appendEncodedByte(buffer, value, state);
            --length;
            }
            appendEncodedByte(buffer, ESCAPE_BYTE, state);
            appendEncodedByte(buffer, (byte)length, state);
            appendEncodedByte(buffer, value, state); // Don't need to escape this value
        }
    }

    /**
     * Append a byte to the given StringBuffer, packing two bytes into each
     * character.  The state parameter maintains intermediary data between
     * calls.
     * @param state A two-element array, with state[0] == 0 if this is the
     * first byte of a pair, or state[0] != 0 if this is the second byte
     * of a pair, in which case state[1] is the first byte.
     */
    private static final void appendEncodedByte(StringBuffer buffer, byte value,
                        byte[] state) {
        if (state[0] != 0) {
            char c = (char) ((state[1] << 8) | (((int) value) & 0xFF));
            buffer.append(c);
            state[0] = 0;
        }
        else {
            state[0] = 1;
            state[1] = value;
        }
    }

    /**
     * Construct an array of ints from a run-length encoded string.
     */
    static public final int[] RLEStringToIntArray(String s) {
        int length = getInt(s, 0);
        int[] array = new int[length];
        int ai = 0, i = 1;

        int maxI = s.length() / 2;
        while (ai < length && i < maxI) {
            int c = getInt(s, i++);

            if (c == ESCAPE) {
                c = getInt(s, i++);
                if (c == ESCAPE) {
                    array[ai++] = c;
                } else {
                    int runLength = c;
                    int runValue = getInt(s, i++);
                    for (int j=0; j<runLength; ++j) {
                        array[ai++] = runValue;
                    }
                }
            }
            else {
                array[ai++] = c;
            }
        }

        if (ai != length || i != maxI) {
            throw new InternalError("Bad run-length encoded int array");
        }

        return array;
    }
    static final int getInt(String s, int i) {
        return (((int) s.charAt(2*i)) << 16) | (int) s.charAt(2*i+1);
    }


    /**
     * Construct an array of shorts from a run-length encoded string.
     */
    ///CLOVER:OFF
    static public final short[] RLEStringToShortArray(String s) {
        int length = (((int) s.charAt(0)) << 16) | ((int) s.charAt(1));
        short[] array = new short[length];
        int ai = 0;
        for (int i=2; i<s.length(); ++i) {
            char c = s.charAt(i);
            if (c == ESCAPE) {
                c = s.charAt(++i);
                if (c == ESCAPE) {
                    array[ai++] = (short) c;
                } else {
                    int runLength = (int) c;
                    short runValue = (short) s.charAt(++i);
                    for (int j=0; j<runLength; ++j) array[ai++] = runValue;
                }
            }
            else {
                array[ai++] = (short) c;
            }
        }

        if (ai != length)
            throw new InternalError("Bad run-length encoded short array");

        return array;
    }
    ///CLOVER:ON

    /**
     * Construct an array of shorts from a run-length encoded string.
     */
    static public final char[] RLEStringToCharArray(String s) {
        int length = (((int) s.charAt(0)) << 16) | ((int) s.charAt(1));
        char[] array = new char[length];
        int ai = 0;
        for (int i=2; i<s.length(); ++i) {
            char c = s.charAt(i);
            if (c == ESCAPE) {
                c = s.charAt(++i);
                if (c == ESCAPE) {
                    array[ai++] = c;
                } else {
                    int runLength = (int) c;
                    char runValue = s.charAt(++i);
                    for (int j=0; j<runLength; ++j) array[ai++] = runValue;
                }
            }
            else {
                array[ai++] = c;
            }
        }

        if (ai != length)
            throw new InternalError("Bad run-length encoded short array");

        return array;
    }

    /**
     * Construct an array of bytes from a run-length encoded string.
     */
    static public final byte[] RLEStringToByteArray(String s) {
        int length = (((int) s.charAt(0)) << 16) | ((int) s.charAt(1));
        byte[] array = new byte[length];
        boolean nextChar = true;
        char c = 0;
        int node = 0;
        int runLength = 0;
        int i = 2;
        for (int ai=0; ai<length; ) {
            // This part of the loop places the next byte into the local
            // variable 'b' each time through the loop.  It keeps the
            // current character in 'c' and uses the boolean 'nextChar'
            // to see if we've taken both bytes out of 'c' yet.
            byte b;
            if (nextChar) {
                c = s.charAt(i++);
                b = (byte) (c >> 8);
                nextChar = false;
            }
            else {
                b = (byte) (c & 0xFF);
                nextChar = true;
            }

            // This part of the loop is a tiny state machine which handles
            // the parsing of the run-length encoding.  This would be simpler
            // if we could look ahead, but we can't, so we use 'node' to
            // move between three nodes in the state machine.
            switch (node) {
            case 0:
                // Normal idle node
                if (b == ESCAPE_BYTE) {
                    node = 1;
                }
                else {
                    array[ai++] = b;
                }
                break;
            case 1:
                // We have seen one ESCAPE_BYTE; we expect either a second
                // one, or a run length and value.
                if (b == ESCAPE_BYTE) {
                    array[ai++] = ESCAPE_BYTE;
                    node = 0;
                }
                else {
                    runLength = b;
                    // Interpret signed byte as unsigned
                    if (runLength < 0) runLength += 0x100;
                    node = 2;
                }
                break;
            case 2:
                // We have seen an ESCAPE_BYTE and length byte.  We interpret
                // the next byte as the value to be repeated.
                for (int j=0; j<runLength; ++j) array[ai++] = b;
                node = 0;
                break;
            }
        }

        if (node != 0)
            throw new InternalError("Bad run-length encoded byte array");

        if (i != s.length())
            throw new InternalError("Excess data in RLE byte array string");

        return array;
    }

    static public String LINE_SEPARATOR = System.getProperty("line.separator");

    /**
     * Format a String for representation in a source file.  This includes
     * breaking it into lines and escaping characters using octal notation
     * when necessary (control characters and double quotes).
     */
    static public final String formatForSource(String s) {
        StringBuffer buffer = new StringBuffer();
        for (int i=0; i<s.length();) {
            if (i > 0) buffer.append('+').append(LINE_SEPARATOR);
            buffer.append("        \"");
            int count = 11;
            while (i<s.length() && count<80) {
            char c = s.charAt(i++);
            if (c < '\u0020' || c == '"' || c == '\\') {
                // Represent control characters, backslash and double quote
                // using octal notation; otherwise the string we form
                // won't compile, since Unicode escape sequences are
                // processed before tokenization.
                buffer.append('\\');
                buffer.append(HEX_DIGIT[(c & 0700) >> 6]); // HEX_DIGIT works for octal
                buffer.append(HEX_DIGIT[(c & 0070) >> 3]);
                buffer.append(HEX_DIGIT[(c & 0007)]);
                count += 4;
            }
            else if (c <= '\u007E') {
                buffer.append(c);
                count += 1;
            }
            else {
                buffer.append("\\u");
                buffer.append(HEX_DIGIT[(c & 0xF000) >> 12]);
                buffer.append(HEX_DIGIT[(c & 0x0F00) >> 8]);
                buffer.append(HEX_DIGIT[(c & 0x00F0) >> 4]);
                buffer.append(HEX_DIGIT[(c & 0x000F)]);
                count += 6;
            }
            }
            buffer.append('"');
        }
        return buffer.toString();
    }

    static final char[] HEX_DIGIT = {'0','1','2','3','4','5','6','7',
                     '8','9','A','B','C','D','E','F'};

    /**
     * Format a String for representation in a source file.  Like
     * formatForSource but does not do line breaking.
     */
    static public final String format1ForSource(String s) {
        StringBuffer buffer = new StringBuffer();
        buffer.append("\"");
        for (int i=0; i<s.length();) {
            char c = s.charAt(i++);
            if (c < '\u0020' || c == '"' || c == '\\') {
                // Represent control characters, backslash and double quote
                // using octal notation; otherwise the string we form
                // won't compile, since Unicode escape sequences are
                // processed before tokenization.
                buffer.append('\\');
                buffer.append(HEX_DIGIT[(c & 0700) >> 6]); // HEX_DIGIT works for octal
                buffer.append(HEX_DIGIT[(c & 0070) >> 3]);
                buffer.append(HEX_DIGIT[(c & 0007)]);
            }
            else if (c <= '\u007E') {
                buffer.append(c);
            }
            else {
                buffer.append("\\u");
                buffer.append(HEX_DIGIT[(c & 0xF000) >> 12]);
                buffer.append(HEX_DIGIT[(c & 0x0F00) >> 8]);
                buffer.append(HEX_DIGIT[(c & 0x00F0) >> 4]);
                buffer.append(HEX_DIGIT[(c & 0x000F)]);
            }
        }
        buffer.append('"');
        return buffer.toString();
    }

    /**
     * Convert characters outside the range U+0020 to U+007F to
     * Unicode escapes, and convert backslash to a double backslash.
     */
    public static final String escape(String s) {
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<s.length(); ) {
            int c = UTF16.charAt(s, i);
            i += UTF16.getCharCount(c);
            if (c >= ' ' && c <= 0x007F) {
                if (c == '\\') {
                    buf.append("\\\\"); // That is, "\\"
                } else {
                    buf.append((char)c);
                }
            } else {
                boolean four = c <= 0xFFFF;
                buf.append(four ? "\\u" : "\\U");
                hex(c, four ? 4 : 8, buf);
            }
        }
        return buf.toString();
    }

    /* This map must be in ASCENDING ORDER OF THE ESCAPE CODE */
    static private final char[] UNESCAPE_MAP = {
        /*"   0x22, 0x22 */
        /*'   0x27, 0x27 */
        /*?   0x3F, 0x3F */
        /*\   0x5C, 0x5C */
        /*a*/ 0x61, 0x07,
        /*b*/ 0x62, 0x08,
        /*e*/ 0x65, 0x1b,
        /*f*/ 0x66, 0x0c,
        /*n*/ 0x6E, 0x0a,
        /*r*/ 0x72, 0x0d,
        /*t*/ 0x74, 0x09,
        /*v*/ 0x76, 0x0b
    };

    /**
     * Convert an escape to a 32-bit code point value.  We attempt
     * to parallel the icu4c unesacpeAt() function.
     * @param offset16 an array containing offset to the character
     * <em>after</em> the backslash.  Upon return offset16[0] will
     * be updated to point after the escape sequence.
     * @return character value from 0 to 10FFFF, or -1 on error.
     */
    public static int unescapeAt(String s, int[] offset16) {
        int c;
        int result = 0;
        int n = 0;
        int minDig = 0;
        int maxDig = 0;
        int bitsPerDigit = 4;
        int dig;
        int i;
        boolean braces = false;

        /* Check that offset is in range */
        int offset = offset16[0];
        int length = s.length();
        if (offset < 0 || offset >= length) {
            return -1;
        }

        /* Fetch first UChar after '\\' */
        c = UTF16.charAt(s, offset);
        offset += UTF16.getCharCount(c);

        /* Convert hexadecimal and octal escapes */
        switch (c) {
        case 'u':
            minDig = maxDig = 4;
            break;
        case 'U':
            minDig = maxDig = 8;
            break;
        case 'x':
            minDig = 1;
            if (offset < length && UTF16.charAt(s, offset) == 0x7B /*{*/) {
                ++offset;
                braces = true;
                maxDig = 8;
            } else {
                maxDig = 2;
            }
            break;
        default:
            dig = UCharacter.digit(c, 8);
            if (dig >= 0) {
                minDig = 1;
                maxDig = 3;
                n = 1; /* Already have first octal digit */
                bitsPerDigit = 3;
                result = dig;
            }
            break;
        }
        if (minDig != 0) {
            while (offset < length && n < maxDig) {
                c = UTF16.charAt(s, offset);
                dig = UCharacter.digit(c, (bitsPerDigit == 3) ? 8 : 16);
                if (dig < 0) {
                    break;
                }
                result = (result << bitsPerDigit) | dig;
                offset += UTF16.getCharCount(c);
                ++n;
            }
            if (n < minDig) {
                return -1;
            }
            if (braces) {
                if (c != 0x7D /*}*/) {
                    return -1;
                }
                ++offset;
            }
            offset16[0] = offset;
            return result;
        }

        /* Convert C-style escapes in table */
        for (i=0; i<UNESCAPE_MAP.length; i+=2) {
            if (c == UNESCAPE_MAP[i]) {
                offset16[0] = offset;
                return UNESCAPE_MAP[i+1];
            } else if (c < UNESCAPE_MAP[i]) {
                break;
            }
        }

        /* Map \cX to control-X: X & 0x1F */
        if (c == 'c' && offset < length) {
            c = UTF16.charAt(s, offset);
            offset16[0] = offset + UTF16.getCharCount(c);
            return 0x1F & c;
        }

        /* If no special forms are recognized, then consider
         * the backslash to generically escape the next character. */
        offset16[0] = offset;
        return c;
    }

    /**
     * Convert all escapes in a given string using unescapeAt().
     * @exception IllegalArgumentException if an invalid escape is
     * seen.
     */
    public static String unescape(String s) {
        StringBuffer buf = new StringBuffer();
        int[] pos = new int[1];
        for (int i=0; i<s.length(); ) {
            char c = s.charAt(i++);
            if (c == '\\') {
                pos[0] = i;
                int e = unescapeAt(s, pos);
                if (e < 0) {
                    throw new IllegalArgumentException("Invalid escape sequence " +
                                                       s.substring(i-1, Math.min(i+8, s.length())));
                }
                UTF16.append(buf, e);
                i = pos[0];
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }

    /**
     * Convert all escapes in a given string using unescapeAt().
     * Leave invalid escape sequences unchanged.
     */
    ///CLOVER:OFF
    public static String unescapeLeniently(String s) {
        StringBuffer buf = new StringBuffer();
        int[] pos = new int[1];
        for (int i=0; i<s.length(); ) {
            char c = s.charAt(i++);
            if (c == '\\') {
                pos[0] = i;
                int e = unescapeAt(s, pos);
                if (e < 0) {
                    buf.append(c);
                } else {
                    UTF16.append(buf, e);
                    i = pos[0];
                }
            } else {
                buf.append(c);
            }
        }
        return buf.toString();
    }
    ///CLOVER:ON

    /**
     * Convert a char to 4 hex uppercase digits.  E.g., hex('a') =>
     * "0041".
     */
    ///CLOVER:OFF
    public static String hex(char ch) {
        StringBuffer temp = new StringBuffer();
        return hex(ch, temp).toString();
    }
    ///CLOVER:ON

    /**
     * Convert a string to comma-separated groups of 4 hex uppercase
     * digits.  E.g., hex('ab') => "0041,0042".
     */
    ///CLOVER:OFF
    public static String hex(String s) {
        StringBuffer temp = new StringBuffer();
        return hex(s, temp).toString();
    }
    ///CLOVER:ON

    /**
     * Convert a string to comma-separated groups of 4 hex uppercase
     * digits.  E.g., hex('ab') => "0041,0042".
     */
    ///CLOVER:OFF
    public static String hex(StringBuffer s) {
        return hex(s.toString());
    }
    ///CLOVER:ON

    /**
     * Convert a char to 4 hex uppercase digits.  E.g., hex('a') =>
     * "0041".  Append the output to the given StringBuffer.
     */
    ///CLOVER:OFF
    public static StringBuffer hex(char ch, StringBuffer output) {
        return appendNumber(output, ch, 16, 4);
    }
    ///CLOVER:ON

    /**
     * Convert a integer to size width hex uppercase digits.
     * E.g., hex('a', 4, str) => "0041".
     * Append the output to the given StringBuffer.
     * If width is too small to fit, nothing will be appended to output.
     */
    public static StringBuffer hex(int ch, int width, StringBuffer output) {
        return appendNumber(output, ch, 16, width);
    }

    /**
     * Convert a integer to size width (minimum) hex uppercase digits.
     * E.g., hex('a', 4, str) => "0041".  If the integer requires more
     * than width digits, more will be used.
     */
    public static String hex(int ch, int width) {
        StringBuffer buf = new StringBuffer();
        return appendNumber(buf, ch, 16, width).toString();
    }

    /**
     * Convert a string to comma-separated groups of 4 hex uppercase
     * digits.  E.g., hex('ab') => "0041,0042".  Append the output
     * to the given StringBuffer.
     */
    ///CLOVER:OFF
    public static StringBuffer hex(String s, StringBuffer result) {
        for (int i = 0; i < s.length(); ++i) {
            if (i != 0) result.append(',');
            hex(s.charAt(i), result);
        }
        return result;
    }
    ///CLOVER:ON

    /**
     * Split a string into pieces based on the given divider character
     * @param s the string to split
     * @param divider the character on which to split.  Occurrences of
     * this character are not included in the output
     * @param output an array to receive the substrings between
     * instances of divider.  It must be large enough on entry to
     * accomodate all output.  Adjacent instances of the divider
     * character will place empty strings into output.  Before
     * returning, output is padded out with empty strings.
     */
    ///CLOVER:OFF
    public static void split(String s, char divider, String[] output) {
        int last = 0;
        int current = 0;
        int i;
        for (i = 0; i < s.length(); ++i) {
            if (s.charAt(i) == divider) {
                output[current++] = s.substring(last,i);
                last = i+1;
            }
        }
        output[current++] = s.substring(last,i);
        while (current < output.length) {
            output[current++] = "";
        }
    }
    ///CLOVER:ON

    /**
     * Look up a given string in a string array.  Returns the index at
     * which the first occurrence of the string was found in the
     * array, or -1 if it was not found.
     * @param source the string to search for
     * @param target the array of zero or more strings in which to
     * look for source
     * @return the index of target at which source first occurs, or -1
     * if not found
     */
    ///CLOVER:OFF
    public static int lookup(String source, String[] target) {
        for (int i = 0; i < target.length; ++i) {
            if (source.equals(target[i])) return i;
        }
        return -1;
    }
    ///CLOVER:ON

    /**
     * Skip over a sequence of zero or more white space characters
     * at pos.  Return the index of the first non-white-space character
     * at or after pos, or str.length(), if there is none.
     */
    public static int skipWhitespace(String str, int pos) {
        while (pos < str.length()) {
            int c = UTF16.charAt(str, pos);
            if (!UCharacterProperty.isRuleWhiteSpace(c)) {
                break;
            }
            pos += UTF16.getCharCount(c);
        }
        return pos;
    }

    /**
     * Skip over a sequence of zero or more white space characters
     * at pos[0], advancing it.
     */
    public static void skipWhitespace(String str, int[] pos) {
        pos[0] = skipWhitespace(str, pos[0]);
    }

    /**
     * Remove all rule white space from a string.
     */
    public static String deleteRuleWhiteSpace(String str) {
        StringBuffer buf = new StringBuffer();
        for (int i=0; i<str.length(); ) {
            int ch = UTF16.charAt(str, i);
            i += UTF16.getCharCount(ch);
            if (UCharacterProperty.isRuleWhiteSpace(ch)) {
                continue;
            }
            UTF16.append(buf, ch);
        }
        return buf.toString();
    }

    /**
     * Parse a single non-whitespace character 'ch', optionally
     * preceded by whitespace.
     * @param id the string to be parsed
     * @param pos INPUT-OUTPUT parameter.  On input, pos[0] is the
     * offset of the first character to be parsed.  On output, pos[0]
     * is the index after the last parsed character.  If the parse
     * fails, pos[0] will be unchanged.
     * @param ch the non-whitespace character to be parsed.
     * @return true if 'ch' is seen preceded by zero or more
     * whitespace characters.
     */
    public static boolean parseChar(String id, int[] pos, char ch) {
        int start = pos[0];
        skipWhitespace(id, pos);
        if (pos[0] == id.length() ||
            id.charAt(pos[0]) != ch) {
            pos[0] = start;
            return false;
        }
        ++pos[0];
        return true;
    }

    /**
     * Parse a pattern string starting at offset pos.  Keywords are
     * matched case-insensitively.  Spaces may be skipped and may be
     * optional or required.  Integer values may be parsed, and if
     * they are, they will be returned in the given array.  If
     * successful, the offset of the next non-space character is
     * returned.  On failure, -1 is returned.
     * @param pattern must only contain lowercase characters, which
     * will match their uppercase equivalents as well.  A space
     * character matches one or more required spaces.  A '~' character
     * matches zero or more optional spaces.  A '#' character matches
     * an integer and stores it in parsedInts, which the caller must
     * ensure has enough capacity.
     * @param parsedInts array to receive parsed integers.  Caller
     * must ensure that parsedInts.length is >= the number of '#'
     * signs in 'pattern'.
     * @return the position after the last character parsed, or -1 if
     * the parse failed
     */
    public static int parsePattern(String rule, int pos, int limit,
                                   String pattern, int[] parsedInts) {
        // TODO Update this to handle surrogates
        int[] p = new int[1];
        int intCount = 0; // number of integers parsed
        for (int i=0; i<pattern.length(); ++i) {
            char cpat = pattern.charAt(i);
            char c;
            switch (cpat) {
            case ' ':
                if (pos >= limit) {
                    return -1;
                }
                c = rule.charAt(pos++);
                if (!UCharacterProperty.isRuleWhiteSpace(c)) {
                    return -1;
                }
                // FALL THROUGH to skipWhitespace
            case '~':
                pos = skipWhitespace(rule, pos);
                break;
            case '#':
                p[0] = pos;
                parsedInts[intCount++] = parseInteger(rule, p, limit);
                if (p[0] == pos) {
                    // Syntax error; failed to parse integer
                    return -1;
                }
                pos = p[0];
                break;
            default:
                if (pos >= limit) {
                    return -1;
                }
                c = (char) UCharacter.toLowerCase(rule.charAt(pos++));
                if (c != cpat) {
                    return -1;
                }
                break;
            }
        }
        return pos;
    }

    /**
     * Parse a pattern string within the given Replaceable and a parsing
     * pattern.  Characters are matched literally and case-sensitively
     * except for the following special characters:
     *
     * ~  zero or more uprv_isRuleWhiteSpace chars
     *
     * If end of pattern is reached with all matches along the way,
     * pos is advanced to the first unparsed index and returned.
     * Otherwise -1 is returned.
     * @param pat pattern that controls parsing
     * @param text text to be parsed, starting at index
     * @param index offset to first character to parse
     * @param limit offset after last character to parse
     * @return index after last parsed character, or -1 on parse failure.
     */
    public static int parsePattern(String pat,
                                   Replaceable text,
                                   int index,
                                   int limit) {
        int ipat = 0;

        // empty pattern matches immediately
        if (ipat == pat.length()) {
            return index;
        }

        int cpat = UTF16.charAt(pat, ipat);

        while (index < limit) {
            int c = text.char32At(index);

            // parse \s*
            if (cpat == '~') {
                if (UCharacterProperty.isRuleWhiteSpace(c)) {
                    index += UTF16.getCharCount(c);
                    continue;
                } else {
                    if (++ipat == pat.length()) {
                        return index; // success; c unparsed
                    }
                    // fall thru; process c again with next cpat
                }
            }

            // parse literal
            else if (c == cpat) {
                int n = UTF16.getCharCount(c);
                index += n;
                ipat += n;
                if (ipat == pat.length()) {
                    return index; // success; c parsed
                }
                // fall thru; get next cpat
            }

            // match failure of literal
            else {
                return -1;
            }

            cpat = UTF16.charAt(pat, ipat);
        }

        return -1; // text ended before end of pat
    }

    /**
     * Parse an integer at pos, either of the form \d+ or of the form
     * 0x[0-9A-Fa-f]+ or 0[0-7]+, that is, in standard decimal, hex,
     * or octal format.
     * @param pos INPUT-OUTPUT parameter.  On input, the first
     * character to parse.  On output, the character after the last
     * parsed character.
     */
    public static int parseInteger(String rule, int[] pos, int limit) {
        int count = 0;
        int value = 0;
        int p = pos[0];
        int radix = 10;

        if (rule.regionMatches(true, p, "0x", 0, 2)) {
            p += 2;
            radix = 16;
        } else if (p < limit && rule.charAt(p) == '0') {
            p++;
            count = 1;
            radix = 8;
        }

        while (p < limit) {
            int d = UCharacter.digit(rule.charAt(p++), radix);
            if (d < 0) {
                --p;
                break;
            }
            ++count;
            int v = (value * radix) + d;
            if (v <= value) {
                // If there are too many input digits, at some point
                // the value will go negative, e.g., if we have seen
                // "0x8000000" already and there is another '0', when
                // we parse the next 0 the value will go negative.
                return 0;
            }
            value = v;
        }
        if (count > 0) {
            pos[0] = p;
        }
        return value;
    }

    /**
     * Parse a Unicode identifier from the given string at the given
     * position.  Return the identifier, or null if there is no
     * identifier.
     * @param str the string to parse
     * @param pos INPUT-OUPUT parameter.  On INPUT, pos[0] is the
     * first character to examine.  It must be less than str.length(),
     * and it must not point to a whitespace character.  That is, must
     * have pos[0] < str.length() and
     * !UCharacterProperty.isRuleWhiteSpace(UTF16.charAt(str, pos[0])).  On
     * OUTPUT, the position after the last parsed character.
     * @return the Unicode identifier, or null if there is no valid
     * identifier at pos[0].
     */
    public static String parseUnicodeIdentifier(String str, int[] pos) {
        // assert(pos[0] < str.length());
        // assert(!UCharacterProperty.isRuleWhiteSpace(UTF16.charAt(str, pos[0])));
        StringBuffer buf = new StringBuffer();
        int p = pos[0];
        while (p < str.length()) {
            int ch = UTF16.charAt(str, p);
            if (buf.length() == 0) {
                if (UCharacter.isUnicodeIdentifierStart(ch)) {
                    UTF16.append(buf, ch);
                } else {
                    return null;
                }
            } else {
                if (UCharacter.isUnicodeIdentifierPart(ch)) {
                    UTF16.append(buf, ch);
                } else {
                    break;
                }
            }
            p += UTF16.getCharCount(ch);
        }
        pos[0] = p;
        return buf.toString();
    }

    /**
     * Trim whitespace from ends of a StringBuffer.
     */
    ///CLOVER:OFF
    public static StringBuffer trim(StringBuffer b) {
        // TODO update to handle surrogates
        int i;
        for (i=0; i<b.length() && Character.isWhitespace(b.charAt(i)); ++i) {}
        b.delete(0, i);
        for (i=b.length()-1; i>=0 && Character.isWhitespace(b.charAt(i)); --i) {}
        return b.delete(i+1, b.length());
    }
    ///CLOVER:ON

    static final char DIGITS[] = {
        '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
        'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H', 'I', 'J',
        'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T',
        'U', 'V', 'W', 'X', 'Y', 'Z'
    };

    /**
     * Append a number to the given StringBuffer in the radix 10
     * generating at least one digit.
     */
    ///CLOVER:OFF
    public static StringBuffer appendNumber(StringBuffer result, int n) {
        return appendNumber(result, n, 10, 1);
    }
    ///CLOVER:ON

    /**
     * Append the digits of a positive integer to the given
     * <code>StringBuffer</code> in the given radix. This is
     * done recursively since it is easiest to generate the low-
     * order digit first, but it must be appended last.
     *
     * @param result is the <code>StringBuffer</code> to append to
     * @param n is the positive integer
     * @param radix is the radix, from 2 to 36 inclusive
     * @param minDigits is the minimum number of digits to append.
     */
    private static void recursiveAppendNumber(StringBuffer result, int n,
                                                int radix, int minDigits)
    {
        int digit = n % radix;

        if (n >= radix || minDigits > 1) {
            recursiveAppendNumber(result, n / radix, radix, minDigits - 1);
        }

        result.append(DIGITS[digit]);
    }

    /**
     * Append a number to the given StringBuffer in the given radix.
     * Standard digits '0'-'9' are used and letters 'A'-'Z' for
     * radices 11 through 36.
     * @param result the digits of the number are appended here
     * @param n the number to be converted to digits; may be negative.
     * If negative, a '-' is prepended to the digits.
     * @param radix a radix from 2 to 36 inclusive.
     * @param minDigits the minimum number of digits, not including
     * any '-', to produce.  Values less than 2 have no effect.  One
     * digit is always emitted regardless of this parameter.
     * @return a reference to result
     */
    public static StringBuffer appendNumber(StringBuffer result, int n,
                                             int radix, int minDigits)
        throws IllegalArgumentException
    {
        if (radix < 2 || radix > 36) {
            throw new IllegalArgumentException("Illegal radix " + radix);
        }


        int abs = n;

        if (n < 0) {
            abs = -n;
            result.append("-");
        }

        recursiveAppendNumber(result, abs, radix, minDigits);

        return result;
    }

    /**
     * Parse an unsigned 31-bit integer at the given offset.  Use
     * UCharacter.digit() to parse individual characters into digits.
     * @param text the text to be parsed
     * @param pos INPUT-OUTPUT parameter.  On entry, pos[0] is the
     * offset within text at which to start parsing; it should point
     * to a valid digit.  On exit, pos[0] is the offset after the last
     * parsed character.  If the parse failed, it will be unchanged on
     * exit.  Must be >= 0 on entry.
     * @param radix the radix in which to parse; must be >= 2 and <=
     * 36.
     * @return a non-negative parsed number, or -1 upon parse failure.
     * Parse fails if there are no digits, that is, if pos[0] does not
     * point to a valid digit on entry, or if the number to be parsed
     * does not fit into a 31-bit unsigned integer.
     */
    public static int parseNumber(String text, int[] pos, int radix) {
        // assert(pos[0] >= 0);
        // assert(radix >= 2);
        // assert(radix <= 36);
        int n = 0;
        int p = pos[0];
        while (p < text.length()) {
            int ch = UTF16.charAt(text, p);
            int d = UCharacter.digit(ch, radix);
            if (d < 0) {
                break;
            }
            n = radix*n + d;
            // ASSUME that when a 32-bit integer overflows it becomes
            // negative.  E.g., 214748364 * 10 + 8 => negative value.
            if (n < 0) {
                return -1;
            }
            ++p;
        }
        if (p == pos[0]) {
            return -1;
        }
        pos[0] = p;
        return n;
    }

    /**
     * Return true if the character is NOT printable ASCII.  The tab,
     * newline and linefeed characters are considered unprintable.
     */
    public static boolean isUnprintable(int c) {
        return !(c >= 0x20 && c <= 0x7E);
    }

    /**
     * Escape unprintable characters using <backslash>uxxxx notation
     * for U+0000 to U+FFFF and <backslash>Uxxxxxxxx for U+10000 and
     * above.  If the character is printable ASCII, then do nothing
     * and return FALSE.  Otherwise, append the escaped notation and
     * return TRUE.
     */
    public static boolean escapeUnprintable(StringBuffer result, int c) {
        if (isUnprintable(c)) {
            result.append('\\');
            if ((c & ~0xFFFF) != 0) {
                result.append('U');
                result.append(DIGITS[0xF&(c>>28)]);
                result.append(DIGITS[0xF&(c>>24)]);
                result.append(DIGITS[0xF&(c>>20)]);
                result.append(DIGITS[0xF&(c>>16)]);
            } else {
                result.append('u');
            }
            result.append(DIGITS[0xF&(c>>12)]);
            result.append(DIGITS[0xF&(c>>8)]);
            result.append(DIGITS[0xF&(c>>4)]);
            result.append(DIGITS[0xF&c]);
            return true;
        }
        return false;
    }

    /**
     * Returns the index of the first character in a set, ignoring quoted text.
     * For example, in the string "abc'hide'h", the 'h' in "hide" will not be
     * found by a search for "h".  Unlike String.indexOf(), this method searches
     * not for a single character, but for any character of the string
     * <code>setOfChars</code>.
     * @param text text to be searched
     * @param start the beginning index, inclusive; <code>0 <= start
     * <= limit</code>.
     * @param limit the ending index, exclusive; <code>start <= limit
     * <= text.length()</code>.
     * @param setOfChars string with one or more distinct characters
     * @return Offset of the first character in <code>setOfChars</code>
     * found, or -1 if not found.
     * @see String#indexOf
     */
    public static int quotedIndexOf(String text, int start, int limit,
                                    String setOfChars) {
        for (int i=start; i<limit; ++i) {
            char c = text.charAt(i);
            if (c == BACKSLASH) {
                ++i;
            } else if (c == APOSTROPHE) {
                while (++i < limit
                       && text.charAt(i) != APOSTROPHE) {}
            } else if (setOfChars.indexOf(c) >= 0) {
                return i;
            }
        }
        return -1;
    }

    /**
    * Similar to StringBuffer.getChars, version 1.3.
    * Since JDK 1.2 implements StringBuffer.getChars differently, this method
    * is here to provide consistent results.
    * To be removed after JDK 1.2 ceased to be the reference platform.
    * @param src source string buffer
    * @param srcBegin offset to the start of the src to retrieve from
    * @param srcEnd offset to the end of the src to retrieve from
    * @param dst char array to store the retrieved chars
    * @param dstBegin offset to the start of the destination char array to
    *                 store the retrieved chars
    * @draft since ICU4J 2.0
    */
    public static void getChars(StringBuffer src, int srcBegin, int srcEnd,
                                char dst[], int dstBegin)
    {
        if (srcBegin == srcEnd) {
            return;
        }
        src.getChars(srcBegin, srcEnd, dst, dstBegin);
    }

    /**
     * Append a character to a rule that is being built up.  To flush
     * the quoteBuf to rule, make one final call with isLiteral == true.
     * If there is no final character, pass in (int)-1 as c.
     * @param rule the string to append the character to
     * @param c the character to append, or (int)-1 if none.
     * @param isLiteral if true, then the given character should not be
     * quoted or escaped.  Usually this means it is a syntactic element
     * such as > or $
     * @param escapeUnprintable if true, then unprintable characters
     * should be escaped using escapeUnprintable().  These escapes will
     * appear outside of quotes.
     * @param quoteBuf a buffer which is used to build up quoted
     * substrings.  The caller should initially supply an empty buffer,
     * and thereafter should not modify the buffer.  The buffer should be
     * cleared out by, at the end, calling this method with a literal
     * character (which may be -1).
     */
    public static void appendToRule(StringBuffer rule,
                                    int c,
                                    boolean isLiteral,
                                    boolean escapeUnprintable,
                                    StringBuffer quoteBuf) {
        // If we are escaping unprintables, then escape them outside
        // quotes.  \\u and \\U are not recognized within quotes.  The same
        // logic applies to literals, but literals are never escaped.
        if (isLiteral ||
            (escapeUnprintable && Utility.isUnprintable(c))) {
            if (quoteBuf.length() > 0) {
                // We prefer backslash APOSTROPHE to double APOSTROPHE
                // (more readable, less similar to ") so if there are
                // double APOSTROPHEs at the ends, we pull them outside
                // of the quote.

                // If the first thing in the quoteBuf is APOSTROPHE
                // (doubled) then pull it out.
                while (quoteBuf.length() >= 2 &&
                       quoteBuf.charAt(0) == APOSTROPHE &&
                       quoteBuf.charAt(1) == APOSTROPHE) {
                    rule.append(BACKSLASH).append(APOSTROPHE);
                    quoteBuf.delete(0, 2);
                }
                // If the last thing in the quoteBuf is APOSTROPHE
                // (doubled) then remove and count it and add it after.
                int trailingCount = 0;
                while (quoteBuf.length() >= 2 &&
                       quoteBuf.charAt(quoteBuf.length()-2) == APOSTROPHE &&
                       quoteBuf.charAt(quoteBuf.length()-1) == APOSTROPHE) {
                    quoteBuf.setLength(quoteBuf.length()-2);
                    ++trailingCount;
                }
                if (quoteBuf.length() > 0) {
                    rule.append(APOSTROPHE);
                    // jdk 1.3.1 does not have append(StringBuffer) yet
                    if(ICUDebug.isJDK14OrHigher){
                        rule.append(quoteBuf);
                    }else{
                        rule.append(quoteBuf.toString());
                    }
                    rule.append(APOSTROPHE);
                    quoteBuf.setLength(0);
                }
                while (trailingCount-- > 0) {
                    rule.append(BACKSLASH).append(APOSTROPHE);
                }
            }
            if (c != -1) {
                /* Since spaces are ignored during parsing, they are
                 * emitted only for readability.  We emit one here
                 * only if there isn't already one at the end of the
                 * rule.
                 */
                if (c == ' ') {
                    int len = rule.length();
                    if (len > 0 && rule.charAt(len-1) != ' ') {
                        rule.append(' ');
                    }
                } else if (!escapeUnprintable || !Utility.escapeUnprintable(rule, c)) {
                    UTF16.append(rule, c);
                }
            }
        }

        // Escape ' and '\' and don't begin a quote just for them
        else if (quoteBuf.length() == 0 &&
                 (c == APOSTROPHE || c == BACKSLASH)) {
            rule.append(BACKSLASH).append((char)c);
        }

        // Specials (printable ascii that isn't [0-9a-zA-Z]) and
        // whitespace need quoting.  Also append stuff to quotes if we are
        // building up a quoted substring already.
        else if (quoteBuf.length() > 0 ||
                 (c >= 0x0021 && c <= 0x007E &&
                  !((c >= 0x0030/*'0'*/ && c <= 0x0039/*'9'*/) ||
                    (c >= 0x0041/*'A'*/ && c <= 0x005A/*'Z'*/) ||
                    (c >= 0x0061/*'a'*/ && c <= 0x007A/*'z'*/))) ||
                 UCharacterProperty.isRuleWhiteSpace(c)) {
            UTF16.append(quoteBuf, c);
            // Double ' within a quote
            if (c == APOSTROPHE) {
                quoteBuf.append((char)c);
            }
        }

        // Otherwise just append
        else {
            UTF16.append(rule, c);
        }
    }

    /**
     * Append the given string to the rule.  Calls the single-character
     * version of appendToRule for each character.
     */
    public static void appendToRule(StringBuffer rule,
                                    String text,
                                    boolean isLiteral,
                                    boolean escapeUnprintable,
                                    StringBuffer quoteBuf) {
        for (int i=0; i<text.length(); ++i) {
            // Okay to process in 16-bit code units here
            appendToRule(rule, text.charAt(i), isLiteral, escapeUnprintable, quoteBuf);
        }
    }

    /**
     * Given a matcher reference, which may be null, append its
     * pattern as a literal to the given rule.
     */
    public static void appendToRule(StringBuffer rule,
                                    UnicodeMatcher matcher,
                                    boolean escapeUnprintable,
                                    StringBuffer quoteBuf) {
        if (matcher != null) {
            appendToRule(rule, matcher.toPattern(escapeUnprintable),
                         true, escapeUnprintable, quoteBuf);
        }
    }

    /**
     * Compares 2 unsigned integers
     * @param source 32 bit unsigned integer
     * @param target 32 bit unsigned integer
     * @return 0 if equals, 1 if source is greater than target and -1
     *         otherwise
     */
    public static final int compareUnsigned(int source, int target)
    {
        source += MAGIC_UNSIGNED;
        target += MAGIC_UNSIGNED;
        if (source < target) {
            return -1;
        } 
        else if (source > target) {
            return 1;
        }
        return 0;
    }

    /**
     * Find the highest bit in a positive integer. This is done
     * by doing a binary search through the bits.
     *
     * @param n is the integer
     *
     * @return the bit number of the highest bit, with 0 being
     * the low order bit, or -1 if <code>n</code> is not positive
     */
    public static final byte highBit(int n)
    {
        if (n <= 0) {
            return -1;
        }

        byte bit = 0;

        if (n >= 1 << 16) {
            n >>= 16;
            bit += 16;
        }

        if (n >= 1 << 8) {
            n >>= 8;
            bit += 8;
        }

        if (n >= 1 << 4) {
            n >>= 4;
            bit += 4;
        }

        if (n >= 1 << 2) {
            n >>= 2;
            bit += 2;
        }

        if (n >= 1 << 1) {
            n >>= 1;
            bit += 1;
        }

        return bit;
    }
}
