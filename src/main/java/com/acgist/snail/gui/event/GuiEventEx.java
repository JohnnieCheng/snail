package com.acgist.snail.gui.event;

/**
 * <p>GUI事件扩展</p>
 * <p>处理变长参数</p>
 * 
 * @author acgist
 * @since 1.1.0
 */
public abstract class GuiEventEx extends GuiEvent {

	protected GuiEventEx(Type type, String name) {
		super(type, name);
	}
	
	@Override
	protected void executeNative(Object ... args) {
		executeEx(true, args);
	}

	@Override
	protected void executeExtend(Object ... args) {
		executeEx(false, args);
	}
	
	/**
	 * <p>处理变长参数</p>
	 * 
	 * @param gui 是否是本地GUI事件：{@code ture}-本地GUI；{@code false}-扩展GUI；
	 * @param args 参数
	 */
	protected abstract void executeEx(boolean gui, Object ... args);

}