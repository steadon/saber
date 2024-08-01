package com.steadon.saber.service;

import com.steadon.saber.model.dao.Admin;

import java.util.List;

public interface AccessService {

    Admin queryAdmin(String username);

    List<String> queryAdminList();

}
