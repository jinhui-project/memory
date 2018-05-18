package com.jinhui.common.service;


import com.jinhui.common.entity.domain.Message;
import com.jinhui.common.entity.domain.VenueUser;

import java.util.List;

/**
 * 馆服务层
 *
 * @autor wsc
 * @create 2018-05-16 9:51
 **/
public interface VenueService {

    int createVenue(VenueUser venue,String createId);

    List<VenueUser> queryPrivateVenue(String openId);

    List<VenueUser> queryPublicVenue();

    VenueUser  queryVenue(String venueId);

    int addMessage(Message message);

    List<Message> queryMessageListByVenueId(String venueId);
}
