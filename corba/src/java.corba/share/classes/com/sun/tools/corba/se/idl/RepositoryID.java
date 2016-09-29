/*
 * Copyright (c) 1999, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */
/*
 * COMPONENT_NAME: idl.parser
 *
 * ORIGINS: 27
 *
 * Licensed Materials - Property of IBM
 * 5639-D57 (C) COPYRIGHT International Business Machines Corp. 1997, 1999
 * RMI-IIOP v1.0
 *
 */

package com.sun.tools.corba.se.idl;

// NOTES:
// -D57110<daz> Add method to verify format (CORBA 2.3).

public class RepositoryID
{
  public RepositoryID ()
  {
    _id = "";
  } // ctor

  public RepositoryID (String id)
  {
    _id = id;
  } // ctor

  public String ID ()
  {
    return _id;
  } // ID

  public Object clone ()
  {
    return new RepositoryID (_id);
  } // clone

  public String toString ()
  {
    return ID ();
  } // toString

  /**
   * Determine is a supplied string meets the minimal format requirement
   * for a Repository ID.
   * @return true if supplied string has form {@code '<format>:<string>'}, where
   * {@code <format>} is any non-empty string not containing ':'.
   **/
  public static boolean hasValidForm (String string)
  {
    return string != null && string.indexOf (':') > 0;
  } // hasValidForm

  private String _id;
} // class RepositoryID
