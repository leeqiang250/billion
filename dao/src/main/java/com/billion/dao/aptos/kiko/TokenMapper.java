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
    /**
     * selectByScene
     *
     * @param scene scene
     * @return List<Token>
     */
    @Select("select * from token t left join token_scene p on t.id = p.token_id where t.chain = #{chain,jdbcType=VARCHAR} and p.`scene`= #{scene,jdbcType=VARCHAR}")
    List<Token> selectByScene(@Param("chain") String chain, @Param("scene") String scene);
}