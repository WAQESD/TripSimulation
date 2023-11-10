import { defineStore } from "pinia";
import { ref } from "vue";

export const useModalStore = defineStore("modal", () => {
  const isActive = ref(false);
  const content = ref(null);

  function setModal(state, component = null) {
    isActive.value = state;
    content.value = component;
  }

  return { isActive, content, setModal };
});
