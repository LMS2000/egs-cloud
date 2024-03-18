/**
 * 智能导入输入示例
 */
export const AUTO_INPUT_EXAMPLE = "id，用户名，创建时间，更新时间，is_deleted";

/**
 * JSON 输入示例
 */
export const JSON_INPUT_EXAMPLE = {
	dbName: 'lms_db',
	tableList: [
		{
			tableName: 'user',
			tableComment: '用户表',
			fieldList: [
				{
					fieldName: 'username',
					comment: '用户名',
					fieldType: 'varchar(256)',
					mockType: '随机',
					mockParams: '人名',
					notNull: true,
					primaryKey: false,
					autoIncrement: false,
				},
				{
					fieldName: 'id',
					comment: '主键',
					fieldType: 'bigint',
					mockType: '固定',
					notNull: true,
					primaryKey: true,
					autoIncrement: true,
				},
				{
					fieldName: 'create_time',
					comment: '创建时间',
					defaultValue: 'CURRENT_TIMESTAMP',
					fieldType: 'datetime',
					mockType: '固定',
					notNull: true,
					primaryKey: false,
					autoIncrement: false,
				},
				{
					fieldName: 'update_time',
					comment: '更新时间',
					defaultValue: 'CURRENT_TIMESTAMP',
					fieldType: 'datetime',
					mockType: '固定',
					notNull: true,
					primaryKey: false,
					autoIncrement: false,
					extra: 'on update CURRENT_TIMESTAMP',
				},
				{
					fieldName: 'is_deleted',
					comment: '是否删除(0-未删, 1-已删)',
					defaultValue: '0',
					fieldType: 'tinyint',
					mockType: '固定',
					notNull: true,
					primaryKey: false,
					autoIncrement: false,
				},
			]
		}
	],


};

/**
 * SQL 输入示例
 */
export const SQL_INPUT_EXAMPLE =
	'-- 用户表\n' +
	'create table if not exists lms_db.user\n' +
	'(\n' +
	"id bigint not null auto_increment comment '主键' primary key,\n" +
	"username varchar(256) not null comment '用户名',\n" +
	"create_time datetime default CURRENT_TIMESTAMP not null comment '创建时间',\n" +
	"update_time datetime default CURRENT_TIMESTAMP not null on update CURRENT_TIMESTAMP comment '更新时间',\n" +
	"is_deleted tinyint default 0 not null comment '是否删除(0-未删, 1-已删)'\n" +
	") comment '用户表';";



export const SQL_GENERATE_INIT_EXAMPLE = {
	"main": "必填, 代码从这里开始生成, 用 @规则名() 引用其他语句",
	"规则名": "可以编写任意 SQL 语句 @规则名2() @动态传参(a = 求给 ||| b = star)",
	"规则名2": {
		"sql": "用 #{参数名} 指定可被替换的值",
		"params": {
			"参数名": "在 params 中指定静态参数, 会优先被替换"
		}
	},
	"动态传参": "#{a}lms#{b}"
};
/**
 * SQL 输入示例
 */
export const SQL_GENERATE_EXAMPLE = {
	"main": "select (a / b - 1) from (@查整体(date = 今天)) a, (@查整体(date = 昨天)) b",
	"查整体": "@查年级() union @查1班() union @查2班() where date = #{date}",
	"查年级": "@查汇总_性别汇总() union @查汇总_性别分组() union @查汇总_爱好汇总() union @查汇总_爱好分组() union @查汇总_电脑类别汇总() union @查汇总_电脑类别分组()",
	"查汇总_性别汇总": "@查除电脑关联表()",
	"查汇总_性别分组": "@查除电脑关联表() group by 性别",
	"查汇总_爱好汇总": "@查除电脑关联表()",
	"查汇总_爱好分组": "@查除电脑关联表() where 爱好 in (xx) group by 爱好",
	"查汇总_电脑类别汇总": "@查除三连和学习表()",
	"查汇总_电脑类别分组": "@查除三连和学习表() group by 电脑类别",
	"查1班": "@查1班_性别汇总() union @查1班_性别分组() union @查1班_爱好汇总() union @查1班_爱好分组() union @查1班_电脑类别汇总() union @查汇总_电脑类别分组()",
	"查1班_性别汇总": "@查除电脑关联表() where 1班",
	"查1班_性别分组": "@查除电脑关联表() where 1班 group by 性别",
	"查1班_爱好汇总": "@查除电脑关联表() where 1班",
	"查1班_爱好分组": "@查除电脑关联表() where 1班 and 爱好 in (xx) group by 爱好",
	"查1班_电脑类别汇总": "@查除三连和学习表() where 1班",
	"查1班_电脑类别分组": "@查除三连和学习表() where 1班 group by 电脑类别",
	"查2班": "@查2班_性别汇总() union @查2班_性别分组() union @查2班_电脑类别汇总() union @查2班_电脑类别分组()",
	"查2班_性别汇总": "@查除电脑关联表() where 2班",
	"查2班_性别分组": "@查除电脑关联表() where 2班 group by 性别",
	"查2班_电脑类别汇总": "@查除三连和学习表() where 2班",
	"查2班_电脑类别分组": "@查除三连和学习表() where 2班 group by 电脑类别",
	"查所有关联表": "@查信息表() left join (@查三连表()) left join (@查学习表()) left join (@查电脑表()) left join (@查全校信息())",
	"查除电脑关联表": "@查信息表() left join (@查三连表()) left join (@查学习表()) left join (@查全校信息())",
	"查除三连和学习表": "@查信息表() left join (@查电脑表()) left join (@查全校信息())",
	"查信息表": "select 字段 from 信息表 where 年级 = 1",
	"查三连表": "select 字段 from 三连表 where 年级 = 1",
	"查学习表": "select 字段 from 学习表 where 年级 = 1",
	"查电脑表": "select 字段 from 电脑表 where 年级 = 1",
	"查全校信息": "select 字段 from 信息表"
};


