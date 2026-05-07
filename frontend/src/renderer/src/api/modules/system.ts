import request from '../request'
import type { ApiResult } from '@/types/api'
import type { Subject, SubjectReq } from '@/types/subject'
import type { UserCreateReq, UserUpdateReq, UserProfile, RoleType } from '@/types/user'

interface PageResult<T> {
  content: T[]
  totalElements: number
  totalPages: number
  size: number
  number: number
}

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

export function createUser(req: UserCreateReq) {
  return request.post<ApiResult<UserProfile>>('/api/v1/system/user', req).then(r => r.data.data)
}

export function updateUser(id: number, req: UserUpdateReq) {
  return request.put<ApiResult<UserProfile>>(`/api/v1/system/user/${id}`, req).then(r => r.data.data)
}

export function deleteUser(id: number) {
  return request.delete<ApiResult<void>>(`/api/v1/system/user/${id}`).then(r => r.data.data)
}

export function listUsers(params: { page?: number; size?: number; role?: RoleType }) {
  return request.get<ApiResult<PageResult<UserProfile>>>('/api/v1/system/users', { params }).then(r => r.data.data)
}
