package puzzle.lianche.mapper;

import puzzle.lianche.utils.Page;
import org.apache.ibatis.session.SqlSession;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Map;


public class SqlMapper implements IMapper{

    @Autowired
    private SqlSession sqlSession;

    @Override
    public boolean insert(String statement, Object param) {
        return sqlSession.insert(statement, param) > 0;
    }

    @Override
    public boolean delete(String statement, Object param) {
        return sqlSession.delete(statement, param) > 0;
    }

    @Override
    public boolean update(String statement, Object param) {
        return sqlSession.update(statement, param) > 0;
    }

    @Override
    public <T> T query(String statement, Object param) {
        return sqlSession.selectOne(statement, param);
    }

    @Override
    public <T> List<T> queryList(String statement, Object param) {
        return sqlSession.selectList(statement, param);
    }

    @Override
    public <T> List<T> queryList(String statement,Map<String,Object> map, Page page) {
        return sqlSession.selectList(statement, map, page);
    }
}
