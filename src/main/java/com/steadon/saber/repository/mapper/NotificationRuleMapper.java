package com.steadon.saber.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.steadon.saber.model.dao.NotificationRule;
import com.steadon.saber.model.dto.NotificationRuleDto;
import com.steadon.saber.model.param.SearchNotificationRuleParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author steadon
 * @since 2023-10-08
 */
@Mapper
public interface NotificationRuleMapper extends BaseMapper<NotificationRule> {
    IPage<NotificationRuleDto> selectVoPage(@Param("param") SearchNotificationRuleParam param, @Param("appId") String appId, IPage<NotificationRule> page);
}