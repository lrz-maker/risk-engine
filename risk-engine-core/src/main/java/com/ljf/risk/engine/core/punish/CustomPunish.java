package com.ljf.risk.engine.core.punish;

import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.entity.Punish;

/**
 * @author lijinfeng
 */
public interface CustomPunish {

	void punish(CurrentContext.Context context, Punish punish);

}
