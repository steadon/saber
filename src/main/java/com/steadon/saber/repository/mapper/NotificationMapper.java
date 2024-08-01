package com.steadon.saber.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.steadon.saber.model.dao.Notification;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author DoudiNCer
 * @since 2023-11-21
 */
@Mapper
public interface NotificationMapper extends BaseMapper<Notification> {
    Page<Notification> selectListForRecord(String appId, Page<Notification> page);
}
