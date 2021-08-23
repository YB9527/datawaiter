package cn.yb.auth.service.impl;

import cn.yb.auth.model.Position;

import java.util.List;

public interface IPositionService {
    int edit(Position position);

    int delete(Position position);

    List<Position> findGroupByProjectid(String projectid);

    List<Position> findByProjectid(String projectid);
}
