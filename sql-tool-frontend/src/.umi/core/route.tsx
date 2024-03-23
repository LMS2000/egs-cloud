// @ts-nocheck
// This file is generated by Umi automatically
// DO NOT CHANGE IT MANUALLY!
import React from 'react';

export async function getRoutes() {
  const routes = {"1":{"name":"代码生成","path":"/","parentId":"ant-design-pro-layout","id":"1"},"2":{"path":"/generator","icon":"home","name":"生成器大全","parentId":"ant-design-pro-layout","id":"2"},"3":{"path":"/generator/add","icon":"plus","access":"canUser","name":"创建生成器","parentId":"ant-design-pro-layout","id":"3"},"4":{"path":"/generator/update","icon":"plus","name":"修改生成器","hideInMenu":true,"parentId":"ant-design-pro-layout","id":"4"},"5":{"path":"/generator/use/:id","icon":"home","name":"使用生成器","access":"canUser","hideInMenu":true,"parentId":"ant-design-pro-layout","id":"5"},"6":{"path":"/generator/detail/:id","icon":"home","name":"生成器详情","hideInMenu":true,"parentId":"ant-design-pro-layout","id":"6"},"7":{"name":"词库大全","path":"/dict/all","parentId":"ant-design-pro-layout","id":"7"},"8":{"name":"表大全","path":"/table/all","parentId":"ant-design-pro-layout","id":"8"},"9":{"name":"字段大全","path":"/field/all","parentId":"ant-design-pro-layout","id":"9"},"10":{"name":"创建词库","path":"","access":"canUser","parentId":"11","id":"10","originPath":"/dict/add"},"11":{"path":"/dict/add","isWrapper":true,"parentId":"ant-design-pro-layout","id":"11"},"12":{"name":"设置","path":"/user/profile","hideInMenu":true,"parentId":"ant-design-pro-layout","id":"12"},"13":{"name":"个人中心","path":"/user/center","hideInMenu":true,"parentId":"ant-design-pro-layout","id":"13"},"14":{"path":"/user/help","icon":"plus","name":"帮助文档","hideInMenu":true,"parentId":"ant-design-pro-layout","id":"14"},"15":{"name":"创建词库成功","path":"","hideInMenu":true,"parentId":"16","id":"15","originPath":"/dict/add_result"},"16":{"path":"/dict/add_result","isWrapper":true,"parentId":"ant-design-pro-layout","id":"16"},"17":{"path":"/user","hideInMenu":true,"headerRender":false,"parentId":"ant-design-pro-layout","id":"17"},"18":{"name":"用户登录","path":"/user/login","parentId":"17","id":"18"},"19":{"name":"用户注册","path":"/user/register","parentId":"17","id":"19"},"20":{"name":"忘记密码","path":"/user/findback","parentId":"17","id":"20"},"21":{"path":"/admin","access":"canAdmin","name":"管理","parentId":"ant-design-pro-layout","id":"21"},"22":{"name":"用户管理","path":"/admin/user","parentId":"21","id":"22"},"23":{"name":"生成器管理","path":"/admin/generator","parentId":"21","id":"23"},"24":{"name":"词库管理","path":"/admin/dict","parentId":"21","id":"24"},"25":{"name":"表管理","path":"/admin/table","parentId":"21","id":"25"},"26":{"name":"字段管理","path":"/admin/field","parentId":"21","id":"26"},"27":{"name":"举报管理","path":"/admin/report","parentId":"21","id":"27"},"ant-design-pro-layout":{"id":"ant-design-pro-layout","path":"/","isLayout":true}} as const;
  return {
    routes,
    routeComponents: {
'1': React.lazy(() => import(/* webpackChunkName: "p__index__index" */'@/pages/index/index.tsx')),
'2': React.lazy(() => import(/* webpackChunkName: "p__generatorList__index" */'@/pages/generatorList/index.tsx')),
'3': React.lazy(() => import(/* webpackChunkName: "p__generator__Add__index" */'@/pages/generator/Add/index.tsx')),
'4': React.lazy(() => import(/* webpackChunkName: "p__generator__Add__index" */'@/pages/generator/Add/index.tsx')),
'5': React.lazy(() => import(/* webpackChunkName: "p__generator__Use__index" */'@/pages/generator/Use/index.tsx')),
'6': React.lazy(() => import(/* webpackChunkName: "p__generator__Detail__index" */'@/pages/generator/Detail/index.tsx')),
'7': React.lazy(() => import(/* webpackChunkName: "p__dict__index" */'@/pages/dict/index.tsx')),
'8': React.lazy(() => import(/* webpackChunkName: "p__tableInfo__index" */'@/pages/tableInfo/index.tsx')),
'9': React.lazy(() => import(/* webpackChunkName: "p__fieldInfo__index" */'@/pages/fieldInfo/index.tsx')),
'10': React.lazy(() => import(/* webpackChunkName: "p__dict__add__index" */'@/pages/dict/add/index.tsx')),
'11': React.lazy(() => import(/* webpackChunkName: "wrappers__auth__index" */'@/wrappers/auth/index.tsx')),
'12': React.lazy(() => import(/* webpackChunkName: "p__user__settings__index" */'@/pages/user/settings/index.tsx')),
'13': React.lazy(() => import(/* webpackChunkName: "p__user__center__index" */'@/pages/user/center/index.tsx')),
'14': React.lazy(() => import(/* webpackChunkName: "p__user__help__index" */'@/pages/user/help/index.tsx')),
'15': React.lazy(() => import(/* webpackChunkName: "p__dict__add_result__index" */'@/pages/dict/add_result/index.tsx')),
'16': React.lazy(() => import(/* webpackChunkName: "wrappers__auth__index" */'@/wrappers/auth/index.tsx')),
'17': React.lazy(() => import( './EmptyRoute')),
'18': React.lazy(() => import(/* webpackChunkName: "p__user__login__index" */'@/pages/user/login/index.tsx')),
'19': React.lazy(() => import(/* webpackChunkName: "p__user__register__index" */'@/pages/user/register/index.tsx')),
'20': React.lazy(() => import(/* webpackChunkName: "p__user__findback__index" */'@/pages/user/findback/index.tsx')),
'21': React.lazy(() => import( './EmptyRoute')),
'22': React.lazy(() => import(/* webpackChunkName: "p__admin__user__index" */'@/pages/admin/user/index.tsx')),
'23': React.lazy(() => import(/* webpackChunkName: "p__admin__generator__index" */'@/pages/admin/generator/index.tsx')),
'24': React.lazy(() => import(/* webpackChunkName: "p__admin__dict__index" */'@/pages/admin/dict/index.tsx')),
'25': React.lazy(() => import(/* webpackChunkName: "p__admin__tableInfo__index" */'@/pages/admin/tableInfo/index.tsx')),
'26': React.lazy(() => import(/* webpackChunkName: "p__admin__fieldInfo__index" */'@/pages/admin/fieldInfo/index.tsx')),
'27': React.lazy(() => import(/* webpackChunkName: "p__admin__report__index" */'@/pages/admin/report/index.tsx')),
'ant-design-pro-layout': React.lazy(() => import(/* webpackChunkName: "umi__plugin-layout__Layout" */'E:/sqltool-cloud/sqltool-cloud/sql-tool-frontend/src/.umi/plugin-layout/Layout.tsx')),
},
  };
}
