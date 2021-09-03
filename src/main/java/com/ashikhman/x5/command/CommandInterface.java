package com.ashikhman.x5.command;

import com.ashikhman.x5.client.api.model.CurrentTickRequest;

public interface CommandInterface {

    void updateRequest(CurrentTickRequest request);
}
