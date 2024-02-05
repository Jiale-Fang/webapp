package pers.fjl.healthcheck.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pers.fjl.healthcheck.po.User;

@Mapper
public interface UserDao extends BaseMapper<User> {
}
