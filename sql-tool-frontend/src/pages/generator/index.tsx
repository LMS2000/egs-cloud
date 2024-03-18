import './index.less';
import React, { useRef,useEffect,useState  } from 'react';
import { Row, Col, Button, Divider, Drawer,message } from 'antd';
import { GithubOutlined } from '@ant-design/icons';
import MonacoEditor from 'react-monaco-editor';
import { SQL_GENERATE_EXAMPLE, SQL_GENERATE_INIT_EXAMPLE } from '@/constants/examples';

import SearchableTree from '@/components/SearchableTree';
import {doGenerateSQL} from './generateUtil';
import { format } from "sql-formatter";
const generateSqlPage: React.FC = () => {
	
	 
	// const inputEditor = useRef<IStandaloneCodeEditor>(null);
	//  const outputEditor = useRef<IStandaloneCodeEditor>(null);
	 // const drawerVisible = useRef(false);
	  const inputEditorRef = useRef<any>(null);
	  const outputEditorRef = useRef<any>(null);
	  const invokeTreeRef = useRef<any>(null);
	  const drawerVisibleRef = useRef<boolean>(false);
	  const initJSONValue = JSON.stringify(SQL_GENERATE_INIT_EXAMPLE,null,2);
	   
	  const [inputEditorVal,setInputEditorVal]=useState<any>(initJSONValue);
	  const [outputEditorVal,setOutputEditorVal]=useState<any>(null);

	  const [drawerVisible, setDrawerVisible] = React.useState(false);

      const [invokeTree,setInvokeTree]=useState(null);
	 const getSQL = () => {
	      if (!inputEditorVal) {
					message.error('调用失败')
	        return;
	      }
		
	      const inputJSON = JSON.parse(inputEditorVal);
	      const generateResult = doGenerateSQL(inputJSON);
	      if (!generateResult) {
					message.error('生成失败')
	        return;
	      }
						try{
							let result = format(generateResult.resultSQL);
							// 针对执行引擎，处理自动格式化的问题
							result = result.replaceAll("{ {", "{{");
							result = result.replaceAll("} }", "}}");
							setOutputEditorVal(result);
							// 获取调用树
							setInvokeTree([generateResult.invokeTree]);
							console.log("调用树"+generateResult.invokeTree);
						}catch(errorInfo){
							message.error(errorInfo)
						}
	     
	    };
	
	    const importExampleAndCal = () => {
	     
	        const exampleJSON = JSON.stringify(SQL_GENERATE_EXAMPLE,null,2);
					setInputEditorVal(exampleJSON);
	
	    };
	
	    const showInvokeTree = () => {
	//       if (!invokeTreeRef.current) {
	//         getSQL();
	//       }
				setDrawerVisible(true);
	//       drawerVisibleRef.current = true;
	//     };
	
	//     if (inputEditorRef.current) {
	//       const initValue = localStorage.getItem("draft") ?? initJSONValue;
	//       inputEditorRef.current = monaco.editor.create(inputEditorRef.current, {
	//         value: initValue,
	//         language: "json",
	//         theme: "vs-dark",
	//         formatOnPaste: true,
	//         automaticLayout: true,
	//         fontSize: 16,
	//         minimap: {
	//           enabled: false,
	//         },
	//       });
	//       setTimeout(() => {
	//         if (inputEditorRef.current) {
	//           inputEditorRef.current.getAction("editor.action.formatDocument").run();
	//         }
	//       }, 500);
	//       setInterval(() => {
	//         if (inputEditorRef.current) {
	//           localStorage.setItem("draft", inputEditorRef.current?.getValue());
	//         }
	//       }, 3000);
	//     }
	
	//     if (outputEditorRef.current) {
	//       outputEditorRef.current = monaco.editor.create(outputEditorRef.current, {
	//         value: "",
	//         language: "sql",
	//         theme: "vs-dark",
	//         formatOnPaste: true,
	//         automaticLayout: true,
	//         fontSize: 16,
	//         minimap: {
	//           enabled: true,
	//         },
	//       });
	    }

 const handleEditorChange = (value: string) => {
    console.log(value);
		setInputEditorVal(value);
		// const exampleJSON = JSON.stringify(value,null,2);
		// setInputEditorVal(exampleJSON);
		
		// const inputJSON = JSON.parse(inputEditorRef.current?.getValue());
		// const generateResult = doGenerateSQL(inputJSON);
		// if (!generateResult) {
		//   return;
		// }
		// let result = format(generateResult.resultSQL);
		// // 针对执行引擎，处理自动格式化的问题
		// result = result.replaceAll("{ {", "{{");
		// result = result.replaceAll("} }", "}}");
		// outputEditorRef.current?.setValue(result);
		// // 获取调用树
		// invokeTreeRef.current = [generateResult.invokeTree];
		// console.log(imnvokeTreeRef.current);
  };

	return (
		<div>
			<Row justify="space-between" gutter={[0, 16]}>
				<h1 style={{ marginBottom: 0 }}></h1>
				
				<div style={{ margin: '0 0 9px' }}>
					<Button size="large" type="primary" onClick={getSQL}>
						生成 SQL
					</Button>
					<Button size="large" type="primary" ghost onClick={showInvokeTree}>
						查看调用树
					</Button>
					<Button size="large" type="default" onClick={importExampleAndCal}>
						导入例子
					</Button>
				</div>
			</Row>
			<div style={{ marginTop: '16px' }} />
			<Row gutter={[16, 16]}>
				<Col span={24} md={12}>
				
				 <div>
				  <MonacoEditor
				       width="90%"
				       height="70vh"
				       language="json"
				       theme="vs-dark"
							 options={{
							     fontSize: 20, // 设置字体大小
							   }}
								 onChange={handleEditorChange}
				       value={inputEditorVal}
				     />
				    </div>
				</Col>
				<Col span={24} md={12} >
					<div>
					   <MonacoEditor
					        width="90%"
					        height="70vh"
									options={{
									    fontSize: 20, // 设置字体大小
									  }}
									language="sql"
					        theme="vs-dark"
					        value={outputEditorVal}
					      />
					   </div>
				</Col>
			</Row>
			<br />
			
		
			<Drawer
				open={drawerVisible}
				title="调用树"
				placement="right"
				bodyStyle={{ width: '50vw' }}
				onClose={() => setDrawerVisible(false)}
			>
				<SearchableTree tree={invokeTree} />
			</Drawer>
		</div>
	);

};

export default generateSqlPage;