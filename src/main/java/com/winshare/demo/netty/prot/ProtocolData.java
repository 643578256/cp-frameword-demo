package com.winshare.demo.netty.prot;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProtocolData<T> {
    private int len;
    private T data;
}
