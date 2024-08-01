package com.steadon.saber.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.steadon.saber.model.dao.Admin;
import com.steadon.saber.repository.mapper.AdminMapper;
import com.steadon.saber.service.AccessService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AccessServiceImpl implements AccessService {

    private final AdminMapper adminMapper;

    @Override
    public Admin queryAdmin(String username) {
        QueryWrapper<Admin> wrapper = new QueryWrapper<Admin>().eq("username", username);
        return adminMapper.selectOne(wrapper);
    }

    @Override
    public List<String> queryAdminList() {
        List<Admin> adminList = adminMapper.selectList(null);
        return adminList.stream().map(Admin::getUsername).toList();
    }
}
