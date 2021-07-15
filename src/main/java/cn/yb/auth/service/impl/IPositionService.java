package cn.yb.auth.service.impl;

import cn.yb.auth.model.Position;

public interface IPositionService {
    int edit(Position position);

    int delete(Position position);
}
