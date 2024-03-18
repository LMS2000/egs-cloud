import { getLoginUser,uploadUserAvatar } from '@/services/userService';
import { Link, useModel } from '@umijs/max';
import { Button, Card, Empty, Input, message, Space, Upload, Avatar,  UploadProps } from 'antd';
import React, { useEffect, useState } from 'react';
import { ProCard } from '@ant-design/pro-components';
import { LockOutlined, UserOutlined } from '@ant-design/icons';
import './index.less';

// 默认分页大小
const DEFAULT_PAGE_SIZE = 10;

interface Props {
	title?: string;
	needLogin?: boolean;
	showTag?: boolean;
	userInfo?: UserType.User;
	onLoad?: (
		setUserInfo: (data: UserType.User) => void,
	) => void;
	onImport?: (values: TableInfoType.TableInfo) => void;
}

/**
 * 表信息卡片
 *
 * @constructor
  * @author https://github.com/LMS2000
 */
const ProfileCard: React.FC<Props> = (props) => {
	const { title = '个人信息', needLogin = false, userInfo = {}, showTag = true, onLoad, onImport } = props;

	// 公开数据
	const [userData, setUserData] = useState<UserType.User>();
	const [total, setTotal] = useState<number>(0);
	const [loading, setLoading] = useState<boolean>(true);
	const initSearchParams: TableInfoType.TableInfoQueryRequest = {
		current: 1,
		pageSize: DEFAULT_PAGE_SIZE,
		sortField: 'create_time',
		sortOrder: 'descend',
	};
	const [searchParams, setSearchParams] =
		useState<TableInfoType.TableInfoQueryRequest>(initSearchParams);

	const { initialState } = useModel('@@initialState');
	const loginUser = initialState?.loginUser;
	const [avatarUrl, setAvatarUrl] = useState('');
	/**
	 * 加载数据
	 */
	const innerOnLoad = () => {
		getLoginUser()
			.then((res) => {
				setUserData(res.data);
				setAvatarUrl(res.data.userAvatar)
			})
			.catch((e) => {
				message.error('加载失败，' + e.message);
			});
	};
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
					
								setAvatarUrl(res.data)
								console.log(avatarUrl)
							}else{
								message.error("上传失败！")
							}
      } catch (e: any) {
        message.error('操作失败，' + e.message);
      }
    },
  };
	// 加载数据
	useEffect(() => {
		// 需要登录
		if (needLogin && !loginUser) {
			return;
		}
		setLoading(true);

		innerOnLoad();
		setLoading(false);
	}, [searchParams]);

	return (
		<div className="table-info-card">
			<Card
				title={title}
			>
				{!needLogin || loginUser ? (
					<>

						<div style={{ display: 'flex', justifyContent: 'center', alignItems: 'center', flexDirection: 'column' }}>
							<Upload
								{...uploadProps}
							>
								<Avatar key={avatarUrl} size={128} src={avatarUrl} icon={<UserOutlined />} />
							</Upload>

							{
								userData ? userData.userName : 'username'
							}
							{
								userData ? (
									<>
									 <ProCard title="" colSpan="60%">
									   <div style={{ margin: '10px 0', fontSize: '20px' }}><span style={{ paddingRight: '10px' }}>用户名:</span>{userData.userName}</div>
									   <div style={{ margin: '10px 0', fontSize: '20px' }}><span style={{ paddingRight: '10px' }}>邮箱:</span>{userData.email}</div>
									   <div style={{ margin: '10px 0', fontSize: '20px' }}><span style={{ paddingRight: '10px' }}>性别:</span>{userData.gender}</div>
									   <div style={{ margin: '10px 0', fontSize: '20px' }}><span style={{ paddingRight: '10px' }}>创建时间:</span>{userData.createTime}</div>
									 </ProCard>
								
										
									</>

								) : (<>
								
								</>)
							}
						</div>
					</>
				) : (
					<Empty
						description={
							<Link to="/user/login">
								<Button type="primary" ghost style={{ marginTop: 8 }}>
									请先登录
								</Button>
							</Link>
						}
					/>
				)}
			</Card>
		</div>
	);
};

export default ProfileCard;
