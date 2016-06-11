package com.garbled.util.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.text.TextUtils;

import java.lang.reflect.Method;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * 网络相关的工具类
 * 
 * 判断网络是否可用，wifi，数据上网开关等
 * 
 * @author IT蓝豹
 *
 */
public class NetworkUtils {
	public static final int NETWORK_TYPE_UNKNOWN = 0;
    public static final int NETWORK_TYPE_WIFI = 2;
    public static final int NETWORK_TYPE_CMWAP = 3;
    public static final int NETWORK_TYPE_CMNET = 4;
    public static final int NETWORK_TYPE_CTNET = 5;
    public static final int NETWORK_TYPE_CTWAP = 6;
    public static final int NETWORK_TYPE_3GWAP = 7;
    public static final int NETWORK_TYPE_3GNET = 8;
    public static final int NETWORK_TYPE_UNIWAP = 9;
    public static final int NETWORK_TYPE_UNINET = 10;
    
	private Context context;
	private ConnectivityManager connManager;


	public NetworkUtils(Context context) {
		this.context = context;
		connManager = (ConnectivityManager) this.context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
	}
	
	/**
	 * 网络是否可用
	 * 
	 * @return
	 */
	public boolean isAvailable() {
        final NetworkInfo[] info = connManager.getAllNetworkInfo();
        if (info != null)
        {
            final int size = info.length;
            for (int i = 0; i < size; i++)
            {
                if (info[i].getState() == NetworkInfo.State.CONNECTED)
                {
                    return true;
                }
            }
        }
        return false;
    }

	/**
	 * 网络是否连接可用
	 * 
	 * @return 
	 */
	public boolean isNetworkConnected() {

	    if (connManager == null) {
	        connManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
	    }
	    
	    if (connManager != null) {
	        final NetworkInfo networkinfo = connManager.getActiveNetworkInfo();
	        
	        if (networkinfo != null) {
	            return networkinfo.isConnected();
	        }
	    } else {
	        return true;
	    }

		return false;
	}
	
	public static boolean isAvailable(Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo info = connectivityManager.getActiveNetworkInfo();
		if (info != null && info.isAvailable()) {
			return true;
		}
		return false;
	}

	/**
	 * wifi是否连接可用
	 * 
	 * @return 
	 */
	public boolean isWifiConnected() {

		try {
			final NetworkInfo mWifi = connManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);

			if (mWifi != null) {
				return mWifi.isConnected();
			}
		} catch (Exception e) {
			e.printStackTrace();
			return true;
		}

		return false;
	}

	/**
	 * 当wifi不能访问网络时，mobile才会起作用
	 * 
	 * @return GPRS是否连接可用
	 */
	public boolean isMobileConnected() {

		final NetworkInfo mMobile = connManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

		if (mMobile != null) {
			return mMobile.isConnected();
		}
		return false;
	}

	/**
	 * GPRS网络开关 反射ConnectivityManager中hide的方法setMobileDataEnabled 可以开启和关闭GPRS网络
	 * 
	 * @param isEnable
	 * @throws Exception
	 */
	public void toggleGprs(boolean isEnable) throws Exception {
		final Class<?> cmClass = connManager.getClass();
		final Class<?>[] argClasses = new Class[1];
		argClasses[0] = boolean.class;

		// 反射ConnectivityManager中hide的方法setMobileDataEnabled，可以开启和关闭GPRS网络
		final Method method = cmClass.getMethod("setMobileDataEnabled", argClasses);
		method.invoke(connManager, isEnable);
	}

	/**
	 * WIFI网络开关
	 * 
	 * @param enabled
	 * @return 设置是否success
	 */
	public boolean toggleWiFi(boolean enabled) {
		final WifiManager wm = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
		return wm.setWifiEnabled(enabled);

	}
	
	/**
	 * 获取网络类型
	 * @param context
	 * @return
	 */
	public static String getNetworkType(Context context) {
		final ConnectivityManager connectivityManager = (ConnectivityManager) context
				.getSystemService(Context.CONNECTIVITY_SERVICE);
		final NetworkInfo mobNetInfoActivity = connectivityManager
				.getActiveNetworkInfo();
		
		if (mobNetInfoActivity == null) {
			return "";
		}
		
		String netTypeMode = "";
		
		final int netType = mobNetInfoActivity.getType();
		if (netType == ConnectivityManager.TYPE_WIFI) {
			// wifi上网
			netTypeMode = "wifi";
		} else if (netType == ConnectivityManager.TYPE_MOBILE) {
			// 接入点上网
			final String netMode = mobNetInfoActivity.getExtraInfo();
			if (!TextUtils.isEmpty(netMode)) {
				return netMode;
			}
			
			{/* 由于4.0以上版本手机禁用了android.permission.WRITE_APN_SETTINGS权限，所以以下代码不可用
				// 获取当前设置的APN
				private static final Uri CURRENT_APN_URI = Uri.parse("content://telephony/carriers/preferapn");
				// 如果netMode没有获取到，则从数据库中查找当前使用的接入点
				final Cursor c = context.getContentResolver().query(
				        CURRENT_APN_URI, null, null, null, null);
				if (c != null) {
					c.moveToFirst();
					final String apn = c.getString(c.getColumnIndex("apn"));
					if (!TextUtils.isEmpty(apn)) {
						netTypeMode = apn;
					}
					c.close();
				}
			*/}
			
		}
		
		return netTypeMode;
	}
	
	/**
	 * 获取网络类型id
	 * @param context
	 * 
	 * @return 0:无网络,2:wifi、3:cmwap、4:cmnet、5:ctnet、6:ctwap、7:3gwap、8:3gnet、9:uniwap、10:uninet
	 */
//	public static int getNetworkTypeId(Context context)
//	{
//		final String type = getNetworkType(context);
//		if ("wifi".equalsIgnoreCase(type)) {
//			return NETWORK_TYPE_WIFI;
//		} else if ("cmwap".equalsIgnoreCase(type)) {
//			return NETWORK_TYPE_CMWAP;
//		} else if ("cmnet".equalsIgnoreCase(type)) {
//			return NETWORK_TYPE_CMNET;
//		} else if ("ctnet".equalsIgnoreCase(type)) {
//			return NETWORK_TYPE_CTNET;
//		} else if ("ctwap".equalsIgnoreCase(type)) {
//			return NETWORK_TYPE_CTWAP;
//		} else if ("3gwap".equalsIgnoreCase(type)) {
//			return NETWORK_TYPE_3GWAP;
//		} else if ("3gnet".equalsIgnoreCase(type)) {
//			return NETWORK_TYPE_3GNET;
//		} else if ("uniwap".equalsIgnoreCase(type)) {
//			return NETWORK_TYPE_UNIWAP;
//		} else if ("uninet".equalsIgnoreCase(type)) {
//			return NETWORK_TYPE_UNINET;
//		}
//	    return NETWORK_TYPE_UNKNOWN;
//	}
	
	/**
     * 获取网络类型id
     * @param context
     * 
     * @return 0:无网络,2:wifi,11:2G网络,12:3G网络,13:4G网络
     */
	public static int getNetworkTypeId(Context context)
	{
//	    String strNetworkType = "";
	    int strNetworkType = 0;
	    
	    NetworkInfo networkInfo = ((ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
	    if (networkInfo != null && networkInfo.isConnected())
	    {
	        if (networkInfo.getType() == ConnectivityManager.TYPE_WIFI)
	        {
//	            strNetworkType = "WIFI";
	            strNetworkType = 2;
	        }
	        else if (networkInfo.getType() == ConnectivityManager.TYPE_MOBILE)
	        {
	            String _strSubTypeName = networkInfo.getSubtypeName();
	            
	            if (LogUtils.DEBUG) {
                    LogUtils.e("cocos2d-x", "Network getSubtypeName : " + _strSubTypeName);
	            }
	            
	            // TD-SCDMA   networkType is 17
	            int networkType = networkInfo.getSubtype();
	            switch (networkType) {
	                case TelephonyManager.NETWORK_TYPE_GPRS:
	                case TelephonyManager.NETWORK_TYPE_EDGE:
	                case TelephonyManager.NETWORK_TYPE_CDMA:
	                case TelephonyManager.NETWORK_TYPE_1xRTT:
	                case TelephonyManager.NETWORK_TYPE_IDEN: //api<8 : replace by 11
//	                    strNetworkType = "2G";
	                    strNetworkType = 11;
	                    break;
	                case TelephonyManager.NETWORK_TYPE_UMTS:
	                case TelephonyManager.NETWORK_TYPE_EVDO_0:
	                case TelephonyManager.NETWORK_TYPE_EVDO_A:
	                case TelephonyManager.NETWORK_TYPE_HSDPA:
	                case TelephonyManager.NETWORK_TYPE_HSUPA:
	                case TelephonyManager.NETWORK_TYPE_HSPA:
	                case TelephonyManager.NETWORK_TYPE_EVDO_B: //api<9 : replace by 14
	                case TelephonyManager.NETWORK_TYPE_EHRPD:  //api<11 : replace by 12
	                case TelephonyManager.NETWORK_TYPE_HSPAP:  //api<13 : replace by 15
//	                    strNetworkType = "3G";
	                    strNetworkType = 12;
	                    break;
	                case TelephonyManager.NETWORK_TYPE_LTE:    //api<11 : replace by 13
//	                    strNetworkType = "4G";
	                    strNetworkType = 13;
	                    break;
	                default:
	                    // http://baike.baidu.com/item/TD-SCDMA 中国移动 联通 电信 三种3G制式
	                    if (_strSubTypeName.equalsIgnoreCase("TD-SCDMA") || _strSubTypeName.equalsIgnoreCase("WCDMA") || _strSubTypeName.equalsIgnoreCase("CDMA2000")) 
	                    {
//	                        strNetworkType = "3G";
	                        strNetworkType = 12;
	                    }
	                    else
	                    {
//	                        strNetworkType = _strSubTypeName;
	                        strNetworkType = 0;
	                    }
	                    
	                    break;
	             }
	             
	            if (LogUtils.DEBUG) {
	                LogUtils.e("cocos2d-x", "Network getSubtype : " + Integer.valueOf(networkType).toString());
	            }
	        }
	    }
	    
	    if (LogUtils.DEBUG) {
            LogUtils.e("cocos2d-x", "Network Type : " + strNetworkType);
	    }
	    
	    return strNetworkType;
	}
	
	/**
	 * 获取ip地址
	 * @return
	 */
	public static String getLocalIpAddress() {
		try {

			for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
				NetworkInterface intf = en.nextElement();
				for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements();) {
					InetAddress inetAddress = enumIpAddr.nextElement();
					if (!inetAddress.isLoopbackAddress()
							&& inetAddress instanceof Inet4Address) {
						return inetAddress.getHostAddress().toString();
					}
				}
			}

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return "";
	}
	

	/**
	 * 判断是否使用WAP连接
	 */
	public static boolean isUsingWap(Context context) {
		boolean result = false;
		if (isNetworkAvailable(context) && getNetworkConnectType(context) == NetworkConnectType.MOBILE) {
			final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
			final NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
			if (networkInfo != null) {
				final String netExtraInfo = networkInfo.getExtraInfo();
				if (LogUtils.DEBUG) {
					LogUtils.e("getFromHttp netExtraInfo " + netExtraInfo);
				}
				if (!TextUtils.isEmpty(netExtraInfo) && netExtraInfo.toLowerCase().contains("wap")) {
					result = true;
				}
			}
		}
		return result;
	}
	
	/**
	 * 判断网络是否可用
	 */
	public static boolean isNetworkAvailable(Context context) {
		boolean result = false;
		ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			NetworkInfo[] networkInfos = connectivityManager.getAllNetworkInfo();
			if (networkInfos != null) {
				final int length = networkInfos.length;
				for (int i = 0; i < length; i++) {
					if (networkInfos[i].getState() == NetworkInfo.State.CONNECTED) {
						return true;
					}
				}
			}
		}
		return result;
	}
	
	/**
	 * 获得当前网络连接类型
	 */
	public static NetworkConnectType getNetworkConnectType(Context context) {
		NetworkConnectType networkConnectType = null;
		final ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
		if (connectivityManager != null) {
			// mobile
			final NetworkInfo mobile = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);
			// wifi
			final NetworkInfo wifi = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
			if (mobile != null && (mobile.getState() == NetworkInfo.State.CONNECTED || mobile.getState() == NetworkInfo.State.CONNECTING)) {
				// mobile
				networkConnectType = NetworkConnectType.MOBILE;
			} else if (wifi != null && (wifi.getState() == NetworkInfo.State.CONNECTED || wifi.getState() == NetworkInfo.State.CONNECTING)) {
				// wifi
				networkConnectType = NetworkConnectType.WIFI;
			}
		}
		return networkConnectType;
	}
	
	/**
	 * 网络连接枚举类型
	 */
	public enum NetworkConnectType {
		MOBILE, WIFI
	}
}
