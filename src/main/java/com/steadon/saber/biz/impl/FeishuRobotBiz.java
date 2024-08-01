package com.steadon.saber.biz.impl;

import com.steadon.saber.biz.IFeishuRobotBiz;
import com.steadon.saber.handler.token.TokenHandler;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.FeishuRobot;
import com.steadon.saber.model.dto.AppDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.AddRobotParam;
import com.steadon.saber.model.param.FeishuRobotParam;
import com.steadon.saber.model.resultEnum.ResultCode;
import com.steadon.saber.service.AccessService;
import com.steadon.saber.service.AppService;
import com.steadon.saber.service.FeishuRobotService;
import com.steadon.saber.util.StringUtils;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@AllArgsConstructor
public class FeishuRobotBiz implements IFeishuRobotBiz {

    private final AccessService accessService;
    private final AppService appService;
    private final FeishuRobotService feishuRobotService;

    @Override
    public SaberResponse<FeishuRobot> addRobot(AddRobotParam param) {
        FeishuRobot robot = new FeishuRobot();
        robot.setName(param.getName());
        robot.setDescription(param.getDescription());
        robot.setFeishuAppId(param.getFeishuAppId());
        robot.setFeishuAppSecret(param.getFeishuAppSecret());
        robot.setCreateBy(TokenHandler.getUsername());
        feishuRobotService.addOne(robot);
        log.info("addedRobot: robot={}", robot);
        return SaberResponse.success(robot);
    }

    /**
     * 获取飞书机器人列表（含搜索）
     */
    @Override
    public SaberResponse<PageData<FeishuRobot>> getRobotList(String keyword, int pageNo, int pageSize) {
        PageData<FeishuRobot> pageData = feishuRobotService.queryList(keyword, pageNo, pageSize);
        // 敏感信息做暗提示处理
        for (FeishuRobot feishuRobot : pageData.getResultList()) {
            String appSecret = feishuRobot.getFeishuAppSecret();
            String processed = StringUtils.coverSecret(appSecret);
            feishuRobot.setFeishuAppSecret(processed);
        }
        // 查询引用方列表并统计数量
        for (FeishuRobot feishuRobot : pageData.getResultList()) {
            List<AppDto> appList = appService.queryAppList(feishuRobot.getFeishuAppId());
            feishuRobot.setAppList(appList);
            feishuRobot.setAppId(accessService.queryAdmin(feishuRobot.getCreateBy()).getAppId());
        }
        return SaberResponse.success(pageData);
    }

    /**
     * 删除飞书机器人
     */
    @Override
    public SaberResponse<String> deleteRobotById(int robotId) {
        FeishuRobot feishuRobot = feishuRobotService.queryOne(robotId);
        if (feishuRobot == null) {
            return SaberResponse.failure(ResultCode.INVALID_PARAM, "机器人不存在");
        }
        feishuRobotService.deleteOne(feishuRobot);
        log.info("deletedRobot: robotId={}", robotId);
        return SaberResponse.success();
    }

    /**
     * 更新飞书机器人
     */
    @Override
    public SaberResponse<String> updateFeishuRobot(int robotId, FeishuRobotParam param) {
        String name = param.getName();
        String description = param.getDescription();

        FeishuRobot feishuRobot = feishuRobotService.queryOne(robotId);
        feishuRobot.setName(name);
        feishuRobot.setDescription(description);

        feishuRobotService.updateOne(feishuRobot);
        log.info("updatedRobot: robotId={}", robotId);
        return SaberResponse.success();
    }
}