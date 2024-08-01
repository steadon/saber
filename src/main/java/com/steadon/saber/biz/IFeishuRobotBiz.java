package com.steadon.saber.biz;

import com.steadon.saber.model.SaberResponse;
import com.steadon.saber.model.dao.FeishuRobot;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.model.param.AddRobotParam;
import com.steadon.saber.model.param.FeishuRobotParam;

public interface IFeishuRobotBiz {

    SaberResponse<FeishuRobot> addRobot(AddRobotParam param);

    SaberResponse<PageData<FeishuRobot>> getRobotList(String keyword, int pageNo, int pageSize);

    SaberResponse<String> deleteRobotById(int robotId);

    SaberResponse<String> updateFeishuRobot(int robotId, FeishuRobotParam param);
}
