package com.example.mapper;

import com.example.domain.User;
import org.apache.ibatis.annotations.Mapper;

import java.util.List;

@Mapper
public interface DaoMapper {
    int insertUserData(List<User> users);
}
