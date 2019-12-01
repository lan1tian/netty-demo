package com.lantian.network.netty;

import lombok.Data;

@Data
public class Message {

    private MessageType type;
    private String echo;



}
