import FileUploader from '@/components/FileUploader';
import PictureUploader from '@/components/PictureUploader';
import { COS_HOST } from '@/constants';
import FileInfoConfig from '@/components/FileModelInput/FileInfoConfig';
import ModelInfoConfig from '@/components/FileModelInput/ModelInfoConfig';
import FileConfigForm from '@/pages/generator/Add/components/FileConfigForm';
import GeneratorMaker from '@/pages/generator/Add/components/GeneratorMaker';
import ModelConfigForm from '@/pages/generator/Add/components/ModelConfigForm';
import {
	addGeneratorUsingPost,
	editGeneratorUsingPost,
	getGeneratorVoByIdUsingGet,
} from '@/services/generatorService';
import { useSearchParams } from '@@/exports';
import type { ProFormInstance } from '@ant-design/pro-components';
import {
	ProCard,
	ProFormSelect,
	ProFormText,
	ProFormTextArea,
	StepsForm,
} from '@ant-design/pro-components';
import { ProFormItem } from '@ant-design/pro-form';
import { history } from '@umijs/max';
import { message, UploadFile, Button, Alert } from 'antd';
import React, { useEffect, useRef, useState } from 'react';

/**
 * 创建生成器页面
 * @constructor
 */
const GeneratorAddPage: React.FC = () => {
	const [searchParams] = useSearchParams();
	const id = searchParams.get('id');
	const [oldData, setOldData] = useState<API.GeneratorEditRequest>();
	const formRef = useRef<ProFormInstance>();
	// 记录表单已填数据
	const [basicInfo, setBasicInfo] = useState<API.GeneratorEditRequest>();
	const [modelConfig, setModelConfig] = useState<API.ModelConfig>();
	const [fileConfig, setFileConfig] = useState<API.FileConfig>();
	const [inputModelConfigVisible, setInputModelConfigVisible] = useState<boolean>(false);
	const [inputFileConfigVisible, setInputFileConfigVisible] = useState<boolean>(false);



	/**
	 * 导入 文件配置
	 * @param fileConfig
	 */
	const importFileConfig = (fileConfig: API.FileConfig) => {
		formRef.current?.setFieldValue('fileConfig', fileConfig);
		setFileConfig(fileConfig);
		setInputFileConfigVisible(false);
		message.success('导入成功');
	};


	/**
	 * 导入模型配置
	 */
	const importModelConfig = (modelConfig: API.ModelConfig) => {
		formRef.current?.setFieldValue('modelConfig', modelConfig);
		setModelConfig(modelConfig);
		setInputModelConfigVisible(false);
		message.success('导入成功');
	}

	/**
	 * 加载数据
	 */
	const loadData = async () => {
		if (!id) {
			return;
		}
		try {
			const res = await getGeneratorVoByIdUsingGet({
				id,
			});

			// 处理文件路径
			if (res.data) {
				const { distPath } = res.data ?? {};
				if (distPath) {
					// @ts-ignore
					res.data.distPath = [
						{
							uid: id,
							name: '文件' + id,
							status: 'done',
							url: COS_HOST + distPath,
							response: distPath,
						} as UploadFile,
					];
				}
				setOldData(res.data);
			}
		} catch (error: any) {
			message.error('加载数据失败，' + error.message);
		}
	};

	useEffect(() => {
		if (id) {
			loadData();
		}
	}, [id]);

	/**
	 * 创建
	 * @param values
	 */
	const doAdd = async (values: API.GeneratorAddRequest) => {
		try {
			const res = await addGeneratorUsingPost(values);
			if (res.data) {
				message.success('创建成功');
				history.push(`/generator/detail/${res.data}`);
			}
		} catch (error: any) {
			message.error('创建失败，' + error.message);
		}
	};

	/**
	 * 更新
	 * @param values
	 */
	const doUpdate = async (values: API.GeneratorEditRequest) => {
		try {
			const res = await editGeneratorUsingPost(values);
			if (res.data) {
				message.success('更新成功');
				history.push(`/generator/detail/${id}`);
			}
		} catch (error: any) {
			message.error('更新失败，' + error.message);
		}
	};

	/**
	 * 提交
	 * @param values
	 */
	const doSubmit = async (values: API.GeneratorAddRequest) => {
		// 数据转换
		if (!values.fileConfig) {
			values.fileConfig = {};
		}
		if (!values.modelConfig) {
			values.modelConfig = {};
		}
		// 文件列表转 url
		if (values.distPath && values.distPath.length > 0) {
			// @ts-ignore
			values.distPath = values.distPath[0].response;
		}

		if (id) {
			await doUpdate({
				id,
				...values,
			});
		} else {
			await doAdd(values);
		}
	};

	return (
		<ProCard>
			{/* 创建或者已加载要更新的数据时，才渲染表单，顺利填充默认值 */}
			{(!id || oldData) && (
				<StepsForm<API.GeneratorAddRequest | API.GeneratorEditRequest>
					formRef={formRef}
					formProps={{
						initialValues: oldData,
					}}
					onFinish={doSubmit}
				>
					<StepsForm.StepForm
						name="base"
						title="基本信息"
						onFinish={async (values) => {
							setBasicInfo(values);
							return true;
						}}
					>
						<ProFormText name="name" label="名称" placeholder="请输入名称"
							rules={[
								{
									required: true,
									message: '请输入项目名称!',
								},
							]}
						/>
						<ProFormTextArea name="description" label="描述" placeholder="请输入描述" />
						<ProFormText name="basePackage" label="基础包" placeholder="请输入基础包"
							rules={[
								{
									required: true,
									message: '请输入基础包!',
								},
							]}
						/>
						<ProFormText name="version" label="版本" placeholder="请输入版本"
							rules={[
								{
									required: true,
									message: '请输入基础包!',
								},
							]}
						/>
						<ProFormText name="author" label="作者" placeholder="请输入作者"
							rules={[
								{
									required: true,
									message: '请输入作者!',
								},
							]}
						/>
						<ProFormSelect label="标签" mode="tags" name="tags" placeholder="请输入标签列表"
							rules={[
								{
									required: true,
									message: '请输入标签列表!',
								},
							]}
						/>
						<ProFormItem label="图片" name="picture">
							<PictureUploader biz="generator_picture" />
						</ProFormItem>
					</StepsForm.StepForm>
					<StepsForm.StepForm
						name="modelConfig"
						title="模型配置"
						onFinish={async (values) => {
							setModelConfig(values);
							return true;
						}}
					>
						<Button type="dashed" style={{ width: '500px' }} onClick={() => setInputModelConfigVisible(true)}>导入模型配置</Button>
						<div style={{ marginBottom: 16 }} />
						<ModelInfoConfig
							onSubmit={importModelConfig}
							visible={inputModelConfigVisible}
							onClose={() => setInputModelConfigVisible(false)}
						/>
						<ModelConfigForm formRef={formRef} oldData={oldData} />
					</StepsForm.StepForm>
					<StepsForm.StepForm
						name="fileConfig"
						title="文件配置"
						onFinish={async (values) => {
							setFileConfig(values);
							return true;
						}}
					>
						<Alert message="如果不需要使用在线制作功能，可不填写" type="warning" closable />
						<div style={{ marginBottom: 16 }} />
						<Button type="dashed" style={{ width: '500px' }} onClick={() => setInputFileConfigVisible(true)}>导入文件配置</Button>
						<div style={{ marginBottom: 16 }} />
						<FileInfoConfig
							onSubmit={importFileConfig}
							visible={inputFileConfigVisible}
							onClose={() => setInputFileConfigVisible(false)}
						/>
						<FileConfigForm formRef={formRef} oldData={oldData} />
					</StepsForm.StepForm>
					<StepsForm.StepForm name="dist" title="生成器文件">
						<ProFormItem label="产物包" name="distPath"
							rules={[
								{
									required: true,
									message: '请上传生成器文件!',
								},
							]}
						>
							<FileUploader biz="generator_dist" description="请上传生成器文件zip压缩包" />
						</ProFormItem>

						<GeneratorMaker
							meta={{
								...basicInfo,
								...modelConfig,
								...fileConfig,
							}}
						/>
					</StepsForm.StepForm>
				</StepsForm>
			)}
		</ProCard>
	);
};

export default GeneratorAddPage;
