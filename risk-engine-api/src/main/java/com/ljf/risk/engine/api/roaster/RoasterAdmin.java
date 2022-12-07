package com.ljf.risk.engine.api.roaster;

import com.ljf.risk.engine.api.Response;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;

/**
 * 名单管理接口
 */
@FeignClient(contextId = "engine.roaster", value = "risk-engine", path = "/roaster")
public interface RoasterAdmin {
    /**
     * 添加名单：注意同一个value在同一个名单中只能出现一次
     */
    @PostMapping("/{id}/list")
    Response addRoasterList(@PathVariable("id") Long roasterId, @RequestBody List<RoasterListModel> roasterListModel);

    /**
     * 名单删除
     */
    @DeleteMapping("/{id}/list")
    Response deleteLists(@PathVariable("id") Long roasterId, @RequestBody List<Long> roasterListModel);

    /**
     * 创建或更新名单，名单code需要唯一。否则报错
     */
    @PostMapping()
    Response createOrUpdate(@RequestBody RoasterModel roasterModel);

    @DeleteMapping()
    Response delete(@RequestBody List<Long> roasters);

    @PostMapping("/{roasterId}/list/update/")
    Response update(@RequestBody RoasterListModel roasterListModel, @PathVariable("roasterId") Long roasterId);
}
