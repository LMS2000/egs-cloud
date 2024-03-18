/**
 * 文章类型定义
 */
declare namespace PostType {
/**
   * 实体
   */
  interface Post {
    id: number;
    title: string;
    content: string;
    content: string;
    thumbNum: number;
    favourNum?: number;
    userId: number;
    createTime: Date;
    updateTime: Date;
		tagList:string[];
		user:UserType.User;
  }
	/**
	 * 创建请求
	 */
	interface PostQueryRequest {
	  title: string;
		current:number;
		pageSize:number;
		sortField:string;
		
	}
	
	
}