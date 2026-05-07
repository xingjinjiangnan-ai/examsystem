<script setup lang="ts">
import { ref, computed, watch } from "vue";
import type {
  QuestionType,
  QuestionCreateReq,
  QuestionContent,
} from "@/types/question";
import type { Subject } from "@/types/subject";
import { useToast } from "@/composables/useToast";

const toast = useToast();

const props = defineProps<{
  subjects: Subject[];
  initialData?: QuestionCreateReq;
}>();

const emit = defineEmits<{
  submit: [req: QuestionCreateReq];
}>();

const type = ref<QuestionType>("SINGLE_CHOICE");
const subjectId = ref<number | undefined>(undefined);
const difficulty = ref(3);
const stem = ref("");
const analysis = ref("");

// Single/Multi choice
const options = ref<string[]>(["", "", "", ""]);
const singleAnswer = ref<string | null>(null);
const multiAnswers = ref<string[]>([]);

// True/False
const tfAnswer = ref<boolean>(true);

// Fill blank
const blanks = ref<
  Array<{
    position: number;
    answers: Array<{ text: string; maxLength: number }>;
    matchMode: string;
    ignoreCase: boolean;
  }>
>([{ position: 1, answers: [{ text: "", maxLength: 100 }], matchMode: "EXACT", ignoreCase: false }]);

// Subjective
const maxLength = ref(500);
const referenceAnswer = ref("");
const scoringGuide = ref("");

const questionTypeMap: Record<QuestionType, string> = {
  SINGLE_CHOICE: "单选题",
  MULTI_CHOICE: "多选题",
  TRUE_FALSE: "判断题",
  FILL_BLANK: "填空题",
  SUBJECTIVE: "主观题",
};

function resetForm() {
  type.value = "SINGLE_CHOICE";
  subjectId.value = props.subjects[0]?.id;
  difficulty.value = 3;
  stem.value = "";
  analysis.value = "";
  options.value = ["", "", "", ""];
  singleAnswer.value = null;
  multiAnswers.value = [];
  tfAnswer.value = true;
  blanks.value = [
    { position: 1, answers: [{ text: "", maxLength: 100 }], matchMode: "EXACT", ignoreCase: false },
  ];
  maxLength.value = 500;
  referenceAnswer.value = "";
  scoringGuide.value = "";
}

function buildContent(): QuestionContent | null {
  if (!stem.value.trim()) return null;
  switch (type.value) {
    case "SINGLE_CHOICE":
      const validOptions = options.value.filter((o) => o.trim());
      if (validOptions.length < 2 || !singleAnswer.value) return null;
      return {
        type: "SINGLE_CHOICE",
        stem: stem.value.trim(),
        options: validOptions,
        answer: singleAnswer.value!,
        analysis: analysis.value.trim() || undefined,
      };
    case "MULTI_CHOICE":
      const validMultiOptions = options.value.filter((o) => o.trim());
      if (validMultiOptions.length < 2 || multiAnswers.value.length === 0)
        return null;
      return {
        type: "MULTI_CHOICE",
        stem: stem.value.trim(),
        options: validMultiOptions,
        answer: multiAnswers.value,
        analysis: analysis.value.trim() || undefined,
      };
    case "TRUE_FALSE":
      return {
        type: "TRUE_FALSE",
        stem: stem.value.trim(),
        answer: tfAnswer.value,
        analysis: analysis.value.trim() || undefined,
      };
    case "FILL_BLANK":
      const validBlanks = blanks.value.filter((b) =>
        b.answers.some((a) => a.text.trim()),
      );
      if (validBlanks.length === 0) return null;
      return {
        type: "FILL_BLANK",
        stem: stem.value.trim(),
        blanks: validBlanks.map((b, i) => ({
          position: i + 1,
          answers: Object.fromEntries(
            b.answers
              .filter((a) => a.text.trim())
              .map((a) => [a.text.trim(), a.maxLength]),
          ),
          matchMode: b.matchMode,
          ignoreCase: b.ignoreCase,
        })),
        analysis: analysis.value.trim() || undefined,
      };
    case "SUBJECTIVE":
      if (!maxLength.value || maxLength.value < 1) return null;
      return {
        type: "SUBJECTIVE",
        stem: stem.value.trim(),
        maxLength: maxLength.value,
        referenceAnswer: referenceAnswer.value.trim() || undefined,
        scoringGuide: scoringGuide.value.trim() || undefined,
      };
  }
}

const canSubmit = computed(() => {
  if (!subjectId.value || !stem.value.trim()) return false;
  const content = buildContent();
  return content !== null;
});

function validate(): string | null {
  if (!subjectId.value) return "请选择科目";
  if (!stem.value.trim()) return "请填写题干";
  switch (type.value) {
    case "SINGLE_CHOICE": {
      const validOptions = options.value.filter((o) => o.trim());
      if (validOptions.length < 2) return "单选题至少需要2个有效选项";
      if (!singleAnswer.value) return "请选择单选题的正确答案";
      return null;
    }
    case "MULTI_CHOICE": {
      const validOptions = options.value.filter((o) => o.trim());
      if (validOptions.length < 2) return "多选题至少需要2个有效选项";
      if (multiAnswers.value.length === 0) return "请至少选择一个正确答案";
      return null;
    }
    case "TRUE_FALSE": {
      return null;
    }
    case "FILL_BLANK": {
      const validBlanks = blanks.value.filter((b) =>
        b.answers.some((a) => a.text.trim()),
      );
      if (validBlanks.length === 0) return "至少需要1个有效填空位置";
      for (let i = 0; i < validBlanks.length; i++) {
        const b = validBlanks[i];
        if (b.answers.some((a) => !a.maxLength || a.maxLength < 1)) {
          return `填空 ${i + 1} 中存在最大字数未填写或不合法`;
        }
      }
      return null;
    }
    case "SUBJECTIVE": {
      if (!maxLength.value || maxLength.value < 1) return "请填写最大字数限制（至少1）";
      return null;
    }
  }
}

function handleSubmit() {
  const err = validate();
  if (err) {
    toast.error(err);
    return;
  }
  const content = buildContent()!;
  emit("submit", {
    type: type.value,
    content,
    difficulty: difficulty.value,
    subjectId: subjectId.value!,
  });
}

function addOption() {
  options.value.push("");
}

function removeOption(idx: number) {
  if (options.value.length <= 2) return;
  const removed = options.value[idx];
  options.value.splice(idx, 1);
  if (singleAnswer.value === removed) singleAnswer.value = null;
  multiAnswers.value = multiAnswers.value.filter((a) => a !== removed);
}

function addBlank() {
  blanks.value.push({
    position: blanks.value.length + 1,
    answers: [{ text: "", maxLength: 100 }],
    matchMode: "EXACT",
    ignoreCase: false,
  });
}

function removeBlank(idx: number) {
  if (blanks.value.length <= 1) return;
  blanks.value.splice(idx, 1);
}

function addBlankAnswer(blankIdx: number) {
  blanks.value[blankIdx].answers.push({ text: "", maxLength: 100 });
}

function removeBlankAnswer(blankIdx: number, ansIdx: number) {
  if (blanks.value[blankIdx].answers.length <= 1) return;
  blanks.value[blankIdx].answers.splice(ansIdx, 1);
}

watch(
  () => props.initialData,
  (data) => {
    if (data) {
      type.value = data.type;
      subjectId.value = data.subjectId;
      difficulty.value = data.difficulty;
      const c = data.content;
      if ("stem" in c) stem.value = c.stem;
      if ("analysis" in c && c.analysis) analysis.value = c.analysis;
      if (data.type === "SINGLE_CHOICE") {
        options.value = [...c.options];
        singleAnswer.value = c.answer || null;
      }
      if (data.type === "MULTI_CHOICE") {
        options.value = [...c.options];
        multiAnswers.value = [...c.answer];
      }
      if (data.type === "TRUE_FALSE") {
        tfAnswer.value = c.answer;
      }
      if (data.type === "FILL_BLANK") {
        blanks.value = c.blanks.map((b) => ({
          position: b.position,
          answers: Object.entries(b.answers).map(([text, maxLen]) => ({
            text,
            maxLength: Number(maxLen),
          })),
          matchMode: b.matchMode,
          ignoreCase: b.ignoreCase,
        }));
      }
      if (data.type === "SUBJECTIVE") {
        maxLength.value = Number(c.maxLength) || 500;
        referenceAnswer.value = c.referenceAnswer || "";
        scoringGuide.value = c.scoringGuide || "";
      }
    } else {
      resetForm();
    }
  },
  { immediate: true },
);

defineExpose({ resetForm, submit: handleSubmit });
</script>

<template>
  <form class="space-y-4" @submit.prevent="handleSubmit">
    <!-- Type & Subject & Difficulty -->
    <div class="grid grid-cols-3 gap-4">
      <div class="form-control">
        <label class="label"
          ><span class="label-text text-sm">题型</span></label
        >
        <select v-model="type" class="select select-bordered w-full rounded-lg">
          <option value="SINGLE_CHOICE">单选题</option>
          <option value="MULTI_CHOICE">多选题</option>
          <option value="TRUE_FALSE">判断题</option>
          <option value="FILL_BLANK">填空题</option>
          <option value="SUBJECTIVE">主观题</option>
        </select>
      </div>
      <div class="form-control">
        <label class="label"
          ><span class="label-text text-sm">科目</span></label
        >
        <select
          v-model="subjectId"
          class="select select-bordered w-full rounded-lg"
        >
          <option v-for="s in subjects" :key="s.id" :value="s.id">
            {{ s.name }}
          </option>
        </select>
      </div>
      <div class="form-control">
        <label class="label"
          ><span class="label-text text-sm">难度 (1-5)</span></label
        >
        <select
          v-model="difficulty"
          class="select select-bordered w-full rounded-lg"
        >
          <option v-for="n in 5" :key="n" :value="n">{{ n }}</option>
        </select>
      </div>
    </div>

    <!-- Stem -->
    <div class="form-control">
      <label class="label"><span class="label-text text-sm">题干</span></label>
      <textarea
        v-model="stem"
        class="textarea textarea-bordered w-full rounded-lg"
        rows="3"
        placeholder="请输入题目内容"
      />
    </div>

    <!-- Single Choice -->
    <template v-if="type === 'SINGLE_CHOICE'">
      <div class="space-y-2">
        <label class="label-text text-sm">选项（至少2项）</label>
        <div
          v-for="(opt, idx) in options"
          :key="idx"
          class="flex items-center gap-2"
        >
          <div class="flex-1">
            <input
              v-model="options[idx]"
              type="text"
              class="input flex-1 rounded-lg w-full"
              :class="options.filter((o, i) => i !== idx && o.trim() && o.trim() === opt.trim()).length ? 'input-error' : 'input-bordered'"
              :placeholder="`选项 ${String.fromCharCode(65 + idx)}`"
            />
            <div
              v-if="options.filter((o, i) => i !== idx && o.trim() && o.trim() === opt.trim()).length"
              class="text-xs text-error mt-1"
            >选项不能重复</div>
          </div>
          <label class="label cursor-pointer gap-1 shrink-0">
            <input
              v-model="singleAnswer"
              type="radio"
              :value="opt"
              class="radio radio-sm radio-primary"
              :disabled="!opt.trim()"
            />
            <span class="label-text text-xs">正确答案</span>
          </label>
          <button
            type="button"
            class="btn btn-circle btn-ghost btn-xs text-error shrink-0"
            @click="removeOption(idx)"
          >
            &times;
          </button>
        </div>
        <button
          type="button"
          class="btn btn-ghost btn-sm rounded-lg"
          @click="addOption"
        >
          + 添加选项
        </button>
      </div>
      <div v-if="!singleAnswer" class="text-xs text-warning">
        请选择一个正确答案
      </div>
    </template>

    <!-- Multi Choice -->
    <template v-if="type === 'MULTI_CHOICE'">
      <div class="space-y-2">
        <label class="label-text text-sm">选项（至少2项）</label>
        <div
          v-for="(opt, idx) in options"
          :key="idx"
          class="flex items-center gap-2"
        >
          <div class="flex-1">
            <input
              v-model="options[idx]"
              type="text"
              class="input flex-1 rounded-lg w-full"
              :class="options.filter((o, i) => i !== idx && o.trim() && o.trim() === opt.trim()).length ? 'input-error' : 'input-bordered'"
              :placeholder="`选项 ${String.fromCharCode(65 + idx)}`"
            />
            <div
              v-if="options.filter((o, i) => i !== idx && o.trim() && o.trim() === opt.trim()).length"
              class="text-xs text-error mt-1"
            >选项不能重复</div>
          </div>
          <label class="label cursor-pointer gap-1 shrink-0">
            <input
              v-model="multiAnswers"
              type="checkbox"
              :value="opt"
              class="checkbox checkbox-sm checkbox-primary"
              :disabled="!opt.trim()"
            />
            <span class="label-text text-xs">正确答案</span>
          </label>
          <button
            type="button"
            class="btn btn-circle btn-ghost btn-xs text-error shrink-0"
            @click="removeOption(idx)"
          >
            &times;
          </button>
        </div>
        <button
          type="button"
          class="btn btn-ghost btn-sm rounded-lg"
          @click="addOption"
        >
          + 添加选项
        </button>
      </div>
      <div v-if="multiAnswers.length === 0" class="text-xs text-warning">
        请至少选择一个正确答案
      </div>
    </template>

    <!-- True/False -->
    <template v-if="type === 'TRUE_FALSE'">
      <div class="form-control">
        <label class="label"
          ><span class="label-text text-sm">正确答案</span></label
        >
        <div class="flex gap-4">
          <label class="label cursor-pointer gap-2">
            <input
              v-model="tfAnswer"
              type="radio"
              :value="true"
              class="radio radio-primary"
            />
            <span class="label-text">正确</span>
          </label>
          <label class="label cursor-pointer gap-2">
            <input
              v-model="tfAnswer"
              type="radio"
              :value="false"
              class="radio radio-primary"
            />
            <span class="label-text">错误</span>
          </label>
        </div>
      </div>
    </template>

    <!-- Fill Blank -->
    <template v-if="type === 'FILL_BLANK'">
      <div class="space-y-3">
        <label class="label-text text-sm">填空位置（至少1个）</label>
        <div
          v-for="(blank, bIdx) in blanks"
          :key="bIdx"
          class="surface p-4 space-y-2"
        >
          <div class="flex items-center justify-between">
            <span class="text-sm font-medium">填空 {{ bIdx + 1 }}</span>
            <button
              type="button"
              class="btn btn-circle btn-ghost btn-xs text-error"
              @click="removeBlank(bIdx)"
            >
              &times;
            </button>
          </div>
          <div class="grid grid-cols-2 gap-3 items-center">
            <div class="form-control">
              <label class="label"
                ><span class="label-text text-xs">匹配模式</span></label
              >
              <select
                v-model="blank.matchMode"
                class="select select-bordered select-sm rounded-lg w-full"
              >
                <option value="EXACT">精确匹配</option>
                <option value="CONTAINS">包含匹配</option>
              </select>
            </div>
            <div class="form-control">
              <label class="label"
                ><span class="label-text text-xs">忽略大小写</span></label
              >
              <br />
              <label class="label cursor-pointer gap-2">
                <input
                  v-model="blank.ignoreCase"
                  type="checkbox"
                  class="toggle toggle-sm toggle-primary"
                />
                <span class="label-text text-xs">{{
                  blank.ignoreCase ? "是" : "否"
                }}</span>
              </label>
            </div>
          </div>
          <div class="space-y-1">
            <label class="label-text text-xs">可接受答案（至少1个）</label>
            <div
              v-for="(ans, aIdx) in blank.answers"
              :key="aIdx"
              class="flex items-center gap-2"
            >
              <input
                v-model="ans.text"
                type="text"
                class="input input-bordered input-sm flex-1 rounded-lg"
                placeholder="答案文本"
              />
              <input
                v-model.number="ans.maxLength"
                type="number"
                min="1"
                class="input input-bordered input-sm w-24 rounded-lg"
                placeholder="最大字数"
              />
              <button
                type="button"
                class="btn btn-circle btn-ghost btn-xs text-error"
                @click="removeBlankAnswer(bIdx, aIdx)"
              >
                &times;
              </button>
            </div>
            <button
              type="button"
              class="btn btn-ghost btn-xs rounded-lg"
              @click="addBlankAnswer(bIdx)"
            >
              + 添加答案
            </button>
          </div>
        </div>
        <button
          type="button"
          class="btn btn-ghost btn-sm rounded-lg"
          @click="addBlank"
        >
          + 添加填空
        </button>
      </div>
    </template>

    <!-- Subjective -->
    <template v-if="type === 'SUBJECTIVE'">
      <div class="form-control">
        <label class="label"
          ><span class="label-text text-sm">最大字数</span></label
        >
        <input
          v-model.number="maxLength"
          type="number"
          min="1"
          class="input input-bordered w-full rounded-lg"
          placeholder="请输入最大字数限制"
        />
      </div>
      <div class="form-control">
        <label class="label"
          ><span class="label-text text-sm">参考答案</span></label
        >
        <textarea
          v-model="referenceAnswer"
          class="textarea textarea-bordered w-full rounded-lg"
          rows="3"
          placeholder="请输入参考答案"
        />
      </div>
      <div class="form-control">
        <label class="label"
          ><span class="label-text text-sm">评分标准</span></label
        >
        <textarea
          v-model="scoringGuide"
          class="textarea textarea-bordered w-full rounded-lg"
          rows="2"
          placeholder="请输入评分标准"
        />
      </div>
    </template>

    <!-- Analysis (common) -->
    <div class="form-control">
      <label class="label"
        ><span class="label-text text-sm">解析（选填）</span></label
      >
      <textarea
        v-model="analysis"
        class="textarea textarea-bordered w-full rounded-lg"
        rows="2"
        placeholder="请输入题目解析"
      />
    </div>
  </form>
</template>
