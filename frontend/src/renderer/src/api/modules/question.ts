import request from '../request'
import type { ApiResult } from '@/types/api'
import type { QuestionCreateReq, QuestionType, QuestionVO } from '@/types/question'

interface PageResult<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

export function createQuestion(req: QuestionCreateReq) {
  return request.post<ApiResult<QuestionVO>>('/api/v1/question/', req).then(r => r.data.data)
}

export function getQuestion(id: number) {
  return request.get<ApiResult<QuestionVO>>(`/api/v1/question/${id}`).then(r => r.data.data)
}

export function listQuestions(params: {
  page?: number
  size?: number
  subjectId?: number
  type?: QuestionType
  difficulty?: number
}) {
  return request.get<ApiResult<PageResult<QuestionVO>>>('/api/v1/question/list', { params }).then(r => r.data.data)
}

export function updateQuestion(id: number, req: QuestionCreateReq) {
  return request.put<ApiResult<QuestionVO>>(`/api/v1/question/${id}`, req).then(r => r.data.data)
}

export function deleteQuestion(id: number) {
  return request.delete<ApiResult<void>>(`/api/v1/question/${id}`).then(r => r.data.data)
}
