/* ----------------------------------------------------------------------------
 * This file was automatically generated by SWIG (http://www.swig.org).
 * Version 3.0.12
 *
 * Do not make changes to this file unless you know what you are doing--modify
 * the SWIG interface file instead.
 * ----------------------------------------------------------------------------- */

package chilkatsoft;

public class CkHashtable {
  private transient long swigCPtr;
  protected transient boolean swigCMemOwn;

  protected CkHashtable(long cPtr, boolean cMemoryOwn) {
    swigCMemOwn = cMemoryOwn;
    swigCPtr = cPtr;
  }

  protected static long getCPtr(CkHashtable obj) {
    return (obj == null) ? 0 : obj.swigCPtr;
  }

  protected void finalize() {
    delete();
  }

  public synchronized void delete() {
    if (swigCPtr != 0) {
      if (swigCMemOwn) {
        swigCMemOwn = false;
        chilkatJNI.delete_CkHashtable(swigCPtr);
      }
      swigCPtr = 0;
    }
  }

  public CkHashtable() {
    this(chilkatJNI.new_CkHashtable(), true);
  }

  public boolean get_LastMethodSuccess() {
    return chilkatJNI.CkHashtable_get_LastMethodSuccess(swigCPtr, this);
  }

  public void put_LastMethodSuccess(boolean newVal) {
    chilkatJNI.CkHashtable_put_LastMethodSuccess(swigCPtr, this, newVal);
  }

  public boolean AddFromXmlSb(CkStringBuilder sbXml) {
    return chilkatJNI.CkHashtable_AddFromXmlSb(swigCPtr, this, CkStringBuilder.getCPtr(sbXml), sbXml);
  }

  public boolean AddInt(String key, int value) {
    return chilkatJNI.CkHashtable_AddInt(swigCPtr, this, key, value);
  }

  public boolean AddQueryParams(String queryParams) {
    return chilkatJNI.CkHashtable_AddQueryParams(swigCPtr, this, queryParams);
  }

  public boolean AddStr(String key, String value) {
    return chilkatJNI.CkHashtable_AddStr(swigCPtr, this, key, value);
  }

  public void Clear() {
    chilkatJNI.CkHashtable_Clear(swigCPtr, this);
  }

  public boolean ClearWithNewCapacity(int capacity) {
    return chilkatJNI.CkHashtable_ClearWithNewCapacity(swigCPtr, this, capacity);
  }

  public boolean Contains(String key) {
    return chilkatJNI.CkHashtable_Contains(swigCPtr, this, key);
  }

  public boolean ContainsIntKey(int key) {
    return chilkatJNI.CkHashtable_ContainsIntKey(swigCPtr, this, key);
  }

  public boolean GetKeys(CkStringTable strTable) {
    return chilkatJNI.CkHashtable_GetKeys(swigCPtr, this, CkStringTable.getCPtr(strTable), strTable);
  }

  public int LookupInt(String key) {
    return chilkatJNI.CkHashtable_LookupInt(swigCPtr, this, key);
  }

  public boolean LookupStr(String key, CkString outStr) {
    return chilkatJNI.CkHashtable_LookupStr(swigCPtr, this, key, CkString.getCPtr(outStr), outStr);
  }

  public String lookupStr(String key) {
    return chilkatJNI.CkHashtable_lookupStr(swigCPtr, this, key);
  }

  public boolean Remove(String key) {
    return chilkatJNI.CkHashtable_Remove(swigCPtr, this, key);
  }

  public boolean ToXmlSb(CkStringBuilder sbXml) {
    return chilkatJNI.CkHashtable_ToXmlSb(swigCPtr, this, CkStringBuilder.getCPtr(sbXml), sbXml);
  }

}
