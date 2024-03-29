/**
*******************************************************************************
* Copyright (C) 1996-2001, International Business Machines Corporation and    *
* others. All Rights Reserved.                                                *
*******************************************************************************
*
* $Source: 
*     /usr/cvs/icu4j/icu4j/src/com/ibm/icu/text/UCharacterNameChoiceEnum.java $ 
* $Date: 2004/01/17 21:28:03 $ 
* $Revision: 1.1 $
*
*******************************************************************************
*/

package com.ibm.icu.impl;

/**
* Internal class containing selector constants for the unicode character names.
* Constants representing the "modern" name of a Unicode character or the name 
* that was defined in Unicode version 1.0, before the Unicode standard 
* merged with ISO-10646.
* Arguments for <a href=UCharacterName.html>UCharacterName</a>
* @author Syn Wee Quek
* @since oct0600
*/

public interface UCharacterNameChoice
{
  // public variables =============================================
  
  static final int UNICODE_CHAR_NAME = 0;
  static final int UNICODE_10_CHAR_NAME = 1;
  static final int EXTENDED_CHAR_NAME = 2;
  static final int CHAR_NAME_CHOICE_COUNT = 3;
  static final int ISO_COMMENT_ = CHAR_NAME_CHOICE_COUNT;
}
