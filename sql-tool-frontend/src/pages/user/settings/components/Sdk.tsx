import React, { useRef } from 'react';
import { UploadOutlined } from '@ant-design/icons';
import { Button, Input, Upload, message,UploadProps } from 'antd';
import ProForm, {
  ProFormDependency,
  ProFormFieldSet,
  ProFormSelect,
  ProFormText,
  ProFormTextArea,
} from '@ant-design/pro-form';
import { useRequest } from 'umi';
import { uploadUserAvatar,getLoginUser,updateCurrentUser } from '@/services/userService';

import styles from './BaseView.less';


const SdkModel: React.FC = () => {
  const { data: currentUser, loading } = useRequest(() => {
    return getLoginUser();
  });
 
 
  const userFormRef=useRef<any>();

  
  // const handleFinish = async (fields: UserType.UserUpdateRequest) => {
		// 	fields.id=currentUser.id;
		// 	try{
		// 		 const result=  await updateCurrentUser(fields);
		// 		 if(result.code===20000){
		// 			  message.success('更新基本信息成功');
		// 		 }else {
		// 			 message.error('更新失败')
		// 		 }
				 
		// 	}catch(errorInfo){
		// 		message.error(errorInfo)
		// 	}
  
  // };
  return (
    <div className={styles.baseView}>
      {loading ? null : (
        <>
          <div className={styles.left}>
            <ProForm<UserType.UserUpdateRequest>
              layout="vertical"
	            formRef={userFormRef}
              onFinish={async (formData) => {
					// await handleFinish(formData);
				}}
							initialValues={{
							  ...currentUser
							}}
              submitter={{
                searchConfig: {
                  submitText: '刷新密钥',
                },
                render: (_, dom) => dom[1],
              }}
              hideRequiredMark
            >
						<ProFormText
                width="md"
                name="accessKey"
                label="公钥"
              /> 
            <ProFormText
                width="md"
                name="secretKey"
                label="私钥"
              /> 
          
             </ProForm>
          </div>			
        </>
      )}
    </div>
  );
};

export default SdkModel;
