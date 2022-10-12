package com.billion.dao.aptos.kiko;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.billion.model.entity.Token;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author liqiang
 */
@Mapper
public interface TokenMapper extends BaseMapper<Token> {

}