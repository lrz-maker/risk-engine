package com.ljf.risk.engine.core.fuction;

import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.entity.Function;
import com.ljf.risk.engine.entity.FunctionExtend;

/**
 * @author lijinfeng
 */
public interface CustomFunction {
	
	/**
	 * 函数执行
	 * @param context
	 * @param functionExtend
	 * @return
	 */
	Object execute(CurrentContext.Context context, FunctionExtend functionExtend);

	Boolean precondition(FunctionExtend functionExtend, CurrentContext.Context context);

	Function getModel();

}
