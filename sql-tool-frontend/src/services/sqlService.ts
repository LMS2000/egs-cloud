import { request } from '@umijs/max';

/**
 * 根据 schema 生成
 * @param params
 */
export async function generateBySchema(params: TableSchema) {
  return request<ResultData<GenerateVO>>('/sql/generate/schema', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: params,
  });
}

/**
 * 智能获取 schema
 * @param params
 */
export async function getSchemaByAuto(params: GenerateByAutoRequest) {
  return request<ResultData<TableSchema>>('/sql/get/schema/auto', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: params,
  });
}

/** downloadGeneratorById GET /api/generator/download */
export async function downloadGeneratedCode(
  // 叠加生成的Param类型 (非body参数swagger默认没有生成对象)
  params:  GenerateBySqlRequest,
  options?: { [key: string]: any },
) {
  return request<any>('/sql/generate/code', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
		data: params,
    ...(options || {}),
  });
}


/**
 * 智能获取 建表SQL
 * @param params
 */
export async function getSqlByAi(params: AiCreateSqlRequest) {
  return request<ResultData<TableSchema>>('/sql/generate/ai', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: params,
  });
}

/**
 * 根据 SQL 获取 schema
 * @param params
 */
export async function getSchemaBySql(params: GenerateBySqlRequest) {
  return request<ResultData<TableSchema>>('/sql/get/schema/sql', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    data: params,
  });
}

/**
 * 根据 Excel 获取 schema
 * @param file
 */
export async function getSchemaByExcel(file: any) {
  const params = new FormData();
  params.append('file', file);
  return request<ResultData<TableSchema>>('/sql/get/schema/excel', {
    method: 'POST',
    headers: {
      'Content-Type': 'multipart/form-data',
    },
    data: params,
  });
}

/**
 * 下载模拟数据 Excel
 * @param params
 */
export async function downloadDataExcel(params: GenerateVO) {
  return request<BlobPart>('/sql/download/data/excel', {
    method: 'POST',
    headers: {
      'Content-Type': 'application/json',
    },
    responseType: 'blob',
    data: params,
  });
}
