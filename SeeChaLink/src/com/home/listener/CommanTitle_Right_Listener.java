package com.home.listener;

/**
 * 建立一个接口，用于监听公共标题的按下
 * 
 * @author Catherine
 * 
 * */
public interface CommanTitle_Right_Listener {
	public void DotRight(boolean isDot);

	public void DotLeft(boolean isDot);

	public void DotRightEdit(boolean isEdit);

	public void DotRightFinish(boolean isFinish);
}
