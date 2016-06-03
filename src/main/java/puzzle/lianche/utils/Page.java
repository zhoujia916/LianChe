package puzzle.lianche.utils;

import org.apache.ibatis.session.RowBounds;
import puzzle.lianche.Constants;

public class Page extends RowBounds {
	private int pageIndex = 1;
	private int pageSize = Constants.PageHelper.PAGE_SIZE_COMMON;
	private int total = 0;
	
	public void setPageIndex(int pageIndex){
		this.pageIndex = pageIndex;
	}
	
	public int getPageIndex(){
		return this.pageIndex;
	}
	
	public void setPageSize(int pageSize){
		this.pageSize = pageSize;
	}
	
	public int getPageSize(){
		return this.pageSize;
	}
	
	public void setTotal(int total){
		this.total = total;
	}
	
	public int getTotal(){
		return this.total;
	}
}
