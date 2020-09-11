/**
 * @TODO
 * Author 苟俊 jndx_taibai@163.com
 * Date  @2013-10-25
 * Copyright 2013 www.daoxila.com. All rights reserved.
 */
package cn.sancell.xingqiu.util.observer;

/**
 * @author goujun
 * 
 */
public interface ObserverChanger {

	public void registerObserver(OnObserver o);

	public void removeObserver(OnObserver o);

	public void notifyObserver(Object obj);

}
