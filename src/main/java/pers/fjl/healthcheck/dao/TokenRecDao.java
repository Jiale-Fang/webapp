package pers.fjl.healthcheck.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import pers.fjl.healthcheck.po.TokenRec;

@Mapper
public interface TokenRecDao extends BaseMapper<TokenRec> {
}
