package com.jerry.core;

import com.jerry.common.entity.RpcRequest;

public interface RpcClient {
    Object sendRequest(RpcRequest rpcRequest);
}
