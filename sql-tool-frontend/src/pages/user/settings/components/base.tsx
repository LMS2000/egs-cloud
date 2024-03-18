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


const BaseView: React.FC = () => {
  const { data: currentUser, loading } = useRequest(() => {
    return getLoginUser();
  });
 /**
    * 头像上传组件属性
    */
   const uploadProps: UploadProps = {
     name: 'file',
     showUploadList: false,
     customRequest: async (options) => {
       if (!options) {
         return;
       }
       try {
         const res = await uploadUserAvatar(options.file);
               if(res.code===20000){
 								message.success("上传成功！")
								currentUser.userAvatar=res.data
 							}else{
 								message.error("上传失败！")
 							}
       } catch (e: any) {
         message.error('操作失败，' + e.message);
       }
     },
   };
 
 
 // 头像组件 方便以后独立，增加裁剪之类的功能
 const AvatarView = ({ avatar }: { avatar: string }) => (
   <>
     <div className={styles.avatar_title}>头像</div>
     <div className={styles.avatar}>
       <img src={avatar} alt="avatar" />
     </div>
     <Upload  	{...uploadProps}>
       <div className={styles.button_view}>
         <Button>
           <UploadOutlined />
           更换头像
         </Button>
       </div>
     </Upload>
   </>
 );
 
    const userFormRef=useRef<any>();
  const getAvatarURL = () => {
    if (currentUser) {
      if (currentUser.userAvatar) {
        return currentUser.userAvatar;
      }
      const url = 'https://gw.alipayobjects.com/zos/rmsportal/BiazfanxmamNRoxxVxka.png';
      return url;
    }
    return '';
  };
  
  const handleFinish = async (fields: UserType.UserUpdateRequest) => {
			fields.id=currentUser.id;
			try{
				 const result=  await updateCurrentUser(fields);
				 if(result.code===20000){
					  message.success('更新基本信息成功');
				 }else {
					 message.error('更新失败')
				 }
				 
			}catch(errorInfo){
				message.error(errorInfo)
			}
  
  };
  return (
    <div className={styles.baseView}>
      {loading ? null : (
        <>
          <div className={styles.left}>
            <ProForm<UserType.UserUpdateRequest>
              layout="vertical"
	            formRef={userFormRef}
              onFinish={async (formData) => {
					await handleFinish(formData);
				}}
							initialValues={{
							  ...currentUser
							}}
              submitter={{
                searchConfig: {
                  submitText: '更新基本信息',
                },
                render: (_, dom) => dom[1],
              }}
              hideRequiredMark
            >
            
              <ProFormText
                width="md"
                name="nickname"
                label="昵称"
                rules={[
                  {
                    required: true,
                    message: '请输入您的昵称!',
                  },
                ]}
              />  <ProFormTextArea
                name="profile"
                label="个人简介"
                rules={[
                  {
                    required: true,
                    message: '请输入个人简介!',
                  },
                ]}
                placeholder="个人简介"
              />
           
             </ProForm>
          </div>
          <div className={styles.right}>
            <AvatarView avatar={getAvatarURL()} />
          </div>
					
        </>
      )}
    </div>
  );
};

export default BaseView;
