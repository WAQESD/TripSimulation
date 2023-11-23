import { defineStore } from "pinia";
import { ref } from "vue";

export const useUserStore = defineStore("user", () => {
  const userInfo = ref(null);
  const accessToken = ref(null);
  const socialType = ref(null);

  const setUserInfo = (newUserInfo) => {
    userInfo.value = newUserInfo;
    console.log(userInfo.value);
  };

  return { userInfo, accessToken, socialType, setUserInfo };
});
