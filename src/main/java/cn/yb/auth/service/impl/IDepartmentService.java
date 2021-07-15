package cn.yb.auth.service.impl;

import cn.yb.auth.model.Department;
import cn.yb.auth.model.Position;

public interface IDepartmentService {
    int edit(Department department);

    int delete(Department department);
}
