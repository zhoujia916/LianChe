package puzzle.lianche.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import puzzle.lianche.mapper.SqlMapper;

public class BaseServiceImpl {
    @Autowired
    protected SqlMapper sqlMapper;
}
