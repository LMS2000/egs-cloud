/**
 * 生成器点赞服务
 */
import { request } from '@umijs/max';

/** addGenerator POST /api/generator/add */
export async function doPostThumb(
  body: API.doThumbRequest,
  options?: { [key: string]: any },
) {
  return request<API.BaseResponseLong_>('/post/thumb', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: body,
    ...(options || {}),
  });
}