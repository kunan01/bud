package com.bud.code.common.exception;

public class SysException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public SysException(String message){
		super(message);
	}
	
	public SysException(Throwable cause)
	{
		super(cause);
	}
	
	public SysException(String message, Throwable cause)
	{
		super(message,cause);
	}
}
