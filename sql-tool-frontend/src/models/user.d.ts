/**
 * 用户类型定义
 */
declare namespace UserType {
	type UserGenderEnum = 'MALE' | 'FEMALE';

	/**
	 * 实体
	 */
	interface User {
		id?: number;
		username?: string;
		nickname?: string;
		userAvatar?: string;
		gender?: UserGenderEnum;
		userRole?: string;
		email?: string;
		tags?: string[];
		profile?: string;
		userPassword?: string;
		createTime?: Date;
		updateTime?: Date;
	}

	/**
	 * 用户类型
	 */
	interface UserVO {
		id?: number;
		username?: string;
		nickname?: string;
		userAvatar?: string;
		gender?: UserGenderEnum;
		userRole?: string;
		email?: string;
		tags?: string[];
		profile?: string;
		userPassword?: string;
		createTime?: Date;
		updateTime?: Date;
	}

	/**
	 * 用户注册请求
	 */
	interface UserRegisterRequest {
		username: string;
		nickname: string;
		userPassword: string;
		checkPassword: string;
		email: string;
		emailCode: string;
	}
	/**
	 * 用户发送邮箱验证码请求
	 */
	interface UserSerndEmailCodeRequest {
		email: string;
		type: number;
		code: string;
	}


	/**
	 * 用户登录请求
	 */
	interface UserLoginRequest {
		account: string;
		userPassword: string;
		code: string;
	}

	/**
	 * 用户创建请求
	 */
	interface UserAddRequest {
		username: string;
		nickname: string;
		userAvatar?: string;
		gender?: UserGenderEnum;
		userRole: string;
		userPassword: string;
	}
	/**
	 * 用户校验邮箱验证码请求
	 */
	interface UserEmailCodeRequest {
		emailCode: string;
		email: string;
	}
	/**
	 * 用户修改密码请求
	 */
	interface UserResetPasswordRequest {
		emailCode: string;
		email: string;
		userPassword: string;
		checkPassword: string;
	}

	/**
	 * 用户删除请求
	 */
	interface UserDeleteRequest {
		id: number;
	}

	/**
	 * 用户更新请求
	 */
	interface UserUpdateRequest {
		id: number;
		username?: string;
		nickname?: string;
		userAvatar?: string;
		email?: string;
		tags?:string[]
		gender?: UserGenderEnum;
		userRole?: string;
	}

	/**
	 * 用户查询请求
	 */
	interface UserQueryRequest extends PageRequest {
		id?: number;
		username?: string;
		nickname?: string;
		userAvatar?: string;
		gender?: UserGenderEnum;
		userRole?: string;
		createTime?: Date;
		updateTime?: Date;
	}
}
