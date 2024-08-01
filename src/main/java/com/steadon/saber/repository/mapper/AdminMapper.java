package com.steadon.saber.repository.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.steadon.saber.model.dao.Admin;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * Mapper 接口
 * </p>
 *
 * @author steadon
 * @since 2023-09-19
 */
@Mapper
public interface AdminMapper extends BaseMapper<Admin> {
}
