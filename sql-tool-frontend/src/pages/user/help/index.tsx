import React, { useState, useEffect } from 'react';
import MarkdownEditor from "@uiw/react-markdown-editor";
import { Radio } from "antd"
import ReactMarkdown from 'react-markdown';
// 各种markdown引入，
import readmeMD from "./help.md";
import remarkGfm from 'remark-gfm';
 import 'github-markdown-css';
import "./index.less"
 const MarkdownViewer = () => {
   const [mdContent, setMdContent] = useState('');
 
   useEffect(() => {
 
     fetch(readmeMD)
       .then(response => response.text())
       .then(data => setMdContent(data))
       .catch(error => console.error('Error fetching the file:', error));
   }, []);
 
   return (
     <div
          style={{
  
            justifyContent: 'center',
            backgroundColor: 'white',
            padding: '0 50px',
          }}
        >
          <ReactMarkdown remarkPlugins={[remarkGfm]} className={"markdown-body"}>{mdContent}</ReactMarkdown>
        </div>
   );
 };
 export default MarkdownViewer;