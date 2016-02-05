package com.example.wenzhao.helpinghand.BLEHandler;

import android.content.res.XmlResourceParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

public class GattInfo {
  // Bluetooth SIG identifiers
  public static final UUID CLIENT_CHARACTERISTIC_CONFIG = UUID.fromString("00002902-0000-1000-8000-00805f9b34fb");

  private static Map<String, String> mNameMap = new HashMap<String, String>();
  private static Map<String, String> mDescrMap = new HashMap<String, String>();
  private static Map<String, String> mIconMap = new HashMap<String, String>();
  

  public GattInfo(XmlResourceParser xpp) {
    // XML data base
    try {
      readUuidData(xpp);
    } catch (XmlPullParserException e) {
      e.printStackTrace();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public static String uuidToName(UUID uuid) {
    String str = toShortUuidStr(uuid);
    return uuidToName(str.toUpperCase(Locale.ENGLISH));
  }
  
  public static String uuidToIcon(UUID uuid) {
	    String str = toShortUuidStr(uuid);
	    return uuidToIcon(str.toUpperCase(Locale.ENGLISH));
  }

  static private String toShortUuidStr(UUID u) {
    return u.toString().substring(4, 8);
  }

  private static String uuidToName(String uuidStr16) {
    return mNameMap.get(uuidStr16);
  }
  
  private static String uuidToIcon(String uuidStr16) {
	  return mIconMap.get(uuidStr16);
  }

  //
  // XML loader
  //
  private void readUuidData(XmlResourceParser xpp) throws XmlPullParserException, IOException {
    xpp.next();
    String tagName = null;
    String uuid = null;
    String descr = null;
    String icon = null;
    int eventType = xpp.getEventType(); 

    while (eventType != XmlPullParser.END_DOCUMENT) {
      if (eventType == XmlPullParser.START_DOCUMENT) {
        // do nothing
      } else if (eventType == XmlPullParser.START_TAG) {
        tagName = xpp.getName();
        uuid = xpp.getAttributeValue(null, "uuid");
        descr = xpp.getAttributeValue(null, "descr");
        icon = xpp.getAttributeValue(null,"icon");
      } else if (eventType == XmlPullParser.END_TAG) {
        // do nothing
      } else if (eventType == XmlPullParser.TEXT) {
        if (tagName.equalsIgnoreCase("item")) {
          if (!uuid.isEmpty()) {
            uuid = uuid.replace("0x", "");
            mNameMap.put(uuid, xpp.getText());
            mDescrMap.put(uuid, descr);
            mIconMap.put(uuid, icon);
          }
        }
      }
      eventType = xpp.next();
    }
  }
}
