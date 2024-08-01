package com.steadon.saber.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.steadon.saber.exception.SaberDBException;
import com.steadon.saber.model.dao.AppRobotMerge;
import com.steadon.saber.model.dao.FeishuRobot;
import com.steadon.saber.model.page.PageData;
import com.steadon.saber.repository.mapper.AppRobotMergeMapper;
import com.steadon.saber.repository.mapper.FeishuRobotMapper;
import com.steadon.saber.service.FeishuRobotService;
import com.steadon.saber.util.StringUtils;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@AllArgsConstructor
public class FeishuRobotServiceImpl implements FeishuRobotService {

    private final FeishuRobotMapper feishuRobotMapper;
    private final AppRobotMergeMapper appRobotMergeMapper;

    @Override
    public void addOne(FeishuRobot feishuRobot) {
        if (feishuRobotMapper.insert(feishuRobot) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    public String querySecret(String feishuAppId) {
        QueryWrapper<FeishuRobot> wrapper = new QueryWrapper<FeishuRobot>().eq("feishu_app_id", feishuAppId);
        return feishuRobotMapper.selectOne(wrapper).getFeishuAppSecret();
    }

    @Override
    public List<AppRobotMerge> queryMergeList(String appId) {
        QueryWrapper<AppRobotMerge> wrapper = new QueryWrapper<AppRobotMerge>().eq("app_id", appId);
        return appRobotMergeMapper.selectList(wrapper);
    }

    @Override
    public PageData<FeishuRobot> queryList(String keyword, int pageNo, int pageSize) {
        QueryWrapper<FeishuRobot> wrapper = new QueryWrapper<>();
        if (StringUtils.isNotEmpty(keyword)) {
            wrapper.eq("feishu_app_id", keyword)
                    .or().like("description", keyword)
                    .or().like("name", keyword);
        }
        wrapper.orderByDesc("create_time");
        Page<FeishuRobot> page = new Page<>(pageNo, pageSize);
        Page<FeishuRobot> pageData = feishuRobotMapper.selectPage(page, wrapper);
        return new PageData<FeishuRobot>().praise(pageData);
    }

    @Override
    public FeishuRobot queryOne(int robotId) {
        return feishuRobotMapper.selectById(robotId);
    }

    @Override
    public FeishuRobot queryByFeishuAppId(String feishuAppId) {
        return feishuRobotMapper.selectOne(new QueryWrapper<FeishuRobot>()
                .eq("feishu_app_id", feishuAppId));
    }

    @Override
    public void updateOne(FeishuRobot feishuRobot) {
        if (feishuRobotMapper.updateById(feishuRobot) == 0) {
            throw new SaberDBException();
        }
    }

    @Override
    @Transactional(isolation = Isolation.READ_COMMITTED)
    public void deleteOne(FeishuRobot feishuRobot) {
        if (feishuRobotMapper.deleteById(feishuRobot) == 0) {
            throw new SaberDBException();
        }
    }
}