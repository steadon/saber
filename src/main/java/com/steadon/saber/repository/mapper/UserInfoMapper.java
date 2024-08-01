package com.steadon.saber.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.steadon.saber.model.dao.UserInfo;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author steadon
 * @since 2023-09-20
 */
@Mapper
public interface UserInfoMapper extends BaseMapper<UserInfo> {
}
