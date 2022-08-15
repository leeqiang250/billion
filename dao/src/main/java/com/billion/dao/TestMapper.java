package com.billion.dao;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.billion.model.entity.Test;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Repository;

/**
 * @author liqiang
 */
@Mapper
@Repository
public interface TestMapper extends BaseMapper<Test> {

}
