package com.steadon.saber.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.steadon.saber.exception.SaberDBException;
import com.steadon.saber.model.dao.App;
import com.steadon.saber.model.dao.AppRobotMerge;
import com.steadon.saber.model.dto.AppDto;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.repository.mapper.AppMapper;
import com.steadon.saber.repository.mapper.AppRobotMergeMapper;
import com.steadon.saber.service.AppService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AppServiceImpl implements AppService {

    private final AppMapper appMapper;
    private final AppRobotMergeMapper appRobotMergeMapper;

    @Override
    public PageData<App> queryList(Integer pageNo, Integer pageSize) {
        Page<App> page = new Page<>(pageNo, pageSize);
        QueryWrapper<App> wrapper = new QueryWrapper<>();
        wrapper.orderByDesc("create_time");
        Page<App> pageData = appMapper.selectPage(page, wrapper);
        return new PageData<App>().praise(pageData);
    }

    @Override
    public void addOne(App app) {
        if (appMapper.insert(app) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void updateOne(App app) {
        if (appMapper.updateById(app) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public void deleteOne(Integer appDaoId) {
        if (appMapper.deleteById(appDaoId) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public Integer validAppId(String appId) {
        QueryWrapper<App> wrapper = new QueryWrapper<App>().eq("app_id", appId);
        App app = appMapper.selectOne(wrapper);
        return app == null ? null : app.getId();
    }

    @Override
    public List<AppDto> queryAppList(String feishuAppId) {
        return appMapper.selectAppList(feishuAppId);
    }

    @Override
    public void bindRobot(String appId, String feishuAppId, Boolean isDefault) {
        AppRobotMerge merge = new AppRobotMerge();
        merge.setAppId(appId);
        merge.setFeishuAppId(feishuAppId);
        merge.setIsDefault(isDefault != null && isDefault);
        appRobotMergeMapper.insert(merge);
    }

    @Override
    public boolean existDefaultRobot(String appId) {
        QueryWrapper<AppRobotMerge> wrapper = new QueryWrapper<AppRobotMerge>()
                .eq("app_id", appId)
                .eq("is_default", true);
        return !appRobotMergeMapper.selectList(wrapper).isEmpty();
    }

    @Override
    public boolean queryMerge(String appId, String feishuAppId) {
        QueryWrapper<AppRobotMerge> wrapper = new QueryWrapper<AppRobotMerge>()
                .eq("app_id", appId)
                .eq("feishu_app_id", feishuAppId);
        return !appRobotMergeMapper.selectList(wrapper).isEmpty();
    }

    @Override
    public App updateAppSecret(String appId) {
        QueryWrapper<App> wrapper = new QueryWrapper<App>()
                .eq("app_id", appId)
                .last("limit 1");
        App app = appMapper.selectOne(wrapper);
        if (Objects.nonNull(app)) {
            app.setAppSecret(UUID.randomUUID().toString().replace("-", ""));
            appMapper.updateById(app);
        }
        return app;
    }

    @Override
    public App queryApp(String appId, String appSecret) {
        QueryWrapper<App> wrapper = new QueryWrapper<App>()
                .eq("app_id", appId)
                .eq("app_secret", appSecret)
                .last("limit 1");
        return appMapper.selectOne(wrapper);
    }
}
