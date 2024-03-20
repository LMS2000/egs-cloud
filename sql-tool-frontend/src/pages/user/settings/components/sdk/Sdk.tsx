import React, { useRef, useState, useEffect } from 'react';
import { UploadOutlined } from '@ant-design/icons';
import { Button, Input, Upload, message, UploadProps, Space } from 'antd';
import ProForm, {
	ProFormDependency,
	ProFormFieldSet,
	ProFormSelect,
	ProFormText,
	ProFormTextArea
} from '@ant-design/pro-form';
import { useRequest } from 'umi';
import { getLoginUser, flushKeys } from '@/services/userService';
import './index.less';
import styles from '../BaseView.less';


const SdkModel: React.FC = () => {
	const [currentUser, setCurrentUser] = useState(null);
	const [loading, setLoading] = useState(false);

	const doGetLoginUser = async () => {
		setLoading(true);
		try {
			const res = await getLoginUser();
			setCurrentUser(res.data);

		} catch (error: any) {
			message.error('获取数据失败，' + error.message);
		}
		setLoading(false);
	};

	useEffect(() => {
		doGetLoginUser();
	}, [])
	const userFormRef = useRef<any>();

	// useEffect(() => {
	// 	doGetLoginUser();
	// }, [currentUser]);

	return (
		<div className={styles.baseView}>
		
			{loading ? null : (
				<>
					<div className={styles.left}>
						<ProForm<UserType.UserUpdateRequest>
							layout="vertical"
							formRef={userFormRef}
							onFinish={async () => {
								await flushKeys();
								doGetLoginUser()

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
								disabled
								className="blueText"
							/>
							<ProFormText
								width="md"
								name="secretKey"
								label="私钥"
								className="blueText"
								disabled
							/>
							<Space.Compact style={{ width: '100%', marginBottom: '10px' }}>
								<Input defaultValue="https://github.com/LMS2000/sql-generator" disabled />
								<a href="https://github.com/LMS2000/sql-generator">
									<Button type="primary">跳转下载</Button>
								</a>
							</Space.Compact>
							<Space.Compact style={{ width: '100%', marginBottom: '10px' }}>
								<Input defaultValue="https://github.com/LMS2000/lmszi-generator-maker" disabled />
								<a href="https://github.com/LMS2000/lmszi-generator-maker">
									<Button type="primary">跳转下载</Button>
								</a>
							</Space.Compact>
							<Space.Compact style={{ marginBottom: '10px',marginRight:'10px' }}>
								<a href="	https://service-edu-2000.oss-cn-hangzhou.aliyuncs.com/sql-generator/sql-generator-v1.0.jar">
									<Button type="primary">下载</Button>
								</a>
							</Space.Compact>
						</ProForm>

					</div>
				</>
			)}
		</div>
	);
};

export default SdkModel;
