import { PlusOutlined, HomeOutlined, ContactsOutlined, ClusterOutlined } from '@ant-design/icons';
import { Avatar, Card, Col, Divider, Input, Row, Tag, message } from 'antd';
import React, { useState, useRef } from 'react';
import { GridContent } from '@ant-design/pro-layout';
import { Link, useRequest } from 'umi';
import type { RouteChildrenProps } from 'react-router';
import Projects from './components/Projects';
import type { CurrentUser, TagType, tabKeyType } from './data.d';
import { queryCurrent } from './service';
import { updateCurrentUser } from '@/services/userService';
import styles from './Center.less';
import { useModel } from '@umijs/max';
import FavourProjects from './components/FavourProjects';
const operationTabList = [
	{
		key: 'projects',
		tab: (
			<span>
				项目 <span style={{ fontSize: 14 }}></span>
			</span>
		),
	},
	{
		key: 'favours',
		tab: (
			<span>
				收藏 <span style={{ fontSize: 14 }}></span>
			</span>
		),
	},
];

const TagList: React.FC<{ tags: String[] }> = ({ tags }) => {
	const ref = useRef<Input | null>(null);
	const [newTags, setNewTags] = useState<String[]>([]);
	const [delTags, setDelTags] = useState<String[]>([]);
	const [inputVisible, setInputVisible] = useState<boolean>(false);
	const [inputValue, setInputValue] = useState<string>('');

	const showInput = () => {
		setInputVisible(true);
		if (ref.current) {
			// eslint-disable-next-line no-unused-expressions
			ref.current?.focus();
		}
	};

	const handleInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
		setInputValue(e.target.value);
	};

	const handleTagClose = async (deleteTag: string) => {
		let tempsTags = [...delTags];
		if (deleteTag && tempsTags.filter((tag) => tag === deleteTag).length === 0) {
			tempsTags = [...tempsTags, deleteTag];
		}
		// 设置
		setDelTags(tempsTags);
		console.log(tempsTags)
		const combinedArray = [...newTags, ...tags];
		const filteredArray = combinedArray.filter(tag => !tempsTags.includes(tag));
		try {
			await updateCurrentUser({ "tags": filteredArray })
		} catch (error: any) {
			message.error(error)
		}
	}
	const handleInputConfirm = async () => {
		let tempsTags = [...newTags];
		if (inputValue && tempsTags.filter((tag) => tag === inputValue).length === 0) {
			tempsTags = [...tempsTags, inputValue];
		}
		setNewTags(tempsTags);
		// 发送请求
		try {
			console.log(tempsTags)
			console.log(tags)
			const combinedArray = [...tempsTags, ...tags];
			const filteredArray = combinedArray.filter(tag => !delTags.includes(tag));
			const res = await updateCurrentUser({ "tags": filteredArray })

		} catch (error: any) {
			message.error(error)
		}
		setInputVisible(false);
		setInputValue('');
	};

	return (
		<div className={styles.tags}>
			<div className={styles.tagsTitle}>标签</div>
			{(tags || []).concat(newTags || []).map((tag, index) => (
				<Tag closable onClose={() => handleTagClose(tag)} key={index}>{tag}</Tag>
			))}
			{inputVisible && (
				<Input
					ref={ref}
					type="text"
					size="small"
					style={{ width: 78 }}
					value={inputValue}
					onChange={handleInputChange}
					onBlur={handleInputConfirm}
					onPressEnter={handleInputConfirm}
				/>
			)}
			{!inputVisible && (
				<Tag onClick={showInput} style={{ borderStyle: 'dashed' }}>
					<PlusOutlined />
				</Tag>
			)}
		</div>
	);
};

const Center: React.FC<RouteChildrenProps> = () => {
	const [tabKey, setTabKey] = useState<tabKeyType>('projects');

	//  获取用户信息
	const { initialState } = useModel('@@initialState');
	const loginUser = initialState?.loginUser;

	//  渲染用户信息
	const renderUserInfo = (profile: String) => {
		return (
			<div className={styles.detail}>
				<p>
					<ContactsOutlined
						style={{
							marginRight: 8,
						}}
					/>
					{profile}
				</p>
			</div>
		);
	};

	// 渲染tab切换
	const renderChildrenByTabKey = (tabValue: tabKeyType) => {
		if (tabValue === 'projects') {
			return <Projects />;
		}
		if (tabValue === 'favours') {
			return <FavourProjects />;
		}
		return null;
	};

	return (
		<GridContent>
			<Row gutter={24}>
				<Col lg={7} md={24}>
					<Card bordered={false} style={{ marginBottom: 16 }} >
						{loginUser && (
							<div>
								<div className={styles.avatarHolder}>
									<img alt="" src={loginUser.userAvatar} />
									<div className={styles.name}>{loginUser.nickname}</div>
									<div>{loginUser?.email}</div>
								</div>
								{renderUserInfo(loginUser.profile)}
								<Divider dashed />
								<TagList tags={loginUser.tags || []} />
								<Divider style={{ marginTop: 16 }} dashed />
							</div>
						)}
					</Card>
				</Col>
				<Col lg={17} md={24}>
					<Card
						className={styles.tabsCard}
						bordered={false}
						tabList={operationTabList}
						activeTabKey={tabKey}
						onTabChange={(_tabKey: string) => {
							setTabKey(_tabKey as tabKeyType);
						}}
					>
						{renderChildrenByTabKey(tabKey)}
					</Card>
				</Col>
			</Row>
		</GridContent>
	);
};
export default Center;
