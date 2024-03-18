import { SQL_INPUT_EXAMPLE } from '@/constants/examples';
import { getSchemaBySql,getSqlByAi } from '@/services/sqlService';
import { Button, Form, message, Modal, Space,Switch } from 'antd';
import { FormInstance } from 'antd/es/form';
import TextArea from 'antd/es/input/TextArea';
import React, { useRef, useState } from 'react';

interface Props {
  onSubmit: (values: TableSchema) => void;
  visible: boolean;
  onClose: () => void;
}

/**
 * 建表 SQL 输入模态框
 *
 * @constructor
 */
const SqlInput: React.FC<Props> = (props) => {
  const { onSubmit, visible, onClose } = props;
  const [form] = Form.useForm();
	const [loading, setLoading] = useState(false);
const [showAiChat, setshowAiChat] = useState(false);
  /**
   * sql 转为 schema
   * @param values
   */
  const onFinish = async (values: any) => {
    if (!values.sql) {
      return;
    }
    try {
      const res = await getSchemaBySql(values);
      onSubmit?.(res.data);
    } catch (e: any) {
      message.error('导入错误，' + e.message);
    }
  };
	
	const doAiBuildSql= async()=>{
		console.log("开始构建")
		     
        setLoading(true);
				
				// try{
				// 	const result=await getSqlByAi({message:form.getFieldValue('aiMessage')})
				// 	console.log(result)
				// 	if(result.code==20000){
				// 		form.setFieldValue('sql', result.data)
				// 		message.success("智能构建成功！")
				// 	}else{
				// 		message.error("构建失败！")
				// 	}
				// }catch(errorInfo){
				// 	message.error(errorInfo)
				// }

	}

  return (
    <Modal title="导入建表 SQL" open={visible} footer={null} onCancel={onClose}>
      <Form<GenerateBySqlRequest>
        form={form}
        layout="vertical"
        onFinish={onFinish}
      >
        <Form.Item 
          name="sql"
          label={
            <>
              请输入建表 SQL 语句：
              <Button
                onClick={() => form.setFieldValue('sql', SQL_INPUT_EXAMPLE)}
              >
                导入示例
              </Button>
							<span style={{marginLeft:'20px',marginRight:'10px'}}>智能构建</span>
							<Switch onChange={(value) => setshowAiChat(value)} />
            </>
          }
          rules={[{ required: true, message: '请输入建表 SQL' }]}
        >
				 
          <TextArea onLoad={(event)=>{
					   return loading;
					}}
            placeholder="请输入建表 SQL 语句，可以在生成结果后复制"
            autoSize={{ minRows: 16 }}
          />
        </Form.Item>
				{
					showAiChat&&(
					<Form.Item  name="aiMessage">
					 <TextArea autoSize={{minRows:3}} placeholder="请输入系统设计的需求">
					 </TextArea>
					</Form.Item>
					)
				}
			
        <Form.Item>
          <Space size="large">
				{
					showAiChat&&(
					<Button type="primary" onLoad={(event)=>{
						return loading;
					}} onClick={ async (event)=>{
						  
					     await	doAiBuildSql()
						}}  style={{ width: 120 }}>
					  开始构建
					</Button>
					)
				}
            <Button type="primary" htmlType="submit" style={{ width: 120 }}>
              导入
            </Button>
            <Button htmlType="reset">重置</Button>
          </Space>
        </Form.Item>
				
      </Form>
    </Modal>
  );
};

export default SqlInput;
