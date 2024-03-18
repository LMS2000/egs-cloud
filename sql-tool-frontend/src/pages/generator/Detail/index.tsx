import AuthorInfo from '@/pages/generator/Detail/components/AuthorInfo';
import FileConfig from '@/pages/generator/Detail/components/FileConfig';
import ModelConfig from '@/pages/generator/Detail/components/ModelConfig';
import {
	downloadGeneratorByIdUsingGet,
	getGeneratorVoByIdUsingGetWithStarAndFavour,
	deleteGeneratorUsingPost
} from '@/services/generatorService';
import {
	doPostThumb
} from '@/services/postThumbService';
import {
	doPostFavour
} from '@/services/postFavourService';
import { Link, useModel, useParams } from '@@/exports';
import { DownloadOutlined, EditOutlined, DeleteOutlined, LikeOutlined, MessageOutlined, StarOutlined } from '@ant-design/icons';
import { PageContainer } from '@ant-design/pro-components';
import { Button, Card, Col, Image, message, Row, Space, Tabs, Tag, Typography, Modal } from 'antd';
import { saveAs } from 'file-saver';
import moment from 'moment';
import './index.less';
import React, { useEffect, useState } from 'react';

const Thumb: React.FC<{
	text: number;
	isStared?: number; //点赞
	id?: String;
}> = ({ text, isStared = 0, id }) => {
	const [stared, setStared] = useState(isStared);
	const [num, setNum] = useState(text);
	console.log(text)
	const selectedStyle = { color: 'blue' }; // 自定义选中状态的样式
	const handleClickStared = async () => {
		console.log("触发点赞")
		// 发送请求实现点赞
		try {
			const result = await doPostThumb({
				generatorId: id
			});
			// console.log(result)
			setNum((prevNum) => (prevNum + Number(result.data)));
		} catch (e: any) {
			message.error('点赞失败，' + e.message);
		}
		setStared((prevStared) => (prevStared === 1 ? 0 : 1));
	};
	return (
		<span onClick={handleClickStared}>
			{stared === 1 ? (
				<LikeOutlined style={{ ...selectedStyle, marginRight: 8 }} />
			) : (
				<LikeOutlined style={{ marginRight: 8 }} />
			)}
			{num}
		</span>
	);
};

const IconText: React.FC<{
	type: string;
	text: number;
	isStared?: number; //点赞
	isFavoured?: number; // 收藏
	id?: String;
}> = ({ type, text, isStared = 0, isFavoured = 0, id }) => {
	const [favoured, setFavoured] = useState(isFavoured);
	const [stared, setStared] = useState(isStared);
	const [num, setNum] = useState(text);
	const handleClickFavoured = async () => {
		console.log("触发收藏")
		// 发送请求实现收藏

		try {
			const result = await doPostFavour({
				generatorId: id,
			});
			setNum((prevNum) => (prevNum + Number(result.data)));
			setFavoured((prevFavoured) => (prevFavoured === 1 ? 0 : 1));
		} catch (e: any) {
			message.error('收藏失败，' + e.message);
		}

		
	};
	const handleClickStared = async () => {
		console.log("触发点赞")
		// 发送请求实现点赞
		try {
			const result = await doPostThumb({
				generatorId: id
			});
			// console.log(result)
			setNum((prevNum) => (prevNum + Number(result.data)));
			setStared((prevStared) => (prevStared === 1 ? 0 : 1));
		} catch (e: any) {
			message.error('点赞失败，' + e.message);
		}
		
	};
	const selectedStyle = { color: 'blue' }; // 自定义选中状态的样式
	switch (type) {
		case 'favour-o':
			return (
				<span onClick={handleClickFavoured}>
					{favoured === 1 ? (
						<StarOutlined style={{ ...selectedStyle, marginRight: 8 }} />
					) : (
						<StarOutlined style={{ marginRight: 8 }} />
					)}
					{num}
				</span>
			);
		case 'thumb-o':
			return (
				<span onClick={handleClickStared}>
					{stared === 1 ? (
						<LikeOutlined style={{ ...selectedStyle, marginRight: 8 }} />
					) : (
						<LikeOutlined style={{ marginRight: 8 }} />
					)}
					{num}
				</span>
			);
		case 'message':
			return (
				<span>
					<MessageOutlined style={{ marginRight: 8 }} />
					{num}
				</span>
			);
		default:
			return null;
	}
};

/**
 * 生成器详情页
 * @constructor
 */
const GeneratorDetailPage: React.FC = () => {
	const { id } = useParams();

	const [loading, setLoading] = useState<boolean>(false);
	const [data, setData] = useState<API.GeneratorVO>({});
	const { initialState } = useModel('@@initialState');
	const loginUser = initialState?.loginUser;
	const my = loginUser?.id === data?.userId;

	// 在需要的地方定义一个函数用于显示确认框
	const showDeleteConfirm = (generatorId: any) => {
		Modal.confirm({
			title: '确认删除',
			content: '您确定要删除吗？',
			okText: '确认',
			cancelText: '取消',
			onOk() {
				handleDelete(generatorId); // 用户点击确认后执行删除操作
			},
			onCancel() {
				message.info('已取消删除操作');
			},
		});
	};

	/**
		 * 删除生成器
		 *
		 * @param id
		 */
	const handleDelete = async (generatorId: any) => {
		const hide = message.loading('正在删除');
		if (!generatorId) return true;
		try {
			await deleteGeneratorUsingPost({
				id: generatorId,
			});
			hide();
			message.success('删除成功');
			// actionRef?.current?.reload(); 跳转到列表
			return true;
		} catch (error: any) {
			hide();
			message.error('删除失败，' + error.message);
			return false;
		}
	};

	/**
	 * 加载数据
	 */
	const loadData = async () => {
		if (!id) {
			return;
		}
		setLoading(true);
		try {
			const res = await getGeneratorVoByIdUsingGetWithStarAndFavour({
				id,
			});
			setData(res.data || {});
		} catch (error: any) {
			message.error('获取数据失败，' + error.message);
		}
		setLoading(false);
	};

	useEffect(() => {
		loadData();
	}, [id]);

	/**
	 * 标签列表视图
	 * @param tags
	 */
	const tagListView = (tags?: string[]) => {
		if (!tags) {
			return <></>;
		}

		return (
			<div style={{ marginBottom: 8 }}>
				{tags.map((tag: string) => {
					return <Tag key={tag}>{tag}</Tag>;
				})}
			</div>
		);    
	};

	/**
	 * 下载按钮
	 */
	const downloadButton = data.distPath && loginUser && (
		<Button
			icon={<DownloadOutlined />}
			onClick={async () => {
				const blob = await downloadGeneratorByIdUsingGet(
					{
						id: data.id,
					},
					{
						responseType: 'blob',
					},
				);
				// 使用 file-saver 来保存文件
				const fullPath = data.distPath || '';
				saveAs(blob, fullPath.substring(fullPath.lastIndexOf('/') + 1));
			}}
		>
			下载
		</Button>
	);

	/**
	 * 编辑按钮
	 */
	const editButton = my && (
		<Link to={`/generator/update?id=${data.id}`}>
			<Button icon={<EditOutlined />}>编辑</Button>
		</Link>
	);

	/**
	 * 删除按钮
	 */
	const deldeteButton = my && (
		<Button icon={<DeleteOutlined />} className="redButton" onClick={async () => {
			showDeleteConfirm(data.id)
		}}>删除</Button>
	);
	return (
		<PageContainer title={<></>} loading={loading}>
			<Card>
				<Row justify="space-between" gutter={[32, 32]}>
					<Col flex="auto">
						<Space size="large" align="center">
							<Typography.Title level={4}>{data.name}</Typography.Title>
							{tagListView(data.tags)}
						</Space>
						<Typography.Paragraph>{data.description}</Typography.Paragraph>
						<Typography.Paragraph type="secondary">
							创建时间：{moment(data.createTime).format('YYYY-MM-DD hh:mm:ss')}
						</Typography.Paragraph>
						<Typography.Paragraph type="secondary">基础包：{data.basePackage}</Typography.Paragraph>
						<Typography.Paragraph type="secondary">版本：{data.version}</Typography.Paragraph>
						<Typography.Paragraph type="secondary">作者：{data.author}</Typography.Paragraph>

						<div style={{ display: 'flex', gap: '10px' }}>
				      {data&& <Thumb   text={data.thumbNum} id={data.id} isStared={data.stared} />}
				     {data&& <IconText key="favour" type="favour-o" text={data.favourNum} id={data.id} isFavoured={data.favoured} /> }
						</div>
						<div style={{ marginBottom: 24 }} />
						<Space size="middle">

							<Link to={`/generator/use/${data.id}`}>
								<Button type="primary">立即使用</Button>
							</Link>
							{downloadButton}
							{editButton}
							{deldeteButton}
						</Space>
					</Col>
					<Col flex="320px">
						<Image src={data.picture} />
					</Col>
				</Row>
			</Card>
			<div style={{ marginBottom: 24 }} />
			<Card>
				<Tabs
					size="large"
					defaultActiveKey={'fileConfig'}
					onChange={() => { }}
					items={[
						{
							key: 'fileConfig',
							label: '文件配置',
							children: <FileConfig data={data} />,
						},
						{
							key: 'modelConfig',
							label: '模型配置',
							children: <ModelConfig data={data} />,
						},
						{
							key: 'userInfo',
							label: '作者信息',
							children: <AuthorInfo data={data} />,
						},
					]}
				/>
			</Card>
		</PageContainer>
	);
};

export default GeneratorDetailPage;
