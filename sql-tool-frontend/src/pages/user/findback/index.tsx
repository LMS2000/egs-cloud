import type { ProFormInstance } from '@ant-design/pro-components';
import {
  ProCard,
  ProForm,
  ProFormCheckbox,
  ProFormDatePicker,
  ProFormDateRangePicker,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
		ProFormCaptcha,
  StepsForm,
} from '@ant-design/pro-components';
import {  userSendEmailCode, validEmailCode,resetPassword } from '@/services/userService';
import './index.less';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import { message } from 'antd';
import type { CaptFieldRef } from '@ant-design/pro-components';
import { useState, useRef } from 'react';
import { Link } from '@@/exports';
const waitTime = (time: number = 100) => {
  return new Promise((resolve) => {
    setTimeout(() => {
      resolve(true);
    }, time);
  });
};

export default () => {
  const formRef = useRef<ProFormInstance>();
const codeBaseUrl_1 = "http://localhost:8102/api/user/checkCode?type=1"
// const codeBaseUrl_1 = "http://123.249.23.88:8102/api/user/checkCode?type=1"
	const [codeUrl, SetCodeUrl] = useState(codeBaseUrl_1);
	const captchaRef = useRef<CaptFieldRef | null | undefined>();
	
	
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
	 * 重置密码
	 */
	const doResetPassword= async(fields:UserType.UserResetPasswordRequest)=>{
			const formValues = formRef.current?.getFieldsFormatValue?.();
		await formRef.current.validateFields(['email']);
		await formRef.current.validateFields(['emailCode']);
		const email = formValues.email;
		const emailCode = formValues.emailCode;
		fields.emailCode=emailCode;
		fields.email=email;
		 try{
			 const result= await resetPassword(fields);
			 message.success("修改成功！")
			 return true;
		 }catch(errorInfo){
			 message.error(errorInfo)
			 return false;
		 }
	}
	
  return (
	
	<div style={{
			height: '87vh',
		
			background:
				'url(https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png)',
			backgroundSize: '60% 60%',
			padding: '8px 0 0px',
		}}>
	
	<ProCard style={{
			height: '87vh',
		  marginTop:'50px',
			background:
				'url(https://gw.alipayobjects.com/zos/rmsportal/FfdJeJRQWjEeGTpqgBKj.png)',
			backgroundSize: '60% 60%',
			padding: '8px 0 0px',
		}}>
	  <StepsForm<{
	    name: string;
	  }>
	    onFinish={async () => {
	      // await waitTime(1000);
	      message.success('修改成功');
				window.location.href = '/user/login'
	    }}
	    formProps={{
	      validateMessages: {
	        required: '此项为必填项',
	      },
	    }}
	  >
	    <StepsForm.StepForm<UserType.UserEmailCodeRequest>
	      name="base"
	      title="验证邮箱"
				formRef={formRef}
	      onFinish={async (formData) => {
	         let flag=await validEmail(formData);
	        return flag;
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
			 
			 <div className="container">
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
			 		<img src={codeUrl} style={{ pointerEvents: 'none' }} />
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
			 <Link to="/user/login" style={{
			 		float: 'right',
			 	}}>返回登录</Link>
	    </StepsForm.StepForm>
	    
	    <StepsForm.StepForm<{
				userPassword:string;
				checkPassword:string;
			}>
	      name="time"
	      title="重置密码"
	     onFinish={async (formData) => {
	       
	        const flag= await doResetPassword(formData);
					return flag;
	     }}
	    >
	   <ProFormText
	   	name="userPassword"
	   	fieldProps={{
	   		size: 'large',
	   		prefix: <LockOutlined className={'prefixIcon'} />,
	   	}}
	   	placeholder={'请输入新密码'}
	   	rules={[
	   		{
	   			required: true,
	   			message: '请输入新密码!',
	   		}, 
	   	]}
	   />
		 <ProFormText
		 	name="checkPassword"
		 	fieldProps={{
		 		size: 'large',
		 		prefix: <LockOutlined className={'prefixIcon'} />,
		 	}}
		 	placeholder={'请输入确认密码'}
		 	rules={[
		 		{
		 			required: true,
		 			message: '请输入确认密码!',
		 		},
		 	]}
		 />
	    </StepsForm.StepForm>
	  </StepsForm>
	</ProCard>
	
	</div>
   
  );
};