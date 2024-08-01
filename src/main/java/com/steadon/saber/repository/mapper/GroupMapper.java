package com.steadon.saber.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.steadon.saber.model.dao.Group;
import com.steadon.saber.model.dto.UserInfoDto;
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
public interface GroupMapper extends BaseMapper<Group> {
    List<UserInfoDto> queryUserInfoById(@Param("id") Integer id);
}
