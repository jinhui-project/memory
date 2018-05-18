package com.jinhui.common.mapper;


import com.jinhui.common.entity.domain.Message;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Component;

import java.util.List;

@Mapper
@Component
public interface MessageMapper {

    int insertSelective(Message record);

    List<Message> selectMessageListByVenueId(@Param("venueId") String venueId);

    int updateByPrimaryKeySelective(Message record);

}