import React, { useState, useEffect } from 'react';
import { Input, Tree, Popover } from 'antd';
interface Props {
  tree: InvokeTree;
}
const SearchableTree : React.FC<Props> = (props) => {
	const tree=props.tree;
  const [expandedKeys, setExpandedKeys] = useState([]);
  const [searchValue, setSearchValue] = useState('');
  const [autoExpandParent, setAutoExpandParent] = useState(true);
  const [dataList,setDataList]=useState<any>([]);

  const getParentKey = (key, tree) => {
    let parentKey;
    for (let i = 0; i < tree.length; i++) {
      const node = tree[i];
      if (node.children) {
        if (node.children.some((item) => item.key === key)) {
          parentKey = node.key;
        } else {
          const parent = getParentKey(key, node.children);
          if (parent) {
            parentKey = parent;
          }
        }
      }
    }
    return parentKey;
  };

  const onExpand = (keys) => {
    setExpandedKeys(keys);
    setAutoExpandParent(false);
  };

  const generateList = (data, preKey) => {
    for (let i = 0; i < data.length; i++) {
      const node = data[i];
      const key = preKey + '-' + i;
      node.key = key;
      dataList.push(node);
      if (node.children) {
        generateList(node.children, key);
      }
    }
  };

  useEffect(() => {
    if (tree) {
      generateList(tree, '');
    }
  }, [tree]);

  useEffect(() => {
    const newExpandedKeys = dataList
      .map((item) => {
        if (item.title.indexOf(searchValue) > -1) {
          return getParentKey(item.key, tree);
        }
        return null;
      })
      .filter((item, i, self) => item && self.indexOf(item) === i);
    setExpandedKeys(newExpandedKeys);
    setAutoExpandParent(true);
  }, [searchValue]);

  return (
    <>
      <Input.Search
        value={searchValue}
        size="large"
        style={{ marginBottom: 16 }}
        placeholder="输入规则名搜索"
        enterButton
        onChange={(e) => setSearchValue(e.target.value)}
      />
      <Tree
        expandedKeys={expandedKeys}
        autoExpandParent={autoExpandParent}
        treeData={tree}
        onExpand={onExpand}
        showLine={true} // You can enable line for tree structure
        switcherIcon={<span />} // Customize the switcher icon if needed
        // Other Tree props you might want to use
      >
        {dataList.map((node) => (
          <Tree.TreeNode
            title={
              <Popover title="详情" placement="top" content={node.content}>
                {node.title.indexOf(searchValue) > -1 ? (
                  <>
                    {node.title.substring(0, node.title.indexOf(searchValue))}
                    <span style={{ color: '#f50' }}>{searchValue}</span>
                    {node.title.substring(node.title.indexOf(searchValue) + searchValue.length)}
                  </>
                ) : (
                  node.title
                )}
              </Popover>
            }
            key={node.key}
          >
            {node.children && node.children.length > 0
              ? node.children.map((child) => (
                  <Tree.TreeNode
                    title={
                      <Popover title="详情" placement="top" content={child.content}>
                        {child.title.indexOf(searchValue) > -1 ? (
                          <>
                            {child.title.substring(0, child.title.indexOf(searchValue))}
                            <span style={{ color: '#f50' }}>{searchValue}</span>
                            {child.title.substring(child.title.indexOf(searchValue) + searchValue.length)}
                          </>
                        ) : (
                          child.title
                        )}
                      </Popover>
                    }
                    key={child.key}
                  />
                ))
              : null}
          </Tree.TreeNode>
        ))}
      </Tree>
    </>
  );
};

export default SearchableTree;
