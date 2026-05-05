<script setup lang="ts">
import { useTranslation } from 'i18next-vue'
import { computed, ref } from 'vue'

// Agent 授权管理已遮蔽，如需恢复请取消以下注释
// import AgentKeyManage from './AgentKeyManage.vue'
import ApiKeyManage from './ApiKeyManage.vue'

const activeKey = ref('apiKey')
const { t } = useTranslation()

const options = computed(() => ([
  {
    label: t('settingCenter.apiKeyManage.rpaAuthManage'),
    value: 'apiKey',
  },
  // Agent 授权管理 Tab 已遮蔽
  // {
  //   label: t('settingCenter.apiKeyManage.agentAuthManage'),
  //   value: 'starAgent',
  // },
]))
</script>

<template>
  <div class="flex flex-col h-full overflow-hidden">
    <div class="mb-3">
      <a-segmented v-model:value="activeKey" :options="options" />
    </div>
    <div class="flex-1">
      <ApiKeyManage v-show="activeKey === 'apiKey'" />
      <!-- Agent 授权管理已遮蔽 -->
      <!-- <AgentKeyManage v-show="activeKey === 'starAgent'" /> -->
    </div>
  </div>
</template>
