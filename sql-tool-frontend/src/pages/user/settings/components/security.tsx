import React, { useState,useRef } from 'react';
import {
  ModalForm,
  ProForm,
  ProFormDateRangePicker,
  ProFormSelect,
  ProFormText,
	ProFormCaptcha,
} from '@ant-design/pro-components';
import { LockOutlined, UserOutlined,PlusOutlined } from '@ant-design/icons';
import { userSendEmailCode,getLoginUser,updateCurrentUser,validEmailCode } from '@/services/userService';
import { useRequest } from 'umi';
import { Button, Form, message,List } from 'antd';
import { Link } from '@@/exports';
import type { CaptFieldRef } from '@ant-design/pro-components';
import { CurrentUser } from '../data';
type Unpacked<T> = T extends (infer U)[] ? U : T;

const passwordStrength = {
  strong: <span className="strong">强</span>,
  medium: <span className="medium">中</span>,
  weak: <span className="weak">弱 Weak</span>,
};

const SecurityView: React.FC = () => {
	const codeBaseUrl_1 = "http://localhost:8102/api/user/checkCode?type=1"
		const [codeUrl, SetCodeUrl] = useState(codeBaseUrl_1);
		const captchaRef = useRef<CaptFieldRef | null | undefined>();
		  const formRef = useRef<any>();
		/**
		 * 校验邮箱验证码
		 */
		const validEmail= async(fields:UserType.UserEmailCodeRequest)=>{
			
		
			try{
				const  result= await validEmailCode(fields)
				return true;
			}catch(errorInfo){
				SetCodeUrl(codeBaseUrl_1 + "&time=" + new Date().getTime())
				message.error(errorInfo)
				return false;
			}
		}
		
		
		/**
		 * 发送邮件
		 */
		const doSendEmailCode = async()=>{
			const formValues = formRef.current?.getFieldsFormatValue?.();
			
			
			const email = formValues.email;
			
			const code = formValues.code;
			
			try {
				//校验图片验证码是否输入
				await formRef.current.validateFields(['code']);
				console.log('code验证通过');
				//校验邮箱是否合法
				await formRef.current.validateFields(['email']);
				console.log('email验证通过');
				const sendEmail = {
					code: code,
					email: email,
					type: 1
				};
				//调用
				await userSendEmailCode(sendEmail);
				message.success('已发送邮件到' + email);
				captchaRef.current?.startTiming();
				return true;
			} catch (errorInfo) {
				SetCodeUrl(codeBaseUrl_1 + "&time=" + new Date().getTime())
				captchaRef.current?.endTiming();
				message.error(errorInfo.message)
				return false;
			}
			
		}
	const { data: currentUser, loading } = useRequest(() => {
	  return getLoginUser();
	});
  const getData = () => [
    {
      title: '账户密码',
      description: (
        <>
          当前密码强度：
          {passwordStrength.strong}
        </>
      ),
      actions: [<a key="Modify"><Link to='/user/findback'>修改</Link></a>],
    },
    {
      title: '绑定邮箱',
      description: `已绑定邮箱：ant***sign.com`,
      actions: [	<ModalForm<UserType.UserEmailCodeRequest>
			    title="绑定邮箱"
					formRef={formRef}
					initialValues={{
							  ...currentUser
							}}
			    trigger={
			      <Button type="text">
			        修改
			      </Button>
			    }
		
			    autoFocusFirstInput
			    modalProps={{
			      destroyOnClose: true,
			      onCancel: () => console.log('run'),
			    }}
			    submitTimeout={2000}
			    onFinish={async (values) => {
			      // await waitTime(2000);
			      console.log(values);
					  let flag=	 await validEmail(values);
						if(flag){
							//更新用户信息
							 const userDto={
								 id:currentUser.id,
								 email:values.email
							 }
							 let isUpdate= await updateCurrentUser(userDto);
							 if(isUpdate.code===20000){
								  message.success('绑定成功');
									return true;
							 }else{
								 message.error('绑定失败')
							 }
						}else{
							message.error('绑定失败')
						}
			     
			      return false;
			    }}
			  >
			    <ProFormText
			    	name="email"
					
			    	fieldProps={{
			    		size: 'large',
			    		prefix: <LockOutlined className={'prefixIcon'} />,
			    	}}
			    	placeholder={'请输入邮箱'}
			    	rules={[
			    		{
			    			required: true,
			    			message: '请输入邮箱!',
			    		},
			    	]}
			    />
			    
			    <div className="container" 	style={{display: 'flex',alignItems:'center'}}>
			    	<ProFormText
			    		name="code"
			    		fieldProps={{
			    			size: 'large',
			    			prefix: <LockOutlined className={'prefixIcon'} />
			    		}}
			    		placeholder={'请输入图片验证码'}
			    		rules={[
			    			{
			    				required: true,
			    				message: '请输入图片验证码！',
			    			}
			    		]}
			    	/>
			    	<div onClick={() => {
			    		console.log('触发事件')
			    		SetCodeUrl(codeBaseUrl_1 + "&time=" + new Date().getTime())
			    	}}>
			    		<img src={codeUrl} style={{ pointerEvents: 'none',marginLeft:'10px',
							 marginBottom:'25px'}} />
			    	</div>
			    </div>
			    <ProFormCaptcha
			    	fieldRef={captchaRef}
					
			    	fieldProps={{
			    		size: 'large',
			    		prefix: <LockOutlined className={'prefixIcon'} />,
			    	}}
			    	captchaProps={{
			    		size: 'large',
			    	}}
			    	placeholder={'请输入验证码'}
			    	name="emailCode"
			    	rules={[
			    		{
			    			required: true,
			    			message: '请输入验证码！',
			    		},
			    	]}
			    	onGetCaptcha={async () => {
			    		//校验邮箱验证码
			    		doSendEmailCode()
			    	}}
			    />
			  </ModalForm>],
    }
  ];
 const [form] = Form.useForm<{ name: string; company: string }>();
  const data = getData();

  return (
    <>
      <List<Unpacked<typeof data>>
        itemLayout="horizontal"
        dataSource={data}
        renderItem={(item) => (
          <List.Item actions={item.actions}>
            <List.Item.Meta title={item.title} description={item.description} />
          </List.Item>
        )}	  
      />
		
			
    </>
  );
};

export default SecurityView;
