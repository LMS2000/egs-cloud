import type { Settings as ProSettings } from '@ant-design/pro-layout';
import React from 'react';
import AvatarDropdown from './AvatarDropdown';
import styles from './index.less';
import { Link } from '@@/exports';
type GlobalHeaderRightProps = Partial<ProSettings>;

/**
 * 全局菜单右侧
 * @constructor
  * @author https://github.com/LMS2000
 */
const GlobalHeaderRight: React.FC<GlobalHeaderRightProps> = () => {
  return (
    <div className={styles.right}>
		<div style={{ fontSize: '10px',marginRight:'20px' }}>
		<Link to="/user/help">
		<img alt="帮助文档" src="https://gw.alipayobjects.com/zos/rmsportal/NbuDUAuBlIApFuDvWiND.svg" style={{ width: '14px', height: '14px' }} />{' '}
		帮助
		</Link>
		</div>
		
      <AvatarDropdown />
    </div>
  );
};

export default GlobalHeaderRight;
