import React, { useEffect } from 'react';
import { List, Avatar } from 'antd';

const PostList = (props) => {
  const gege = '#'; // Assuming gege.jpg is in the same folder as this component

  useEffect(() => {
    // Do something when the component is mounted
  }, []);
  return (
    <List
      itemLayout="horizontal"
      dataSource={props.postList}
      renderItem={(item) => (
        <List.Item>
          <List.Item.Meta
            description={item.content}
            title={
              <a href="https://www.antdv.com/">{item.title}</a>
            }
            avatar={
              <Avatar src={'#'} />
            }
          />
        </List.Item>
      )}
    />
  );
};

export default PostList;
