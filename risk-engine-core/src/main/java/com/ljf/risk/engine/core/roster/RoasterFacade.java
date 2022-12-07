package com.ljf.risk.engine.core.roster;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.ljf.risk.engine.api.Response;
import com.ljf.risk.engine.api.roaster.RoasterAdmin;
import com.ljf.risk.engine.api.roaster.RoasterListModel;
import com.ljf.risk.engine.api.roaster.RoasterModel;
import com.ljf.risk.engine.entity.Roaster;
import com.ljf.risk.engine.entity.RoasterList;
import com.ljf.risk.engine.service.RoasterListService;
import com.ljf.risk.engine.service.RoasterService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController()
@Slf4j
public class RoasterFacade implements RoasterAdmin {
    private RoasterService roasterService;
    private RoasterListService roasterListService;
    private RoasterHolder roasterHolder;

    public RoasterFacade(RoasterService roasterService, RoasterListService roasterListService, RoasterHolder roasterHolder) {
        this.roasterService = roasterService;
        this.roasterListService = roasterListService;
        this.roasterHolder = roasterHolder;
    }

    @Override
    @PostMapping("/roaster/{id}/list")
    public Response addRoasterList(@PathVariable Long id, @RequestBody List<RoasterListModel> roasterListModel) {
        Roaster roaster = roasterService.getById(id);
        if (roaster == null) {
            return Response.failure("000001", "名单不存在");
        }
        List<RoasterList> roasters = new ArrayList<>(roasterListModel.size());
        roasterListModel.forEach(model -> {
            RoasterList roasterList = new RoasterList();
            BeanUtils.copyProperties(model, roasterList);
            roasterList.setRoasterId(id);
            roasters.add(roasterList);
        });

        try {
            if (roasterListService.saveBatch(roasters)) {
                //添加名单 刷新缓存
                roasterHolder.remove(roaster.getCode(), roasters.stream().map(RoasterList::getValue).collect(Collectors.toList()));
            }
        } catch (Exception ex) {
            log.error("addRoasterList failed: ", ex);
            if (ex instanceof DuplicateKeyException) {
                return Response.failure("00001", "重复数据");
            }
            return Response.failure("00001", ex.getMessage());
        }
        return Response.success();
    }

    @Override
    @DeleteMapping("/roaster/{id}/list")
    public Response deleteLists(@PathVariable Long id, @RequestBody List<Long> roasterListModel) {
        Roaster roaster = roasterService.getById(id);
        if (roaster == null) {
            return Response.failure("000001", "名单不存在");
        }
        List<RoasterList> roasterLists = roasterListService.listByIds(roasterListModel);
        if (CollectionUtils.isEmpty(roasterLists)) {
            return Response.success();
        }
        roasterListService.removeBatchByIds(roasterListModel);
        roasterHolder.remove(roaster.getCode(), roasterLists.stream().map(RoasterList::getValue).collect(Collectors.toList()));
        return Response.success();
    }

    @Override
    @PostMapping("/roaster")
    public Response createOrUpdate(@RequestBody RoasterModel roasterModel) {
        //code不能更新！！！！
        Roaster roaster = new Roaster();
        BeanUtils.copyProperties(roasterModel, roaster);
        roaster.setCreateTime(new Date());
        try {
            roasterService.saveOrUpdate(roaster);
        } catch (Exception ex) {
            return Response.failure("00001", ex.getMessage());
        }

        return Response.success();
    }

    @Override
    @DeleteMapping("/roaster")
    public Response delete(@RequestBody List<Long> roasters) {
        //名单删除实际上需要校验名单在规则中的使用情况！！！
        List<Roaster> r = roasterService.listByIds(roasters);
        if (CollectionUtils.isEmpty(r)) {
            return Response.success();
        }
        List<RoasterList> roasterLists = roasterListService.list(new LambdaQueryWrapper<>(RoasterList.class).in(RoasterList::getRoasterId, roasters));
        Map<Long, List<Roaster>> mp = r.stream().collect(Collectors.groupingBy(Roaster::getId));
        if (!CollectionUtils.isEmpty(roasterLists)) {
            roasterListService.removeByIds(roasterLists.stream().map(RoasterList::getId).collect(Collectors.toList()));
        }
        roasterService.removeBatchByIds(roasters);
        roasterLists
                .stream()
                .collect(Collectors.groupingBy(RoasterList::getRoasterId))
                .forEach((k, v) -> roasterHolder.remove(mp.get(k).get(0).getCode(), roasterLists
                        .stream()
                        .map(RoasterList::getValue)
                        .collect(Collectors.toList())));
        return Response.success();
    }

    @Override
    @PostMapping("/roaster/{roasterId}/list/update/")
    public Response update(@RequestBody RoasterListModel roasterListModel, @PathVariable Long roasterId) {
        Roaster roaster = roasterService.getById(roasterId);
        if (roaster == null) {
            return Response.failure("000001", "名单不存在");
        }
        RoasterList roasterList = roasterListService.getById(roasterListModel.getId());
        if (roasterList == null) {
            return Response.failure("000001", "名单不存在");
        }
        roasterList.setUpdateTime(new Date());
        roasterList.setExpiredAt(roasterListModel.getExpiredAt());
        roasterList.setValue(roasterListModel.getValue());
        roasterList.setRemark(roasterListModel.getRemark());
        roasterList.setUpdateUser(roasterListModel.getUpdateUser());
        if (roasterListService.saveOrUpdate(roasterList)) {
            roasterHolder.remove(roaster.getCode(), roasterList.getValue());
            return Response.success();
        }
        return Response.success();
    }
}
