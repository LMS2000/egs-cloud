import { Avatar, Card, Flex, Image, Input, List, message, Tabs, Tag, Typography } from 'antd';
import {Link } from 'umi';
import React, { useEffect, useState } from 'react';
import moment from 'moment';
import { UserOutlined } from '@ant-design/icons';
import {listMyGeneratorVoByPageUsingPost} from '@/services/generatorService';
import { PageContainer, ProFormSelect, ProFormText, QueryFilter } from '@ant-design/pro-components';

/**xxxx
 * 默认分页参数
 */
const DEFAULT_PAGE_PARAMS: PageRequest = {
  current: 1,
  pageSize: 12,
  sortField: 'create_time',
  sortOrder: 'descend',
};

const Projects: React.FC = () => {
	moment.locale('zh-cn');
	const [loading, setLoading] = useState<boolean>(true);
	const [dataList, setDataList] = useState<API.GeneratorVO[]>([]);
	const [total, setTotal] = useState<number>(0);
	// 搜索条件
	const [searchParams, setSearchParams] = useState<API.GeneratorQueryRequest>({
	  ...DEFAULT_PAGE_PARAMS,
	});
	/**
	 * 搜索
	 */
	const doSearch = async () => {
	  setLoading(true);
	  try {
	    const res = await listMyGeneratorVoByPageUsingPost(searchParams);
	    setDataList(res.data?.records ?? []);
	    setTotal(Number(res.data?.total) ?? 0);
	  } catch (error: any) {
	    message.error('获取数据失败，' + error.message);
	  }
	  setLoading(false);
	};
	
	useEffect(() => {
	  doSearch();
	}, [searchParams]);
	
	
	
  /**
   * 标签列表
   * @param tags
   */
  const tagListView = (tags?: string[]) => {
    if (!tags) {
      return <></>;
    }
  
    return (
      <div style={{ marginBottom: 8 }}>
        {tags.map((tag) => (
          <Tag key={tag}>{tag}</Tag>
        ))} 
      </div>
    );
  };
  
  return (
    <PageContainer title={<></>}>

    

    
    <List<API.GeneratorVO>
      rowKey="id"
      loading={loading}
      grid={{ gutter: 24, xxl: 3, xl: 2, lg: 2, md: 2, sm: 2, xs: 1 }}
      dataSource={dataList}
      pagination={{
        current: searchParams.current,
        pageSize: searchParams.pageSize,
        total,
        onChange(current: number, pageSize: number) {
          setSearchParams({
            ...searchParams,
            current,
            pageSize,
          });
        },
      }}
      renderItem={(data) => (
        <List.Item>
          <Link to={`/generator/detail/${data.id}`}>
            <Card hoverable cover={<Image alt={data.name} src={data.picture} style={{ height: '150px' }} />} 
						   style={{ width: '250px' }}
							 >
              <Card.Meta
                title={<a>{data.name}</a>}
								
                description={
                  <Typography.Paragraph ellipsis={{ rows: 1 }} style={{ height: 20 }}>
                    {data.description}
                  </Typography.Paragraph>
                }
              />
              {tagListView(data.tags)}
              <Flex justify="space-between" align="center">
                <Typography.Text type="secondary" style={{ fontSize: 16 }}>
                  {moment(data.createTime).fromNow()}
                </Typography.Text>
                <div>
                  <Avatar src={data.user?.userAvatar ?? <UserOutlined />} />
                </div>
              </Flex>
            </Card>
          </Link>
        </List.Item>
      )}
    />
    </PageContainer>
  );
};

export default Projects;
