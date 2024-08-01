package com.steadon.saber.service;

import com.steadon.saber.model.dao.AppRobotMerge;
import com.steadon.saber.model.dao.FeishuRobot;
import com.steadon.saber.model.page.PageData;

import java.util.List;

public interface FeishuRobotService {

    void addOne(FeishuRobot feishuRobot);

    String querySecret(String feishuAppId);

    List<AppRobotMerge> queryMergeList(String appId);

    PageData<FeishuRobot> queryList(String keyword, int pageNo, int pageSize);

    FeishuRobot queryOne(int robotId);

    FeishuRobot queryByFeishuAppId(String fieshuAppId);

    void deleteOne(FeishuRobot feishuRobot);

    void updateOne(FeishuRobot feishuRobot);
}
