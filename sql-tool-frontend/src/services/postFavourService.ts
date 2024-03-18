/**
 * 生成器收藏服务
 */
import { request } from '@umijs/max';

/** addGenerator POST /api/generator/add */
export async function doPostFavour(
  body: API.doFavourRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/post/favour', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}


/** addGenerator POST /api/generator/add */
export async function queryMyGenratorPage(
  body: API.queryFavourRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponsePageGeneratorVO_>('/post/favour/my/list/page', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}