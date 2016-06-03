package puzzle.lianche.service.impl;

import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import puzzle.lianche.Constants;
import puzzle.lianche.entity.SystemAuthority;
import puzzle.lianche.entity.SystemMenuAction;
import puzzle.lianche.service.ISystemAuthorityService;
import puzzle.lianche.utils.ConvertUtil;
import puzzle.lianche.utils.Page;
import puzzle.lianche.mapper.SqlMapper;
import puzzle.lianche.utils.StringUtil;

@Service("systemAuthorityService")
public class SystemAuthorityServiceImpl implements ISystemAuthorityService {
	
	@Autowired
	private SqlMapper sqlMapper;
	
	/**
	* 插入单条记录
	*/
	@Override
	public boolean insert(SystemAuthority entity){
		return sqlMapper.insert("SystemAuthorityMapper.insert", entity);
	}

	/**
	* 批量插入记录
	*/
	@Override
    public boolean insertBatch(List<SystemAuthority> list){
    	return sqlMapper.update("SystemAuthorityMapper.insertBatch", list);
    }

    /**
     * 更新单条记录
     */
    @Override
    public boolean update(SystemAuthority entity){
        return sqlMapper.update("SystemAuthorityMapper.update", entity);
    }

	/**
	* 删除单条记录
	*/
	@Override
    public boolean delete(Map<String, Object> map){
    	return sqlMapper.delete("SystemAuthorityMapper.delete", map);
    }

    /**
     * 保存记录
     */
    @Override
    public boolean save(SystemAuthority entity){

        HashMap<String,Object> map = new HashMap<String, Object>();
        map.put("sourceId", entity.getSourceId());
        map.put("sourceType", entity.getSourceType());
        List<SystemAuthority> oldList = queryList(map);

        List<SystemAuthority> addList = new ArrayList<SystemAuthority>();
        List<Integer> ids = new ArrayList<Integer>();

        if(oldList != null && oldList.size() > 0) {
            if (StringUtil.isNotNullOrEmpty(entity.getTarget())) {
                String[] array = entity.getTarget().split(",");
                for(String target : array){
                    String[] infos = target.split("-");
                    int targetId = ConvertUtil.toInt(infos[1]);
                    int targetType = ConvertUtil.toInt(infos[0]);
                    boolean find = false;
                    for(SystemAuthority item : oldList){
                        if(item.getTargetId().equals(targetId) && item.getTargetType().equals(targetType)){
                            find = true;
                            break;
                        }
                    }
                    if(!find){
                        SystemAuthority authority = new SystemAuthority();
                        authority.setSourceType(entity.getSourceType());
                        authority.setSourceId(entity.getSourceId());
                        authority.setTargetType(targetType);
                        authority.setTargetId(targetId);
                        addList.add(authority);
                    }
                }
                for(SystemAuthority item : oldList){
                    boolean find = false;
                    for(String target : array){
                        String[] infos = target.split("-");
                        int targetId = ConvertUtil.toInt(infos[1]);
                        int targetType = ConvertUtil.toInt(infos[0]);
                        if(item.getTargetId().equals(targetId) && item.getTargetType().equals(targetType)){
                            find = true;
                            break;
                        }
                    }
                    if(!find){
                        ids.add(item.getAuthorityId());
                    }
                }
            } else {
                for(SystemAuthority item : oldList){
                    ids.add(item.getAuthorityId());
                }
            }
        }else{
            if (StringUtil.isNotNullOrEmpty(entity.getTarget())) {
                String[] array = entity.getTarget().split(",");

                for(String target : array){
                    String[] infos = target.split("-");

                    SystemAuthority authority = new SystemAuthority();
                    authority.setSourceType(entity.getSourceType());
                    authority.setSourceId(entity.getSourceId());
                    authority.setTargetType(ConvertUtil.toInt(infos[0]));
                    authority.setTargetId(ConvertUtil.toInt(infos[1]));

                    addList.add(authority);
                }

            }
        }
        if(addList.size() > 0){
            this.insertBatch(addList);
        }
        if(ids.size() > 0){
            map.clear();
            map.put("authorityIds", ids);
            this.delete(map);
        }
        return true;
    }

	/**
	* 查询单条记录
	*/
	@Override
    public SystemAuthority query(Map<String, Object> map){
    	return sqlMapper.query("SystemAuthorityMapper.query", map);
    }

	/**
	* 查询多条记录
	*/
	@Override
    public List<SystemAuthority> queryList(Object param){
    	return sqlMapper.queryList("SystemAuthorityMapper.queryList", param);
    }
    
    
    /**
	* 查询分页记录
	*/
	@Override
    public List<SystemAuthority> queryList(Map<String, Object> map, Page page){
    	return sqlMapper.queryList("SystemAuthorityMapper.queryList", map, page);
    }
    
}
