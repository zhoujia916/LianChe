package puzzle.lianche.mapper;

import puzzle.lianche.utils.Page;

import java.util.List;
import java.util.Map;

public interface IMapper {

    boolean insert(String statement, Object param);

    boolean delete(String statement, Object param);

    boolean update(String statement, Object param);

    <T> T query(String statement, Object param);

    <T> List<T> queryList(String statement, Object param);

    <T> List<T> queryList(String statement, Map<String, Object> map, Page page);

}
