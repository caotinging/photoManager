package com.maozhen.sso.common.utils;

import java.util.Collection;

/**
 * @author caoting
 * @date 2018年10月18日
 */
public class CommomUtils {

	public static <E> Boolean listIsEmpty(Collection<E> coll) {
		Boolean flag = true;
		if ( null != coll && coll.size() !=0 ) {
			flag = false;
		}
		return flag;
	}
	
	public static <E> Boolean listIsNotEmpty(Collection<E> coll) {
		Boolean flag = true;
		if ( null == coll || coll.size() ==0 ) {
			flag = false;
		}
		return flag;
	}
}
