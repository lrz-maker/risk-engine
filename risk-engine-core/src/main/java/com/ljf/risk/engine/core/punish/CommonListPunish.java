package com.ljf.risk.engine.core.punish;

import com.ljf.risk.engine.api.Response;
import com.ljf.risk.engine.api.roaster.RoasterListModel;
import com.ljf.risk.engine.core.constants.CurrentContext;
import com.ljf.risk.engine.core.roster.RoasterFacade;
import com.ljf.risk.engine.entity.Punish;
import com.ljf.risk.engine.utils.TimeUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author lijinfeng
 */
@Slf4j
@Component("COMMON_LIST_PUNISH")
public class CommonListPunish extends AbstractPunishCost<RoasterListModel> {

    @Autowired
    private RoasterFacade roasterFacade;

    @Override
    protected void doService(CurrentContext.Context context, Punish punish) {
        Map<Long, List<RoasterListModel>> collect = buildModel(context, punish).stream().collect(Collectors.groupingBy(RoasterListModel::getRoasterId));
        for (Map.Entry<Long, List<RoasterListModel>> roasterListEntry : collect.entrySet()) {
            Response response = roasterFacade.addRoasterList(roasterListEntry.getKey(), roasterListEntry.getValue());
            log.info("{} success: {} code: {}, msg: {}", this.getClass().getSimpleName(), response.isSuccess(), response.getCode(), response.getMsg());
        }
    }

    public List<RoasterListModel> buildModel(CurrentContext.Context context, Punish punish) {
        List<RoasterListModel> temp = new ArrayList<>();
        for (String param : punish.getPunishField()) {
            String value = MapUtils.getString(context.getEngineCheckReq().getEventDetails(), param);
            if (StringUtils.isBlank(value)) {
                continue;
            }
            String punishPeriod = punish.getPunishPeriod();
            Date date = TimeUtils.timeAmountChangeDate(punishPeriod);
            RoasterListModel roasterList = new RoasterListModel();
            roasterList.setRoasterId(punish.getRoasterId());
            roasterList.setExpiredAt(date);
            roasterList.setRemark("engine");
            roasterList.setCreateTime(new Date());
            roasterList.setUpdateTime(new Date());
            roasterList.setUpdateUser("engine");
            roasterList.setValue(value);
            temp.add(roasterList);
        }
        return temp;
    }

}
