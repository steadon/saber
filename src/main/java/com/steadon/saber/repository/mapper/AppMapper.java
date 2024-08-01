package com.steadon.saber.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.steadon.saber.model.dao.App;
import com.steadon.saber.model.dto.AppDto;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author steadon
 * @since 2023-09-19
 */
@Mapper
public interface AppMapper extends BaseMapper<App> {

    List<AppDto> selectAppList(@Param("feishuAppId") String feishuAppId);
}
