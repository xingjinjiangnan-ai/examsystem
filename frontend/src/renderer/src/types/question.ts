export type QuestionType = 'SINGLE_CHOICE' | 'MULTI_CHOICE' | 'TRUE_FALSE' | 'FILL_BLANK' | 'SUBJECTIVE'

export interface SingleChoiceContent {
  type: 'SINGLE_CHOICE'
  stem: string
  options: string[]
  answer: string
  analysis?: string
}

export interface MultiChoiceContent {
  type: 'MULTI_CHOICE'
  stem: string
  options: string[]
  answer: string[]
  analysis?: string
}

export interface TrueFalseContent {
  type: 'TRUE_FALSE'
  stem: string
  answer: boolean
  analysis?: string
}

export interface FillBlankContent {
  type: 'FILL_BLANK'
  stem: string
  blanks: BlankItem[]
  analysis?: string
}

export interface BlankItem {
  position: number
  answers: Record<string, number>
  matchMode: string
  ignoreCase?: boolean
}

export interface SubjectiveContent {
  type: 'SUBJECTIVE'
  stem: string
  maxLength: number
  referenceAnswer?: string
  scoringGuide?: string
}

export type QuestionContent =
  | SingleChoiceContent
  | MultiChoiceContent
  | TrueFalseContent
  | FillBlankContent
  | SubjectiveContent

export interface QuestionVO {
  id: number
  type: QuestionType
  content: QuestionContent
  difficulty: number
  subjectId: number
  subjectName: string
  createdBy: number
  createdAt: string
  updatedAt: string
}

export interface QuestionCreateReq {
  type: QuestionType
  content: QuestionContent
  difficulty: number
  subjectId: number
}
