import ProfileCard from '@/components/ProfileCard';
import { listMyAddTableInfoByPage } from '@/services/tableInfoService';
import { PageContainer,ProCard } from '@ant-design/pro-components';
import { useNavigate } from '@umijs/max';
import { Col, message, Radio, RadioChangeEvent, Row } from 'antd';
import React, { useState } from 'react';

import './index.less';

/**
 * 表信息页
 *
 * @constructor
 */
const TableInfoPage: React.FC = () => {
  const [layout, setLayout] = useState('half');

  const navigate = useNavigate();

  /**
   * 加载我的数据
   * @param searchParams
   * @param setDataList
   * @param setTotal 
   */
  const loadMyData = (
    searchParams: TableInfoType.TableInfoQueryRequest,
    setDataList: (dataList: TableInfoType.TableInfo[]) => void,
    setTotal: (total: number) => void,
  ) => {
    listMyAddTableInfoByPage(searchParams)
      .then((res) => {
        setDataList(res.data.records);
        setTotal(res.data.total);
      })
      .catch((e) => {
        message.error('加载失败，' + e.message);
      });
  };

  // 导入表，跳转到主页
  const doImport = (tableInfo: TableInfoType.TableInfo) => {
    navigate(`/?table_id=${tableInfo.id}`);
  };

  /**
   * 更改布局
   * @param e
   */
  const onLayoutChange = (e: RadioChangeEvent) => {
    setLayout(e.target.value);
  };

  return (
    <div className="table-info">
      <PageContainer
        title={false}
        extra={
          <div style={{ marginLeft: 0 }}>
            切换布局：
            <Radio.Group onChange={onLayoutChange} value={layout}>
              <Radio.Button value="input">公开</Radio.Button>
              <Radio.Button value="half">同屏</Radio.Button>
              <Radio.Button value="output">个人</Radio.Button>
            </Radio.Group>
          </div>
        }
      >
        <Row gutter={[8, 8]}>
          <Col
            xs={18}
            xl={layout === 'half' ? 12 : 24}
            order={layout === 'output' ? 2 : 1}
          >
            <ProfileCard
              title="个人信息"
              showTag={false}
              onImport={doImport}
            />
          </Col>
         <Col
           xs={24}
           xl={layout === 'half' ? 12 : 24}
           order={layout === 'output' ? 2 : 1}
         >
            <PageContainer
						  title={false}
                 fixedHeader
                 tabList={[
                   {
                     tab: '修改密码',
                     key: '1',
                   },
                   {
                     tab: '修改信息',
                     key: '2',
                   },
                   {
                     tab: '查看SDK',
                     key: '3',
                     disabled: true,
                   },
                 ]}
               >
                 <ProCard direction="column" ghost gutter={[0, 16]}>
                   <ProCard style={{ height: 200 }} />
                   <ProCard gutter={16} ghost>
                     <ProCard colSpan={16} style={{ height: 200 }} />
                     <ProCard colSpan={8} style={{ height: 200 }} />
                   </ProCard>
                 </ProCard>
               </PageContainer>
         </Col>
        </Row>
      </PageContainer>
    </div>
  );
};

export default TableInfoPage;
