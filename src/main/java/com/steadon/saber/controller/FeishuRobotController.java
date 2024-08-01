package com.steadon.saber.controller;

import com.steadon.saber.biz.IFeishuRobotBiz;
import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.FeishuRobot;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.AddRobotParam;
import com.steadon.saber.model.param.FeishuRobotParam;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

/**
 * 飞书机器人
 */

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/saber")
public class FeishuRobotController {

    private final IFeishuRobotBiz iFeishuRobotBiz;

    /**
     * 新增机器人
     */
    @PutMapping("/robot/add")
    public SaberResponse<FeishuRobot> addRobot(@RequestBody AddRobotParam param) {
        return iFeishuRobotBiz.addRobot(param);
    }

    /**
     * 获得机器人列表
     */
    @GetMapping("/robot/list")
    public SaberResponse<PageData<FeishuRobot>> getRobotList(@RequestParam(value = "k", required = false, defaultValue = "") String keyword,
                                                             @RequestParam(value = "page_no", required = false, defaultValue = "1") Integer pageNo,
                                                             @RequestParam(value = "page_size", required = false, defaultValue = "15") Integer pageSize) {
        return iFeishuRobotBiz.getRobotList(keyword, pageNo, pageSize);
    }

    /**
     * 删除机器人
     */
    @PostMapping("/robot/delete")
    public SaberResponse<String> deleteRobot(@RequestParam(value = "id") int robotId) {
        return iFeishuRobotBiz.deleteRobotById(robotId);
    }

    /**
     * 更新机器人
     */
    @PostMapping("/robot/update")
    public SaberResponse<String> updateRobot(@RequestParam(value = "id") int robotId,
                                             @RequestBody FeishuRobotParam param) {
        return iFeishuRobotBiz.updateFeishuRobot(robotId, param);
    }
}