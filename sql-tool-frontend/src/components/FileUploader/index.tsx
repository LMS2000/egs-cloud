import { uploadFileUsingPost } from '@/services/fileController';
import { InboxOutlined } from '@ant-design/icons';
import { message, UploadFile, UploadProps } from 'antd';
import Dragger from 'antd/es/upload/Dragger';
import React, { useState } from 'react';

interface Props {
	biz: string;
	onChange?: (fileList: UploadFile[]) => void;
	value?: UploadFile[];
	description?: string;
}

/**
 * 文件上传组件
 * @constructor
 */
const FileUploader: React.FC<Props> = (props) => {
	const { biz, value, description, onChange } = props;
	const [loading, setLoading] = useState(false);

	const uploadProps: UploadProps = {
		name: 'file',
		listType: 'text',
		multiple: false,
		maxCount: 1,
		fileList: value,
		disabled: loading,
		
		beforeUpload: (file) => {
			console.log(file.type)
			const isZip = file.type === 'application/x-zip-compressed';
			if (!isZip) {
				message.error("只能上传 ZIP 格式的文件");
				return false;
			}
			return true;
		},
		onChange: ({ fileList }) => {
		 if (fileList.every(file => file.type === 'application/x-zip-compressed')) {
		    console.log("触发")
		    onChange?.(fileList);
		  }
		},
		customRequest: async (fileObj: any) => {
			setLoading(true);
			try {
				const res = await uploadFileUsingPost(
					{
						biz,
					},
					{},
					fileObj.file,
				);
				fileObj.onSuccess(res.data);
			} catch (e: any) {
				message.error('上传失败，' + e.message);
				fileObj.onError(e);
			}
			setLoading(false);
		},
	};

	return (
		<Dragger {...uploadProps}>
			<p className="ant-upload-drag-icon">
				<InboxOutlined />
			</p>
			<p className="ant-upload-text">点击或拖拽文件上传</p>
			<p className="ant-upload-hint">{description}</p>
		</Dragger>
	);
};

export default FileUploader;
