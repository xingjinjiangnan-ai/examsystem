import request from '../request'
import type { ApiResult } from '@/types/api'
import type { Subject, SubjectReq } from '@/types/subject'

export function createSubject(req: SubjectReq) {
  return request.post<ApiResult<Subject>>('/api/v1/system/subject', req).then(r => r.data.data)
}

export function updateSubject(id: number, req: SubjectReq) {
  return request.put<ApiResult<Subject>>(`/api/v1/system/subject/${id}`, req).then(r => r.data.data)
}

export function deleteSubject(id: number) {
  return request.delete<ApiResult<void>>(`/api/v1/system/subject/${id}`).then(r => r.data.data)
}

export function listSubjects() {
  return request.get<ApiResult<Subject[]>>('/api/v1/system/subjects').then(r => r.data.data)
}
